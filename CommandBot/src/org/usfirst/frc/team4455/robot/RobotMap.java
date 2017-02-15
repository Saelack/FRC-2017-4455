package org.usfirst.frc.team4455.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.interfaces.Accelerometer.Range;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
    public static DoubleSolenoid activeSqueeze;
    public static DoubleSolenoid activeLift;
    public static DoubleSolenoid passiveCylinder;
    public static ADXRS450_Gyro navigationgyro;
    public static BuiltInAccelerometer navigationaccelRIO;

    public static void init() {
        activeSqueeze = new DoubleSolenoid(0, 2, 3);
        LiveWindow.addActuator("Active", "activeSqueeze", activeSqueeze);
        
        activeLift = new DoubleSolenoid(0, 4, 5);
        LiveWindow.addActuator("Active", "activeLift", activeLift);
        
        passiveCylinder = new DoubleSolenoid(0, 0, 1);
        LiveWindow.addActuator("Passive", "passiveCylinder", passiveCylinder);
        
        navigationgyro = new ADXRS450_Gyro();
        LiveWindow.addSensor("Navigation", "gyro", navigationgyro);
        navigationaccelRIO = new BuiltInAccelerometer(Range.k4G);
        LiveWindow.addSensor("Navigation", "accelRIO", navigationaccelRIO);
    }
}