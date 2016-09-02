
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
    public String input;
    
    /**
     * Luokan konstruktori.
     */
    public UI() {
        interp = new Interpreter();
        strings = new LinkedDeque<>();
        booleans = new LinkedDeque<>();
        scanner = new Scanner(System.in);
        input = "";
    }
    
    /**
     * Metodi joka sisältää käyttöliittymän taustaloopin ja käynnistää
     * käyttöliittymän.
     */
    public void run() {
        printIntro();
        while(!input.equals("defregex") && !input.equals("exit")) {
            printStartingCommandsList();
            askForCommand();
            if(input.equals("defregex")) defineRegEx();
        }
        while(!input.equals("exit")) {
            printCommandsList();
            askForCommand();
            
            if(input.equals("defregex")) defineRegEx();
            else if(input.equals("testString")) printMatchingStrings();
            else if(input.equals("traverseString")) traverseString();
            else if(input.equals("showdfa")) interp.dfaBuilder.printAllDFAStates();
            else if(input.equals("showEvalRegex")) printEvalRegex();
            //else if(input.equals("results")) printResults();
        }
    }
    
    public void printIntro() {
        System.out.println("Welcome to VebMazers regular expression interpreter. ");
        System.out.println("Currently only has functions for concatenation, operators: '|', '*', '+', '?'");
        System.out.println("and also understands how to use parenthesis: '(' ')' and backslash '\\'");
    }
    
    public void printStartingCommandsList() {
        System.out.println("");
        System.out.println("Enter one of the following commands:");
        System.out.println("defregex (Define the regular expression form.)");
        System.out.println("exit (Exits the program.)");
        System.out.println("");
    }
    
    public void printCommandsList() {
        System.out.println("");
        System.out.println("Enter one of the following commands:");
        System.out.println("defregex (Define the regular expression form.)");
        System.out.println("testString (Test string to find patterns that match the RegEx.)");
        System.out.println("traverseString (Test whether or not a string traverses the regex automata.)");
        System.out.println("showdfa (Map current DFA data)");
        System.out.println("showEvalRegex (Shows the edited Regex that is used for evaluation.)");
        //System.out.println("results (Prints the results of all the traversal tests you have made so far.)");
        System.out.println("exit (Exits the program.)");
        System.out.println("");
    }
    
    public void askForCommand() {
        System.out.print("Enter command: ");
        input = scanner.next();
        System.out.println("");
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
    
    public void printEvalRegex() {
        System.out.println(interp.nfaBuilder.evalRegEx);
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