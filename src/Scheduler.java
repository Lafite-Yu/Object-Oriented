import static java.lang.Math.abs;

public class Scheduler extends Dispatcher implements IF
{
    private int depth;
    public Scheduler(RequestsQueue RQ)
    {
        super(RQ);
        depth = 0;
    }

    @Override
    public void schedule()
    {
        if (!requestsQueue.isEmpty())
            info = requestsQueue.pop();
        int pickCounter = 0;
        int sameFloorPickCounter = 0;
        while (true)
        {
            requestIndex = info[0];
            requestType = info[1];
            requestFloor = info[2];
            requestTime = info[3];
            requestFlag = info[4];

//            System.out.println("# "+requestIndex);

            if(requestFlag != 1 && requestFlag != 3)
            // 有效 未执行
            {
                if(requestFloor == currentFloor)
                // 目标楼层和现楼层相同 STILL
                {
                    timer = timer > requestTime ? timer : requestTime;
                    timer += 1;
                    this.toString(requestIndex, 0);
                    requestsQueue.setComplete((int)requestIndex);
                    int scanCounter = (int)requestIndex+1;
//                    this.sameJudger(scanCounter);
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
                        scanCounter ++;
                    }
                }
                else
                // 目标楼层和现楼层不同
                {
                    timer = timer > requestTime ? timer : requestTime;
                    double endTime = abs(currentFloor - requestFloor)*0.5 + timer;
                    int direction = (requestFloor - currentFloor) > 0 ? 1 : 2; // 1: UP 2: DOWN
                    int scanCounter = 0;
//                    System.out.println("OJBK");
                    while(!requestsQueue.isEmpty(scanCounter))
                    {
//                        System.out.println("scanCounter0:\t"+scanCounter);
                        if (getRequestQueueFlag(scanCounter) == 3 || getRequestQueueFlag(scanCounter) == 1)
                        {
                            scanCounter += 1;
                            continue;
                        }
                        if ( getRequestQueueTime(scanCounter) > endTime+1)
                            break;
                        else if (getRequestQueueTime(scanCounter) >= endTime)
                        {
                            sameJudger((int)requestIndex, scanCounter);
                        }
                        else
                        {
//                            System.out.println("scanCounter0:\t"+scanCounter);
//                            System.out.println("direction:"+direction);
                            if ( !sameJudger((int)requestIndex, scanCounter) )
                            {
                                if (direction == 1) //UP
                                {
                                    if (getRequestQueueType(scanCounter) == 1 )
                                    {
                                        if (currentFloor < getRequestQueueFloor(scanCounter) && getRequestQueueFloor(scanCounter) < requestFloor)
                                        {
                                            if ((getRequestQueueFloor(scanCounter) - currentFloor) * 0.5 + timer > getRequestQueueTime(scanCounter))
                                            {
                                                endTime += identifier(scanCounter);
                                            }
                                        }
                                        else if (getRequestQueueFloor(scanCounter) == requestFloor)
                                        {
                                            if (scanCounter > requestIndex)
                                            {
                                                requestsQueue.setSameFloor(scanCounter);
                                                sameFloorPickCounter += 1;
                                            }
                                            else
                                                endTime += identifier(scanCounter);
                                        }
                                    }
                                    else if (getRequestQueueType(scanCounter) == 0)
                                    {
                                        if (currentFloor < getRequestQueueFloor(scanCounter) && getRequestQueueFloor(scanCounter) < requestFloor)
                                        {
                                            if ( (getRequestQueueFloor(scanCounter) - currentFloor)*0.5+timer > getRequestQueueTime(scanCounter) )
                                            {
                                                endTime += identifier(scanCounter);
                                            }
                                        }
                                        else if (requestFloor == getRequestQueueFloor(scanCounter))
                                        {
                                            if (scanCounter > requestIndex)
                                            {
                                                requestsQueue.setSameFloor(scanCounter);
                                                sameFloorPickCounter += 1;
                                            }
                                            else
                                                endTime += identifier(scanCounter);
                                        }
                                        else if (requestFloor < getRequestQueueFloor(scanCounter) && getRequestQueueFloor(scanCounter) <= 10)
                                        {
                                            requestsQueue.setPick(scanCounter);
                                            pickCounter += 1;
                                        }
                                    }
                                }
                                else // DOWN
                                {
//                                    System.out.println("requestType:"+getRequestQueueType(scanCounter));
                                    if (getRequestQueueType(scanCounter) == 2 )
                                    {
                                        if (currentFloor > getRequestQueueFloor(scanCounter) && getRequestQueueFloor(scanCounter) > requestFloor)
                                        {
                                            if ( (currentFloor - getRequestQueueFloor(scanCounter))*0.5+timer > getRequestQueueTime(scanCounter) )
                                            {
                                                endTime += identifier(scanCounter);
                                            }
                                        }
                                        else if (getRequestQueueFloor(scanCounter) == requestFloor)
                                        {
                                            if (scanCounter > requestIndex)
                                            {
                                                requestsQueue.setSameFloor(scanCounter);
                                                sameFloorPickCounter ++;
                                            }
                                            else
                                                endTime += identifier(scanCounter);
                                        }
                                    }
                                    else if (getRequestQueueType(scanCounter) == 0)
                                    {
                                        if (currentFloor > getRequestQueueFloor(scanCounter) && getRequestQueueFloor(scanCounter) > requestFloor)
                                        {
                                            if ( (currentFloor - getRequestQueueFloor(scanCounter))*0.5+timer > getRequestQueueTime(scanCounter) )
                                            {
                                                endTime += identifier(scanCounter);
                                            }
                                        }
                                        else if (requestFloor == getRequestQueueFloor(scanCounter))
                                        {
                                            if (scanCounter > requestIndex)
                                            {
                                                requestsQueue.setSameFloor(scanCounter);
                                                sameFloorPickCounter += 1;
                                            }
                                            else
                                                endTime += identifier(scanCounter);
                                        }
                                        else if (requestFloor > getRequestQueueFloor(scanCounter) && getRequestQueueFloor(scanCounter) >= 1)
                                        {
                                                requestsQueue.setPick(scanCounter);
                                                pickCounter += 1;
                                        }
                                    }
                                }
                            }
                        }
                        scanCounter += 1;
//                        System.out.println("scanCounter1:\t"+scanCounter);
                    }
                    /* 执行该指令 */
                    if (getRequestQueueFlag((int)requestIndex) != 3)
                    {
                        timer = endTime;
                        currentFloor = requestFloor;
                        requestsQueue.setComplete((int) requestIndex);
//                        System.out.println("test1:"+requestIndex);
                        toString(requestIndex, direction);
//                        System.out.println("samefloor:\t"+sameFloorPickCounter);
                        /* 同层捎带处理 同层捎带同质判断 */
                        int j = (int) requestIndex;
                        for (; sameFloorPickCounter > 0; sameFloorPickCounter--)
                        {
                            while (!requestsQueue.isEmpty(j))
                            {
                                if (getRequestQueueFlag(j) == 4)
                                    break;
                                else
                                    j++;
                            }
                            if (!requestsQueue.isEmpty(j))
                            {
                                requestsQueue.setComplete(j);
                                //                            System.out.println("test0:"+j);
                                toString(j, direction);
                                int k = j + 1;
                                while (!requestsQueue.isEmpty(k))
                                {
                                    if (getRequestQueueTime(k) > timer + 1)
                                        break;
                                    else
                                        sameJudger(j, k);
                                    k++;
                                }
                                j++;
                            }
                        }
                        timer += 1;
                    }
                }
            }
            else if (requestFlag == 1)
            // 无效
            {
                toString(requestIndex);
            }
//            else if (requestFlag == 3)
//            // 已执行
//            {
//            }
            /* 确认是否捎带队列是否为空 */
            if (pickCounter > 0)
            {
                int j = 0;
                while (!requestsQueue.isEmpty(j))
                {
                    if (getRequestQueueFlag(j) == 2)
                    {
                        pickCounter = 0;
                        info = requestsQueue.get(j);
                        break;
                    }
                    else
                        j++;
                }
            }
            else
            {
                if (!requestsQueue.isEmpty())
                {
                    info = requestsQueue.pop();
                }
                else
                    break;
            }
        }
    }


    @Override
    public int identifier(int index)
    {
//        System.out.printf("# identifier:\t");
//        toString(index);
        depth += 1;
//        System.out.print("depth:"+depth);
//        System.out.println("\tidentifier:\t"+index);
        int pickTime = 1;
        double endTime = abs(currentFloor - getRequestQueueFloor(index))*0.5 + timer;
        int direction = (getRequestQueueFloor(index) - currentFloor) > 0 ? 1 : 2; // 1: UP 2: DOWN
        int scanCounter = index+1;
        int sameFloorPickCounter = 0;
        while(!requestsQueue.isEmpty(scanCounter))
        {
            if (getRequestQueueFlag(scanCounter) == 3 || getRequestQueueFlag(scanCounter) == 1)
            {
                scanCounter += 1;
                continue;
            }
            if ( getRequestQueueTime(scanCounter) > endTime+1)
                break;
            else if (getRequestQueueTime(scanCounter) >= endTime)
            {
                sameJudger(index, scanCounter);
            }
            else
            {
                if ( !sameJudger(index, scanCounter) )
                {
//                    System.out.println("direction:"+direction);
                    if (direction == 1) //UP
                    {
                        if (getRequestQueueType(scanCounter) == 1 || getRequestQueueType(scanCounter) == 0)
                        {
                            if (currentFloor < getRequestQueueFloor(scanCounter) && getRequestQueueFloor(scanCounter) < getRequestQueueFloor(index))
                            {
                                if ((getRequestQueueFloor(scanCounter) - currentFloor) * 0.5 + timer > getRequestQueueTime(scanCounter))
                                {
                                    pickTime += 1;
                                    endTime += identifier(scanCounter);
                                }
                            }
                            else if (getRequestQueueFloor(scanCounter) == getRequestQueueFloor(index))
                            {
                                if (scanCounter > index)
                                {
                                    sameFloorPickCounter += 1;
//                                    pickTime += 1;
//                                    System.out.println("同层捎带请求 pickTime:"+pickTime);
                                    requestsQueue.setSameFloor(scanCounter, depth);
                                }
//                                else
//                                {
//                                    endTime += identifier(scanCounter);
//                                    pickTime += 1;
//                                }
                            }
                        }
                    }
                    else // DOWN
                    {
                        if (getRequestQueueType(scanCounter) == 2 || getRequestQueueType(scanCounter) == 0)
                        {
                            if (currentFloor > getRequestQueueFloor(scanCounter) && getRequestQueueFloor(scanCounter) > getRequestQueueFloor(index))
                            {
                                if ((currentFloor - getRequestQueueFloor(scanCounter)) * 0.5 + timer > getRequestQueueTime(scanCounter))
                                {
                                    pickTime += 1;
                                    endTime += identifier(scanCounter);
                                }
                            }
                            else if (getRequestQueueFloor(scanCounter) == getRequestQueueFloor(index))
                            {
                                if (scanCounter > index)
                                {
                                    sameFloorPickCounter += 1;
//                                    pickTime += 1;
//                                    System.out.println("同层捎带请求 pickTime:"+pickTime);
                                    requestsQueue.setSameFloor(scanCounter, depth);
                                }
//                                else
//                                {
//                                    endTime += identifier(scanCounter);
//                                    pickTime ++;
//                                }
                            }
                        }
                    }
                }
            }
            scanCounter += 1;
        }
        timer = endTime;
        currentFloor = getRequestQueueFloor(index);
        requestsQueue.setComplete(index);
        toString(index, direction);
        /* 同层捎带处理 */
        int j = index;
//        System.out.println("OJBK");
        for (; sameFloorPickCounter > 0; sameFloorPickCounter--)
        {
//            System.out.println("sameFloorPickCounter 1:\t"+sameFloorPickCounter);
            while(!requestsQueue.isEmpty(j))
            {
                if (getRequestQueueFlag(j) == 4+depth)
                    break;
                else
                    j++;
            }
//            System.out.println("sameFloorPickCounter 2:\t"+sameFloorPickCounter);
            if ( !requestsQueue.isEmpty(j) )
            {
                requestsQueue.setComplete(j);
                toString(j, direction);
                int k = j+1;
                while (!requestsQueue.isEmpty(k))
                {
                    if ( getRequestQueueTime(k) > timer+1 )
                        break;
                    else
                        sameJudger(j, k);
                    k++;
                }
//            System.out.println("sameFloorPickCounter 3:\t"+sameFloorPickCounter);
                j += 1;
            }
        }
        timer += 1;
//        System.out.println("pickTime:"+pickTime);
        depth -= 1;
        return pickTime;
    }

    public boolean sameJudger(int requestIndex, int scanCounter)
    {
        if ( getRequestQueueType(scanCounter)==getRequestQueueType(requestIndex) && getRequestQueueFloor(scanCounter)==getRequestQueueFloor(requestIndex) && getRequestQueueTime(requestIndex) <= getRequestQueueTime(scanCounter))
            //操作类型相同且目标楼层相同
        {
            requestsQueue.setSame(scanCounter > requestIndex ? scanCounter : requestIndex);
            return true;
        }
        return false;
    }

    public String toString()
    {
        String s = String.format("电梯运行状态：当前时间：%f\t当前楼层%d\t", timer, currentFloor);
        System.out.println(s);
        return s;
    }
    private void toString(long index)
    {
//        System.out.printf("# 同质请求：输入的第%d条合法请求\n", index+1);
        int i = (int)index;
        if (getRequestQueueType(i) == 0) //ER
        {
            System.out.printf("#SAME[ER,%d,%d]\n", getRequestQueueFloor(i), getRequestQueueTime(i));
        }
        else if (getRequestQueueType(i) == 1) // FR UP
        {
            System.out.printf("#SAME[FR,%d,UP,%d]\n", getRequestQueueFloor(i), getRequestQueueTime(i));
        }
        else if (getRequestQueueType(i) == 2) // FR DOWN
        {
            System.out.printf("#SAME[FR,%d,DOWN,%d]\n", getRequestQueueFloor(i), getRequestQueueTime(i));
        }
    }

    private void toString(long index, int status)
    {
        int i = (int)index;
//        System.out.println("\nindex:"+i);
        if (getRequestQueueType(i) == 0) //ER
        {
            System.out.printf("[ER,%d,%d]/", getRequestQueueFloor(i), getRequestQueueTime(i));
        }
        else if (getRequestQueueType(i) == 1) // FR UP
        {
            System.out.printf("[FR,%d,UP,%d]/", getRequestQueueFloor(i), getRequestQueueTime(i));
        }
        else if (getRequestQueueType(i) == 2) // FR DOWN
        {
            System.out.printf("[FR,%d,DOWN,%d]/", getRequestQueueFloor(i), getRequestQueueTime(i));
        }
//        System.out.println("\tstatus:"+status);
        if (status == 0)
            System.out.printf("(%d,STILL,%.1f)\n", currentFloor, timer);
        else if (status == 1)
            System.out.printf("(%d,UP,%.1f)\n", currentFloor, timer);
        else if (status == 2)
            System.out.printf("(%d,DOWN,%.1f)\n", currentFloor, timer);
    }

}
