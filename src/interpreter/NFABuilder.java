
package interpreter;

import dataStructures.CustomSet;
import dataStructures.LinkedDeque;
import java.util.Iterator;
import java.util.Set;

/**
 * Luokka, joka rakentaa Epädeterministisen äärellisen automaatin(NFA) 
 * RegEx lausekkeesta.
 */
public class NFABuilder {
    
    public Interpreter interpreter;
    
    public String regex;
    public String evalRegEx;
    public LinkedDeque<LinkedDeque<State>> operationStack;
    public LinkedDeque<Character> functionStack;

    public Set<State> allNFAStates;
    
        public NFABuilder(Interpreter interpreter) {
        this.interpreter = interpreter;
        
        this.regex = interpreter.regex;
        this.operationStack = interpreter.operationStack;
        this.functionStack = interpreter.functionStack;
        
        allNFAStates = new CustomSet<>();
    }
    
     /**
     * Metodin tarkoitus on luoda epädeterministinen äärellinen automaatti 
     * säännöllisestä lausekkeesta(RegEx).
     * @return Palauttaa true jos operaatio onnistui.
     */
    public boolean createNFA() {
        evalRegEx = makeEvalRegex();
        boolean wasBackSlash = false;
        for (int i = 0; i < evalRegEx.length(); i++) {
            char c = evalRegEx.charAt(i);
            if(c == '\\' && !wasBackSlash) wasBackSlash = true;
            else if(wasBackSlash) {
                if(c != '¤') {
                    push(c);
                    wasBackSlash = false;
                }
            }
            else if(!functionalInput(c)) push(c); 
            else if(functionStack.empty()) functionStack.addLast(c);
            else if(c == '(') functionStack.addLast(c);
            else if(c == ')') {
                while( functionStack.getLastElement() != '(') {
                    if(!evaluate()) {
                        return false;
                    }
                }
                functionStack.pollLast();
            } else {
                while(!functionStack.empty() && priority(c, functionStack.getLastElement())) {
                    if(!evaluate()) {
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
        
        return true;
    }
    
    /**
     * Lisää säännöllisen lausekkeen määrittelevään merkkijonoon merkkejä, jotka
     * ohjaavat evaluoinnissa concat() operaation käyttöä.
     * @return Evaluointiin valmis regex merkkijono.
     */
    public String makeEvalRegex() {
        String evalRegex1 = regex;
        String evalRegex2 = "";
        
        //Adding concatenations
        for (int i = 0; i < evalRegex1.length()-1; i++) {
            char cLeft = evalRegex1.charAt(i);
            char cRight = evalRegex1.charAt(i+1);
            evalRegex2 += cLeft;
            if(!functionalInput(cLeft) || cLeft == ')' || cLeft == '*' || cLeft == '+' || cLeft == '?') {
                if(!functionalInput(cRight) || cRight == '(') {
                    char ch = '¤';
                    evalRegex2 += ch;
                } 
            }
        }
        evalRegex2 += evalRegex1.charAt(evalRegex1.length()-1);
        return evalRegex2;
    }
    
    /**
     * Tarkistaa onko syöte merkki, johonkin toimintoon johtava symboli, vai ei.
     * @param c Syöte merkki.
     * @return true jos merkki johtaa johonkin funktioon.
     */
    public boolean functionalInput(char c) {
        if(c == '¤' || c == '|' || c == '*' || c== '+' || c == '?' || c == '(' || c == ')')  return true;
        return false;
    }
    
    /**
     * Kutsuu tilojen käsittely operaatioita sen mukaan, mikä merkki on pinossa.
     * @return true operaation onnistuessa.
     */
    public boolean evaluate() {
        if(!functionStack.empty()) {
            char functionChar = functionStack.pollLast();
            switch(functionChar) {
                case '¤': return concat();
                case '|': return union();
                case '*': return reducedStar();
                case '+': return reducedPlus();
                case '?': return reducedQuestion();
            }
        }
        return false;
    }
    
    /**
     * Avustaa RegEx symbolien evaluoimisessa oikeaan aikaan.
     * @param left Merkki vasemmalla.
     * @param right Merkki oikealla.
     * @return Yleensä true jos RegEx symboli on oikealla.
     */
    public boolean priority(char left, char right) {
        if(left == right) return true;
        else if(left == '*') return false;
        else if(right == '*') return true;
        else if(left == '+') return false;
        else if(right == '+') return true;
        else if(left == '?') return false;
        else if(right == '?') return true;
        else if(left == '¤') return false;
        else if(right == '¤') return true;
        else if(left == '|') return false;
        
        return true;
    }
    
    /**
     * Asettaa operaatio pinoon automaatin tiloista koostuvan jonon, joka 
     * kuvaa siirtymää vastaanotetun merkin perusteella.
     * @param character Merkki, jota seuraa tilan muutos.
     */
    public void push(char character) {
        State s0 = new State(interpreter.nextState++);
        State s1 = new State(interpreter.nextState++);
        
        s0.addTransition(character, s1);
        
        LinkedDeque NFADeque = new LinkedDeque<>();
        NFADeque.addLast(s0);
        NFADeque.addLast(s1);
        
        operationStack.addLast(NFADeque);
        
        allNFAStates.add(s0);
        allNFAStates.add(s1);
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
    public boolean reducedStar() {
        LinkedDeque<State> A;
        if(operationStack.empty()) return false;
        A = operationStack.pollLast();
        
        State endState = new State(interpreter.nextState++);
        
        A.getFirstElement().addTransition('0', endState);
        A.getLastElement().addTransition('0', A.getFirstElement());
        A.getLastElement().addTransition('0', endState);
        
        A.addLast(endState);
        
        allNFAStates.add(endState);
        
        operationStack.addLast(A);
        
        return true;
    }
    
    /**
     * Muodostaa plus(+) operaatiota kuvaavat tilat viimeisestä
     * jonosta ja lisää niistä muodostetun jonon operaatio pinoon.
     * @return Totuusarvo sen mukaan toteutuiko operaatio.
     */
    public boolean reducedPlus() {
        LinkedDeque<State> A;
        if(operationStack.empty()) return false;
        A = operationStack.pollLast();
        
        A.getLastElement().addTransition('0', A.getFirstElement());
        
        operationStack.addLast(A);
        
        return true;
    }
    
    /**
     * Muodostaa question(?) operaatiota kuvaavat tilat viimeisestä
     * jonosta ja lisää niistä muodostetun jonon operaatio pinoon.
     * @return Totuusarvo sen mukaan toteutuiko operaatio.
     */
    public boolean reducedQuestion() {
        LinkedDeque<State> A;
        if(operationStack.empty()) return false;
        A = operationStack.pollLast();
        
        A.getFirstElement().addTransition('0', A.getLastElement());
        
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
            
            State startState = new State(interpreter.nextState++);
            State endState = new State(interpreter.nextState++);
            startState.addTransition('0', A.getFirstElement());
            startState.addTransition('0', B.getFirstElement());
            A.getLastElement().addTransition('0', endState);
            B.getLastElement().addTransition('0', endState);
            
            B.addLast(endState);
            A.addFirst(startState);
            A.connectDequeToLast(B);
            
            allNFAStates.add(startState);
            allNFAStates.add(endState);
            
            operationStack.addLast(A);
            
            return true;
        }
        return false;
    }
    
    /**
     * Tulostaa listan tiloista NFA:ssa.
     */
    public void printAllStates() {
        Iterator<State> iterator = allNFAStates.iterator();
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
