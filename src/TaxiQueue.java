import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.LinkedList;

public class TaxiQueue implements DEFINE
{
    private LinkedList<Taxi> taxis = new LinkedList<>();


    public void setTaxis()
    {
        System.out.println("\t"+"SETTING TAXIS");
        for (int i = 0; i < 100; i++)
        {
            Taxi taxi = new Taxi(i);
            this.taxis.addLast(taxi);
        }
    }

    public void startTaxis()
    {
        for(Taxi each: taxis)
            each.start();
//        taxis.getFirst().start();
    }

    public Point getTaxiPosition(int i)
    {
        return taxis.get(i).getPosition();
    }

    public int getTaxiCredit(int i)
    {
        return taxis.get(i).getCredit();
    }

    public int getTaxiStatus(int i)
    {
        return taxis.get(i).getStatus();
    }

    public void setTaxiRequest(int i, int ID)
    {
        taxis.get(i).setRequest(ID);
    }

    public void addTaxiCredit(int taxiNum, int credit)
    {
        taxis.get(taxiNum).addCredit(credit);
    }

}
