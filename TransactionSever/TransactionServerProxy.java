public class TransactionServerProxy 
{
    private Boolean transactionIsOpen;
    private int transactionID;
    private Socket connection;
    private ObjectOutputStream writeToNet;
    private ObjectInputStream readFromNet;
    private int serverPort;

    // constructor
    public TransactionServerProxy(int serverPort)
    {
        this.transactionIsOpen = false;
        this.serverPort = serverPort;
    }

    public void openTransaction()
    {
        // check if transaction is already opened
        if (transactionIsOpen)
        {
            System.out.println("Transaction is already open!");
            return;
        }
        try
        {
            connection = new Socket(serverPort);

            // open connection with server
            writeToNet = new ObjectOutputStream(connection.getOutputStream());
            readFromNet = new ObjectInputStream(connection.getInputStream());

            // send MsgOpenTransaction
            writeToNet.writeObject(new MsgOpenTransaction());

            // get response as int (transactionID)
            MsgTransactionID message = (MsgTransactionID)readFromNet.readObject();

            transactionID = message.transactionID;
        }
        catch (Exception e)
        {
            System.out.println("Failed to open transaction.");
            return;
        }
        System.out.println("Transaction successfully opened.");

        transactionIsOpen = true;
    }

    public void closeTransaction()
    {
        if(!transactionIsOpen)
        {
            System.out.println("Transaction is not open!");
            return;
        }
        try
        {
            // send with transactionID
        }
        catch (Exception e)
        {
            System.out.println("Failed to close transaction.");
            return;
        }
        System.out.println("Transaction successfully closed.");

        transactionIsOpen = false;   
    }

    public int read(int accountNumber)
    {
        // check if transaction is open
        if(!transactionIsOpen)
        {
            System.out.println("Transaction is not open!");
            return;
        }
        try
        {
            // assume connection exists
            writeToNet = new ObjectOutputStream(connection.getOutputStream());
            readFromNet = new ObjectInputStream(connection.getInputStream());

            // send message
            writeToNet.writeObject(new MsgReadRequest());

            // get response
            MsgAccountBalance message = (MsgAccountBalance)readFromNet.readObject();

            // return val
            return message.accountBalance;
        }
        catch (Exception e)
        {
            System.out.println("Failed to read from account.");
            return -1;
        }
    }

    public void write(int accountNumber, int amount)
    {
        if(!transactionIsOpen)
        {
            System.out.println("Transaction is not open!");
            return;
        }
        try
        {
            // assume connection exists
            writeToNet = new ObjectOutputStream(connection.getOutputStream());
            readFromNet = new ObjectInputStream(connection.getInputStream());

            // send message
            writeToNet.writeObject(new MsgWriteRequest(accountNumber, amount));

            // no response
            System.out.println("write successfull");
        }
        catch (Exception e)
        {
            System.out.println("Failed to write to account.");
            return;
        }
    }
}
