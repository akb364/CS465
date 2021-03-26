import java.util.ArrayList;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import transaction.comm.Message;
import transaction.comm.MessageTypes;
import transaction.server.TransactionServer;


public class TransactionManager implements MessageTypes
{
  private static int transactionCounter = 0;
  private static final ArrayList<Transaction> transactions = new ArrayList<>();

  public ArrayList<transaction> getTransactions()
  {
    return transactions;
  }

  public void runTransaction(Socket client)
  {
    (new TransactionManagerWorker(client)).start();
  }
  public class TransactionManagerWorker extends Thread {

      private TransactionManagerWorker(Socket client)
      {
        this.client = client;
        try{
          readFromNet = new ObjectInputStream(client.getInputStream());
          writeToNet = new ObjectOutputStream(client.getOutputStream());
        } catch (IOException e) {
          System.out.println("[TransactionManagerWorker.run] failed to open object stream");
          e.printStackTrace();
          System.exit(1);
        }
      }


  Transaction transaction = null;
  int accountNumber = 0;
  int balance = 0;

  boolean keepgoing = true;


      @Override
      public void run()
      {
        while(keepgoing)
        {
          //reading msg
          try{
            message = (Message) readFromNet.readObject();
          } catch(IOException | ClassNotFoundException e){
            System.out.println("[TransactionManagerWorker.run] Message could not be read from object stream.");
            System.exit(1);
          }

        switch(message.getType())
        {
        //////////OPEN
        case OPEN_TRANSACTION:

          synchronized (transactions) {
            transaction = new Transaction(TransactionCounter++);
            transactions.add(transaction);
          }

          try {
            writeToNet.writeObject(transaction.getTransactionID());
          } catch (IOException e) {
              System.out.println("[TransactionManagerWorker.run] OPEN_TRANSACTION - Error when writing transactionID");
          }
          transaction.log("[TransactionManagerWorker.run] OPEN_TRANSACTION #" + transaction.getTransactionID());
          break;

        //////////CLOSE
            case CLOSE_TRANSACTION:
            TransactionServer.lockManager.unLock(transaction);
            transactions.remove(transaction);
            try
            {
              readFromNet.close();
              writeToNet.close();
              client.close();
              keepgoing = false;
            } catch(IOException e)
            {
              System.out.println("[TransactionManagerWorker.run] CLOSE_TRANSACTION - Error when closing connectiont to client");
            }

            transaction.log("[TransactionManagerWorker.run] CLOSE_TRANSACTION #" + transaction.getTransactionID());

            //print all transactions logs
            if(TransactionServer.transactionView)
            {
              System.out.println(transaction.getLog());
            }
            break;

        //////////READ
            case READ_REQUEST:
              accountNumber = (Integer) message.content();
              transaction.log("[TransactionManagerWorker.run] READ_REQUEST >>>>>>>>>>>> account #: " + accountNumber");
              balance = TransactionServer.AccountManager.read(accountNumber, transaction);

              try
              {
                writeToNet.writeObject((Integer) balance);
              } catch (IOException e) {
                System.out.println("[TransactionManagerWorker.run] READ_REQUEST - Error whent writing to object stream");
              }

              transaction.log("[TransactionManagerWorker.run] READ_REQUEST <<<<<<<<<<<< account #: " + accountNumber + ", balance: " + balance);
              break;

        //////////WRITE
            case WRITE_REQUEST:
              Object[] content = (Object[]) message.getContent();
              accountNumber = ((Integer) content[0]);
              balance = ((Integer) content[1]);
              transaction.log("[TransactionManagerWorker.run] WRITE_REQUEST >>>>>>>>>>>> account #: " + accountNumber")

              balance = TransactionServe.AccountManager.write(accountNumber,transaction,balance);
              try
              {
                writeToNet.writeObject((Integer) balance);
              } catch (IOException e)
              {
                System.out.println("[TransactionManagerWorker.run] WRITE_REQUEST - Error whent writing");
              }

              transaction.log("[TransactionManagerWorker.run] WRITE_REQUEST <<<<<<<<<<<< account #: " + accountNumber")
              break;

              default:
                System.out.println("[TransactionManagerWorker.run] Warning: message type not implemented");

            }
          }
        }

      }
    }
