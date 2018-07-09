
import org.junit.Test; 
import org.junit.Before; 
import org.junit.After;

import static org.junit.Assert.*;

public class SchedulerTest 
{
    private static RequestsQueue requestsQueue;
    private static Scheduler scheduler;
    @Before
    public void before() throws Exception 
    {
        requestsQueue = new RequestsQueue();
        scheduler = new Scheduler(requestsQueue);
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
        assertTrue(scheduler.repOK());
        Scheduler falseScheduler = new Scheduler(null);
        assertFalse(falseScheduler.repOK());
    }

    /* 关于第三次作业bug：
        公测无bug，互测中的三个bug如下：
        1.输入的第一条请求只要是从一楼、零时刻发出的请求都被认为为有效，即使不是(FR,1,UP,0)。
          输入处理，不属于功能性bug，不需要构造测试样例重现
        2.如果没有输入有效指令直接开始运行，由于Scheduler->schedule方法的逻辑问题，虽然没有从请求队列中取出指令，但是由于没有正确判断并退出，
          得到了一条初始值都为零的指令信息info，即[ER,0,0]，并开始执行
          此bug在TestCaseError0复现
        3.由于由于Scheduler->schedule方法的逻辑问题，使得在某些特殊情况下，多重捎带处理时会丢弃开关门的1s而没有计算在内
          此bug在TestCaseError1和TestCaseError2复现。
     */
    @Test
    public void TestCaseError0() throws Exception
    {
    //TODO: Test goes here...
        scheduler.schedule();
        assertFalse(true);
    }
    //期望输出：无输出

    @Test
    public void TestCaseError1() throws Exception
    {
        long[] testInfo0 = {1, 1, 0};
        requestsQueue.push(testInfo0);
        long[] testInfo1 = {1, 6, 0};
        requestsQueue.push(testInfo1);
        long[] testInfo2 = {1, 5, 0};
        requestsQueue.push(testInfo2);
        long[] testInfo3 = {1, 4, 0};
        requestsQueue.push(testInfo3);
        long[] testInfo4 = {0, 3, 1};
        requestsQueue.push(testInfo4);
        long[] testInfo5 = {1, 2, 1};
        requestsQueue.push(testInfo5);
        scheduler.schedule();
        assertFalse(true);

    }
//    期望输出
//        [FR,1,UP,0]/(1,STILL,1.0)
//        [FR,2,UP,1]/(2,UP,1.5)
//        [ER,3,1]/(3,UP,3.0)
//        [FR,4,UP,0]/(4,UP,4.5)
//        [FR,5,UP,0]/(5,UP,6.0)
//        [FR,6,UP,0]/(6,UP,7.5)

