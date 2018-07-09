import java.io.File;

public class TriggerInfo implements DEFINE
{
    private String fileName;
    private File filePointer;
    private int monitorType;
    private int taskType;

    public boolean setAndExist(String fileName)
    {
        this.fileName = fileName;
        this.filePointer = new File(fileName);
        if (filePointer.exists())
            return true;
        else
            return false;
    }

    public boolean equals(String newFile)
    {
        return fileName.equals(newFile);
    }

    public String getFileName()
    {
        return fileName;
    }

    public boolean isDictionary()
    {
        return this.filePointer.isDirectory();
    }

    public void setMonitorType(int monitorType)
    {
        this.monitorType = monitorType;
    }

    public void setTaskType(int taskType)
    {
        this.taskType = taskType;
    }

    public int getMonitorType()
    {
        return monitorType;
    }

    public int getTaskType()
    {
        return taskType;
    }
}
