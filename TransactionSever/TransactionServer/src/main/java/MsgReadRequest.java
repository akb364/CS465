import java.io.*;

public class MsgReadRequest implements Serializable
{
    public int accountNumber;
    public int transID;

    public MsgReadRequest(int accountNumber)
    {
        this.accountNumber = accountNumber;
    }
}