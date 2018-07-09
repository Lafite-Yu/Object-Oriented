import java.util.LinkedList;
import java.util.Scanner;

public class Trigger implements DEFINE
{
    private LinkedList<FileMonitor> files;
    private LinkedList<DictMonitor> dicts;
    private LinkedList<TriggerInfo> triggers;
    private Workspace workspace;
    private SnapShot snapShot;
    private Summary summary;
    private Detail detail;
    private SafeFile safeFile;


    public Trigger(Workspace workspace, LinkedList<FileMonitor> files, LinkedList<DictMonitor> dicts, SnapShot snapShot, Summary summary, Detail detail, SafeFile safeFile)
    {
        this.files = files;
        this.dicts = dicts;
        this.triggers = new LinkedList<>();
        this.workspace = workspace;
        this.snapShot = snapShot;
        this.summary = summary;
        this.detail = detail;
        this.safeFile = safeFile;
    }

    public void start() throws NullPointerException
    {
        int MAX_TRIGGER = 10;
        System.out.println("--------------------Start Reading Commands--------------------");
        Scanner scan = new Scanner(System.in);
        for (int i = 0; i < MAX_TRIGGER;)
        {
            String str = scan.nextLine();
            if (str.equalsIgnoreCase("end"))
                break;
            String args[] = str.split(" ");
            boolean SameFlag = false;
            int triggerType = 0;
            switch (args[TRIGGER_INDEX])
            {
                case "renamed": triggerType = RENAMED; break;
                case "Modified": triggerType = MODIFIED; break;
                case "path-changed": triggerType = PATH_CHANGED; break;
                case "size-changed": triggerType = SIZE_CHANGED; break;
            }
            int taskType = 0;
            switch (args[TASK_INDEX])
            {
                case "record-summary": taskType = SUMMARY; break;
                case "record-detail": taskType =  DETAIL; break;
                case "recover": taskType = RECOVER; break;
            }
            if(taskType == RECOVER && (triggerType == MODIFIED || triggerType == SIZE_CHANGED) )
            {
                System.out.println("ERROR:Recover for Modified/Size-changed.");
                continue;
            }
            for (TriggerInfo eachTrigger: triggers)
            {
                if (eachTrigger.equals(args[FILE_INDEX]) && triggerType == eachTrigger.getMonitorType() && taskType == eachTrigger.getTaskType())
                {
                    SameFlag = true;
                    break;
                }
            }
            if (SameFlag)
            {
                System.out.println("Same Trigger Already Exist!");
            } else
            {
                TriggerInfo newTrigger = new TriggerInfo();
                if ( !newTrigger.setAndExist(args[FILE_INDEX]) )
                {
                    System.out.println("File/path not exist.");
                    continue;
                } else
                {
                    newTrigger.setMonitorType(triggerType);
                    newTrigger.setTaskType(taskType);
                    triggers.addLast(newTrigger);
                    i++;
                }
            }
        }
        scan.close();
        System.out.println("--------------------Reading Command Finished.--------------------");
        setFiles();
    //END OF FUNCTION: `readin`
    }

    private void setFiles()
    {
        System.out.println("--------------------Initializing--------------------");
        System.out.println("Setting Monitors.");
        for (TriggerInfo each: triggers)
        {
            if (!each.isDictionary())
            {
                if(workspace.fileExist(each.getFileName()) == 0) // Not Exist
                {
                    FileMonitor newFile = new FileMonitor(each.getFileName(), snapShot, summary, detail,safeFile);
                    newFile.setMonitorEnable(each.getMonitorType(), each.getTaskType());
                    files.addLast(newFile);
                    workspace.addFile(each.getFileName());
                } else if (workspace.fileExist(each.getFileName()) == 1) // Already Exist
                {
                    for (FileMonitor eachFile: files)
                    {
                        if ( eachFile.getFileName().equals(each.getFileName()) )
                            eachFile.setMonitorEnable(each.getMonitorType(), each.getTaskType());
                    }
                } else // Error
                {
                    System.out.println("ERROR:已经对该文件 " + each.getFileName() +"所在的目录进行了监控");
                }
            }else
            {
//                System.out.println(workspace.dictExist(each.getFileName()));
                if (workspace.dictExist(each.getFileName()) == 0) //Not Exist
                {
                    DictMonitor newDict = new DictMonitor(each.getFileName(), snapShot, summary, detail, safeFile);
                    newDict.setMonitorEnable(each.getMonitorType(), each.getTaskType());
                    dicts.addLast(newDict);
                    workspace.addDict(each.getFileName());
                }else if (workspace.dictExist(each.getFileName()) == 1) //Already Exist
                {
                    for (DictMonitor eachDict: dicts)
                    {
                        if ( eachDict.getDictName().equals(each.getFileName()) )
                            eachDict.setMonitorEnable(each.getMonitorType(), each.getTaskType());
                    }
                }else if (workspace.dictExist(each.getFileName()) == 2) //Error
                {
                    System.out.println("ERROR：已经对目录 "+ each.getFileName() +" 的子目录或父目录进行了监控");
                }else // Error
                {
                    System.out.println("ERROR：已经对该目录 "+ each.getFileName() +" 下的文件进行了监控");
                }
            }
        }
        System.out.println("Monitors Set.");
        System.out.println("Recording File Details.");
        for (FileMonitor each: files)
            each.takeSnapShot();
        for (DictMonitor each: dicts)
        {
            each.setFileMonitor();
            each.takeSnapShot();
        }
        System.out.println("File Details Recorded.");
        System.out.println("Taking SnapShot.");
        snapShot.takeOriginalSnapShot(workspace);
        System.out.println("SnapShot Taken.");
        System.out.println("--------------------Initialization Finished.--------------------");
    //END OF FUNCTION: `setFiles`
    }

//END OF CLASS
}
