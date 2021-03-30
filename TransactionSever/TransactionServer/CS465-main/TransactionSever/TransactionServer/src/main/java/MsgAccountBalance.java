import java.io.*;

public class MsgAccountBalance implements Serializable
{
    public int accountBalance;

    public MsgAccountBalance(int accountBalance)
    {
        this.accountBalance = accountBalance;
    }
}