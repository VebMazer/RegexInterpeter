
package ui;

import dataStructures.LinkedDeque;
import interpreter.Interpreter;

import java.util.Iterator;
import java.util.Scanner;
import java.io.File;

/**
 * User Interface class for the interpreter.
 */
public class UI {
    public Interpreter interpreter;
    public LinkedDeque<String> strings;
    public LinkedDeque<Boolean> booleans;
    
    public Scanner scanner;
    public String input;
    
    /**
     * The Constructor initializes the objects internal variables.
     */
    public UI() {
        interpreter = new Interpreter();
        strings = new LinkedDeque<>();
        booleans = new LinkedDeque<>();
        scanner = new Scanner(System.in);
        input = "";
    }
    

    /**
     * Method that starts the UI and includes the main command loops.
     */
    public void run() {
        printIntro();
        
        // Starting command loop
        while(!input.equals("1") && !input.equals("0")) {
            printStartingCommandsList();
            askForCommand();
            if(input.equals("1")) defineRegEx();
        }
        
        // Main command loop
        while(!input.equals("0")) {
            printCommandsList();
            askForCommand();
            
            if      (input.equals("1")) defineRegEx();
            else if (input.equals("2")) findMatchesInString();
            else if (input.equals("3")) findMatchesInFile();
            else if (input.equals("4")) {
                
                // Debugging command loop
                while(!input.equals("9") && !input.equals("0")) {
                    printDebuggingTools();
                    askForCommand();
                    
                    if(input.equals("5")) traverseString();
                    else if(input.equals("6")) interpreter.dfaBuilder.printAllDFAStates();
                    else if(input.equals("7")) printEvalRegex();
                    else if(input.equals("8")) printTraversalTestResults();
                }
            }
        }
    }
    
    /**
     * Prints the UI introduction text.
     */
    public void printIntro() {
        System.out.println("Welcome to the regular expression interpreter program.");
        System.out.println("* Supported operators are: '|', '*', '+', '?'.");
        System.out.println("* Also supports the use of parenthesis: '(' ')' and backslash '\\'.");
    }
    
    /**
     * Prints the commands available in the starting command loop.
     */
    public void printStartingCommandsList() {
        System.out.println("");
        System.out.println("Options:");
        System.out.println("1: Define a regular expression.");
        System.out.println("0: Exit the program.");
        System.out.println("");
    }
    
    /**
     * Prints the commands available in the main command loop.
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
        System.out.println("0: Exit the program.");
        System.out.println("");
    }
    
    /**
     * Prints the commands available in the debugging command loop.
     */
    public void printDebuggingTools() {
        System.out.println("");
        System.out.println("Currently defined RegEx is: " + interpreter.regex);
        System.out.println("");
        System.out.println("Debugging options:");
        System.out.println("5: Test whether or not a string traverses the regex DFA.");
        System.out.println("6: Show current DFA data.");
        System.out.println("7: Show the edited Regex that is used for NFA construction.");
        System.out.println("8: Print the results of all the traversal tests you have made so far.");
        System.out.println("9: Exit debugging tools.");
        System.out.println("0: Exit the program.");
        System.out.println("");
    }
    
    /**
     * Requests the user for an input command number.
     */
    public void askForCommand() {
        System.out.print("Enter a command number: ");
        input = scanner.next();
        System.out.println("");
    }
    
    /**
     * Method requests a string from the user and defines a new
     * RegEx with it.
     */
    public void defineRegEx() {
        System.out.print("Define the RegEx: ");
        interpreter.nextState = 0;
        interpreter.constructRegEx(scanner.next());
        
        // Initialize the lists for the DFA traversal tests.
        strings = new LinkedDeque<>();
        booleans = new LinkedDeque<>();
    }
    
    /**
     * The method requests a string from the user, adds it to a list,
     * tests whether or not it traverses in the RegEx DFA (not whether
     * the state is accepting or not), adds the test result to a list
     * and prints it.
     */
    public void traverseString() {
        System.out.print("Enter a string: ");
        
        // Request input and save it.
        String input = scanner.next();
        strings.addLast(input);
        
        // Apply the traversal test.
        boolean result = interpreter.testTraversal(input);
        
        // Save and print the result.
        booleans.addLast(result);
        System.out.println(result);
    }
    
    /**
     * Prints a version of the current Regex with a symbol added for each
     * concatenation. Adding the concatenation symbols is helpful in
     * NFA construction.
     */
    public void printEvalRegex() {
        System.out.println(interpreter.nfaBuilder.evalRegEx);
    }
    
    /**
     * Asks the user for an input string, collects the substrings that 
     * match the RegEx from it. Then prints all the matches and the
     * total number of matches.
     */
    public void findMatchesInString() {

        // Ask and read input.
        System.out.print("Enter a string: ");
        String input = scanner.next();
        
        // Find matches.
        Iterator<String> resultsIterator = interpreter.findMatchingStrings(input).iterator();
        
        System.out.println("MatchesFound:");
        int matchesFound = 0;
        
        // Count the results and print them.
        while(resultsIterator.hasNext()) {
            matchesFound++;
            System.out.println(resultsIterator.next());
        }
        System.out.println(matchesFound + " matches");
    }
    
    /**
     * Prints a list of the tested strings with their results.
     */
    public void printTraversalTestResults() {
        Iterator<String> stringIterator = strings.iterator();
        Iterator<Boolean> booleanIterator = booleans.iterator();
        while(stringIterator.hasNext()) {
            System.out.println(stringIterator.next() + ":   " + booleanIterator.next());
        }
    }

    /**
     * Returns a string that contains all the content in the file that was
     * found with the filepath string.
     */
    public String readFile(String filepath) {
        String result = "";
        try {
            Scanner fileReader = new Scanner(new File(filepath));
            while(fileReader.hasNextLine()) {
                result += fileReader.nextLine();
            }
        } catch (Exception e) {
            System.out.println("File not found.");
        }
        return result;
    }

    /**
     * Asks the user to input a filepath, then reads the file and
     * finds all the patterns in it that match the currently defined regex.
     */
    public void findMatchesInFile() {

        // Request a filepath from the user and read the file.
        System.out.print("Enter filepath: ");
        String input = scanner.next();
        input = readFile(input);
        
        // Stops the method if the file was not found, or if it was empty.
        if (input.equals("")) return;
        
        // Count the number of matches found and print them.
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