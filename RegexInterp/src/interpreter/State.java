
package interpreter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Äärellisen automaatin tilaa kuvaava luokka.
 */
public class State {
    public int stateID;
    public Map<Character, Set<State>> transitions;
    public Set<State> consStates;
    public boolean acceptingState;
    
    /**
     * Oletus konstruktori.
     */
    public State() {
        stateID = -1;
        acceptingState = false;
        consStates = new HashSet<>();
        transitions = new HashMap<>();
    }
    
    /**
     * Konstruktori, kun tunnistenumero on tarjottu.
     * @param ID Tilan tunnistenumero.
     */
    public State(int ID) {
        stateID = ID;
        acceptingState = false;
        consStates = new HashSet<>();
        transitions = new HashMap<>();
    }
    
    /**
     * Konstruktori, kun tunnistenumero ja joukko tiloja, joista tämä tila 
     * rakentuu on tarjottu.
     * @param ID
     * @param NFAStates 
     */
    public State(int ID, Set<State> NFAStates) {
        stateID = ID;
        consStates = NFAStates;
        acceptingState = false;
        transitions = new HashMap<>();
        //combineTransitions();
        Iterator<State> iterator = NFAStates.iterator();
        while(iterator.hasNext()) {
            if(iterator.next().acceptingState) {
                acceptingState = true;
                break;
            }
        }
    }
    
    /**
     * Lisää kaikki consStates muuttujan sisältämien tilojen tilanmuutokset
     * tämän tilan tilanmuutos mappiin.
     */
    public void combineTransitions() {
        Iterator<State> iterState = consStates.iterator();
        while(iterState.hasNext()) {
            State currentState = iterState.next();
            Iterator<Character> iterChar = currentState.transitions.keySet().iterator();
            while(iterChar.hasNext()) {
                char c = iterChar.next();
                addTransitions(c, currentState.getTransitions(c));
            }
            
        }
    }
    
    /**
     * Lisää tilanmuutoksia.
     * @param c Merkki joka kuvautuu lisättäviin tiloihin.
     * @param states Tilat joihin tilanmuunnokset kohdistuvat.
     */
    public void addTransitions(char c, Set<State> states) {
        Iterator<State> stateIterator = states.iterator();
        while(stateIterator.hasNext()) {
            addTransition(c, stateIterator.next());
        }
    }
    
    /**
     * Lisää tilan muutoksen mappiin.
     * @param character Merkki, joka saa tilan aikaan.
     * @param state Tila, joka seuraa merkin ilmentymistä.
     */
    public void addTransition(char character, State state) {
        if(transitions.containsKey(character)) {
            transitions.get(character).add(state);
        } else {
            HashSet<State> states = new HashSet<State>();
            states.add(state);
            transitions.put(character, states);
        }
    }
     /**
      * Poistaa kaikki annettuun tilaan johtavat muunnokset.
      * @param state Tila johon johtavat muunnokset poistetaan.
      */
    public void removeTransitionsTo(State state) {
        Iterator<Character> iterator = transitions.keySet().iterator();
        while(iterator.hasNext()) {
            char ch = iterator.next();
            Set<State> states = transitions.get(ch);
            if(states.contains(state)) {
                states.remove(state);
                if(!states.isEmpty()) transitions.replace(ch, states);
                else transitions.remove(ch);
            }
        }
    }
    
    /**
     * Palauttaa ne joukot, joihin annettu merkki johtaa.
     * @param ch Merkki, jota seuraavat muutos tilat haetaan.
     * @return Joukko merkkiä seuraavia tiloja.
     */
    public Set<State> getTransitions(char ch) {
        return transitions.get(ch);
    }
    
    /**
     * Metodi selvittää johtaako tämä tila umpikujaan.
     * @return Totuusarvo, joka kertoo onko tämä tila umpikuja.
     */
    public boolean deadEnd() {
        if(acceptingState) return false;
        if(transitions.isEmpty()) return true;
        Iterator<Set<State>> iterator1 = transitions.values().iterator();
        while(iterator1.hasNext()) {
            Iterator<State> iterator2 = iterator1.next().iterator();
            while(iterator2.hasNext()) {
                if(!iterator2.next().equals(this)) return false;
            }
        }
        return true;
    }
    
    /**
     * Metodi palauttaa joukon kaikista niistä merkeistä, jotka johtavat tilanmuutoksiin
     * jossakin tämän tilan sisältämässä tilassa.
     * @return Joukko tiloja, jotka johtavat tilanmuutokseen jossakin tämän tilan sisältämässä tilassa.
     */
    public Set<Character> getAllTransitInputs() {
        Set<Character> inputs = new HashSet<Character>();
        if(!consStates.isEmpty()) {
            Iterator<State> iterator1 = consStates.iterator();
            while(iterator1.hasNext()) {
                State state = iterator1.next();
                Set<Character> characters = state.transitions.keySet();
                if(!characters.isEmpty()) {
                    Iterator<Character> iterator2 = characters.iterator();
                    while(iterator2.hasNext()) {
                        inputs.add(iterator2.next());
                    }
                }
            }
        }
        inputs.remove('0');
        return inputs;
    }
    
    
    /**
     * Testaa onko verrattava olio sama, kuin tämä olio.
     * @param ob Verrattava olio.
     * @return Palauttaa true jos tämä ja parametri objekti ovat samoja
     */
    @Override
    public boolean equals(Object ob) {
        if(ob.getClass() != this.getClass()) return false;
        State state = (State) ob;
        if(this.stateID == state.stateID) return true;
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + this.stateID;
        return hash;
    }
    
    @Override
    public String toString() {
        return "(" + stateID + transitions.keySet() + ")";
    }
}
