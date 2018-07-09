// version 1.2

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Poly
{
    private int[] num;

    public Poly()
    {
        num = new int[1000050];
    }

    public void createPoly(String str)
    {
        String REGEX1 = "\\((-|\\+)?\\d{1,6},((-0{1,6})|(\\+?\\d{1,6}))\\)";
        Pattern P = Pattern.compile(REGEX1);
        Matcher M = P.matcher(str);
        String temp;
        int c = 0, n = 0;

        while(M.find())
        {
            temp = str.substring(M.start()+1, M.end()-1);
//            System.out.println(temp);

            String REGEX2 = "-?\\d+";
            Pattern P2 = Pattern.compile(REGEX2);
            Matcher M2 = P2.matcher(temp);
            if(M2.find())
                c = Integer.parseInt( temp.substring(M2.start(), M2.end()) );
            if(M2.find())
                n = Integer.parseInt( temp.substring(M2.start(), M2.end()) );

//            System.out.println("c:"+c+"\tn:"+n);

            num[n] = c;
        }

    }

    public int getNum(int n)
    {
        return num[n];
    }

    public void calc(Poly SourcePoly, int op)
    {
        if (op == 0) // add
        {
            for (int i = 0; i < num.length; i++)
            {
                this.num[i] += SourcePoly.getNum(i);
            }
        }
        else // minus
        {
            for (int i = 0; i < num.length; i++)
            {
                this.num[i] -= SourcePoly.getNum(i);
            }
        }
    }

    public void print()
    {
        int flag = 0;
        for (int i = 0; i < num.length; i++)
        {
            if (num[i] != 0)
            {
                if (flag == 1)
                    System.out.print(",");
                else
                    System.out.print("{");
                System.out.print("(" + num[i] + "," + i + ")");
                flag = 1;
            }
        }
        if (flag == 0)
            System.out.print("0");
        else
            System.out.print("}");
    }

}
