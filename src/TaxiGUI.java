import java.awt.*;

public class TaxiGUI
{// GUI接口类

    public void LoadMap(int[][] map, int size)
    {
        guigv.m.map = new int[size + 5][size + 5];
        // 复制地图
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                guigv.m.map[i][j] = map[i][j];
            }
        }
        // 开始绘制地图,并每100ms刷新
        new myform();
        Thread th = new Thread(new Runnable()
        {
            public void run()
            {
                while (true)
                {
                    gv.stay(100);
                    guigv.drawboard.repaint();
                }
            }
        });
        th.start();
        guigv.m.initmatrix();// 初始化邻接矩阵
    }

    public void SetTaxiStatus(int index, Point point, int status)
    {
        guitaxi gt = guigv.taxilist.get(index);
        gt.x = point.x;
        gt.y = point.y;
        gt.status = status;
    }

    public void RequestTaxi(Point src, Point dst)
    {
        // 将src周围标红
        guigv.srclist.add(src);
        // 计算最短路径的值,通过一个窗口弹出
        int distance = guigv.m.distance(src.x, src.y, dst.x, dst.y);
        debugform form1 = new debugform();
        form1.text1.setText("从(" + src.x + "," + src.y + ")到(" + dst.x + "," + dst.y + ")的最短路径长度是" + distance);
    }

    public TaxiGUI()
    {
        // 初始化taxilist
        for (int i = 0; i < 101; i++)
        {
            guitaxi gt = new guitaxi();
            guigv.taxilist.add(gt);
        }
    }
}
