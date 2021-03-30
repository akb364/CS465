import java.util.*;
import java.net.*;
import java.io.*;

// only one thread
// can ask to join or leave or send message
class Sender extends Thread implements Serializable {
    public static boolean hasJoined;

    public Sender() 
    {
        this.hasJoined = false;
    }

    @Override
    public void run() 
    {
        // get user input
        Scanner userInput = new Scanner(System.in);
        System.out.print("Commands: JOIN, LEAVE\n");
        String inputLine = "";

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

                // get ip and port from user
                String[] connectionInfo = inputLine.split(" ");
                String joinAddress = "";
                int joinPort = 0;
                
                try 
                {
                    joinAddress = connectionInfo[1];
                    joinPort = Integer.parseInt(connectionInfo[2]);
                } 
                catch (ArrayIndexOutOfBoundsException ex) 
                {
                    System.out.println(ex.toString());
                }
                // connect
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

                // loop through participant list and send joinedMessage
                try 
                {
                    for (int index = 0; index < ChatNode.participantList.size(); index++) 
                    {
                        joinedConnection = new Socket(ChatNode.participantList.get(index).ip, ChatNode.participantList.get(index).port);
                        ObjectOutputStream writeToNet = new ObjectOutputStream(joinedConnection.getOutputStream());
                        writeToNet.writeObject(new JoinedMessage(ChatNode.self));
                        writeToNet.close();
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

                // loop though participantList and send leave message
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
                    ChatNode.participantList = new LinkedList<Participant>();
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
                if (hasJoined == false) 
                {
                    System.err.println("You need to join chat first!");
                    continue;
                }

                Socket messageConnection;

                // send message to each participant
                try 
                {
                    for (int index = 0; index < ChatNode.participantList.size(); index++) 
                    {
                        Object[] noteMessageContent = new Object[2];
                        noteMessageContent[0] = ChatNode.self;
                        noteMessageContent[1] = ChatNode.self.name + ": " + inputLine;
                        ChatMessage noteMessage = new ChatMessage(ChatNode.self, inputLine);
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