public class Account 
{
    private int balance;
    public final int accountNum;
    
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

    @Override
    public boolean equals(Object other) {
        return other instanceof Account && ((Account) other).accountNum == this.accountNum;
    }
    
}
