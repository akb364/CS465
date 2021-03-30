import java.net.*;
import java.io.*;

public class EchoServer{
  public static void main(String[] args) throws IOException{

    // initialize new socket
    ServerSocket serverSock = null;
    
    try{
      serverSock = new ServerSocket(8899);
    }catch(IOException ie){
      System.out.println("can listen on port # 8899");
      System.exit(1);
    }

    // boolean to run while loop
    Boolean runServer = true;

    // initialize client socket
    Socket clientSock = null;


    System.out.println("[+]Listening for connection... ");

    // clientSock = serverSock.accept();


    // Run server, create threads
    while(runServer)
    {
      clientSock = serverSock.accept();

      System.out.println("[+]Connection successful.");
      System.out.println("[+]Listening for input");

      new Thread(new EchoThread(clientSock)).start();
    }

    // Close sockets
    clientSock.close();
    serverSock.close();
  }
}
