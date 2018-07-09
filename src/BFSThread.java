import java.awt.*;

public class BFSThread extends Thread
{
    Point srcPoint;
    Point dstPoint;

    public BFSThread(Point src, Point dst)
    {
        this.srcPoint = src;
        this.dstPoint = dst;
    }

    public void run()
    {
        guigv.m.pointbfs(srcPoint.x*80+srcPoint.y);
        guigv.m.pointbfs(dstPoint.x*80+dstPoint.y);
//        System.out.println("OJBK");
    }
}
