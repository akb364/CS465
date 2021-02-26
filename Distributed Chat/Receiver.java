import java.util.*;
import java.net.*;
import java.io.*;

public class Receiver extends Thread implements Serializable
{

    static ServerSocket receiverSock;

    public Receiver(Participant myInfo)
    {
        try
        {
            receiverSock = new ServerSocket(myInfo.port);
            System.out.println("Receiver socket created on port " + myInfo.port);
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