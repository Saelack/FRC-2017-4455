package org.usfirst.frc.team4455.robot.commands;

import org.opencv.core.Point;
import org.usfirst.frc.team4455.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveAndSeek extends Command {
	private boolean done = false; 
	
    public DriveAndSeek() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.driveTrain);
    	requires(Robot.vision);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
		System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ DriveAndSeek Init ^^^^^^^^^^^^^^^^^^^^^^^^" );

    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {    	
    	// drive forward at .75, adjusting as need be to center...
    	Point center = Robot.vision.getTargetCenter();     // we MIGHT not get a center back... fix that.
    	done = center.y >= 100;
    	double targetX = Math.min(Math.max((center.x-120)/100, -0.35), 0.35);
    	double targetY = Math.min((((center.y)/40.0)-2.6)/4.0, -0.2);
    	System.out.println("center.y = "+center.y+ ", targetX = "+ targetX+ ", targetY = "+targetY);
    	Robot.driveTrain.set(targetX, targetY);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return done; 
    }

    // Called once after isFinished returns true
    protected void end() {
		System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ DriveAndSeek End ^^^^^^^^^^^^^^^^^^^^^^^" );
    	Robot.driveTrain.set(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
