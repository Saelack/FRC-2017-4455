package org.usfirst.frc.team4455.robot.subsystems;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Lifter extends Subsystem {
	CANTalon one = new CANTalon(1);

    public Lifter() {
//    	one.
    }
	
	// Put methods for controlling this subsystem
    // here. Call these from Commands.
	

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public void set(double lifter) {
//    	System.out.println("Lifter.set("+lifter+")");
    	one.set(lifter);
//    	System.out.println("\tone.get() = "+one.get());
    }
}

