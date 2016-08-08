
package main;

import ui.UI;

/**
 * Ohjelman käynnistävän päämetodin sisältävä luokka.
 */
public class Main {
    
    /**
     * Ohjelman päämetodi, joka käynnistää ohjelman käyttöliittymän.
     * @param args Taulukko, joka koostuu ohjelman vastaanottamista parametreista.
     */
    public static void main(String[] args) {
        UI ui = new UI();
        ui.run();
    }
    
}

