// this file is separated from gui.java

import java.awt.*;
import java.util.LinkedList;
import java.util.Vector;

public class GUIInfo
{
    class Node
    {
        Point point = new Point();
        Point frontPoint = new Point();
        int flux = 0;
        int depth = gv.MAXNUM;

        public Node(int point, int frontPoint, int flux, int depth)
        {
            this.point = new Point(point/80, point%80);
            this.frontPoint = new Point(frontPoint/80, frontPoint%80);
            this.flux = flux;
            this.depth = depth;
        }

        public void setNode(int point, int frontPoint, int flux, int depth)
        {
            this.point = new Point(point/80, point%80);
            this.frontPoint = new Point(frontPoint/80, frontPoint%80);
            this.flux = flux;
            this.depth = depth;
        }
    }

    public int[][] map;
    int[][] graph = new int[6405][6405];// 邻接矩阵
    int[][] D = new int[6405][6405];// 保存从i到j的最小路径值
    Node[] nodes = new Node[6405];

    public void initmatrix()
    {
        // 初始化邻接矩阵
        // Requires:无
        // Modifies:graph[][]
        // Effects:对邻接矩阵赋初值
        int MAXNUM = gv.MAXNUM;
        for (int i = 0; i < 6400; i++)
        {
            for (int j = 0; j < 6400; j++)
            {
                if (i == j)
                    graph[i][j] = 0;
                else
                    graph[i][j] = MAXNUM;
            }
        }
        for (int i = 0; i < 80; i++)
        {
            for (int j = 0; j < 80; j++)
            {
                if (map[i][j] == 1 || map[i][j] == 3)
                {
                    graph[i * 80 + j][i * 80 + j + 1] = 1;
                    graph[i * 80 + j + 1][i * 80 + j] = 1;
                }
                if (map[i][j] == 2 || map[i][j] == 3)
                {
                    graph[i * 80 + j][(i + 1) * 80 + j] = 1;
                    graph[(i + 1) * 80 + j][i * 80 + j] = 1;
                }
            }
        }

    }

    public void pointbfs(int root)
    {
        // 单点广度优先搜索
        // Requires:int类型的点号root
        // Modifies:D[][],System.out
        // Effects:对整个地图进行广度优先搜索，获得任意点到root之间的最短路信息，储存在D[][]中
        int[] offset = new int[]{0, 1, -1, 80, -80};
        Vector<node> queue = new Vector<node>();
        boolean[] view = new boolean[6405];
//        for (int i = 0; i < 6400; i++)
//        {
//            for (int j = 0; j < 6400; j++)
//            {
//                if (i == j)
//                {
//                    D[i][j] = 0;
//                } else
//                {
//                    D[i][j] = graph[i][j];// 赋初值
//                }
//            }
//        }
        int x = root;// 开始进行单点bfs
//        for (int i = 0; i < 6400; i++)
//            view[i] = false;
        queue.add(new node(x, 0));
        while (queue.size() > 0)
        {
            node n = queue.get(0);
            view[n.NO] = true;
            for (int i = 1; i <= 4; i++)
            {
                int next = n.NO + offset[i];
                if (next >= 0 && next < 6400 && view[next] == false && graph[n.NO][next] == 1)
                {
                    view[next] = true;
                    queue.add(new node(next, n.depth + 1));// 加入遍历队列
                    D[x][next] = n.depth + 1;
                    D[next][x] = n.depth + 1;
                }
            }
            queue.remove(0);// 退出队列
        }
//        // 检测孤立点
//        int count = 0;
//        for (int i = 0; i < 6400; i++)
//        {
//            if (D[i][x] == gv.MAXNUM)
//            {
//                count++;
//            }
//        }
//        if (count > 0)
//        {
//            System.out.println("地图并不是连通的,程序退出");
//            System.exit(1);
//        }
    }

    public void liteFluxBFS(int root)
    {
        // 应对请求的发出地进行BFS，可同时得到接乘客、送乘客的两条最短路径
        // Requires:int类型的点号root
        // Modifies:D[][],System.out
        // Effects:对整个地图进行广度优先搜索，获得任意点到root之间的最短路信息，储存在D[][]中
        int[] offset = new int[]{0, 1, -1, 80, -80};
        Vector<node> queue = new Vector<node>();
        boolean[] view = new boolean[6405];

        int x = root;// 开始进行单点bfs
//        System.out.println(x);
        queue.add(new node(x, 0));
        this.nodes[x] = new Node(x, x, 0, 0);
        while (queue.size() > 0)
        {
            node n = queue.get(0);
            view[n.NO] = true;
            for (int i = 1; i <= 4; i++)
            {
                int next = n.NO + offset[i];
                if (next >= 0 && next < 6400 && graph[n.NO][next] == 1)
                {
                    if (view[next] == false)
                    {
                        view[next] = true;
                        queue.add(new node(next, n.depth + 1));// 加入遍历队列
                        nodes[next] = new Node(next, n.NO, nodes[n.NO].flux+GUIGv.GetFlow(n.NO/80, n.NO%80, next/80, next%80), n.depth+1);
                    } else
                    {
                        if (nodes[next].depth - n.depth == 1)
                        {
                            if (nodes[next].flux > nodes[n.NO].flux+GUIGv.GetFlow(n.NO/80, n.NO%80, next/80, next%80))
                            {
                                nodes[next].setNode(next, n.NO, nodes[n.NO].flux+GUIGv.GetFlow(n.NO/80, n.NO%80, next/80, next%80), n.depth+1);
                            }
                        }
                    }
                }
            }
            queue.remove(0);// 退出队列
        }
    }

    public synchronized LinkedList<Integer> getPath(Point cur, Point src, Point dst, boolean reachedSrc)
    {
        synchronized (GUIGv.m.map)
        {
            if (!reachedSrc)
            {
                this.liteFluxBFS(src.x*80 + src.y);
                LinkedList<Integer> movePath = new LinkedList<>();
                LinkedList<Integer> srcToDst = new LinkedList<>();
                int point = dst.x*80+dst.y;
                while (point != src.x*80+src.y)
                {
                    srcToDst.addFirst(point);
                    Point curPoint = nodes[point].frontPoint;
                    point = curPoint.x*80+curPoint.y;
                }
                srcToDst.addFirst(src.x*80+src.y);
                point = cur.x*80+cur.y;
                while (point != src.x*80+src.y)
                {
                    movePath.addLast(point);
                    Point curPoint = nodes[point].frontPoint;
                    point = curPoint.x*80+curPoint.y;
                }
                for (Integer aInt: srcToDst)
                {
                    movePath.addLast(aInt);
                }
                return movePath;
            } else
            {
                this.liteFluxBFS(dst.x*80 + dst.y);
                LinkedList<Integer> movePath = new LinkedList<>();
                int point = cur.x*80+cur.y;
                while (point != dst.x*80+dst.y)
                {
                    movePath.addLast(point);
                    Point curPoint = nodes[point].frontPoint;
                    point = curPoint.x*80+curPoint.y;
                }
                movePath.addLast(point);
                return movePath;
            }

        }
    }

    public int distance(int x1, int y1, int x2, int y2)
    {// 使用bfs计算两点之间的距离
        pointbfs(x1 * 80 + y1);
        return D[x1 * 80 + y1][x2 * 80 + y2];
    }

    public int getDis(Point src, Point dst)
    {
        synchronized (GUIGv.m.map)
        {
            return distance(src.x, src.y, dst.x, dst.y);
        }
    }

    public boolean isConnect(int src, int dst)
    {
        synchronized (graph)
        {
            return graph[src][dst] == 1;
        }
    }
}
