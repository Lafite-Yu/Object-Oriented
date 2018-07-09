// this file is separated from gui.java

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

class gv
{// 常用工具
    public static int MAXNUM = 1000000;

    public static long getTime()
    {// 获得当前系统时间
        // Requires:无
        // Modifies:无
        // Effects:返回long类型的以毫秒计的系统时间
        return System.currentTimeMillis();
    }

    @SuppressWarnings("static-access")
    public static void stay(long time)
    {
        // Requires:long类型的以毫秒计的休眠时间
        // Modifies:无
        // Effects:使当前线程休眠time的时间
        try
        {
            Thread.currentThread().sleep(time);
        } catch (InterruptedException e)
        {

        }
    }

    public static void printTime()
    {
        // Requires:无
        // Modifies:System.out
        // Effects:在屏幕上打印HH:mm:ss:SSS格式的当前时间
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");
        System.out.println(sdf.format(new Date(getTime())));
    }

    public static String getFormatTime()
    {
        // Requires:无
        // Modifies:无
        // Effects:返回String类型的HH:mm:ss格式的时间
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(new Date(getTime()));
    }
}

class node
{// 结点信息
    int NO;
    int depth;

    public node(int _NO, int _depth)
    {
        // Requires:int类型的结点号,int类型的深度信息
        // Modifies:创建一个新的node对象，修改了此对象的NO,depth属性
        // Effects:创建了一个新的node对象并初始化
        NO = _NO;
        depth = _depth;
    }
}


class guitaxi
{
    public int x = 1;
    public int y = 1;
    public int status = -1;
}


class DrawBoard extends JPanel
{// 绘图板类
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        brush.draw(g2D);
    }
}

class myform extends JFrame
{// 我的窗体程序
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int left = 100;
    private int top = 100;
    private int width = 630;
    private int height = 600;

