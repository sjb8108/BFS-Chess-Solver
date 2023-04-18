package puzzles.strings;

import puzzles.common.solver.Solver;

public class Strings {
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
