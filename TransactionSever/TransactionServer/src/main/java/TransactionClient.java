import java.io.*;
import java.util.*;
import java.net.*;
import java.util.concurrent.ThreadLocalRandom;


public class TransactionClient extends Thread
{
    public int numTransactions;
    //private TransactionClient self;
    private int port;
    private String ip;
    static TransactionServerProxy proxy;
    private static int numAccounts;

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
            this.numAccounts = Integer.parseInt(prop.getProperty("NUMBER_ACCOUNTS"));

            //this.self = new TransactionClient();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        proxy = new TransactionServerProxy(this.ip, this.port);

    }

    @Override
    public void run()
    {
        TransactionServerProxy serverProxy = new TransactionServerProxy(ip, port);
        int transID = serverProxy.openTransaction();

        // stuff
        System.out.println("hi");

        serverProxy.closeTransaction(transID);
    }

    public static void main(String[] args)
    {
        TransactionClient self = new TransactionClient();
       /* for(int i = 0; i < self.numTransactions; i++)
        {
          runTransactionTest(numAccounts);
        }*/
       runTransaction();
    }
    
    
    
    public static void runTransaction()
    {
        Integer transactionID = proxy.openTransaction();
        
        System.out.println(proxy.read(0));
        
        proxy.write(0,100);
        System.out.println("hi");
        System.out.println(proxy.read(0));
        
        proxy.closeTransaction(transactionID);
    }
    
    public static void runTransactionTest(int accountCnt) 
    {
        Integer transactionID = proxy.openTransaction();
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        int srcAcct = rand.nextInt(accountCnt);
        int dstAcct = rand.nextInt(accountCnt);
        
        while (srcAcct == dstAcct) 
        {
            dstAcct = rand.nextInt(accountCnt);
        }

        int srcBal = proxy.read(srcAcct);

        int dstBal = proxy.read(dstAcct);

        int withdrawAmt = rand.nextInt(srcBal + 1);
        
        proxy.write(srcAcct, srcBal - withdrawAmt);
       
        // Deposit to dst
        proxy.write(dstAcct, dstBal + withdrawAmt);

        // Close transaction
        proxy.closeTransaction(transactionID);
    }
}