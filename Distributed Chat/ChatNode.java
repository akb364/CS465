import java.util.*;
import java.net.*;
import java.io.*;

public class ChatNode
{
    private Participant self;
    private LinkedList<Participant> participantList;
    private Socket sock;
    private ServerSocket serverSock;
    //private Sender sender;
    //private Receiver receiver;

    public ChatNode(String ip)
    {
        // create a new server socket with random port
        try
        {
            serverSock = new ServerSocket(0);
        }
        catch(IOException e)
        {

        }

        this.self = new Participant(ip, serverSock.getLocalPort());
        this.participantList = new LinkedList<Participant>();

        //this.sender = new Sender(this.self);
        //this.receiver = new Receiver(this.self);

    }

    public static void main(String[] args)
    {

        String ownIP = args[0];

        ChatNode node = new ChatNode(ownIP);

        println("listening on port " + (String) node.self.port);

        // if user gives other ip & port
        if( args[1] != NULL && args[2] != NULL)
        {
            otherNodeIP = args[1];
            int otherNodePort = Integer.parseInt(args[2]);

            connectToMesh(new Participant(otherNodeIP, otherNodePort))
        }

        runReceiver();

        println("done message");

    }

    public void connectToMesh(Participant meshParticipant)
    {
        // open connection with other node
        joinConnection = new Socket(meshParticipant.ip, meshParticipant.port);

        // send request message to node
        writeToNet = ObjectOutputStream(joinConnection.getOutputStream());
        readFromNet = ObjectInputStream(joinConnection.getInputStream());

        writeToNet.writeObject(new JoinMessage(self));

        participantList = readFromNet.readObject();
    }

    public void runReceiver()
    {
        Boolean runServer = true;

        while(runServer)
        {

        }

    }

}

class Participant
{
    public String ip;
    public int port;

    public Participant(String ip, int port)
    {
        this.ip = ip;
        this.port = port;
    }
}

class Sender
{
    private Participant self;

    public Sender(Participant self)
    {
        this.self = self;
    }


}

class Receiver
{

    private ServerSocket serverSock;
    private int port;

    public Receiver(int port)
    {
        this.port = port;
    }

    public void runReceiver()
    {
        Boolean runServer = true;

        while(runServer)
        {

        }


    }

    private handleReceive() implements Runnable
    {

    }

}

// base class
class MessageType
{
    public Participant sender;

    public MessageType(Participant sender)
    {
        this.sender = sender;
    }
}

// sent from joining node to random node in mesh
class JoinMessage extends MessageType implements Serializable
{
    public JoinMessage(Participant sender)
    {
       super(sender);
    }
}

// sent from one node in the mesh to all others
// to tell them a new node has joined so they
// can add the node to their participants list
class JoinedMessage extends MessageType
{
    public Participant newNode;

    JoinedMessage(Participant sender,Participant newNode)
    {
        super(sender);
        newNode = newNode;
    }
}

// sent from one node to the joining node
class ParticipantsMessage
{
    public LinkedList<Participant> participantList;

    public ParticipantsMessage(LinkedList<Participant>
                               participantList)
    {
        this.participantList = participantList;
    }
}

// chat message sent from one node to all others
class ChatMessage extends MessageType
{
    public String message;

    public ChatMessage(Participant sender, String message)
    {
        super(sender);
        this.message = message;
    }
}

// message sent from one node to all others
// saying it is leaving so they can remove the node
// from the participant list
class LeaveMessage extends MessageType
{
    public LeaveMessage(Participant sender)
    {
        super(sender);
    }
}
