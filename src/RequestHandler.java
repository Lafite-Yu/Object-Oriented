import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.abs;
import static java.lang.Math.signum;

public class RequestHandler extends Thread implements DEFINE
{
    /** @OVERVIEW: 从控制台读入loadFile/CR/CLOSE/OPEN/CHECK请求及相应的处理;
     * @INHERIT: Thread;
     * @INVARIANT: scan;
     */

    LinkedList<Request> requests = new LinkedList<>();
    LinkedList<RequestThread> threads = new LinkedList<>();
    Scanner scan = new Scanner(System.in);
    long startTime;
    LinkedList<String> requestString = new LinkedList<>();

    public boolean repOK()
    {
        if (requests != null && threads != null && scan != null && startTime > 0 && requestString != null)
            return true;
        else
            return false;
    }

    /** @REQUIRES: None;
     * @MODIFIES: this, System.in;
     * @EFFECTS: effect.equals(获取输入);
     *           \result.equals(合法输入==>构造合法输入，放入指定队列，构造请求线程，开始执行
     *                          不合法输入==>报错，重新获取
     *                          停止==>停止程序);
     */
    public void readRequests()
    {


        System.out.println(LINE + "READING REQUESTS" + LINE);
        try
        {
            while(true)
            {
                System.out.println("Do you want to load a file? Y/N");
                String str = scan.nextLine();
                if (str.equalsIgnoreCase("y"))
                {
                    while(true)
                    {
                        System.out.println("Load File:");
                        str = scan.nextLine();
                        String LF_REGEX = "Load \\S*";
                        Pattern LF_Pattern = Pattern.compile(LF_REGEX);
                        Matcher LF_Matcher = LF_Pattern.matcher(str);
                        if (LF_Matcher.matches())
                        {
                            String fileName = str.split(" ")[1];
                            File file = new File(fileName);
                            if (file.exists())
                            {
                                loadFile(file);
                            } else
                            {
                                System.out.printf("File:`%s` not Exist.\n", fileName);
//                                file.createNewFile();
                                continue;
                            }
                            break;
                        }
                    }
                    break;
                }
                else if (str.equalsIgnoreCase("n"))
                {
                    System.out.println("\t STARTING TAXIS");
                    Main.map.readMap(MAP_PATH);
                    Main.GUI.LoadMap(Main.map.map, 80);
                    Main.light.readLight(LIGHT_PATH);
                    Main.taxiQueue.setTaxis();
                    break;
                }
            }
//            sleep(2000);
            Main.taxiQueue.startTaxis();
            System.out.println("Taxis run.");
            Main.light.start();
            System.out.println("Lights run.");
        } catch (Exception e) { e.printStackTrace(); }
        startTime = System.currentTimeMillis();
        String CR_REGEX = "\\[CR,\\(\\d*,\\d*?\\),\\(\\d*,\\d*\\)\\]";
        Pattern CR_Pattern = Pattern.compile(CR_REGEX);
        String CLOSE_REGEX = "\\[CLOSE,\\(\\d*,\\d*?\\),\\(\\d*,\\d*\\)\\]";
        Pattern CLOSE_Pattern = Pattern.compile(CLOSE_REGEX);
        String OPEN_REGEX = "\\[OPEN,\\(\\d*,\\d*?\\),\\(\\d*,\\d*\\)\\]";
        Pattern OPEN_Pattern = Pattern.compile(OPEN_REGEX);
        String CHECK_REGEX = "\\[CHECK,\\d*\\]";
        Pattern CHECK_Pattern = Pattern.compile(CHECK_REGEX);

        for(int i = 0; i < MAX_REQUESTS; i++)
        {
            try
            {
                String str = requestString.isEmpty() ? scan.nextLine() : requestString.remove(0);
                System.out.println("Request:" + str);
                str = str.replace(" ","");
                if (str.equalsIgnoreCase("END"))
                    System.exit(0);
//                if (str.equalsIgnoreCase(""))
//                {
//                    i--;
//                    continue;
//                }
                Matcher CR_Matcher = CR_Pattern.matcher(str);
                Matcher CLOSE_Matcher = CLOSE_Pattern.matcher(str);
                Matcher OPEN_Matcher = OPEN_Pattern.matcher(str);
                Matcher CHECK_Matcher = CHECK_Pattern.matcher(str);

                if (CHECK_Matcher.matches())
                {
                    String[] args = str.split("\\[|\\]|,");
                    int index = Integer.parseInt(args[2]);
                    if (Main.taxiQueue.getTaxiType(index) && index >= 0 && index < 30)
                    {
                        System.out.printf("%sCHECK FOR VIP TAXI%d%s\n", LINE, index, LINE);
                        ListIterator<HistoryRecord> iterator = Main.taxiQueue.getIterator(index);
                        while(iterator.hasNext())
                        {
                            iterator.next().printHistory();
                        }
                    } else
                    {
                        System.out.println("Invalid VIP Taxi index: " + index);
                    }
                    i--;
                    continue;
                }
                if (!CR_Matcher.matches() && !CLOSE_Matcher.matches() && !OPEN_Matcher.matches())
                {
                    System.out.println("Invalid Input.");
                    i--;
                    continue;
                }
                String[] args = str.split("\\(|\\)|,");
                Point srcPoint = new Point(Integer.parseInt(args[2]), Integer.parseInt(args[3]));
                Point dstPoint = new Point(Integer.parseInt(args[6]), Integer.parseInt(args[7]));
                if (srcPoint.x < 0 | srcPoint.x > 79 | srcPoint.y < 0 | srcPoint.y > 79 | dstPoint.x < 0 | dstPoint.x > 79 | dstPoint.y < 0 | dstPoint.y > 79)
                {
                    System.out.println("Invalid Position.");
                    i--;
                    continue;
                }
                if (srcPoint.x == dstPoint.x && srcPoint.y == dstPoint.y)
                {
                    System.out.println("srcPoint and dstPoint is the same.");
                    i--;
                    continue;
                }
                if (CLOSE_Matcher.matches() | OPEN_Matcher.matches())
                {
                    if ((srcPoint.x == dstPoint.x && abs(srcPoint.y - dstPoint.y) == 1) || (abs(srcPoint.x - dstPoint.x) == 1 && srcPoint.y == dstPoint.y))
                    {
                        synchronized (GUIGv.m.graph)
                        {
                            if (OPEN_Matcher.matches() && !GUIGv.m.isConnect(srcPoint.x*80+srcPoint.y, dstPoint.x*80+dstPoint.y, true))
                            {
                                System.out.println("尝试打开不连通的道路");
                                i--;
                                continue;
                            }
                            i--;
                            GUIGv.m.graph[srcPoint.x*80+srcPoint.y][dstPoint.x*80+dstPoint.y] = CLOSE_Matcher.matches() ? gv.MAXNUM : 1;
                            GUIGv.m.graph[dstPoint.x*80+dstPoint.y][srcPoint.x*80+srcPoint.y] = CLOSE_Matcher.matches() ? gv.MAXNUM : 1;
                            Main.GUI.SetRoadStatus(srcPoint, dstPoint, CLOSE_Matcher.matches() ? 0 : 1);
                            System.out.printf("(%d,%d)->(%d,%d) Road status:%s\n", srcPoint.x, srcPoint.y, dstPoint.x, dstPoint.y, GUIGv.m.isConnect(srcPoint.x*80+srcPoint.y, dstPoint.x*80+dstPoint.y, false) ? "connect" : "disconnect");
//                            System.out.printf("srcPoint.x:%d srcPoint.y:%d, dstPoint.x:%d+dstPoint.x");
//                            System.out.println(GUIGv.m.isConnect(srcPoint.x*80+srcPoint.y, dstPoint.x*80+dstPoint.y));
//                            System.out.println(GUIGv.m.isConnect(41*80+36, 41*80+37));
                        }
                    }
                    else
                    {
                        System.out.println("Invalid Road.");
                        i--;
                    }
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
                i--;
                System.out.println("Input Format Error.");
//                e.printStackTrace();
            }
        }
    }

    /** @REQUIRES: 0 <= i < threads.length;
     * @MODIFIES:  this;
     * @EFFECTS: None;
     */
    public Point getSrcPoint(int i)
    {
        return threads.get(i).getSrcPoint();
    }

    /** @REQUIRES: 0 <= i < threads.length;
     * @MODIFIES:  this;
     * @EFFECTS: None;
     */
    public long getTime(int i)
    {
        return threads.get(i).getTime();
    }


    /** @REQUIRES: 0 <= i < threads.length
     * @MODIFIES: None
     * @EFFECTS: None
     */
    public Point getDstPoint(int i)
    {
        return threads.get(i).getDstPoint();
    }

    /** @REQUIRES: filePointer.equals(filePointer是一个有效的Load file指定的文件指针);
     * @MODIFIES: this;
     * @EFFECTS: \result.equals(对loadFile内的信息进行处理，按输入信息初始化地图、出租车、路线等);
     */
    public void loadFile(File filePointer)
    {
        try
        {
            String str;
            System.out.printf("File:`%s` Loaded\n", filePointer.getAbsolutePath());
            Scanner file = new Scanner(filePointer);
            while(!file.nextLine().equals("#map"));
            int count = 0;
            while(!(str = file.nextLine()).equals("#end_map"))
            {
                Main.map.readMap(str);
                System.out.println("Map:" + str);
                Main.GUI.LoadMap(Main.map.map, 80);
                count++;
            }
            if (count == 0)
            {
                Main.map.readMap(MAP_PATH);
                System.out.println("Map:" + MAP_PATH);
                Main.GUI.LoadMap(Main.map.map, 80);
            }
            while(!file.nextLine().equals("#light"));
            count = 0;
            while(!(str = file.nextLine()).equals("#end_light"))
            {
                System.out.println("Light:" + str);
                Main.light.readLight(str);
                count++;
            }
            if (count == 0)
            {
                str = LIGHT_PATH;
                System.out.println("Light:" + str);
                Main.light.readLight(str);
            }
            Main.taxiQueue.setTaxis();
//            sleep(2000);
            while(!file.nextLine().equals("#flow"));
            while(!(str = file.nextLine()).equals("#end_flow"))
            {
                String[] temp = str.split("\\(|,|\\)| ");
                    for (int i = 0; i < Integer.parseInt(temp[8]); i++)
                    {
//                        System.out.println(i);
                        GUIGv.AddFlow(Integer.parseInt(temp[1]), Integer.parseInt(temp[2]), Integer.parseInt(temp[5]), Integer.parseInt(temp[6]));
                    }
            }
            while(!file.nextLine().equals("#taxi"));
            while(!(str = file.nextLine()).equals("#end_taxi"))
            {
                String[] temp = str.split("\\(|,|\\)| ");
                int i = Integer.parseInt(temp[0]);
                int status = Integer.parseInt(temp[1]) == 3 ? STOP : WAITING;
                int credit = Integer.parseInt(temp[2]);
                int x = Integer.parseInt(temp[4]);
                int y = Integer.parseInt(temp[5]);
                Main.taxiQueue.initTaxi(i, status, credit, x, y);
            }
            while(!file.nextLine().equals("#request"));
            while(!(str = file.nextLine()).equals("#end_request"))
            {
                requestString.addLast(str);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("WRONG FORMAT OF LOADFILE");
            System.exit(0);
        }
    }

}