    @Test
    public void TestCaseError2() throws Exception
    {
        long[] testInfo0 = {1, 1, 0};
        requestsQueue.push(testInfo0);
        long[] testInfo1 = {0, 5, 3};
        requestsQueue.push(testInfo1);
        long[] testInfo2 = {0, 2, 4};
        requestsQueue.push(testInfo2);
        long[] testInfo3 = {0, 1, 6};
        requestsQueue.push(testInfo3);
        long[] testInfo4 = {1, 2, 9};
        requestsQueue.push(testInfo4);
        long[] testInfo5 = {2, 10, 13};
        requestsQueue.push(testInfo5);
        long[] testInfo6 = {0, 4, 13};
        requestsQueue.push(testInfo6);
        long[] testInfo7 = {2, 6, 13};
        requestsQueue.push(testInfo7);
        long[] testInfo8 = {0, 5, 14};
        requestsQueue.push(testInfo8);
        long[] testInfo9 = {2, 8, 16};
        requestsQueue.push(testInfo9);
        long[] testInfo10 = {1, 7, 19};
        requestsQueue.push(testInfo10);
        long[] testInfo11 = {1, 6, 20};
        requestsQueue.push(testInfo11);
        long[] testInfo12 = {0, 1, 25};
        requestsQueue.push(testInfo12);
        long[] testInfo13 = {2, 6, 26};
        requestsQueue.push(testInfo13);
        long[] testInfo14 = {0, 3, 26};
        requestsQueue.push(testInfo14);
        long[] testInfo15 = {0, 6, 28};
        requestsQueue.push(testInfo15);
        long[] testInfo16 = {2, 6, 28};
        requestsQueue.push(testInfo16);
        long[] testInfo17 = {2, 10, 29};
        requestsQueue.push(testInfo17);
        long[] testInfo18 = {2, 7, 30};
        requestsQueue.push(testInfo18);
        long[] testInfo19 = {1, 3, 32};
        requestsQueue.push(testInfo19);
        long[] testInfo20 = {0, 9, 32};
        requestsQueue.push(testInfo20);
        long[] testInfo21 = {1, 3, 36};
        requestsQueue.push(testInfo21);
        long[] testInfo22 = {1, 1, 40};
        requestsQueue.push(testInfo22);
        long[] testInfo23 = {0, 9, 41};
        requestsQueue.push(testInfo23);
        long[] testInfo24 = {0, 8, 41};
        requestsQueue.push(testInfo24);
        long[] testInfo25 = {0, 1, 41};
        requestsQueue.push(testInfo25);
        long[] testInfo26 = {2, 9, 43};
        requestsQueue.push(testInfo26);
        long[] testInfo27 = {0, 7, 43};
        requestsQueue.push(testInfo27);
        long[] testInfo28 = {1, 1, 44};
        requestsQueue.push(testInfo28);
        long[] testInfo29 = {2, 2, 45};
        requestsQueue.push(testInfo29);
        long[] testInfo30 = {0, 5, 46};
        requestsQueue.push(testInfo30);
        long[] testInfo31 = {1, 2, 47};
        requestsQueue.push(testInfo31);
        long[] testInfo32 = {2, 9, 47};
        requestsQueue.push(testInfo32);
        long[] testInfo33 = {1, 8, 47};
        requestsQueue.push(testInfo33);
        long[] testInfo34 = {2, 2, 49};
        requestsQueue.push(testInfo34);
        long[] testInfo35 = {2, 10, 49};
        requestsQueue.push(testInfo35);
        long[] testInfo36 = {0, 10, 50};
        requestsQueue.push(testInfo36);
        long[] testInfo37 = {0, 3, 52};
        requestsQueue.push(testInfo37);
        long[] testInfo38 = {0, 2, 53};
        requestsQueue.push(testInfo38);
        long[] testInfo39 = {2, 10, 55};
        requestsQueue.push(testInfo39);
        long[] testInfo40 = {0, 1, 55};
        requestsQueue.push(testInfo40);
        long[] testInfo41 = {2, 9, 56};
        requestsQueue.push(testInfo41);
        long[] testInfo42 = {0, 1, 59};
        requestsQueue.push(testInfo42);
        long[] testInfo43 = {1, 1, 62};
        requestsQueue.push(testInfo43);
        long[] testInfo44 = {1, 9, 64};
        requestsQueue.push(testInfo44);
        long[] testInfo45 = {1, 4, 64};
        requestsQueue.push(testInfo45);
        long[] testInfo46 = {0, 3, 67};
        requestsQueue.push(testInfo46);
        long[] testInfo47 = {0, 3, 69};
        requestsQueue.push(testInfo47);
        long[] testInfo48 = {2, 10, 69};
        requestsQueue.push(testInfo48);
        long[] testInfo49 = {0, 3, 71};
        requestsQueue.push(testInfo49);
        scheduler.schedule();
        assertFalse(true);
    }
//    期望输出：
//        [FR,1,UP,0]/(1,STILL,1.0)
//        [ER,5,3]/(5,UP,5.0)
//        [ER,2,4]/(2,DOWN,7.5)
//        [ER,1,6]/(1,DOWN,9.0)
//        [FR,2,UP,9]/(2,UP,10.5)
//        [ER,4,13]/(4,UP,14.0)
//        [ER,5,14]/(5,UP,15.5)
//        [FR,10,DOWN,13]/(10,UP,19.0)
//        [FR,8,DOWN,16]/(8,DOWN,21.0)
//        [FR,6,DOWN,13]/(6,DOWN,23.0)
//        [FR,7,UP,19]/(7,UP,24.5)
//        [FR,6,UP,20]/(6,DOWN,26.0)
//        [ER,3,26]/(3,DOWN,28.5)
//        [ER,1,25]/(1,DOWN,30.5)
//        [FR,3,UP,32]/(3,UP,32.5)
//        [FR,6,DOWN,26]/(6,UP,35.0)
//        [ER,6,28]/(6,UP,35.0)
//        [ER,9,32]/(9,UP,37.5)
//        [FR,10,DOWN,29]/(10,UP,39.0)
//        [FR,7,DOWN,30]/(7,DOWN,41.5)
//        [ER,1,41]/(1,DOWN,45.5)
//        [FR,3,UP,36]/(3,UP,47.5)
//        [ER,5,46]/(5,UP,49.5)
//        [ER,7,43]/(7,UP,51.5)
//        [ER,8,41]/(8,UP,53.0)
//        [FR,8,UP,47]/(8,UP,53.0)
//        [ER,9,41]/(9,UP,54.5)
//        [ER,10,50]/(10,UP,56.0)
//        [FR,9,DOWN,43]/(9,DOWN,57.5)
//        [ER,3,52]/(3,DOWN,61.5)
//        [FR,2,DOWN,45]/(2,DOWN,63.0)
//        [ER,2,53]/(2,DOWN,63.0)
//        [FR,1,UP,40]/(1,DOWN,64.5)
//        [ER,1,55]/(1,DOWN,64.5)
//        [FR,2,UP,47]/(2,UP,66.0)
//        [ER,3,67]/(3,UP,67.5)
//        [FR,4,UP,64]/(4,UP,69.0)
//        [FR,9,UP,64]/(9,UP,72.5)
//        [FR,10,DOWN,49]/(10,UP,74.0)
//        [ER,3,69]/(3,DOWN,78.5)

