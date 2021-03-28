import java.io.*;

public class MsgCloseTransaction implements Serializable
{
    public int transactionID;

    public MsgCloseTransaction(int transactionID)
    {
        this.transactionID = transactionID;
    }
}