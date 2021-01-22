public class StateMachine
{
    private boolean stateQ, stateU, stateI, stateT;

    // StateMachine Constructor
    StateMachine()
    {
        stateQ = false;
        stateU = false;
        stateI = false;
        stateT = false;
    }

    // Function to check whether client input contains keyword 'quit'
    // changes letter "state" based on user input
    public void updateState(char inputChar)
    {
        if( isLetter(inputChar) )
        {
            if(inputChar == 'q')
            {
                stateQ = true;
                stateU = false;
                stateI = false;
                stateT = false;
            }
            else if(stateQ && inputChar == 'u')
            {
                stateQ = false;
                stateU = true;
            }
            else if(stateU && inputChar == 'i')
            {
                stateU = false;
                stateI = true;
            }
            else if(stateI && inputChar == 't')
            {
                stateI = false;
                stateT = true;
            }
            else
            {
                stateQ = false;
                stateU = false;
                stateI = false;
                stateT = false;
            }
        }
    }

    // Helper function to check if client has entered letter "T"
    public boolean isAtFinalState()
    {
        return stateT;
    }

    // Helper function that checks if char is a valid letter
    private boolean isLetter(char inputChar)
    {
        return (inputChar >= 'a' && inputChar <= 'z') ||
               (inputChar >= 'A' && inputChar <= 'Z');
    }
}
