import java.util.Enumeration;
import java.util.Hashtable;

public class LockManager {

    private Hashtable theLocks;

    public void setLock(Object object, Trans trans, LockType lockType)
    {
        Lock foundLock;
        synchronized(this) {
            // find the lock associated with object
            // if there isnt one, create it and add to the hashtable
        }
        foundLock.aquire(trans, lockType);
    }
    // synchronize this one because we want to remove all entries

    public synchronized void unLock(TransID trans)
    {
        Enumeration e = theLocks.elements();
        while(e.hasMoreElements()) {
            Lock aLock = (Lock) (e.nextElement());
            if( /* trans is a holder of this lock*/) {
                aLock.release(trans);
            }
        }
    }

}
