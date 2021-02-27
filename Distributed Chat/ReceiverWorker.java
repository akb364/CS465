import java.util.*;
import java.net.*;
import java.io.*;

public class ReceiverWorker extends Thread
{
    Socket peerConnection = null;
    ObjectInputStream readFromNet = null;
    ObjectOutputStream writeToNet = null;
    Object message = null;

    public ReceiverWorker(Socket peerConnection)
    {
        System.out.println("yo");
        this.peerConnection = peerConnection;
        try
        {
            readFromNet = new ObjectInputStream(peerConnection.getInputStream());
            writeToNet = new ObjectOutputStream(peerConnection.getOutputStream());
        }
        catch(IOException ex)
        {
            System.out.println("Message could not be read from obj stream");
            System.exit(1);
        }
    }
        
    public void run()
    {
        try
        {
            message = readFromNet.readObject();
            
        }
        catch (Exception ex)
        {
            System.out.println("Message could not be read from obj stream");
            System.exit(1);
        }

        if(message instanceof JoinMessage)
        {
                try
                {
                    writeToNet.writeObject(new ParticipantsMessage(ChatNode.participantList));
                }
                catch (IOException ex)
                {
                    System.out.println(ex.toString());
                }
        }
        
        else if(message instanceof JoinedMessage)
        {
            JoinedMessage joined = (JoinedMessage) message;
            ChatNode.participantList.add(joined.newNode);
            System.out.println(joined.newNode.name + " has joined!");
        }

        else if(message instanceof LeaveMessage)
        {
            LeaveMessage leave = (LeaveMessage) message;
            ChatNode.participantList.remove(leave.sender);
            System.out.println(leave.sender.name + " has left!");
        }

        else if(message instanceof ChatMessage)
        {
            ChatMessage chat = (ChatMessage) message;
            System.out.println(chat.sender.name + ": " + chat.message);
        }

    }
}