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
           
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void main(String[] args)
    {
        for(int i = 48; i > 0; i-- )
        {
            (new FibonacciClient("../../config/Server.properties", i)).start();
        }
    }
}
