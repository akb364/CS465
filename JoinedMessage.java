// sent from one node in the mesh to all others
// to tell them a new node has joined so they
// can add the node to their participants list
public class JoinedMessage extends MessageType
{
    public Participant newNode;

    JoinedMessage(Participant sender,Participant newNode)
    {
        super(sender);
        newNode = newNode;
    }
}