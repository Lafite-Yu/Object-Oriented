import java.awt.*;

public class Request implements DEFINE
{
    /** @OVERVIEW: 读入的每一个请求及相对应的get方法、判断相同等等;
     * @INHERIT: None
     * @INVARIANT: ID, srcPoint, dstPoint, time;
     */
    private int ID;
    private Point srcPoint;
    private Point dstPoint;
    private long time;

    public boolean repOK()
    {
        if (ID >= 0 && srcPoint != null && dstPoint != null && time > 0)
            return true;
        else
            return false;
    }

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
        System.out.printf("New request: No.%d from(%d, %d) to:(%d, %d) time:%d\n", num, (int)srcPoint.getX(), (int)srcPoint.getY(), (int)dstPoint.getX(), (int)dstPoint.getY(), time);
    }

    /** @REQUIRES:  requests.equals(request是已有的一个请求);
     * @MODIFIES: None;
     * @EFFECTS: \result.equals(abs(this.time - request.time) <= 100ms && this.srcPoint.equals(request.srcPoint) && this.dstPoint.equals(request.dstPoint));
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
     * @EFFECTS: \result.equals((inTime - time)/1000 > 1);
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
