import java.io.*;

// sent from joining node to random node in mesh
public class JoinMessage extends MessageType implements Serializable
{
    public JoinMessage(Participant sender)
    {
       super(sender);
    }
}