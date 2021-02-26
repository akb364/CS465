import java.util.*;
import java.net.*;
import java.io.*;

public class ChatNode
{
    public static Participant self;
    public static LinkedList<Participant> participantList;
    private Socket sock;
    private ServerSocket serverSock; 
    //private Sender sender;
    private Receiver receiver;
    String ip;
    String otherNodeIP;
    int otherNodePort;


    public ChatNode(String[] args)
    {
        // create a new server socket with generated port
        try
        {
            serverSock = new ServerSocket(0);
        }
        catch(IOException e)
        {

        }

        if( args.length > 1 )
        {
            ip = args[0];
            otherNodeIP = args[1];
            otherNodePort = Integer.parseInt(args[2]);
            

            //node.connectToMesh(new Participant(otherNodeIP, otherNodePort));
        }

        this.self = new Participant("name", ip, serverSock.getLocalPort());
        this.participantList = new LinkedList<Participant>();


    }

    public static void main(String[] args)
    {
        ChatNode node = new ChatNode(args);
    }

    public void run()
    {
        System.out.println("listening on port " + Integer.toString(this.self.port));

        new Receiver(this).start();

        System.out.println("done message");
    }

}















