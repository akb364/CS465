import java.io.*;
import java.util.*;
import java.net.*;

public class TransactionClient extends Thread
{
    public int numTransactions;
    //private TransactionClient self;
    private int port;
    private String ip;

    public TransactionClient()
    {
        try (InputStream input = new FileInputStream("TransactionClient.properties")) {

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            // get the property values
            this.numTransactions = Integer.parseInt(prop.getProperty("NUMBER_TRANSACTIONS"));
            this.ip = prop.getProperty("HOST");
            this.port = Integer.parseInt(prop.getProperty("PORT"));

            //this.self = new TransactionClient();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run()
    {
        TransactionServerProxy serverProxy = new TransactionServerProxy(ip, port);
        int transID = serverProxy.openTransaction();

        // stuff

        serverProxy.closeTransaction(transID);
    }

    public static void main(String[] args)
    {
        TransactionClient self = new TransactionClient();

        for(int i = 0; i < self.numTransactions; i++)
        {
            self.start();
        }
    }
}