
package ui;

import interpreter.Interpreter;
import interpreter.State;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * Luokka toimii teksti käyttöliittymänä tulkkia varten.
 */
public class UI {
    public Interpreter interp;
    public ArrayList<String> strings;
    public ArrayList<Boolean> booleans;
    public Scanner scanner;
    
    /**
     * Luokan konstruktori.
     */
    public UI() {
        interp = new Interpreter();
        strings = new ArrayList<>();
        booleans = new ArrayList<>();
        scanner = new Scanner(System.in);
    }
    
    /**
     * Metodi joka sisältää käyttöliittymän taustaloopin ja käynnistää
     * käyttöliittymän.
     */
    public void run() {
        System.out.println("Welcome to VebMazers Regular expression interpreter. ");
        System.out.println("Currently only has functions for concatenation, operators: '|', '*'");
        System.out.println("and also understands how to use parenthesis: '(' ')'");
        String input = "";
        while(true) {
            System.out.println("");
            System.out.println("Enter one of the following commands:");
            System.out.println("defregex (Define the regular expression form.)");
            //System.out.println("shownfa (Map current NFA data)");
            System.out.println("showdfa (Map current DFA data)");
            System.out.println("testString (Test whether or not a string traverses the regex automata.)");
            System.out.println("results (Prints the results of all the tests you have made so far.)");
            System.out.println("exit (Exits the program.)");
            System.out.println("");
            
            System.out.print("Enter command: ");
            input = scanner.next();
            System.out.println("");
            
            if(input.equals("defregex")) defineRegEx();
            //else if(input.equals("shownfa")) mapNFA();
            else if(input.equals("showdfa")) interp.dfaBuilder.printAllDFAStates();
            else if(input.equals("testString")) testString();
            else if(input.equals("results")) printResults();
            else if(input.equals("exit")) break;
        }
    }
    
    /**
     * Metodi vastaanottaa käyttäjältä regex määrittelyjoukkoa vastaavan
     * merkkijonon tulkin käytettäväksi.
     */
    public void defineRegEx() {
        System.out.print("Define the RegEx: ");
        interp.nextState = 0;
        interp.constructRegEx(scanner.next());
        strings = new ArrayList<>();
        booleans = new ArrayList<>();
    }
    
    /**
     * Metodi vastaanottaa merkkijonon käyttäjältä, lisää sen listaan,
     * testaa vastaako se määrittelyehtoja, lisää testin tuloksen listaan
     * ja tulostaa sen.
     */
    public void testString() {
        System.out.print("Enter a string: ");
        String input = scanner.next();
        strings.add(input);
        boolean result = interp.testTraversal(input);
        booleans.add(result);
        System.out.println(result);
    }
    
    /**
     * Tulostaa listan testatuista merkkijonoista tuloksineen.
     */
    public void printResults() {
        for (int i = 0; i < strings.size(); i++) {
            System.out.println(strings.get(i) + ":   " + booleans.get(i));
        }
    }
    
}