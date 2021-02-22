import java.util.*;
import java.net.*;
import java.io.*;

public class Receiver
{

    private ServerSocket serverSock;
    private int port;

    public Receiver(int port)
    {
        this.port = port;
    }

    // should spawn off worker thread for each accept
    public void runReceiver() // extends thread?
    {
        Boolean runServer = true;

        while(runServer)
        {
            try
            {
            Socket clientSock = serverSock.accept();

            ObjectInputStream readFromNet = new ObjectInputStream(clientSock.getInputStream());

            Object input = readFromNet.readObject();

            if(input instanceof JoinMessage)
            {

            }
            }
            catch(IOException e)
            {
                System.out.println("hi");
            }
            catch(ClassNotFoundException e)
            {
                System.out.println("hi");
            } 

        }
    }

}

// server loop
    // spawns receiverWorker