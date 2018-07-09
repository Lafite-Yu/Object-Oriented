import static java.lang.Thread.sleep;

public class Main implements DEFINE
{
    static Map map = new Map();
    static TaxiGUI GUI = new TaxiGUI();
//    static MapBFS mapBFS = new MapBFS();
    static TaxiQueue taxiQueue = new TaxiQueue();
    static RequestHandler requestHandler = new RequestHandler();
    static FileWriter fileWriter = new FileWriter();

    public static void main(String[] args)
    {
        try
        {
            System.out.println(LINE + "INITIALIZING" + LINE);
            map.readMap();
            GUI.LoadMap(map.map, 80);
            taxiQueue.setTaxis();
            taxiQueue.startTaxis();
            sleep(1000);
            requestHandler.readRequests();
        } catch (Exception e) {}
    }

}
