import java.awt.*;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import static java.lang.Math.abs;

public class RequestThread extends Thread implements DEFINE
{
    /** @OVERVIEW: 对于每一个请求的处理，包括扫描接单的出租车和派单等;
     * @INHERIT: Thread;
     * @INVARIANT: ID, srcPoint, dstPoint;
     */
    private int ID;
    private Point srcPoint;
    private Point dstPoint;
    private LinkedList<Integer> taxis = new LinkedList<>();
    long startTime;

    public boolean repOK()
    {
        if (ID >= 0 && srcPoint != null && dstPoint != null && taxis != null && startTime > 0)
            return true;
        else
            return false;
    }

    /** @REQUIRES:  requests.equals(request is a valid request already exists);
     * @MODIFIES: this;
     * @EFFECTS: None;
     */
    public RequestThread(Request request)
    {
        this.srcPoint = request.getSrcPoint();
        this.dstPoint = request.getDstPoint();
        this.ID = request.getID();
    }

    /** @REQUIRES:  None;
     * @MODIFIES: this;
     * @EFFECTS: None;
     */
    public void run()
    {
        startTime = System.currentTimeMillis();
        Main.GUI.RequestTaxi(srcPoint, dstPoint);
        if (ID == 0)
            Main.fileWriter.setStartTime(startTime);
        Main.fileWriter.recordRequest(ID, startTime, srcPoint, dstPoint);
        while(true)
        {
            //扫描出租车状态
            findValidTaxis();
            if (System.currentTimeMillis() - startTime >= 7500)
            {
                //进行派单
                sendRequest();
                return;
            }
        }
    }

    /** @REQUIRES:  None;
     * @MODIFIES: None;
     * @EFFECTS: \result.equals(将请求分配给距离最近的最高信用的出租车，返回分配成功与否的结果);
     */
    public boolean sendRequest()
    {
        if (taxis.size() != 0)
        {
//            System.out.printf("Request%d: All Valid Taxis:\n", ID);
            Collections.sort(taxis, new CreditComparator());
            for(int taxi:taxis)
            {
                System.out.printf("Request%d: Taxi%d Credit:%d\n", ID, taxi, Main.taxiQueue.getTaxiCredit(taxi));
                Main.fileWriter.recordTaxi(ID, taxi);
            }
            for (int i = 0; i < taxis.size(); i++)
            {
                if (Main.taxiQueue.getTaxiStatus(taxis.get(i)) == WAITING)
                {
                    for (int j = i; j < taxis.size(); j++)
                    {
                        if (Main.taxiQueue.getTaxiCredit(taxis.get(j)) < Main.taxiQueue.getTaxiCredit(taxis.get(i)))
                            break;
                        if (Main.taxiQueue.getTaxiStatus(taxis.get(j)) == WAITING)
                            if (GUIGv.m.getDis(Main.taxiQueue.getTaxiPosition(taxis.get(j)), srcPoint, Main.taxiQueue.getTaxiType(taxis.get(j))) < GUIGv.m.getDis(Main.taxiQueue.getTaxiPosition(taxis.get(i)), srcPoint, Main.taxiQueue.getTaxiType(taxis.get(i))))
                                i = j;
                    }
                    Main.taxiQueue.setTaxiRequest(taxis.get(i), this.ID);
                    Main.fileWriter.recordGetRequest(taxis.get(i), ID, System.currentTimeMillis());
                    return true;
                }
            }
        }
        System.out.printf("Request%d: No valid taxis.\n", ID);
        return false;
    }

    /** @REQUIRES:  None;
     * @MODIFIES: None;
     * @EFFECTS: \result.equals(寻找可抢单的出租车，记录车辆编号并增加其信用度);
     * @THREAD_EFFECTS: \locked();
     */
    public synchronized void findValidTaxis()
    {

        for (int i = 0; i < 100; i++)
        {
            if (!taxis.contains(i) && Main.taxiQueue.getTaxiStatus(i) == WAITING)
            {
                Point position = Main.taxiQueue.getTaxiPosition(i);
                if ( abs(position.x - srcPoint.x) <= 2 && abs(position.y - srcPoint.y) <= 2 )
                {
                    taxis.addLast(i);
                    Main.taxiQueue.addTaxiCredit(i, 1);
                }
            }
        }
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
        return startTime;
    }
}
