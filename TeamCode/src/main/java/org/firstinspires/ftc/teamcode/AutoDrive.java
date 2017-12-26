package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by Kaden on 11/25/2017.
**/

public class AutoDrive {
    private DcMotor FrontLeft;
    private DcMotor FrontRight;
    private DcMotor RearLeft;
    private DcMotor RearRight;
    private REVGyro imu;
    private double cir = 3.937 * Math.PI;
    private int CPR = 1120; //Clicks per rotation of the encoder with the NeveRest motors. Please do not edit...
    public double heading;
    private HardwareMap hwMap;
    private Telemetry telemetry;
    public final double SPIN_ON_BALANCE_BOARD_SPEED = 0.15;
    public final double SPIN_ON_BALANCE_BOARD_DISTANCE = 3;
    public final double DRIVE_OFF_BALANCE_BAORD_SPEED = 0.4;
    public final double TURN_TO_CRYPTOBOX_SPEED = 0.25;
    public final double DRIVE_INTO_CRYPTOBOX_SPEED = 0.4;
    public final double DEFAULT_

    public AutoDrive(DcMotor FrontLeft, DcMotor FrontRight, DcMotor RearLeft, DcMotor RearRight, HardwareMap hwMap, Telemetry telemetry) {
        this.FrontLeft = FrontLeft;
        this.FrontRight = FrontRight;
        this.FrontRight.setDirection(DcMotor.Direction.REVERSE);
        this.RearLeft = RearLeft;
        this.RearRight = RearRight;
        this.RearRight.setDirection(DcMotor.Direction.REVERSE);
        this.hwMap = hwMap;
        this.imu = new REVGyro(this.hwMap.get(BNO055IMU.class, "imu"));
        this.telemetry = telemetry;
        setBRAKE();
    }

    public AutoDrive(HardwareMap hwMap, Telemetry telemetry) {
        this.FrontLeft = hwMap.dcMotor.get("m1");
        this.FrontRight = hwMap.dcMotor.get("m2");
        this.FrontRight.setDirection(DcMotor.Direction.REVERSE);
        this.RearLeft = hwMap.dcMotor.get("m3");
        this.RearRight = hwMap.dcMotor.get("m4");
        this.RearRight.setDirection(DcMotor.Direction.REVERSE);
        this.imu = new REVGyro(hwMap.get(BNO055IMU.class, "imu"));
        this.telemetry = telemetry;
        setBRAKE();


    }

    public void driveTranslateRotate(double x, double y, double z, double distance) {
        resetEncoders();
        double clicks = Math.abs(distance * CPR / cir);
        double fl = clip(-y + -x - z);
        double fr = clip(-y + x + z);
        double rl = clip(-y + x - z);
        double rr = clip(-y + -x + z);
        double[] list = {fl, fr, rl, rr};
        double high = findHigh(list);
        driveSpeeds(fl, fr, rl, rr);
        while (!(isMotorAtTarget(FrontLeft, fl / high * clicks)) && (!(isMotorAtTarget(FrontRight, fr / high * clicks))) && (!(isMotorAtTarget(RearLeft, rl / high * clicks))) && (!(isMotorAtTarget(RearRight, rr / high * clicks)))) {
            telemetrizeEncoders();
            telemetrizeSpeeds();
            telemetry.update();
        }
        stopMotors();
    }


    public void driveSpeeds(double flSpeed, double frSpeed, double rlSpeed, double rrSpeed) {
        FrontLeft.setPower(clip(flSpeed));
        FrontRight.setPower(clip(frSpeed));
        RearLeft.setPower(clip(rlSpeed));
        RearRight.setPower(clip(rrSpeed));
    }

    public double clip(double value) {
        return Range.clip(value, -1, 1);
    }

    public double findHigh(double[] nums) {
        double high = 0;
        int i = 0;
        while (i < nums.length) {
            if (Math.abs(nums[i]) > high) {
                high = Math.abs(nums[i]);
            }
            i++;
        }
        return high;
    }

    public void resetEncoders() {
        FrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RearLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RearRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        FrontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        FrontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        RearLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        RearRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }

    public void stopMotors() {
        driveSpeeds(0, 0, 0, 0);
    }

    public int getCurrentPosition(DcMotor motor) {
        return motor.getCurrentPosition();
    }

    public boolean isMotorAtTarget(DcMotor motor, double target) {
        return Math.abs(getCurrentPosition(motor)) >= Math.abs(target);
    }

    public void rightGyro(double x, double y, double z, double target) {
        heading = getHeading();
        double change = 0;
        double fl = clip(-y + -x - z);
        double fr = clip(-y + x + z);
        double rl = clip(-y + x - z);
        double rr = clip(-y + -x + z);
        driveSpeeds(fl, fr, rl, rr);
        if(heading < target) {
            while(change <= 0) {
                change = getHeading() - heading;
                heading = getHeading();
                telemetrizeGyro();
            }
        }
        while(heading >= target) {
            heading = getHeading();
            telemetrizeGyro();
        }
        stopMotors();
    }

    public void leftGyro(double x, double y, double z, double target) {
        heading = getHeading();
        double change = 0;
        double fl = clip(-y + -x - z);
        double fr = clip(-y + x + z);
        double rl = clip(-y + x - z);
        double rr = clip(-y + -x + z);
        driveSpeeds(fl, fr, rl, rr);
        if (target < heading) {
          while(change >= 0) {
            change = getHeading() - heading;
            heading = getHeading();
            telemetrizeGyro();
          }
        }
        while(heading <= target) {
            heading = getHeading();
            telemetrizeGyro();
        }
        stopMotors();
    }

    public void init() {
        imu.calibrate();
        heading = getHeading();
    }

    public double getHeading() {
        return imu.getHeading();
    }

    public void telemetrizeGyro() {
        telemetry.addData("Current heading: ", getHeading());
        telemetry.update();
    }
    public void telemetrizeEncoders() {
        telemetry.addData("First motor: ", FrontLeft.getCurrentPosition());
        telemetry.addData("Second motor: ", FrontRight.getCurrentPosition());
        telemetry.addData("third motor: ", RearLeft.getCurrentPosition());
        telemetry.addData("fourth motor: ", RearRight.getCurrentPosition());
    }
    public void telemetrizeSpeeds() {
        telemetry.addData("First motor: ", FrontLeft.getPower());
        telemetry.addData("Second motor: ", FrontRight.getPower());
        telemetry.addData("third motor: ", RearLeft.getPower());
        telemetry.addData("fourth motor: ", RearRight.getPower());
    }

    public void setBRAKE() {
        this.FrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.FrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.RearLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.RearRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
}
