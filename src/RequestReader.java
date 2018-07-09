import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestReader
{
    /** @OVERVIEW: 读入并处理请求;
     * @Abstract_Function: AF(rr) == (index, readCount, previousTime) where index == rr.index, readCount == rr.readCount, previousTime == rr.previousTime;
     * @INVARIANT: index >= -1, readCount >= -1, previousTime >= 0;
     */

    private int index = -1;
    private int readCount = -1;
    private long previousTime = 0;

    /**@EFFECTS: \result == I_CLASS(\this);
     */
    public boolean repOK()
    {
        if (index >= -1 && readCount >= -1 && previousTime >= 0)
            return true;
        return false;
    }

    /** @REQUIRES:  System.in;
     * @MODIFIES: this.index, this.readCount, this.previousTime;
     * @EFFECTS: 对于输入的某行指令instr，如果符合要求 ==> 生成相应的Request并添加进RequestQueue
     *                                    不符合要求 ==> 打印输出提示
     *           如果输入的是代表停止输入的字符或者达到行数上限 ==> 终止输入
     */
	public void readin()
	{
		Scanner scan = new Scanner(System.in);
		String str;
		while(scan.hasNextLine())
		{
			str = scan.nextLine().replace(" ","");
			if(str.equals("RUN"))
				break;
            readCount += 1;
            if (readCount > 100)
                break;

            // grammarCheck
            String REGEX= "(\\(FR,\\+?\\d{1,2},(UP|DOWN),\\+?\\d{1,10}\\))|(\\(ER,\\+?\\d{1,2},\\+?\\d{1,10}\\))";
            Pattern P = Pattern.compile(REGEX);
            Matcher M = P.matcher(str);
            if (!M.matches())
            {
                print(str, "不符合语法规则");
                continue;
            }
            // valueCheck
            String[] s = str.split("[(,)]");
            int floor = Integer.parseInt(s[2]);
            if (floor > 10 || floor < 1)
            {
                print(str, "请求的楼层不合法");
                continue;
            }
            long time;
            int type;
            if(s[1].equals("ER"))
            {
                time = Long.parseLong(s[3]);
                type = 0;
            }
            else
            {
                time = Long.parseLong(s[4]);
                if(s[3].equals("UP"))
                {
                    type = 1;
                    if (floor == 10)
                    {
                        print(str, "尝试在10楼按下向上键");
                        continue;
                    }
                }
                else
                {
                    type = 2;
                    if (floor == 1)
                    {
                        print(str, "尝试在1楼按下向下键");
                        continue;
                    }
                }
            }
            if (time < 0 || time > 2*(long)Integer.MAX_VALUE+1)
            {
                print(str, "时间超出范围");
                continue;
            }
            if (index < 0)
            {
                if (time != 0 || floor != 1 || type != 1)
                {
                    print(str, "第一条请求不是(FR,1,UP,0)");
                    continue;
                }
            }
            if (time < previousTime)
            {
                print(str, "没有按时间不减的顺序输入");
                continue;
            }
            else
                previousTime = time;

            index++;
            Main.requestsQueue.push(new Request(index, type, floor, time));

		}
		scan.close();
	}

    /** @REQUIRES:  input != null;
     * @MODIFIES: this.index, this.readCount, this.previousTime;
     * @EFFECTS: 对于输入的某行指令instr，如果符合要求 ==> 生成相应的Request并添加进RequestQueue
     *                                    不符合要求 ==> 打印输出提示
     */
	private void print(String input, String reason)
    {
        System.out.printf("INVALID[%s]\n", input);
//        System.out.println("# 非法请求:" + reason);
    }
}
