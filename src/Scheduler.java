import java.io.PrintStream;
import java.util.LinkedList;

public class Scheduler extends Thread
{
    private RequestQueue requestqueue;
    private Elevator[] elevator;
    private LinkedList<Request> allRequests;
    private Tray tray;
    private Stair stair;
    private long startTime;
    private volatile boolean endSignal;
    private PrintStream ps;

    public Scheduler(RequestQueue queue, Elevator e1, Elevator e2, Elevator e3, Tray tray, Stair stair, long time, boolean signal, PrintStream ps)
    {
        this.requestqueue = queue;
        this.elevator = new Elevator[4];
        this.elevator[1] = e1;
        this.elevator[2] = e2;
        this.elevator[3] = e3;
        this.tray = tray;
        this.allRequests = new LinkedList<Request>();
        this.stair = stair;
        this.startTime = time;
        this.endSignal = signal;
        this.ps = ps;
    }

    public void run()
    {
        while (!endSignal || this.allRequests.size() != 0)
        {
//            System.out.println("Scheduler running.");
            LinkedList<Request> tempQueue = requestqueue.fetch();
            if (endSignal)
                System.out.println(endSignal);
            for (int i = 0; i < tempQueue.size(); i++)
            {
                allRequests.addLast(tempQueue.get(i));
                System.out.printf("Request:%s\t add to Scheduler. Queue size:%d type:%d\n", allRequests.getLast().toString(), allRequests.size(), allRequests.getFirst().getRequestType());
            }
            while (allRequests.size() != 0)
            {
//                System.out.println(allRequests.size());
                for (int i = 0; i < allRequests.size(); i++)
                {
//                    System.out.println(allRequests.get(i).getFlag() + "\t" + elevatorSelect(allRequests.get(i)) + "\t" + pickJudger(allRequests.get(i), elevator[1]));
                    if (allRequests.get(i).getRequestType() == -1)
                    {
                        if (allRequests.size() == 1)
                        {
                            System.out.println("Scheduler Stopped.");
                            elevator[1].stopMe();
                            elevator[2].stopMe();
                            elevator[3].stopMe();
                            return;
                        }
                        else
                            continue;
                    } else if (allRequests.get(i).getFlag() == 0 && sameJudger(allRequests.get(i)))
                    {
                        allRequests.get(i).setFlag(1);
                        samePrint(allRequests.get(i));
                        allRequests.remove(i);
                        break;
                    } else if (allRequests.get(i).getFlag() == 0 && elevatorSelect(allRequests.get(i)) != 0)
                    {
//                    System.out.println("i:"+i);
                        allRequests.get(i).setFlag(1);
//                    System.out.printf("Elevator Select:%d\n", elevatorSelect(allRequests.get(i)) );
                        tray.put(allRequests.get(i), elevatorSelect(allRequests.get(i)));
                        allRequests.remove(i);
                        try
                        {
                            sleep(2);
                        } catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }
        }
        elevator[1].stopMe();
        elevator[2].stopMe();
        elevator[3].stopMe();
        System.out.println("Scheduler Stopped.");
    }

    private int elevatorSelect(Request request)
    {
        if (request.getRequestType() == -1)
            return 0;
        int availableCount = 0;
        int availableElevator = 0;
        int i = 0, j = 0;
        boolean[] elevatorCondition = new boolean[4];
        for (i = 1; i < 4; i++)
        {
            elevatorCondition[i] = pickJudger(request, elevator[i]);
            if (elevatorCondition[i])
            {
                availableCount++;
//                System.out.println("Pick Elevator:" + i);
            }
        }
        if (availableCount == 0)
        {
            if (request.getRequestType() == 0) // ER
            {
                if (elevator[request.getElevatorNum()].getStatus() == 0)
                    return request.getElevatorNum();
                else
                    return 0;
            } else // FR
            {
                for (i = 1; i < 4; i++)
                {
                    elevatorCondition[i] = (elevator[i].getStatus() == 0);
                    if (elevatorCondition[i])
                    {
                        availableCount++;
                        if (availableCount == 1)
                        {
                            availableElevator = i;
                        } else if (availableCount > 1)
                        {
                            availableElevator = (elevator[availableElevator].getMoveDis() <= elevator[i].getMoveDis()) ? availableElevator : i;
                        }
                    }

                }
                return availableElevator;
            }

        }
        else
        {
            int x = 0;
            for (i = 1; i < 4; i++)
            {
                if (elevatorCondition[i])
                {
                    x += 1;
                    if (x == 1)
                    {
                        availableElevator = i;
                    } else if (x > 1)
                    {
                        availableElevator = (elevator[availableElevator].getMoveDis() <= elevator[i].getMoveDis()) ? availableElevator : i;
                    }
                }
            }
            return availableElevator;
        }
    }

    private boolean pickJudger(Request request, Elevator elevator)
    {
        if (request.getRequestType() == 1) // FR
        {
//            System.out.println(elevator.getElevatorNum() + " " + elevator.getTargetFloor() + " " + elevator.getCurrentFloor() + " " + request.getDstFloor() + " " + elevator.getDirection() + " " + request.getDirection());
            if ((elevator.getTargetFloor() >= request.getDstFloor() && elevator.getCurrentFloor() < request.getDstFloor() && request.getDirection() == elevator.getDirection() && request.getDirection() == 1) ||
                    (elevator.getTargetFloor() <= request.getDstFloor() && elevator.getCurrentFloor() > request.getDstFloor() && request.getDirection() == elevator.getDirection() && request.getDirection() == -1) ||
                    (elevator.getTargetFloor() == request.getDstFloor() && elevator.getStatus() == 0))
                return true;
            else
                return false;
        } else if (request.getRequestType() == 0)// ER
        {
            if (elevator.getElevatorNum() == request.getElevatorNum())
            {
                if ((elevator.getCurrentFloor() < request.getDstFloor() && elevator.getDirection() == 1) ||
                        (elevator.getCurrentFloor() > request.getDstFloor() && elevator.getDirection() == -1) ||
                        (elevator.getCurrentFloor() == request.getDstFloor() && elevator.getStatus() == 0))
                    return true;
                else
                    return false;
            } else
                return false;
        } else
            return false;
    }

    private boolean sameJudger(Request request)
    {
        if (request.getRequestType() == 0) // ER
        {
            int id = request.getElevatorNum();
            return elevator[id].getLight(request.getDstFloor());
        } else if (request.getRequestType() == 1)// FR
        {
            int direction = request.getDirection();
            if (direction == 1) // UP
            {
                return stair.getUpLight(request.getDstFloor());
            } else // DOWN
            {
                return stair.getDownLight(request.getDstFloor());
            }
        } else
            return false;
    }

    private void samePrint(Request request)
    {
        System.out.printf("#%d:SAME[%s]\n", System.currentTimeMillis(), request.toString());
        ps.printf("#%d:SAME[%s]\n", System.currentTimeMillis(), request.toString());
    }

    public void setStartTime(long startTime)
    {
        this.startTime = startTime;
    }

    public void stopMe()
    {
        this.endSignal = true;
//        System.out.println("Size:" + this.allRequests.size());
        Thread.currentThread().interrupt();
    }


}
