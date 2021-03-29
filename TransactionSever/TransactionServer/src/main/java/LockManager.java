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
        
        synchronized(this) 
        {
            for(int i = 0; i < lockList.size(); i++)
            {
                if(lockList.get(i).lockedAccount.equals(acc))
                {
                   foundLock = lockList.get(i); 
                }
            }
            if(foundLock == null)
            {
                foundLock = new Lock(acc);
                lockList.add(foundLock);
            }
        }
        foundLock.acquire(trans, lockType);
    }
    public synchronized void unLock(Transaction trans)
    {
        for(int i = 0; i < lockList.size(); i++)
        {
             if(lockList.get(i).holders.contains(trans))
            {
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
