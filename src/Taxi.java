import com.sun.org.apache.regexp.internal.RE;

import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

public class Taxi extends Thread implements DEFINE
{
    private int ID;
    private long startTime;
    private Point position;
    private int status = WAITING;
    private int credit = 0;

    private boolean requestMark = false;
    private int requestNum = 0;
    private boolean reachedSrc = false;
    private Point srcPoint= new Point();
    private Point dstPoint = new Point();
    private LinkedList<Integer> movePath = new LinkedList<>();
    private long fakeTime = 0;

    /** @REQUIRES: 0 <= i < 100;
     * @MODIFIES: this;
     * @EFFECTS: None;
     */
    public Taxi(int i)
    {
        this.ID = i;
        this.position = new Point(new Random().nextInt(80), new Random().nextInt(80));
        Main.GUI.SetTaxiStatus(ID, position, status);
//        System.out.printf("\tTaxi %d position:(%d, %d)\n", this.ID, (int) position.getX(), (int) position.getY());
    }

    /** @REQUIRES: status == WAITING || status == STOP;
     *               credit.equals(credit is valid);
     *               0 <= x,y <= 79;
     * @MODIFIES: this;
     * @EFFECTS: None;
     */
    public void initTaxi(int status, int credit, int x, int y)
    {
        this.status = status;
        this.credit = credit;
        this.position.setLocation(x, y);
        Main.GUI.SetTaxiStatus(ID, position, status);
    }

