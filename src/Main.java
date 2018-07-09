// version: 1.2

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main
{

    public static void main(String[] args)
    {
        // TODO Auto-generate	d method stub

        try
        {
            Scanner inputScan = new Scanner(System.in);
            ComputePoly result = new ComputePoly();
            Poly temp = new Poly();
            int op;
            int flag = 0;
            int errorCode = 0;
            int PolyCount = 0;

            String input = inputScan.nextLine();
            String str;
            str = input.replace(" ", "");

            String REGEX = "\\{((\\((-|\\+)?\\d{1,6},((-0{1,6})|(\\+?\\d{1,6}))\\)),){0,49}(\\((-|\\+)?\\d{1,6},((-0{1,6})|(\\+?\\d{1,6}))\\))}";

            Pattern P = Pattern.compile(REGEX);

            while (str.length() > 0)
            {
                if (PolyCount >= 20)
                {
                    errorCode = 3; // 多项式数目超出
                    break;
                }
                if (str.charAt(0) == '+')
                {
                    op = 0;
                    str = str.substring(1);
                } else if (str.charAt(0) == '-')
                {
                    op = 1;
                    str = str.substring(1);
                } else if (flag == 0 && str.charAt(0) == '{')
                    op = 0;
                else
                {
                    errorCode = 1; // 多项式之间的符号错误
                    break;
                }

                flag = 1;

                Matcher M = P.matcher(str);

                if (M.lookingAt())
                {
                    //                System.out.println(M.start() + " | " + M.end());
                    //                System.out.println(str.substring(M.start() + 1, M.end() - 1));

                    PolyCount += 1;
                    temp = new Poly();
                    temp.createPoly(str.substring(M.start() + 1, M.end() - 1));
                    result.compute(temp, op);

                    str = str.substring(M.end());
                } else
                {
                    errorCode = 2; // 某个多项式内输入不合法
                    break;
                }
            }

            inputScan.close();
            if (errorCode == 0)
            {
                if (PolyCount == 0)
                {
                    System.out.println("ERROR");
                    System.out.println("# 没有输入多项式，请检查输入");
                } else
                    result.print();

            } else if (errorCode == 1)
            {
                System.out.println("ERROR");
                System.out.println("# 多项式之间的符号错误");
            } else if (errorCode == 2)
            {
                System.out.println("ERROR");
                System.out.println("# 某个多项式输入有误");
            } else if (errorCode == 3)
            {
                System.out.println("ERROR");
                System.out.println("# 多项式总数超出20项");
            }
        }
        catch (Exception E)
        {
            System.out.println("ERROR");
        }
    }

}
