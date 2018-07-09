import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import static java.lang.Thread.sleep;

public class SnapShot
{
    private HashMap<String, HashMap<String, LinkedList<Object>>> originalFileStruct;
    private HashMap<String, HashMap<String, LinkedList<Object>>> newFileStruct;
    private Workspace workspace;

    public SnapShot()
    {
        originalFileStruct = new HashMap<>();
        newFileStruct = new HashMap<>();
    }

    public synchronized void takeSnapShot()
    {
        try
        {
            deepCopy();
            newFileStruct.clear();
            sleep(30);
            newFileStruct = workspace.takeSnapshot();
        }catch (Exception e){}
    }

    public synchronized void takeOriginalSnapShot(Workspace workspace)
    {
        this.workspace = workspace;
        newFileStruct = workspace.takeSnapshot();
        deepCopy();
    }

    public synchronized HashMap<String, LinkedList<Object>> getSameNameFiles(String name)
    {
        return originalFileStruct.get(name);
    }

    public synchronized HashMap<String, LinkedList<Object>> getNewSameNameFiles(String name)
    {
        return newFileStruct.get(name);
    }

    public synchronized boolean exist(String name)
    {
        return originalFileStruct.containsKey(name);
    }

    public synchronized boolean existNew(String name)
    {
        return newFileStruct.containsKey(name);
    }

    public synchronized HashMap<String, HashMap<String, LinkedList<Object>>> getFileStruct()
    {
        return newFileStruct;
    }

    public synchronized HashMap<String, HashMap<String, LinkedList<Object>>> getOriginalFileStruct()
    {
        return originalFileStruct;
    }

    public void printFileStruct()
    {
        System.out.println("SnapShot Listed Bellow.");
        for (Map.Entry<String, HashMap<String, LinkedList<Object>>> entry: newFileStruct.entrySet())
        {
            System.out.println("\t" + entry.getKey());
            for (Map.Entry<String, LinkedList<Object>> insideEntry: entry.getValue().entrySet())
            {
                System.out.println("\t\tAbsolute File Name:\t" + insideEntry.getKey());
                System.out.println("\t\t\tFile Size: " + insideEntry.getValue().get(0) + "\tModified Time: " + insideEntry.getValue().get(1));
            }
        }
    }

    public void printOriginalFileStruct()
    {
        System.out.println("Original SnapShot Listed Bellow.");
        for (Map.Entry<String, HashMap<String, LinkedList<Object>>> entry: originalFileStruct.entrySet())
        {
            System.out.println("\t" + entry.getKey());
            for (Map.Entry<String, LinkedList<Object>> insideEntry: entry.getValue().entrySet())
            {
                System.out.println("\t\tAbsolute File Name:\t" + insideEntry.getKey());
                System.out.println("\t\t\tFile Size: " + insideEntry.getValue().get(0) + "\tModified Time: " + insideEntry.getValue().get(1));
            }
        }
    }

    public void deepCopy()
    {
        originalFileStruct.clear();
        for (Map.Entry<String, HashMap<String, LinkedList<Object>>> entry: newFileStruct.entrySet())
        {
            String L1Key = entry.getKey();
            HashMap<String, LinkedList<Object>> L2HashMap = new HashMap<>();
            for (Map.Entry<String, LinkedList<Object>> insideEntry: entry.getValue().entrySet())
            {
                String L2Key = insideEntry.getKey();
                L2HashMap.put(L2Key, insideEntry.getValue());
            }
            originalFileStruct.put(L1Key, L2HashMap);
        }
    }
}
