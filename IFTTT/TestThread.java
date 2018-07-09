import java.io.File;

public class TestThread extends Thread
{
    private SafeFile safeFile;

    public TestThread(SafeFile safeFile)
    {
        this.safeFile = safeFile;
    }
    public boolean addFile(String fileName)
    {
        //使用被测者提供的SafeFile类来实现增加一个文件，如果成功则返回true；否则返回false（如参数有误，或者遇到了任何文件访问异常）。
        try
        {
            File file = new File(fileName);
            safeFile.createNewFile(file.getParent(), file.getName());
            return true;
        } catch (Exception e) {return false;}
    }

    public boolean rename(String from, String to)
    {
    //使用被测者提供的SafeFile类来实现文件重命名，如果成功则返回true；否则返回false（如参数有误，或者遇到了任何文件访问异常）。
        try
        {
            safeFile.rename(from, to);
            return true;
        } catch (Exception e) {return false;}
    }

    public boolean delete(String src)
    {
    //使用被测者提供的SafeFile类来删除一个文件，如果成功则返回true；否则返回false（如参数有误，或者遇到了任何文件访问异常）。
        try
        {
            safeFile.deleteFile(src);
            return true;
        }catch (Exception e) {return false;}
    }

    public boolean move(String from, String to)
    {
    //使用被测者提供的SafeFile类来移动一个文件，如果成功则返回true；否则返回false（如参数有误，或者遇到了任何文件访问异常）。
        try
        {
            safeFile.moveFile(from, to);
            return true;
        } catch (Exception e) {return false;}
    }

    public boolean changeSize(String file)
    {
    //使用被测者提供的SafeFile类来改变一个文件的规模和最后修改时间，如果成功则返回true；否则返回false（如参数有误，或者遇到了任何文件访问异常）。
        try
        {
            safeFile.write(file, "testcase\n");
            return true;
        } catch (Exception e) {return false;}
    }

    public boolean changeTime(String file)
    {
    //使用被测者提供的SafeFile类来改变一个文件的最后修改时间，如果成功则返回true；否则返回false（如参数有误，或者遇到了任何文件访问异常）。
        try
        {
            safeFile.changeLastModified(file);
            return true;
        } catch (Exception e) {return false;}
    }

    public boolean testcase()
    {
        try
        {
            //        if (!addFile("D:\\code\\test\\a.txt")) return false;
//            if (!rename("D:\\Code\\test\\a.txt", "c.txt")) return false;
//            if (!delete("D:\\code\\test\\a.txt")) return false;
//            if(!changeSize("H:\\test\\TextFile\\a\\a_a\\a_x1.txt")) return false;
            if (!rename("H:\\test\\TextFile\\a\\a_a\\1.txt" , "11.txt")) return false;
            sleep(1000);
            if (!rename("H:\\test\\TextFile\\a\\a_a\\2.txt" , "22.txt")) return false;

            sleep(1000);
            if (!rename("H:\\test\\TextFile\\a\\a_a\\3.txt" , "33.txt")) return false;

            sleep(1000);
            if (!rename("H:\\test\\TextFile\\a\\a_a\\4.txt" , "44.txt")) return false;

            sleep(1000);
            if (!rename("H:\\test\\TextFile\\a\\a_a\\5.txt" , "55.txt")) return false;

            sleep(1000);
            if (!rename("H:\\test\\TextFile\\a\\a_a\\6.txt" , "66.txt")) return false;

            sleep(1000);
            sleep(500); // 如果出现
//            if (!rename("D:\\code\\test\\b.txt", "a.txt")) return false;
            return true;
        }catch (Exception e){return false;}

    }

    public void run()
    {
        if (!testcase())
        {
            //输出相关提示信息，或者进行额外的必要处理。
            System.out.println("TestCase Error.");
        }
    }
}
