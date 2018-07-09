import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

public class TaxiQueue implements DEFINE
{
    /** @OVERVIEW: 100辆出租车线程的队列;
     * @INHERIT: None;
     * @INVARIANT: None;
     */

    private LinkedList<Taxi> taxis = new LinkedList<>();

    public boolean repOK()
    {
        if (taxis != null)
            return true;
        else
            return false;
    }

    /** @REQUIRES: None;
     * @MODIFIES:  this;
     * @EFFECTS: \result.equals(对taxis队列中的100辆出租车进行初始化设定);
     */
    public void setTaxis()
    {

        System.out.println("\t"+"SETTING TAXIS");
        for (int i = 0; i < 100; i++)
        {
            if (i < 30)
            {
                Taxi taxi = new VIPTaxi(i);
                this.taxis.addLast(taxi);
            }
            else
            {
                Taxi taxi = new Taxi(i);
                this.taxis.addLast(taxi);
            }
            Main.GUI.SetTaxiType(i, i<30?1:0);
        }
    }

    /** @REQUIRES: None;
     * @MODIFIES:  None;
     * @EFFECTS: \result.equals(开始运行taxis队列中的100辆出租车);
     */
    public void startTaxis()
    {
        for(Taxi each: taxis)
            each.start();
//        taxis.getFirst().start();
    }

    /** @REQUIRES: 0 <= i < 100;
     * @MODIFIES:  None;
     * @EFFECTS: None;
     */
    public Point getTaxiPosition(int i)
    {
        return taxis.get(i).getPosition();
    }

    /** @REQUIRES: 0 <= i < 100;
     * @MODIFIES:  None;
     * @EFFECTS: None;
     */
    public int getTaxiCredit(int i)
    {
        return taxis.get(i).getCredit();
    }

    /** @REQUIRES: 0 <= i < 100;
     * @MODIFIES:  None;
     * @EFFECTS: None;
     */
    public int getTaxiStatus(int i)
    {
        return taxis.get(i).getStatus();
    }

    /** @REQUIRES: 0 <= i < 100;
     * @MODIFIES:  None;
     * @EFFECTS: (taxi[i] instanceof VIPTaxi) ==> true;
     */
    public boolean getTaxiType(int i)
    {

        return (taxis.get(i) instanceof VIPTaxi);
    }

    /** @REQUIRES: 0 <= i < 100;
     *              0 <= ID < Main.requestHandler.threads.length;
     * @MODIFIES:  None;
     * @EFFECTS: \result.equals(将指定的请求分配给指定的出租车);
     */
    public void setTaxiRequest(int i, int ID)
    {
        taxis.get(i).setRequest(ID);
    }

    /** @REQUIRES: 0 <= i < 100;
     * @MODIFIES:  None;
     * @EFFECTS: \result.equals(使得指定的出租车增加给定的信用度）;
    @ */
    public void addTaxiCredit(int taxiNum, int credit)
    {

        taxis.get(taxiNum).addCredit(credit);
    }

    /** @REQUIRES: 0 <= i < 100;
     *              status = STOP || status == WAITING;
     *              0 <= x,y <=79;
     * @MODIFIES:  None;
     * @EFFECTS: \result.equals(将指定的出租车设定成给定的状态);
     */
    public void initTaxi(int i, int status, int credit, int x, int y)
    {

        taxis.get(i).initTaxi(status, credit, x, y);
    }

    /** @REQUIRES: 0 <= i < 100;
     * @MODIFIES:  None;
     * @EFFECTS: (taxi[num] instanceof VIPTaxi) ==> \result.equals(VIPTaxi.iterator);
     *          (!taxi[num] instanceof VIPTaxi) ==> \result.equals(null);
    @ */
    public ListIterator<HistoryRecord> getIterator(int num)
    {
        if (getTaxiType(num))
        {
            return ((VIPTaxi)taxis.get(num)).iterator();
        } else
        {
            return null;
        }
    }

}
