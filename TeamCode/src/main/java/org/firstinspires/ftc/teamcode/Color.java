package org.firstinspires.ftc.teamcode;

/**
 * Created by Kaden on 1/2/2018.
 */

public enum Color {
    RED, BLUE, UNKNOWN;
    Color not(Color target) {
        switch (target) {
            case RED: return BLUE;
            case BLUE: return RED;
            case UNKNOWN: return UNKNOWN;
        }
        return UNKNOWN;
    }
}