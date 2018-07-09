import static java.lang.Math.abs;

public class Dispatcher
{
    private double timer;
    private long currentFloor;

    public Dispatcher()
    {
        timer = 0;
        currentFloor = 1;
    }

    public void run(RequestsQueue requestsQueue, Floor floor, Elevator elevator)
    {
        while(!requestsQueue.isEmpty())
        {
            long[] info = requestsQueue.pop();
//            System.out.println("----------------");
//            for (long k: info)
//            {
//                System.out.println(k);
//            }
            if(info[4] == 0)
            {
                int direction = (currentFloor - info[2]) < 0 ? 1 : -1;
                if(info[2] == currentFloor)
                // 目标楼层和现楼层相同
                {
                    timer = timer > info[3] ? timer : info[3];
                    timer += 1;
                    print(currentFloor, 0, timer);
                }
                else
                // 目标楼层和现楼层不同
                {
                    timer = timer > info[3] ? timer : info[3];
                    timer += abs(currentFloor - info[2])*0.5+1;
                    currentFloor = info[2];
                    if(info[1] == 0)
                    // ER
                    {
                        print(currentFloor, direction, timer-1);
                    }
                    else if(info[1] == 1)
                    // FR UP
                    {
                        print(currentFloor, direction, timer-1);
                    }
                    else if (info[1] == 2)
                    // FR DOWN
                    {
                        print(currentFloor, direction, timer-1);
                    }
                }
                int scanCounter = (int)info[0]+1;
                while(!requestsQueue.isEmpty(scanCounter))
                {
                    if ( requestsQueue.get(scanCounter)[3]>timer )
                        break;
                    else
                    // 指令发出后，电梯门关闭前
                    {
                        if ( requestsQueue.get(scanCounter)[1]==info[1] && requestsQueue.get(scanCounter)[2]==info[2] )
                            //操作类型相同且目标楼层相同
                            requestsQueue.setInvalid(scanCounter);
                    }
                    scanCounter += 1;
                }
            }
            else
            {
                print(info[0]);
            }
        }
    }


    private void print(long floor, int status, double timer)
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

    private void print(long index)
    {
        System.out.printf("# 同质请求：输入的第%d条合法请求\n", index+1);
    }


}
