import java.util.*;
import java.net.*;
import java.io.*;

public class ChatNode
{
    private Participant self;
    private LinkedList<Participant> participantList;
    private ServerSocket serverSock;
    private Sender sender;
    private Receiver receiver;

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
        self = new Participant(ip, serverSock.getLocalPort());

        participantList = new LinkedList<Participant>();
        participantList.add(self);

    }

    public void run()
    {

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

}

class Receiver
{

}

class MessageType
{
    public Participant sender;

    public MessageType(Participant sender)
    {
        this.sender = sender;
    }
}

// sent from joining node to random node in mesh
class JoinMessage extends MessageType
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

