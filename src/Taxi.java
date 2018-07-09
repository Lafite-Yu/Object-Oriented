import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

public class Taxi extends Thread implements DEFINE
{
    /** @OVERVIEW: 出租车线程，每个线程负责一辆出租车的移动;
     * @INHERIT: Thread;
     * @INVARIANT: ID;
     */

    private int ID;
    private long startTime;
    private int stopTime;
    private Point position;
    private int status = WAITING;
    private int credit = 0;

    private boolean requestMark = false;
    private int requestNum = 0;
    private boolean reachedSrc = false;
    private Point srcPoint = new Point();
    private Point dstPoint = new Point();
    private LinkedList<Integer> movePath = new LinkedList<>();
    private long fakeTime = 0;
    private Point lastPosition = new Point();

    public boolean repOK()
    {
        if (ID >= 0 && ID < 100 && position != null && srcPoint != null && dstPoint != null)
            return true;
        else
            return false;
    }

    /**
     * @REQUIRES: 0 <= i < 100;
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

    /**
     * @REQUIRES: status == WAITING || status == STOP;
     * credit.equals(credit is valid);
     * 0 <= x,y <= 79;
     * @MODIFIES: this;
     * @EFFECTS: None;
     */
    public void initTaxi(int status, int credit, int x, int y)
    {
        this.status = status;
        this.credit = credit;
        this.position.setLocation(x, y);
        lastPosition.setLocation(position.x, position.y);
        Main.GUI.SetTaxiStatus(ID, position, status);
    }

    /**
     * @REQUIRES: None;
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
            } catch (Exception e)
            {
            }
            this.status = WAITING;
//            Main.GUI.SetTaxiStatus(ID, position, status);
        }
        this.status = WAITING;
        this.startTime = System.currentTimeMillis();
        while (true)
        {
            try
            {
//                sleep(500);
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
            } catch (Exception e)
            {
            }
        }
    }

    /**
     * @REQUIRES: 0 <= nextPosition.x, nextPosition.y < 80;
     * @MODIFIES: this;
     * @EFFECTS: (lastPosition->position->nextPosition).sameDirection(UP->RIGHT || LEFT->UP || DOWN->LEFT || RIGHT->DOWN) ==> \result.equals(true);
     */
    public boolean turnRight(Point nextPosition)
    {
        if (position.x == lastPosition.x && position.y == lastPosition.y)
            return false;
//        UP->RIGHT
        if (position.x+1 == lastPosition.x && position.y == lastPosition.y)
            if (position.x == nextPosition.x && position.y+1 == nextPosition.y)
            {
//                System.out.printf("Last:(%d,%d) current:(%d,%d) next:(%d,%d) UP->RIGHT\n", lastPosition.x, lastPosition.y, position.x, position.y, nextPosition.x, nextPosition.y);
                return true;
            }
//        LEFT->UP
        if (position.x == lastPosition.x && position.y+1 == lastPosition.y)
            if (position.x-1 == nextPosition.x && position.y == nextPosition.y)
            {
//                System.out.printf("Last:(%d,%d) current:(%d,%d) next:(%d,%d) LEFT->UP\n", lastPosition.x, lastPosition.y, position.x, position.y, nextPosition.x, nextPosition.y);
                return true;
            }
//        DOWN->LEFT
        if (position.x-1 == lastPosition.x && position.y == lastPosition.y)
            if (position.x == nextPosition.x && position.y-1 == nextPosition.y)
            {
//                System.out.printf("Last:(%d,%d) current:(%d,%d) next:(%d,%d) DOWN->LEFT\n", lastPosition.x, lastPosition.y, position.x, position.y, nextPosition.x, nextPosition.y);
                return true;
            }
//        RIGHT->DOWN
        if (position.x == lastPosition.x && position.y-1 == lastPosition.y)
            if (position.x+1 == nextPosition.x && position.y == nextPosition.y)
            {
//                System.out.printf("Last:(%d,%d) current:(%d,%d) next:(%d,%d) RIGHT->DOWN\n", lastPosition.x, lastPosition.y, position.x, position.y, nextPosition.x, nextPosition.y);
                return true;
            }
        return false;
    }

