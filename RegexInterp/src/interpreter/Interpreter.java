
package interpreter;

import java.util.ArrayList;

public class Interpreter {
    public String set;
    public String string;
    public ArrayList<String> groups;
    /**
     * Tulkin ydin luokka.
     */
    public Interpreter() {
        set = "";
        string = "";
    }
    
    /**
     * Merkkijonoja regex määrittelyyn vertaava metodi.
     * @param str Testattava merkkijono.
     * @return Testin tulos. True jos merkkijono kuuluu regex määrittelyjoukkoon,
     * muuten false.
     */
    public boolean test(String str) {
        string = str;
        groups();
        
        return false;
    }
    
    public void groups() {
        ArrayList<Integer> starts = new ArrayList<Integer>();
        for (int i = 0; i < string.length(); i++) {
            if(string.charAt(i) == '(' && i == 0 || string.charAt(i) != '\\') {
                starts.add(i+1);
            } else if(string.charAt(i) == ')' && starts.size() > 0 && string.charAt(i) != '\\') {
                groups.add(string.substring(starts.get(starts.size()), i));
                starts.remove(starts.size()-1);
            }
        }
    }
}