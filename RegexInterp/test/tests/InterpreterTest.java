
package tests;

import dataStructures.LinkedDeque;
import interpreter.Interpreter;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author xenron
 */
public class InterpreterTest {
    Interpreter interpreter;
    
    public InterpreterTest() {
        interpreter = new Interpreter();
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
    
    public void defRegEx1(){
        interpreter.constructRegEx("axd(vg|ne*)+");
    }
    
    @Test
    public void testTraversalRegEx1_1() {
        defRegEx1();
        assertEquals(true, interpreter.testTraversal("axd"));
    }
    
    @Test
    public void testTraversalRegEx1_2() {
        defRegEx1();
        assertEquals(true, interpreter.testTraversal("axdvg"));
    }
    
    @Test
    public void testTraversalRegEx1_3() {
        defRegEx1();
        assertEquals(true, interpreter.testTraversal("axdvgvg"));
    }
    
    @Test
    public void testTraversalRegEx1_4() {
        defRegEx1();
        assertEquals(true, interpreter.testTraversal("axdn"));
    }
    
    @Test
    public void testTraversalRegEx1_5() {
        defRegEx1();
        assertEquals(true, interpreter.testTraversal("axdne"));
    }
    
    @Test
    public void testTraversalRegEx1_6() {
        defRegEx1();
        assertEquals(true, interpreter.testTraversal("axdneee"));
    }
    
    @Test
    public void testTraversalRegEx1_manyTests() {
        defRegEx1();
        boolean fails = false;
        if(!interpreter.testTraversal("axdvgneee")) fails = true;
        if(interpreter.testTraversal("axdvgneeed")) fails = true;
        assertEquals(true, !fails);
    }
    
    public void defRegEx2(){
        interpreter.constructRegEx("ax(ab?)d(vg|ne*)+cc?df+");
    }
    
    @Test
    public void testTraversalRegEx2_manyTests() {
        defRegEx2();
        boolean fails = false;
        if(!interpreter.testTraversal("axadncdf")) fails = true;
        if(!interpreter.testTraversal("axadvgvgneeeenccdffff")) fails = true;
        assertEquals(true, !fails);
    }
    
    @Test
    public void testRegEx2_matchTest1() {
        defRegEx2();
        boolean fails = false;
        LinkedDeque<String> matches = interpreter.findMatchingStrings("ddaxadncdfddaxabdneeeeneeeeccdffffdd");
        if(!matches.contains("axadncdf")) fails = true;
        if(!matches.contains("axabdneeeeneeeeccdffff")) fails = true;
        if(matches.size() != 2) fails = true;
        assertEquals(true, !fails);
    }
    
    public void defRegEx3(){
        interpreter.constructRegEx("ac(de|dn)");
    }
    
    @Test
    public void testTraversalRegEx3_manyTests() {
        defRegEx3();
        boolean fails = false;
        if(!interpreter.testTraversal("acde")) fails = true;
        if(!interpreter.testTraversal("acdn")) fails = true;
        if(interpreter.testTraversal("acdnd")) fails = true;
        assertEquals(true, !fails);
    }
    
    public void defRegEx4(){
        interpreter.constructRegEx("ac?r(xn|di)+");
    }
    
    @Test
    public void testRegEx4_matchTest1() {
        defRegEx4();
        boolean fails = false;
        LinkedDeque<String> matches = interpreter.findMatchingStrings("ddarxneeacrdixndix");
        if(!matches.contains("arxn")) fails = true;
        if(!matches.contains("acrdixndi")) fails = true;
        if(matches.size() != 2) fails = true;
        assertEquals(true, !fails);
    }
    
    public void defRegEx5() {
        interpreter.constructRegEx("ax?(f\\+ne*)|df*sn");
    }
    
    @Test
    public void testRegEx5_matchTest1() {
        defRegEx5();
        boolean fails = false;
        LinkedDeque<String> matches = interpreter.findMatchingStrings("ddaf+naadfffsneeaxf+neee");
        if(!matches.contains("af+n")) fails = true;
        if(!matches.contains("dfffsn")) fails = true;
        if(!matches.contains("axf+neee")) fails = true;
        if(matches.size() != 3) fails = true;
        assertEquals(true, !fails);
    }
}
