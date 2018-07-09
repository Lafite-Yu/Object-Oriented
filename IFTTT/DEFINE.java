import java.text.SimpleDateFormat;

public interface DEFINE
{
    static final int IF_INDEX = 0;
    static final int FILE_INDEX = 1;
    static final int TRIGGER_INDEX = 2;
    static final int THEN_INDEX = 3;
    static final int TASK_INDEX = 4;

    static final int RENAMED = 0;
    static final int MODIFIED = 1;
    static final int PATH_CHANGED = 2;
    static final int SIZE_CHANGED = 3;

    static final int SUMMARY = 0;
    static final int DETAIL = 1;
    static final int RECOVER = 2;

    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSSS");


}
