public class Stair
{
    private boolean[] upButton;
    private boolean[] downButton;

    public Stair()
    {
        upButton = new boolean[25];
        downButton = new boolean[25];
    }

    public boolean getUpLight(int floor)
    {
        return upButton[floor];
    }

    public boolean getDownLight(int floor)
    {
        return downButton[floor];
    }

    public void lightOnUpButton(int floor)
    {
        upButton[floor] = true;
    }

    public void lightOnDownButton(int floor)
    {
        downButton[floor] = true;
    }

    public void lightOffUpButton(int floor)
    {
        upButton[floor] = false;
    }

    public void lightOffDownButton(int floor)
    {
        downButton[floor] = false;
    }
}
