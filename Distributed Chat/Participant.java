public class Participant
{
    public String ip;
    public String name;
    public int port;

    public Participant(String name, String ip, int port)
    {
        this.ip = ip;
        this.name = name;
        this.port = port;
    }

    public Participant(String name, String ip)
    {
        this.ip = ip;
        this.name = name;
    }
}