import com.sun.org.apache.regexp.internal.RE;

import java.awt.*;
import java.util.Random;

public class Taxi extends Thread implements DEFINE
{
    private int ID;
    private long startTime;
    private Point position;
    private int status = STOP;
    private int credit = 0;

    private boolean requestMark = false;
    private int requestNum = 0;
    private boolean reachedSrc = false;
    private Point srcPoint= new Point();
    private Point dstPoint = new Point();

    public Taxi(int i)
    {
        this.ID = i;
        this.position = new Point(new Random().nextInt(80), new Random().nextInt(80));
        Main.GUI.SetTaxiStatus(ID, position, status);
//        System.out.printf("\tTaxi %d position:(%d, %d)\n", this.ID, (int) position.getX(), (int) position.getY());
    }

    public void run()
    {
        this.status = WAITING;
        while(true)
        {
            try
            {
                sleep(200);
//                if (ID == 0)
//                    System.out.println(System.currentTimeMillis());
                if (requestMark)
                {
                    // 接单
//                    System.out.println(System.currentTimeMillis());
                    aimMove();
                } else
                {
                    randomMove();
                    Main.GUI.SetTaxiStatus(ID, position, status);
                    sleep20();
                }
            } catch (Exception e) {}
        }
    }

    private void aimMove()
    {
        try
        {
            if (!reachedSrc) // 运行到SRC
            {
                // 沿到DST的BST移动
//                System.out.println(Main.map.getShortestMove(position, srcPoint));
                move(Main.map.getShortestMove(position, srcPoint));
                Main.GUI.SetTaxiStatus(ID, position, status);
                Main.fileWriter.recordMovement(ID, System.currentTimeMillis());
                if(position.x == srcPoint.x && position.y == srcPoint.y)
                {
                    reachedSrc = true;
                    System.out.printf("Taxi%d reached SRC point of Request%d\n", ID, requestNum);
                    Main.fileWriter.recordReachSrc(ID, requestNum, System.currentTimeMillis());
                    status = STOP;
                    Main.GUI.SetTaxiStatus(ID, position, status);
                    sleep(1000);
                    status = SERVING;
                    Main.GUI.SetTaxiStatus(ID, position, status);
                }
            } else // 运行到DST
            {
                // 沿到DST的BST移动
                move(Main.map.getShortestMove(position, dstPoint));
                Main.GUI.SetTaxiStatus(ID, position, status);
                Main.fileWriter.recordMovement(ID, System.currentTimeMillis());
                if(position.x == dstPoint.x && position.y == dstPoint.y)
                {
                    requestMark = false;
                    System.out.printf("Taxi%d reached DST point of Request%d\n", ID, requestNum);
                    addCredit(3);
                    Main.fileWriter.recordReachDst(ID, requestNum, System.currentTimeMillis());
                    sleep(100);
                    status = STOP;
                    Main.GUI.SetTaxiStatus(ID, position, status);
                    sleep(900);
                    status = WAITING;
                    Main.GUI.SetTaxiStatus(ID, position, status);
                    startTime = System.currentTimeMillis();
                }
            }
        } catch (Exception e) {}
    }

    private void  randomMove()
    {
        while(true)
        {
            int move = new Random().nextInt(4);
            switch (move)
            {
                case UP:
                {
                    if (Main.map.downConnect(new Point(position.x-1, position.y)))
                    {
                        position.setLocation(position.x-1, position.y);
                        return ;
                    }
                    break;
                }
                case DOWN:
                {
                    if (Main.map.downConnect(position))
                    {
                        position.setLocation(position.x+1, position.y);
                        return;
                    }
                    break;
                }
                case LEFT:
                {
                    if (Main.map.rightConnect(new Point(position.x, position.y-1)))
                    {
                        position.setLocation(position.x, position.y-1);
                        return;
                    }
                    break;
                }
                case RIGHT:
                {
                    if (Main.map.rightConnect(position))
                    {
                        position.setLocation(position.x, position.y+1);
                        return;
                    }
                    break;
                }
            }
        }
    }

    private void move(int direction)
    {
        switch (direction)
        {
            case UP:
            {
                position.setLocation(position.x-1, position.y);
                return ;
            }
            case DOWN:
            {
                position.setLocation(position.x+1, position.y);
                return;
            }
            case LEFT:
            {
                position.setLocation(position.x, position.y-1);
                return;
            }
            case RIGHT:
            {
                position.setLocation(position.x, position.y+1);
                return;
            }
            default:
            {
                System.out.println("Direction Error.");
                return;
            }
        }
    }

    private void sleep20()
    {
        try
        {
            long time = System.currentTimeMillis();
            if (time - startTime >= 20000)
            {
                this.status = STOP;
                startTime = time;
                Main.GUI.SetTaxiStatus(ID, position, status);
                sleep(1000);
                Main.GUI.SetTaxiStatus(ID, position, status);
                this.status = WAITING;
            }
        } catch (Exception e) { }
    }

    public Point getPosition()
    {
        return position;
    }

    public int getCredit()
    {
        return credit;
    }

    public int getStatus()
    {
        return status;
    }

    public void setRequest(int requestNum)
    {
        this.requestMark = true;
        this.requestNum = requestNum;
        this.status = READY;
        srcPoint = Main.requestHandler.getSrcPoint(requestNum);
        dstPoint = Main.requestHandler.getDstPoint(requestNum);
        System.out.printf("SRC: (%d,%d)\n", srcPoint.x, srcPoint.y);
        Main.GUI.SetTaxiStatus(ID, position, status);
        System.out.printf("Taxi%d get Request%d\n", ID, requestNum);
    }

    public void addCredit(int c)
    {
        this.credit += c;
    }
}
