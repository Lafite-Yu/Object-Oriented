import java.io.File;
import java.util.LinkedList;

public class DictMonitor extends Thread implements DEFINE
{
    private String dictName;
    private LinkedList<FileMonitor> files;
    private LinkedList<String> fileAbsoluteNames;
    private LinkedList<String> newFileAbsoluteNames;
    private SnapShot snapShot;

    private boolean renameEnabled;
    private boolean[] renameTask;
    private boolean modifyEnabled;
    private boolean[] modifyTask;
    private boolean pathChangeEnabled;
    private boolean[] pathChangeTask;
    private boolean sizeChangeEnabled;
    private boolean[] sizeChangeTask;

    private Summary summary;
    private Detail detail;
    private SafeFile safeFile;


    public DictMonitor(String fileName, SnapShot snapShot, Summary summary, Detail detail, SafeFile safeFile)
    {
        this.dictName = fileName;
        this.renameEnabled = false;
        this.modifyEnabled = false;
        this.pathChangeEnabled = false;
        this.sizeChangeEnabled = false;
        this.renameTask = new boolean[3];
        this.modifyTask = new boolean[3];
        this.pathChangeTask = new boolean[3];
        this.sizeChangeTask = new boolean[3];
        this.files = new LinkedList<>();
        this.snapShot = snapShot;
        this.fileAbsoluteNames = new LinkedList<>();
        this.newFileAbsoluteNames = new LinkedList<>();
        this.summary = summary;
        this.detail = detail;
        this.safeFile = safeFile;
    }

    public void run()
    {
        System.out.println("Monitor for dict `" + dictName + "` starts.");
        for (FileMonitor each: files)
         each.start();
        /* Search for new files? */
        while (true)
        {
            File root = new File(dictName);
//            System.out.println("RECORD");
            recordNewFileNames(root);
            for (String each: newFileAbsoluteNames)
            {
//                System.out.println(each);
                if (fileAbsoluteNames.contains(each))
                    continue;
                else
                {
                    System.out.println("New file:" + each);
                    FileMonitor newFileMonitor = new FileMonitor(each, snapShot, summary, detail, safeFile);
                    if (renameEnabled)
                        newFileMonitor.setMonitorEnable(RENAMED, renameTask);
                    if (modifyEnabled)
                        newFileMonitor.setMonitorEnable(MODIFIED, modifyTask);
                    if (pathChangeEnabled)
                        newFileMonitor.setMonitorEnable(PATH_CHANGED, pathChangeTask);
                    if (sizeChangeEnabled)
                        newFileMonitor.setMonitorEnable(SIZE_CHANGED, sizeChangeTask);
                    this.files.addLast(newFileMonitor);
                    newFileMonitor.takeSnapShot();
                    this.fileAbsoluteNames.addLast(each);
                    newFileMonitor.start();
                }
            }
        }
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

    public String getDictName()
    {
        return dictName;
    }

    public void printDetail()
    {
        System.out.println("\tDictName:\t" +  dictName);
        System.out.println("\t\t----------------------------------------");
        System.out.println("\tAll files in this dict:");
        for (String each: fileAbsoluteNames)
            System.out.println("\t\t" + each);
        System.out.println("\t\t----------------------------------------");
        System.out.println("\tTriggers:");
        if (renameEnabled)
            System.out.println("\t\t\tRename | " + (renameTask[SUMMARY]?"\tSummary":"") + (renameTask[DETAIL]?"\tDetail":"") + (renameTask[RECOVER]?"\tRecover":"") );
        if (modifyEnabled)
            System.out.println("\t\t\tModify | " + (modifyTask[SUMMARY]?"\tSummary":"") + (modifyTask[DETAIL]?"\tDetail":"") + (modifyTask[RECOVER]?"\tRecover":"") );
        if (pathChangeEnabled)
            System.out.println("\t\t\tPathChange | " + (pathChangeTask[SUMMARY]?"\tSummary":"") + (pathChangeTask[DETAIL]?"\tDetail":"") + (pathChangeTask[RECOVER]?"\tRecover":"") );
        if (sizeChangeEnabled)
            System.out.println("\t\t\tSizeChange | " + (sizeChangeTask[SUMMARY]?"\tSummary":"") + (sizeChangeTask[DETAIL]?"\tDetail":"") + (sizeChangeTask[RECOVER]?"\tRecover":"") );
        for (FileMonitor each: files)
        {
            each.printDetail(true);
        }
        System.out.println("\t--------------------END OF THIS DICT--------------------");
    }

    public void setFileMonitor()
    {
        setFileMonitor(new File(this.dictName));
    }

    private void setFileMonitor(File path)
    {
        File[] files = path.listFiles();
        if (files == null) // Bottom
            return;
        for (File eachFile: files)
        {
            if (eachFile.isDirectory())
                setFileMonitor(eachFile);
            else
            {
                FileMonitor newFileMonitor = new FileMonitor(eachFile.getAbsoluteFile().toString(), snapShot, summary, detail, safeFile);
                if (renameEnabled)
                    newFileMonitor.setMonitorEnable(RENAMED, renameTask);
                if (modifyEnabled)
                    newFileMonitor.setMonitorEnable(MODIFIED, modifyTask);
                if (pathChangeEnabled)
                    newFileMonitor.setMonitorEnable(PATH_CHANGED, pathChangeTask);
                if (sizeChangeEnabled)
                    newFileMonitor.setMonitorEnable(SIZE_CHANGED, sizeChangeTask);
                this.files.addLast(newFileMonitor);
//                System.out.println(eachFile.getAbsoluteFile().toString());
            }
        }
    }

    public void takeSnapShot()
    {
        recordFileNames(new File(dictName));
        for (FileMonitor each: files)
        {
            each.takeSnapShot();
        }
    }

    private void recordFileNames(File path)
    {
        File[] files = path.listFiles();
        if (files == null) // Bottom
            return;
        for (File eachFile: files)
        {
            if (eachFile.isDirectory())
                recordFileNames(eachFile);
            else
            {
                fileAbsoluteNames.addLast(eachFile.getAbsoluteFile().toString());
            }
        }
    }

    private void recordNewFileNames(File path)
    {
        File[] files = path.listFiles();
        if (files == null) // Bottom
            return;
        for (File eachFile: files)
        {
            if (eachFile.isDirectory())
                recordFileNames(eachFile);
            else
            {
                newFileAbsoluteNames.addLast(eachFile.getAbsoluteFile().toString());
            }
        }
    }


//END OF CLASS
}
