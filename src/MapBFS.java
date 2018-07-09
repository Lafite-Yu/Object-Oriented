import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

public class MapBFS
{
    public int[][] map;
    int[][] graph = new int[6405][6405];// 邻接矩阵
    int[][] D = new int[6405][6405];// 保存从i到j的最小路径值
    static HashMap<Integer, HashMap<Integer, Integer>> distance = new HashMap<>();

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

    public synchronized void pointbfs(int root) {// 单点广度优先搜索
        // Requires:int类型的点号root
        // Modifies:D[][],System.out
        // Effects:对整个地图进行广度优先搜索，获得任意点到root之间的最短路信息，储存在D[][]中
        int[] offset = new int[] { 0, 1, -1, 80, -80 };
        Vector<node> queue = new Vector<node>();
        boolean[] view = new boolean[6405];
        for (int i = 0; i < 6400; i++) {
            for (int j = 0; j < 6400; j++) {
                if (i == j) {
                    D[i][j] = 0;
                } else {
                    D[i][j] = graph[i][j];// 赋初值
                }
            }
        }
        int x = root;// 开始进行单点bfs
        HashMap<Integer, Integer> pointDistance = new HashMap<>();
        for (int i = 0; i < 6400; i++)
            view[i] = false;
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
                    pointDistance.put(next, n.depth + 1);
                }
            }
            queue.remove(0);// 退出队列
        }
        pointDistance.put(x, 0);
        distance.put(x, pointDistance);
        // 检测孤立点
        int count = 0;
        for (int i = 0; i < 6400; i++) {
            if (D[i][x] == gv.MAXNUM) {
                count++;
            }
        }
        if (count > 0) {
            System.out.println("地图并不是连通的,程序退出");
            System.exit(1);
        }
    }

    public int distance(int x1, int y1, int x2, int y2) {// 使用bfs计算两点之间的距离
        pointbfs(x1 * 80 + y1);
        return D[x1 * 80 + y1][x2 * 80 + y2];
    }

    public int getDis(Point src, Point dst)
    {// 使用bfs计算两点之间的距离
//        return distance(src.x, src.y, dst.x, dst.y);
//        pointbfs(x1 * 80 + y1);
//        return D[src.x * 80 + src.y][dst.x * 80 + dst.y];
//        if (!distance.containsKey(dst.x*80+src.y) || !distance.get(dst.x*80+dst.y).containsKey(src.x*80+src.y))
//            pointbfs(dst.x*80+dst.y);
        return distance.get(dst.x*80+dst.y).get(src.x * 80 + src.y);
    }

//    public int[] getDis(Point dst)
//    {
//        pointbfs(dst.x*80+dst.y);
//    }


}
