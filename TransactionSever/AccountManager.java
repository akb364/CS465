import java.util.ArrayList;
import transaction.server.transaction.Transaction;
import transaction.server.TransactionServer;
import transaction.server.lock.LockTypes;

public class AccountManager implements LockTypes
{
    
    private static ArrayList<Account> accounts;
    static int numberAccounts;
    static int initialBalance; 
    private lockManager = new LockManager();

    public AccountManager(int numberAccounts, int initialBalance)
    {
        accounts = new ArrayList();
        AccountManager.numberAccounts = numberAccounts;
        AccountManager.initialBalance = initialBalance;
        int accountIndex;

        for(accountIndex = 0; accountIndex < numberAccounts; accountIndex++)
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
        Account account = getAccount(accountNumber);

        // set read lock and wait until lock is free
        (TransactionServer.lockManager).lock(account, transaction, READ_LOCK);

        // return when lock is released
        return (getAccount(accountNumber)).getBalance();
    }

    public void write(int accountNum, Transaction transaction, int balance)
    {
        // get account
        Account account = getAccount(accountNumber);

        // set write lock and wait until lock is free
        (TransactionServer.lockManager).lock(account, transaction, WRITE_LOCK);
        
        // write amount
        account.setBalance(balance);
    }

}
