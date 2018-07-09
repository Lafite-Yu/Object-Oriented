import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Map implements DEFINE
{
    int[][] map = new int[80][80];

    public void readMap()
    {
        //读入地图信息
        //Requires:String类型的地图路径,System.in
        //Modifies:System.out,map[][]
        //Effects:从文件中读入地图信息，储存在map[][]中

        System.out.println("\t"+"LOADING MAP");

        Scanner scan = null;
        File file = new File(MAP_PATH);
        if (file.exists() == false)
        {
            System.out.println("地图文件不存在,程序退出");
            System.exit(1);
            return;
        }
        try
        {
            scan = new Scanner(new File(MAP_PATH));
        } catch (FileNotFoundException e)
        {

        }
        for (int i = 0; i < 80; i++)
        {
            String[] strArray = null;
            try
            {
                strArray = scan.nextLine().split("");
            } catch (Exception e)
            {
                System.out.println("地图文件信息有误，程序退出");
                System.exit(1);
            }
            for (int j = 0; j < 80; j++)
            {
                try
                {
                    this.map[i][j] = Integer.parseInt(strArray[j]);
                } catch (Exception e)
                {
                    System.out.println("地图文件信息有误，程序退出");
                    System.exit(1);
                }
            }
        }
        scan.close();
    }


    public boolean rightConnect(Point point)
    {
        if (point.x < 80 && point.x >= 0 && point.y < 80 && point.y >= 0)
            if (map[point.x][point.y] == R || map[point.x][point.y] == RD)
                return true;
        return false;
    }

    public boolean downConnect(Point point)
    {
        if (point.x < 80 && point.x >= 0 && point.y < 80 && point.y >= 0)
            if (map[point.x][point.y] == D || map[point.x][point.y] == RD)
                return true;
        return false;
    }

    public int getDistant(Point src, Point dst)
    {
//        System.out.printf("%d %d %d %d %d\n", src.x, src.y, dst.x, dst.y, guigv.m.distance(src.x, src.y, dst.x, dst.y));
//        return guigv.m.distance(src.x, src.y, dst.x, dst.y);
        return guigv.m.getDis(src, dst);
    }

    public int getShortestMove(Point src, Point dst)
    {
//        System.out.printf("(%d,%d) this:%d, UP:%d DOWN:%d LEFT:%d RIGHT:%d\n", src.x, src.y, getDistant(src, dst), getDistant(new Point(src.x-1, src.y), dst), getDistant(new Point(src.x+1, src.y), dst), getDistant(new Point(src.x, src.y-1), dst), getDistant(new Point(src.x, src.y+1), dst));
        int distance = getDistant(src, dst);
        if (src.x > 0)
        {
            if (getDistant(new Point(src.x-1, src.y), dst) + 1 == distance && Main.map.downConnect(new Point(src.x-1, src.y))) // UP
                return UP;
        }
        if (src.x < 79)
        {
            if (getDistant(new Point(src.x+1, src.y), dst) + 1 == distance && Main.map.downConnect(src)) // DOWN
                return DOWN;
        }
        if (src.y > 0)
        {
            if (getDistant(new Point(src.x, src.y-1), dst) + 1 == distance && Main.map.rightConnect(new Point(src.x, src.y-1))) // LEFT
                return LEFT;
        }
        if (src.y < 79)
        {
            if (getDistant(new Point(src.x, src.y+1), dst) + 1 == distance && Main.map.rightConnect(src)) // RIGHT
                return RIGHT;
        }
        return 10;
    }

}
