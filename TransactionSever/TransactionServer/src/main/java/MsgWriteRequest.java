import java.io.*;

public class MsgWriteRequest implements Serializable
{
    public int valToAdd, accountNumber;

    public MsgWriteRequest(int accountNumber, int valToAdd)
    {
        this.valToAdd = valToAdd;
        this.accountNumber = accountNumber;
    }
}