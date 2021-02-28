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
            // create socket with random port
            receiverSock = new ServerSocket(0);
            ChatNode.self.port = receiverSock.getLocalPort();
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
        // server loop
        while(true)
        {
            try
            {
                // spawn new ReceiverWorker thread
                (new ReceiverWorker(receiverSock.accept())).start();
            }
            catch (IOException ex)
            {
                System.out.println(ex.toString());
            }
        }
    }

}