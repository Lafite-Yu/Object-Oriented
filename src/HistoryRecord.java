import java.awt.*;
import java.util.LinkedList;

public class HistoryRecord implements DEFINE
{
    /** @OVERVIEW: 可追踪出租车处理过的每一次请求，存储了请求编号、请求开始处理的时间、请求的发出点、目的点、接到请求的时间、位置信息、处理请求的过程中经过的每一个地图点等信息;
     * @INHERIT: None;
     * @INVARIANT: requestNum, requestTime, requestPosition;
     */
    private int requestNum;
    private long requestTime;
    private Point requestPosition;
    private LinkedList<Point> historyPath;
    private Point srcPoint;
    private Point dstPoint;
    private long requestProduceTime;

    public boolean repOK()
    {
        if (requestNum>=0 && requestNum <= MAX_REQUESTS && requestPosition != null && requestTime > 0)
        {
            return true;
        } else
            return false;
    }

    /** @REQUIRES:  num.isValid(0 <= num <= 300, num是一个有效的请求序号);
     *               time.isValid(time是被派单的出租车调度系统时间);
     *               position.isValid(position是被派单时出租车所处的位置);
     *@MODIFIES: this;
     *@EFFECTS: None;
     */
    public HistoryRecord(int num, long time, Point position, long produceTime, Point src, Point dst)
    {
        this.requestNum = num;
        this.requestTime = time;
        this.requestPosition = position;
        this.historyPath = new LinkedList<>();
        this.requestProduceTime = produceTime;
        this.srcPoint = new Point(src.x, src.y);
        this.dstPoint = new Point(dst.x, dst.y);
    }
    /** @REQUIRES:  nextPoint.isValid(nextPoint是出租车下一步移动的目标点，需要满足与上一步的目标点为1、输入的地图文件中两点连通);
     * @MODIFIES: this.historyPath;
     * @EFFECTS: None;
     * @THREAD_EFFECTS: \locked(\this);
     */
    public void addMovePath(Point nextPoint)
    {
        synchronized (historyPath)
        {
            this.historyPath.addLast(nextPoint);
        }
    }
    /** @REQUIRES:  None;
     * @MODIFIES: System.out;
     * @EFFECTS: result.equals(输出overview中提到的被记录的信息)
     * @THREAD_EFFECTS: \locked(\this);
     */
    public void printHistory()
    {
        synchronized (historyPath)
        {
            System.out.println(LINE + "HANDLED REQUEST" + LINE);
            System.out.printf("Request Number: %d\n", requestNum);
            System.out.printf("Time when request produced: %d\n", requestProduceTime);
            System.out.printf("SRC Point: (%d,%d)\n", srcPoint.x, srcPoint.y);
            System.out.printf("DST Point: (%d,%d)\n", dstPoint.x, dstPoint.y);
            System.out.printf("Position when get request: (%d,%d)\n", requestPosition.x, requestPosition.y);
            System.out.printf("Time when get request: %d\n", requestTime);
            for (Point aPoint: historyPath)
                System.out.printf("(%d,%d)\n", aPoint.x, aPoint.y);
        }
    }
}
