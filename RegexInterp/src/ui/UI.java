
package ui;

import interpreter.Interpreter;
import java.util.ArrayList;
import java.util.Scanner;

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
        strings = new ArrayList<String>();
        booleans = new ArrayList<Boolean>();
        scanner = new Scanner(System.in);
    }
    
    /**
     * Metodi joka sisältää käyttöliittymän taustaloopin ja käynnistää
     * käyttöliittymän.
     */
    public void run() {
        System.out.println("Welcome to VebMazers Regular expression interpreter. ");
        String input = "";
        while(true) {
            System.out.println("");
            System.out.println("Enter one of the following commands:");
            System.out.println("defset (Define the regular expression form.)");
            System.out.println("testString (Test whether or not a string is of the defined form.)");
            System.out.println("results (Prints the results of all the tests you have made so far.)");
            System.out.println("exit (Exits the program.)");
            System.out.println("");
            
            System.out.print("Enter command: ");
            input = scanner.next();
            System.out.println("");
            
            if(input.equals("defset")) defineSet();
            else if(input.equals("testString")) testString();
            else if(input.equals("results")) printResults();
            else if(input.equals("exit")) break;
        }
    }
    
    /**
     * Metodi vastaanottaa käyttäjältä regex määrittelyjoukkoa vastaavan
     * merkkijonon tulkin käytettäväksi.
     */
    public void defineSet() {
        System.out.print("Define the set: ");
        interp.set = scanner.next();
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
        boolean result = interp.test(input);
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