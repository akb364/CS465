import java.io.*;

public class MsgWriteRequest implements Serializable
{
    public int valToAdd, accountNumber;

    public MsgWriteRequest(int valToAdd, int accountNumber)
    {
        this.valToAdd = valToAdd;
        this.accountNumber = accountNumber;
    }
}