
package interpreter;

import dataStructures.CustomSet;
import dataStructures.LinkedDeque;
//import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class NFABuilder {
    
    public Interpreter interpreter;
    
    public String regex;
    public LinkedDeque<LinkedDeque<State>> operationStack;
    public LinkedDeque<Character> functionStack;
    
    //Monitoring Group
    public Set<State> allNFAStates;
    
        public NFABuilder(Interpreter interpreter) {
        this.interpreter = interpreter;
        
        this.regex = interpreter.regex;
        this.operationStack = interpreter.operationStack;
        this.functionStack = interpreter.functionStack;
        
        //allNFAStates = new HashSet<>();
        allNFAStates = new CustomSet<>();
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
            if(!functionalInput(cLeft) || cLeft == ')' || cLeft == '*' || cLeft == '+' || cLeft == '?') {
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
    
        public boolean functionalInput(char c) {
        if(c == '¤' || c == '|' || c == '*' || c== '+' || c == '?' || c == '(' || c == ')')  return true;
        return false;
    }
    
    public boolean evaluate() {
        if(!functionStack.empty()) {
            char functionChar = functionStack.pollLast();
//            switch(functionChar) {
//                case '¤': return concat();
//                case '|': return union();
//                case '*': return star();
//                case '+': return plus();
//                case '?': return question();
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
                case '+': if(plus()) {
                    return true;
                } else {
                    System.out.println("PlusFailed!");
                    return false;
                }
                case '?': if(question()) {
                    return true;
                } else {
                    System.out.println("QuestionFailed!");
                    return false;
                }
            }
        } else System.out.println("Stack empty!"); //Test line
        System.out.println("Eval fail!"); //testLine
        return false;
    }
    
    
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
    public boolean star() {
        LinkedDeque<State> A;
        if(operationStack.empty()) return false;
        A = operationStack.pollLast();
        
        State startState = new State(interpreter.nextState++);
        State endState = new State(interpreter.nextState++);
        
        startState.addTransition('0', endState);
        startState.addTransition('0', A.getFirstElement());
        A.getLastElement().addTransition('0', endState);
        A.getLastElement().addTransition('0', A.getFirstElement());
        
        A.addLast(endState);
        A.addFirst(startState);
        
        allNFAStates.add(startState);
        allNFAStates.add(endState);
        
        operationStack.addLast(A);
        
        return true;
    }
    
    public boolean plus() {
        LinkedDeque<State> A;
        if(operationStack.empty()) return false;
        A = operationStack.pollLast();
        
        State startState = new State(interpreter.nextState++);
        State endState = new State(interpreter.nextState++);
        
        startState.addTransition('0', A.getFirstElement());
        A.getLastElement().addTransition('0', endState);
        A.getLastElement().addTransition('0', A.getFirstElement());
        
        A.addLast(endState);
        A.addFirst(startState);
        
        allNFAStates.add(startState);
        allNFAStates.add(endState);
        
        operationStack.addLast(A);
        
        return true;
    }
    
    public boolean question() {
        LinkedDeque<State> A;
        if(operationStack.empty()) return false;
        A = operationStack.pollLast();
        
        State startState = new State(interpreter.nextState++);
        State endState = new State(interpreter.nextState++);
        
        startState.addTransition('0', endState);
        startState.addTransition('0', A.getFirstElement());
        A.getLastElement().addTransition('0', endState);
        
        A.addLast(endState);
        A.addFirst(startState);
        
        allNFAStates.add(startState);
        allNFAStates.add(endState);
        
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
