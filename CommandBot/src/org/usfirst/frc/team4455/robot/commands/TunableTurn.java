package org.usfirst.frc.team4455.robot.commands;

import org.usfirst.frc.team4455.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class TunableTurn extends Command {
	private double seconds;
	private double power;
	
	public TunableTurn(double seconds, double power) {
    	requires(Robot.driveTrain);
		
		this.seconds = seconds;
		this.power = power;
	}

	@Override
    // Called just before this Command runs the first time
    protected void initialize() {
		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% TunableTurn Init %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%" );
		System.out.println("tunableTurn("+seconds+", "+power+")");
		Robot.driveTrain.set(power, 0);
    }

	@Override
	protected void execute() { // called every 20 ms or so...
		System.out.println("tunableTurn("+seconds+", "+power+")");
		seconds -= .02;
		
	}

	@Override
	protected void end() {
		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% TunableTurn End %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%" );
		Robot.driveTrain.set(0, 0);
	}
	
	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return seconds < 0;
	}
}
