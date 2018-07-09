public class Lights implements DEFINE
{
    class LightInfo
    {
        /** @OVERVIEW: 记录楼层和电梯内对应请求的一个灯的信息，包括是否亮灯、亮灯的指令序号、亮灯的指令时间
         * @Abstract_Function: AF(li)=(light, index, time) where light == li.light, index == li.index, time == li.time
         * @INVARIANT: index >= 0;
         *              && time >= 0
         */
        boolean light = false;
        int index = 0;
        long time = 0;

        /**
         * @EFFECTS: \result == I_CLASS(\this)
         */
        public boolean repOK()
        {
            if (index >= 0 && time >= 0)
                return true;
            return false;
        }
    }
    /** @OVERVIEW: 记录并处理楼层和电梯内对应请求的灯光
     * @Abstract_Function: AF(l)=(ERLight, FRUPLight, FRDOWNLight) where
     *                      ERLight == l.ERLight, FRUPLight == l.FRUPLight, FRDOWNLight == l.FRDOWNLight
     * @INVARIANT: ERLight != null && ( 0 <= i < ERLight.size ==> ERLight[i] instance of LightInfo)
     *              && FRUPLight != null && ( 0 <= i < FRUPLight.size ==> FRUPLight[i] instance of LightInfo)
     *              && FRDOWNLight != null && ( 0 <= i < FRDOWNLight.size ==> FRDOWNLight[i] instance of LightInfo)
     */
    private LightInfo[] ERLight = new LightInfo[10];
    private LightInfo[] FRUPLight = new LightInfo[10];
    private LightInfo[] FRDOWNLight = new LightInfo[10];

    /**@EFFECTS: \result == I_CLASS(\this);
     */
    public boolean repOK()
    {
        if (ERLight == null || FRUPLight == null || FRDOWNLight == null)
            return false;
        for (int i = 0; i < 10; i++)
        {
            if ( !(ERLight[i] instanceof LightInfo) || !(FRUPLight[i] instanceof LightInfo) || !(FRDOWNLight[i] instanceof LightInfo))
                return false;
        }
        return true;
    }

    /**@Modifies: this
     * @Effects: this != null
     *          && 0 <= i < ERLight.size ==> ERLight[i] == new LightInfo
     *          && 0 <= i < FRUPLight.size ==> FRUPLight[i] == new LightInfo
     *          && 0 <= i < FRDOWNLight.size ==> FRDOWNLight[i] == new LightInfo
     */
    public Lights()
    {
        for (int i = 0; i < 10; i++)
        {
            ERLight[i] = new LightInfo();
            FRUPLight[i] = new LightInfo();
            FRDOWNLight[i] = new LightInfo();
        }
    }

    /** @REQUIRES:  type == DEFINE.ER || type == DEFINE.FRUP || type == DEFINE.FRDOWN
     *              && 1 <= floor <= 10;
     * @MODIFIES: None;
     * @EFFECTS: type == DEFINE.ER ==> \result == ERLight[floor-1]
     *          type == DEFINE.FRUP ==> \result == FRUPLight[floor-1]
     *          type == DEFINE.FRDOWN ==> \result == FRDOWNLight[floor-1]
     */
    public LightInfo getLight(int type, int floor)
    {
        if (type == ER)
            return ERLight[floor-1];
        else if (type == FRUP)
            return FRUPLight[floor-1];
        else
            return FRDOWNLight[floor-1];
    }

    /** @REQUIRES:  request != null;
     * @MODIFIES: this.ERLight, this.FRDOWNLight, this.FRUPLight;
     * @EFFECTS: request.type == DEFINE.ER
     *              ==>如果ERLight[request.floor -1]没有亮起 ==>点亮ERLight[request.floor -1]并将对应请求的信息写入其中，\result == true;
     *              ==>如果ERLight[request.floor -1]已经亮起 ==> \result == false;
     *          request.type == DEFINE.FRUP
     *              ==>如果FRUPLight[request.floor -1]没有亮起 ==>点亮FRUPLight[request.floor -1]并将对应请求的信息写入其中，\result == true;
     *              ==>如果FRUPLight[request.floor -1]已经亮起 ==> \result == false;
     *          request.type == DEFINE.FRDOWN
     *              ==>如果FRDOWNLight[request.floor -1]没有亮起 ==>点亮FRDOWNLight[request.floor -1]并将对应请求的信息写入其中，\result == true;
     *              ==>如果FRDOWNLight[request.floor -1]已经亮起 ==> \result == false;
     */
    public boolean lightOn(Request request)
    {
        if (request.getType() == 0)
        {
            if (ERLight[request.getFloor() -1].light)
                return false;
            else{
                ERLight[request.getFloor() -1].light = true;
                ERLight[request.getFloor() -1].time = request.getTime();
                ERLight[request.getFloor() -1].index = request.getIndex();
            }
        }else if (request.getType() == 1)
        {
            if (FRUPLight[request.getFloor() -1].light)
                return false;
            else{
                FRUPLight[request.getFloor() -1].light = true;
                FRUPLight[request.getFloor() -1].time = request.getTime();
                FRUPLight[request.getFloor() -1].index = request.getIndex();
            }
        }else
        {
            if (FRDOWNLight[request.getFloor() -1].light)
                return false;
            else{
                FRDOWNLight[request.getFloor() -1].light = true;
                FRDOWNLight[request.getFloor() -1].time = request.getTime();
                FRDOWNLight[request.getFloor() -1].index = request.getIndex();
            }
        }
        return true;
    }

    /** @REQUIRES:  type == DEFINE.ER || type == DEFINE.FRUP || type == DEFINE.FRDOWN
     *              && 1 <= floor <= 10;
     * @MODIFIES: this.ERLight, this.FRDOWNLight, this.FRUPLight;
     * @EFFECTS: request.type == DEFINE.ER ==> ERLight[request.floor -1].light == false
     *              request.type == DEFINE.FRUP ==> FRUPLight[request.floor -1].light == false
     *              request.type == DEFINE.FRDOWN ==> FRDOWNLight[request.floor -1].light == false
     */
    public void lightOff(int type, int floor)
    {
        if (type == ER)
            ERLight[floor-1].light = false;
        else if (type == FRUP)
            FRUPLight[floor-1].light = false;
        else
            FRDOWNLight[floor-1].light = false;
    }
}
