import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        try
        {
            Requests request = new Requests();
            RequestsQueue requestsQueue = new RequestsQueue();
//            Floor floor = new Floor();
            Elevator elevator = new Elevator();

//            requestsQueue = request.readin();


            Scanner scan = new Scanner(System.in);
            String str;
            int counter = 0;
            while (scan.hasNextLine())
            {
                str = scan.nextLine().replace(" ", "");
                if (str.equals("RUN"))
                    break;
                else if (request.judger(str))
                {
//                    requestsQueue = request.creater(str, requestsQueue);
                    long[] info = new long[3];
                    String[] s = str.split("[(,)]");
                    info[1] = Long.parseLong(s[2]);
                    if (s[1].equals("ER"))
                    {
                        info[0] = 0;
                        info[2] = Long.parseLong(s[3]);
                    } else if (s[1].equals("FR"))
                    {
                        info[2] = Long.parseLong(s[4]);
                        if (s[3].equals("UP"))
                            info[0] = 1;
                        else if (s[3].equals("DOWN"))
                            info[0] = 2;
                    }
                    requestsQueue.push(info);
                }
                counter += 1;
                if (counter >= 100)
                    break;
            }
            scan.close();

            Scheduler scheduler = new Scheduler(requestsQueue);
            scheduler.schedule();
        } catch (Exception ignored)
        {
        }
    }

    /** @REQUIRES:  None;
     * @MODIFIES: None;
     * @EFFECTS: None;
     */
    public boolean repOK()
    {
        return true;
    }
}
