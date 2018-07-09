import java.io.PrintStream;
import java.sql.Time;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestQueue
{
    Request temp;
    long startTime;
    long currentTime;
    private LinkedList<Request> queue;
    private volatile boolean endSignal;
    private boolean startFlag;
    private PrintStream ps;

    public RequestQueue(long time, boolean endSignal, PrintStream ps)
    {
        this.queue = new LinkedList<Request>();
        this.startTime = time;
        this.endSignal = endSignal;
        this.ps = ps;
    }

    public String readin()
    {
        Scanner scan = new Scanner(System.in);
        String str = scan.nextLine();
        if (!startFlag)
        {
            startTime = System.currentTimeMillis();
            startFlag = true;
        }
        currentTime = System.currentTimeMillis();
        str = str.replaceAll(" ", "");
        return str;
    }

    public synchronized void newRequest(String str)
    {
        String[] requestList = str.split(";");
        int count = 0;
        for (String request: requestList)
        {
            if(validate(request, requestList.length) && count < 50)
            {
                queue.addLast(temp);
                count ++;
            }
            else
                invalidPrint(request);
        }
        notifyAll();
    }

    public boolean validate(String str, int length)
    {
        // grammarCheck
        String REGEX= "(\\(FR,\\+?\\d{1,2},(UP|DOWN)\\))|(\\(ER,#[123],\\+?\\d{1,2}\\))";
        Pattern P = Pattern.compile(REGEX);
        Matcher M = P.matcher(str);
        if (!M.matches())
        {
            if(str.equals("END") && length == 1)
            {
                temp = new Request(-1);
                return true;
            } else
            {
                return false;
            }
        }
        // valueCheck
        String[] s = str.split("[(,)]");
        int floor = (s[1].equals("FR") ? Integer.parseInt(s[2]) : Integer.parseInt(s[3]));
        if (floor > 20 || floor < 1)
        {
            return false;
        }
        if(s[1].equals("ER"))
        {
            temp = new Request(0, floor, Integer.parseInt(s[2].replaceAll("#", "")), currentTime, startTime);
        }
        else
        {
            if(s[3].equals("UP"))
            {
                if (floor == 20)
                {
                    return false;
                }
                else
                    temp = new Request(1, floor, 1, currentTime, startTime);
            }
            else if(s[3].equals("DOWN"))
            {
                if (floor == 1)
                {
                    return false;
                }
                else
                    temp = new Request(1, floor, -1, currentTime, startTime);
            }
        }
        return true;
    }

    public synchronized LinkedList<Request> fetch()
    {

        while (queue.size() == 0 && !endSignal)
        {
//            System.out.println("Fetch. "+queue.size());
            try
            {
                wait();
            } catch (InterruptedException e) { }
        }
        LinkedList<Request> temp = new LinkedList<Request>();
        for (int i = 0; i < queue.size(); i++)
        {
            temp.addLast(queue.get(i));
        }
        queue.clear();
        notifyAll();
        return temp;
    }

    public void invalidPrint(String request)
    {
        System.out.printf("%d:INVALID[%s,%.1f]\n", System.currentTimeMillis(), request, (double)(currentTime-startTime)/1000);
        ps.printf("%d:INVALID[%s,%.1f]\n", System.currentTimeMillis(), request, (double)(currentTime-startTime)/1000);
    }

    public long getStartTime()
    {
        return startTime;
    }
}
