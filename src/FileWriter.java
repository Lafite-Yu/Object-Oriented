import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.LinkedList;

import static java.lang.Thread.sleep;

public class FileWriter
{
    private PrintStream ps;
    private PrintStream sps;
    private long startTime;

    /** @REQUIRES: None;
     * @MODIFIES: this;
     * @EFFECTS: \result.equals(构造);
     */
    public FileWriter()
    {
        try
        {
            File file = new File("detail.txt");
            if (!file.exists())
                file.createNewFile();
            FileOutputStream fop = new FileOutputStream(file);
            this.ps = new PrintStream(fop);
            File sFile = new File("summary.txt");
            if (!sFile.exists())
                sFile.createNewFile();
            FileOutputStream sfop = new FileOutputStream(sFile);
            this.sps = new PrintStream(sfop);

        }catch (Exception e1)
        {
//            e1.printStackTrace();
        }
    }

    /** @REQUIRES: \result.equals(time是一个有效的系统时间);
     * @MODIFIES:  this;
     * @EFFECTS: \result.equals(设定startTime);
     */
    public void setStartTime(long time)
    {
        this.startTime = time;
    }

    /** @REQUIRES: 0 <= requestNum < Main.requestHandler.threads.length;
     *               time.equals(time是一个有效的系统时间);
     *               0 <= src.x, src.y, dst.x, dst.y <= 79;
     * @MODIFIES: None;
     * @EFFECTS: \result.equals(将请求信息写入文件);
     * @THREAD_EFFECTS: \locked(\this);
     */
    public void recordRequest(int requestNum, long time, Point src,Point dst)
    {
        synchronized (FileWriter.class)
        {
            try
            {
                ps.printf("Request:%d:\tTime:%d From:(%d,%d) To(%d,%d)\n", requestNum, time-startTime, src.x, src.y, dst.x, dst.y);
            } catch (Exception e) {}
        }
    }

    /** @REQUIRES: 0 <= requestNum < Main.requestHandler.threads.length;
     *               0 <= taxiNum < 100;
     * @MODIFIES: this;
     * @EFFECTS: \result.equals(将所有参与抢单的出租车信息写入文件（写入的位置是派单时刻的出租车位置）);
     * @THREAD_EFFECTS: \locked(\this);
     */
    public void recordTaxi(int requestNum, int taxiNum)
    {
        synchronized (FileWriter.class)
        {
            try
            {
                ps.printf("Request%d:\tTaxi%d Position:(%d,%d) Status:%d Credit:%d\n", requestNum, taxiNum, Main.taxiQueue.getTaxiPosition(taxiNum).x, Main.taxiQueue.getTaxiPosition(taxiNum).y, Main.taxiQueue.getTaxiStatus(taxiNum), Main.taxiQueue.getTaxiCredit(taxiNum));
            } catch (Exception e) {}
        }
    }

    /** @REQUIRES: 0 <= requestNum < Main.requestHandler.threads.length;
     *               0 <= taxiNum < 100;
     *               time.equals(time是一个有效的系统时间);
     *               taxi_status.equals(出租车被派单时）;
     * @MODIFIES: this;
     * @EFFECTS: \result.equals(将被派单的出租车信息写入文件);
     * @THREAD_EFFECTS: \locked(\this);
     */
    public void recordGetRequest(int taxiNum, int requestNum, long time)
    {
        synchronized (FileWriter.class)
        {
            try
            {
                ps.printf("Taxi%d:\tGet Request%d Position:(%d,%d) Time:%d\n", taxiNum, requestNum, Main.taxiQueue.getTaxiPosition(taxiNum).x, Main.taxiQueue.getTaxiPosition(taxiNum).y, time-startTime);
            } catch (Exception e) {}
        }
    }