    /**
     * @REQUIRES: None;
     * @MODIFIES: this;
     * @EFFECTS: \result.equals( position = \old(position).move(movePath.getFirst()) );
     * \result.equals( 出租车运行状态转换 )
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
                    if (position.x == srcPoint.x && position.y == srcPoint.y)
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
                    if (position.x == dstPoint.x && position.y == dstPoint.y)
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
        } catch (Exception e)
        {
        }
    }

    /**
     * @REQUIRES: None;
     * @MODIFIES: this;
     * @EFFECTS: \result.equals(WAITING状态下出租车随机移动，position = \old(position)沿流量最小方向运动一次);
     */
    private void randomMove()
    {
        try
        {
            while (true)
            {
                int move = new Random().nextInt(4);
                int minFlux = 10000;
                int upFlux = 20000, downFlux = 20000, rightFlux = 20000, leftFlux = 20000;

//            case UP:
                if (GUIGv.m.isConnect(position.x * 80 + position.y, (position.x - 1) * 80 + position.y))
                {
                    upFlux = GUIGv.GetFlow(position.x, position.y, position.x - 1, position.y);
                    minFlux = minFlux < upFlux ? minFlux : upFlux;
                }
//            case DOWN:
                if (GUIGv.m.isConnect(position.x * 80 + position.y, (position.x + 1) * 80 + position.y))
                {
                    downFlux = GUIGv.GetFlow(position.x, position.y, position.x + 1, position.y);
                    minFlux = minFlux < downFlux ? minFlux : downFlux;
                }
//            case LEFT:
                if (GUIGv.m.isConnect(position.x * 80 + position.y, position.x * 80 + (position.y - 1)))
                {
                    leftFlux = GUIGv.GetFlow(position.x, position.y, position.x, position.y - 1);
                    minFlux = minFlux < leftFlux ? minFlux : leftFlux;
                }
//            case RIGHT:
                if (GUIGv.m.isConnect(position.x * 80 + position.y, position.x * 80 + (position.y + 1)))
                {
                    rightFlux = GUIGv.GetFlow(position.x, position.y, position.x, position.y + 1);
                    minFlux = minFlux < rightFlux ? minFlux : rightFlux;
                }

                switch (move)
                {
                    case UP:
                    {
                        if (position.x == 0)
                            break;
                        if (GUIGv.m.isConnect(position.x * 80 + position.y, (position.x - 1) * 80 + position.y) && upFlux == minFlux)
                        {
                            if (!turnRight(new Point(position.x - 1, position.y)))
                            {
                                while (Main.light.getLight(position) == WE_ON)
                                {
                                    sleep(5);
                                }
                                if (!GUIGv.m.isConnect(position.x * 80 + position.y, (position.x - 1) * 80 + position.y))
                                    continue;
                            }
//                            System.out.printf("Taxi:%d UP Light:%s \n", ID, Main.light.getLight(position)==WE_ON?"WE_ON":Main.light.getLight(position)==SN_ON?"SN_ON":"NULL");
                            sleep(500);
                            stopTime += 500;
                            lastPosition.setLocation(position.x, position.y);
                            position.setLocation(position.x - 1, position.y);
                            return;
                        }
                        break;
                    }
                    case DOWN:
                    {
                        if (position.x == 79)
                            continue;
                        if (GUIGv.m.isConnect(position.x * 80 + position.y, (position.x + 1) * 80 + position.y) && downFlux == minFlux)
                        {
                            if (!turnRight(new Point(position.x + 1, position.y)))
                            {
                                while (Main.light.getLight(position) == WE_ON)
                                {
                                    sleep(5);
                                }
                                if (!GUIGv.m.isConnect(position.x * 80 + position.y, (position.x + 1) * 80 + position.y))
                                    continue;
                            }
//                            System.out.printf("Taxi:%d DOWN Light:%s \n", ID, Main.light.getLight(position)==WE_ON?"WE_ON":Main.light.getLight(position)==SN_ON?"SN_ON":"NULL");
                            sleep(500);
                            stopTime += 500;
                            lastPosition.setLocation(position.x, position.y);
                            position.setLocation(position.x + 1, position.y);
                            return;
                        }
                        break;
                    }
                    case LEFT:
                    {
                        if (GUIGv.m.isConnect(position.x * 80 + position.y, position.x * 80 + (position.y - 1)) && leftFlux == minFlux)
                        {
                            if (!turnRight(new Point(position.x, position.y - 1)))
                            {
                                while (Main.light.getLight(position) == SN_ON)
                                {
                                    sleep(5);
                                }
                                if (!GUIGv.m.isConnect(position.x * 80 + position.y, position.x * 80 + (position.y - 1)))
                                    continue;
                            }
//                            System.out.printf("Taxi:%d LEFT Light:%s \n", ID, Main.light.getLight(position)==WE_ON?"WE_ON":Main.light.getLight(position)==SN_ON?"SN_ON":"NULL");
                            sleep(500);
                            stopTime += 500;
                            lastPosition.setLocation(position.x, position.y);
                            position.setLocation(position.x, position.y - 1);
                            return;
                        }
                        break;
                    }
                    case RIGHT:
                    {
                        if (GUIGv.m.isConnect(position.x * 80 + position.y, position.x * 80 + (position.y + 1)) && rightFlux == minFlux)
                        {
                            if (!turnRight(new Point(position.x, position.y + 1)))
                            {
                                while(Main.light.getLight(position) == SN_ON)
                                {
                                    sleep(5);
                                }
                                if (!GUIGv.m.isConnect(position.x * 80 + position.y, position.x * 80 + (position.y + 1)))
                                    continue;
                            }
//                            System.out.printf("Taxi:%d RIGHT\n", ID);
                            sleep(500);
                            stopTime += 500;
                            lastPosition.setLocation(position.x, position.y);
                            position.setLocation(position.x, position.y + 1);
                            return;
                        }
                        break;
                    }
                }
            }
        } catch (Exception e)
        {
        }
    }


