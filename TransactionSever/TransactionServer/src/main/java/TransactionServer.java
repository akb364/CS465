import java.net.*;
import java.io.*;
import java.util.*;
import java.lang.*;

public class TransactionServer extends Thread implements Serializable
{
    private ServerSocket serverSock;
    private InetAddress ip;
    private int port, numAccounts, initialBalance;
    private Boolean applyLocking, transactionView;

    // constructor
    public TransactionServer()
    {
        try (InputStream input = new FileInputStream("TransactionServer.properties")) 
        {

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            // get the property values
            String strIP = prop.getProperty("HOST");
            this.ip = InetAddress.getByName(strIP);
            this.port = Integer.parseInt(prop.getProperty("PORT"));
            this.numAccounts = Integer.parseInt(prop.getProperty("NUMBER_ACCOUNTS"));
            this.initialBalance = Integer.parseInt(prop.getProperty("INITIAL_BALANCE"));
            this.applyLocking = Boolean.parseBoolean(prop.getProperty("APPLY_LOCKING"));
            this.transactionView = Boolean.parseBoolean(prop.getProperty("TRANSACTION_VIEW"));

            serverSock = new ServerSocket(port, 50, ip);

            System.out.println("Server listening with ip=" + 
                               strIP + " and port=" + prop.getProperty("PORT"));

        } 
        catch (IOException ex) 
        {
            ex.printStackTrace();
        }

        // NEED TO INITIALIZE ACCOUNTS HERE
    }

    @Override
    public void run()
    {
        // server loop
        while(true)
        {
            try
            {
                TransactionManager.getInstance().runTransaction(serverSock.accept());
            }
            catch (IOException ex)
            {
                System.out.println(ex.toString());
            }
        }
    }

    public static void main(String[] args)
    {
        new TransactionServer().start();
    }
}