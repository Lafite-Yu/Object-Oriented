public class RequestSimulator extends Thread
{
    //输入
    private RequestQueue queue;
    private volatile boolean endSignal;
    private long startTime;
    private Elevator elevator1;
    private Elevator elevator2;
    private Elevator elevator3;
    private Scheduler scheduler;

    public RequestSimulator(RequestQueue queue, boolean signal, long time, Elevator elevator1, Elevator elevator2, Elevator elevator3, Scheduler scheduler)
    {
        this.queue = queue;
        this.endSignal = signal;
        this.startTime = time;
        this.elevator1 = elevator1;
        this.elevator2 = elevator2;
        this.elevator3 = elevator3;
        this.scheduler = scheduler;
    }

    public void run()
    {
        int count = 0;
        while (count < 50 && !endSignal)
        {
            String str = queue.readin();
            if (count == 0)
            {
                long startTime = queue.getStartTime();
                scheduler.setStartTime(startTime);
                elevator1.setStartTime(startTime);
                elevator2.setStartTime(startTime);
                elevator3.setStartTime(startTime);
            }
            queue.newRequest(str);
            if (str.equals("END"))
            {
                endSignal = true;
//                scheduler.stopMe();
//                elevator1.stopMe();
//                elevator2.stopMe();
//                elevator3.stopMe();
                break;
            }
            count ++;
        }
        System.out.println("RequestSimulator Stopped.");
    }
}