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
        System.out.println("Transaction" + trans + "Is looking for a lock for account" + acc);

        synchronized(this) 
        {
            for(int i = 0; i < lockList.size(); i++)
            {
                if(lockList.get(i).lockedAccount.equals(acc))
                {
                   foundLock = lockList.get(i); 
                   System.out.println("Transaction" + trans + "found a lock for account" + acc);

                }
            }
            if(foundLock == null)
            {
                foundLock = new Lock(acc);
                lockList.add(foundLock);
                System.out.println("Transaction" + trans + "did not find a lock for account" + acc + "creating a lock of lock type" + lockType);

            }
        }
        foundLock.acquire(trans, lockType);
    }
    public synchronized void unLock(Transaction trans)
    {
        System.out.println("Transaction" + transactionID + "unlocking held locks");

        for(int i = 0; i < lockList.size(); i++)
        {
             if(lockList.get(i).holders.contains(trans))
            {
                System.out.println("Transaction" + trans + "unlocked lock on account" + acc);
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
