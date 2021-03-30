import java.util.ArrayList;
import java.net.*;

public class TransactionManager 
{
    public int transactionCounter = 0;
    private static TransactionManager INSTANCE;
    public ArrayList<Transaction> transactions;

    public TransactionManager()
    {
        transactions = new ArrayList<Transaction>();
    }

    public void runTransaction(Socket client)
    {
      new TransactionManagerWorker(client).start();
    }

    public static TransactionManager getInstance()
    {
        if(INSTANCE == null)
        {
            INSTANCE = new TransactionManager();
        }

        return INSTANCE;
    }
}
