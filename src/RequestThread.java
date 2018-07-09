import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;

import java.awt.*;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import static java.lang.Math.abs;

public class RequestThread extends Thread implements DEFINE
{
    private int ID;
    private Point srcPoint;
    private Point dstPoint;
    private LinkedList<Integer> taxis = new LinkedList<>();
    long startTime;

    public RequestThread(Request request)
    {
        this.srcPoint = request.getSrcPoint();
        this.dstPoint = request.getDstPoint();
        this.ID = request.getID();
    }

    public void run()
    {
        startTime = System.currentTimeMillis();
        Main.GUI.RequestTaxi(srcPoint, dstPoint);
        if (ID == 0)
            Main.fileWriter.setStartTime(startTime);
        Main.fileWriter.recordRequest(ID, startTime, srcPoint, dstPoint);
        //BFS
        BFSThread BFSthread = new BFSThread(srcPoint, dstPoint);
        BFSthread.start();
        while(true)
        {
            //扫描出租车状态
            findValidTaxis();
            if (System.currentTimeMillis() - startTime >= 3000)
            {
                //进行派单
                sendRequest();
                return;
            }
        }
    }

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
                            if (Main.map.getDistant(Main.taxiQueue.getTaxiPosition(taxis.get(j)), srcPoint) < Main.map.getDistant(Main.taxiQueue.getTaxiPosition(taxis.get(i)), srcPoint))
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

    public synchronized void findValidTaxis()
    {
        for (int i = 0; i < 100; i++)
        {
            if (!taxis.contains(i))
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

    public Point getSrcPoint()
    {
        return srcPoint;
    }

    public Point getDstPoint()
    {
        return dstPoint;
    }
}
