
package interpreter;

import dataStructures.CustomSet;
import dataStructures.LinkedDeque;
import java.util.Iterator;
import java.util.Set;

/**
 * Luokka, joka rakentaa deterministisen äärellisen automaatin(DFA) 
 * epädeterministisestä äärellisestä automaatista(NFA).
 * Luokan tehtäviin, kuuluu myös optimoida valmista DFA:ta.
 */
public class DFABuilder {
    public Interpreter interpreter;
    public LinkedDeque<LinkedDeque<State>> operationStack;
    
    public Set<State> allDFAStates;
    
        public DFABuilder(Interpreter interpreter) {
        this.interpreter = interpreter;
        this.operationStack = interpreter.operationStack;
        
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
    
    /**
     * Tarkistaa vastaako joukko NFA tiloja jotain jo aiemmin luotua DFA tilaa
     * ja lisää tilalle currentState tilanmuutoksen tähän tilaan jos sellainen
     * löytyy.
     * @param currentState Tämän hetkinen tila, jolle tilanmuutos lisätään, jos
     * NFA tilojen joukon todetaan vastaavan jo olemassa olevaa DFA tilaa.
     * @param c Syöte merkki, jolla muunnos toiseen DFA tilaan tapahtuu, jos 
     * sen todetaan olevan jo olemassa.
     * @param NFAStates Joukko NFA tiloja, jotka vastaavat, joko jotain uutta
     * DFA tilaa, tai jo olemassa olevaa DFA tilaa.
     * @return true, jos NFAStates vastaa jo olemassa olevaa DFA tilaa, muuten false.
     */
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
    
    /**
     * Epsilon sulkeuma kerää joukoksi ne tilat, jotka voidaan saavuttaa
     * jostain joukon states tilasta epsilon muunnoksella(Ilman mitään syötettä).
     * Tämä sulkeuma vastaa aina jotain DFA tilaa.
     * @param states NFA tilat, joista epsilon muunnoksia etsitään.
     * @return Joukko tiloja, joka koostuu states muuttujan tiloista ja niistä
     * tiloista, jotka voidaan saavuttaa states muuttujan tiloista epsilon muunnoksilla.
     * 
     */
    public Set<State> epsilonClosure(Set<State> states) {
        Set<State> epsilonTransitStates = new CustomSet<>();
        Iterator<State> iterator = states.iterator();
        
        while(iterator.hasNext()) {
            State state = iterator.next();
            
            epsilonTransitStates.add(state);
            epsilonTransitStates.addAll(reachStates(state, '0'));
        }
        
        return epsilonTransitStates;
    }
    
    /**
     * Rekursiivinen metodi, joka kerää joukon tiloja, jotka voidaan
     * saavuttaa edeten alku tilasta input merkkiä seuraavilla muunnoksilla.
     * Käytetään epsilon sulkeuman kanssa syötteellä '0', joka symboloi epsilon
     * sulkeumaa.
     * @param state Tila, jota käsitellään tällä hetkellä rekursiossa.
     * @param input Syöte merkki, jonka johtamat tilat kerätään joukkoon.
     * @return Joukko tiloja, jotka on saavutettu merkillä input.
     */
    public Set<State> reachStates(State state, char input) {
        Set<State> transitStates = new CustomSet<>();
        Set<State> inputResults  = state.getTransitions(input);
        
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
    
    /**
     * Kerää joukon tiloja, joihin voi edetä joukon states tiloista syöttellä
     * input.
     * @param input Syöte, jota seuraavat tilat kerätään joukkoon.
     * @param states Tilat, input merkkiä seuraavia tiloja etsitään.
     * @return Joukko tiloja, jotka voitiin saavuttaa joukon states tiloista
     * merkillä input.
     */
    public Set<State> move(char input, Set<State> states) {
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
    
    /**
     * Tulostaa tietoa kaikista DFA:n tiloista.
     */
    public void printAllDFAStates() {
        Iterator<State> iterator = allDFAStates.iterator();
        
        while(iterator.hasNext()) {
            State state = iterator.next();
            
            String output = "";
            if(state.acceptingState) output += "_Accepting_";
            output += state.stateID + ":";

            Iterator<Character> chIter = state.transitions.keySet().iterator();
            
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
