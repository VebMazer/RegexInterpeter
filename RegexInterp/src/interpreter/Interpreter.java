
package interpreter;

import dataStructures.LinkedDeque;
import java.util.HashSet;
import java.util.Set;

public class Interpreter {
    public String set;
    public LinkedDeque<LinkedDeque<State>> operationStack;
    
    public String string;
    public Set<Character> inputSet;
    
    public int nextState;
    /**
     * Tulkin ydin luokka.
     */
    public Interpreter() {
        set = "";
        string = "";
        operationStack = new LinkedDeque<LinkedDeque<State>>();
        inputSet = new HashSet<Character>();
        nextState = 0;
    }
    
    /**
     * Merkkijonoja regex määrittelyyn vertaava metodi.
     * @param str Testattava merkkijono.
     * @return Testin tulos. True jos merkkijono kuuluu regex määrittelyjoukkoon,
     * muuten false.
     */
    public boolean test(String str) {
        nextState = 0;
        string = str;
        createNFA();
        
        System.out.println("Operation not supported yet.");
        return false;
    }
    
    /**
     * Metodin tarkoitus on luoda epädeterministinen äärellinen automaatti 
     * säännöllisestä lausekkeesta(RegEx).
     */
    public void createNFA() {
        
    }
    
    /**
     * Asettaa operaatio pinoon automaatin tiloista koostuvan jonon, joka 
     * kuvaa siirtymää vastaanotetun merkin perusteella.
     * @param character Merkki, jota seuraa tilan muutos.
     */
    public void push(char character) {
        State s0 = new State(nextState++);
        State s1 = new State(nextState++);
        
        s0.addTransition(character, s1);
        
        LinkedDeque NFADeque = new LinkedDeque();
        NFADeque.addLast(s0);
        NFADeque.addLast(s1);
        
        operationStack.addLast(NFADeque);
        inputSet.add(character);
    }
    
    /**
     * Ottaa operaatio pinosta viimeksi lisätyn jonon.
     * @return Automaatin tiloista koostuva jono.
     */
    public LinkedDeque<State> pop() {
        return operationStack.pollLast();
    }
    
    /**
     * Muodostaa kahden merkin peräkkäin liittämistä kuvaavat tilat aiemmista
     * jonoista ja lisää ne uutena jonona operaatio pinoon.
     * @return Totuusarvo sen mukaan toteutuiko operaatio.
     */
    public boolean concat() {
        LinkedDeque<State> A, B;
        if(operationStack.hasLast()) {
            B = operationStack.pollLast();
        } else return false;
        if(operationStack.hasLast()) {
            A = operationStack.pollLast();
            
            A.getLastElement().addTransition('0', B.getFirstElement());
            A.connectDequeToLast(B);
            
            operationStack.addLast(A);
            return true;
        }
        return false;
    }
    
    /**
     * Muodostaa tähti(*) operaatiota kuvaavat tilat viimeisestä
     * jonosta ja lisää niistä muodostetun jonon operaatio pinoon.
     * @return Totuusarvo sen mukaan toteutuiko operaatio.
     */
    public boolean star() {
        LinkedDeque<State> A;
        if(!operationStack.hasLast()) return false;
        A = operationStack.pollLast();
        
        State startState = new State(nextState++);
        State endState = new State(nextState++);
        
        startState.addTransition('0', endState);
        startState.addTransition('0', A.getFirstElement());
        A.getLastElement().addTransition('0', endState);
        A.getLastElement().addTransition('0', A.getFirstElement());
        
        A.addLast(endState);
        A.addFirst(startState);
        
        operationStack.addLast(A);
        
        return true;
    }
    
    /**
     * Muodostaa liitto(|) operaatiota kuvaavat tilat aiemmista
     * jonoista ja lisää ne uutena jonona operaatio pinoon.
     * @return Totuusarvo sen mukaan toteutuiko operaatio.
     */
    public boolean union() {
        LinkedDeque<State> A, B;
        if(operationStack.hasLast()) {
            B = operationStack.pollLast();
        } else return false;
        if(operationStack.hasLast()) {
            A = operationStack.pollLast();
            
            State startState = new State(nextState++);
            State endState = new State(nextState++);
            startState.addTransition('0', A.getFirstElement());
            startState.addTransition('0', B.getFirstElement());
            A.getLastElement().addTransition('0', endState);
            B.getLastElement().addTransition('0', endState);
            
            B.addLast(endState);
            A.addFirst(startState);
            A.connectDequeToLast(B);
            
            operationStack.addLast(A);
            
            return true;
        }
        return false;
    }
}