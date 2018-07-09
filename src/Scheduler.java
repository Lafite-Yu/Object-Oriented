import java.util.Arrays;

public class Scheduler implements DEFINE
{
    /** @OVERVIEW: 控制单部电梯的运行和每条合法指令的执行结果输出
     * @Abstract_Function: AF(s) == (timer, floor, mainRequest, direction, handledCount)
     *                      where timer == s.timer, currentFloor == s.floor, mainRequest == s.mainRequest, direction == s.direction, nextHandleIndex == s.handledCount
     * @INVARIANT: timer >= 0
     *             && 1 <= currentFloor <= 10
     *             && mainRequest != null
     *             && direction == UP || direction == DOWN || direction == STILL
     *             && nextHandleIndex >= 0;
     */
    private double timer = 0;
    private int currentFloor = 1;
    private Request mainRequest = new Request(0, 0, 1, 0);
    private int direction = STILL;
    private int nextHandleIndex = 0;

    /**
     * @EFFECTS: \result == I_CLASS(\this)
     */
    public boolean repOK()
    {
        if (timer > 0 && currentFloor >= 1 && currentFloor <= 10 && mainRequest != null && (direction == UP || direction == DOWN || direction == STILL) && nextHandleIndex > 0)
            return true;
        return false;
    }

    /**@MODIFIES: this;
     * @EFFECTS: \result.equals(按照ALS进行电梯的调度，按照时间进行请求的处理和电梯运行判断);
     */
    public void schedule()
    {
        while (!Main.requestsQueue.isEmpty())
        {
//            System.out.printf("time:%f floor:%d\n", timer, currentFloor);
            if (mainRequest.getFloor() == currentFloor)
                getNewMainRequest();
//            System.out.printf("mainRequest:%d\n", mainRequest.index);
            lightUp();
            move();
            handleLight();
        }
    }

    /**@MODIFIES: this.currentFloor, this.timer;
     * @EFFECTS: direction == UP ==> currentFloor==\old(currentFloor)+1. timer==\old(timer)+0.5
     *           direction == DOWN ==> currentFloor==\old(currentFloor)-1. timer==\old(timer)+0.5
     *           direction == STILL ==> None
     */
    private void move()
    {
        if (direction == UP)
        {
            currentFloor += 1;
            timer += 0.5;
        }
        else if (direction == DOWN)
        {
            currentFloor -= 1;
            timer += 0.5;
        } else
            return;
    }

    /**@MODIFIES: this.direction, this.mainRequest, this.timer;
     * @EFFECTS: 如果在刚结束的运动方向上有需要捎带的ER指令 ==> 最远的ER指令为新的主指令
     *              没有需要捎带的ER指令 ==> 最早发出的、未执行的指令为新的主指令
     *              如果新的主指令发出时间晚于timer ==> timer == mainRequest.getTime()
     *              根据新的主指令和当前楼层的比较结果判断运动方向
     */
    private void getNewMainRequest()
    {
        boolean isSet = false;
        if (direction == UP)
        {
            for (int i = 10; i > currentFloor; i--)
            {
                if (Main.lights.getLight(ER, i).light)
                {
                    mainRequest = Main.requestsQueue.get(Main.lights.getLight(ER, i).index);
                    isSet = true;
                    break;
                }
            }
        }else if (direction == DOWN)
        {
            for (int i = 1; i < currentFloor; i++)
            {
                if (Main.lights.getLight(ER, i).light)
                {
                    mainRequest = Main.requestsQueue.get(Main.lights.getLight(ER, i).index);
                    isSet = true;
                    break;
                }
            }
        }

        if (!isSet)
            mainRequest = Main.requestsQueue.getEarliestUnhandled();
        if (mainRequest.getIndex() == 999)
            System.exit(0);
        timer = timer > mainRequest.getTime() ? timer : mainRequest.getTime();
//        System.out.println("index" + mainRequest.index);

        if (mainRequest.getFloor() > currentFloor)
            direction = UP;
        else if (mainRequest.getFloor() < currentFloor)
            direction = DOWN;
        else
            direction = STILL;
    }

    /**@MODIFIES: this.nextHandleIndex;
     * @EFFECTS: nextHandleIndex >= 100 || nextHandleIndex >= Main.requestsQueue.size() ==> None
     *          Main.requestsQueue.get(nextHandleIndex).getTime() > timer ==> None
     *          点亮所有在当前主请求之前的请求对应的灯光，如果某请求的灯未被点亮 ==> 点亮
     *                                             如果已被点亮 ==> 同质请求，输出提示
     */
    private void lightUp()
    {
        while(true)
        {
            if (nextHandleIndex >= 100 || nextHandleIndex >= Main.requestsQueue.size())
                return;
            if (Main.requestsQueue.get(nextHandleIndex).getTime() > timer)
                break;
            if (!Main.lights.lightOn(Main.requestsQueue.get(nextHandleIndex)))
            {
                samePrint(nextHandleIndex);
                Main.requestsQueue.setHandled(nextHandleIndex);
            }
            nextHandleIndex++;
        }
    }

