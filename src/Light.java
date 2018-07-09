import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

public class Light extends Thread implements DEFINE
{
    /** @OVERVIEW: 红绿灯信息的读入、初始化和后续更新;
     * @INHERIT: Thread;
     * @INVARIANT: ps, sps;
     */

    int[][] light = new int[85][85];
    LinkedList<Point> lights = new LinkedList<>();
    LinkedList<Integer> status = new LinkedList<>();

    public boolean repOK()
    {
        if (light != null && lights != null && status != null)
            return true;
        else
            return false;
    }

    /** @REQUIRES:  lightName.equals(lightName是一个有效的地图文件的路径);
     * @MODIFIES: this;
     * @EFFECTS: lightFile.exist() ==> (result == 将指定的路灯文件读入light、lights和status);
     * !lightFile.exist() ==> exit(0);
     */
    public void readLight(String lightName)
    {
        try
        {
            System.out.println("\t"+"LOADING LIGHT");
            File file = new File(lightName);
            Scanner scan = new Scanner(file);

            for (int i = 0; i < 80; i++)
            {
                String[] strArray = scan.nextLine().split("");
                for (int j = 0; j < 80; j++)
                {
                    this.light[i][j] = Integer.parseInt(strArray[j]);
                    if (this.light[i][j] == 1)
                    {
                        int count = 0;
                        Point position = new Point(i, j);
//            case UP:
                        if (GUIGv.m.isConnect(position.x * 80 + position.y, (position.x - 1) * 80 + position.y))
                            count++;
//            case DOWN:
                        if (GUIGv.m.isConnect(position.x * 80 + position.y, (position.x + 1) * 80 + position.y))
                            count++;
//            case LEFT:
                        if (GUIGv.m.isConnect(position.x * 80 + position.y, position.x * 80 + (position.y - 1)))
                            count++;
//            case RIGHT:
                        if (GUIGv.m.isConnect(position.x * 80 + position.y, position.x * 80 + (position.y + 1)))
                            count++;
                        if (count < 3)
                        {
                            System.out.printf("设立的红绿灯：(%d,%d) 不在道路交叉处，已被忽略\n", position.x, position.y);
                            Main.GUI.SetLightStatus(position, 0);
                            continue;
                        }
                        lights.addLast(new Point(i, j));
                        status.addLast(new Random().nextInt(2)+1);
                        Main.GUI.SetLightStatus(position, status.getLast());
                    } else
                    {
                        Main.GUI.SetLightStatus(new Point(i, j), 0);
                    }
                }
            }
            scan.close();
        }
        catch (Exception e)
        {
            System.out.println("红绿灯文件错误");
            System.exit(0);
        }
    }

    /** @REQUIRES:  None;
     * @MODIFIES: None;
     * @EFFECTS: this.threadStatus == runnable;
     */
    public void run()
    {
        try
        {
            int flashTime = new Random().nextInt(500)+500;
            while (true)
            {
                changeLight();
                sleep(flashTime);
            }
        }
        catch (Exception e) {}
    }

    /** @REQUIRES:  None
     * @MODIFIES: this;
     * @EFFECTS: (int 0<=i<lights.size(); light.get(i).isValid(position); status.get(i) == SN_ON ==> status.get(i) = WE_ON)
     * (int 0<=i<lights.size(); light.get(i).isValid(position); status.get(i) == WE_ON ==> status.get(i) = SN_ON)
     * @THREAD_EFFECTS: \locked(lights);
     */
    public void changeLight()
    {
        try
        {
            synchronized (lights)
            {
                for (int i = 0; i < lights.size(); i++)
                {
                    status.set(i, (status.get(i)==SN_ON ? WE_ON : SN_ON));
                    Main.GUI.SetLightStatus(lights.get(i), status.get(i));
                }
            }
        } catch (Exception e) {}
    }

    /** @REQUIRES:  position.isValid(position是一个有效的地图点)
     * @MODIFIES: None;
     * @EFFECTS: \result == (positon.in(lights) ==> getLight(position));
     *             \result == (!positon.in(lights) ==> OFF);
     * @THREAD_EFFECTS: \locked(lights);
     */
    public int getLight(Point position)
    {
        synchronized (lights)
        {
//            return Main.GUI.getLightStatus(position);
            if (!lights.contains(position))
                return OFF;
            else
                return status.get(lights.indexOf(position));
        }
    }
}
