import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.LinkedList;

public class TaxiQueue implements DEFINE
{
    private LinkedList<Taxi> taxis = new LinkedList<>();


    /** @REQUIRES: None;
     * @MODIFIES:  this;
     * @EFFECTS: \result.equals(对taxis队列中的100辆出租车进行初始化设定);
     */
    public void setTaxis()
    {

        System.out.println("\t"+"SETTING TAXIS");
        for (int i = 0; i < 100; i++)
        {
            Taxi taxi = new Taxi(i);
            this.taxis.addLast(taxi);
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

}
