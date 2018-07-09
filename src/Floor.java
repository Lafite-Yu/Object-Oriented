public class Floor
{
    private boolean[] upButton;
    private boolean[] downButton;

    public Floor()
    {
        upButton = new boolean[10];
        downButton = new boolean[10];
    }

    public boolean getUpLight(int floor)
    {
        return upButton[floor-1];
    }

    public boolean getDownLight(int floor)
    {
        return downButton[floor-1];
    }

    public boolean lightOnUpButton(int floor)
    {
        upButton[floor-1] = true;
        return  true;
    }

    public boolean lightOnDownButton(int floor)
    {
        downButton[floor-1] = true;
        return  true;
    }

    public boolean lightOffUpButton(int floor)
    {
        upButton[floor-1] = false;
        return  false;
    }

    public boolean lightOffDownButton(int floor)
    {
        downButton[floor-1] = false;
        return  false;
    }
}
