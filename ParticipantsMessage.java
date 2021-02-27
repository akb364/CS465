import java.util.*;
import java.net.*;
import java.io.*;

// sent from one node to the joining node
public class ParticipantsMessage
{
    public LinkedList<Participant> participantList;

    public ParticipantsMessage(LinkedList<Participant>
                               participantList)
    {
        this.participantList = participantList;
    }
}