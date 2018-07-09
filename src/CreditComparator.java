import java.util.Comparator;

public class CreditComparator implements Comparator<Integer>
{

    @Override
    public int compare(Integer o1, Integer o2)
    {
        return (Main.taxiQueue.getTaxiCredit(o1) - Main.taxiQueue.getTaxiCredit(o2));
    }
}
