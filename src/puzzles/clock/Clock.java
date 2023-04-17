package puzzles.clock;

import puzzles.common.solver.Solver;

public class Clock {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(("Usage: java Clock hours stop end"));
        } else {
            ClockConfig initalConfig = new ClockConfig(args);
            String[] end = new String[3];
            end[0] = args[0];
            end[1] = args[2];
            end[2] = args[2];
            ClockConfig endConfig = new ClockConfig(end);
            System.out.println("Hours: "+initalConfig.getHours()+", Start: "
                    +initalConfig.getStart()+", End: "+initalConfig.getEnd());
            Solver.solve(initalConfig, endConfig);
        }
    }
}
