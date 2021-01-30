package frc.robot;

public class Utils {

    private static double INPUT_SCALE_FACTOR = 100;
    private static double NEGATIVE_ZERO = -0.0d;

    public static double scaleAxis(double getRawAxis) {

        return Math.signum(getRawAxis) * Math.pow(Math.abs(getRawAxis), 1.5);

    }

    public static double getRadius(double turnProp) {

        if (turnProp == 0) {
            return 1e8;
        }

        return INPUT_SCALE_FACTOR * (1 - Math.abs(turnProp)) / turnProp;

    }

    public static int sign(double value) {
        if(Double.doubleToRawLongBits(value) == Double.doubleToRawLongBits(NEGATIVE_ZERO)) {
            return -1;
        } else if (value == 0) {
            return 1;
        } else if (value > 0) {
            return 1;
        } else {
            return -1;
        }
    }

}
