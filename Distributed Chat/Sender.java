import java.util.*;
import java.net.*;
import java.io.*;
import java.util.logging.*;



// only one thread
// can ask to join or leave or send message
class Sender extends Thread implements Serializable
{
    private Participant self;

    public Sender()
    {
      System.out.println("Got here");
    }
    @Override
    public void run()
    {
      Scanner userInput = new Scanner(System.in);
      System.out.print("Enter your information: <Username> <IP>");
      String inputLine = "";
      boolean hasJoined = false;

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
                String joinAddress = "";
                int joinPort = 0;
                String joinName = "";
                try
                {
                    joinName = connectionInfo[0];
                    joinAddress = connectionInfo[1];
                    joinPort = Integer.parseInt(connectionInfo[2]);
                }
                catch(ArrayIndexOutOfBoundsException ex)
                {
                System.out.println(ex.toString());
                }
                Socket joinConnection;
                try
                {
                  joinConnection = new Socket(joinAddress, joinPort);
                }
                catch (IOException ex)
                {
                  System.out.println(ex.toString());

                  continue;
                }

                //send join request
                try
                {
                  //open obj streams
                ObjectOutputStream  writeToNet = new ObjectOutputStream(joinConnection.getOutputStream());
                ObjectInputStream readFromNet = new ObjectInputStream(joinConnection.getInputStream());

                  //send join request
                  writeToNet.writeObject(new JoinMessage(ChatNode.self));

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

                    for( int index = 0; index < ChatNode.participantList.size(); index++ )
                    {
                      joinedConnection = new Socket(ChatNode.self.ip, ChatNode.self.port);
                      ObjectOutputStream writeToNet = new ObjectOutputStream(joinedConnection.getOutputStream());
                      writeToNet.writeObject(new JoinMessage(ChatNode.self));
                      joinedConnection.close();
                    }


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
                LeaveMessage leaveMessage = new LeaveMessage(ChatNode.self);
                Socket leaveConnection = null;

                try {
                  for( int index = 0; index < ChatNode.participantList.size(); index++ )
                  {
                    leaveConnection = new Socket(ChatNode.self.ip, ChatNode.self.port);
                    ObjectOutputStream writeToNet = new ObjectOutputStream(leaveConnection.getOutputStream());
                    writeToNet.writeObject(leaveMessage);
                    leaveConnection.close();
                  }

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
                Object[] noteMessageContent = new Object[2];
                noteMessageContent[0] = ChatNode.self;
                noteMessageContent[1] = ChatNode.self.name + ": " + inputLine;
                ChatMessage noteMessage = new ChatMessage(ChatNode.self, inputLine);
                //send note message to this participant
                try{
                  for( int index = 0; index < ChatNode.participantList.size(); index++ )
                  {
                    messageConnection = new Socket(ChatNode.self.ip, ChatNode.self.port);
                    ObjectOutputStream writeToNet = new ObjectOutputStream(messageConnection.getOutputStream());
                    writeToNet.writeObject(noteMessage);
                    messageConnection.close();
                  }

                } catch(IOException ex)
                {
                  System.out.println(ex.toString());
                }
              }

        }
      }

/*    public void connectToMesh(Participant meshParticipant)
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
    }*/

}
