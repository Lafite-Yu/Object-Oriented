import java.awt.*;
import java.util.LinkedList;
import java.util.ListIterator;

public class VIPTaxi extends Taxi implements Iterable<HistoryRecord>
{
    /** @OVERVIEW: VIP出租车线程，每个线程负责一辆VIP出租车的移动;
     * 与普通出租车相比，除了增加迭代和记录以外，没有增加新的内容;
     * @INHERIT: Taxi, Thread;
     * @INVARIANT: ID;
     */
    private LinkedList<HistoryRecord> historyPath;

    /**
     * @REQUIRES: 0 <= i < 100;
     * @MODIFIES: this;
     * @EFFECTS: None;
     */
    public VIPTaxi(int i)
    {
        super(i);
        historyPath = new LinkedList<>();
    }

    /**
     * @REQUIRES: 0 <= nextPoint <= 79*80+79;
     *             nextPoint.isValid(nextPoint是出租车下一步移动的目标点，需要满足与上一步的目标点为1、输入的地图文件中两点连通);
     * @MODIFIES: this.historyPath;
     * @EFFECTS: \result.equals(move(int nextPoint, boolean VIPTaxi) ==> historyPath.recordPath);
     */
    protected boolean move(int nextPoint)
    {
        boolean result = super.move(nextPoint, true);
        if (result)
        {
            Point newPoint = new Point(nextPoint/80, nextPoint%80);
            historyPath.getLast().addMovePath(newPoint);
        }
        return result;
    }

    /**
     * @REQUIRES: None;
     * @MODIFIES: None;
     * @EFFECTS: None;
     */
    protected void randomMove()
    {
        super.randomMove(true);
    }

    /**
     * @REQUIRES: None;
     * @MODIFIES: None;
     * @EFFECTS: None;
     */
    public void getMovePath()
    {
        super.getMovePath(true);
    }

    /**
     * @REQUIRES: requestNum.equals(requestNum is a valid index of a request already exists);
     * @MODIFIES: this;
     * @EFFECTS: \result.equals(historyPath.recordNew);
     */
    public void setRequest(int requestNum)
    {
        super.setRequest(requestNum);
        this.historyPath.addLast(new HistoryRecord(requestNum, System.currentTimeMillis()-Main.fileWriter.startTime, new Point(position.x, position.y), Main.requestHandler.getTime(requestNum)-Main.fileWriter.startTime, srcPoint, dstPoint));
    }

    /**
     * @REQUIRES: None;
     * @MODIFIES: None;
     * @EFFECTS: \result.equals(historyPath的双向迭代器);
     */
    public ListIterator<HistoryRecord> iterator()
    {
        return historyPath.listIterator();
    }

}
