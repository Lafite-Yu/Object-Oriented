import java.util.LinkedList;

public class Tray
{
    private LinkedList<LinkedList<Request>> queue = new LinkedList<LinkedList<Request>>();
    private volatile boolean endSignal;

    public Tray(boolean endSignal)
    {
        queue.addLast(new LinkedList<Request>());
        queue.addLast(new LinkedList<Request>());
        queue.addLast(new LinkedList<Request>());
        queue.addLast(new LinkedList<Request>());
        this.endSignal = endSignal;
    }

    public synchronized void put(Request request, int elevatorNum)
    {
        while (request == null && !endSignal)
        {
            try
            {
                wait();
            } catch (InterruptedException e) { }
        }
        System.out.printf("Request:%s put into Elevator:%d\n", request.toString(), elevatorNum);
        queue.get(elevatorNum).addLast(request);
        notifyAll();
    }

    public synchronized LinkedList<Request> getRequests1()
    {
        notifyAll();
        return queue.get(1);
    }

    public synchronized LinkedList<Request> getRequests2()
    {
        notifyAll();
        return queue.get(2);
    }

    public synchronized LinkedList<Request> getRequests3()
    {
        notifyAll();
        return queue.get(3);
    }


}