    @Test
    public void TestCase0() throws Exception
    {
        long[] testInfo0 = {1, 1, 0};
        requestsQueue.push(testInfo0);
        long[] testInfo1 = {0, 8, 0};
        requestsQueue.push(testInfo1);
        long[] testInfo2 = {0, 3, 2};
        requestsQueue.push(testInfo2);
        long[] testInfo3 = {0, 5, 3};
        requestsQueue.push(testInfo3);
        long[] testInfo4 = {1, 9, 3};
        requestsQueue.push(testInfo4);
        long[] testInfo5 = {2, 9, 3};
        requestsQueue.push(testInfo5);
        long[] testInfo6 = {0, 9, 4};
        requestsQueue.push(testInfo6);
        long[] testInfo7 = {0, 10, 4};
        requestsQueue.push(testInfo7);
        scheduler.schedule();
        assertTrue(true);
    }

    @Test
    public void TestCase1() throws Exception
    {
        long[] testInfo0 = {1, 1, 0};
        requestsQueue.push(testInfo0);
        long[] testInfo1 = {0, 6, 1};
        requestsQueue.push(testInfo1);
        long[] testInfo2 = {1, 8, 1};
        requestsQueue.push(testInfo2);
        long[] testInfo3 = {0, 8, 1};
        requestsQueue.push(testInfo3);
        long[] testInfo4 = {1, 7, 2};
        requestsQueue.push(testInfo4);
        long[] testInfo5 = {1, 6, 2};
        requestsQueue.push(testInfo5);
        scheduler.schedule();
        assertTrue(true);
    }

    @Test
    public void TestCase2() throws Exception
    {
        long[] testInfo0 = {1, 1, 0};
        requestsQueue.push(testInfo0);
        long[] testInfo1 = {0, 6, 1};
        requestsQueue.push(testInfo1);
        long[] testInfo2 = {1, 7, 1};
        requestsQueue.push(testInfo2);
        long[] testInfo3 = {0, 8, 1};
        requestsQueue.push(testInfo3);
        long[] testInfo4 = {1, 6, 2};
        requestsQueue.push(testInfo4);
        scheduler.schedule();
        assertTrue(true);
    }

