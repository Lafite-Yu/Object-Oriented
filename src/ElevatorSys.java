import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class ElevatorSys
{

    public static void main(String[] args)
    {
        System.out.println("Hello World!");
        Boolean endSignal = false;
        Long startTime = 0L;
        File file = new File("result.txt");
        try
        {
            if (!file.exists())
                file.createNewFile();
            FileOutputStream fop = new FileOutputStream(file);
            PrintStream ps = new PrintStream(fop);
//            System.setOut(ps);
            RequestQueue  queue = new RequestQueue(startTime, endSignal, ps);
            Tray tray = new Tray(endSignal);
            Stair stair = new Stair();
            Elevator elevator1 = new Elevator(1, tray, stair, startTime, endSignal, ps);
            Elevator elevator2 = new Elevator(2, tray, stair, startTime, endSignal, ps);
            Elevator elevator3 = new Elevator(3, tray, stair, startTime, endSignal, ps);
            Scheduler scheduler = new Scheduler(queue, elevator1, elevator2, elevator3, tray, stair, startTime, endSignal, ps);
            RequestSimulator requestSimulator = new RequestSimulator(queue, endSignal, startTime, elevator1, elevator2, elevator3, scheduler);
            requestSimulator.start();
            scheduler.start();
            elevator1.start();
            elevator2.start();
            elevator3.start();
        }catch(Exception e){
            System.out.println("Error!");
        }
    }
}
