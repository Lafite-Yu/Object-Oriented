import java.sql.Time;

public class Request
{
    private int requestType; // 0: ER 1:FR
    private int direction; // -1:DOWN 1:UP
    private int dstFloor;
    private int elevatorNum; // 1/2/3
    private long time;
    private int flag; // 0:Valid
    private long startTime;

    public Request(int type, int floor, int dirOrNum, long time, long startTime)
    {
        this.requestType = type;
        this.dstFloor = floor;
        this.time = time;
        this.flag = 0;
        this.elevatorNum = 0;
        this.direction = 0;
        this.startTime = startTime;

        if (type == 0) // ER
            this.elevatorNum = dirOrNum;
        else // FR
            this.direction = dirOrNum;

        System.out.println(toString());
    }

    public Request(int end)
    {
        this.requestType = end;
    }

    public int getRequestType()
    {
        return requestType;
    }

    public int getDirection()
    {
        return direction;
    }

    public int getDstFloor()
    {
        return dstFloor;
    }

    public int getElevatorNum()
    {
        return elevatorNum;
    }

    public long getTime()
    {
        return time;
    }

    public int getFlag()
    {
        return flag;
    }

    public void setFlag(int flag)
    {
        this.flag = flag;
    }

    public String toString()
    {
        String str = new String();
//        String.format("type:%s floor:%d direction:%s  elevatorNum:%d time:%.1f", requestType == 0 ? "ER" : "FR", dstFloor, direction == -1 ? "DOWN" :(direction == 1 ? "UP" : "NULL"),elevatorNum, (double)(time-startTime)/1000);
        if (requestType == 0) // ER
        {
            str = String.format("ER,#%d,%d,%.1f", elevatorNum, dstFloor, (double)(getTime()-startTime)/1000);
        } else if (requestType == 1) // FR
        {
            str = String.format("FR,%d,%s,%.1f", dstFloor, direction == -1 ? "DOWN" :(direction == 1 ? "UP" : "NULL"), (double)(getTime()-startTime)/1000);
        }
        return str;
    }
}
