import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import static java.lang.Thread.sleep;

public class SafeFile implements DEFINE
{
    private SnapShot snapShot;

    public SafeFile(SnapShot snapShot)
    {
        this.snapShot = snapShot;
    }

    public LinkedList<Object> getFileInfo(String pathname)
    {
        synchronized (SafeFile.class)
        {
            File file = new File(pathname);
            LinkedList<Object> fileInfo = new LinkedList<>();
            if (file.exists() && !file.isDirectory())
            {
                fileInfo.addLast(file.getName());
                fileInfo.addLast(file.length());
//            System.out.println("||||pathname:" + pathname + " " + file.length());
                fileInfo.addLast(simpleDateFormat.format(file.lastModified()));
                fileInfo.addLast(file.getAbsoluteFile().toString());
            } else
            {
                System.out.printf("File `%s` Not Exist.\n", pathname);
            }
            return fileInfo;
        }
    }

    public boolean recover(String newPath, String originalPath)
    {
        synchronized (SafeFile.class)
        {
            try
            {
                File newFile = new File(newPath);
                File originalFile = new File(originalPath);
                if (newFile.exists())
                {
                    newFile.renameTo(originalFile);
                    return true;
                }
                return false;
            } catch (Exception e) {return false;}
        }
    }

    //重命名 rename(原绝对文件名, 新文件名)
    public void rename(String path, String newName)
    {
        synchronized (SafeFile.class)
        {
            try
            {
                sleep(300);
                File file = new File(path);
                if (!file.exists())
                {
                    System.out.println("Rename Error: file not exist.");
                    return;
                }
                if (file.isDirectory())
                {
                    System.out.println("Rename Error: try to rename a directory.");
                    return;
                }
                String newPath = file.getParent() + File.separator + newName;
                if (file.renameTo(new File(newPath)))
                {
                    System.out.println("Rename Succeed: `" + path +"` renamed to `" + newPath +"`.");
                    snapShot.takeSnapShot();
                } else
                {
                    System.out.println("Rename Error: rename failed.");
                }
            } catch (Exception e) {}
        }
    }

    //移动文件 moveFile(文件绝对文件名, 目标目录绝对地址)
    public void moveFile(String newPath, String originalPath)
    {
        synchronized (SafeFile.class)
        {
            try
            {
                sleep(300);
                File originalFile = new File(originalPath);
                File newFile = new File(newPath);
                if (!originalFile.exists() || !newFile.exists())
                {
                    System.out.println("File Move Error: Not Exist.");
                    return;
                }
                if (originalFile.isDirectory())
                {
                    System.out.println("File Move Error: try to move a directory.");
                    return;
                }
                if (!newFile.isDirectory())
                {
                    System.out.println("File Move Error: try to move to a file.");
                    return;
                }
                if (originalFile.renameTo(new File(newFile.getPath() + File.separator + originalFile.getName())))
                {
                    System.out.println("File Move Success.");
                    snapShot.takeSnapShot();
                } else
                {
                    System.out.println("File Move Error.");
                }
            }catch (Exception e) {}
        }
    }

    //创建新文件 createNewFile(文件路径，文件名)
    public void createNewFile(String pathName, String fileName)
    {
        synchronized (SafeFile.class)
        {
            try
            {
                sleep(300);
                File path = new File(pathName);
                if (!path.exists())
                {
                    System.out.println("CreateNewFile Error:Path not exist.");
                    return;
                }
                if (!path.isDirectory())
                {
                    System.out.println("CreateNewFile Error:not a dictionary.");
                    return;
                }
                File file = new File(path.getPath() + File.separator + fileName);
                try
                {
                    if (file.createNewFile())
                    {
                        System.out.println("CreateNewFile Succeed.");
                        snapShot.takeSnapShot();
                    } else
                    {
                        System.out.println("CreateNewFile ERROR.");
                    }
                } catch (Exception e)
                {
                    System.out.println("CreateNewFile ERROR.");
                }
            } catch (Exception e) {}
        }
    }

    //删除文件
    public void deleteFile(String path)
    {
        synchronized (SafeFile.class)
        {
            try
            {
                sleep(300);
                File file = new File(path);
                if (!file.exists() || file.isDirectory())
                {
                    System.out.println("Delete File Error.");
                    return;
                }

                if (file.delete())
                {
                    System.out.println("Delete File Succeed.");
                    snapShot.takeSnapShot();
                } else
                {
                    System.out.println("Delete File Error.");
                }
            } catch (Exception e) {}
        }
    }

    //删除目录及其下所有文件
    public void deleteDict(String path)
    {
        synchronized (SafeFile.class)
        {
            try
            {
                sleep(300);
                File file = new File(path);
                File[] files = file.listFiles();
                if (files == null)
                    return;
                for (File each : files)
                {
                    if (each.isDirectory())
                    {
                        deleteDict(each.getAbsolutePath());
                    }
                    if (each.delete())
                    {
                        System.out.println("Dictionary Delete Succeed.");
                        snapShot.takeSnapShot();
                    }
                    else
                        System.out.println("Dictionary Delete Error.");
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    //文件内容写入,不存在则新建
    public void write(String fileName, String content)
    {
        synchronized (SafeFile.class)
        {
            try
            {
                sleep(300);
                File file = new File(fileName);
                if (file.isDirectory())
                {
                    System.out.println("Write File Error: try to write a directory.");
                    return;
                }
                    FileWriter writer = new FileWriter(fileName, true);
                    writer.write(content);
                    writer.flush();
                    writer.close();
                    System.out.println("Write File Succeed.");
                    snapShot.takeSnapShot();
            } catch (Exception e)
            {

            }
        }
    }

    public void changeLastModified(String fileName)
    {
        synchronized (SafeFile.class)
        {
            try
            {
                sleep(300);
                File file = new File(fileName);
                if (file.setLastModified(System.currentTimeMillis()))
                {
                    System.out.println("Modify File Succeed.");
                    snapShot.takeSnapShot();
                }
                else
                    System.out.println("Modify File Error.");
            }catch (Exception e) {}
        }
    }

//END OF CLASS
}
