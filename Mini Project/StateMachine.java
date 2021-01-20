public class StateMachine
{
    private boolean stateQ, stateU, stateI, stateT;

    StateMachine()
    {
        stateQ = false;
        stateU = false;
        stateI = false;
        stateT = false;
    }
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
    public boolean isAtFinalState()
    {
        return stateT;
    }
    private boolean isLetter(char inputChar)
    {
        return (inputChar >= 'a' && inputChar <= 'z') ||
               (inputChar >= 'A' && inputChar <= 'Z');
    }
}