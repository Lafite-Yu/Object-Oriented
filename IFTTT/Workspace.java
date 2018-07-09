import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;

public class Workspace
{
    private LinkedList<String> files;
    private LinkedList<Boolean> isDict;
    private HashMap<String, HashMap<String, LinkedList<Object>>> snapShot;
    private SafeFile safeFile;

    public Workspace(SafeFile safeFile)
    {
        this.files = new LinkedList<>();
        this.isDict = new LinkedList<>();
        this.snapShot = new HashMap<>();
        this.safeFile = safeFile;
    }

    public int fileExist(String inFile)
    {
        for (int i = 0; i < files.size(); i++)
        {
            if (isDict(i))
            {
//                if(files.get(i).indexOf(inFile) >= 0)
                if(inFile.indexOf(files.get(i)) >= 0)
                    return 2;
            }else
            {
                if(files.get(i).equals(inFile))
                    return 1;
            }
        }
        return 0;
    }

    public int dictExist(String inDict)
    {
        for (int i = 0; i < files.size(); i++)
        {
            if (isDict(i))
            {
                if (files.get(i).equals(inDict))
                    return 1;
                else if(files.get(i).indexOf(inDict) >= 0 || inDict.indexOf(files.get(i)) >= 0)
                    return 2;
            }else
            {
                if(files.get(i).indexOf(inDict) >= 0)
                    return 3;
            }
        }
        return 0;
    }

    public void addFile(String inFile)
    {
        files.addLast(inFile);
        isDict.addLast(false);
    }

    public void addDict(String inDict)
    {
        files.addLast(inDict);
        isDict.addLast(true);
    }

    public boolean isDict(int i)
    {
        return isDict.get(i);
    }

    public HashMap<String, HashMap<String, LinkedList<Object>>> takeSnapshot()
    {
        for (String each: files)
        {
            File eachFile = new File(each);
            if (eachFile.isDirectory())
            {
                takeSnapshot(eachFile);
            }
            else
            {
                LinkedList<Object> fileInfo = safeFile.getFileInfo(each);
                String absoluteName = each;
                String name = (String)fileInfo.get(0);
                long length = (Long)fileInfo.get(1);
                String modifiedTime = (String)fileInfo.get(2);
                LinkedList<Object> info = new LinkedList<>();
                info.addLast(length);
                info.addLast(modifiedTime);
                if (snapShot.containsKey(name))
                    snapShot.get(name).put(absoluteName, info);
                else
                {
                    HashMap<String, LinkedList<Object>> aFile = new HashMap<>();
                    aFile.put(absoluteName, info);
                    snapShot.put(name, aFile);
                }
            }
        }
        return snapShot;
    }

    private void takeSnapshot(File path)
    {
        File[] files = path.listFiles();
        if (files == null) //Bottom
            return;
        for (File eachFile: files)
        {
            if (eachFile.isDirectory())
                takeSnapshot(eachFile);
            else
            {
                String absoluteName = eachFile.getAbsoluteFile().toString();
                LinkedList<Object> fileInfo = safeFile.getFileInfo(absoluteName);
                String name = (String)fileInfo.get(0);
                long length = (Long)fileInfo.get(1);
                String modifiedTime = (String)fileInfo.get(2);
                LinkedList<Object> info = new LinkedList<>();
                info.addLast(length);
                info.addLast(modifiedTime);
                if (snapShot.containsKey(name))
                    snapShot.get(name).put(absoluteName, info);
                else
                {
                    HashMap<String, LinkedList<Object>> aFile = new HashMap<>();
                    aFile.put(absoluteName, info);
                    snapShot.put(name, aFile);
                }
            }
        }
    }
}
