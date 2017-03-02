package org.usfirst.frc.team4455.robot.subsystems;

import org.usfirst.frc.team4455.robot.Robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class DriveTrain extends Subsystem {
	CANTalon zero = new CANTalon(0);		
	CANTalon one = new CANTalon(1);
	CANTalon two = new CANTalon(2);
	CANTalon three = new CANTalon(3);
	CANTalon four = new CANTalon(4);	
	
	public DriveTrain() {
		two.setControlMode(CANTalon.TalonControlMode.Follower.value);
		four.setControlMode(CANTalon.TalonControlMode.Follower.value);
		three.setInverted(true);
		four.setInverted(true);
		
		two.set(0);
		four.set(3);
	}
	
	private boolean driveForward = true;
	
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public void flip()
    {
    	driveForward = !driveForward;
    }
    
    public void set(double x, double y) {
    	System.out.println("DriveTrain.set("+x+", "+y+")");
    
    	if(driveForward) {
			zero.set(y+(x*.25));
			three.set(y-(x*.25));
    	}else {
			zero.set(-y+(x*.25));
			three.set(-y-(x*.25));
    	}
    	if (Math.abs(x) <= 0.01 && Math.abs(y) <= 0.01 ) {
    		Robot.navigation.dampen();
    	}

    }
    
    public boolean isForward() {
    	return driveForward;
    }
}