    /**@MODIFIES: this.timer;
     * @EFFECTS: 获取当前楼层的三种灯：ER, FRUP, FRDOWN的状态
     *           如果是主指令或者可以被捎带的指令 ==> 请求处理，熄灭被处理请求的灯光然后点亮开门过程中接收到的请求的灯
     *           如果没有需要处理的请求 ==> None
     */
    private void handleLight()
    {
//        System.out.printf("\tcurrentFloor:%d time:%.1f mainRequest:\n\t\ttype:%d floor:%d time:%d\n", currentFloor, timer, mainRequest.type, mainRequest.floor, mainRequest.time);
        int[] lights = new int[3];
        for (int i = 0; i < 3; i++)
            lights[i] = (Main.lights.getLight(i, currentFloor).light ? Main.lights.getLight(i, currentFloor).index : 999);
        Arrays.sort(lights);
        for (int i = 0; i < 3; i++)
        {
            if (lights[i] >= 100)
                break;
            if (Main.requestsQueue.get(lights[i]).getType() == ER)
                handledPrint(lights[i]);
            else if (Main.requestsQueue.get(lights[i]).getType() == FRUP)
            {
                if (direction == UP || mainRequest.getIndex() == lights[i])
                    handledPrint(lights[i]);
                else
                    lights[i] = 999;
            } else
            {
                if (direction == DOWN || mainRequest.getIndex() == lights[i])
                    handledPrint(lights[i]);
                else
                    lights[i] = 999;
            }
        }
        if (lights[0] != 999 || lights[1] != 999 || lights[2] != 999)
        {
            timer += 1;
            lightUp();
            for (int i = 0; i < 3; i++)
            {
                if (lights[i] >= 100)
                    continue;
                Main.requestsQueue.setHandled(lights[i]);
                Main.lights.lightOff(Main.requestsQueue.get(lights[i]).getType(), currentFloor);
            }
        }
    }

    /** @REQUIRES:  0 <= index < Main.requestQueue.size;
     * @MODIFIES: None;
     * @EFFECTS: \result.equals(打印同质请求的判断信息);
     */
    private void samePrint(int index)
    {
//        System.out.printf("# 同质请求：输入的第%d条合法请求\n", index+1);
        if (Main.requestsQueue.get(index).getType() == 0) //ER
        {
            System.out.printf("#SAME[ER,%d,%d]\n", Main.requestsQueue.get(index).getFloor(), Main.requestsQueue.get(index).getTime());
        } else if (Main.requestsQueue.get(index).getType() == 1) // FR UP
        {
            System.out.printf("#SAME[FR,%d,UP,%d]\n", Main.requestsQueue.get(index).getFloor(), Main.requestsQueue.get(index).getTime());
        } else
        {
            System.out.printf("#SAME[FR,%d,DOWN,%d]\n", Main.requestsQueue.get(index).getFloor(), Main.requestsQueue.get(index).getTime());
        }
    }

    /** @REQUIRES:  0 <= index < Main.requestQueue.size;
     *              status == 0 || status == 1 || status == 2;
     * @MODIFIES: None;
     * @EFFECTS: \result.equals(打印电梯运行状态);
     */
    private void handledPrint(int index)
    {
//        System.out.println("\nindex:"+i);
        if (Main.requestsQueue.get(index).getType() == 0) //ER
        {
            System.out.printf("[ER,%d,%d]/", Main.requestsQueue.get(index).getFloor(), Main.requestsQueue.get(index).getTime());
        } else if (Main.requestsQueue.get(index).getType() == 1) // FR UP
        {
            System.out.printf("[FR,%d,UP,%d]/", Main.requestsQueue.get(index).getFloor(), Main.requestsQueue.get(index).getTime());
        } else
        {
            System.out.printf("[FR,%d,DOWN,%d]/", Main.requestsQueue.get(index).getFloor(), Main.requestsQueue.get(index).getTime());
        }

        if (direction == STILL)
            System.out.printf("(%d,STILL,%.1f)\n", currentFloor, timer+1);
        else if (direction == UP)
            System.out.printf("(%d,UP,%.1f)\n", currentFloor, timer);
        else
            System.out.printf("(%d,DOWN,%.1f)\n", currentFloor, timer);
    }



}
