import java.io.PrintStream;
import java.util.LinkedList;

public class Elevator extends Thread
{
    private int elevatorNum;
    private int direction; // DOWN:-1 UP:1 STILL:0
    private int status; // 0:IDLE 1:RUNNING 2:STAY
    private int currentFloor;
    private int targetFloor;
    private long startTime;
    private int moveDis;
    private boolean[] light;
    private Tray tray;
    private Stair stair;
    private volatile boolean endSignal;
    private PrintStream ps;


    public Elevator(int num, Tray tray, Stair stair, long time, boolean signal, PrintStream ps)
    {
        this.elevatorNum = num;
        this.direction = 0;
        this.status = 0;
        this.currentFloor = 1;
        this.targetFloor = 1;
        this.startTime = time;
        this.moveDis = 0;
        this.light = new boolean[25];
        this.tray = tray;
        this.stair = stair;
        this.endSignal = signal;
        this.ps = ps;
    }

    public void run()
    {
        while (!endSignal || getRequests().size() != 0)
        {
            systemOn();
        }
        System.out.printf("Elevator %d Stopped.\n", elevatorNum);
    }

    public void systemOn()
    {
        if (getRequests().size() == 0)
        {
            status = 0;
            direction = 0;
        } else
        {
            if (status == 0) // IDLE
            {
                if (currentFloor < getRequests().getFirst().getDstFloor())
                {
                    direction = 1;
                    status = 1;
                    targetFloor = lightUpAll(direction);
                } else if (currentFloor > getRequests().getFirst().getDstFloor())
                {
                    direction = -1;
                    status = 1;
                    targetFloor = lightUpAll(direction);
                } else
                {
                    direction = 0;
                    status = 2;
                    targetFloor = lightUpAll(direction);
                }
            } else if (status == 2) // STAY
            {
                boolean flag = false;
                long currentTime = startTime;
                for (int i = 0; i < getRequests().size(); i++)
                {
                    if (getRequests().get(i).getDstFloor() == currentFloor)
                    {
                        if (!flag)
                        {
                            try
                            {
                                sleep(6000);
                            } catch (InterruptedException e)
                            {
                            }
                            currentTime = System.currentTimeMillis();
                            flag = true;
                        }
                        System.out.printf("%d:[%s]/(#%d,%d,STILL,%d,%.1f)\n", System.currentTimeMillis(), getRequests().get(i).toString(), elevatorNum, currentFloor, moveDis, (double) (currentTime - startTime) / 1000);
                        ps.printf("%d:[%s]/(#%d,%d,STILL,%d,%.1f)\n", System.currentTimeMillis(), getRequests().get(i).toString(), elevatorNum, currentFloor, moveDis, (double) (currentTime - startTime) / 1000);
//                        System.out.println("StartTime:" + startTime);
                        if (getRequests().get(i).getRequestType() == 0) // ER
                            lightOff(currentFloor);
                        else // FR
                            if (getRequests().get(i).getDirection() == 1) // UP
                                stair.lightOffUpButton(currentFloor);
                            else
                                stair.lightOffDownButton(currentFloor);
                        getRequests().remove(i);
                        i -= 1;
                    }
                }
                status = 0;
                direction = 0;
            } else // RUNNING
            {
                if (direction == 1) // UP
                {
                    int flag = 0;
                    long currentTime = startTime;
                    for (int i = 0; i < getRequests().size(); i++)
                    {
                        if (getRequests().get(i).getDstFloor() == currentFloor)
                        {
                            if (flag == 0)
                            {
                                currentTime = System.currentTimeMillis();
                                flag ++;
                            }
                            System.out.printf("%d:[%s]/(#%d,%d,UP,%d,%.1f)\n", System.currentTimeMillis(), getRequests().get(i).toString(), elevatorNum, currentFloor, moveDis, (double) (currentTime - startTime) / 1000);
                            ps.printf("%d:[%s]/(#%d,%d,UP,%d,%.1f)\n", System.currentTimeMillis(), getRequests().get(i).toString(), elevatorNum, currentFloor, moveDis, (double) (currentTime - startTime) / 1000);
                            if (flag == 1)
                            {
                                flag ++;
                                try
                                {
                                    sleep(6000);
                                } catch (InterruptedException e)
                                {
                                }
                            }
                            if (getRequests().get(i).getRequestType() == 0) // ER
                                lightOff(currentFloor);
                            else // FR UP
                                stair.lightOffUpButton(currentFloor);
                            getRequests().remove(i);
                            i -= 1;
                        }
                    }

                    if (currentFloor == targetFloor)
                    {
                        status = 0;
                        direction = 0;
                    } else
                    {
                        try
                        {
                            sleep(3000);
                        } catch (InterruptedException e)
                        {
                        }
                        currentFloor += 1;
                        moveDis += 1;
                    }
                } else if (direction == -1) // DOWN
                {
                    int flag = 0;
                    long currentTime = startTime;
                    for (int i = 0; i < getRequests().size(); i++)
                    {
                        if (getRequests().get(i).getDstFloor() == currentFloor)
                        {
                            if (flag == 0)
                            {
                                currentTime = System.currentTimeMillis();
                                flag ++;
                            }
                            System.out.printf("%d:[%s]/(#%d,%d,DOWN,%d,%.1f)\n", System.currentTimeMillis(), getRequests().get(i).toString(), elevatorNum, currentFloor, moveDis, (double) (currentTime - startTime) / 1000);
                            ps.printf("%d:[%s]/(#%d,%d,DOWN,%d,%.1f)\n", System.currentTimeMillis(), getRequests().get(i).toString(), elevatorNum, currentFloor, moveDis, (double) (currentTime - startTime) / 1000);
                            if (flag == 1)
                            {
                                flag++;
                                try
                                {
                                    sleep(6000);
                                } catch (InterruptedException e)
                                {
                                }
                            }
                            if (getRequests().get(i).getRequestType() == 0) // ER
                                lightOff(currentFloor);
                            else // FR UP
                                stair.lightOffUpButton(currentFloor);
                            getRequests().remove(i);
                            i -= 1;
                        }
                    }

                    if (currentFloor == targetFloor)
                    {
                        status = 0;
                        direction = 0;
                    } else
                    {
                        try
                        {
                            sleep(3000);
                        } catch (InterruptedException e)
                        {
                        }
                        currentFloor -= 1;
                        moveDis += 1;
                    }
                }
            }
        }
    }

