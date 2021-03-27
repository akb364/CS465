import java.util.ArrayList;

public class TransactionManager 
{
    private static int transactionCounter = 0;
    private static TransactionManager INSTANCE;
    private static final ArrayList<Transaction> transactions = new ArrayList<Transaction>();


    public TransactionManager()
    {

    }

    public ArrayList<transaction> getTransactions()
    {
      return transactions;
    }

    public void runTransaction(Socket client)
    {
      (new TransactionManagerWorker(client)).start();
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
