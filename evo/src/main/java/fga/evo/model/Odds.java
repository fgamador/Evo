package fga.evo.model;

public class Odds {
    private static double nextRandom = Math.random();

    public static boolean passed(double odds) {
        boolean passed = odds >= 1 || (odds > 0 && odds <= nextRandom);
        nextRandom = Math.random();
        return passed;
    }

    /** For testing. */
    static void setNextRandom(double val) {
        nextRandom = val;
    }
}
