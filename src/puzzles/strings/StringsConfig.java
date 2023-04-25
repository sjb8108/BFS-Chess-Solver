package puzzles.strings;

import puzzles.common.solver.Configuration;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
/**
 * A single representation of a string that uses
 * methods from the configuration interface
 */
public class StringsConfig implements Configuration {
    private String current;
    private final String end;

    /**
     * Makes a string config with given
     * args list of strings of size two
     * @param args the native array of strings.
     */
    public StringsConfig(String[] args){
        this.current = args[0];
        this.end = args[1];
    }
    /**
     * gets the current config.
     * @return the current config.
     */
    public String getCurrent() {
        return current;
    }
    /**
     * gets the end config.
     * @return the end config.
     */
    public String getEnd() {
        return end;
    }

    /**
     * Sees if the current config is the end config
     * @return true if the current config is the end config and false if not
     */
    @Override
    public boolean isSolution() {
        return this.current.equals(end);
    }
    /**
     * Makes and puts neighbor strings configs into a list
     * @return a list of string configs that would come next/neighbor
     * the current config
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        Set neighbors = new LinkedHashSet<StringsConfig>();
        String[] listneighbor1 = new String[2];
        String[] listneighbor2 = new String[2];
        String[] characters = this.current.split("");
        for (int i = 0; i < end.length(); i++){
            String characterAtIndex = characters[i];
            char charAtIndex = characterAtIndex.charAt(0);
            int asciiValue = charAtIndex;
            if (asciiValue + 1 == 91){
                characters[i] = "A";
                listneighbor1[0] = listToString(characters);
                listneighbor1[1] = end;
                StringsConfig neighbor = new StringsConfig(listneighbor1);
                neighbors.add(neighbor);
                characters[i] = characterAtIndex;
            }
            else {
                char nextChar = (char) (asciiValue+1);
                String nextCharacter = String.valueOf(nextChar);
                characters[i] = nextCharacter;
                listneighbor1[0] = listToString(characters);
                listneighbor1[1] = end;
                StringsConfig neighbor = new StringsConfig(listneighbor1);
                neighbors.add(neighbor);
                characters[i] = characterAtIndex;
            }
            if (asciiValue - 1 == 64){
                characters[i] = "Z";
                listneighbor2[0] = listToString(characters);
                listneighbor2[1] = end;
                StringsConfig neighbor = new StringsConfig(listneighbor2);
                neighbors.add(neighbor);
                characters[i] = characterAtIndex;
            }
            else{
                char nextChar = (char) (asciiValue-1);
                String nextCharacter = String.valueOf(nextChar);
                characters[i] = nextCharacter;
                listneighbor2[0] = listToString(characters);
                listneighbor2[1] = end;
                StringsConfig neighbor = new StringsConfig(listneighbor2);
                neighbors.add(neighbor);
                characters[i] = characterAtIndex;
            }
        }
        return neighbors;
    }

    /**
     * Converts a native array of strings into a string variable
     * @param listString the native array list to be converted
     * @return the string that was converted
     */
    public String listToString(String[] listString){
        String theNewString = "";
        for(int j = 0; j < listString.length; j++) {
            theNewString+=listString[j];
        }
        return theNewString;
    }
    /**
     * Sees if two strings configs are the same object
     * @param other the other string config being compared
     * @return true if they are the same/equal, false if not
     */
    @Override
    public boolean equals(Object other) {
        if(!(other instanceof StringsConfig)) {
            return false;
        }
        else
        {
            StringsConfig otherStrings = (StringsConfig) other;
            if (this.current.equals(otherStrings.current)){
                return true;
            }
            else{
                return false;
            }
        }
    }

    /**
     * Hashes the strings config
     * @return a number representing the hash code of a string config
     */
    @Override
    public int hashCode() {
        return this.current.hashCode() + end.hashCode();
    }
    /**
     * Makes a nice looking string representation of the strings config
     * @return a string containing the info about the strings config
     */
    @Override
    public String toString() {
        return current;
    }
}