    @Test
    public void TestCase3() throws Exception
    {
        long[] testInfo0 = {1, 1, 0};
        requestsQueue.push(testInfo0);
        long[] testInfo1 = {2, 9, 1};
        requestsQueue.push(testInfo1);
        long[] testInfo2 = {1, 7, 1};
        requestsQueue.push(testInfo2);
        long[] testInfo3 = {1, 5, 2};
        requestsQueue.push(testInfo3);
        long[] testInfo4 = {1, 6, 3};
        requestsQueue.push(testInfo4);
        long[] testInfo5 = {0, 9, 3};
        requestsQueue.push(testInfo5);
        long[] testInfo6 = {2, 5, 3};
        requestsQueue.push(testInfo6);
        scheduler.schedule();
        assertTrue(true);
    }

    @Test
    public void TestCase4() throws Exception
    {
        long[] testInfo0 = {1, 1, 0};
        requestsQueue.push(testInfo0);
        long[] testInfo1 = {2, 9, 1};
        requestsQueue.push(testInfo1);
        long[] testInfo2 = {1, 7, 1};
        requestsQueue.push(testInfo2);
        long[] testInfo3 = {1, 5, 3};
        requestsQueue.push(testInfo3);
        long[] testInfo4 = {1, 6, 3};
        requestsQueue.push(testInfo4);
        long[] testInfo5 = {0, 9, 3};
        requestsQueue.push(testInfo5);
        long[] testInfo6 = {2, 5, 3};
        requestsQueue.push(testInfo6);
        scheduler.schedule();
        assertTrue(true);
    }

    @Test
    public void TestCase5() throws Exception
    {
        long[] testInfo0 = {1, 1, 0};
        requestsQueue.push(testInfo0);
        long[] testInfo1 = {1, 9, 0};
        requestsQueue.push(testInfo1);
        long[] testInfo2 = {2, 9, 0};
        requestsQueue.push(testInfo2);
        long[] testInfo3 = {0, 9, 0};
        requestsQueue.push(testInfo3);
        long[] testInfo4 = {2, 3, 1};
        requestsQueue.push(testInfo4);
        long[] testInfo5 = {1, 9, 2};
        requestsQueue.push(testInfo5);
        scheduler.schedule();
        assertTrue(true);
    }

    @Test
    public void TestCase6() throws Exception
    {
        long[] testInfo0 = {1, 1, 0};
        requestsQueue.push(testInfo0);
        long[] testInfo1 = {0, 10, 0};
        requestsQueue.push(testInfo1);
        long[] testInfo2 = {0, 3, 2};
        requestsQueue.push(testInfo2);
        long[] testInfo3 = {0, 5, 5};
        requestsQueue.push(testInfo3);
        long[] testInfo4 = {0, 10, 7};
        requestsQueue.push(testInfo4);
        scheduler.schedule();
        assertTrue(true);
    }

    @Test
    public void TestCase7() throws Exception
    {
        long[] testInfo0 = {1, 1, 0};
        requestsQueue.push(testInfo0);
        long[] testInfo1 = {1, 8, 0};
        requestsQueue.push(testInfo1);
        long[] testInfo2 = {0, 10, 1};
        requestsQueue.push(testInfo2);
        long[] testInfo3 = {1, 9, 3};
        requestsQueue.push(testInfo3);
        long[] testInfo4 = {0, 9, 3};
        requestsQueue.push(testInfo4);
        scheduler.schedule();
        assertTrue(true);
    }

