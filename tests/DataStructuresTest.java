
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
    CustomSet<Integer> set1;
    CustomSet<Integer> set2;
    CustomMap<Integer, String> map1;
    CustomMap<Integer, String> map2;
    
    public DataStructuresTest() {
        deque = new LinkedDeque<>();
        set1 = new CustomSet<>();
        set2 = new CustomSet<>();
        map1 = new CustomMap<>();
        map2 = new CustomMap<>();
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
        deque = new LinkedDeque<>();
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
    
    public void initializeSet() {
        set1 = new CustomSet<>();
    }
    
    public void setupSet1() {
        initializeSet();
        set1.add(2);
        set1.add(3);
        set1.add(1);
        set1.add(4);
        set1.add(5);
        set1.add(6);
    }
    
    @Test
    public void setTest1() {
        setupSet1();
        assertEquals(6, set1.size());
    }
    
    @Test
    public void setTest2() {
        setupSet1();
        set1.remove(1);
        set1.add(5);
        set1.add(7);
        set1.add(8);
        assertEquals(7, set1.size());
    }
    
    public void setupSet2() {
        set2 = new CustomSet<>();
        set2.add(2);
        set2.add(-1);
        set2.add(-3);
        set2.add(-2);
        set2.add(3);
    }
    
    @Test
    public void setTest3() {
        setupSet1();
        setupSet2();
        set1.addAll(set2);
        assertEquals(9, set1.size());
    }
    
    @Test
    public void setTest4() {
        setupSet1();
        setupSet2();
        set1.removeAll(set2);
        assertEquals(4, set1.size());
    }
    
    @Test
    public void setTest5() {
        setupSet1();
        boolean failed = false;
        if(!set1.contains(2)) failed = true;
        if(set1.contains(8)) failed = true;
        if(!set1.contains(3)) failed = true;
        if(set1.contains(-2)) failed = true;
        assertEquals(true, !failed);
    }
    
    @Test
    public void setIterationTest1() {
        setupSet1();
        boolean failed = false;
        Iterator<Integer> iterator = set1.iterator();
        if(iterator.next() != 2) failed = true;
        if(!iterator.hasNext()) failed = true;
        if(iterator.next() != 3) failed = true;
        if(iterator.next() != 1) failed = true;
        if(iterator.next() != 4) failed = true;
        if(iterator.next() != 5) failed = true;
        if(iterator.next() != 6) failed = true;
        if(iterator.hasNext()) failed = true;
        assertEquals(true, !failed);
    }
    
    public void initializeMap1() {
        map1 = new CustomMap<>();
    }
    
    public void setupMap1() {
        map1.put(1, "abcde");
        map1.put(2, "fghij");
        map1.put(3, "klmno");
        map1.put(4, "pqrst");
        map1.put(-1, "ab");
        map1.put(2, "cd");
        map1.put(-3, "ef");
        map1.put(-4, "gh");
    }
    
    public void setupMap2() {
        map2.put(11, "qwe");
        map2.put(12, "asd");
        map2.put(13, "zxc");
        map2.put(12, "rty");
        map2.put(13, "fgh");
        map2.put(14, "vbn");
        map2.put(4, "uio");
        map2.put(-3, "cc");
        
    }
    
    @Test
    public void mapTest1() {
        setupMap1();
        assertEquals("klmno", map1.get(3));
    }
    
    @Test
    public void mapTest2() {
        setupMap1();
        assertEquals("cd", map1.get(2));
    }
    
    @Test
    public void mapTest3() {
        setupMap1();
        assertEquals(false, "fghij".equals(map1.get(2)));
    }
    
    @Test
    public void mapTest4() {
        setupMap1();
        assertEquals(7, map1.size());
    }
    
    @Test
    public void mapTest5() {
        setupMap1();
        setupMap2();
        map1.putAll(map2);
        boolean failed = false;
        if(map1.size() != 11) failed = true;
        if(!map1.get(4).equals("uio")) failed = true;
        if(!map1.get(-3).equals("cc")) failed = true;
        assertEquals(false, failed);
    }
    
}
