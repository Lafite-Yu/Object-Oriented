public class Main
{
    public static void main(String[] args)
    {
        try
        {
            Requests request = new Requests();
            RequestsQueue requestsQueue;
            Floor floor = new Floor();
            Elevator elevator = new Elevator();

            requestsQueue = request.readin();
//            Dispatcher dispatcher = new Dispatcher(requestsQueue);
//            dispatcher.schedule(floor, elevator);
            Scheduler scheduler = new Scheduler(requestsQueue);
            scheduler.schedule();
        }
        catch (Exception ignored)
        {
        }
    }
}
