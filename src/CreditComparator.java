import java.util.Comparator;

public class CreditComparator implements Comparator<Integer>
{

    @Override
    /** @REQUIRES: 0 <= i < 100;
     * @MODIFIES:  None;
     * @EFFECTS: o2.credit < o1.credit;
     */
    public int compare(Integer o1, Integer o2)
    {
        return (Main.taxiQueue.getTaxiCredit(o2) - Main.taxiQueue.getTaxiCredit(o1));
    }
}
