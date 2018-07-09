import java.io.File;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

public class FileMonitor extends Thread implements DEFINE
{
    private String fileName;
    private String name;
    private long length;
    private String modifiedTime;

    private String newFileName;
    private String newName;
    private long newLength;
    private String newModifiedTime;

    private SnapShot snapShot;

    private boolean renameEnabled;
    private boolean[] renameTask;
    private boolean modifyEnabled;
    private boolean[] modifyTask;
    private boolean pathChangeEnabled;
    private boolean[] pathChangeTask;
    private boolean sizeChangeEnabled;
    private boolean[] sizeChangeTask;

    private LinkedList<Monitor> monitors;
    private LinkedList<LinkedBlockingQueue<LinkedList<Boolean>>> signalQueues;
    private LinkedList<LinkedBlockingQueue<LinkedList<Object>>> fileInfos;
    private boolean[] signal;

    private Summary summary;
    private Detail detail;
    private SafeFile safeFile;


    public FileMonitor(String fileName, SnapShot snapShot, Summary summary, Detail detail, SafeFile safeFile)
    {
        this.fileName = fileName;
        this.renameEnabled = false;
        this.modifyEnabled = false;
        this.pathChangeEnabled = false;
        this.sizeChangeEnabled = false;
        this.renameTask = new boolean[3];
        this.modifyTask = new boolean[3];
        this.pathChangeTask = new boolean[3];
        this.sizeChangeTask = new boolean[3];
        this.snapShot = snapShot;
        this.monitors = new LinkedList<>();
        this.signalQueues = new LinkedList<>();
        this.name = new String();
        this.length = 0;
        this.modifiedTime = new String();
        this.newFileName = new String();
        this.newName = new String();
        this.newLength = 0;
        this.newModifiedTime = new String();
        this.signal = new boolean[4];
        this.summary = summary;
        this.detail = detail;
        this.fileInfos = new LinkedList<>();
        this.safeFile = safeFile;
    }

    public void run()
    {
        System.out.println("Monitor for file `" + fileName + "` starts.");
        setMonitors();
        while (true)
        {
            synchronized (SafeFile.class)
            {
                if(takeNewSnapShot()) //文件存在
                {
                    if(newModifiedTime.equals(modifiedTime) && newLength == length)
                        continue;
                    if (!newModifiedTime.equals(modifiedTime))
                    {
                        signal[MODIFIED] = true;
                        if (newLength != length)
                        {
//                         System.out.println("OJBK");
                            signal[SIZE_CHANGED] = true;
                        }
//                     File file = new File(fileName);
//                     System.out.println("NewLength " + file.length());
                        this.newFileName = fileName;
                        putToMonitors();
                        System.out.println(fileName + " This file has been modified/size-changed, monitors stop.");
                        break;
                    }
                }else // 文件丢失
                {
//                    System.out.println("File lost, check for rename.");
                    File file = new File(fileName);
                    File path = new File(file.getParent());
//                System.out.println(file.getParent());
                    File[] files = path.listFiles();
                    if (files != null) // 不是空目录
                        for (File eachFile: files)
                        {
                            if (eachFile.isDirectory())
                                continue;
                            LinkedList<Object> fileInfo;
                            fileInfo = safeFile.getFileInfo(eachFile.getAbsoluteFile().toString());
                            String thisFileName = (String)fileInfo.get(0);
                            long thisFileLength = (Long)fileInfo.get(1);
                            String thisFileModifiedTime = (String)fileInfo.get(2);
                            String thisFileAbsoluteName = (String)fileInfo.get(3);
//                        System.out.println("This file name: " + thisFileAbsoluteName);
                            if(thisFileLength == length && thisFileModifiedTime.equals(modifiedTime))
                            {
//                            System.out.println("OJBK");
                                if (snapShot.exist(thisFileName))
                                {
                                    if ( snapShot.getSameNameFiles(thisFileName).containsKey(thisFileAbsoluteName) )
                                        continue;
                                }
                                this.newFileName = thisFileAbsoluteName;
                                this.newName = thisFileName;
                                this.newLength = thisFileLength;
                                this.newModifiedTime = thisFileModifiedTime;
//                                System.out.println("!!!!!!!!!!!!!"+newFileName);
                                signal[RENAMED] = true;
                                putToMonitors();
                                System.out.println(fileName + " This file has been renamed, monitors stop.");
                                return;
                            }
                        }
                    //如果存在重命名的文件，则上面已经return
                    //Path Changed?
//                    System.out.println("Check for Path Change.");

//                    snapShot.takeSnapShot();
//                snapShot.printOriginalFileStruct();
//                snapShot.printFileStruct();

                    // 更新后获取全部的同名文件的信息
                    if (snapShot.existNew(name))
                    {
                        for (Map.Entry<String, LinkedList<Object>> entry: snapShot.getNewSameNameFiles(name).entrySet())
                        {
//                        System.out.println("\t\tAbsolute File Name:\t" + entry.getKey());
//                        System.out.println("\t\t\tFile Size: " + entry.getValue().get(0) + "\tModified Time: " + entry.getValue().get(1));
                            if (snapShot.exist(name))
                            {
                                if (snapShot.getSameNameFiles(name).containsKey(entry.getKey()))
                                    continue;
                            }//else 新增文件
                            // 原来没有的文件, path-change
                            this.newFileName = entry.getKey();
                            this.newName = name;
                            this.newLength = length;
                            this.newModifiedTime = modifiedTime;
                            signal[PATH_CHANGED] = true;
                            putToMonitors();
                            System.out.println(fileName + " This file has been path-changed, monitors stop.");
                            return;
                        }
                    } else // 文件消失
                    {
                        System.out.println("File vanished.");
                        this.newFileName = fileName;
                        this.newName = name;
                        this.newLength = 0;
                        this.newModifiedTime = modifiedTime;
                        signal[PATH_CHANGED] = true;
                        putToMonitors();
                        System.out.println(fileName + " This file has been vanished, monitors stop.");
                        return;
                    }
                    return;
                }
            }

        }
    }

//    private LinkedList<String> searchForSameFile(File path)
//    {
//
//    }

