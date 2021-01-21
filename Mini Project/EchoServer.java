import java.net.*;
import java.io.*;

public class EchoServer{
  public static void main(String[] args) throws IOException{

    ServerSocket serverSock = null;
    try{
      serverSock = new ServerSocket(8899);
    }catch(IOException ie){
      System.out.println("can listen on port # 8899");
      System.exit(1);
    }

    Boolean runServer = true;

    Socket clientSock = null;

    System.out.println("[+]Listening for connection... ");
    try{
      clientSock = serverSock.accept();
    }catch(IOException ie){
      System.out.println("[-]Accept failed");
      System.exit(1);
    }

    while(runServer) 
    {
      clientSock = serverSock.accept();

      System.out.println("[+]Connection successful.");
      System.out.println("[+]Listening for input");

      new Thread(new EchoThread(clientSock)).start();
    }
    clientSock.close();
    
    serverSock.close();
  }
}
