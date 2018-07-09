import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.concurrent.LinkedBlockingQueue;

public class Detail extends Thread
{
    LinkedBlockingQueue<String> queue;

    public Detail()
    {
        this.queue = new LinkedBlockingQueue<>();
    }

    public void run()
    {
        try
        {

            File file = new File("D:/detail.txt");
            if (!file.exists())
                file.createNewFile();
            FileOutputStream fop = new FileOutputStream(file);
            PrintStream ps = new PrintStream(fop);
            ps.println("--------------------DETAIL--------------------");
            while(true)
            {
                while (!queue.isEmpty())
                    ps.print(queue.take());
                System.out.println("Detail updated.");
                sleep(50000);
            }

        } catch (InterruptedException e)
        {
            e.printStackTrace();
        } catch (Exception e1)
        {
            e1.printStackTrace();
        }
    }

    public void put(String str)
    {
        try
        {
            queue.put(str);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
