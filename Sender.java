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
              String joinName;
              try
              {
                  joinName = connectionInfo[0];
                  joinAddress = connectionInfo[1];
                  joinPort = Integer.parseInt(connectionInfo[2]);
              }
              catch(ArrayIndexOutOfBoundsException ex)
              {
                System.out.println(ex.toString());
               participantUser = new Participant(joinName,joinAddress,joinPort);
               if(participantList.getFirst() == null)
               {
                 participantList.addFirst(participantUser);
               }
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
                System.out.println(ex.toString());

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

                //done
                joinConnection.close();
              }
                catch (IOException ex)
                {
                  System.out.println(ex.toString());
                }
                //send JOINED message to SUCCESSORs PREDECESSOR
                Socket joinedConnection = null;
                try
                {
                  joinedConnection = new Socket(ChatNode.ip);
                  writeToNet = new ObjectOutputStream(joinedConnection.getOutputStream());
                  writeToNet.writeObject(new MessageJoined(ChatNode.ME));
                  joinedConnection.close();
                } catch (IOException ex) {
                  System.out.println(ex.toString());
                  continue;
                }
                hasJoined = true;

            }

            // if starts with LEAVE
            else if(inputLine.startsWith("LEAVE") || inputLine.startsWith("SHUTDOWN"))
            {
              if (hasJoined == false)
              {
                System.err.println("You have not joined a chat yet ...");
                continue;
              }

              // leaving chat
              MessageLeave leaveMessage = new LeaveMessage(ChatNode);
              Socket leaveConnection = null;

              try {
                leaveConnection = new Socket(ChatNode.ip, ChatNode)
                writeToNet = new ObjectOutputStream(leaveConnection.getOutputStream());
                writeToNet.writeObject(leaveMessage);
                leaveConnection.close();
              } catch (IOException ex) {
                System.out.println(ex.toString());
                continue;
              }

              MessageLeft leftMessage = new leftMessage(ChatNode);
              Socket leftConnection = null;
              try {
                leftConnection = new Socket(ChatNode.ip,ChatNode);
                writeToNet = new ObjectOutputStream(leftConnection.getOutputStream());
                writeToNet.writeObject(leftMessage);
                leftConnection.close();
              } catch (IOException ex) {
                System.out.println(ex.toString());
                continue;
              }

              if (inputLine.startsWith("LEAVE"))
              {
                hasJoined = false;
                System.out.println("Left chat");
              }
              else
              {
                // shut down
                System.out.println("Shutting down\n");
                System.exit(0);
              }
            }

            // otherwise, send message

            else
            {
              if(hasJoined = false)
              {
                System.err.println("You need to join chat first!");
                continue;
              }
              Socket messageConnection;
              Onject[] noteMessageContent = new Object[2];
              noteMessageContent[0] = ChatNode.self;
              noteMessageContent[1] = ChatNode.self.getName() + ": " + inputLine
              MessageNote noteMessage = new MessageNote(noteMessageContent);
              //send note message to this participant
              try{
                messageConnection = new Socket(ChatNode.ip);
                writeToNet = new ObjectOutputStream(messageConnection.getOutputStream());
                writeToNet.writeObject(noteMessage);
                messageConnection.close();
              } catch(IOException ex)
              {
                System.out.println(ex.toString());
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
