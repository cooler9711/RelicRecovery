package org.firstinspires.ftc.teamcode;

/**
 * Created by Kaden on 1/3/2018.
 */

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Point;

public class Systems {
    private DriveMecanum DriveMecanum;
    private AutoDrive AutoDrive;
    private ForkLift ForkLift;
    private RelicClaw RelicClaw;
    private Phone phone;
    private JewelArm JewelArm;
    private Telemetry telemetry;
    public AutoGlyphs glyphDetector;
    static final double STRAFING_DAMPEN_FACTOR_FOR_MULTI_GLYPH = 0.2;

    public Systems(DriveMecanum drive, ForkLift ForkLift, RelicClaw RelicClaw) {
        this.DriveMecanum = drive;
        this.ForkLift = ForkLift;
        this.RelicClaw = RelicClaw;
    }

    public Systems(AutoDrive drive, ForkLift ForkLift, JewelArm JewelArm, Phone phone, HardwareMap hardwareMap, Telemetry telemetry) {
        this.AutoDrive = drive;
        this.ForkLift = ForkLift;
        this.JewelArm = JewelArm;
        this.phone = phone;
        this.telemetry = telemetry;
        this.glyphDetector = new AutoGlyphs(hardwareMap, telemetry);
    }

    void pushInBlock() {
        ForkLift.openClaw();
        sleep(100);
        AutoDrive.backward(AutoDrive.DRIVE_INTO_CRYPTOBOX_SPEED, 3);
        ForkLift.moveUntilDown();
        ForkLift.setClawPositionPushInBlock();
        sleep(150);
        AutoDrive.forwardTime(AutoDrive.DRIVE_INTO_CRYPTOBOX_SPEED, 500);
    }

    public void grabSecondGlyph() {
        ForkLift.closeClaw();
        ForkLift.moveMotor(1, 750);
        DriveMecanum.driveTranslateRotate(0, -1, 0, 550);
        ForkLift.openClaw();
        DriveMecanum.driveTranslateRotate(0, 1, 0, 500);
        ForkLift.moveUntilDown(0.75);
        DriveMecanum.driveTranslateRotate(0, -1, 0, 750);
        ForkLift.closeClaw();
        sleep(250);
        ForkLift.moveMotor(1, 250);
    }

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
        }
    }

    public void stopAll() {
        AutoDrive.stopMotors();
        ForkLift.moveMotor(0);
        RelicClaw.moveMotor(0);
    }

    public void getMoreGlyphs(double returnHeading, CryptoboxColumn column) {
        JewelArm.up(); //take this out later
        setUpMultiGlyph();
        ElapsedTime findGlyphTime = new ElapsedTime();
        findGlyphTime.reset();
        double xOffSet;
        double yPos;
        double decisionPoint = 0;
        Point bestPos = new Point(DEFAULT_X_POS_VALUE, 0);
        while (findGlyphTime.seconds() < 0.5) {
        }
        while (findGlyphTime.seconds() < 3.5) {
            xOffSet = glyphDetector.getXOffset();
            yPos = glyphDetector.getYPos();
            if ((Math.abs(xOffSet) < Math.abs(bestPos.x)) && (xOffSet != AutoGlyphs.DEFAULT_X_POS_VALUE)) {// && (yPos < 60)) {
                bestPos.x = xOffSet;
                bestPos.y = yPos;
                decisionPoint = findGlyphTime.seconds();
            }
        }
        telemetry.addData("Glyph Position", bestPos.toString());
        telemetry.addData("Decision made at", decisionPoint);
        telemetry.update();
        glyphDetector.disable();
        ForkLift.openClaw();
        if (bestPos.x == AutoGlyphs.DEFAULT_X_POS_VALUE) {
            bestPos.x = 0;
        }
        double distanceToStrafe = bestPos.x * STRAFING_DAMPEN_FACTOR_FOR_MULTI_GLYPH;
        strafeForMultiGlyph(distanceToStrafe);
        AutoDrive.forward(AutoDrive.DRIVE_INTO_GLYPH_PIT_SPEED, AutoDrive.DRIVE_INTO_GLYPH_PIT_DISTANCE);
        AutoDrive.forward(AutoDrive.DRIVE_INTO_GLYPHS_SPEED, AutoDrive.DRIVE_INTO_GLYPHS_DISTANCE);
        ForkLift.closeClaw();
        sleep(300);
        ForkLift.moveMotor(1, 150);
        AutoDrive.backward(AutoDrive.MAX_SPEED, AutoDrive.DRIVE_INTO_GLYPHS_DISTANCE + AutoDrive.DRIVE_INTO_GLYPH_PIT_DISTANCE);
        double heading = AutoDrive.getHeading();
        if (heading < returnHeading) {
            AutoDrive.leftGyro(AutoDrive.SPIN_TO_CRYPTOBOX_SPEED, returnHeading);
        } else {
            AutoDrive.rightGyro(AutoDrive.SPIN_TO_CRYPTOBOX_SPEED, returnHeading);
        }
        strafeForMultiGlyph(distanceToStrafe);
    }

    public void setUpMultiGlyph() {
        glyphDetector.enable();
        ForkLift.closeAllTheWay();
        phone.faceFront();

    }

    private void strafeForMultiGlyph(double distanceToStrafe) {
        if (distanceToStrafe > 0) {
            AutoDrive.strafeRight(AutoDrive.MULTI_GLYPH_STRAFE_SPEED, distanceToStrafe);
        } else if (distanceToStrafe < 0) {
            AutoDrive.strafeLeft(AutoDrive.MULTI_GLYPH_STRAFE_SPEED, distanceToStrafe);
        }
    }
}
