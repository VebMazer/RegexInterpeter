
package ui;

import dataStructures.LinkedDeque;
import interpreter.Interpreter;

import java.util.Iterator;
import java.util.Scanner;
import java.io.File;

/**
 * Luokka toimii teksti käyttöliittymänä tulkkia varten.
 */
public class UI {
    public Interpreter interpreter;
    public LinkedDeque<String> strings;
    public LinkedDeque<Boolean> booleans;
    
    public Scanner scanner;
    public String input;
    
    /**
     * Luokan konstruktori.
     */
    public UI() {
        interpreter = new Interpreter();
        strings = new LinkedDeque<>();
        booleans = new LinkedDeque<>();
        scanner = new Scanner(System.in);
        input = "";
    }
    
    /**
     * Metodi joka sisältää käyttöliittymän taustaloopit ja käynnistää
     * käyttöliittymän.
     */
    public void run() {
        printIntro();
        while(!input.equals("1") && !input.equals("0")) {
            printStartingCommandsList();
            askForCommand();
            if(input.equals("1")) defineRegEx();
        }
        while(!input.equals("0")) {
            printCommandsList();
            askForCommand();
            
            if(input.equals("1")) defineRegEx();
            else if(input.equals("2")) findMatchesInString();
            else if(input.equals("3")) findMatchesInFile();
            else if(input.equals("4")) {
                while(!input.equals("9") && !input.equals("0")) {
                    printDebuggingTools();
                    askForCommand();
                    
                    if(input.equals("5")) traverseString();
                    else if(input.equals("6")) interpreter.dfaBuilder.printAllDFAStates();
                    else if(input.equals("7")) printEvalRegex();
                    else if(input.equals("8")) printResults();
                }
            }
        }
    }
    
    /**
     * Tulostetaan ohjelman intron sen käynnistyessä.
     */
    public void printIntro() {
        System.out.println("Welcome to the regular expression interpreter program.");
        System.out.println("* Supported operators are: '|', '*', '+', '?'.");
        System.out.println("* Also supports the use of parenthesis: '(' ')' and backslash '\\'");
    }
    
    /**
     * Tulostaa aloitus loopin toiminto listan.
     */
    public void printStartingCommandsList() {
        System.out.println("");
        System.out.println("Options:");
        System.out.println("1: Define a regular expression.");
        System.out.println("0: Exits the program.");
        System.out.println("");
    }
    
    /**
     * Tulostaa listan käyttöliittymän pääloopin toiminnoista.
     */
    public void printCommandsList() {
        System.out.println("");
        System.out.println("Currently defined RegEx is: " + interpreter.regex);
        System.out.println("");
        System.out.println("Options:");
        System.out.println("1: Define a new regular expression.");
        System.out.println("2: Test a string to find patterns matching the RegEx.");
        System.out.println("3: Test a file to find patterns matching the RegEx.");
        System.out.println("4: Use debugging tools.");
        System.out.println("0: Exits the program.");
        System.out.println("");
    }
    
    /**
     * Tulostaa listan debuggaus loopin toiminnoista.
     */
    public void printDebuggingTools() {
        System.out.println("");
        System.out.println("Currently defined RegEx is: " + interpreter.regex);
        System.out.println("");
        System.out.println("Debugging options:");
        System.out.println("5: Test whether or not a string traverses the regex automata.");
        System.out.println("6: Show current DFA data");
        System.out.println("7: Shows the edited Regex that is used for NFA construction.");
        System.out.println("8: Prints the results of all the traversal tests you have made so far.");
        System.out.println("9: Exits debugging tools");
        System.out.println("0: Exits the program.");
        System.out.println("");
    }
    
    /**
     * Pyytää käyttäjää syöttämään toiminto numeron.
     */
    public void askForCommand() {
        System.out.print("Enter a command number: ");
        input = scanner.next();
        System.out.println("");
    }
    
    /**
     * Metodi vastaanottaa käyttäjältä regex määrittelyjoukkoa vastaavan
     * merkkijonon tulkin käytettäväksi.
     */
    public void defineRegEx() {
        System.out.print("Define the RegEx: ");
        interpreter.nextState = 0;
        interpreter.constructRegEx(scanner.next());
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
        boolean result = interpreter.testTraversal(input);
        booleans.addLast(result);
        System.out.println(result);
    }
    
    /**
     * Tulostaa NFA:n muodostuksessa hyödynnettävän concatenaatiot
     * symboleina sisältävän version alkuperäisestä RegEx lausekkeesta.
     */
    public void printEvalRegex() {
        System.out.println(interpreter.nfaBuilder.evalRegEx);
    }
    
    /**
     * Pyytää käyttäjältä syötteen, josta kerätään RegEx lausekkeen
     * mukaiset merkkijonot ja tulostetaan ne sitten käyttäjälle
     * niiden kokonais lukumäärän kanssa.
     */
    public void findMatchesInString() {
        System.out.print("Enter a string: ");
        String input = scanner.next();
        Iterator<String> resultsIterator = interpreter.findMatchingStrings(input).iterator();
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

    /**
     * Returns a string that contains all the content in the file found
     * with the filepath string.
     */
    public String readFile(String filepath) {
        String result = "";
        try {
            Scanner fileReader = new Scanner(new File(filepath));
            while(fileReader.hasNextLine()) {
                result += fileReader.nextLine();
            }
        } catch (Exception e) {
            System.out.println("File not found");
        }
        return result;
    }

    /**
     * Asks the user to input a filepath, then reads the file and
     * finds all the patterns in it that match the currently defined regex.
     */
    public void findMatchesInFile() {
        System.out.print("Enter filepath: ");
        String input = scanner.next();
        input = readFile(input);
        if (input.equals("")) return;
        
        Iterator<String> resultsIterator = interpreter.findMatchingStrings(input).iterator();
        int matchesFound = 0;
        System.out.println("MatchesFound:");
        while(resultsIterator.hasNext()) {
            matchesFound++;
            System.out.println(resultsIterator.next());
        }
        System.out.println(matchesFound + " matches");
    }
}