import java.util.Timer;
import java.util.TimerTask;

public class Main {
    // -------------------- STÖCKLI --------------------

    private static final String HOST = "localhost";
    private static final int PORT = 4223;
    private static final String UID_POTI = "DCn";
    private static final String UID_US_DISTANCE = "Egk";

    private static double output=0;
    private static double actual=0;

    public static int distanceUSMinValue = 95;
    public static int potiMinValue = 0;
    public static int potiMaxValue = 100;

    public static void main(String[] args) {

        // -------------------- STÖCKLI --------------------
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

        // --------------------------------------------------------------------------------------------------------------


        double P = 0.4;
        double D = 0.2;
        double I = 0.1;
        double target = 100;
        double actual = 0;
        double output = 0;
        // MyPID myPID = new MyPID(P);
        //MyPID myPID = new MyPID(P, D);
		MyPID myPID = new MyPID(P, I, D);

        System.out.printf("%s", "Target\tActual\tOutput\tError\n");

        for (int i = 0; i < 100; i++){
			System.out.printf("%3.2f\t%3.2f\t%3.2f\t%3.2f\n", target, actual, output, (target-actual));
            output = myPID.calculateOutput(actual, target);
			actual = actual + output;

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

		/*MiniPID miniPID;

		miniPID = new MiniPID(0.5, 0.01, 0.4);
		//miniPID.setOutputLimits(10);
		//miniPID.setMaxIOutput(2);
		//miniPID.setOutputRampRate(3);
		//miniPID.setOutputFilter(.3);
		//miniPID.setSetpointRange(40);

		double target=50;

		double actual=0;
		double output=0;

		miniPID.setSetpoint(0);
		miniPID.setSetpoint(target);

		System.err.printf("Target\tActual\tOutput\tError\n");
		//System.err.printf("Output\tP\tI\tD\n");

		// Position based test code
		for (int i = 0; i < 100; i++){

			//if(i==50)miniPID.setI(.05);

			*//*if (i == 60)
				target = 50;*//*

			//if(i==75)target=(100);
			//if(i>50 && i%4==0)target=target+(Math.random()-.5)*50;

			output = miniPID.getOutput(actual, target);
			actual = actual + output;

			//System.out.println("==========================");
			//System.out.printf("Current: %3.2f , Actual: %3.2f, Error: %3.2f\n",actual, output, (target-actual));
			System.err.printf("%3.2f\t%3.2f\t%3.2f\t%3.2f\n", target, actual, output, (target-actual));

			//if(i>80 && i%5==0)actual+=(Math.random()-.5)*20;
		}*/
    }
}
