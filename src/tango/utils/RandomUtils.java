package tango.utils;

import java.util.Random;
import tango.prefs.PreferenceManager;

public class RandomUtils {

    public static final Random random = new Random();

    static {
        random.setSeed(System.currentTimeMillis());
    }

    public static double getRandomValue() {
        return random.nextDouble();
    }
    
    public static void setSeed(long seed) {
        random.setSeed(seed);
    }
    public static void resetSeed() {
        random.setSeed(System.currentTimeMillis());
    }

    /**
     * return one value of the array randomly
     */
    public static double randomElement(double[] values) {
        int i = (int) (getRandomValue() * values.length);
        //System.out.println("Got "+i);
        return values[i];
    }

    public static double randomAngleInDeg180() {
        // between 0 and 180
        double deg = (int) (Math.random() * 180);
        // between 0 and 2PI
        return deg;
    }

    public static double randomAngleInDeg() {
        // between 0 and 360, but we use 22.5 degrees steps		
        double deg = (int) (getRandomValue() * PreferenceManager.getManager().getNrAngles()) * PreferenceManager.getManager().getAngleDelta();
        return deg;
    }

    private static void p(String string) {
        System.out.println(string);

    }
}
