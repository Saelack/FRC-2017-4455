package org.usfirst.frc.team4455.robot.subsystems;

import org.usfirst.frc.team4455.robot.RobotMap;
import org.usfirst.frc.team4455.robot.commands.DeadReckoning;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Navigation extends Subsystem {
    private ADXRS450_Gyro gyro = RobotMap.navigationgyro;
    private BuiltInAccelerometer accelRIO = RobotMap.navigationaccelRIO;

    private double vx = 0.0;
    private double vy = 0.0;
    
    private double x = 0.0;
    private double y = 0.0;
    
    private long lastTime = System.currentTimeMillis();

    public void initDefaultCommand() {
        //setDefaultCommand(new DeadReckoning());
    }
    
    public void update() {
    	// do we need to keep a last time?
    	if(!isReady()) {
    	    gyro = RobotMap.navigationgyro;
    	    accelRIO = RobotMap.navigationaccelRIO;
    	    
    		if(!isReady()) {
    			return;
    		}
    	}
    	
    	// a is our acceleration, hopefully in a forward/backward direction.
    	long now = System.currentTimeMillis();
    	double a = accelRIO.getY();
    	
    	// heading is which way we are going...
    	double heading = gyro.getAngle();
    	
    	// let's normalize it...
    	while(heading < 0) {
    		heading = ((heading+360) % 360);
    	}
    	
    	// then pull out the dx/dy components, and multiply by 32ft/s^2 to convert from g's... (32ft/s^2 because we're Imperial, baby!)
    	double dy = Math.cos(Math.toRadians(heading)) * a * 32;
    	double dx = Math.sin(Math.toRadians(heading)) * a * 32;

    	// we're gonna need to scale based on update time...
    	double t = now-lastTime/1000.0;
    	
    	// add our dx/dy (ft/s^s) times t (s) to get vx/vy (ft/s) 
    	vx += dx * t;
    	vy += dy * t;
    	
    	// add our vx/vy (ft/s) times t (s) to get x/y (ft)
    	x += vx * t;
    	y += vy * t;
    	
    	// update our time.
    	lastTime = now;
    	
    	// throw some status and such out.
    	SmartDashboard.putString("navigation-sensor-aX", String.format("%1$.3f", accelRIO.getX()));
    	SmartDashboard.putString("navigation-sensor-aY", String.format("%1$.3f", a));
    	SmartDashboard.putString("navigation-sensor-aZ", String.format("%1$.3f", accelRIO.getZ()));
    	SmartDashboard.putString("navigation-accel-dX", String.format("%1$.3f", dx));
    	SmartDashboard.putString("navigation-accel-dY", String.format("%1$.3f", dy));
    	SmartDashboard.putString("navigation-vel-vX", String.format("%1$.3f", vx));
    	SmartDashboard.putString("navigation-vel-vY", String.format("%1$.3f", vy));
    	SmartDashboard.putString("navigation-X", String.format("%1$.3f", x));
    	SmartDashboard.putString("navigation-Y", String.format("%1$.3f", y));
    }
    
    public boolean isReady() {
    	return (gyro != null && accelRIO != null);
    }
}

