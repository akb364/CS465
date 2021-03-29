import java.util.*;

public class LockManager {

    private ArrayList<Lock> theLocks = new ArrayList<Lock>();
    private static LockManager INSTANCE;

    public void setLock(Account acc, Transaction trans, LockType lockType)
    {
        Lock foundLock = null;
        
        synchronized(this) 
        {
            for(int i = 0; i < theLocks.size(); i++)
            {
                if(theLocks.get(i).lockedAccount.equals(acc))
                {
                   foundLock = theLocks.get(i); 
                }
            }
            if(foundLock == null)
            {
                foundLock = new Lock(acc);
                theLocks.add(foundLock);
            }
        }
        foundLock.acquire(trans, lockType);
    }
    public synchronized void unLock(Transaction trans)
    {
            for(int i = 0; i < theLocks.size(); i++)
            {
                if(theLocks.get(i).holders.contains(trans))
                {
                   theLocks.get(i).release(trans);
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
