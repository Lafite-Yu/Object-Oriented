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

    public void setStartTime(long time)
    {
        this.startTime = time;
    }

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

    public synchronized void taxiCheck(int taxiNum)
    {
        try
        {
            sps.printf("Taxi%d:\tTime:%d Position:(%d,%d) Status:%d\n", taxiNum, System.currentTimeMillis()-startTime, Main.taxiQueue.getTaxiPosition(taxiNum).x, Main.taxiQueue.getTaxiPosition(taxiNum).y, Main.taxiQueue.getTaxiStatus(taxiNum));
        } catch (Exception e) {}
    }

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
