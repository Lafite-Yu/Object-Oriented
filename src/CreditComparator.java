import java.util.Comparator;

public class CreditComparator implements Comparator<Integer>
{
    /** @OVERVIEW: 针对两辆出租车之间信用度的比较，用于在派单时选择信用度最高的出租车
     * @INHERIT: None
     * @INVARIANT: None
     */

    public boolean repOK()
    {
        return true;
    }

    @Override
    /** @REQUIRES: 0 <= o1,o2 < 100;
     * @MODIFIES:  None;
     * @EFFECTS: o2.credit < o1.credit;
     */
    public int compare(Integer o1, Integer o2)
    {
        return (Main.taxiQueue.getTaxiCredit(o2) - Main.taxiQueue.getTaxiCredit(o1));
    }
}
