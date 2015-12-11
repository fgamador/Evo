package fga.evo.model;

public class Chance {
    private static double nextRandom = Math.random();

    public static boolean success(double odds) {
        boolean success = nextRandom < odds;
        nextRandom = Math.random();
        return success;
    }

    /** For testing. */
    static void setNextRandom(double val) {
        assert 0 <= val && val < 1;
        nextRandom = val;
    }
}
