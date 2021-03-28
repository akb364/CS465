import java.util.ArrayList;

public class Transaction {
    
    public int transactionID;
    private ArrayList<String> logs;
    
    public Transaction(int transactionID)
    {
        this.transactionID = transactionID;
        this.logs = new ArrayList<String>();
    }

    public void openTransaction(int transactionID)
    {

    }

    public int readAccount(int accountNum)
    {
        return 0;
    }

    public void writeToAccount(int accountNum, int amount)
    {

    }  

    public int closeTransaction()
    {
        return 0;
    } 
      
    public int transferRandAmount()
    {
        return 0;
    } 
    
    public void log(String log)
    {
        logs.add(log);
    }
    
    public void printLogs()
    {
        System.out.println(logs);
    }

}
