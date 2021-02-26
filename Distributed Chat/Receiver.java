import java.util.*;
import java.net.*;
import java.io.*;

public class Receiver extends Thread implements Serializable
{

    static ServerSocket receiverSock;
    ChatNode me = null;

    public Receiver(ChatNode me)
    {
        try
        {
            this.me = me;
            receiverSock = new ServerSocket(me.self.port);
            System.out.println("Receiver socket created on port " + me.self.port);
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
                (new ReceiverWorker(receiverSock.accept(), this.me)).start();
            }
            catch (IOException ex)
            {
                System.out.println(ex.toString());
            }
        }
    }

}