    public void putToMonitors()
    {
        LinkedList<Boolean> signalToPut = new LinkedList<>();
        signalToPut.addLast(signal[RENAMED]);
        signalToPut.addLast(signal[MODIFIED]);
        signalToPut.addLast(signal[PATH_CHANGED]);
        signalToPut.addLast(signal[SIZE_CHANGED]);
        try
        {
            signalQueues.get(RENAMED).put(signalToPut);
            signalQueues.get(MODIFIED).put(signalToPut);
            signalQueues.get(PATH_CHANGED).put(signalToPut);
            signalQueues.get(SIZE_CHANGED).put(signalToPut);
            LinkedList<Object> info = new LinkedList<>();
            info.addLast(newFileName);
            info.addLast(newName);
            info.addLast(newLength);
            info.add(newModifiedTime);
            fileInfos.get(RENAMED).put(info);
            fileInfos.get(MODIFIED).put(info);
            fileInfos.get(PATH_CHANGED).put(info);
            fileInfos.get(SIZE_CHANGED).put(info);
        } catch (InterruptedException e)
        {
        }
    }

    public void setMonitors()
    {
        signalQueues.addLast(new LinkedBlockingQueue<LinkedList<Boolean>>());
        fileInfos.addLast(new LinkedBlockingQueue<LinkedList<Object>>());
        monitors.addLast(new Monitor(RENAMED, renameTask, signalQueues.get(RENAMED), fileInfos.get(RENAMED), fileName, name, length, modifiedTime, newFileName, newName, newLength, newModifiedTime, summary, detail, safeFile));
        signalQueues.addLast(new LinkedBlockingQueue<LinkedList<Boolean>>());
        fileInfos.addLast(new LinkedBlockingQueue<LinkedList<Object>>());
        monitors.addLast(new Monitor(MODIFIED, modifyTask, signalQueues.get(MODIFIED), fileInfos.get(MODIFIED), fileName, name, length, modifiedTime, newFileName, newName, newLength, newModifiedTime, summary, detail, safeFile));
        signalQueues.addLast(new LinkedBlockingQueue<LinkedList<Boolean>>());
        fileInfos.addLast(new LinkedBlockingQueue<LinkedList<Object>>());
        monitors.addLast(new Monitor(PATH_CHANGED, pathChangeTask, signalQueues.get(PATH_CHANGED), fileInfos.get(PATH_CHANGED), fileName, name, length, modifiedTime, newFileName, newName, newLength, newModifiedTime, summary, detail, safeFile));
        signalQueues.addLast(new LinkedBlockingQueue<LinkedList<Boolean>>());
        fileInfos.addLast(new LinkedBlockingQueue<LinkedList<Object>>());
        monitors.addLast(new Monitor(SIZE_CHANGED, sizeChangeTask, signalQueues.get(SIZE_CHANGED), fileInfos.get(SIZE_CHANGED), fileName, name, length, modifiedTime, newFileName, newName, newLength, newModifiedTime, summary, detail, safeFile));
        if (renameEnabled)
            monitors.get(RENAMED).start();
        if (modifyEnabled)
            monitors.get(MODIFIED).start();
        if (pathChangeEnabled)
            monitors.get(PATH_CHANGED).start();
        if (sizeChangeEnabled)
            monitors.get(SIZE_CHANGED).start();
    }

