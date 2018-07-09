import java.awt.*;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.Scanner;

public class RequestHandler extends Thread implements DEFINE
{
    LinkedList<Request> requests = new LinkedList<>();
    LinkedList<RequestThread> threads = new LinkedList<>();
    Scanner scan = new Scanner(System.in);
    long startTime;

    public void readRequests()
    {
        System.out.println(LINE + "READING REQUESTS" + LINE);
        startTime = System.currentTimeMillis();

        for(int i = 0; i < MAX_REQUESTS; i++)
        {
            try
            {
                String str = scan.nextLine();
                str = str.replace(" ","");
                if (str.equalsIgnoreCase("END"))
                    System.exit(0);
                if (str.equalsIgnoreCase(""))
                    continue;
                String[] args = str.split("\\(|\\)|,");
                Point srcPoint = new Point(Integer.parseInt(args[2]), Integer.parseInt(args[3]));
                Point dstPoint = new Point(Integer.parseInt(args[6]), Integer.parseInt(args[7]));
                if (srcPoint.x < 0 | srcPoint.x > 79 | srcPoint.y < 0 | srcPoint.y > 79 | dstPoint.x < 0 | dstPoint.x > 79 | dstPoint.y < 0 | dstPoint.y > 79)
                {
                    System.out.println("Invalid Position.");
                    i--;
                    continue;
                }
                long time = System.currentTimeMillis();
                Request request = new Request(i, srcPoint, dstPoint, time);
                boolean isSame = false;
                for (Request eachRequest: requests)
                {
                    if (eachRequest.isSame(request))
                    {
                        System.out.printf("Same Request: [request %d] is same with previous [request %d]\n", i, eachRequest.getID());
                        isSame = true;
                    }
                }
                if (!isSame)
                {
                    RequestThread newRequest = new RequestThread(request);
                    newRequest.start();
                    requests.addLast(request);
                    threads.add(newRequest);
                } else
                    i--;
                if ((time - startTime)/1000 >= 5)
                {
                    for (Request eachRequest: requests)
                        if (eachRequest.isDelete(time))
                            requests.remove(eachRequest);
                    startTime = time;
                }
            } catch (ConcurrentModificationException e0)
            {
                System.out.println("Warning: removing old requests when new one comes.");
            } catch (Exception e)
            {
                System.out.println("Input Format Error.");
//                e.printStackTrace();
            }
        }
    }

    public Point getSrcPoint(int i)
    {
        return threads.get(i).getSrcPoint();
    }

    public Point getDstPoint(int i)
    {
        return threads.get(i).getDstPoint();
    }

}
