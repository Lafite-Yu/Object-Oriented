public class Request
{
    /** @OVERVIEW: 记录请求信息
     * @INHERIT: None
     * @Abstract_Function: AF(r)=(index, type, floor, time, handled) where index==r.index, type==r.type, floor==r.floor,
     *                      time==r.time, handled==r.handled
     * @INVARIANT: index >= 0;
     *              && type == DEFINE.ER || type == DEFINE.FRUP || type == DEFINE.FRDOWN
     *              && 1 <= floor <= 10
     *              && time >= 0
     */
    private int index = 0;
    private int type = 0;
    private int floor = 0;
    private long time = 0;
    private boolean handled = false;

    /**
     * @EFFECTS: \result == I_CLASS(\this)
     */
    public boolean repOK()
    {
        if (index >= 0 && (type == DEFINE.ER || type == DEFINE.FRUP || type == DEFINE.FRDOWN) && floor >= 1 && floor <= 10 && time >= 0)
            return true;
        return false;
    }

    /**@Requires: index >= 0;
     *              && type == DEFINE.ER || type == DEFINE.FRUP || type == DEFINE.FRDOWN
     *              && 1 <= floor <= 10
     *              && time >= 0
     *@Modifies: this
     * @Effects: this != null
     */
    public Request(int index, int type, int floor, long time)
    {
        this.type = type;
        this.floor = floor;
        this.time = time;
        this.index = index;
        this.handled = false;
    }

    /** @Effects: \result == this.index;
     * */
    public int getIndex()
    {
        return index;
    }

    /** @Effects: \result == this.type;
     */
    public int getType()
    {
        return type;
    }

    /** @Effects: \result == this.floor;
     */
    public int getFloor()
    {
        return floor;
    }

    /** @Effects: \result == this.time;
     */
    public long getTime()
    {
        return time;
    }

    /**@Modifies: this.handled
     * @Effects: this.handled == true
     */
    public void setHandled()
    {
        this.handled = true;
    }

    /**@Effects: \result == this.handled
     */
    public boolean isHandled()
    {
        return handled;
    }
}
