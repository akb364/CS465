import java.io.*;

// base class
public class MessageType implements Serializable
{
    public Participant sender;

    public MessageType(Participant sender)
    {
        this.sender = sender;
    }
}