import java.io.*;
import java.util.concurrent.LinkedBlockingQueue;

public class Summary extends Thread implements DEFINE
{
    private int renameCount = 0;
    private int modifyCount = 0;
    private int pathChangeCount = 0;
    private int sizeChangeCount = 0;


    public void run()
    {
        while(true)
        {
            try
            {
                File file = new File("D:/summary.txt");
                if (!file.exists())
                    file.createNewFile();
                FileOutputStream fop = new FileOutputStream(file);
                PrintStream ps = new PrintStream(fop);
                ps.println("--------------------SUMMARY--------------------");
                ps.println("Rename Count:\t" + renameCount);
                ps.println("Modify Count:\t" + modifyCount);
                ps.println("Path Change Count:\t" + pathChangeCount);
                ps.println("Size Change Count:\t" + sizeChangeCount);
                System.out.println("Summary updated.");
                sleep(50000);
            }catch(InterruptedException e0)
            {
            }catch (Exception e1)
            {
                e1.printStackTrace();
            }
        }
    }

    public synchronized void put(int type)
    {
        switch (type)
        {
            case RENAMED: renameCount++; break;
            case MODIFIED: modifyCount++; break;
            case PATH_CHANGED: pathChangeCount++; break;
            case SIZE_CHANGED: sizeChangeCount++; break;
        }
    }

}
