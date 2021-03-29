import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

public class AccountManager
{
    private static ArrayList<Account> accounts;
    static int numberAccounts;
    static int initialBalance; 
    private static AccountManager INSTANCE;

    public AccountManager()
    {
        try (InputStream input = new FileInputStream("TransactionServer.properties")) {

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);
            
            this.numberAccounts = Integer.parseInt(prop.getProperty("NUMBER_ACCOUNTS"));
            this.initialBalance = Integer.parseInt(prop.getProperty("INITIAL_BALANCE"));

        } 
        catch (IOException ex) 
        {
            ex.printStackTrace();
        }
        accounts = new ArrayList();
        this.numberAccounts = numberAccounts;
        this.initialBalance = initialBalance;
        
        for(int accountIndex = 0; accountIndex < numberAccounts; accountIndex++)
        {
            accounts.add(accountIndex, new Account(accountIndex, initialBalance));
        }
    }

    public Account getAccount(int accountNumber)
    {
        return accounts.get(accountNumber);
    }

    public ArrayList<Account> getAccounts()
    {
        return accounts;
    }

    public int read(int accountNum, Transaction transaction)
    {
        // get account
        Account account = getAccount(accountNum);

        // set read lock and wait until lock is free
        LockManager.getInstance().setLock(account, transaction, LockType.READ_LOCK);

        // return when lock is released
        return (getAccount(accountNum)).getBalance();
    }

    public int write(int accountNum, Transaction transaction, int balance)
    {
        // get account
        Account account = getAccount(accountNum);

        // set write lock and wait until lock is free
        LockManager.getInstance().setLock(account, transaction, LockType.WRITE_LOCK);
        
        // write amount
        account.setBalance(balance);

        return balance;
    }

    public static AccountManager getInstance()
    {
        if(INSTANCE == null)
        {
            INSTANCE = new AccountManager();
        }

        return INSTANCE;
    }
}
