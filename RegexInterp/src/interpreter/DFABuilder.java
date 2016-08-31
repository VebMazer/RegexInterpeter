
package interpreter;

import dataStructures.CustomSet;
import dataStructures.LinkedDeque;
//import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class DFABuilder {
    public Interpreter interpreter;
    public LinkedDeque<LinkedDeque<State>> operationStack;
    
    public Set<State> allDFAStates;
    
        public DFABuilder(Interpreter interpreter) {
        this.interpreter = interpreter;
        this.operationStack = interpreter.operationStack;
        
        //allDFAStates = new HashSet<>();
        allDFAStates = new CustomSet<>();
        }
    
    /**
     * Muuntaa epädeterministisen äärellisen automaatin(NFA) deterministiseksi
     * äärelliseksi automaatiksi(DFA).
     * @return Palauttaa true jos operaatio onnistui.
     */
    public boolean NFAtoDFA() {
        LinkedDeque<State> NFADeque = operationStack.pollLast();
        LinkedDeque<State> DFADeque = new LinkedDeque<>();
        //Set<State> startingStates = new HashSet<>();
        Set<State> startingStates = new CustomSet<>();
        startingStates.add(NFADeque.pollFirst());
        State DFAStartState = new State(interpreter.nextState++, epsilonClosure(startingStates));
        allDFAStates.add(DFAStartState);
        DFADeque.addLast(DFAStartState);
        interpreter.regexDFADeque = buildDFADeque(DFAStartState, DFADeque);
        return true;
    }    
    
    /**
     * Rekursiivinen metodi, joka rakentaa deterministisen äärellisen automaatin(DFA)
     * kun sille annetaan tämän automaatin aloitustila.
     * @param state Tila, jonka perään metodi rakentaa automaattia.
     * @param DFADeque Jono, johon sijoitetaan kaikki automaatin tilat.
     * @return Jono, joka sisältää automaatin tilat.
     */
    public LinkedDeque<State> buildDFADeque(State state, LinkedDeque<State> DFADeque) {
        Set<State> states = state.consStates;
        Iterator<Character> iterator = state.getAllTransitInputs().iterator();
        while(iterator.hasNext()) {
            char c = iterator.next();
            Set<State> newStateComponents = epsilonClosure(move(c, states));
            if(!connectsBack(state, c, newStateComponents)) {
                State st = new State(interpreter.nextState++, newStateComponents);
                allDFAStates.add(st);
                state.addTransition(c, st);
                DFADeque.addLast(st);
                buildDFADeque(st, DFADeque);
            }
        }
        return DFADeque;
    }
    
    public boolean connectsBack(State currentState, char c ,Set<State> NFAStates) {
        Iterator<State> dfaStatesIterator = allDFAStates.iterator();
        while(dfaStatesIterator.hasNext()) {
            State dfaState = dfaStatesIterator.next();
            if(dfaState.consStates.equals(NFAStates)) {
                currentState.addTransition(c, dfaState);
                return true;
            }
        }
        return false;
    }
    
    public Set<State> epsilonClosure(Set<State> states) {
        //Set<State> epsilonTransitStates = new HashSet<>();
        Set<State> epsilonTransitStates = new CustomSet<>();
        Iterator<State> iterator = states.iterator();
        while(iterator.hasNext()) {
            State state = iterator.next();
            epsilonTransitStates.add(state);
            epsilonTransitStates.addAll(reachStates(state, '0'));
        }
        return epsilonTransitStates;
    }
    
    public Set<State> reachStates(State state, char input) {
        //Set<State> transitStates = new HashSet<>();
        Set<State> transitStates = new CustomSet<>();
        Set<State> inputResults = state.getTransitions(input);
        if(inputResults != null && !inputResults.isEmpty()) {
            Iterator<State> iterator = inputResults.iterator();
            while(iterator.hasNext()) {
                State st = iterator.next();
                transitStates.add(st);
                transitStates.addAll(reachStates(st, input));
            }
        }
        return transitStates;
    }
    
    public Set<State> move(char input, Set<State> states) {
        //Set<State> transitStates =  new HashSet<>();
        Set<State> transitStates =  new CustomSet<>();
        Iterator<State> iterator1 = states.iterator();
        while(iterator1.hasNext()) {
            Set<State> inputResults = iterator1.next().getTransitions(input);
            if(inputResults != null) {
                Iterator<State> iterator2 = inputResults.iterator();
                while(iterator2.hasNext()) transitStates.add(iterator2.next());
            }
        }
        return transitStates;
    }
    
    /**
     * Optimoi determinististä äärellistä automaattia poistamalla siitä ne tilat,
     * jotka eivät ole hyväksyviä tiloja eivätkä johda mihinkään toiseen tilaan.
     * @param DFADeque Jono, joka koostuu automaatin tiloista.
     * @return Jono, joka koostuu automaatin tiloista.
     */
    public LinkedDeque<State> optimizeDFA(LinkedDeque<State> DFADeque) {
        LinkedDeque<State> statesToBeRemoved = new LinkedDeque<>();
        LinkedDeque<State> newDFADeque = new LinkedDeque<>();
        Iterator<State> iterator = DFADeque.iterator();
        while(iterator.hasNext()) {
            State st = iterator.next();
            if(!st.acceptingState && st.transitions.isEmpty()) {
                statesToBeRemoved.addLast(st);
            } else newDFADeque.addLast(st);
        }
        iterator = DFADeque.iterator();
        while(iterator.hasNext()) {
            State st1 = iterator.next();
            Iterator<State> iterator2 = statesToBeRemoved.iterator();
            while(iterator2.hasNext()) {
                st1.removeTransitionsTo(iterator.next());
            }
        }
        DFADeque = newDFADeque;
        return DFADeque;
    }
    
    public void printAllDFAStates() {
        Iterator<State> iterator = allDFAStates.iterator();
        while(iterator.hasNext()) {
            State state = iterator.next();
            Iterator<Character> chIter = state.transitions.keySet().iterator();
            String output = "";
            if(state.acceptingState) output += "_Accepting_";
            output += state.stateID + ":";
            while(chIter.hasNext()) {
                char c = chIter.next();
                Iterator<State> nextStatesIter = state.transitions.get(c).iterator();
                output += "_" + c + ">";
                while(nextStatesIter.hasNext()) {
                    output += nextStatesIter.next().stateID + ",";
                }
            }
            System.out.println(output);
            System.out.println("");
        }
    }
}
