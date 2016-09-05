
package dataStructures;

import java.util.Iterator;

/**
 * Kaksisuuntaisena jonona toimiva tietorakenne. Toteutus tapahtuu
 * kaksisuuntaisen linkitetyn listan tavoin.
 * @param <E> Jonon varastoima elementti.
 */
public class LinkedDeque<E> implements Iterable<E>{
    
    /**
     * Listan solmuna toimiva luokka, joka sisältää osoitteet
     * edeltävään ja seuraavaan solmuun, sekä tietoalkion.
     */
    private class Node {
        E element;
        Node left;
        Node right;
    }
    
    private Node first;   //(left)
    private Node last;    //(right)
    private int size;
    
    /**
     * Luokan konstruktori. jonon koko määrätään nollaksi sen luomisen yhteydessä.
     */
    public LinkedDeque() {
        size = 0;
    }
    
    /**
     * Listan alkuun alkion lisäävä metodi.
     * @param element Lisättävä lista-alkio.
     */
    public void addFirst(E element) {
        Node node = new Node();
        node.element = element;
        if(first != null) {
            node.right = first;
            first.left = node;
        } else last = node;
        first = node;
        size++;
    }
    
    /**
     * Listan loppuun alkion lisäävä metodi.
     * @param element Lisättävä lista-alkio.
     */
    public void addLast(E element) {
        Node node = new Node();
        node.element = element;
        if(last != null) {
            node.left = last;
            last.right = node;
            } else first = node;
        last = node;
        size++;
    }
    
    /**
     * Listan alusta elementin ottava metodi, joka samalla poistaa kyseisen 
     * alkion listasta.
     * @return Listan alussa ollut elementti.
     */
    public E pollFirst() {
        if(first == null) throw new NullPointerException();
        E element = first.element;
        first = first.right;
        size--;
        return element;
    }
    
    /**
     * Listan lopusta elementin ottava metodi, joka samalla poistaa kyseisen 
     * alkion listasta.
     * @return Listan lopussa ollut elementti.
     */
    public E pollLast() {
        if(last == null) throw new NullPointerException();
        E element = last.element;
        last = last.left;
        size--;
        return element;
    }
    
    /**
     * Palauttaa listan alussa olevan elementin.
     * @return Listan alussa ollut elementti.
     */
    public E getFirstElement() {
        return first.element;
    }
     /**
      * Palauttaa listan lopussa olevan elementin.
      * @return Listan lopussa ollut elementti.
      */
    public E getLastElement() {
        return last.element;
    }
    
    /**
     * Liittää parametrina saadun jonon tämän jonon
     * oikeaan loppuun.
     * @param deque Jonoon liitettävä jono.
     */
    public void connectDequeToLast(LinkedDeque<E> deque) {
        if(deque != null) {
            this.last.right = deque.first;
            deque.first.left = this.last;
            this.last = deque.last;
            this.size += deque.size();
        }
    }
    
    /**
     * Listan alkioiden määrän palauttava luokka.
     * @return Listan alkioiden määrä.
     */
    public int size() {return size;}
    
    public boolean empty() {
        return size == 0;
    }
    
    /**
     * Testaa sisältääkö jono olion o.
     * @param o olio, jonka läsnäoloa tässä jonossa testataan.
     * @return true jos o löytyy tästä jonosta.
     */
    public boolean contains(Object o) {
        Iterator<E> iterator = this.iterator();
        while(iterator.hasNext()) {
            if(iterator.next().equals(o)) return true;
        }
        return false;
    }
    
    /**
     * Iteraattorin palauttava metodi.
     * @return Iteraattori
     */
    @Override
    public Iterator<E> iterator() {
        return new ElementsIterator();
    }
    
    /**
     * Listan iterointiin vasemmalta oikealle(first-last) tarkoitettu luokka.
     */
    private class ElementsIterator implements Iterator<E> {
        private Node current;
        
        public ElementsIterator() {
            current = first;
        }

        @Override
        public boolean hasNext()  {
            return current != null;
        }
        
        @Override
        public E next() {
            Node node = current;
            current = current.right;
            return node.element;
        }
        
    }
}