    public void setMonitorEnable(int monitorType, int taskType)
    {
        switch (monitorType)
        {
            case RENAMED: renameEnabled = true; renameTask[taskType] = true; break;
            case MODIFIED: modifyEnabled = true; modifyTask[taskType] = true; break;
            case PATH_CHANGED: pathChangeEnabled = true; pathChangeTask[taskType] = true; break;
            case SIZE_CHANGED: sizeChangeEnabled = true; sizeChangeTask[taskType] = true; break;
        }
    }

    public void setMonitorEnable(int monitorType, boolean[] task)
    {
        switch (monitorType)
        {
            case RENAMED: renameEnabled = true; renameTask = task.clone(); break;
            case MODIFIED: modifyEnabled = true; modifyTask = task.clone(); break;
            case PATH_CHANGED: pathChangeEnabled = true; pathChangeTask = task.clone(); break;
            case SIZE_CHANGED: sizeChangeEnabled = true; sizeChangeTask = task.clone(); break;
        }
    }

    public String getFileName()
    {
        return fileName;
    }

    public void printDetail(boolean isFromDict)
    {
        System.out.println("\tFileName:\t" +  fileName);
        System.out.println("\t\tFile Detail:");
        System.out.printf("\t\t\tName:%s\tLength:%d\tModifiedTime:%s\n", this.name, this.length, this.modifiedTime);
        System.out.println("\t\tTriggers:");
        if (renameEnabled)
            System.out.println("\t\t\tRename | " + (renameTask[SUMMARY]?"\tSummary":"") + (renameTask[DETAIL]?"\tDetail":"") + (renameTask[RECOVER]?"\tRecover":"") );
        if (modifyEnabled)
            System.out.println("\t\t\tModify | " + (modifyTask[SUMMARY]?"\tSummary":"") + (modifyTask[DETAIL]?"\tDetail":"") + (modifyTask[RECOVER]?"\tRecover":"") );
        if (pathChangeEnabled)
            System.out.println("\t\t\tPathChange | " + (pathChangeTask[SUMMARY]?"\tSummary":"") + (pathChangeTask[DETAIL]?"\tDetail":"") + (pathChangeTask[RECOVER]?"\tRecover":"") );
        if (sizeChangeEnabled)
            System.out.println("\t\t\tSizeChange | " + (sizeChangeTask[SUMMARY]?"\tSummary":"") + (sizeChangeTask[DETAIL]?"\tDetail":"") + (sizeChangeTask[RECOVER]?"\tRecover":"") );
        if (!isFromDict)
            System.out.println("\t--------------------END OF THIS FILE--------------------");
    }

    public void takeSnapShot()
    {
        LinkedList<Object> fileInfo;
        fileInfo = safeFile.getFileInfo(this.fileName);
        this.name = (String)fileInfo.get(0);
        this.length = (Long)fileInfo.get(1);
        this.modifiedTime = (String)fileInfo.get(2);
    }

    public boolean takeNewSnapShot()
    {
        File file = new File(fileName);
        if (!file.exists() || file.isDirectory())
            return false;
//        System.out.println(file.exists());
        LinkedList<Object> fileInfo;
        try
        {
            fileInfo = safeFile.getFileInfo(this.fileName);
            this.newName = (String)fileInfo.get(0);
            this.newLength = (long)fileInfo.get(1);
            this.newModifiedTime = (String)fileInfo.get(2);
//            this.newLength = file.length();
        } catch (Exception e)
        {
            return false;
        }

        return true;
    }

//END OF CLASS
}
