import java.awt.*;

public class Request implements DEFINE
{
    private int ID;
    private Point srcPoint;
    private Point dstPoint;
    private long time;

    /** @REQUIRES:  id.equals(id是一个有效的request编号);
     *                   0 <= srcPoint.x, srcPoint.y, dstPoint.x, dstPoint.y <= 79;
     *                   time.equals(time是一个有效的系统时间);
     *@MODIFIES: this;
     *@EFFECTS: None;
     */
    public Request(int num, Point srcPoint, Point dstPoint, long time)
    {

        this.ID = num;
        this.srcPoint = srcPoint;
        this.dstPoint =  dstPoint;
        this.time = time;
        System.out.printf("New request:from(%d, %d) to:(%d, %d) time:%d\n", (int)srcPoint.getX(), (int)srcPoint.getY(), (int)dstPoint.getX(), (int)dstPoint.getY(), time);
    }

    /** @REQUIRES:  requests.equals(request是已有的一个请求);
     * @MODIFIES: None;
     * @EFFECTS: abs(this.time - request.time) <= 100ms && this.srcPoint.equals(request.srcPoint) && this.dstPoint.equals(request.dstPoint);
     */
    public boolean isSame(Request request)
    {

        if ( request.getTime()/100 == time/100 )
            if (request.getSrcPoint().getX() == srcPoint.getX() && request.getSrcPoint().getY() == srcPoint.getY())
                if (request.getDstPoint().getX() == dstPoint.getX() && request.getDstPoint().getY() == dstPoint.getY())
                    return true;
        return false;
    }

    /** @REQUIRES:  inTime.equals(inTime is a valid system time);
     * @MODIFIES: None;
     * @EFFECTS: (inTime - time)/1000 > 1;
     */
    public boolean isDelete(long inTime)
    {

        if ((inTime - time)/1000 > 1)
            return true;
        else
            return false;
    }

    /** @REQUIRES:  None;
     * @MODIFIES: None;
     * @EFFECTS: None;
     */
    public Point getSrcPoint()
    {
        return srcPoint;
    }

    /** @REQUIRES:  None;
     * @MODIFIES: None;
     * @EFFECTS: None;
     */
    public Point getDstPoint()
    {
        return dstPoint;
    }

    /** @REQUIRES:  None;
     * @MODIFIES: None;
     * @EFFECTS: None;
     */
    public long getTime()
    {
        return time;
    }

    /** @REQUIRES:  None;
     * @MODIFIES: None;
     * @EFFECTS: None;
     */
    public int getID()
    {
        return ID;
    }
}
