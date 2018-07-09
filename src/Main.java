public class Main
{
    /** @OVERVIEW: 初始化其它类，调用请求读入和请求处理
     * @INHERIT: None
     * @INVARIANT: requestReader != null
     *              && requestsQueue != null
     *              && lights !=  null
     *              && scheduler != null
     */
    static RequestReader requestReader = new RequestReader();
    static RequestsQueue requestsQueue = new RequestsQueue();
    static Lights lights = new Lights();
    static Scheduler scheduler = new Scheduler();

    /**
     * @EFFECTS: use other methods to read in and handle the requests.
     */
    public static void main(String[] args)
    {
        requestReader.readin();
        scheduler.schedule();
    }

    /**
     * @EFFECTS: \result == I_CLASS(\this)
     */
    public boolean repOK()
    {
        if (requestReader != null && requestsQueue != null && lights != null && scheduler != null)
            return true;
        return false;
    }
}
