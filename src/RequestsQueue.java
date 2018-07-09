public class RequestsQueue
{
    /** @OVERVIEW: 用于存储产生的请求，调度器通过从该类中取出得到请求并开始进行调度
     * @INHERIT: None
     * @INVARIANT: None
     */
    private long[] requestType;
    private long[] floor;
    private long[] time;
    private long[] flag; // 初始化为0，0代表有效
    private int start, end;

    /** @REQUIRES:  None;
     * @MODIFIES: None;
     * @EFFECTS: None;
     */
    public boolean repOK()
    {
//        if (end >= start && requestType != null && floor != null && time != null && flag != null)
////            return true;
////        else
////            return false;
        return true;
    }

    /** @REQUIRES:  None;
     * @MODIFIES: this.requestType, this.floor, this.time, this.flag, this.start, this.end;
     * @EFFECTS: None;
     */
    public RequestsQueue()
    {
        requestType = new long[120];
        floor = new long[120];
        time = new long[120];
        flag = new long[120];
        start = 0;
        end = 0;
    }

    /** @REQUIRES:  None;
     * @MODIFIES: None;
     * @EFFECTS: \result == (this.size <= current);
     */
    public boolean isEmpty(int current)
    {
        return end <= current;
    }

    /** @REQUIRES:  None;
     * @MODIFIES: None;
     * @EFFECTS: \result == (this.size <= 0);
     */
    public boolean isEmpty()
    {
        return end <= start;
    }

    /** @REQUIRES:  info.size == 3;
     *              info[0] == 0 || info[0] == 1 || info[0] == 2;
     *              1 <= info[1] <= 10;
     *              info[2] >= 0;
     * @MODIFIES: this;
     * @EFFECTS: this.eachInfoOfTheRequest.contains(info[eachInfo]);
     *            this.eachInfoOfTheRequest.size == \old(this.eachInfoOfTheRequest.size) + 1;
     *            \result.equals(将输入的请求的信息写入类中的各个数据结构)；
     */
    public void push(long[] info)
    {
        requestType[end] = info[0];
        floor[end] = info[1];
        time[end] = info[2];
        end += 1;
    }

    /** @REQUIRES:  None;
     * @MODIFIES: this;
     * @EFFECTS: !this.infoOfTheRequests.contains(this.Requests[0]);
     *            this.infoOfTheRequests.size == \old(this.eachInfoOfTheRequest.size) - 1;
     *            returnInfo.contains(eachInfoOfTheRequest[0])
     *            \result.equals(从请求队列类中弹出第一个请求的相应信息并封装);
     */
    public long[] pop()
    {

        long[] info = new long[5];

        info[0] = start;
        info[1] = requestType[start];
        info[2] = floor[start];
        info[3] = time[start];
        info[4] = flag[start];
        start += 1;
        return info;
    }

    /** @REQUIRES:  None;
     * @MODIFIES: None;
     * @EFFECTS:  this.eachInfoOfTheRequest.contains(this.Requests[i]);
     *            this.eachInfoOfTheRequest.size == \old(this.eachInfoOfTheRequest.size);
     *            returnInfo.contains(eachInfoOfTheRequest[i])
     *            \result.equals(从请求队列类中获取第i个请求的相应信息并封装，但不从队列中删除该请求);
     */
    public long[] get(int i)
    {
        long[] info = new long[5];
        info[0] = i;
        info[1] = requestType[i];
        info[2] = floor[i];
        info[3] = time[i];
        info[4] = flag[i];
        return info;
    }

    /** @REQUIRES:  None;
     * @MODIFIES: this.flag;
     * @EFFECTS:  this.flag[i] == 1;
     */
    public void setSame(int i)
    {
        if (i < 0) return;
        flag[i] = 1;
    }

    /** @REQUIRES:  0 < i <= end;
     * @MODIFIES: this.flag;
     * @EFFECTS:  this.flag[i] == 2;
     */
    public void setPick(int i)
    {
        flag[i] = 2;
    }

    /** @REQUIRES:  0 < i <= end;
     * @MODIFIES: this.flag;
     * @EFFECTS:  this.flag[i] == 3;
     */
    public void setComplete(int i)
    {
        flag[i] = 3;
    }
    /** @REQUIRES:  0 < i <= end;
     * @MODIFIES: this.flag;
     * @EFFECTS:  this.flag[i] == 4;
     */
    public void setSameFloor(int i)
    {
        flag[i] = 4;
    }

    /** @REQUIRES:  0 < index <= end;
     *              depth > 0;
     * @MODIFIES: this.flag;
     * @EFFECTS:  this.flag[i] == 4+depth;
     */
    public void setSameFloor(int index, int depth)
    {
        flag[index] = 4 + depth;
    }
}
