public class Elevator
{
    private boolean[] light;

    public Elevator()
    {
        light = new boolean[10];
    }

    public boolean getLight(int floor)
    {
        return light[floor-1];
    }

    public boolean lightOn(int floor)
    {
        light[floor-1] = true;
        return true;
    }

    public boolean lightOff(int floor)
    {
        light[floor-1] = false;
        return false;
    }
}
