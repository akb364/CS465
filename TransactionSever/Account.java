public class Account {

    private int balance;
    private final int accountNum;
    
    public Account(int num, int initialBalance)
    {
        this.balance = initialBalance;
        this.accountNum = num;
    }

    public int getBalance()
    {
        return this.balance;
    }

    public int setBalance(int newBalance)
    {
        this.balance = newBalance;
        return this.balance;
    }

    public void transferRandAmount()
    {
        
    }

}
