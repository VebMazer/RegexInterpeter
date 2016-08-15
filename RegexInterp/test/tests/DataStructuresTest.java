
package tests;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import dataStructures.*;
import java.util.Iterator;

public class DataStructuresTest {
    LinkedDeque<Integer> deque;
    
    public DataStructuresTest() {
        deque = new LinkedDeque<Integer>();
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    /**
     * Alustaa jonon.
     */
    public void initializeDeque() {
        deque = new LinkedDeque<Integer>();
    }
    
    /**
     * Testataan onko jonon koko oikea sen luomisen jälkeen.
     */
    @Test
    public void LDbeginTest1() {
        initializeDeque();
        assertEquals(deque.size(), 0);
    }
    
    /**
     * Testataa onko jonon koko oikea alkion lisäämisen jälkeen.
     */
    @Test
    public void LDAddingTest1() {
        initializeDeque();
        deque.addFirst(5);
        assertEquals(deque.size(), 1);
    }
     
    /**
     * Testataa toimiiko alkion ottaminen jonosta oikein.
     */
    @Test
    public void LDAddingTest2() {
        initializeDeque();
        deque.addFirst(5);
        assertEquals(deque.pollFirst() == 5, true);
    }
    
    /**
     * Testataa alkion ottamista toiselta puolelta.
     */
    @Test
    public void LDAddingTest3() {
        initializeDeque();
        deque.addFirst(5);
        assertEquals(deque.pollLast() == 5, true);
    }
    
    /**
     * Alustaa jonon ja asettaa sinne alkioita alku ja loppupään kautta.
     */
    public void dequeSetup() {
        initializeDeque();
        deque.addFirst(2);
        deque.addFirst(1);
        deque.addLast(3);
        deque.addLast(4);
        //results in ---> 1 2 3 4
    }
    
    /**
     * Testaa että alkiot tulevat ulos jonon edestä oikeassa järjestyksessä ja 
     * katsoo, että jonon koko on alussa ja alkioiden poiston jälkeen oikea.
     */
    @Test
    public void LDAdvTest1() {
        dequeSetup();
        boolean fail = false;
        if(deque.size() != 4) fail = true;
        if(deque.pollFirst() != 1) fail = true;
        if(deque.pollFirst() != 2) fail = true;
        if(deque.pollFirst() != 3) fail = true;
        if(deque.pollFirst() != 4) fail = true;
        if(deque.size() != 0) fail = true;
        assertEquals(fail, false);
    }
    
    /**
     * Testaa että alkiot tulevat ulos jonon takaa oikeassa järjestyksessä ja, 
     * että jonon koko on oikea tämän jälkeen.
     */
    @Test
    public void LDAdvTest2() {
        dequeSetup();
        boolean fail = false;
        if(deque.pollLast() != 4) fail = true;
        if(deque.pollLast() != 3) fail = true;
        if(deque.pollLast() != 2) fail = true;
        if(deque.pollLast() != 1) fail = true;
        if(deque.size() != 0) fail = true;
        assertEquals(fail, false);
    }
    
    /**
     * Testaa alkioiden poistoa oikeassa järjestyksessä sekä edestä, että takaa
     * ja tarkistaa jonon koon tämän jälkeen.
     */
    @Test
    public void LDAdvTest3() {
        dequeSetup();
        boolean fail = false;
        if(deque.pollFirst() != 1) fail = true;
        if(deque.pollFirst() != 2) fail = true;
        if(deque.pollLast() != 4) fail = true;
        if(deque.pollFirst() != 3) fail = true;
        if(deque.size() != 0) fail = true;
        assertEquals(fail, false);
    }
    
    /**
     * Testaa luokan vasemmalta oikealle iteroinnin toimivuutta.
     */
    @Test
    public void LDIterationTest1() {
        dequeSetup();
        boolean fail = false;
        int tester = 1;
        Iterator<Integer> iterator  = deque.iterator();
        while(iterator.hasNext()) {
            if(tester != iterator.next()) fail = true;
            tester++;
        }
        assertEquals(fail, false);
    }
}
