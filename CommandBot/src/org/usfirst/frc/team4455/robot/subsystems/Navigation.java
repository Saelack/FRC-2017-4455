package org.usfirst.frc.team4455.robot.subsystems;

import org.usfirst.frc.team4455.robot.Robot;
import org.usfirst.frc.team4455.robot.RobotMap;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
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
    
    private double drift;
    private double offset;
    
    private long lastTime = System.currentTimeMillis();

    private double window = 4.0;
    
    public void initDefaultCommand() {
        //setDefaultCommand(new DeadReckoning());
    }
    
    public void update() {
    	if(!isReady()) {
    	    gyro = RobotMap.navigationgyro;
    	    accelRIO = RobotMap.navigationaccelRIO;
    	    
    		if(!isReady()) {
    			return;
    		}
    	}
    	
    	// a is our acceleration, hopefully in a forward/backward direction.
    	long now = System.currentTimeMillis();
    	double a = accelRIO.getY()-offset-drift;
    	
    	if (drift != 0.0)
    		a = ((int)(a/(drift*window)))*(drift*window); 
    	
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
    	double dt = (now-lastTime)/1000.0;

    	System.out.println(String.format("a: %1$.3f, heading: %2$.3f, dx: %3$.3f, dy: %4$.3f, dt: %5$.3f", a, heading, dx, dy, dt));
    	
    	// add our dx/dy (ft/s^s) times t (s) to get vx/vy (ft/s) 
    	vx += dx * dt;
    	vy += dy * dt;
    	
    	// add our vx/vy (ft/s) times t (s) to get x/y (ft)
    	x += vx * dt;
    	y += vy * dt;
    	
    	System.out.println(String.format("vx: %1$.3f, vy: %2$.3f, x: %3$.3f, y: %4$.3f", vx, vy, x, y));

    	// update our time.
    	lastTime = now;
    	
    	// throw some status and such out.
    	SmartDashboard.putString("navigation-sensor-aY", String.format("%1$.3f", a));
    	SmartDashboard.putString("navigation-accel", String.format("%1$.3f, %2$.3f", dx, dy));
    	SmartDashboard.putString("navigation-vel", String.format("%1$.3f, %2$.3f", vx, vy));
    	SmartDashboard.putString("navigation-loc", String.format("%1$.3f, %2$.3f", x, y));
    	SmartDashboard.putString("navigation-correction", String.format("%1$.3f, %2$.3f", drift, offset));
    }
    
    public void reset() {
    	vx = vy = x = y = 0;
    }
    
    public boolean isReady() {
    	return (gyro != null && accelRIO != null);
    }
    
    public void calibrate( double drift, double offset ) {
    	this.drift = drift;
    	this.offset = offset;
    	Robot.debug("Navigation: Calibrating with "+drift+", "+offset);
    	
    }
    public void dampen(){
    	vx *= .5;
    	if (vx < 0.01) vx= 0;
    	vy *= .5;
    	if (vy < 0.01) vy= 0;
    	
    }
}