    /** @REQUIRES: None;
     * @MODIFIES: this;
     * @EFFECTS: None;
     */
    public void run()
    {
        if (this.status == STOP)
        {
            Main.GUI.SetTaxiStatus(ID, position, status);
            try
            {
                sleep(1000);
            } catch (Exception e) {}
            this.status = WAITING;
//            Main.GUI.SetTaxiStatus(ID, position, status);
        }
        this.status = WAITING;
        this.startTime = System.currentTimeMillis();
        while(true)
        {
            try
            {
                sleep(500);
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

    /** @REQUIRES: None;
     * @MODIFIES: this;
     * @EFFECTS: \result.equals(接送乘客状态下的沿最短路径移动);
     */
    private void aimMove()
    {
        try
        {
            getMovePath();
            fakeTime = System.currentTimeMillis();
            while (true)
            {
                if (!reachedSrc) // 运行到SRC
                {
                    // 沿到DST的BST移动
//                System.out.println(Main.map.getShortestMove(position, srcPoint));
                    int nextPoint = movePath.remove(0);
                    sleep(500);
                    fakeTime += 500;
                    while (!move(nextPoint))
                    {
                        getMovePath();
                        nextPoint = movePath.remove(0);
                    }
                    Main.GUI.SetTaxiStatus(ID, position, status);
                    Main.fileWriter.recordMovement(ID, fakeTime);
                    if(position.x == srcPoint.x && position.y == srcPoint.y)
                    {
                        reachedSrc = true;
                        System.out.printf("Taxi%d reached SRC point of Request%d\n", ID, requestNum);
                        Main.fileWriter.recordReachSrc(ID, requestNum, fakeTime);
                        status = STOP;
                        Main.GUI.SetTaxiStatus(ID, position, status);
                        sleep(1000);
                        fakeTime += 1000;
                        status = SERVING;
                        Main.GUI.SetTaxiStatus(ID, position, status);
                    }
                } else // 运行到DST
                {
                    // 沿到DST的BST移动
                    int nextPoint = movePath.remove(0);
                    sleep(500);
                    fakeTime += 500;
                    while (!move(nextPoint))
                    {
//                        System.out.println("Not Connected.");
                        getMovePath();
                        nextPoint = movePath.remove(0);
                    }
                    Main.GUI.SetTaxiStatus(ID, position, status);
                    Main.fileWriter.recordMovement(ID, fakeTime);
                    if(position.x == dstPoint.x && position.y == dstPoint.y)
                    {
                        requestMark = false;
                        System.out.printf("Taxi%d reached DST point of Request%d\n", ID, requestNum);
                        addCredit(3);
                        Main.fileWriter.recordReachDst(ID, requestNum, fakeTime);
                        sleep(100);
                        fakeTime += 1000;
                        status = STOP;
                        Main.GUI.SetTaxiStatus(ID, position, status);
                        sleep(900);
                        status = WAITING;
                        Main.GUI.SetTaxiStatus(ID, position, status);
                        startTime = System.currentTimeMillis();
                        break;
                    }
                }
            }
        } catch (Exception e) {}
    }

    /** @REQUIRES: None;
     * @MODIFIES: this;
     * @EFFECTS: \result.equals(WAITING状态下出租车随机移动);
     */
    private void randomMove()
    {
        while(true)
        {
            int move = new Random().nextInt(4);
            switch (move)
            {
                case UP:
                {

                    if (GUIGv.m.isConnect(position.x*80+position.y, (position.x-1)*80+position.y))
                    {
                        position.setLocation(position.x-1, position.y);
                        return ;
                    }
                    break;
                }
                case DOWN:
                {
                    if (GUIGv.m.isConnect(position.x*80+position.y, (position.x+1)*80+position.y))
                    {
                        position.setLocation(position.x+1, position.y);
                        return;
                    }
                    break;
                }
                case LEFT:
                {
                    if (GUIGv.m.isConnect(position.x*80+position.y, position.x*80+(position.y-1)))
                    {
                        position.setLocation(position.x, position.y-1);
                        return;
                    }
                    break;
                }
                case RIGHT:
                {
                    if (GUIGv.m.isConnect(position.x*80+position.y, position.x*80+(position.y+1)))
                    {
                        position.setLocation(position.x, position.y+1);
                        return;
                    }
                    break;
                }
            }
        }
    }

    /** @REQUIRES: None;
     * @MODIFIES: None;
     * @EFFECTS: \result.equals(接单后获取出租车移动路径);
     */
    public void getMovePath()
    {
        movePath.clear();
        for (int nextPoint: GUIGv.m.getPath(position, srcPoint, dstPoint, reachedSrc))
        {
//            System.out.printf("(%d,%d)\n", nextPoint/80,nextPoint%80);
            movePath.addLast(nextPoint);
        }
        int first = movePath.remove(0);
        if (first != position.x*80+position.y)
        {
            System.out.printf("positon:(%d,%d) index0:(%d,%d)\n", position.x, position.y, first/80, first%80);
            System.out.println("FATAL ERROR: UNKNOWN ERROR");
//            gv.stay(2000);
            return;
        }
    }

    /** @REQUIRES: 0 <= nextPoint <= 79*80+79;
     * @MODIFIES: this;
     * @EFFECTS: GUIGv.m.isConnect(curPoint, nextPoint) ==> move, return true;
     *           !GUIGv.m.isConnect(curPoint, nextPoint) ==> return false;
     */
    private boolean move(int nextPoint)
    {
//        System.out.printf("(%d,%d) %s\n", nextPoint/80, nextPoint%80, GUIGv.m.isConnect(position.x*80+position.y, nextPoint) ? "y" : "n");
//        System.out.printf("%d: (%d,%d) (%d, %d) connect?:%s\n",fakeTime, position.x, position.y, nextPoint/80, nextPoint%80, GUIGv.m.isConnect(position.x*80+position.y, nextPoint), GUIGv.m.isConnect(41*80+36, 41*80+37) ? "connect" : "no");
        if (GUIGv.m.isConnect(position.x*80+position.y, nextPoint))
        {
            position.setLocation(nextPoint/80, nextPoint%80);
            return true;
        }
        else
            return false;
    }

    /** @REQUIRES: None;
     * @MODIFIES: None;
     * @EFFECTS: \reqult.equals(判断是否达到20s并判断是否停止1s);
     */
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
                this.status = WAITING;
//                Main.GUI.SetTaxiStatus(ID, position, status);
            }
        } catch (Exception e) { }
    }

    /** @REQUIRES:  None;
     * @MODIFIES: None;
     * @EFFECTS: None;
     */
    public Point getPosition()
    {
        return position;
    }

    /** @REQUIRES:  None;
     * @MODIFIES: None;
     * @EFFECTS: None;
     */
    public int getCredit()
    {
        return credit;
    }

    /** @REQUIRES:  None;
     * @MODIFIES: None;
     * @EFFECTS: None;
     */
    public int getStatus()
    {

        return status;
    }

    /** @REQUIRES: requestNum.equals(requestNum is a valid index of a request already exists);
     * @MODIFIES: this;
     * @EFFECTS: \result.equals(接单);
     */
    public void setRequest(int requestNum)
    {
        this.requestMark = true;
        this.requestNum = requestNum;
        this.status = READY;
        srcPoint = Main.requestHandler.getSrcPoint(requestNum);
        dstPoint = Main.requestHandler.getDstPoint(requestNum);
//        System.out.printf("SRC: (%d,%d)\n", srcPoint.x, srcPoint.y);
        Main.GUI.SetTaxiStatus(ID, position, status);
        System.out.printf("Taxi%d get Request%d\n", ID, requestNum);
    }

    /** @REQUIRES: c.equals(c是一个有效的信用度增量);
     * @MODIFIES: this;
     * @EFFECTS: None;
     */
    public void addCredit(int c)
    {
        this.credit += c;
    }
}
