package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.Telemetry;


public class ForkLift {
    private Servo rightClaw;
    private Servo leftClaw;
    public DcMotor motor;
    private DigitalChannel topButton;
    private DigitalChannel bottomButton;
    private Telemetry telemetry;
    private final double CLAW_CLOSE_POSITION = 1;
    private final double CLAW_OPEN_POSITION = 0;

    public ForkLift(Servo rightClaw, Servo leftClaw, DcMotor motor, DigitalChannel topButton, DigitalChannel bottomButton, Telemetry telemetry) {
        this.rightClaw = rightClaw;
        this.leftClaw = leftClaw;
        this.motor = motor;
        this.topButton = topButton;
        this.bottomButton = bottomButton;
        this.rightClaw.setDirection(Servo.Direction.REVERSE);
        resetEncoder();
        this.telemetry = telemetry;
        this.motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void init() {
        openClaw();
        moveUntilDown(0.5);
    }
    public void autoInit() {
        openClaw();
        sleep(500);
        moveMotor(0.5, 200);
        init();
    }

    public void closeClaw() {
        setClawPosition(CLAW_CLOSE_POSITION);
    }

    public void openClaw() {
        setClawPosition(CLAW_OPEN_POSITION);
    }

    public void moveMotor(double speed) {
            if (speed < 0 && !bottomButton.getState()) {
                    speed = 0;
            }
            if (speed > 0 && !topButton.getState()) {
                    speed = 0;
            }
        motor.setPower(speed);
    }

    private void setClawPosition(double position) {
        rightClaw.setPosition(position);
        rightClaw.setPosition(position);
        leftClaw.setPosition(position);
        leftClaw.setPosition(position);
    }

    private void resetEncoder() {
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    public void moveMotor(double speed, long time) {
        moveMotor(speed);
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {}
        stop();
    }
    public void moveUntilDown(double speed) {
        while (bottomButton.getState()) {
            moveMotor(-Math.abs(speed));
        }
        stop();
    }
    public void stop() {
        moveMotor(0);
    }

    private void sleep(long time) {try {Thread.sleep(time);} catch (InterruptedException e) {}}

}