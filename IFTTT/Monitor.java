import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

public class Monitor extends Thread implements DEFINE
{
    private int monitorType;
    private boolean[] taskType;
    private LinkedBlockingQueue<LinkedList<Boolean>> signalQueue;
    private LinkedBlockingQueue<LinkedList<Object>> fileInfos;

    private String fileName;
    private String name;
    private long length;
    private String modifiedTime;

    private String newFileName;
    private String newName;
    private long newLength;
    private String newModifiedTime;

    private Summary summary;
    private Detail detail;
    private SafeFile safeFile;

    public Monitor (int type, boolean[] taskType, LinkedBlockingQueue<LinkedList<Boolean>> queue, LinkedBlockingQueue<LinkedList<Object>> fileInfos,String fileName, String name, long length, String modifiedTime, String newFileName, String newName, long newLength, String newModifiedTime, Summary summary, Detail detail, SafeFile safeFile)
    {
        this.monitorType = type;
        this.taskType = taskType;
        this.signalQueue = queue;
        this.fileInfos = fileInfos;
        this.fileName = fileName;
        this.name = name;
        this.length = length;
        this.modifiedTime = modifiedTime;
        this.newFileName = newFileName;
        this.newName = newName;
        this.newLength = newLength;
        this.newModifiedTime = newModifiedTime;
        this.summary = summary;
        this.detail = detail;
        this.safeFile = safeFile;
    }

    public void run()
    {
        switch (monitorType)
        {
            case RENAMED: System.out.println("\tMonitor `" + fileName +"` Rename ->" + (taskType[SUMMARY] ? " SUMMARY" : "") + (taskType[DETAIL] ? " DETAIL" : "") + (taskType[RECOVER] ? " RECOVER" : "") ); break;
            case MODIFIED: System.out.println("\tMonitor `" + fileName +"` Modify ->" + (taskType[SUMMARY] ? " SUMMARY" : "") + (taskType[DETAIL] ? " DETAIL" : "") + (taskType[RECOVER] ? " RECOVER" : "") ); break;
            case PATH_CHANGED: System.out.println("\tMonitor `" + fileName +"` Path-Change ->" + (taskType[SUMMARY] ? " SUMMARY" : "") + (taskType[DETAIL] ? " DETAIL" : "") + (taskType[RECOVER] ? " RECOVER" : "") ); break;
            case SIZE_CHANGED: System.out.println("\tMonitor `" + fileName +"` Size-Change ->" + (taskType[SUMMARY] ? " SUMMARY" : "") + (taskType[DETAIL] ? " DETAIL" : "") + (taskType[RECOVER] ? " RECOVER" : "") ); break;
        }
        while(true)
        {
            try
            {
                if (signalQueue.take().get(monitorType))
                {
//                    switch (monitorType)
//                    {
//                        case RENAMED: System.out.println(fileName + " Detected Rename."); break;
//                        case MODIFIED: System.out.println(fileName + " Detected Modify."); break;
//                        case PATH_CHANGED: System.out.println(fileName + " Detected Path-Change." ); break;
//                        case SIZE_CHANGED: System.out.println(fileName + " Detected Size-Change."); break;
//                    }
                    LinkedList<Object> info = fileInfos.take();
                    this.newFileName = (String)info.get(0);
                    this.newName = (String)info.get(1);
                    this.newLength = (long)info.get(2);
                    this.newModifiedTime = (String)info.get(3);
                    String str = new String();
                    switch (monitorType)
                    {
                        case RENAMED: str = String.format("--------------------RENAME DETECTED--------------------\nFile Name: %s -> %s\nFile Absolute Name: %s -> %s\nFile Size: %d -> %d\nFile Modified Time: %s -> %s\n------------------------------------------------------------\n", name, newName, fileName, newFileName, length, newLength, modifiedTime, newModifiedTime); break;
                        case MODIFIED: str = String.format("--------------------MODIFY DETECTED--------------------\nFile Name: %s -> %s\nFile Absolute Name: %s -> %s\nFile Size: %d -> %d\nFile Modified Time: %s -> %s\n------------------------------------------------------------\n", name, newName, fileName, newFileName, length, newLength, modifiedTime, newModifiedTime); break;
                        case PATH_CHANGED: str = String.format("--------------------PATH-CHANGE DETECTED--------------------\nFile Name: %s -> %s\nFile Absolute Name: %s -> %s\nFile Size: %d -> %d\nFile Modified Time: %s -> %s\n------------------------------------------------------------\n", name, newName, fileName, newFileName, length, newLength, modifiedTime, newModifiedTime); break;
                        case SIZE_CHANGED: str = String.format("--------------------SIZE-CHANGE DETECTED--------------------\nFile Name: %s -> %s\nFile Absolute Name: %s -> %s\nFile Size: %d -> %d\nFile Modified Time: %s -> %s\n------------------------------------------------------------\n", name, newName, fileName, newFileName, length, newLength, modifiedTime, newModifiedTime); break;
                    }
                    System.out.print(str);
                    if (taskType[SUMMARY])
                    {
                        summary.put(monitorType);
                    }
                    if (taskType[DETAIL])
                    {
                        detail.put(str);
                    }
                    if (taskType[RECOVER])
                    {
                        if (monitorType == RENAMED || monitorType == PATH_CHANGED)
                        {
                            safeFile.recover(newFileName, fileName);
                            System.out.printf("New file `%s` recovered to `%s`\n", newFileName, fileName);
                        }
                        else
                            System.out.printf("Error：Trying to recover the operation of Modify/Size-change\n");
                    }
                    break;
                }
            }
            catch (InterruptedException e) {}
        }
    }
}
