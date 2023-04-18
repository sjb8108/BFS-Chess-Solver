package puzzles.clock;

import puzzles.common.solver.Solver;

public class Clock {
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
