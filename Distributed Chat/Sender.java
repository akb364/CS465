import java.util.*;
import java.net.*;
import java.io.*;

// only one thread
// can ask to join or leave or send message
class Sender extends Thread implements Serializable {
    //private Participant self;

    public Sender() 
    {

    }

    @Override
    public void run() 
    {
        Scanner userInput = new Scanner(System.in);
        System.out.print("Commands: JOIN, LEAVE\n");
        String inputLine = "";
        boolean hasJoined = false;

        // loop - runs until SHUTDOWN
        while (true) 
        {
            // get user input
            inputLine = userInput.nextLine();

            // if starts with JOIN
            if (inputLine.startsWith("JOIN")) 
            {
                if (hasJoined == true) 
                {
                    System.err.println("You have already joined the chat..");
                    continue;
                }
                String[] connectionInfo = inputLine.split(" ");
                String joinAddress = "";
                int joinPort = 0;
                String joinName = "";
                
                try 
                {
                    joinName = connectionInfo[0];
                    joinAddress = connectionInfo[1];
                    joinPort = Integer.parseInt(connectionInfo[2]);
                } 
                catch (ArrayIndexOutOfBoundsException ex) 
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
                ParticipantsMessage participants;
                //send join request
                try 
                {
                    //open obj streams
                    ObjectOutputStream writeToNet = new ObjectOutputStream(joinConnection.getOutputStream());
                    ObjectInputStream readFromNet = new ObjectInputStream(joinConnection.getInputStream());

                    //send join request
                    writeToNet.writeObject(new JoinMessage(ChatNode.self));

                    // read object
                    participants = (ParticipantsMessage) readFromNet.readObject();

                    ChatNode.participantList = participants.participantList;

                    //done
                    joinConnection.close();
                } 
                catch (ClassNotFoundException ex) 
                {
                    System.out.println(ex.toString());
                }
                catch (IOException ex)
                {
                    System.out.println(ex.toString());
                }

                Socket joinedConnection = null;
                hasJoined = true;

                try 
                {
                    for (int index = 0; index < ChatNode.participantList.size(); index++) 
                    {
                        joinedConnection = new Socket(ChatNode.participantList.get(index).ip, ChatNode.participantList.get(index).port);
                        ObjectOutputStream writeToNet = new ObjectOutputStream(joinedConnection.getOutputStream());
                        writeToNet.writeObject(new JoinedMessage(ChatNode.self));
                        joinedConnection.close();
                    }

                } catch (IOException ex) 
                {
                    System.out.println(ex.toString());
                    continue;
                }
            }

            // if starts with LEAVE
            else if (inputLine.startsWith("LEAVE") || inputLine.startsWith("SHUTDOWN")) 
            {
                if (hasJoined == false) 
                {
                    System.err.println("You have not joined a chat yet ...");
                    continue;
                }

                // leaving chat
                LeaveMessage leaveMessage = new LeaveMessage(ChatNode.self);
                Socket leaveConnection = null;

                try 
                {
                    for (int index = 0; index < ChatNode.participantList.size(); index++) 
                    {
                        leaveConnection = new Socket(ChatNode.participantList.get(index).ip, ChatNode.participantList.get(index).port);
                        ObjectOutputStream writeToNet = new ObjectOutputStream(leaveConnection.getOutputStream());
                        writeToNet.writeObject(leaveMessage);
                        leaveConnection.close();
                    }

                } 
                catch (IOException ex) 
                {
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
                if (hasJoined = false) 
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
                try 
                {
                    for (int index = 0; index < ChatNode.participantList.size(); index++) 
                    {
                        messageConnection = new Socket(ChatNode.participantList.get(index).ip, ChatNode.participantList.get(index).port);
                        ObjectOutputStream writeToNet = new ObjectOutputStream(messageConnection.getOutputStream());
                        writeToNet.writeObject(noteMessage);
                        messageConnection.close();
                    }

                } catch (IOException ex) 
                {
                    System.out.println(ex.toString());
                }
            }

        }
    }

}