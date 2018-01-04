package fga.evo.model.util;

public class Util {
    public static double sqr(double value) {
        return value * value;
    }

    // http://stackoverflow.com/questions/2887815/speeding-up-math-calculations-in-java
    // domain -inf..inf (-5..5 really, maybe -10..10), range 0..1
    public static double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }
}
