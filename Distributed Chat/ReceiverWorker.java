import java.util.*;
import java.net.*;
import java.io.*;


public class ReceiverWorker
{
    Socket peerConnection = null;
    ObjectInputStream readFromNet = null;
    ObjectOutputStream writeToNet = null;
    MessageType message = null;

    public ReceiverWorker(Socket peerConnection)
    {
        this.peerConnection = peerConnection;


        readFromNet = new ObjectInputStream(peerConnection.getInputStream());
        writeToNet = new ObjectOutputStream(peerConnection.getOutputStream());
    }

    public void run()
    {
        try
        {
            message = (MessageType) readFromNet.readObject();
        }
        catch (Exception ex)
        {
            System.out.println("Message could not be read from obj stream");
            System.exit(1);
        }

        Iterator<Participant> participantIterator;


        switch (message.getType())
        {

            case JoinMessage:

                try
                {
                    writeToNet.writeObject(new ParticipantsMessage(me.participantList));
                }
                catch (IOException ex)
                {
                    System.out.println(ex.ToString());
                }

            case JoinedMessage:

                try
                {
                    me.participantList.add(message.newNode);
                    System.out.println(message.newNode.name + "has joined!");
                }
                catch (IOException ex)
                {
                    System.out.println(ex.ToString());
                }

            case LeaveMessage:

                try
                {
                    me.participantList.remove(message.newNode);
                }
                catch (IOException ex)
                {
                    System.out.println(ex.ToString());
                }

            case ChatMessage:

                try
                {
                    System.out.println(message.sender.name + ": " + message.message);
                }
                catch (IOException ex)
                {
                    System.out.println(ex.ToString());
                }
        }
    }
}
 