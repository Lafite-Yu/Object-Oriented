import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Map implements DEFINE
{
    /** @OVERVIEW: 从文件读入地图;
     * @INHERIT: None;
     * @INVARIANT: None;
     */
    int[][] map = new int[80][80];

    public boolean repOK()
    {
        if (map != null)
            return true;
        else
            return false;
    }

    /** @REQUIRES:  mapFile.equals(mapFile是一个有效的地图文件的路径);
     * @MODIFIES: this;
     * @EFFECTS: \result.equals将指定的地图读入map);
     */
    public void readMap(String mapFile)
    {
        //读入地图信息
        //Requires:String类型的地图路径,System.in
        //Modifies:System.out,map[][]
        //Effects:从文件中读入地图信息，储存在map[][]中

        System.out.println("\t"+"LOADING MAP");

        Scanner scan = null;
        File file = new File(mapFile);
        if (file.exists() == false)
        {
            System.out.println("地图文件不存在,程序退出");
            System.exit(0);
            return;
        }
        try
        {
            scan = new Scanner(file);
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
                System.exit(0);
            }
            for (int j = 0; j < 80; j++)
            {
                try
                {
                    this.map[i][j] = Integer.parseInt(strArray[j]);
                } catch (Exception e)
                {
                    System.out.println("地图文件信息有误，程序退出");
                    System.exit(0);
                }
            }
        }
        scan.close();
    }


}
