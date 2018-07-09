import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Requests
{
    private boolean start;
    private long previousTime;

    /** @REQUIRES:  None;
     * @MODIFIES: None;
     * @EFFECTS: None;
     */
    public boolean repOK()
    {
        return true;
    }

    public Requests()
    {
        start = false;
        previousTime = 0;

    }

//    public RequestsQueue readin()
//    {
//        RequestsQueue requestQueue = new RequestsQueue();
//
//        Scanner scan = new Scanner(System.in);
//        String str;
//        int counter = 0;
//        while (scan.hasNextLine())
//        {
//            str = scan.nextLine().replace(" ", "");
//            if (str.equals("RUN"))
//                break;
//            else if (this.judger(str))
//                requestQueue = this.creater(str, requestQueue);
//
//            counter += 1;
//            if (counter >= 100)
//                break;
//        }
//        scan.close();
//        return requestQueue;
//    }

    public boolean judger(String str)
    {
        // grammarCheck
        String REGEX = "(\\(FR,\\+?\\d{1,2},(UP|DOWN),\\+?\\d{1,10}\\))|(\\(ER,\\+?\\d{1,2},\\+?\\d{1,10}\\))";
        Pattern P = Pattern.compile(REGEX);
        Matcher M = P.matcher(str);
        if (!M.matches())
        {
            print(str, "不符合语法规则");
            return false;
        }
        // valueCheck
        String[] s = str.split("[(,)]");
        long floor = Long.parseLong(s[2]);
        if (floor > 10 || floor < 1)
        {
            print(str, "请求的楼层不合法");
            return false;
        }
        long time;
        if (s[1].equals("ER"))
        {
            time = Long.parseLong(s[3]);
        } else
        {
            time = Long.parseLong(s[4]);
            if (s[3].equals("UP"))
            {
                if (floor == 10)
                {
                    print(str, "尝试在10楼按下向上键");
                    return false;
                }
            } else if (s[3].equals("DOWN"))
            {
//                System.out.println("OJBK");
                if (floor == 1)
                {
                    print(str, "尝试在1楼按下向下键");
                    return false;
                }
            }
        }
        if (time < 0 || time > 2 * (long) Integer.MAX_VALUE + 1)
        {
            print(str, "时间超出范围");
            return false;
        }
        if (!start)
        {
            if (time != 0 || floor != 1)
            {
                print(str, "请求没有从0时刻开始");
                return false;
            } else
                start = true;
        }
        if (time < previousTime)
        {
            print(str, "没有按时间不减的顺序输入");
            return false;
        } else
            previousTime = time;
        return true;
    }

//    public RequestsQueue creater(String str, RequestsQueue requestQueue)
//    {
//
//
//        long[] info = new long[3];
//        String[] s = str.split("[(,)]");
//        info[1] = Long.parseLong(s[2]);
//        if (s[1].equals("ER"))
//        {
//            info[0] = 0;
//            info[2] = Long.parseLong(s[3]);
//        } else if (s[1].equals("FR"))
//        {
//            info[2] = Long.parseLong(s[4]);
//            if (s[3].equals("UP"))
//                info[0] = 1;
//            else if (s[3].equals("DOWN"))
//                info[0] = 2;
//        }
//        requestQueue.push(info);
//        return requestQueue;
//    }

    public void print(String input, String reason)
    {
        System.out.printf("INVALID[%s]\n", input);
//        System.out.println("# 非法请求:" + reason);
    }
}
