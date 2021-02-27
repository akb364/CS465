import java.io.*;

// message sent from one node to all others
// saying it is leaving so they can remove the node
// from the participant list
public class LeaveMessage extends MessageType implements Serializable
{
    public LeaveMessage(Participant sender)
    {
        super(sender);
    }
}