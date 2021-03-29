import java.io.*;
import java.util.*;
import java.net.*;

public class TransactionServerProxy 
{
    private Boolean transactionIsOpen;
    private int transactionID;
    private Socket connection;
    private ObjectOutputStream writeToNet;
    private ObjectInputStream readFromNet;
    private int serverPort;
    private InetAddress ip;

    // constructor
    public TransactionServerProxy(String ip, int serverPort)
    {
        this.transactionIsOpen = false;
        this.serverPort = serverPort;
        try
        {
            this.ip = InetAddress.getByName(ip);
        }
        catch (UnknownHostException ex)
        {
            System.out.println(ex.toString());
        }
    }

    public int openTransaction()
    {
        System.out.println("yo");
        // check if transaction is already opened
        if (transactionIsOpen)
        {
            System.out.println("Transaction is already open!");
            return -1;
        }
        try
        {
            connection = new Socket(ip, serverPort);
            
            writeToNet = new ObjectOutputStream(connection.getOutputStream());
            readFromNet = new ObjectInputStream(connection.getInputStream());

            // send MsgOpenTransaction
            writeToNet.writeObject(new MsgOpenTransaction());

            // get response as int (transactionID)
            MsgTransactionID message = (MsgTransactionID)readFromNet.readObject();

            transactionID = message.transactionID;
            System.out.println(transactionID);
            
            writeToNet.flush();
        }
        catch (IOException | ClassNotFoundException e)
        {
            return -1;
        }
        System.out.println("Transaction successfully opened.");

        transactionIsOpen = true;

        return transactionID;
    }

    public void closeTransaction(int transactionID)
    {
        if(!transactionIsOpen)
        {
            System.out.println("Transaction is not open!");
            return;
        }
        try
        {
            // send with transactionID
            writeToNet.writeObject(new MsgCloseTransaction(transactionID));
            writeToNet.flush();
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
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
            return -1;
        }
        try
        {
            // send message
            writeToNet.writeObject(new MsgReadRequest(accountNumber));
            writeToNet.flush();

            // get response
            MsgAccountBalance message = (MsgAccountBalance)readFromNet.readObject();

            // return val
            return message.accountBalance;
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
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

            // send message
            writeToNet.writeObject(new MsgWriteRequest(accountNumber, amount));
            writeToNet.flush();

            // no response
            System.out.println("write successfull");
            return;
        }
        catch (Exception e)
        {
            System.out.println("Failed to write to account.");
        }
    }
}
