package appserver.satellite;

import appserver.job.Job;
import appserver.comm.ConnectivityInfo;
import appserver.job.UnknownToolException;
import appserver.comm.Message;
import static appserver.comm.MessageTypes.JOB_REQUEST;
import static appserver.comm.MessageTypes.REGISTER_SATELLITE;
import appserver.job.Tool;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.PropertyHandler;
import java.net.*;
import java.io.*;
import java.util.*;
import java.lang.*;

/**
 * Class [Satellite] Instances of this class represent computing nodes that execute jobs by
 * calling the callback method of tool a implementation, loading the tool's code dynamically over a network
 * or locally from the cache, if a tool got executed before.
 *
 * @author Dr.-Ing. Wolf-Dieter Otte
 */
public class Satellite extends Thread {

    private ServerSocket serverSock;
    private ConnectivityInfo satelliteInfo = new ConnectivityInfo();
    private ConnectivityInfo serverInfo = new ConnectivityInfo();
    private HTTPClassLoader classLoader = null;
    private Hashtable toolsCache = null;
    private int port;
    private InetAddress host;

    public Satellite(String satellitePropertiesFile, String classLoaderPropertiesFile, String serverPropertiesFile) {

        // read this satellite's properties and populate satelliteInfo object,
        // which later on will be sent to the server
        // ...

        try (InputStream input = new FileInputStream(satellitePropertiesFile))
        {

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            // get the property values
            satelliteInfo.setPort(Integer.parseInt(prop.getProperty("PORT")));
            satelliteInfo.setName(prop.getProperty("NAME"));

        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

        // read properties of the application server and populate serverInfo object
        // other than satellites, the as doesn't have a human-readable name, so leave it out
        // ...

        try (InputStream input = new FileInputStream(serverPropertiesFile))
        {

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            // get the property values
            serverInfo.setHost(prop.getProperty("HOST"));
            serverInfo.setPort(Integer.parseInt(prop.getProperty("PORT")));


        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

        // read properties of the code server and create class loader
        // -------------------
        // ...

        try (InputStream input = new FileInputStream(classLoaderPropertiesFile))
        {

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            // get the property values
            classLoader = new HTTPClassLoader(prop.getProperty("HOST"), Integer.parseInt(prop.getProperty("PORT")));
            classLoader.classRootDir = prop.getProperty("DOC_ROOT");


        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

        // create tools cache
        // -------------------
        // ...


         try (InputStream input = new FileInputStream("../../../../../config/Server.properties"))
        {

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            // get the property values
            String strIP = prop.getProperty("HOST");
            this.host = InetAddress.getByName(strIP);
            this.port = Integer.parseInt(prop.getProperty("PORT"));



            System.out.println("Server listening with ip=" +
                               strIP + " and port=" + prop.getProperty("PORT"));

        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

    }

    @Override
    public void run() {

        // register this satellite with the SatelliteManager on the server
        // ---------------------------------------------------------------
        // ...IGNORE


        // create server socket
        // ---------------------------------------------------------------
        // ...
        try
        {
            this.serverSock = new ServerSocket(port, 50, host);
        }
        catch(Exception e)
        {
             System.out.println(e.toString());
        }


        // start taking job requests in a server loop
        // ---------------------------------------------------------------
        // ...
        while(true)
        {
            try
            {
                new SatelliteThread(serverSock.accept(), this).start();

            }
            catch(Exception e)
            {
                System.out.println(e.toString());
            }
        }
    }

    // inner helper class that is instanciated in above server loop and processes single job requests
    private class SatelliteThread extends Thread {

        Satellite satellite = null;
        Socket jobRequest = null;
        ObjectInputStream readFromNet = null;
        ObjectOutputStream writeToNet = null;
        Message message = null;

        SatelliteThread(Socket jobRequest, Satellite satellite) {
            this.jobRequest = jobRequest;
            this.satellite = satellite;
        }

        @Override
        public void run() {
            // setting up object streams
            // ...

            // reading message
            // ...

            switch (message.getType()) {
                case JOB_REQUEST:
                    // processing job request
                    // ...
                    break;

                default:
                    System.err.println("[SatelliteThread.run] Warning: Message type not implemented");
            }
        }
    }

    /**
     * Aux method to get a tool object, given the fully qualified class string
     * If the tool has been used before, it is returned immediately out of the cache,
     * otherwise it is loaded dynamically
     */
    public Tool getToolObject(String toolClassString) throws UnknownToolException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        Tool toolObject = null;

        // ...

        return toolObject;
    }

    public static void main(String[] args) {
        // start the satellite
        Satellite satellite = new Satellite(args[0], args[1], args[2]);
        satellite.run();
    }
}
