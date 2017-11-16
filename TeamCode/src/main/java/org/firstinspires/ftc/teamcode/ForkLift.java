package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.DigitalChannel;

public class ForkLift {
	private Servo rightClaw;
	private Servo leftClaw;
	private DcMotor drawerSlide;
	private DigitalChannel topButton;
	private DigitalChannel bottomButton;
	private double clawPosition = 0.0;
    private double clawHighEnd = 0.45;
    private double clawLowEnd = 0.1;
    private double DrawerSlideSpeed = 0;
    private double up = 0;
    private double down = 0;
    
	public ForkLift(Servo rightClaw, Servo leftClaw, DcMotor drawerSlide, DigitalChannel TopButton, DigitalChannel BottomButton) {
		this.rightClaw = rightClaw;
		this.leftClaw = leftClaw;
		this.drawerSlide = drawerSlide;
		this.topButton = topButton;
		this.bottomButton = bottomButton;
		this.rightClaw.setDirection(Servo.Direction.REVERSE);
	}
	public void initClaw() {
		rightClaw.setPosition(clawPosition);
		rightClaw.setPosition(clawPosition);
		leftClaw.setPosition(clawPosition);
		leftClaw.setPosition(clawPosition);
	}
	public void closeClaw() {
		rightClaw.setPosition(clawHighEnd);
		rightClaw.setPosition(clawHighEnd);
		leftClaw.setPosition(clawHighEnd);
		leftClaw.setPosition(clawHighEnd);	
	}
	public void openClaw() {
		rightClaw.setPosition(clawLowEnd);
		rightClaw.setPosition(clawLowEnd);
		leftClaw.setPosition(clawLowEnd);
		leftClaw.setPosition(clawLowEnd);
	}

}