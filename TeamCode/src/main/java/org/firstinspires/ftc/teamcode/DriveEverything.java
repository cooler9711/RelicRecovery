package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "DriveEverything", group = "linear OpMode")
public class DriveEverything extends LinearOpMode {
    private Robot robot;

    @Override
    public void runOpMode() {
        robot = new Robot(this);
        robot.mapRobot();
        waitForStart();
        while (opModeIsActive()) {
            //drive
            if (Math.abs(gamepad1.left_stick_x) + Math.abs(gamepad1.left_stick_y) + Math.abs(gamepad1.right_stick_x) + Math.abs(gamepad1.right_stick_y) > Math.abs(gamepad2.left_stick_x) + Math.abs(gamepad2.left_stick_y) + Math.abs(gamepad2.right_stick_x) + Math.abs(gamepad2.right_stick_y)) {
                if (gamepad1.right_bumper) {
                    robot.drive.driveLeftRight(gamepad1.left_stick_x * robot.drive.BUMPER_SLOW_SPEED, gamepad1.right_stick_x * robot.drive.BUMPER_SLOW_SPEED, gamepad1.left_stick_y * robot.drive.BUMPER_SLOW_SPEED, gamepad1.right_stick_y * robot.drive.BUMPER_SLOW_SPEED);
                } else {
                    robot.drive.driveLeftRight(gamepad1.left_stick_x, gamepad1.right_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_y);
                }
            } else if (Math.abs(gamepad2.left_stick_x) + Math.abs(gamepad2.left_stick_y) + Math.abs(gamepad2.right_stick_x) + Math.abs(gamepad2.right_stick_y) > Math.abs(gamepad1.left_stick_x) + Math.abs(gamepad1.left_stick_y) + Math.abs(gamepad1.right_stick_x) + Math.abs(gamepad1.right_stick_y)) {
                if (gamepad2.right_bumper) {
                    robot.drive.driveLeftRight(gamepad2.left_stick_x * robot.drive.BUMPER_SLOW_SPEED, gamepad2.right_stick_x * robot.drive.BUMPER_SLOW_SPEED, gamepad2.left_stick_y * robot.drive.BUMPER_SLOW_SPEED, gamepad2.right_stick_y * robot.drive.BUMPER_SLOW_SPEED);
                } else {
                    robot.drive.driveLeftRight(gamepad2.left_stick_x, gamepad2.right_stick_x, gamepad2.left_stick_y, gamepad2.right_stick_y);

                }
            } else {
                if (gamepad1.dpad_up) { //Forward
                    robot.drive.forward(robot.drive.D_PAD_SLOW_SPEED);
                } else if (gamepad1.dpad_left) { //Right
                    robot.drive.strafeRight(robot.drive.D_PAD_SLOW_SPEED);
                } else if (gamepad1.dpad_down) { //Backward
                    robot.drive.backward(robot.drive.D_PAD_SLOW_SPEED);
                } else if (gamepad1.dpad_right) { //Left
                    robot.drive.strafeLeft(robot.drive.D_PAD_SLOW_SPEED);
                } else if (gamepad2.dpad_left) { //Left
                    robot.drive.strafeLeft(robot.drive.D_PAD_SLOW_SPEED);
                } else if (gamepad2.dpad_right) { //Right
                    robot.drive.strafeRight(robot.drive.D_PAD_SLOW_SPEED);
                } else {
                    robot.drive.stopMotors();
                }
            }
            if (gamepad1.left_bumper) {
                robot.drive.setFLOAT();
            }

            if (gamepad2.left_bumper) {
                robot.drive.setBRAKE();
            }

            //ForkLift
            if (gamepad1.a) {
                robot.forkLift.closeClaw();
            }

            if (gamepad1.b) {
                robot.forkLift.openClaw();
                robot.jewelArm.up(); //just in case
                robot.phone.faceSideways();
            }

            if (gamepad1.x) {
                robot.grabSecondGlyph();
            }

            if(gamepad1.y) {
                robot.grabSecondGlyphSimple();
            }
            robot.forkLift.moveMotor(gamepad1.right_trigger - gamepad1.left_trigger);

            //RelicClaw
            if (gamepad2.a) {
                robot.relicClaw.closeClaw();
            }
            if (gamepad2.b) {
                robot.relicClaw.openClaw();
            }
            if (gamepad2.dpad_up) {
                robot.relicClaw.up();
            }
            if (gamepad2.dpad_down) {
                robot.relicClaw.down();
            }
            if (gamepad2.x) {
                robot.relicClaw.pickup();
            }
            if (gamepad2.y) {
                robot.relicClaw.driving();
            }
            robot.relicClaw.moveMotor(gamepad2.left_trigger - gamepad2.right_trigger);
        }
    }
}
