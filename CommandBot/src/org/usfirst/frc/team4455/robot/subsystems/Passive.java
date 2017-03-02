package org.usfirst.frc.team4455.robot.subsystems;

import org.usfirst.frc.team4455.robot.RobotMap;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Passive extends Subsystem {
	DoubleSolenoid passiveSolonoid = RobotMap.passiveCylinder;
	
	public Passive() {
		new Compressor().setClosedLoopControl(true);
	}
	
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public void open() {
    	if(passiveSolonoid == null) {
    		passiveSolonoid = RobotMap.passiveCylinder;
    	}
    	passiveSolonoid.set(Value.kForward);
    }
    
    public void close() {
    	if(passiveSolonoid == null) {
    		passiveSolonoid = RobotMap.passiveCylinder;
    	}
    	passiveSolonoid.set(Value.kReverse);
    }
}

