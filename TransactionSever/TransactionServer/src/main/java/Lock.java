import java.util.*;


public class Lock 
{  
    public Account lockedAccount;
    public List<Transaction> holders;
    public LockType lockType;

    public Lock(Account acc) {
        // Tie the account to the lock
        this.lockedAccount = acc;
        this.lockType = null;
        this.holders = Collections.synchronizedList(new ArrayList<>());
    }
    
    public synchronized void acquire(Transaction trans, LockType lockType)
    {
        while(!(holders.isEmpty() || 
                (holders.size() == 1 && holders.contains(trans)) ||
                (lockType == LockType.READ_LOCK && this.lockType == LockType.READ_LOCK)))
        {
            try 
            {
                System.out.println("Transaction" + trans + "waiting for lockType of" + lockType + "for account" + acc);
                wait();
            } 
            catch (InterruptedException e) 
            {
            }        
        }
        
        //holders.add(trans);
        //this.lockType = lockType;
        if(holders.isEmpty() || lockType == LockType.READ_LOCK && this.lockType == LockType.READ_LOCK)
        {
            System.out.println("Transaction" + transactionID + "No holder for this account" + acc + "setting lock to locktype of" + lockType);
            holders.add(trans);
            this.lockType = lockType;
        }
        else if (this.holders.size() == 1 && holders.contains(trans))
        {
            System.out.println("Transaction" + transactionID + "Promoting lock on account" + acc);
            this.lockType = LockType.WRITE_LOCK;
        }
        
    }

    public synchronized void release(Transaction trans) 
    {
        if (holders.contains(trans)) 
        {
            holders.remove(trans);
            // Notify every transaction thread waiting on this lock.
            notifyAll();
        }
    }

}
