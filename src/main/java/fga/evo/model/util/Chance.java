package fga.evo.model.util;

public class Chance {
    private static double nextRandom = Math.random();

    public static boolean beats(double odds) {
        boolean success = nextRandom < odds;
        nextRandom = Math.random();
        return success;
    }

    /** For testing. */
    public static void setNextRandom(double val) {
        assert 0 <= val && val < 1;
        nextRandom = val;
    }
}