    /**
     * @REQUIRES: None;
     * @MODIFIES: None;
     * @EFFECTS: \result.equals(接单后获取出租车移动路径);
     */
    public void getMovePath()
    {
        movePath.clear();
        for (int nextPoint : GUIGv.m.getPath(position, srcPoint, dstPoint, reachedSrc))
        {
//            System.out.printf("(%d,%d)\n", nextPoint/80,nextPoint%80);
            movePath.addLast(nextPoint);
        }
        int first = movePath.remove(0);
        if (first != position.x * 80 + position.y)
        {
            System.out.printf("positon:(%d,%d) index0:(%d,%d)\n", position.x, position.y, first / 80, first % 80);
            System.out.println("FATAL ERROR: UNKNOWN ERROR");
//            gv.stay(2000);
            return;
        }
    }

    /**
     * @REQUIRES: 0 <= nextPoint <= 79*80+79;
     * @MODIFIES: this;
     * @EFFECTS: GUIGv.m.isConnect(curPoint, nextPoint) ==> move, return true;
     * !GUIGv.m.isConnect(curPoint, nextPoint) ==> return false;
     */
    private boolean move(int nextPoint)
    {
        try
        {
            Point nextPosition = new Point(nextPoint / 80, nextPoint % 80);

            if (GUIGv.m.isConnect(position.x * 80 + position.y, nextPoint))
            {
                long time = System.currentTimeMillis();
                if (!turnRight(nextPosition))
                {
//            UP
                    if (position.x - 1 == nextPosition.x && position.y == nextPosition.y)
                    {
                        while (Main.light.getLight(position) == WE_ON)
                        {
                            sleep(1);
                        }
                    }
//                DOWN
                    if (position.x + 1 == nextPosition.x && position.y == nextPosition.y)
                    {
                        while (Main.light.getLight(position) == WE_ON)
                        {
                            sleep(1);
                        }
                    }
//                LEFT
                    if (position.x == nextPosition.x && position.y - 1 == nextPosition.y)
                    {
                        while (Main.light.getLight(position) == SN_ON)
                        {
                            sleep(1);
                        }
                    }
//                RIGHT
                    if (position.x == nextPosition.x && position.y+1 == nextPosition.y)
                    {
                        while (Main.light.getLight(position) == SN_ON)
                        {
                            sleep(1);
                        }
                    }
                    if (!GUIGv.m.isConnect(position.x * 80 + position.y, nextPoint))
                        return false;
                }
                time = System.currentTimeMillis() - time;
                fakeTime += time;
                lastPosition.setLocation(position.x, position.y);
                position.setLocation(nextPoint / 80, nextPoint % 80);
                return true;
            } else
                return false;
        } catch (Exception e)
        {
            return false;
        }
    }

    /**
     * @REQUIRES: None;
     * @MODIFIES: None;
     * @EFFECTS: \reqult.equals(判断是否达到20s并判断是否停止1s);
     */
    private void sleep20()
    {
        try
        {
            long time = System.currentTimeMillis();
//            if (time - startTime >= 20000)
            if (stopTime >= 20000)
            {
                this.status = STOP;
                startTime = time;
                stopTime = 0;
                Main.GUI.SetTaxiStatus(ID, position, status);
                sleep(1000);
                this.status = WAITING;
//                Main.GUI.SetTaxiStatus(ID, position, status);
            }
        } catch (Exception e)
        {
        }
    }

    /**
     * @REQUIRES: None;
     * @MODIFIES: None;
     * @EFFECTS: None;
     */
    public Point getPosition()
    {
        return position;
    }

    /**
     * @REQUIRES: None;
     * @MODIFIES: None;
     * @EFFECTS: None;
     */
    public int getCredit()
    {
        return credit;
    }

    /**
     * @REQUIRES: None;
     * @MODIFIES: None;
     * @EFFECTS: None;
     */
    public int getStatus()
    {

        return status;
    }

    /**
     * @REQUIRES: requestNum.equals(requestNum is a valid index of a request already exists);
     * @MODIFIES: this;
     * @EFFECTS: \result.equals(设置requestMark, requestNum, status, src/dstPoint);
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

    /**
     * @REQUIRES: c.equals(c是一个有效的信用度增量);
     * @MODIFIES: this;
     * @EFFECTS: None;
     */
    public void addCredit(int c)
    {
        this.credit += c;
    }
}
