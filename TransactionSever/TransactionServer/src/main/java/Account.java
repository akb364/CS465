public class Account 
{
    private int balance;
    private final int accountNum;
    
    public Account(int num, int initialBalance)
    {
        this.balance = initialBalance;
        this.accountNum = num;
    }

    public int getBalance()
    {
        return balance;
    }

    public void setBalance(int newBalance)
    {
        balance = newBalance;
    }

}
