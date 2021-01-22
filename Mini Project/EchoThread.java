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
       // Initialize State Machine 
      StateMachine currentState = new StateMachine();

      // Initialize empty string for client input
      char charFromClient = ' ';


       try
      {
         PrintStream toClient = new PrintStream(csocket.getOutputStream());

         // While the client has not entered letter "quit",
         // Read input from client, if char is a valid letter
         // print the char to terminal
         while(!currentState.isAtFinalState())
         {
            InputStreamReader fromClient = new InputStreamReader(csocket.getInputStream());

            BufferedReader reader = new BufferedReader(fromClient);

            charFromClient = (char)reader.read();

            toClient.println();

            if(isLetter(charFromClient))
            {
               toClient.println((char) charFromClient);
            }

            currentState.updateState(charFromClient);
         }

         // Close connections
         toClient.close();
         csocket.close();
      }
      catch (IOException ex)
      {
         System.out.println(ex);
      }
    }
    
    // Helper function that checks if char is a valid letter
    private boolean isLetter(char inputChar)
    {
        return (inputChar >= 'a' && inputChar <= 'z') ||
               (inputChar >= 'A' && inputChar <= 'Z');
    }
  }
