import static java.lang.Math.abs;

public class Dispatcher
{
    protected double timer;
    protected long currentFloor;
    protected RequestsQueue requestsQueue;
    protected long[] info;
    protected long requestIndex;
    protected long requestType;
    protected long requestFloor;
    protected long requestTime;
    protected long requestFlag;

    public Dispatcher(RequestsQueue RQ)
    {
        timer = 0;
        currentFloor = 1;
        requestsQueue = RQ;
        info = new long[5];
        requestIndex = 0;
        requestType = 0;
        requestFloor = 0;
        requestFlag = 0;
        requestType = 0;

    }

    protected long getRequestQueueType(int index)
    {
        return requestsQueue.get(index)[1];
    }

    protected long getRequestQueueFloor(int index)
    {
        return requestsQueue.get(index)[2];
    }

    protected long getRequestQueueTime(int index)
    {
        return requestsQueue.get(index)[3];
    }

    protected long getRequestQueueFlag(int index)
    {
        return requestsQueue.get(index)[4];
    }

    public void schedule(Floor floorClass, Elevator elevatorClass)
    {
        while(!requestsQueue.isEmpty())
        {
            info = requestsQueue.pop();
            requestIndex = info[0];
            requestType = info[1];
            requestFloor = info[2];
            requestTime = info[3];
            requestFlag = info[4];

            if(requestFlag == 0)
            {
                if(requestFloor == currentFloor)
                // 目标楼层和现楼层相同
                {
                    timer = timer > requestTime ? timer : requestTime;
                    timer += 1;
                    toString(currentFloor, 0, timer);
                }
                else
                // 目标楼层和现楼层不同
                {
                    run();
                }
                int scanCounter = (int)requestIndex+1;
                sameJudger(scanCounter);
            }
            else
            {
                toString(requestIndex);
            }
        }
    }

    protected void run()
    {
        int direction = (currentFloor - requestFloor) < 0 ? 1 : -1;
        timer = timer > requestTime ? timer : requestTime;
        timer += abs(currentFloor - requestFloor)*0.5+1;
        currentFloor = requestFloor;
        if(requestType == 0)
            // ER
            toString(currentFloor, direction, timer-1);
        else if(requestType == 1)
            // FR UP
            toString(currentFloor, direction, timer-1);
        else if (requestType == 2)
            // FR DOWN
            toString(currentFloor, direction, timer-1);
    }

    protected void sameJudger(int scanCounter)
    {
        while(!requestsQueue.isEmpty(scanCounter))
        {
            if ( getRequestQueueTime(scanCounter)>timer )
                break;
            else
            // 指令发出后，电梯门关闭前
            {
                if ( getRequestQueueType(scanCounter)==requestType && getRequestQueueFloor(scanCounter)==requestFloor )
                    //操作类型相同且目标楼层相同
                    requestsQueue.setSame(scanCounter);
            }
            scanCounter += 1;
        }
    }

    private void toString(long floor, int status, double timer)
    {
//        String s = new String();
        if (status == 0)
            System.out.printf("(%d,STILL,%.1f)\n", floor, timer);
        else if (status == 1)
            System.out.printf("(%d,UP,%.1f)\n", floor, timer);
        else if (status == -1)
            System.out.printf("(%d,DOWN,%.1f)\n", floor, timer);
//        System.out.println(s);
    }

    private void toString(long index)
    {
        System.out.printf("# 同质请求：输入的第%d条合法请求\n", index+1);

    }


}