    public int lightUpAll(int dir)
    {
        int maxFloor = currentFloor, minFloor = currentFloor;
        for (int i = 0; i <getRequests().size(); i++)
        {
            if (getRequests().get(i).getRequestType() == 0) // ER
            {
                if (getLight(getRequests().get(i).getDstFloor()))
                {
                    samePrint(getRequests().get(i));
                    getRequests().remove(i);
                    i--;
                }
                else
                    lightOn(getRequests().get(i).getDstFloor());
            }

            else // FR
                if (getRequests().get(i).getDirection() == 1) // UP
                {
                    if (stair.getUpLight(getRequests().get(i).getDstFloor()))
                    {
                        samePrint(getRequests().get(i));
                        getRequests().remove(i);
                        i--;
                    }
                    else
                        stair.lightOnUpButton(getRequests().get(i).getDstFloor());
                }
                else
                {
                    if (stair.getDownLight(getRequests().get(i).getDstFloor()))
                    {
                        samePrint(getRequests().get(i));
                        getRequests().remove(i);
                        i--;
                    }
                    else
                        stair.lightOnDownButton(getRequests().get(i).getDstFloor());
        }
            maxFloor = (maxFloor > getRequests().get(i).getDstFloor() ? maxFloor : getRequests().get(i).getDstFloor());
            minFloor = (minFloor < getRequests().get(i).getDstFloor() ? minFloor : getRequests().get(i).getDstFloor());
        }
        return (dir == 1 ? maxFloor : (dir == -1 ? minFloor : currentFloor));
    }

    public LinkedList<Request> getRequests()
    {
        if (elevatorNum == 1)
            return tray.getRequests1();
        else if (elevatorNum == 2)
            return tray.getRequests2();
        else
            return tray.getRequests3();
    }

    public boolean getLight(int floor)
    {
        return light[floor];
    }

    public void lightOn(int floor)
    {
        light[floor] = true;
    }

    public void lightOff(int floor)
    {
        light[floor] = false;
    }

    public int getTargetFloor()
    {
        return targetFloor;
    }

    public int getCurrentFloor()
    {
        return currentFloor;
    }

    public int getDirection()
    {
        return direction;
    }

    public int getStatus()
    {
        return status;
    }

    public int getMoveDis()
    {
        return moveDis;
    }

    public int getElevatorNum()
    {
        return elevatorNum;
    }

    public void setStartTime(long startTime)
    {
        this.startTime = startTime;
    }

    public void stopMe()
    {
        this.endSignal = true;
    }
    private void samePrint(Request request)
    {
        System.out.printf("#%d:SAME[%s]\n", System.currentTimeMillis(), request.toString());
        ps.printf("#%d:SAME[%s]\n", System.currentTimeMillis(), request.toString());
    }
}