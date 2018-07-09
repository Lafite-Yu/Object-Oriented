// this file is separated from gui.java

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.lang.Math.abs;

public class GUIGv
{
    public static GUIInfo m = new GUIInfo();// 地图备份
    public static CopyOnWriteArrayList<guitaxi> taxilist = new CopyOnWriteArrayList<guitaxi>();// 出租车列表
    public static CopyOnWriteArrayList<Point> srclist = new CopyOnWriteArrayList<Point>();// 出发点列表
    public static HashMap<String, LinkedList<Long>> flowmap = new HashMap<>();//当前流量
    public static HashMap<String, LinkedList<Long>> memflowmap = new HashMap<>();//之前统计的流量
    /* GUI */
    public static JPanel drawboard;
    public static int[][] colormap;
    public static int[][] lightmap;
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
//            int count = 0;
//            count = GUIGv.flowmap.get(Key(x1, y1, x2, y2)) == null ? 0 : GUIGv.flowmap.get(Key(x1, y1, x2, y2)).size();
            long time = System.currentTimeMillis();
            //添加流量
            if ( GUIGv.flowmap.get(Key(x1, y1, x2, y2)) == null )
            {
                LinkedList<Long> flux = new LinkedList<>();
                flux.addLast(time);
                GUIGv.flowmap.put(Key(x1, y1, x2, y2), flux);
                GUIGv.flowmap.put(Key(x2, y2, x1, y1), flux);
            } else
            {
                GUIGv.flowmap.get(Key(x1, y1, x2, y2)).addLast(time);
                GUIGv.flowmap.get(Key(x2, y2, x1, y1)).addLast(time);
            }
//            GUIGv.flowmap.put(Key(x1, y1, x2, y2), count + 1);
//            GUIGv.flowmap.put(Key(x2, y2, x1, y1), count + 1);
        }
    }

    public static int GetFlow(int x1, int y1, int x2, int y2)
    {//查询流量信息
        synchronized (GUIGv.memflowmap)
        {
            long currentTime = System.currentTimeMillis();
            int count = GUIGv.flowmap.get(Key(x1, y1, x2, y2)) == null ? 0 : GUIGv.flowmap.get(Key(x1, y1, x2, y2)).size();
            if (GUIGv.memflowmap.get(Key(x1, y1, x2, y2))!=null)
            {
                for (Long time: GUIGv.memflowmap.get(Key(x1, y1, x2, y2)))
                {
                    if (abs(time-currentTime) <= 500)
                        count++;
                }
            }
            if (m.isConnect(x1*80+y1, x2*80+y2, false))
                return count;
            else
                return 0;
        }
    }

    @SuppressWarnings("unchecked")
    public static void ClearFlow()
    {//清空流量信息
        synchronized (GUIGv.flowmap)
        {
            synchronized (GUIGv.memflowmap)
            {
                GUIGv.memflowmap = (HashMap<String, LinkedList<Long>>) GUIGv.flowmap.clone();
                GUIGv.flowmap = new HashMap<>();
            }
        }
    }
}
