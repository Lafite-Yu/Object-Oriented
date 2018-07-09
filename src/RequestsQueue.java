public class RequestsQueue
{
	private long[] requestType;
	private long[] floor;
	private long[] time;
	private long[] flag; // 初始化为0，0代表有效
    private int start, end;
    
	public RequestsQueue()
	{
		requestType = new long[120];
		floor = new long[120];
		time = new long[120];
		flag = new long[120];
        start = 0;
        end = 0;
	}
	
    public boolean isEmpty(int current)
    {
        return end <= current;
    }

    public boolean isEmpty()
    {
        return end <= start;
    }
    
	public void push(long[] info)
	{
        requestType[end] = info[0];
        floor[end] = info[1];
        time[end] = info[2];
        end += 1;
    }
    
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


    public void setSame(int i)
    {
        flag[i] = 1;
    }

    public void setPick(int i)
    {
        flag[i] = 2;
    }

    public void setComplete(int i)
    {
        flag[i] = 3;
    }

    public void setSameFloor(int i)
    {
        flag[i] = 4;
    }
    
    public void setSameFloor(int index, int depth)
    {
        flag[index] = 4+depth;
    }
}
