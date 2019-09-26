public class MyPID {

    private double P = 0;
    private double I = 0;
    private double D = 0;

    private double error = 0;

    private double targetPoint = 0; // setpoint

    private boolean reversed=false;

    /**
     * P-only Constructor
     * @param p
     */
    public MyPID(double p){
        this.P = p;
    }

    /**
     * Create a MyPID object
     * @param p Proportional gain.
     * @param i Integral gain.
     * @param d Derivative gain
     */
    public MyPID(double p, double i, double d){
        this.P = p;
        this.I = i;
        this.D = d;
        checkSigns();
    }

    public void setTargetPoint(double targetPoint){
        this.targetPoint = targetPoint;
    }

    public double calculateOutput(double actual, double target){
        double output;
        double error;
        double Poutput;

        // calculate error - delta von target and actual
        error = target - actual;

        // calculate P - P*error
        Poutput = this.P * error;

        output = Poutput;
        return output;

    }

    /**
     * To operate correctly, all PID parameters require the same sign
     * This should align with the {@literal}reversed value
     */
    private void checkSigns(){
        if (this.reversed) {

            // all values should be below zero
            if(this.P>0) this.P*=-1;
            if(this.I>0) this.I*=-1;
            if(this.D>0) this.D*=-1;
        } else {

            // all values should be above zero
            if(this.P<0) this.P*=-1;
            if(this.I<0) this.I*=-1;
            if(this.D<0) this.D*=-1;
        }
    }
}
