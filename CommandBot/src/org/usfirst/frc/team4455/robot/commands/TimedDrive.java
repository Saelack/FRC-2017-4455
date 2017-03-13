package org.usfirst.frc.team4455.robot.commands;

import org.usfirst.frc.team4455.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class TimedDrive extends Command {
	private double seconds;
	private double power;
	
	public TimedDrive(double seconds, double power) {
    	requires(Robot.driveTrain);
		
		this.seconds = seconds;
		this.power = power;
	}

	@Override
    // Called just before this Command runs the first time
    protected void initialize() {
		System.out.println("##################################### TimeDrive Init #######################" );
		System.out.println("timedDrive("+seconds+", "+power+")");
		Robot.driveTrain.set(0, power);
    }

	@Override
	protected void execute() { // called every 20 ms or so...
		seconds -= .02;
		System.out.println("timedDrive("+seconds+", "+power+")");
	}

	@Override
	protected void end() {
		System.out.println("##################################### TimeDrive End #######################" );
		Robot.driveTrain.set(0, 0);
	}
	
	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return seconds < 0;
	}
}
