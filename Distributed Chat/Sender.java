import java.util.*;
import java.net.*;
import java.io.*;
import java.util.logging.*;


// only one thread
// can ask to join or leave or send message
class Sender
{
    private Participant self;

    public Sender(Participant self)
    {
        this.self = self;
    }

    // loop - runs until SHUTDOWN
    while(true)
    {
        // get user input
        inputLine = userInput.nextLine();
        // switch statement

            // if starts with JOIN
            if(inputLine.startsWith("JOIN"))
            {
              if(hasJoined == true)
              {
                System.err.println("You have already joined the chat..");
                continue;
              }
              String[] connectionInfo = inputLine.split("[]+");
              String joinAddress;
              int joinPort;

              try
              {
                  joinAddress = connectionInfo[1];
                  joinPort = Integer.parseInt(connectionInfo[2]);
              }
              catch(ArrayIndexOutOfBoundsException ex)
              {
                Logger.getLogger(Sender.class.getName()).log(level.INFO, "Adding node");

                ChatNode.SUCCESSOR = ChatNode.self;
                ChatNode.PREDECESSOR = ChatNode.self;
                hasJoined = true;

               continue;
              }

              Socket joinConnection;
              try
              {
                joinConnection = new Socket(joinAddress, joinPort);
              }
              catch
              {
                Logger.getLogger(Sender.class.getName()).log(level.SEVERE,"Connection failed", ex);
                continue;
              }

              //send join request
              try
              {
                //open obj streams
                writeToNet = new ObjectOutputStream(joinConnection.getOutputStream());
                readFromNet = new ObjectInputStream(joinConnection.getInputStream());

                //send join request
                writeToNet.writeObject(new MessageJoin(ChatNode.self));

                //recieve SUCCESSOR INFO
                ChatNode.SUCCESSOR = (NodeInfo)readFromNet.readObject()
                ChatNode.PREDECESSOR = (NodeInfo)readFromNet.readObject()

                //done
                joinConnection.close();
              }
                catch (IOException ex)
                {
                Logger.getLogger(Sender.class.getName()).log(level.SEVERE,"Connection failed", ex);)
                }
                catch (ClassNotFoundException ex)
                {
                Logger.getLogger(Sender.class.getName()).log(level.SEVERE,"Connection failed", ex);
                }

                //send JOINED message to SUCCESSORs PREDECESSOR
                Socket joinedConnection = null;
                try
                {
                  joinedConnection = new Socket(ChatNode.PREDECESSOR.getAddress())
                  writeToNet = new ObjectOutputStream(joinedConnection.getOutputStream)
                  writeToNet.writeObject(new MessageJoined(ChatNode.ME));
                  joinedConnection.close();
                } catch (IOException ex) {
                Logger.getLogger(Sender.class.getName()).log(level.SEVERE,"Connection failed", ex);
                  continue;
                }
                hasJoined = true;

            }

            // if starts with LEAVE
            else if(inputLine.startsWith("LEAVE"))
            {

            }

            // otherwise, send message

            else
            {
              if(hasJoined = false)
              {
                System.err.println("You need to join chat first!");
                continue;

              }
              Socket noteConnection;
              Onject[] noteMessageContent = new Object[2];
              noteMessageContent[0] = ChatNode.self;
              noteMessageContent[1] = ChatNode.self.getName() + ": " + inputLine
              MessageNote noteMessage = new MessageNote(noteMessageContent);
              //send note message to this participant
              try{
                noteConnection = new Socket(ChatNode.SUCCESSOR.getAddress)
                writeToNet = new ObjectOutputStream(noteConnection.getOutputStream)
                writeToNet.writeObject(noteMessage);
                noteConnection.close();
              } catch(IOException ex)
              {
                Logger.getLogger(Sender.class.getName()).log(level.SEVERE,"Connection failed", ex);
              }
            }

      }

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
