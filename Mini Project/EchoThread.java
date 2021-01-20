import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoThread implements Runnable 
{
    Socket csocket;

    EchoThread(Socket csocket) 
    {
        this.csocket = csocket;
    }
    public void run()
    {
      StateMachine currentState = new StateMachine();

      char charFromClient = ' ';

       try 
      {
         PrintStream toClient = new PrintStream(csocket.getOutputStream());
         
         while(!currentState.isAtFinalState())
         {
            InputStreamReader fromClient = new InputStreamReader(csocket.getInputStream());

            BufferedReader reader = new BufferedReader(fromClient);

            charFromClient = (char)reader.read();

            toClient.println((char) charFromClient);

            currentState.updateState(charFromClient);
         }
         toClient.close();

         csocket.close();
      } 
      catch (IOException ex) 
      {
         System.out.println(ex);
      } 
    }
  }
