
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
    public Set<Character> inputSet;
    
    public int nextState;
    
    public UI ui;   //variable for testing
    /**
     * Tulkin ydin luokka.
     */
    public Interpreter(UI ui) {
        regex = "";
        string = "";
        operationStack = new LinkedDeque<>();
        functionStack = new LinkedDeque<>();
        //inputSet = new HashSet<>();
        nextState = 0;
        this.ui = ui;
    }
    
    /**
     * Merkkijonoja regex määrittelyyn vertaava metodi.
     * @param str Testattava merkkijono.
     * @return Testin tulos. True jos merkkijono kuuluu regex määrittelyjoukkoon,
     * muuten false.
     */
    public boolean test(String str) {
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
    public boolean constructRegex(String str) {
        regex = str;
        nextState = 0;
        operationStack = new LinkedDeque<>();
        functionStack = new LinkedDeque<>();
        //inputSet = new HashSet<>();
        if(!createNFA()) System.out.println("Failed to create NFA");
        else {
            if(!NFAtoDFA()) System.out.println("Failed to transform NFA to DFA");  
            else {
                //optimizeDFA(regexDFADeque);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Metodin tarkoitus on luoda epädeterministinen äärellinen automaatti 
     * säännöllisestä lausekkeesta(RegEx).
     * @return Palauttaa true jos operaatio onnistui.
     */
    public boolean createNFA() {
        String evalRegex = makeEvalRegex();
        for (int i = 0; i < evalRegex.length(); i++) {
            char c = evalRegex.charAt(i);
            if(!functionalInput(c)) push(c); 
            else if(functionStack.empty()) functionStack.addLast(c);
            else if(c == '(') functionStack.addLast(c);
            else if(c == ')') {
                while( functionStack.getLastElement() != '(') {
                    if(!evaluate()) {
                        System.out.println("Fail at 1st, i:"+i); //DebuggingLine
                        return false;
                    }
                }
                functionStack.pollLast();
            } else {
                while(!functionStack.empty() && priority(c, functionStack.getLastElement())) {
                    if(!evaluate()) {
                        System.out.println("Fail at 2nd, i:"+i); //DebuggingLine
                        return false;
                    }
                }
                functionStack.addLast(c);
            }
        }
        
        while(!functionStack.empty()) {
            if(!evaluate()) return false;
        }
        
        if(operationStack.empty()) return false;
        operationStack.getLastElement().getLastElement().acceptingState = true;
        
        System.out.println("");
        //ui.mapNFA(operationStack.getLastElement().getFirstElement()); //testing line
        
        return true;
    }
    
    /**
     * Muuntaa epädeterministisen äärellisen automaatin(NFA) deterministiseksi
     * äärelliseksi automaatiksi(DFA).
     * @return Palauttaa true jos operaatio onnistui.
     */
    public boolean NFAtoDFA() {
        LinkedDeque<State> NFADeque = operationStack.pollLast();
        LinkedDeque<State> DFADeque = new LinkedDeque<>();
        Set<State> startingStates = new HashSet<>();
        startingStates.add(NFADeque.pollFirst());
        State DFAStartState = new State(nextState++, epsilonClosure(startingStates));
        DFADeque.addLast(DFAStartState);
        regexDFADeque = buildDFADeque(DFAStartState, DFADeque);
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
            if(!newStateComponents.equals(state.consStates)) {
                State st = new State(nextState++, newStateComponents);
                state.addTransition(c, st);
                DFADeque.addLast(st);
                buildDFADeque(st, DFADeque);
            } else state.addTransition(c, state);
        }
        return DFADeque;
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
     * 
     * @param states
     * @return 
     */
    public Set<State> epsilonClosure(Set<State> states) {
        Set<State> epsilonTransitStates = new HashSet<State>();
        Iterator<State> iterator = states.iterator();
        while(iterator.hasNext()) {
            State state = iterator.next();
            epsilonTransitStates.add(state);
            epsilonTransitStates.addAll(reachStates(state, '0'));
        }
        return epsilonTransitStates;
    }
    
    public Set<State> reachStates(State state, char input) {
        Set<State> transitStates = new HashSet<State>();
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
        Set<State> transitStates =  new HashSet<State>();
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
    
    public boolean evaluate() {
        if(!functionStack.empty()) {
            char functionChar = functionStack.pollLast();
//            switch(functionChar) {
//                case '¤': return concat();
//                case '|': return union();
//                case '*': return star();
//            }
            switch(functionChar) {  //Test version
                case '¤': if(concat()) {
                    return true;
                } else {
                    System.out.println("ConcatFailed!");
                    return false;
                }
                case '|': if(union()) {
                    return true;
                } else {
                    System.out.println("UnionFailed!");
                    return false;
                }
                case '*': if(star()) {
                    return true;
                } else {
                    System.out.println("StarFailed!");
                    return false;
                }
            }
        } else System.out.println("Stack empty!"); //Test line
        System.out.println("Eval fail!"); //testLine
        return false;
    }
    
    public boolean functionalInput(char c) {
        if(c == '¤' || c == '|' || c == '*' || c == '(' || c == ')')  return true;
        return false;
    }
    
    public boolean priority(char left, char right) {
        if(left == right) return true;
        else if(left == '*') return false;
        else if(right == '*') return true;
        else if(left == '¤') return false;
        else if(right == '¤') return true;
        else if(left == '|') return false;
        
        return true;
    }
    
    /**
     * Lisää säännöllisen lausekkeen määrittelevään merkkijonoon merkkejä, jotka
     * ohjaavat evaluoinnissa concat() operaation käyttöä.
     * @return Evaluointiin valmis regex merkkijono.
     */
    public String makeEvalRegex() {
        String evalRegex = "";
        for (int i = 0; i < regex.length()-1; i++) {
            char cLeft = regex.charAt(i);
            char cRight = regex.charAt(i+1);
            evalRegex += cLeft;
            if(!functionalInput(cLeft) || cLeft == ')' || cLeft == '*') {
                if(!functionalInput(cRight) || cRight == '(') {
                    char ch = '¤';
                    evalRegex += ch;
                } 
            }
        }
        evalRegex += regex.charAt(regex.length()-1);
        System.out.println(evalRegex);//Testing line!
        return evalRegex;
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
        
        LinkedDeque NFADeque = new LinkedDeque<>();
        NFADeque.addLast(s0);
        NFADeque.addLast(s1);
        
        operationStack.addLast(NFADeque);
        //inputSet.add(character);
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
        if(!operationStack.empty()) {
            B = operationStack.pollLast();
        } else return false;
        if(!operationStack.empty()) {
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
        if(operationStack.empty()) return false;
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
        if(!operationStack.empty()) {
            B = operationStack.pollLast();
        } else return false;
        if(!operationStack.empty()) {
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