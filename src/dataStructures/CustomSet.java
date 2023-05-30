
package dataStructures;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * Joukkona toimiva tietorakenne, joka toeteutetaan taulukon avulla.
 * @param <E> Kuvaa joukon sisältämää luokkaa.
 */
public class CustomSet<E> implements Set<E>, Iterable<E> {
    private E[] array;
    private int size;
    private int nextIndex;
    
    /**
     * Pääkonstruktori.
     */
    public CustomSet() {
        array = (E[]) new Object[8];
        size = 0;
        nextIndex = 0;
    }
    
    /**
     * Palauttaa muuttujassa ylläpidettävän koon. Aikavaativuus O(1).
     * @return Joukon koko.
     */
    @Override
    public int size() {
        return size;
    }
    
    @Override
    public boolean isEmpty() {
        if(size == 0) return true;
        return false;
    }
    
    /**
     * Lisää E tyypin olion joukkoon, jos vastaavaa oliota ei vielä löydy
     * tästä joukosta.
     * @param e Lisättävä olio.
     * @return true, jos olio lisätään joukkoon ja false, jos vastaava olio 
     * on jo siellä.
     */
    @Override
    public boolean add(E e) {
        for (int i = 0; i < array.length; i++) {
            if(e != null && e.equals(array[i])) return false;
        }
        if(nextIndex == array.length) array = reSizeArray(size*2);
        array[nextIndex] = e;
        nextIndex++;
        size++;
        return true;
    }
    
    /**
     * Yrittää lisätä kaikki kokoelman oliot tähän joukkoon.
     * @param c Kokoelma, jonka alkiot yritetään lisätä.
     * @return true, jos jokin olio lisättiin onnistuneesti tähän joukkoon.
     */
    @Override
    public boolean addAll(Collection<? extends E> c) {
        Iterator<? extends E> iterator = c.iterator();
        boolean changed = false;
        while(iterator.hasNext()) {
            if(this.add(iterator.next())) changed = true;
        }
        return changed;
    }
    
    /**
     * Luo uuden taulukon, joka sisältää tämän joukon sisältämät arvot
     * ja asettaa sen kooksi muuttujan newSize arvon.
     * @param newSize Uuden taulukon koko.
     * @return Luotu taulukko.
     */
    public E[] reSizeArray(int newSize) {
        E[] newArray = (E[]) new Object[newSize];
        nextIndex = 0;
        for (int i = 0; i < array.length; i++) {
            if(array[i] != null) {
                newArray[nextIndex] = array[i];
                nextIndex++;
            }
        }
        return newArray;
    }
    
    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < array.length; i++) {
            if(array[i] != null && array[i].equals(o)) {
                array[i] = null;
                size--;
                return true;
            }
        }
        return false;
    }
    
        @Override
    public boolean removeAll(Collection<?> c) {
        Iterator<?> iterator = c.iterator();
        boolean changed = false;
        while(iterator.hasNext()) {
            if(this.remove(iterator.next())) changed = true;
        }
        return changed;
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < array.length; i++) {
            if(array[i] != null && array[i].equals(o)) return true;
        }
        return false;
    }
    
    @Override
    public boolean containsAll(Collection<?> c) {
        Iterator<?> iterator = c.iterator();
        while(iterator.hasNext()) {
            if(!this.contains(iterator.next())) return false;
        }
        return true;
    }
    
    @Override
    public boolean equals(Object o) {
        if(o == null) return false;
        if(this.getClass() != o.getClass()) return false;
        Set set = (Set) o;
        if(this.size() != set.size()) return false;
        return this.containsAll(set);
    }
    
    @Override
    public Object[] toArray() {
        return reSizeArray(size);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Not supported yet.");
//        E[] newArray = reSizeArray(size);
//        if(newArray.length <= a.length) {
//            for (int i = 0; i < newArray.length; i++) {
//                E e = newArray[i];
//                
//            }
//            return a;
//        }
//        T[] returnArray = (T[]) new Object[size];
//        for (int i = 0; i < size; i++) {
//            returnArray[i] = newArray[i];
//        }
//        return returnArray;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = false;
        for (int i = 0; i < array.length; i++) {
            if(array[i] != null) {
                if(!c.contains(array[i])) {
                    array[i] = null;
                    size--;
                    changed = true;
                }
            }
        }
        return changed;
    }
    
    /**
     * Tyhjentää joukon olioista ja alustaa taulukon array uusiksi.
     */
    @Override
    public void clear() {
        array = (E[]) new Object[8];
        size = 0;
        nextIndex = 0;
    }
    
        @Override
    public Iterator<E> iterator() {
         return new ElementsIterator();
    }
    
    private class ElementsIterator implements Iterator<E> {
        private int next;
        
        public ElementsIterator() {
            next = -1;
            findNext();
        }

        @Override
        public boolean hasNext()  {
            if(next == nextIndex) return false;
            return array[next] != null;
        }
        
        @Override
        public E next() {
            int current = next;
            findNext();
            return array[current];
        }
        
        /**
         * Etsii seuraavan indeksin, joka ei ole tyhjä.
         */
        public void findNext() {
            next++;
            for (int i = next; i < nextIndex; i++) {
                if(array[i] != null) {
                    next = i;
                    break;
                }
            }
        }
    }
}
