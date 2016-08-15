
package interpreter;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Äärellisen automaatin tilaa kuvaava luokka.
 */
public class State {
    public int stateID;
    Map<Character, Set<State>> transitions;
    Set<State> consStates;
    public boolean acceptingState;
    
    /**
     * Oletus konstruktori.
     */
    public State() {
        stateID = -1;
        acceptingState = false;
    }
    
    /**
     * Konstruktori, kun tunnistenumero on tarjottu.
     * @param ID Tilan tunnistenumero.
     */
    public State(int ID) {
        stateID = ID;
        acceptingState = false;
    }
    
    /**
     * Konstruktori, kun tunnistenumero ja joukko tiloja, joista tämä tila 
     * rakentuu on tarjottu.
     * @param ID
     * @param NFAState 
     */
    public State(int ID, Set<State> NFAState) {
        stateID = ID;
        consStates = NFAState;
        acceptingState = false;
        Iterator<State> iterator = NFAState.iterator();
        while(iterator.hasNext()) {
            if(iterator.next().acceptingState) {
                acceptingState = true;
                break;
            }
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
     * Testaa onko verrattava olio sama, kuin tämä olio.
     * @param ob Verrattava olio.
     * @return Palauttaa true jos tämä ja parametri objekti ovat samoja
     */
    @Override
    public boolean equals(Object ob) {
        if(ob.getClass() != this.getClass()) return false;
        State state = (State) ob;
        if(this.stateID != state.stateID) return false;
        return true;
    }
}
