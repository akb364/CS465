import java.io.*;

public class MsgReadRequest implements Serializable
{
    public int accountNumber;

    public MsgReadRequest(int accountNumber)
    {
        this.acccountNumber = accountNumber;
    }
}