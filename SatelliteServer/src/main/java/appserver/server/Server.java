package appserver.server;

import appserver.comm.Message;
import static appserver.comm.MessageTypes.JOB_REQUEST;
import static appserver.comm.MessageTypes.REGISTER_SATELLITE;
import appserver.comm.ConnectivityInfo;
import appserver.satellite.HTTPClassLoader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import utils.PropertyHandler;

/**
 *
 * @author Dr.-Ing. Wolf-Dieter Otte
 */
public class Server {

    // Singleton objects - there is only one of them. For simplicity, this is not enforced though ...
    static SatelliteManager satelliteManager = null;
    static LoadManager loadManager = null;
    static ServerSocket serverSocket = null;
    private String ip;
    private int port;
    
    public Server(String serverPropertiesFile) {

        // create satellite manager and load manager
        satelliteManager = new SatelliteManager();
        System.out.println("[Server.Server]Satellite Manager created.");
        loadManager = new LoadManager();
        System.out.println("[Server.Server]Load Manager created.");
        
        // read server properties and create server socket
        try (InputStream input = new FileInputStream(serverPropertiesFile))
        {
            
            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            // get the property values
            this.ip = prop.getProperty("HOST");
            this.port = Integer.parseInt(prop.getProperty("PORT"));
            this.serverSocket = new ServerSocket(this.port, 50, InetAddress.getByName(this.ip));
            System.out.println("[Server.Server]Server Socket created.");

        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    public void run() {
    // serve clients in server loop ...
    // when a request comes in, a ServerThread object is spawned
    while(true)
        {
            try
            {
                new ServerThread(serverSocket.accept()).start();
            }
            catch(Exception e)
            {
                System.out.println(e.toString());
            }
        }
    }

    // objects of this helper class communicate with satellites or clients
    private class ServerThread extends Thread {

        Socket client = null;
        ObjectInputStream readFromNet = null;
        ObjectOutputStream writeToNet = null;
        Message message = null;

        private ServerThread(Socket client) {
           
            this.client = client;
        }

        @Override
        public void run() {
            // set up object streams and read message
            try
            {
                readFromNet = new ObjectInputStream(client.getInputStream());
                writeToNet = new ObjectOutputStream(client.getOutputStream());
            }
            catch(Exception e)
            {
                System.out.println(e.toString());
            }

            // reading message
            try
            {
                message = (Message)readFromNet.readObject();
            }
            catch(Exception e)
            {
                System.out.println(e.toString());
            }
            
            // process message
            switch (message.getType()) {
                case REGISTER_SATELLITE:
                    
                    // register satellite
                    synchronized (Server.satelliteManager) {
                       Server.satelliteManager.registerSatellite((ConnectivityInfo)message.getContent());
                    }

                    // add satellite to loadManager
                    synchronized (Server.loadManager) {
                        ConnectivityInfo obj = (ConnectivityInfo)message.getContent();
                        Server.loadManager.satelliteAdded(obj.getName());
                        System.out.println("[Server.Server]REGISTER_SATELLITE");
                        System.out.println("Satellite IP: " + obj.getHost());
                        System.out.println("Satellite port: " + obj.getPort());
                        System.out.println("Satellite name: " + obj.getName());
                    }
                    
                    break;

                case JOB_REQUEST:
                    System.err.println("\n[ServerThread.run] Received job request");

                    String satelliteName = null;
                    Socket satelliteSock = null;
                    ConnectivityInfo satellite;
                    synchronized (Server.loadManager) 
                    {
                        // get next satellite from load manager
                        try
                        {
                          satelliteName = Server.loadManager.nextSatellite();
                        }
                        catch(Exception e)
                        {
                            System.out.println(e);
                        }
                        
                        // get connectivity info for next satellite from satellite manager
                        satellite = Server.satelliteManager.getSatelliteForName(satelliteName);
                    }

                    
                    // connect to satellite
                    

                    // open object streams,
                    // forward message (as is) to satellite,
                    // receive result from satellite and
                    // write result back to client
                    try
                    {
                        satelliteSock = new Socket(InetAddress.getByName(satellite.getHost()),satellite.getPort());
                        ObjectOutputStream writeToSat = new ObjectOutputStream(satelliteSock.getOutputStream());
                        ObjectInputStream readFromSat = new ObjectInputStream(satelliteSock.getInputStream());
                        
                        
                        writeToSat.writeObject(message);
                        //Integer result = (Integer) readFromNet.readObject();
                        //System.out.println(result.toString());
                        
                        
                        //writeToNet = new ObjectOutputStream(client.getOutputStream());
                        writeToNet.writeObject(readFromSat.readObject());
                        
                        
                    }
                    catch(Exception e)
                    {
                        System.out.println(e.toString());
                    }

                    break;

                default:
                    System.err.println("[ServerThread.run] Warning: Message type not implemented");
            }
        }
    }

    // main()
    public static void main(String[] args) {
        // start the application server
        Server server = null;
        if(args.length == 1) {
            server = new Server(args[0]);
        } else {
            server = new Server("../../config/Server.properties");
        }
        server.run();
    }
}
