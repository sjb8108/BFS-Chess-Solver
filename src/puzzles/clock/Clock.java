package puzzles.clock;

import puzzles.common.solver.Solver;
/**
 * The main class for where the Clock Configs are created
 * and called by the solver.
 */
public class Clock {
    /**
     * Makes the start config and prints the config
     * @param args the first number is the amount of hours the clock has
     *             the second number is the starting hour
     *             the last number is the ending hour
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(("Usage: java Clock hours stop end"));
        } else {
            ClockConfig initalConfig = new ClockConfig(args);
            System.out.println("Hours: "+initalConfig.getHours()+", Start: "
                    +initalConfig.getStart()+", End: "+initalConfig.getEnd());
            Solver.solve(initalConfig);
        }
    }
}