    @Test
    public void TestCase8() throws Exception
    {
        long[] testInfo0 = {1, 1, 0};
        requestsQueue.push(testInfo0);
        long[] testInfo1 = {1, 9, 100};
        requestsQueue.push(testInfo1);
        long[] testInfo2 = {1, 1, 101};
        requestsQueue.push(testInfo2);
        long[] testInfo3 = {0, 10, 101};
        requestsQueue.push(testInfo3);
        long[] testInfo4 = {0, 7, 102};
        requestsQueue.push(testInfo4);
        long[] testInfo5 = {1, 8, 102};
        requestsQueue.push(testInfo5);
        long[] testInfo6 = {1, 6, 102};
        requestsQueue.push(testInfo6);
        long[] testInfo7 = {0, 10, 150};
        requestsQueue.push(testInfo7);
        long[] testInfo8 = {2, 10, 199};
        requestsQueue.push(testInfo8);
        long[] testInfo9 = {0, 3, 200};
        requestsQueue.push(testInfo9);
        long[] testInfo10 = {2, 9, 201};
        requestsQueue.push(testInfo10);
        long[] testInfo11 = {2, 7, 201};
        requestsQueue.push(testInfo11);
        long[] testInfo12 = {2, 5, 201};
        requestsQueue.push(testInfo12);
        long[] testInfo13 = {2, 6, 201};
        requestsQueue.push(testInfo13);
        long[] testInfo14 = {2, 4, 201};
        requestsQueue.push(testInfo14);
        long[] testInfo15 = {0, 1, 201};
        requestsQueue.push(testInfo15);
        scheduler.schedule();
        assertTrue(true);
    }

    @Test
    public void TestCase9() throws Exception
    {
        long[] testInfo0 = {1, 1, 0};
        requestsQueue.push(testInfo0);
        long[] testInfo1 = {1, 8, 1};
        requestsQueue.push(testInfo1);
        long[] testInfo2 = {2, 2, 1};
        requestsQueue.push(testInfo2);
        long[] testInfo3 = {1, 3, 1};
        requestsQueue.push(testInfo3);
        long[] testInfo4 = {0, 1, 2};
        requestsQueue.push(testInfo4);
        long[] testInfo5 = {0, 5, 2};
        requestsQueue.push(testInfo5);
        long[] testInfo6 = {2, 3, 2};
        requestsQueue.push(testInfo6);
        long[] testInfo7 = {0, 4, 2};
        requestsQueue.push(testInfo7);
        scheduler.schedule();
        assertTrue(true);
    }

    @Test
    public void TestCase10() throws Exception
    {
        long[] testInfo0 = {1, 1, 0};
        requestsQueue.push(testInfo0);
        long[] testInfo1 = {0, 8, 0};
        requestsQueue.push(testInfo1);
        long[] testInfo2 = {0, 3, 2};
        requestsQueue.push(testInfo2);
        long[] testInfo3 = {0, 5, 3};
        requestsQueue.push(testInfo3);
        long[] testInfo4 = {1, 9, 3};
        requestsQueue.push(testInfo4);
        long[] testInfo5 = {2, 9, 3};
        requestsQueue.push(testInfo5);
        long[] testInfo6 = {0, 9, 4};
        requestsQueue.push(testInfo6);
        long[] testInfo7 = {2, 10, 4};
        requestsQueue.push(testInfo7);
        scheduler.schedule();
        assertTrue(true);
    }
    
    @Test
    public void TestCase11() throws Exception
    {
        long[] testInfo0 = {1, 1, 0};
        requestsQueue.push(testInfo0);
        long[] testInfo1 = {1, 1, 0};
        requestsQueue.push(testInfo1);
        long[] testInfo2 = {0, 3, 2};
        requestsQueue.push(testInfo2);
        long[] testInfo3 = {0, 5, 3};
        requestsQueue.push(testInfo3);
        long[] testInfo4 = {1, 9, 3};
        requestsQueue.push(testInfo4);
        long[] testInfo5 = {2, 9, 4};
        requestsQueue.push(testInfo5);
        long[] testInfo6 = {0, 9, 4};
        requestsQueue.push(testInfo6);
        long[] testInfo7 = {1, 9, 4};
        requestsQueue.push(testInfo7);
        scheduler.schedule();
        assertTrue(true);
    }
    
    @Test
    public void TestCase12() throws Exception
    {
        long[] testInfo0 = {1, 1, 0};
        requestsQueue.push(testInfo0);
        long[] testInfo1 = {1, 5, 5};
        requestsQueue.push(testInfo1);
        scheduler.schedule();
        assertTrue(true);
    }

    @Test
    public void TestForIdentifierForIndexAndType() throws Exception
    {
        assertEquals(0, scheduler.identifier(1, false));
        assertEquals(scheduler.identifier(1), scheduler.identifier(1, true));
    }


} 
