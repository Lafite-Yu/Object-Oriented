// this file is separated from gui.java

import java.awt.*;

public class TaxiGUI
{// GUI接口类

    public void LoadMap(int[][] map, int size)
    {
        GUIGv.m.map = new int[size + 5][size + 5];
        GUIGv.lightmap=new int[size+5][size+5];//初始化红绿灯
        // 复制地图
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                GUIGv.m.map[i][j] = map[i][j];
            }
        }
        // 开始绘制地图,并每100ms刷新
        new myform();
        Thread th = new Thread(new Runnable()
        {
            public void run()
            {
                int timewindow = 500;//时间窗设置为200ms
                int timecount = 0;//计时
                while (true)
                {
                    gv.stay(100);
                    timecount += 100;
                    if (timecount > timewindow)
                    {
                        timecount = 0;
                        //重新记录流量信息
                        GUIGv.ClearFlow();
                    }
                    GUIGv.drawboard.repaint();
                }
            }
        });
        th.start();
        GUIGv.m.initmatrix();// 初始化邻接矩阵
    }

    public void SetTaxiType(int index, int type) {
        guitaxi gt = GUIGv.taxilist.get(index);
        gt.type = type;
    }

    public void SetTaxiStatus(int index, Point point, int status)
    {
        guitaxi gt = GUIGv.taxilist.get(index);
        GUIGv.AddFlow(gt.x, gt.y, point.x, point.y);//统计流量
        gt.x = point.x;
        gt.y = point.y;
        gt.status = status;
    }

    public void RequestTaxi(Point src, Point dst)
    {
        // 将src周围标红
        GUIGv.srclist.add(src);
        // 计算最短路径的值,通过一个窗口弹出
        int distance = GUIGv.m.distance(src.x, src.y, dst.x, dst.y, false);
//        debugform form1 = new debugform();
//        form1.text1.setText("从(" + src.x + "," + src.y + ")到(" + dst.x + "," + dst.y + ")的最短路径长度是" + distance);
    }

    public void SetLightStatus(Point p,int status)
    {
        //设置红绿灯 status 0 没有红绿灯 1 东西方向为绿灯 2 东西方向为红灯
        GUIGv.lightmap[p.x][p.y]=status;
    }

    public int getLightStatus(Point p)
    {
        return GUIGv.lightmap[p.x][p.y];
    }

    public void SetRoadStatus(Point p1, Point p2, int status)
    {
        // status 0关闭 1打开
        synchronized (GUIGv.m.map)
        {
            int di = p1.x - p2.x;
            int dj = p1.y - p2.y;
            Point p = null;
            if (di == 0)
            {// 在同一水平线上
                if (dj == 1)
                {// p2-p1
                    p = p2;
                } else if (dj == -1)
                {// p1-p2
                    p = p1;
                } else
                {
                    return;
                }
                if (status == 0)
                {// 关闭
                    if (GUIGv.m.map[p.x][p.y] == 3)
                    {
                        GUIGv.m.map[p.x][p.y] = 2;
                    } else if (GUIGv.m.map[p.x][p.y] == 1)
                    {
                        GUIGv.m.map[p.x][p.y] = 0;
                    }
                } else if (status == 1)
                {// 打开
                    if (GUIGv.m.map[p.x][p.y] == 2)
                    {
                        GUIGv.m.map[p.x][p.y] = 3;
                    } else if (GUIGv.m.map[p.x][p.y] == 0)
                    {
                        GUIGv.m.map[p.x][p.y] = 1;
                    }
                }
            } else if (dj == 0)
            {// 在同一竖直线上
                if (di == 1)
                {// p2-p1
                    p = p2;
                } else if (di == -1)
                {// p1-p2
                    p = p1;
                } else
                {
                    return;
                }
                if (status == 0)
                {// 关闭
                    if (GUIGv.m.map[p.x][p.y] == 3)
                    {
                        GUIGv.m.map[p.x][p.y] = 1;
                    } else if (GUIGv.m.map[p.x][p.y] == 2)
                    {
                        GUIGv.m.map[p.x][p.y] = 0;
                    }
                } else if (status == 1)
                {// 打开
                    if (GUIGv.m.map[p.x][p.y] == 1)
                    {
                        GUIGv.m.map[p.x][p.y] = 3;
                    } else if (GUIGv.m.map[p.x][p.y] == 0)
                    {
                        GUIGv.m.map[p.x][p.y] = 2;
                    }
                }
            }
            return;
        }
    }

    public TaxiGUI()
    {
        // 初始化taxilist
        for (int i = 0; i < 101; i++)
        {
            guitaxi gt = new guitaxi();
            GUIGv.taxilist.add(gt);
        }
    }
}
