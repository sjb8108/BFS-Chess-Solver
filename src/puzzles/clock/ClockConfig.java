package puzzles.clock;

import puzzles.common.solver.Configuration;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class ClockConfig implements Configuration{
    private final String hours;
    private String current;
    private final String end;
    /**
     * Makes a clock config with given
     * args list of strings of size three
     * @param args the native array of strings
     */
    public ClockConfig(String[] args){
        this.hours = args[0];
        this.current = args[1];
        this.end = args[2];
    }

    /**
     * gets the starting config
     * @return the start config
     */
    public String getStart() {
        return current;
    }
    /**
     * gets the end config
     * @return the end config
     */
    public String getEnd(){
        return end;
    }
    /**
     * gets the amount of hours the clock has
     * @return the number of hours the clock has
     */
    public String getHours() {
        return hours;
    }
    /**
     * Sees if the current config is the end config
     * @return true if the current config is the end config and false if not
     */
    @Override
    public boolean isSolution() {
        if (getStart().equals(getEnd())){
            return true;
        }
        return true;
    }

    /**
     * Makes and puts neighbor clock configs into a list
     * @return a list of clock configs that would come next/neighbor
     * the current config
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        Set neighbors = new LinkedHashSet<ClockConfig>();
        String[] listneighbor1 = new String[3];
        String[] listneighbor2 = new String[3];
        if (Integer.parseInt(this.current) +1 > Integer.parseInt(hours)){
            listneighbor1[0] = hours;
            listneighbor1[1] = "1";
            listneighbor1[2] = end;
            ClockConfig neighbor = new ClockConfig(listneighbor1);
            neighbors.add(neighbor);
        }
        else {
            listneighbor1[0] = hours;
            listneighbor1[1] = String.valueOf(Integer.parseInt(this.current) + 1);
            listneighbor1[2] = end;
            ClockConfig neighbor = new ClockConfig(listneighbor1);
            neighbors.add(neighbor);
        }

        if (Integer.parseInt(this.current) -1 == 0){
            listneighbor2[0] = hours;
            listneighbor2[1] = hours;
            listneighbor2[2] = end;
            ClockConfig neighbor = new ClockConfig(listneighbor2);
            neighbors.add(neighbor);
        }
        else {
            listneighbor2[0] = hours;
            listneighbor2[1] = String.valueOf(Integer.parseInt(this.current) - 1);
            listneighbor2[2] = end;
            ClockConfig neighbor = new ClockConfig(listneighbor2);
            neighbors.add(neighbor);
        }
        return neighbors;
    }

    /**
     * Sees if two clock configs are the same object
     * @param other the other clock config being compared
     * @return true if they are the same/equal, false if not
     */
    @Override
    public boolean equals(Object other) {
        if(!(other instanceof ClockConfig)) {
            return false;
        }
        else
        {
            ClockConfig otherClock = (ClockConfig) other;
            if (this.current.equals(otherClock.current)){
                return true;
            }
            else{
                return false;
            }
        }
    }
    /**
     * Hashes the clock config
     * @return a number representing the hash code of a clock config
     */
    @Override
    public int hashCode() {
        return hours.hashCode() + this.current.hashCode() + end.hashCode();
    }

    /**
     * Makes a nice looking string representation of the clock config
     * @return a string containing the info about the clock config
     */
    @Override
    public String toString() {
        return current;
    }
}
