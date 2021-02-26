import java.util.*;
import java.net.*;
import java.io.*;


public class ReceiverWorker extends Thread
{
    Socket peerConnection = null;
    ObjectInputStream readFromNet = null;
    ObjectOutputStream writeToNet = null;
    Object message = null;
    ChatNode me = null;

    public ReceiverWorker(Socket peerConnection, ChatNode me)
    {
        this.peerConnection = peerConnection;
        this.me = me;
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
            message = (MessageType) readFromNet.readObject();
            
        }
        catch (Exception ex)
        {
            System.out.println("Message could not be read from obj stream");
            System.exit(1);
        }

        Iterator<Participant> participantIterator;


        if(message instanceof JoinMessage)
        {
                try
                {
                    writeToNet.writeObject(new ParticipantsMessage(me.participantList));
                }
                catch (IOException ex)
                {
                    System.out.println(ex.toString());
                }
        }
        
        else if(message instanceof JoinedMessage)
        {
            JoinedMessage joined = (JoinedMessage) message;
            me.participantList.add(joined.newNode);
            System.out.println(joined.newNode.name + "has joined!");

            // try
            // {
            //     JoinedMessage joined = (JoinedMessage) message;
            //     me.participantList.add(joined.newNode);
            //     System.out.println(joined.newNode.name + "has joined!");
            // }
            // catch (IOException ex)
            // {
            //     System.out.println(ex.toString());
            // }

        }

        else if(message instanceof LeaveMessage)
        {
            LeaveMessage leave = (LeaveMessage) message;
            me.participantList.remove(leave.sender);
            // try
            // {
            //     LeaveMessage leave = (LeaveMessage) message;
            //     me.participantList.remove(leave.sender);
            // }
            // catch (IOException ex)
            // {
            //     System.out.println(ex.toString());
            // }

        }

        else if(message instanceof ChatMessage)
        {
            ChatMessage chat = (ChatMessage) message;
            System.out.println(chat.sender.name + ": " + chat.message);
            // try
            // {
            //     ChatMessage chat = (ChatMessage) message;
            //     System.out.println(chat.sender.name + ": " + chat.message);
            // }
            // catch (IOException ex)
            // {
            //     System.out.println(ex.toString());
            // }
        }

        // switch (message.getType())
        // {

        //     case JoinMessage:

        //         try
        //         {
        //             writeToNet.writeObject(new ParticipantsMessage(me.participantList));
        //         }
        //         catch (IOException ex)
        //         {
        //             System.out.println(ex.toString());
        //         }

        //     case JoinedMessage:

        //         try
        //         {
        //             me.participantList.add(message.newNode);
        //             System.out.println(message.newNode.name + "has joined!");
        //         }
        //         catch (IOException ex)
        //         {
        //             System.out.println(ex.toString());
        //         }

        //     case LeaveMessage:

        //         try
        //         {
        //             me.participantList.remove(message.newNode);
        //         }
        //         catch (IOException ex)
        //         {
        //             System.out.println(ex.toString());
        //         }

        //     case ChatMessage:

        //         try
        //         {
        //             System.out.println(message.sender.name + ": " + message.message);
        //         }
        //         catch (IOException ex)
        //         {
        //             System.out.println(ex.toString());
        //         }
        // }
    }
}