    /** @REQUIRES:  0 <= taxiNum < 100;
     *               time.equals(time是一个有效的系统时间);
     * @MODIFIES: this;
     * @EFFECTS: \result.equals(将被派单的出租车的运动信息写入文件);
     * @THREAD_EFFECTS: \locked(\this);
     */
    public void recordMovement(int taxiNum, long time)
    {
        synchronized (FileWriter.class)
        {
            try
            {
                ps.printf("Taxi%d:\tPosition:(%d,%d) Time:%d\n", taxiNum, Main.taxiQueue.getTaxiPosition(taxiNum).x, Main.taxiQueue.getTaxiPosition(taxiNum).y, time-startTime);
            } catch (Exception e) {}
        }
    }

    /** @REQUIRES:  0 <= taxiNum < 100;
     *               0 <= requestNum < Main.requestHandler.threads.length;
     *               time.equals(time是一个有效的系统时间);
     *               taxi_status.equals(被派单的出租车到达请求目的点时);
     * @MODIFIES: this;
     * @EFFECTS: \result.equals(将被派单的出租车的运动信息写入文件);
     * @THREAD_EFFECTS: \locked(\this);
     */
    public void recordReachSrc(int taxiNum, int requestNum, long time)
    {

        synchronized (FileWriter.class)
        {
            try
            {
                ps.printf("Taxi%d:\tReached SRC point. Request%d Position:(%d,%d) Time:%d\n", taxiNum, requestNum, Main.taxiQueue.getTaxiPosition(taxiNum).x, Main.taxiQueue.getTaxiPosition(taxiNum).y, time-startTime);
            } catch (Exception e) {}
        }
    }

    /** @REQUIRES:  0 <= taxiNum < 100;
     *                0 <= requestNum < Main.requestHandler.threads.length;
     *                time.equals(time是一个有效的系统时间);
     *                taxi_status.equals(被派单的出租车到达请求目的点时);
     * @MODIFIES: None;
     * @EFFECTS: \result.equals(将被派单的出租车的运动信息写入文件);
     * @THREAD_EFFECTS: \locked(\this);
     */
    public void recordReachDst(int taxiNum, int requestNum, long time)
    {
        synchronized (FileWriter.class)
        {
            try
            {
                ps.printf("Taxi%d:\tReached DST point. Request%d Position:(%d,%d) Time:%d\n", taxiNum, requestNum, Main.taxiQueue.getTaxiPosition(taxiNum).x, Main.taxiQueue.getTaxiPosition(taxiNum).y, time-startTime);
            } catch (Exception e) {}
        }
    }

    /** @REQUIRES:  0 <= taxiNum < 100;
     * @MODIFIES: this;
     * @EFFECTS: \result.equals(将指定的出租车的信息写入文件);
     * @THREAD_EFFECTS: \locked();
     */
    public synchronized void taxiCheck(int taxiNum)
    {
        try
        {
            sps.printf("Taxi%d:\tTime:%d Position:(%d,%d) Status:%d\n", taxiNum, System.currentTimeMillis()-startTime, Main.taxiQueue.getTaxiPosition(taxiNum).x, Main.taxiQueue.getTaxiPosition(taxiNum).y, Main.taxiQueue.getTaxiStatus(taxiNum));
        } catch (Exception e) {}
    }

    /** @REQUIRES:  status == READY || status == WAITING || status == SERVING || status == STOP;
     * @MODIFIES: this;
     * @EFFECTS: \result.equals(将指定的状态的所有出租车的编号写入文件）;
     *            (\\all int status; status == READY || status == WAITING || status == SERVING || status == STOP; taxi[i].status == status);
     * @THREAD_EFFECTS: \locked();
     */
    public synchronized LinkedList<Integer> statusCheck(int status)
    {
        long time = System.currentTimeMillis() - startTime;
        LinkedList<Integer> taxis = new LinkedList<>();
        sps.printf("All taxis in status %d at the time of %d:\n", status, time);
        try
        {
            for (int i = 0; i < 100; i++)
            {
                if (Main.taxiQueue.getTaxiStatus(i) == status)
                {
                    taxis.addLast(i);
                    sps.printf("\tTaxi%d.\n", i);
                }
            }
        } catch (Exception e) {} finally
        {
            return taxis;
        }
    }
}
