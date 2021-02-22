import java.util.*;
import java.net.*;
import java.io.*;

// only one thread
// can ask to join or leave or send message
class Sender
{
    private Participant self;

    public Sender(Participant self)
    {
        this.self = self;
    }

    // loop - runs until leave

        // get user input

        // switch statement

            // if starts with JOIN

            // if starts with LEAVE

            // otherwise, send message

    public void connectToMesh(Participant meshParticipant)
    {
        try
        {
        // open connection with other node
        Socket joinConnection = new Socket(meshParticipant.ip, meshParticipant.port);

        // send request message to node
        ObjectOutputStream writeToNet = new ObjectOutputStream(joinConnection.getOutputStream());
        ObjectInputStream readFromNet = new ObjectInputStream(joinConnection.getInputStream());

        writeToNet.writeObject(new JoinMessage(self));

        //Object incoming = readFromNet.readObject();
        participantList.add((Participant)readFromNet.readObject());
        }
        catch(IOException e)
        {
            System.out.println("hi");
        }
        catch(ClassNotFoundException e)
        {
            System.out.println("hi");
        }
    }

}