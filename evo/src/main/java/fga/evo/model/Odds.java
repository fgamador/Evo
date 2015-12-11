package fga.evo.model;

public class Odds {
    private static double nextRandom = Math.random();

    public static boolean passed(double odds) {
        boolean passed = nextRandom < odds;
        nextRandom = Math.random();
        return passed;
    }

    /** For testing. */
    static void setNextRandom(double val) {
        assert 0 <= val && val < 1;
        nextRandom = val;
    }
}
