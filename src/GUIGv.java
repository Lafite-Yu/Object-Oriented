// this file is separated from gui.java

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class GUIGv
{
    public static GUIInfo m = new GUIInfo();// 地图备份
    public static CopyOnWriteArrayList<guitaxi> taxilist = new CopyOnWriteArrayList<guitaxi>();// 出租车列表
    public static CopyOnWriteArrayList<Point> srclist = new CopyOnWriteArrayList<Point>();// 出发点列表
    public static HashMap<String, Integer> flowmap = new HashMap<String, Integer>();//当前流量
    public static HashMap<String, Integer> memflowmap = new HashMap<String, Integer>();//之前统计的流量
    /* GUI */
    public static JPanel drawboard;
    public static int[][] colormap;
    public static boolean redraw = false;
    public static int xoffset = 0;
    public static int yoffset = 0;
    public static int oldxoffset = 0;
    public static int oldyoffset = 0;
    public static int mousex = 0;
    public static int mousey = 0;
    public static double percent = 1.0;
    public static boolean drawstr = false;
    public static boolean drawflow = false;//是否绘制流量信息

    private static String Key(int x1, int y1, int x2, int y2)
    {//生成唯一的Key
        return "" + x1 + "," + y1 + "," + x2 + "," + y2;
    }

    public static void AddFlow(int x1, int y1, int x2, int y2)
    {//增加一个道路流量
        synchronized (GUIGv.flowmap)
        {
            //查询之前的流量数量
            int count = 0;
            count = GUIGv.flowmap.get(Key(x1, y1, x2, y2)) == null ? 0 : GUIGv.flowmap.get(Key(x1, y1, x2, y2));
            //添加流量
            GUIGv.flowmap.put(Key(x1, y1, x2, y2), count + 1);
            GUIGv.flowmap.put(Key(x2, y2, x1, y1), count + 1);
        }
    }

    public static int GetFlow(int x1, int y1, int x2, int y2)
    {//查询流量信息
        synchronized (GUIGv.memflowmap)
        {
            return GUIGv.memflowmap.get(Key(x1, y1, x2, y2)) == null ? 0 : GUIGv.memflowmap.get(Key(x1, y1, x2, y2));
        }
    }

    @SuppressWarnings("unchecked")
    public static void ClearFlow()
    {//清空流量信息
        synchronized (GUIGv.flowmap)
        {
            synchronized (GUIGv.memflowmap)
            {
                GUIGv.memflowmap = (HashMap<String, Integer>) GUIGv.flowmap.clone();
                GUIGv.flowmap = new HashMap<String, Integer>();

            }
        }
    }
}