    public myform()
    {
        super();
        /* 设置按钮属性 */
        // button1
        JButton button1 = new JButton();// 创建一个按钮
        button1.setBounds(10, 515, 100, 40);// 设置按钮的位置
        button1.setText("重置");// 设置按钮的标题
        button1.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                GUIGv.xoffset = 0;
                GUIGv.yoffset = 0;
                GUIGv.oldxoffset = 0;
                GUIGv.oldyoffset = 0;
                GUIGv.percent = 1.0;
                GUIGv.drawboard.repaint();
            }
        });
        // button2
        JButton button2 = new JButton();// 创建一个按钮
        button2.setBounds(120, 515, 100, 40);// 设置按钮的位置
        button2.setText("放大");// 设置按钮的标题
        button2.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                GUIGv.percent += 0.1;
                GUIGv.drawboard.repaint();
            }
        });
        // button3
        JButton button3 = new JButton();// 创建一个按钮
        button3.setBounds(230, 515, 100, 40);// 设置按钮的位置
        button3.setText("缩小");// 设置按钮的标题
        button3.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                if (GUIGv.percent > 0.1)
                    GUIGv.percent -= 0.1;
                GUIGv.drawboard.repaint();
            }
        });
        // button4
        JButton button4 = new JButton();
        button4.setBounds(340, 515, 100, 40);
        button4.setText("清除轨迹");
        button4.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                // 清除colormap
                for (int i = 0; i < 85; i++)
                {
                    for (int j = 0; j < 85; j++)
                    {
                        GUIGv.colormap[i][j] = 0;
                    }
                }
                GUIGv.drawboard.repaint();
            }
        });
        /* 设置复选框属性 */
        JCheckBox check1 = new JCheckBox("显示位置");
        check1.setBounds(450, 515, 80, 40);
        check1.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                GUIGv.drawstr = check1.isSelected();
                GUIGv.drawboard.repaint();
            }
        });
        JCheckBox check2 = new JCheckBox("显示流量");
        check2.setBounds(530, 515, 80, 40);
        check2.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                GUIGv.drawflow = check2.isSelected();
                GUIGv.drawboard.repaint();
            }
        });
        /* 设置绘图区属性 */
        DrawBoard drawboard = new DrawBoard();// 创建新的绘图板
        drawboard.setBounds(10, 10, 500, 500);// 设置大小
        drawboard.setBorder(BorderFactory.createLineBorder(Color.black, 1));// 设置边框
        drawboard.setOpaque(true);
        drawboard.addMouseListener(new MouseListener()
        {
            public void mousePressed(MouseEvent e)
            {// 按下鼠标
                GUIGv.redraw = true;
                GUIGv.mousex = e.getX();
                GUIGv.mousey = e.getY();
            }

            public void mouseReleased(MouseEvent e)
            {// 松开鼠标
                GUIGv.oldxoffset = GUIGv.xoffset;
                GUIGv.oldyoffset = GUIGv.yoffset;
                GUIGv.redraw = false;
            }

            @Override
            public void mouseClicked(MouseEvent e)
            {
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
            }
        });
        drawboard.addMouseMotionListener(new MouseMotionAdapter()
        {// 添加鼠标拖动
            public void mouseDragged(MouseEvent e)
            {
                if (GUIGv.redraw == true)
                {
                    GUIGv.xoffset = GUIGv.oldxoffset + e.getX() - GUIGv.mousex;
                    GUIGv.yoffset = GUIGv.oldyoffset + e.getY() - GUIGv.mousey;
                    GUIGv.drawboard.repaint();
                }
            }
        });
        drawboard.addMouseWheelListener(new MouseWheelListener()
        {// 添加鼠标滚轮
            public void mouseWheelMoved(MouseWheelEvent e)
            {
                if (e.getWheelRotation() == 1)
                {// 滑轮向前
                    if (GUIGv.percent > 0.1)
                        GUIGv.percent -= 0.1;
                    GUIGv.drawboard.repaint();
                } else if (e.getWheelRotation() == -1)
                {// 滑轮向后
                    GUIGv.percent += 0.1;
                    GUIGv.drawboard.repaint();
                }
            }
        });
        GUIGv.drawboard = drawboard;// 获得一份drawboard的引用

        /* 设置窗体属性 */
        setTitle("实时查看");// 设置窗体标题
        setLayout(null);// 使用绝对布局
        setBounds(left, top, width, height);// 设置窗体位置大小

        /* 添加控件，显示窗体 */
        Container c = getContentPane();// 获得一个容器
        c.add(button1);// 添加button1
        c.add(button2);
        c.add(button3);
        c.add(button4);
        c.add(check1);
        c.add(check2);
        c.add(drawboard);
        setVisible(true);// 使窗体可见
        setAlwaysOnTop(true);// 设置窗体置顶
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 设置默认关闭方式
    }
}

class brush
{// 画笔
    public static int[][] colormap = new int[85][85];

