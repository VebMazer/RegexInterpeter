
package ui;

import dataStructures.LinkedDeque;
import interpreter.Interpreter;
import interpreter.State;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * Luokka toimii teksti käyttöliittymänä tulkkia varten.
 */
public class UI {
    public Interpreter interp;
    public LinkedDeque<String> strings;
    public LinkedDeque<Boolean> booleans;
    public Scanner scanner;
    
    /**
     * Luokan konstruktori.
     */
    public UI() {
        interp = new Interpreter();
        strings = new LinkedDeque<>();
        booleans = new LinkedDeque<>();
        scanner = new Scanner(System.in);
    }
    
    /**
     * Metodi joka sisältää käyttöliittymän taustaloopin ja käynnistää
     * käyttöliittymän.
     */
    public void run() {
        System.out.println("Welcome to VebMazers Regular expression interpreter. ");
        System.out.println("Currently only has functions for concatenation, operators: '|', '*', '+', '?'");
        System.out.println("and also understands how to use parenthesis: '(' ')'");
        String input = "";
        while(true) {
            System.out.println("");
            System.out.println("Enter one of the following commands:");
            System.out.println("defregex (Define the regular expression form.)");
            System.out.println("showdfa (Map current DFA data)");
            System.out.println("traverseString (Test whether or not a string traverses the regex automata.)");
            System.out.println("testString (Test string to find patterns that match the RegEx.)");
            System.out.println("results (Prints the results of all the tests you have made so far.)");
            System.out.println("exit (Exits the program.)");
            System.out.println("");
            
            System.out.print("Enter command: ");
            input = scanner.next();
            System.out.println("");
            
            if(input.equals("defregex")) defineRegEx();
            else if(input.equals("showdfa")) interp.dfaBuilder.printAllDFAStates();
            else if(input.equals("traverseString")) traverseString();
            else if(input.equals("testString")) printMatchingStrings();
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
        strings = new LinkedDeque<>();
        booleans = new LinkedDeque<>();
    }
    
    /**
     * Metodi vastaanottaa merkkijonon käyttäjältä, lisää sen listaan,
     * testaa vastaako se määrittelyehtoja, lisää testin tuloksen listaan
     * ja tulostaa sen.
     */
    public void traverseString() {
        System.out.print("Enter a string: ");
        String input = scanner.next();
        strings.addLast(input);
        boolean result = interp.testTraversal(input);
        booleans.addLast(result);
        System.out.println(result);
    }
    
    public void printMatchingStrings() {
        System.out.print("Enter a string: ");
        String input = scanner.next();
        Iterator<String> resultsIterator = interp.findMatchingStrings(input).iterator();
        int matchesFound = 0;
        System.out.println("MatchesFound:");
        while(resultsIterator.hasNext()) {
            matchesFound++;
            System.out.println(resultsIterator.next());
        }
        System.out.println(matchesFound + " matches");
    }
    
    /**
     * Tulostaa listan testatuista merkkijonoista tuloksineen.
     */
    public void printResults() {
        Iterator<String> stringIterator = strings.iterator();
        Iterator<Boolean> booleanIterator = booleans.iterator();
        while(stringIterator.hasNext()) {
            System.out.println(stringIterator.next() + ":   " + booleanIterator.next());
        }
    }
    
}