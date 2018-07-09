public class Elevator
{
    /** @OVERVIEW: 记录电梯请求产生的电梯亮灯情况
     * @INHERIT: None
     * @INVARIANT: None
     */
    private boolean[] light;

    /** @REQUIRES:  None;
     * @MODIFIES: None;
     * @EFFECTS: None;
     */
    public boolean repOK()
    {
//        if (light != null)
////            return true;
////        else
////            return false;
        return true;
    }

    /** @REQUIRES:  None;
     * @MODIFIES: this.light;
     * @EFFECTS: None;
     */
    public Elevator()
    {
        light = new boolean[10];
    }

    /** @REQUIRES:  1 <= floor <= 10;
     * @MODIFIES: None;
     * @EFFECTS: None;
     */
    public boolean getLight(int floor)
    {
        return light[floor - 1];
    }

    /** @REQUIRES:  1 <= floor <= 10;
     * @MODIFIES: this.light;
     * @EFFECTS: light[floor-1] == true;
     */
    public boolean lightOn(int floor)
    {
        light[floor - 1] = true;
        return true;
    }

    /** @REQUIRES:  1 <= floor <= 10;
     * @MODIFIES: this.light;
     * @EFFECTS: light[floor-1] == false;
     */
    public boolean lightOff(int floor)
    {
        light[floor - 1] = false;
        return false;
    }
}
