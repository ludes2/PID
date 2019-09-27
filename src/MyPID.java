public class MyPID {

    private double P = 0;
    private double I = 0;
    private double D = 0;

    private double targetPoint = 0; // setpoint

    private boolean reversed=false;

    private double lastError = 0;
    private boolean firstRun = true;

    /**
     * P-only Constructor
     * @param p
     */
    public MyPID(double p){
        this.P = p;
        checkSigns();
    }

    /**
     * PD-only Constructor
     * @param p
     * @param d
     */
    public MyPID(double p, double d){
        this.P = p;
        this.D = d;
        checkSigns();
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
        double Poutput;
        double Doutput;
        this.targetPoint = target;

        // calculate error - delta von target and actual
        double error = target - actual;


        // calculate P --> P * error
        Poutput = this.P * error;

        // calculate D --> D * (error - lastError)
        Doutput = this.D * (error - lastError);
        this.lastError = error;

        // calculate I -->

        output = Poutput + Doutput;
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
