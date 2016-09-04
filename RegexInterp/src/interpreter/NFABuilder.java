
package interpreter;

import dataStructures.CustomSet;
import dataStructures.LinkedDeque;
import java.util.Iterator;
import java.util.Set;

public class NFABuilder {
    
    public Interpreter interpreter;
    
    public String regex;
    public String evalRegEx;
    public LinkedDeque<LinkedDeque<State>> operationStack;
    public LinkedDeque<Character> functionStack;
//    public final String alphabet = "abcdefghijklmnopqrstuvwxyzåäö";
//    public final String alphabetEval = "a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z|å|ä|ö";
//    public final String capsAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZÅÄÖ";
//    public final String capsAlphabetEval = "A|B|C|D|E|F|G|H|I|J|K|L|M|N|O|P|Q|R|S|T|U|V|W|X|Y|Z|Å|Ä|Ö";
//    public final String numbers = "0123456789";
//    public final String numbersEval = "0|1|2|3|4|5|6|7|8|9";
    

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
            //-----------------untested section below-------------------
            //else if(c == '[') i += evalUnionGroup(i);
            //else if(c == '.') ;
            //-------------------untested section above-----------------
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
//        String evalRegex1 = "";
//        //Adding things prior to adding concatenations.
//        for (int i = 0; i < regex.length(); i++) {
//            if(regex.charAt(i) == '[' && i-1 >= 0 && regex.charAt(i-1) != '\\') {
//                int groupLength = unionGroupLength(i);
//                evalRegex1 += unionGroupEvalString(i+1, i+groupLength-1);
//                i += groupLength-1;
//            } else if(regex.charAt(i) == '.' && i-1 >= 0 && regex.charAt(i-1) != '\\') {
//                evalRegex1 += alphabetEval + '|' + capsAlphabetEval + '|' + numbersEval;
//            } else evalRegex1 += regex.charAt(i);
//        }
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
        
//    public int unionGroupLength(int index) {
//            int iterations = 0;
//            while(regex.charAt(index++) != ']') iterations++;
//            return iterations+1;
//    }
//    
//    public String unionGroupEvalString(int index, int evalEndIndex) {
//        String groupEvalString = "";
//        if(index < evalEndIndex) {
//            char last = regex.charAt(index);
//            groupEvalString += "(" + last;
//            if(index+1 < evalEndIndex) groupEvalString += "|";
//            char next = '¤';
//            for (int i = index+1; i < evalEndIndex; i++) {
//                char c = regex.charAt(i);
//                if(i-1 >= index) last = regex.charAt(i-1);
//               else last = '¤';
//                if(i+1 < evalEndIndex) last = regex.charAt(i+1);
//              else next = '¤';
//               if(c == '-' && last != '¤' && next != '¤') {
//                   groupEvalString += findOrderString(last, next);
//              } else groupEvalString += '|' + c;
//            }
//            groupEvalString += ")";
//        }
//        return groupEvalString;
//    }
//    
//    public String findOrderString(char a, char z) {
//        String orderedString = stringFromAtoZ(a, z, alphabet, alphabetEval);
//        if(!orderedString.equals("")) return orderedString;
//        orderedString = stringFromAtoZ(a, z, numbers, numbersEval);
//        if(!orderedString.equals("")) return orderedString;
//        orderedString = stringFromAtoZ(a, z, capsAlphabet, capsAlphabetEval);
//        if(!orderedString.equals("")) return orderedString;
//        return "-";
//    }
//    
//    public String stringFromAtoZ(char a, char z, String orderlyString, String evalString) {
//        char[] letters = orderlyString.toCharArray();
//        String returnString = "";
//        int startIndex = 0;
//        int endIndex = 0;
//        boolean startFound = false;
//        for (int i = 0; i < letters.length; i++) {
//            if(letters[i] == a)  {
//                startFound = true;
//                startIndex = i+1;
//            }
//            if(letters[i] == z) {
//                endIndex = i;
//                if(startFound) break;
//            }
//        }
//        if(startFound && startIndex < endIndex) returnString = evalString.substring(startIndex*2, endIndex*2-1);
//        return returnString;
//    }
    
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
    
//    /**
//     * Muodostaa tähti(*) operaatiota kuvaavat tilat viimeisestä
//     * jonosta ja lisää niistä muodostetun jonon operaatio pinoon.
//     * @return Totuusarvo sen mukaan toteutuiko operaatio.
//     */
//    public boolean originalStar() {
//        LinkedDeque<State> A;
//        if(operationStack.empty()) return false;
//        A = operationStack.pollLast();
//        
//        State startState = new State(interpreter.nextState++);
//        State endState = new State(interpreter.nextState++);
//        
//        startState.addTransition('0', endState);
//        startState.addTransition('0', A.getFirstElement());
//        A.getLastElement().addTransition('0', endState);
//        A.getLastElement().addTransition('0', A.getFirstElement());
//        
//        A.addLast(endState);
//        A.addFirst(startState);
//        
//        allNFAStates.add(startState);
//        allNFAStates.add(endState);
//        
//        operationStack.addLast(A);
//        
//        return true;
//    }
//    
//    public boolean originalPlus() {
//        LinkedDeque<State> A;
//        if(operationStack.empty()) return false;
//        A = operationStack.pollLast();
//        
//        State startState = new State(interpreter.nextState++);
//        State endState = new State(interpreter.nextState++);
//        
//        startState.addTransition('0', A.getFirstElement());
//        A.getLastElement().addTransition('0', endState);
//        A.getLastElement().addTransition('0', A.getFirstElement());
//        
//        A.addLast(endState);
//        A.addFirst(startState);
//        
//        allNFAStates.add(startState);
//        allNFAStates.add(endState);
//        
//        operationStack.addLast(A);
//        
//        return true;
//    }
//    
//    public boolean originalQuestion() {
//        LinkedDeque<State> A;
//        if(operationStack.empty()) return false;
//        A = operationStack.pollLast();
//        
//        State startState = new State(interpreter.nextState++);
//        State endState = new State(interpreter.nextState++);
//        
//        startState.addTransition('0', endState);
//        startState.addTransition('0', A.getFirstElement());
//        A.getLastElement().addTransition('0', endState);
//        
//        A.addLast(endState);
//        A.addFirst(startState);
//        
//        allNFAStates.add(startState);
//        allNFAStates.add(endState);
//        
//        operationStack.addLast(A);
//        
//        return true;
//    }
    
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
