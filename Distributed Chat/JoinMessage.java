// sent from joining node to random node in mesh
public class JoinMessage extends MessageType 
{
    public JoinMessage(Participant sender)
    {
       super(sender);
    }
}