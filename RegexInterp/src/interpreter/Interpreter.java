
package interpreter;

import dataStructures.CustomSet;
import dataStructures.LinkedDeque;
import java.util.Iterator;
import java.util.Set;

public class Interpreter {
    public String regex;
    public LinkedDeque<LinkedDeque<State>> operationStack;
    public LinkedDeque<Character> functionStack;
    public LinkedDeque<State> regexDFADeque;
    
    public String string;
    
    public int nextState;
    
    public NFABuilder nfaBuilder;
    public DFABuilder dfaBuilder;
    
    /**
     * Tulkin ydin luokka.
     */
    public Interpreter() {
        regex = "";
        string = "";
        operationStack = new LinkedDeque<>();
        functionStack = new LinkedDeque<>();
        nextState = 0;
    }
    
    /**
     * Merkkijonoja regex määrittelyyn vertaava metodi.
     * @param str Testattava merkkijono.
     * @return Testin tulos. True jos merkkijono kuuluu regex määrittelyjoukkoon,
     * muuten false.
     */
    public boolean testTraversal(String str) {
        string = str;
        if(regex.equals("")) {
            System.out.println("No regex defined");
            return false;
        } 
        return traverseString(0, string.length(), regexDFADeque.getFirstElement());
    }
    
    /**
     * Testaa hyväksyykö säännöllinen lauseke merkkijonon.
     * @param i Merkkijonon indeksi rekursiossa.
     * @param length Merkkijonon koko.
     * @param state Tila jonka kohdalla ollaan rekursiossa.
     * @return Palauttaa true jos merkkijono läpäisee testin.
     */
    public boolean traverseString(int i, int length, State state) {
        if(i >= length) return true;
        Set<State> states = state.getTransitions(string.charAt(i));
        if(states != null && !states.isEmpty()) {
            Iterator<State> iterator = states.iterator();
            while(iterator.hasNext()) {
                if(traverseString(i+1, length, iterator.next())) return true;
            }
        }
        return false;
    }
    
    public LinkedDeque<String> findMatchingStrings(String inputString) {
        LinkedDeque<String> matches = new LinkedDeque<>();
        int index = 0;
        while(index < inputString.length()) {
            String str = buildMatch(index, "", inputString, regexDFADeque.getFirstElement());
            if(!str.equals("")) {
                matches.addLast(str);
                index += str.length();
            } else index++;
        }
        return matches;
    }
    
    public String buildMatch(int index, String consStr, String inputString, State state) {
            if(index < inputString.length()) {
                char c = inputString.charAt(index);
                Set<State> states = state.getTransitions(c);
                if(states != null && states.size() > 0) {
                    //DFA States only ever have one following state for an input character.
                    String build = buildMatch(index+1, consStr+c, inputString, states.iterator().next());
                    if(!build.equals("")) return build;
                    if(state.acceptingState) return consStr;
                    return "";
                }
                if(state.acceptingState) return consStr;
                return "";
            }
            if(state.acceptingState) return consStr;
            return "";
    }
    
    /**
     * Muuntaa säännöllistä lauseketta kuvaavan merkkijonon tietorakenteeksi.
     * @param str Säännöllistä lauseketta kuvaava merkkijono.
     * @return Palauttaa true jos kaikki muuntamis operaatiot onnistuvat.
     */
    public boolean constructRegEx(String str) {
        regex = str;
        nextState = 0;
        operationStack = new LinkedDeque<>();
        functionStack = new LinkedDeque<>();
        nfaBuilder = new NFABuilder(this);
        if(!nfaBuilder.createNFA()) System.out.println("Failed to create NFA");
        else {
            //nfaBuilder.printAllStates();
            dfaBuilder = new DFABuilder(this);
            if(!dfaBuilder.NFAtoDFA()) System.out.println("Failed to transform NFA to DFA");  
            else {
                dfaBuilder.optimizeDFA(regexDFADeque);
                return true;
            }
        }
        return false;
    }

}