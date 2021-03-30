import java.util.*;

public class LockManager {

    private ArrayList<Lock> lockList;
    private static LockManager INSTANCE;
    
    
    public LockManager()
    {
        lockList = new ArrayList<Lock>();
    }

    public void setLock(Account acc, Transaction trans, LockType lockType)
    {
        
        Lock foundLock = null;
        System.out.println("Transaction " + trans.transactionID + " is looking for a lock for account " + acc.accountNum);

        synchronized(this) 
        {
            for(int i = 0; i < lockList.size(); i++)
            {
                if(lockList.get(i).lockedAccount.equals(acc))
                {
                   foundLock = lockList.get(i); 
                   System.out.println("Transaction " + trans.transactionID + " found a lock for account " + acc.accountNum);

                }
            }
            if(foundLock == null)
            {
                foundLock = new Lock(acc);
                lockList.add(foundLock);
                System.out.println("Transaction " + trans.transactionID + " did not find a lock for account " + acc.accountNum + " creating a lock of lock type " + lockType);

            }
        }
        foundLock.acquire(trans, lockType);
    }
    public synchronized void unLock(Transaction trans)
    {
        System.out.println("Transaction " + trans.transactionID + " unlocking held locks");

        for(int i = 0; i < lockList.size(); i++)
        {
             if(lockList.get(i).holders.contains(trans))
            {
                System.out.println("Transaction " + trans.transactionID + " unlocked lock on account " + lockList.get(i).lockedAccount.accountNum);
                lockList.get(i).release(trans);
            }
        }
    }

    public static LockManager getInstance()
    {
        if(INSTANCE == null)
        {
            INSTANCE = new LockManager();
        }

        return INSTANCE;
    }
}
