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
    public boolean applyLocking;
    private static AccountManager INSTANCE;

    public AccountManager()
    {
        try (InputStream input = new FileInputStream("TransactionServer.properties")) {

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);
            
            this.numberAccounts = Integer.parseInt(prop.getProperty("NUMBER_ACCOUNTS"));
            this.initialBalance = Integer.parseInt(prop.getProperty("INITIAL_BALANCE"));
            this.applyLocking = Boolean.parseBoolean(prop.getProperty("APPLY_LOCKING"));

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
    public ArrayList<Account> getAccounts()
    {
        return accounts;
    }

    public int read(int accountNum, Transaction transaction)
    {
        // get account
        Account account = accounts.get(accountNum);
        System.out.println("Transaction " + transaction.transactionID + ": Account Manager reading account " + accountNum);

        // set read lock and wait until lock is free
        if(applyLocking)
        {
            LockManager.getInstance().setLock(account, transaction, LockType.READ_LOCK);
        }

        // return when lock is released
        return account.getBalance();
    }

    public int write(int accountNum, Transaction transaction, int balance)
    {
        // get account
        Account account = accounts.get(accountNum);
        System.out.println("Transaction " + transaction.transactionID +": Account Manager writing to account " + accountNum);
        account.setBalance(balance);

        // set write lock and wait until lock is free
        if(applyLocking)
        {
            LockManager.getInstance().setLock(account, transaction, LockType.WRITE_LOCK);
        }

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
    public int branchTotal() {
        int total = 0;

        for (Account currAccount : accounts) 
        {
            total += currAccount.getBalance();
        }
        return total;
    }
}
