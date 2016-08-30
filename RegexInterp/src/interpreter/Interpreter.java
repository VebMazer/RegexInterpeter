
package interpreter;

import dataStructures.LinkedDeque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import ui.UI;

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
        return testString(0, string.length(), regexDFADeque.getFirstElement());
    }
    
    /**
     * Testaa hyväksyykö säännöllinen lauseke merkkijonon.
     * @param i Merkkijonon indeksi rekursiossa.
     * @param length Merkkijonon koko.
     * @param state Tila jonka kohdalla ollaan rekursiossa.
     * @return Palauttaa true jos merkkijono läpäisee testin.
     */
    public boolean testString(int i, int length, State state) {
        if(i >= length) return true;
//        if(state.acceptingState) return true;
//        else if(i >= length) return false;
        Set<State> states = state.getTransitions(string.charAt(i));
        if(states != null && !states.isEmpty()) {
            Iterator<State> iterator = states.iterator();
            while(iterator.hasNext()) {
                if(testString(i+1, length, iterator.next())) return true;
            }
        }
        return false;
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
            nfaBuilder.printAllStates();
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