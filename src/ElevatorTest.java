
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import static org.junit.Assert.*;

public class ElevatorTest
{
    private static Elevator elevator = new Elevator();

    @Before
    public void before() throws Exception
    {

    }

    @After
    public void after() throws Exception
    {
    }

    @Test
    public void testRepOK() throws Exception
    {
        //TODO: Test goes here...
        assertTrue(elevator.repOK());
    }

    /**
     * Method: getLight(int floor)
     */
    @Test
    public void testGetLight() throws Exception
    {
        //TODO: Test goes here...
        assertFalse(elevator.getLight(1));
    }

    /**
     * Method: lightOn(int floor)
     */
    @Test
    public void testLightOn() throws Exception
    {
//TODO: Test goes here...
        elevator.lightOn(1);
        assertTrue(elevator.getLight(1));
    }

    /**
     * Method: lightOff(int floor)
     */
    @Test
    public void testLightOff() throws Exception
    {
        //TODO: Test goes here...
        elevator.lightOff(1);
        assertFalse(elevator.getLight(1));
    }


} 
