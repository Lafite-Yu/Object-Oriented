import java.awt.*;

public class Request implements DEFINE
{
    private int ID;
    private Point srcPoint;
    private Point dstPoint;
    private long time;

    public Request(int num, Point srcPoint, Point dstPoint, long time)
    {
        this.ID = num;
        this.srcPoint = srcPoint;
        this.dstPoint =  dstPoint;
        this.time = time;
        System.out.printf("New request:from(%d, %d) to:(%d, %d) time:%d\n", (int)srcPoint.getX(), (int)srcPoint.getY(), (int)dstPoint.getX(), (int)dstPoint.getY(), time);
    }

    public boolean isSame(Request request)
    {
        if ( request.getTime()/100 == time/100 )
            if (request.getSrcPoint().getX() == srcPoint.getX() && request.getSrcPoint().getY() == srcPoint.getY())
                if (request.getDstPoint().getX() == dstPoint.getX() && request.getDstPoint().getY() == dstPoint.getY())
                    return true;
        return false;
    }

    public boolean isDelete(long inTime)
    {
        if ((inTime - time)/1000 > 1)
            return true;
        else
            return false;
    }

    public Point getSrcPoint()
    {
        return srcPoint;
    }

    public Point getDstPoint()
    {
        return dstPoint;
    }

    public long getTime()
    {
        return time;
    }

    public int getID()
    {
        return ID;
    }
}
