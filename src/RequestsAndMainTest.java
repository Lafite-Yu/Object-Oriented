
import org.junit.Test; 
import org.junit.Before; 
import org.junit.After; 
import static org.junit.Assert.*;


/** 
* Requests Tester. 
* 
* @author <Authors name> 
* @since <pre>六月 6, 2018</pre> 
* @version 1.0 
*/ 
public class RequestsAndMainTest
{
    Requests requests = new Requests();

    @Before
    public void before() throws Exception 
    {
        requests = new Requests();
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
        assertTrue(requests.repOK());
    } 

        /** 
    * 
    * Method: judger(String str) 
    * 
    */ 
    @Test
    public void testJudger() throws Exception 
    { 
    //TODO: Test goes here... 
        assertFalse(requests.judger("(ER,5,1)"));
        assertTrue(requests.judger("(FR,1,UP,0)"));
        assertFalse(requests.judger("123"));
        assertFalse(requests.judger("(FR,11,UP,0)"));
        assertFalse(requests.judger("(FR,10,UP,0)"));
        assertFalse(requests.judger("(FR,0,UP,0)"));
        assertFalse(requests.judger("(FR,1,DOWN,0)"));
        assertFalse(requests.judger("(ER,1,9999999999)"));
        assertFalse(requests.judger("(ER,1,-1)"));
        assertTrue(requests.judger("(FR,10,DOWN,8)"));
        assertFalse(requests.judger("(FR,5,DOWN,5)"));


    }

}