    public static void draw(Graphics2D g)
    {
        boolean drawcolor = true;
        int factor = (int) (35 * GUIGv.percent);
        int width = (int) (20 * GUIGv.percent);
        int xoffset = -5;
        int yoffset = 3;
        // 检索一遍出租车位置信息，将有出租车的位置标上1
        int[][] taximap = new int[85][85];
        // 获得colormap的引用
        GUIGv.colormap = colormap;
        // 设置出租车位置
        for (int i = 0; i < 80; i++)
            for (int j = 0; j < 80; j++)
            {
                taximap[i][j] = -1;
            }
        for (int i = 0; i < GUIGv.taxilist.size(); i++)
        {
            guitaxi gt = GUIGv.taxilist.get(i);
            if (gt.status > -1)
            {
                // System.out.println("####"+gt.x+" "+gt.y);
                taximap[gt.x][gt.y] = gt.status;
                if (gt.status == 1)
                {
                    colormap[gt.x][gt.y] = 1;// 路线染色
                }
            }
        }
        // synchronized (GUIGv.m.taxilist) {
        // for (taxiInfo i : GUIGv.m.taxilist) {
        // taximap[i.nowPoint.x][i.nowPoint.y] = 1;
        // if (i.state == STATE.WILL || i.state == STATE.RUNNING) {
        // taximap[i.nowPoint.x][i.nowPoint.y] = 2;
        // colormap[i.nowPoint.x][i.nowPoint.y] = 1;// 路线染色
        // }
        // }
        // }
        // ...

        for (int i = 0; i < 80; i++)
        {
            for (int j = 0; j < 80; j++)
            {
                if (j < 10)
                {
                    xoffset = -5;
                    yoffset = 3;
                } else
                {
                    xoffset = -7;
                    yoffset = 3;
                }
                g.setStroke(new BasicStroke(2));
                g.setFont(new Font("Arial", Font.PLAIN, (int) (10 * GUIGv.percent)));
                if (GUIGv.m.map[i][j] == 2 || GUIGv.m.map[i][j] == 3)
                {
                    if (drawcolor && colormap[i][j] == 1 && colormap[i + 1][j] == 1)
                        g.setColor(Color.RED);
                    else
                        g.setColor(Color.BLACK);
                    int memj = (int) ((j * factor + GUIGv.xoffset) * GUIGv.percent);
                    g.drawLine(memj,
                            (int) ((i * factor + GUIGv.yoffset) * GUIGv.percent),
                            memj,
                            (int) (((i + 1) * factor + GUIGv.yoffset) * GUIGv.percent));
                    //绘制道路流量
                    if (GUIGv.drawflow)
                    {
                        g.setColor(Color.BLUE);
                        g.drawString("" + GUIGv.GetFlow(i, j, i + 1, j), memj,
                                (int) (((i + 0.5) * factor + GUIGv.yoffset) * GUIGv.percent));
                    }
                }
                if (GUIGv.m.map[i][j] == 1 || GUIGv.m.map[i][j] == 3)
                {
                    if (drawcolor && colormap[i][j] == 1 && colormap[i][j + 1] == 1)
                        g.setColor(Color.RED);
                    else
                        g.setColor(Color.BLACK);
                    int memi = (int) ((i * factor + GUIGv.yoffset) * GUIGv.percent);
                    g.drawLine((int) ((j * factor + GUIGv.xoffset) * GUIGv.percent),
                            memi,
                            (int) (((j + 1) * factor + GUIGv.xoffset) * GUIGv.percent),
                            memi);
                    //绘制道路流量
                    if (GUIGv.drawflow)
                    {
                        g.setColor(Color.BLUE);
                        g.drawString("" + GUIGv.GetFlow(i, j, i, j + 1), (int) (((j + 0.5) * factor + GUIGv.xoffset) * GUIGv.percent),
                                memi);
                    }
                }
                int targetWidth;
                if (taximap[i][j] == 3)
                {
                    g.setColor(Color.GREEN);
                    targetWidth = 2;
                } else if (taximap[i][j] == 2)
                {
                    g.setColor(Color.RED);
                    targetWidth = 2;
                } else if (taximap[i][j] == 1)
                {
                    g.setColor(Color.BLUE);
                    targetWidth = 2;
                } else if (taximap[i][j] == 0)
                {
                    g.setColor(Color.YELLOW);
                    targetWidth = 2;
                } else
                {
                    g.setColor(Color.BLACK);
                    targetWidth = 1;
                }
                int cleft = (int) ((j * factor - width / 2 + GUIGv.xoffset) * GUIGv.percent);
                int ctop = (int) ((i * factor - width / 2 + GUIGv.yoffset) * GUIGv.percent);
                int cwidth = (int) (width * GUIGv.percent) * targetWidth;
                if (targetWidth > 1)
                {
                    cleft = cleft - (int) (cwidth / 4);
                    ctop = ctop - (int) (cwidth / 4);
                }
                // g.fillOval((int)((j*factor-width/2+GUIGv.xoffset)*GUIGv.percent),(int)((i*factor-width/2+GUIGv.yoffset)*GUIGv.percent),(int)(width*GUIGv.percent)*targetWidth,(int)(width*GUIGv.percent)*targetWidth);
                g.fillOval(cleft, ctop, cwidth, cwidth);// 绘制点
                //绘制红绿灯
                if(GUIGv.lightmap[i][j]==1){//东西方向为绿灯
                    g.setColor(Color.GREEN);
                    g.fillRect(cleft-cwidth/4, ctop+cwidth/4, cwidth/2, cwidth/8);
                    g.setColor(Color.RED);
                    g.fillRect(cleft+cwidth/8, ctop-cwidth/4, cwidth/8, cwidth/2);
                }
                else if(GUIGv.lightmap[i][j]==2){//东西方向为红灯
                    g.setColor(Color.RED);
                    g.fillRect(cleft-cwidth/4, ctop+cwidth/4, cwidth/2, cwidth/8);
                    g.setColor(Color.GREEN);
                    g.fillRect(cleft+cwidth/8, ctop-cwidth/4, cwidth/8, cwidth/2);
                }
                // 标记srclist中的点
                for (Point p : GUIGv.srclist)
                {
                    g.setColor(Color.RED);
                    int x = p.x;
                    int y = p.y;
                    g.drawLine((int) (((y - 2) * factor + GUIGv.xoffset) * GUIGv.percent),
                            (int) (((x - 2) * factor + GUIGv.yoffset) * GUIGv.percent),
                            (int) (((y + 2) * factor + GUIGv.xoffset) * GUIGv.percent),
                            (int) (((x - 2) * factor + GUIGv.yoffset) * GUIGv.percent));
                    g.drawLine((int) (((y + 2) * factor + GUIGv.xoffset) * GUIGv.percent),
                            (int) (((x - 2) * factor + GUIGv.yoffset) * GUIGv.percent),
                            (int) (((y + 2) * factor + GUIGv.xoffset) * GUIGv.percent),
                            (int) (((x + 2) * factor + GUIGv.yoffset) * GUIGv.percent));
                    g.drawLine((int) (((y + 2) * factor + GUIGv.xoffset) * GUIGv.percent),
                            (int) (((x + 2) * factor + GUIGv.yoffset) * GUIGv.percent),
                            (int) (((y - 2) * factor + GUIGv.xoffset) * GUIGv.percent),
                            (int) (((x + 2) * factor + GUIGv.yoffset) * GUIGv.percent));
                    g.drawLine((int) (((y - 2) * factor + GUIGv.xoffset) * GUIGv.percent),
                            (int) (((x + 2) * factor + GUIGv.yoffset) * GUIGv.percent),
                            (int) (((y - 2) * factor + GUIGv.xoffset) * GUIGv.percent),
                            (int) (((x - 2) * factor + GUIGv.yoffset) * GUIGv.percent));
                }
                if (GUIGv.drawstr == true)
                {
                    g.setColor(Color.WHITE);
                    g.setFont(new Font("Arial", Font.PLAIN, (int) (8 * GUIGv.percent)));
                    g.drawString("" + i + "," + j, (int) ((j * factor + xoffset + GUIGv.xoffset) * GUIGv.percent),
                            (int) ((i * factor + yoffset + GUIGv.yoffset) * GUIGv.percent));
                }
            }
        }
    }
}

class processform extends JFrame
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    JProgressBar progressBar = new JProgressBar();
    JLabel label1 = new JLabel("BFS进度", SwingConstants.CENTER);

    public processform()
    {
        super();
        // 将进度条设置在窗体最北面
        getContentPane().add(progressBar, BorderLayout.NORTH);
        getContentPane().add(label1, BorderLayout.CENTER);
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        progressBar.setStringPainted(true);
        // 设置窗体各种属性方法
        setBounds(100, 100, 100, 100);
        setAlwaysOnTop(true);// 设置窗体置顶
        setVisible(true);
    }
}

class debugform extends JFrame
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    JTextArea text1 = new JTextArea();

    public debugform()
    {
        super();
        getContentPane().add(text1);
        setBounds(0, 100, 500, 100);
        setAlwaysOnTop(true);
        setVisible(true);
    }
}

