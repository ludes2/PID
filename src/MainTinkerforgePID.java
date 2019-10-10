import com.tinkerforge.*;

import java.util.Timer;
import java.util.TimerTask;

public class Main {
    private static final String HOST = "localhost";
    private static final int PORT = 4223;
    private static final String UID_POTI = "DCn";
    private static final String UID_US_DISTANCE = "Egk";

    private static double output=0;
    private static double actual=0;

    public static int distanceUSMinValue = 95;
    public static int potiMinValue = 0;
    public static int potiMaxValue = 100;

    public static void main(String args[]) throws Exception {
        MiniPID miniPID = new MiniPID(0.25, 0.01, 0.4);

        IPConnection ipcon = new IPConnection();
        BrickletMotorizedLinearPoti poti = new BrickletMotorizedLinearPoti(UID_POTI, ipcon);
        BrickletDistanceUS usDistance = new BrickletDistanceUS(UID_US_DISTANCE, ipcon);
        ipcon.connect(HOST, PORT);

        Timer timer = new Timer();
        timer.schedule( new TimerTask() {
            public void run() {
                int distance = 0;
                try {
                    distance = usDistance.getDistanceValue();
                } catch (TinkerforgeException e) {
                    e.printStackTrace();
                }
                System.out.println("Distance Value: " + distance);

                try {
                    int target = distance - distanceUSMinValue;
                    target = Math.abs(target - potiMaxValue);

                    if (distance >= (distanceUSMinValue + potiMaxValue)) {
                        target = potiMinValue;
                    } else if (distance <= distanceUSMinValue) {
                        target = potiMaxValue;
                    }

                    output = miniPID.getOutput(actual, target);
                    System.out.println("Output: " + output);
                    actual = actual + output;
                    System.out.println("Actual: " + actual);

                    poti.setMotorPosition((int) actual, BrickletMotorizedLinearPoti.DRIVE_MODE_FAST, false);
                } catch (TinkerforgeException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 100); //alle 100ms

        System.out.println("Press key to exit"); System.in.read();
        ipcon.disconnect();
    }
}
