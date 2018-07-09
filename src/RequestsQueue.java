import java.util.LinkedList;

public class RequestsQueue
{
    /** @OVERVIEW: 用于存储产生的请求，调度器通过从该类中取出得到请求并开始进行调度
     * @Abstract_Function: AF(rq) == (requestLinkedList) where requestLinkedList == rq.requestLinkedList
     * @INVARIANT: requestLinkedList != null && (0 <= i < requestLinkedList.size-1 ==> requestLinkedList.get(i).index < requestLinkedList.get(i+1).index)
     */

    private LinkedList<Request> requestLinkedList = new LinkedList<>();

    /**
     * @EFFECTS: \result == I_CLASS(\this)
     */
    public boolean repOK()
    {
        if (requestLinkedList == null)
            return false;
        for (int i = 0; i < requestLinkedList.size()-1; i++)
        {
            if (requestLinkedList.get(i).getIndex() >= requestLinkedList.get(i+1).getIndex())
                return false;
        }
        return true;
    }


    /** @REQUIRES:  newRequest != null && newRequest.index > requestLinkedList.getLast().index
     * @MODIFIES: this.requestLinkedList;
     * @EFFECTS: this.requestLinkedList.contains(newRequest) && requestLinkedList.size == \old(requestLinkedList.size)+1
     */
    public void push(Request newRequest)
    {
        requestLinkedList.addLast(newRequest);
    }


    /** @EFFECTS: \result == requestLinkedList.size
     */
    public int size()
    {
        return requestLinkedList.size();
    }

    /** @REQUIRES:  0 <= index < requestLinkedList.size
     * @EFFECTS: \result == requestLinkedList.get(index)
     */
    public Request get(int index)
    {
        return requestLinkedList.get(index);
    }

    /** @EFFECTS: \result == requestLinkedList.isEmpty()
     */
    public boolean isEmpty()
    {
        return requestLinkedList.isEmpty();
    }

    /** @EFFECTS: \result == requestLinkedList.isEmpty()
     */
    public Request getEarliestUnhandled()
    {
        for (int i = 0; i < size(); i++)
        {
            if (!requestLinkedList.get(i).isHandled())
                return requestLinkedList.get(i);
        }
        return new Request(999, 0, 1, 0);
    }

    /**@Modifies: this.requestLinkedList
     * @Effects: requestLinkedList.get(index).handled == true
     */
    public void setHandled(int index)
    {
        requestLinkedList.get(index).setHandled();
    }
}
