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
            System.out.println("Type anything to stop the server loop.");

        } 
        catch (IOException ex) 
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void run()
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        boolean run = true;
        // server loop
        while(run)
        {
            try
            {
                TransactionManager.getInstance().runTransaction(serverSock.accept());
                
                if (br.readLine() != null)
                {
                    run = false;
                }
            }
            catch (IOException ex)
            {
                System.out.println(ex.toString());
            }
        }
        System.out.println("Branch total is: " + AccountManager.getInstance().branchTotal());
    }

    public static void main(String[] args)
    {
        new TransactionServer().start();
    }
}