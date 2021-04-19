/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appserver.client;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.Thread;
import java.util.Properties;
import java.net.InetAddress;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import appserver.comm.Message;
import static appserver.comm.MessageTypes.JOB_REQUEST;
import static appserver.comm.MessageTypes.REGISTER_SATELLITE;
import appserver.job.Job;


/**
 *
 * @author Kevyn
 */
public class FibonacciClient extends Thread
{
    private String ip;
    private int port;
    
    public FibonacciClient(String serverProperties, int fibNum)
    {
        try (InputStream input = new FileInputStream(serverProperties)) {

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            // get the property values
            this.ip = prop.getProperty("HOST");
            this.port = Integer.parseInt(prop.getProperty("PORT"));

            Socket server = new Socket(InetAddress.getByName(ip), port);

            ObjectOutputStream writeToNet = new ObjectOutputStream(server.getOutputStream());
            ObjectInputStream readFromNet = new ObjectInputStream(server.getInputStream());

            writeToNet.writeObject(new Message(JOB_REQUEST, new Job("appserver.job.impl.Fibonacci", fibNum)));

            Integer result = (Integer)readFromNet.readObject();
            System.out.println(result.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        
        
    }
    
    public static void main(String[] args)
    {
        for(int i = 0; i<47; i++ )
        {
            new FibonacciClient("../../config/Server.properties", i).start();
        }
    }
}
