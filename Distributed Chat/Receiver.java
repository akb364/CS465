import java.util.*;
import java.net.*;
import java.io.*;

public class Receiver extends Thread implements Serializable
{

    private ServerSocket receiverSock;

    public Receiver()
    {
        try
        {
            receiverSock = new ServerSocket(0);
            System.out.println("Receiver socket created on port " + receiverSock.getLocalPort());
        }
        catch (IOException ex)
        {
            System.out.println(ex.toString());
        }
    }

    @Override
    public void run()
    {
        while(true)
        {
            try
            {
                (new ReceiverWorker(receiverSock.accept())).start();
            }
            catch (IOException ex)
            {
                System.out.println(ex.toString());
            }
        }
    }

}