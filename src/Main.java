import java.awt.*;
import java.util.LinkedList;

import static java.lang.Thread.sleep;

public class Main implements DEFINE
{
    static Map map = new Map();
    static TaxiGUI GUI = new TaxiGUI();
//    static MapBFS mapBFS = new MapBFS();
    static TaxiQueue taxiQueue = new TaxiQueue();
    static RequestHandler requestHandler = new RequestHandler();
    static FileWriter fileWriter = new FileWriter();

    /**REQUIRES: None;
     * @MODIFIES: None;
     * @EFFECTS: None;
     */
    public static void main(String[] args)
    {
        try
        {
//            testSample();
            System.out.println(LINE + "INITIALIZING" + LINE);
            requestHandler.readRequests();
        } catch (Exception e) { e.printStackTrace(); }
    }

//    public static void testSample()
//    {
//        System.out.println(LINE + "TEST SAMPLE" + LINE);
//        map.readMap(MAP_PATH);
//        GUI.LoadMap(map.map, 80);
//        GUI.SetTaxiStatus(1, new Point(1,1), 1);
////        GUI.RequestTaxi(new Point(1 ,1), new Point(35,20));
//        GUI.RequestTaxi(new Point(35,20), new Point(70,70));
////        long time = System.currentTimeMillis();
////        GUIGv.AddFlow(5,4,6,4);
////        gv.stay(5000);
//        LinkedList<Integer> path =  GUIGv.m.getPath(new Point(35, 20), new Point(35,20), new Point(70,70), true);
////        System.out.printf("FLow:%d\n", GUIGv.GetFlow(5,4,6,4));
////        System.out.println("time" + (System.currentTimeMillis() - time) );
////        System.out.printf("Path Length:%d\n", path.size()-1);
//        for (int aInt: path)
//        {
//            System.out.printf("(%d,%d)\n", aInt/80,aInt%80);
//            gv.stay(200);
//            GUI.SetTaxiStatus(1, new Point(aInt/80,aInt%80), 1);
//        }
//    }

}
