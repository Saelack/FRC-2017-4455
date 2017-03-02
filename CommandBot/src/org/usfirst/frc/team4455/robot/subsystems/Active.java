package org.usfirst.frc.team4455.robot.subsystems;

import org.usfirst.frc.team4455.robot.RobotMap;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Active extends Subsystem {
	DoubleSolenoid activeSqueezeSolonoid = RobotMap.activeSqueeze;
	DoubleSolenoid activeLiftSolonoid = RobotMap.activeLift;

	public Active() {
		new Compressor().setClosedLoopControl(true);
	}
	
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    public void open() {
    	if(activeSqueezeSolonoid == null) {
    		activeSqueezeSolonoid = RobotMap.activeSqueeze;
    	}
    	activeSqueezeSolonoid.set(Value.kForward);
    }
    
    public void close() {
    	if(activeSqueezeSolonoid == null) {
    		activeSqueezeSolonoid = RobotMap.activeSqueeze;
    	}
    	activeSqueezeSolonoid.set(Value.kReverse);
    }

    public void lift() {
    	if(activeLiftSolonoid == null) {
    		activeLiftSolonoid = RobotMap.activeLift;
    	}
    	activeLiftSolonoid.set(Value.kForward);
    }
    
    public void drop() {
    	if(activeLiftSolonoid == null) {
    		activeLiftSolonoid = RobotMap.activeLift;
    	}
    	activeLiftSolonoid.set(Value.kReverse);
    }

}

