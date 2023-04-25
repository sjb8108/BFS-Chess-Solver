package puzzles.strings;

import puzzles.common.solver.Solver;
/**
 * The main class for where the String Configs are created
 * and called by the solver.
 */
public class Strings {
    /**
     * Makes the start config and prints it
     * @param args the first string is where the string will start
     *             the last string is wgere the string should end
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(("Usage: java Strings start finish"));
        } else {
            StringsConfig initialConfig = new StringsConfig(args);
            System.out.println("Start: "+initialConfig.getCurrent()+
                    ", End: "+initialConfig.getEnd());
            Solver.solve(initialConfig);
        }
    }
}
