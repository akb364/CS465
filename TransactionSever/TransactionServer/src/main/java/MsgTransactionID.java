import java.io.*;

public class MsgTransactionID implements Serializable
{
    public int transactionID;

    public MsgTransactionID(int transactionID)
    {
        this.transactionID = transactionID;
    }
}