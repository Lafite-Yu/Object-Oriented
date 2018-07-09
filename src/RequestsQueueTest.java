
import org.junit.Test; 
import org.junit.Before; 
import org.junit.After; 
import static org.junit.Assert.*;

public class RequestsQueueTest 
{ 

    private static RequestsQueue requestsQueue;
    @Before
    public void before() throws Exception 
    {
        requestsQueue = new RequestsQueue();
    } 

    @After
    public void after() throws Exception 
    { 
    }

    /**
     *
     * Method: repOK()
     *
     */
    @Test
    public void testRepOK() throws Exception
    {
        //TODO: Test goes here...
        assertTrue(requestsQueue.repOK());
    }

    /**
    * Method: isEmpty(int current)
    */ 
    @Test
    public void testIsEmptyCurrent() throws Exception 
    { 
    //TODO: Test goes here...
        assertTrue(requestsQueue.isEmpty());
        long info[] = new long[3];
        info[0] = 0;
        info[1] = 1;
        info[2] = 0;
        requestsQueue.push(info);
        assertTrue(requestsQueue.isEmpty(1));
        assertFalse(requestsQueue.isEmpty(0));
    }
    
    /**
    * Method: push(long[] info)
    */ 
    @Test
    public void testPush() throws Exception 
    { 
    //TODO: Test goes here...
        assertTrue(requestsQueue.isEmpty());
        long info[] = new long[3];
        info[0] = 0;
        info[1] = 1;
        info[2] = 0;
        requestsQueue.push(info);
        assertFalse(requestsQueue.isEmpty());
    } 
    
    /**
    * Method: pop()
    */ 
    @Test
    public void testPop() throws Exception 
    { 
    //TODO: Test goes here...
        long info[] = new long[3];
        info[0] = 0;
        info[1] = 1;
        info[2] = 0;
        requestsQueue.push(info);
        requestsQueue.push(info);

        long getInfo0[] = requestsQueue.pop();
        long expectedInfo1[] = {0, 0, 1, 0, 0};
        assertArrayEquals(expectedInfo1, getInfo0);

        long getInfo1[] = requestsQueue.pop();
        long expectedInfo2[] = {1, 0, 1, 0, 0};
        assertArrayEquals(expectedInfo2, getInfo1);

    } 
    
    /**
    * Method: get(int i)
    */ 
    @Test
    public void testGet() throws Exception 
    { 
    //TODO: Test goes here...
        long info[] = new long[3];
        info[0] = 0;
        info[1] = 1;
        info[2] = 0;
        requestsQueue.push(info);
        requestsQueue.push(info);

        long getInfo0[] = requestsQueue.get(0);
        long expectedInfo1[] = {0, 0, 1, 0, 0};
        assertArrayEquals(expectedInfo1, getInfo0);

        long getInfo1[] = requestsQueue.get(1);
        long expectedInfo2[] = {1, 0, 1, 0, 0};
        assertArrayEquals(expectedInfo2, getInfo1);
    } 
    
    /**
    * Method: setSame(int i)
    */ 
    @Test
    public void testSetSame() throws Exception 
    { 
    //TODO: Test goes here...
        long info[] = new long[3];
        info[0] = 0;
        info[1] = 1;
        info[2] = 0;
        requestsQueue.push(info);

        requestsQueue.setSame(0);
        long getInfo0[] = requestsQueue.pop();
        long expectedInfo1[] = {0, 0, 1, 0, 1};
        assertArrayEquals(expectedInfo1, getInfo0);
    } 
    
    /**
    * Method: setPick(int i)
    */ 
    @Test
    public void testSetPick() throws Exception 
    { 
    //TODO: Test goes here...
        long info[] = new long[3];
        info[0] = 0;
        info[1] = 1;
        info[2] = 0;
        requestsQueue.push(info);

        requestsQueue.setPick(0);
        long getInfo0[] = requestsQueue.pop();
        long expectedInfo1[] = {0, 0, 1, 0, 2};
        assertArrayEquals(expectedInfo1, getInfo0);
    } 
    
    /**
    * Method: setComplete(int i)
    */ 
    @Test
    public void testSetComplete() throws Exception 
    { 
    //TODO: Test goes here...
        long info[] = new long[3];
        info[0] = 0;
        info[1] = 1;
        info[2] = 0;
        requestsQueue.push(info);

        requestsQueue.setComplete(0);
        long getInfo0[] = requestsQueue.pop();
        long expectedInfo1[] = {0, 0, 1, 0, 3};
        assertArrayEquals(expectedInfo1, getInfo0);
    } 
    
    /**
    * Method: setSameFloor(int i)
    */ 
    @Test
    public void testSetSameFloorI() throws Exception 
    { 
    //TODO: Test goes here...
        long info[] = new long[3];
        info[0] = 0;
        info[1] = 1;
        info[2] = 0;
        requestsQueue.push(info);

        requestsQueue.setSameFloor(0);
        long getInfo0[] = requestsQueue.pop();
        long expectedInfo1[] = {0, 0, 1, 0, 4};
        assertArrayEquals(expectedInfo1, getInfo0);
    } 
    
    /**
    * Method: setSameFloor(int index, int depth)
    */ 
    @Test
    public void testSetSameFloorForIndexDepth() throws Exception 
    { 
    //TODO: Test goes here...
        long info[] = new long[3];
        info[0] = 0;
        info[1] = 1;
        info[2] = 0;
        requestsQueue.push(info);

        requestsQueue.setSameFloor(0, 5);
        long getInfo0[] = requestsQueue.pop();
        long expectedInfo1[] = {0, 0, 1, 0, 9};
        assertArrayEquals(expectedInfo1, getInfo0);
    } 
    
        
    } 
