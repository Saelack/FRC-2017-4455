
package org.usfirst.frc.team4455.robot;

import org.usfirst.frc.team4455.robot.commands.ActiveClose;
import org.usfirst.frc.team4455.robot.commands.ActiveDrop;
import org.usfirst.frc.team4455.robot.commands.ActiveLift;
import org.usfirst.frc.team4455.robot.commands.ActiveOpen;
import org.usfirst.frc.team4455.robot.commands.DeadReckoning;
import org.usfirst.frc.team4455.robot.commands.NavigationCalibration;
import org.usfirst.frc.team4455.robot.commands.PassiveClose;
import org.usfirst.frc.team4455.robot.commands.PassiveOpen;
import org.usfirst.frc.team4455.robot.commands.Seek;
import org.usfirst.frc.team4455.robot.subsystems.Active;
import org.usfirst.frc.team4455.robot.subsystems.DriveTrain;
import org.usfirst.frc.team4455.robot.subsystems.Lifter;
import org.usfirst.frc.team4455.robot.subsystems.Navigation;
import org.usfirst.frc.team4455.robot.subsystems.Passive;
import org.usfirst.frc.team4455.robot.subsystems.V2Vision;
import org.usfirst.frc.team4455.robot.subsystems.Vision;

import edu.wpi.cscore.AxisCamera;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	public static final Passive passive = new Passive();
	public static final Active active = new Active();
	public static final DriveTrain driveTrain = new DriveTrain();
	public static final Lifter lifter = new Lifter();
	public static final Navigation navigation = new Navigation();
	public static final Vision vision = new Vision();
	public static final V2Vision v2vision = new V2Vision();
	
	public static OI oi;

	Command calibrate = new NavigationCalibration();
	Command deadReckoning = new DeadReckoning();

	Command activeOpen = new ActiveOpen();
	Command activeClose = new ActiveClose();
	Command activeLift = new ActiveLift();
	Command activeDrop = new ActiveDrop();
	Command passiveOpen = new PassiveOpen();
	Command passiveClose = new PassiveClose();	
	Command seek = new Seek();
	
	private Joystick driver;
	private Joystick codriver;
	
	UsbCamera usb;
	
	Command autonomousCommand;
	SendableChooser<Command> chooser = new SendableChooser<>();
	Ultrasonic sonar;
	
	public static final boolean DEBUG = false;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {	
		
//		sonar = new Ultrasonic();

		
		oi = new OI();
		
		driver = oi.driver;
		codriver = oi.codriver;
		
		RobotMap.init();
		
		AxisCamera alpha = CameraServer.getInstance().addAxisCamera("Alpha", "axis-alpha");
		AxisCamera beta = CameraServer.getInstance().addAxisCamera("Beta", "axis-beta");
		
		
		usb = CameraServer.getInstance().startAutomaticCapture("Active USB", 0);
		usb.setFPS(2);
		CameraServer.getInstance().startAutomaticCapture("Passive USB", 1 );
		// chooser.addObject("My Auto", new MyAutoCommand());
		SmartDashboard.putData("Auto mode", chooser);
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {
		navigation.reset();
	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
		autonomousCommand = chooser.getSelected();

		/*
		 * String autoSelected = SmartDashboard.getString("Auto Selector",
		 * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
		 * = new MyAutoCommand(); break; case "Default Auto": default:
		 * autonomousCommand = new ExampleCommand(); break; }
		 */

		autonomousCommand = seek;
		
		// schedule the autonomous command (example)
		if (autonomousCommand != null)
			autonomousCommand.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
		
	}

	@Override
	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (autonomousCommand != null)
			autonomousCommand.cancel();
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		
		//System.out.println(Runtime.getRuntime().freeMemory());
//		System.gc();
		//usb.free();
	
		//System.out.println(sonar.getRangeInches());
		
		SmartDashboard.putData("Nav Subsystem", navigation);
		SmartDashboard.putData("Calibrate", calibrate);
		SmartDashboard.putData("Start Dead Reckoning", deadReckoning);
		SmartDashboard.putData("accel", RobotMap.navigationaccelRIO);

		SmartDashboard.putData("Active Subsystem", active);
		SmartDashboard.putData("Active Open", activeOpen);
		SmartDashboard.putData("Active Close", activeClose);
		SmartDashboard.putData("Active Lift", activeLift);
		SmartDashboard.putData("Active Drop", activeDrop);
		
		SmartDashboard.putData("Passive Subsystem", passive);
		SmartDashboard.putData("Passive Open", passiveOpen);
		SmartDashboard.putData("Passive Close", passiveClose);
		
		SmartDashboard.putData("Vision System", vision);
		SmartDashboard.putData("Vision", seek);
		
		SmartDashboard.putBoolean("Drive Forward", driveTrain.isForward());
		
		double x = driver.getX();
		double y = driver.getY();
		
		driveTrain.set(x, y*y*y);
		lifter.set(codriver.getY());
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
		LiveWindow.run();
	}
	
	public static void debug(String msg) {
		if(DEBUG) {
			System.out.println(msg);
		}
	}
}
