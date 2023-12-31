package puzzles.common.solver;

import java.util.*;
/**
 * BFS implementation of Conifuration.java interface objects
 */
public class Solver {
    /**
     * Implements a BFS on any type of object that
     * implements the Configuration.java interface
     * @param start the starting configuration of the interface object
     * @return a list of config object that show the order of how it
     * got from the start to end config in the least amount of configs
     */
    public static List<Configuration> solve(Configuration start){
        Map<Configuration, Configuration> predecessor = new HashMap<>();
        Configuration startConfig;
        startConfig = start;
        predecessor.put(startConfig, null);
        Queue<Configuration> toVisit = new LinkedList<>();
        toVisit.offer(startConfig);
        int totalConfigs = 1;
        while (!toVisit.isEmpty() && !toVisit.peek().isSolution()) {
            Configuration current = toVisit.remove();
            for (Configuration neighbors : current.getNeighbors()) {
                totalConfigs+=1;
                if (!(predecessor.containsKey(neighbors))) {
                    predecessor.put(neighbors, current);
                    toVisit.offer(neighbors);
                }
            }
        }
        System.out.println("Total Configs: "+(totalConfigs));
        System.out.println("Unique Configs: "+(predecessor.size()));
        if ( toVisit.isEmpty() ) {
            System.out.println("No solution");
            return null;
        }
        else {
            Configuration finishConfig = toVisit.remove();
            List<Configuration> path = new LinkedList<>();
            path.add( 0, finishConfig );
            Configuration node = predecessor.get( finishConfig );
            while ( node != null ) {
                path.add( 0, node );
                node = predecessor.get( node );
            }
            for (int i = 0; i < path.size(); i++){
                System.out.println("Step "+i+": \n"+path.get(i).toString());
            }
            return path;
        }
    }
}
