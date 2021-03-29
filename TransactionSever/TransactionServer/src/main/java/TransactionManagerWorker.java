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
            System.out.println("[TransactionWorker.run] failed to open object stream");
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void run()
    {
        while(keepgoing)
        {
            //reading msg
            try{
                message = readFromNet.readObject();
            } catch(IOException | ClassNotFoundException e){
                System.out.println("[TransactionWorker.run] Message could not be read from object stream.");
                System.exit(1);
            }

            if(message instanceof MsgOpenTransaction)
            {
                
                transaction = new Transaction(TransactionManager.getInstance().transactionCounter++);
                TransactionManager.getInstance().transactions.add(transaction); 

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

            if(message instanceof MsgCloseTransaction)
            {
                LockManager.getInstance().unLock(transaction);
                 TransactionManager.getInstance().transactions.remove(transaction);
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

                //transaction.log("[TransactionWorker.run] CLOSE_TRANSACTION #" + transaction.transactionID);

            }

            if(message instanceof MsgReadRequest)
            {
                MsgReadRequest msg = (MsgReadRequest) message;
                accountNumber = msg.accountNumber;
                //transaction.log("[TransactionWorker.run] READ_REQUEST >>>>>>>>>>>> account #: " + accountNumber);
                balance = AccountManager.getInstance().read(accountNumber, transaction);

                try
                {
                    writeToNet.writeObject((Integer) balance);
                } 
                catch (IOException e) 
                {
                    System.out.println("[TransactionWorker.run] READ_REQUEST - Error whent writing to object stream");
                }

                //transaction.log("[TransactionWorker.run] READ_REQUEST <<<<<<<<<<<< account #: " + accountNumber + " balance: " + balance);
            }

            if(message instanceof MsgReadRequest)
            {
                MsgWriteRequest msg = (MsgWriteRequest) message;
                accountNumber = msg.accountNumber;
                balance = msg.valToAdd;
                //transaction.log("[TransactionWorker.run] WRITE_REQUEST >>>>>>>>>>>> account #: " + accountNumber);

                balance = AccountManager.getInstance().write(accountNumber,transaction,balance);
                try
                {
                    writeToNet.writeObject(balance);
                } 
                catch (IOException e)
                {
                    System.out.println("[TransactionWorker.run] WRITE_REQUEST - Error whent writing");
                }

                //transaction.log("[TransactionWorker.run] WRITE_REQUEST <<<<<<<<<<<< account #: " + accountNumber);
            }

        }
    }

}
        
