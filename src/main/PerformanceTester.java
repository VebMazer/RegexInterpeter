
package main;

import interpreter.Interpreter;
import dataStructures.*;
import java.util.Random;

public class PerformanceTester {
    
    public Interpreter interpreter;
    
    public PerformanceTester() {
        interpreter = new Interpreter();
    }
    
    public void test() {
        constructRegExTest("xdf?|(sn+d|fg*)df|esd+(gk?|e+f)");
        System.out.println("");
        findMatchesTest("a+", constructRandomTestString(100000000));
        System.out.println("");
        LinkedDequeTests(100000);
        System.out.println("");
        CustomSetTests(10000);
        System.out.println("");
        CustomMapTests(10000);
    }
    
    public String constructRandomTestString(int stringLength) {
        String characters = "abcde fghij klmno pqrst uvw xyz";
        char[] charTable = new char[characters.length()];
        Random random = new Random();
        char[] returnStringCharacters = new char[stringLength];
        for (int i = 0; i < stringLength; i++) {
            returnStringCharacters[i] = charTable[random.nextInt(charTable.length)];
        }
        return new String(returnStringCharacters);
    }
    
    public void constructRegExTest(String regex) {
        long time = System.nanoTime();
        
        interpreter.constructRegEx(regex);
        
        double returnTime = (System.nanoTime() - time)/1000000.0;
        System.out.println("constructRegExTest");
        System.out.println("RegEx '" + regex + "': " + returnTime + "milliseconds");
    }
    
    public void findMatchesTest(String regex, String string) {
        interpreter.constructRegEx(regex);
        long time = System.nanoTime();
        
        interpreter.findMatchingStrings(string);
        
        double returnTime = (System.nanoTime() - time)/1000000.0;
        System.out.println("findMatchesTest for regex: " + regex + " with input length of " + string.length());
        System.out.println("Time: "+ returnTime + "milliseconds");
    }
    
    public void LinkedDequeTests(int amountOfObjects) {
        LinkedDeque<Integer> deque = new LinkedDeque<>();
        
        System.out.println("LinkedDeque tests with " + amountOfObjects + "objects");
        long time = System.nanoTime();
        for (int i = 0; i < amountOfObjects; i++) {
            deque.addLast(i);
        }
        double returnTime = (System.nanoTime() - time)/1000000.0;
        System.out.println("Adding " + amountOfObjects + " objects: " + returnTime + "milliseconds");
        
        time = System.nanoTime();
        deque.pollFirst();
        deque.pollLast();
        returnTime = (System.nanoTime() - time)/1000.0;
        System.out.println("Polling first and last: " + returnTime + "microseconds");
    }
    
    public void CustomSetTests(int amountOfObjects) {
        CustomSet<Integer> set = new CustomSet<>();
        
        System.out.println("CustomSet tests with " + amountOfObjects + "objects");
        
        long time = System.nanoTime();
        for (int i = 0; i < amountOfObjects; i++) {
            set.add(i);
        }
        double returnTime = (System.nanoTime() - time)/1000000000.0;
        System.out.println("Adding " + amountOfObjects + " objects: " + returnTime + "seconds");
        
        time = System.nanoTime();
        set.contains(amountOfObjects/2);
        returnTime = (System.nanoTime() - time)/1000000.0;
        System.out.println("Average contains: " + returnTime + "milliseconds");
    }
    
    public void CustomMapTests(int amountOfObjects) {
        CustomMap<Integer, String> map = new CustomMap<>();
        
        System.out.println("CustomMap tests with " + amountOfObjects + "objects");
        
        long time = System.nanoTime();
        for (int i = 0; i < amountOfObjects; i++) {
            map.put(i, "str");
        }
        double returnTime = (System.nanoTime() - time)/1000000000.0;
        System.out.println("Putting " + amountOfObjects + " object pairs: "+ returnTime + "seconds");
        
        time = System.nanoTime();
        map.get(amountOfObjects/2);
        returnTime = (System.nanoTime() - time)/1000000.0;
        System.out.println("Average get: " + returnTime + "milliseconds");
    }
}
