import java.util.ArrayList;

public class Transaction 
{
    
    public int transactionID;
    public ArrayList<Lock> locks;
    
    public Transaction(int transactionID)
    {
        this.transactionID = transactionID;
        this.locks = new ArrayList<>();
    }

    public int readAccount(int accountNum)
    {
        return AccountManager.getInstance().read(accountNum, this);
    }

    public void writeToAccount(int accountNum, int amount)
    {
        AccountManager.getInstance().write(accountNum, this ,amount);
    }
    
    public void addLock(Lock lock) 
    {
        locks.add(lock);
    }
    
    
}
