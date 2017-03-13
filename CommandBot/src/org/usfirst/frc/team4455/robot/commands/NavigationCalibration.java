package org.usfirst.frc.team4455.robot.commands;

import org.usfirst.frc.team4455.robot.Robot;
import org.usfirst.frc.team4455.robot.RobotMap;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class NavigationCalibration extends Command {

	private int samples = 0;
	private BuiltInAccelerometer accel;
	private double total;
	private double min;
	private double max;

	
    public NavigationCalibration() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.navigation);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.debug("Calibration: Initializing.");
    	samples = 0;
    	accel = RobotMap.navigationaccelRIO;
    	max = -20;
    	min = 20;
    	total = 0;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	samples++;
    	double a = accel.getY();
    	total += a;
    	if (a < min ) min = a;
    	if (a > max ) max = a;
    	Robot.debug("Calibration: Taking sample#"+samples+" as "+a);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	boolean finished = (samples > 1000); 
        return finished;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.debug("Calibration: Ending.");
    	double avg = total / samples;
    	double drift = Math.min(avg-min, max-avg);
    	
    	Robot.navigation.calibrate(drift, avg);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.debug("Calibration: Interrupted.");
    }
}
