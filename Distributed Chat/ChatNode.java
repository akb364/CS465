import java.util.*;
import java.net.*;
import java.io.*;

public class ChatNode
{
    public static Participant self;
    public static LinkedList<Participant> participantList;
    private Sender sender;
    private Receiver receiver;

    public ChatNode(String[] args)
    {
        // args will be: username, ip
        // JOIN message will be sent later in a separate command

        if( args.length >= 2 )
        {
            this.self = new Participant(args[0], args[1]);
        }
        else
        {
            System.out.println("not enough args");
            System.exit(1);
        }
    }

    public static void main(String[] args)
    {
        ChatNode node = new ChatNode(args);
        node.run();
    }

    public void run()
    {
        new Receiver().start();
        new Sender().start();

    }

}