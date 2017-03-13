package org.usfirst.frc.team4455.robot.commands;

import org.opencv.core.Point;
import org.usfirst.frc.team4455.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class TurnAndSeek extends Command {
	private boolean done = false; 
	private double power;

    public TurnAndSeek(double power) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.driveTrain);
    	requires(Robot.vision);
    	
    	this.power = power;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
		System.out.println("///////////////////////////////////// TurnAndSeek Init ////////////////////////" );

    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {    	
    	double targetX = 0.0;
    	Point center = Robot.vision.getTargetCenter();     // we MIGHT not get a center back... fix that.
    	if(center == null) {
    		targetX = power;
    		System.out.println("Aquiring vision... targetX = "+targetX);
    	} else {
	    	targetX = (center.x-120)/100;
	    	done = Math.abs(targetX) < 10.0;   // how close is close enough?
	    	System.out.println("center.y = "+center.y+ ", targetX = "+ targetX);
    	}
    	Robot.driveTrain.set(targetX, 0.0);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return done; 
    }

    // Called once after isFinished returns true
    protected void end() {
		System.out.println("///////////////////////////////////// TurnAndSeek End ////////////////////////" );
    	Robot.driveTrain.set(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
