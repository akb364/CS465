import java.io.*;

// chat message sent from one node to all others
public class ChatMessage extends MessageType implements Serializable
{
    public String message;

    public ChatMessage(Participant sender, String message)
    {
        super(sender);
        this.message = message;
    }
}