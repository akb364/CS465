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
        this.peerConnection = peerConnection;

        try
        {
            readFromNet = new ObjectInputStream(peerConnection.getInputStream());
            writeToNet = new ObjectOutputStream(peerConnection.getOutputStream());
        }
        catch(IOException ex)
        {
            System.out.println("connection could not be established");
            System.exit(1);
        }
    }
        
    public void run()
    {
        try
        {
            // read message from connection
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
                // make participants list to send
                // with local list + self
                LinkedList<Participant> participants = new LinkedList<Participant>();
                participants.add(ChatNode.self);

                if (ChatNode.participantList.size() != 0)
                {
                    participants.addAll(ChatNode.participantList);  
                }

                // send message to joining node
                writeToNet.writeObject(new ParticipantsMessage(participants));

                Sender.hasJoined = true;

            }
            catch (IOException ex)
            {
                System.out.println(ex.toString());
            }
        }
        
        else if(message instanceof JoinedMessage)
        {
            // add joining node to participant list
            JoinedMessage joined = (JoinedMessage) message;

            ChatNode.participantList.add(joined.sender);

            System.out.println(joined.sender.name + " has joined!");
        }

        else if(message instanceof LeaveMessage)
        {
            // find leaving node and remove it from participantsList
            LeaveMessage leave = (LeaveMessage) message;
            for(int index = 0; index < ChatNode.participantList.size(); index++)
            {
                String leave_name = leave.sender.name;
                if(ChatNode.participantList.get(index).name.equals(leave_name))
                {
                    ChatNode.participantList.remove(index);
                }
            }

            System.out.println(leave.sender.name + " has left!");
        }

        else if(message instanceof ChatMessage)
        {
            // display chat message
            ChatMessage chat = (ChatMessage) message;
            System.out.println(chat.sender.name + ": " + chat.message);
        }

    }
}