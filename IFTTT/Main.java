import java.io.IOException;
import java.rmi.server.ExportException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Thread.sleep;

public class Main
{
    public static void main(String[] args)
    {
        try
        {
            System.out.println("Hello World!");

            SnapShot snapShot = new SnapShot();
            SafeFile safeFile = new SafeFile(snapShot);
            Workspace workspace = new Workspace(safeFile);
            LinkedList<FileMonitor> files = new LinkedList<FileMonitor>();
            LinkedList<DictMonitor> dicts = new LinkedList<DictMonitor>();
            Summary summary = new Summary();
            Detail detail = new Detail();
            Trigger trigger = new Trigger(workspace, files, dicts, snapShot, summary, detail, safeFile);
            trigger.start();

            System.out.println("--------------------START OF FILE DETAILS--------------------");
            System.out.println("All Files and Triggers Listed Below.");
            for (FileMonitor each: files)
                each.printDetail(false);
            for (DictMonitor each: dicts)
                each.printDetail();
            System.out.println("--------------------END OF FILE DETAILS--------------------");
            System.out.println("--------------------START OF ORIGINAL SNAPSHOT--------------------");
            snapShot.printFileStruct();
            System.out.println("--------------------END OF ORIGINAL SNAPSHOT--------------------");
            System.out.println("--------------------STARTING MONITORS THREADS--------------------");
            for (FileMonitor each: files)
                each.start();
            for (DictMonitor each: dicts)
                each.start();
            summary.start();
            detail.start();
            sleep(3000);
            System.out.println("--------------------TEST BEGIN--------------------");
            TestThread testThread = new TestThread(safeFile);
            testThread.start();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}
