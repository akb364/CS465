import java.util.ArrayList;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TransactionManagerWorker extends Thread 
{
    private ObjectInputStream readFromNet;
    private ObjectOutputStream writeToNet;
    private Socket client;

    Transaction transaction = null;
    int accountNumber = 0;
    int balance = 0;
    Object message = null;
    boolean keepgoing = true;
    AccountManager accManager;
    TransactionManager transManager;
    

    public TransactionManagerWorker(Socket client)
    {
        this.client = client;
        try
        {
            readFromNet = new ObjectInputStream(client.getInputStream());
            writeToNet = new ObjectOutputStream(client.getOutputStream());
        } 
        catch (IOException e) 
        {
            System.out.println("[TransactionWorker.run] " + e.toString());
            
            System.exit(1);
        }
        accManager = AccountManager.getInstance();
        transManager = TransactionManager.getInstance();
    }

    @Override
    public void run()
    {
        while(keepgoing)
        {
            //reading msg
            try
            {
                message = readFromNet.readObject();
            } 
            catch(IOException | ClassNotFoundException e)
            {
                System.out.println("[TransactionWorker.run] Message could not be read from object stream.");
                System.exit(1);
            }

            if(message instanceof MsgOpenTransaction)
            {
                
                transaction = new Transaction(TransactionManager.getInstance().transactionCounter++);
                transManager.transactions.add(transaction); 

                try 
                {
                    writeToNet.writeObject(new MsgTransactionID(transaction.transactionID));
                } 
                catch (IOException e) 
                {
                    System.out.println("[TransactionWorker.run] OPEN_TRANSACTION - Error when writing transactionID");
                }
                System.out.println("[TransactionWorker.run] OPEN_TRANSACTION #" + transaction.transactionID);
            }

            else if(message instanceof MsgCloseTransaction)
            {
                LockManager.getInstance().unLock(transaction);
                transManager.transactions.remove(transaction);
                try
                {
                    readFromNet.close();
                    writeToNet.close();
                    client.close();
                    keepgoing = false;
                } 
                catch(IOException e)
                {
                    System.out.println("[TransactionWorker.run] CLOSE_TRANSACTION - Error when closing connectiont to client");
                }

                System.out.println("[TransactionWorker.run] CLOSE_TRANSACTION #" + transaction.transactionID);

            }

            else if(message instanceof MsgReadRequest)
            {
                MsgReadRequest msg = (MsgReadRequest) message;
                accountNumber = msg.accountNumber;
                
                System.out.println("[TransactionWorker.run] READ_REQUEST  >>>>>>>>>>>> account #: " + accountNumber + " balance: " + balance);
                
                balance = accManager.read(accountNumber, transaction);
                try
                {
                    writeToNet.writeObject(new MsgAccountBalance(balance));
                } 
                catch (IOException e) 
                {
                    System.out.println("[TransactionWorker.run] READ_REQUEST - Error whent writing to object stream");
                }

                System.out.println("[TransactionWorker.run] READ_REQUEST  <<<<<<<<<<<< account #: " + accountNumber + " balance: " + balance);
            }

            else if(message instanceof MsgWriteRequest)
            {
                MsgWriteRequest msg = (MsgWriteRequest) message;
                accountNumber = msg.accountNumber;
                balance = msg.valToAdd;
                
                System.out.println("[TransactionWorker.run] WRITE_REQUEST >>>>>>>>>>>> account #: " + accountNumber);

                accManager.write(accountNumber,transaction,balance);

                System.out.println("[TransactionWorker.run] WRITE_REQUEST <<<<<<<<<<<< account #: " + accountNumber);
            }
            else
            {
                System.out.println("?????????????????");
            }

        }
    }

}
        
