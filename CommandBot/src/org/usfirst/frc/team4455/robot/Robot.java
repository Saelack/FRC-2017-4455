
package org.usfirst.frc.team4455.robot;

import org.usfirst.frc.team4455.robot.commands.ActiveClose;
import org.usfirst.frc.team4455.robot.commands.ActiveDrop;
import org.usfirst.frc.team4455.robot.commands.ActiveLift;
import org.usfirst.frc.team4455.robot.commands.ActiveOpen;
import org.usfirst.frc.team4455.robot.commands.DeadReckoning;
import org.usfirst.frc.team4455.robot.commands.DriveAndSeek;
import org.usfirst.frc.team4455.robot.commands.DriveTrainFlip;
import org.usfirst.frc.team4455.robot.commands.NavigationCalibration;
import org.usfirst.frc.team4455.robot.commands.PassiveClose;
import org.usfirst.frc.team4455.robot.commands.PassiveOpen;
import org.usfirst.frc.team4455.robot.commands.Seek;
import org.usfirst.frc.team4455.robot.commands.TimedDrive;
import org.usfirst.frc.team4455.robot.commands.TunableTurn;
import org.usfirst.frc.team4455.robot.subsystems.Active;
import org.usfirst.frc.team4455.robot.subsystems.DriveTrain;
import org.usfirst.frc.team4455.robot.subsystems.Lifter;
import org.usfirst.frc.team4455.robot.subsystems.Navigation;
import org.usfirst.frc.team4455.robot.subsystems.Passive;
import org.usfirst.frc.team4455.robot.subsystems.Vision;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
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
	
	public static OI oi;

	private Command calibrate = new NavigationCalibration();
	private Command deadReckoning = new DeadReckoning();

	private Command flip = new DriveTrainFlip();

	private Command activeOpen = new ActiveOpen();
	private Command activeClose = new ActiveClose();
	private Command activeLift = new ActiveLift();
	private Command activeDrop = new ActiveDrop();
	private Command passiveOpen = new PassiveOpen();
	private Command passiveClose = new PassiveClose();	
	private Command seek = new Seek();
	
	private Joystick driver;
	private Joystick codriver;
	
	private SendableChooser<Double> turn1Direction = new SendableChooser<>();
	private SendableChooser<Double> turn1TurnTime = new SendableChooser<>();
	private SendableChooser<Double> turn1ForwardTime = new SendableChooser<>();
	private SendableChooser<Double> turn2Direction = new SendableChooser<>();
	private SendableChooser<Double> turn2TurnTime = new SendableChooser<>();
	private SendableChooser<Double> turn2ForwardTime = new SendableChooser<>();
	private SendableChooser<Double> turn3Direction = new SendableChooser<>();
	private SendableChooser<Double> turn3TurnTime = new SendableChooser<>();
	private SendableChooser<Double> turn3ForwardTime = new SendableChooser<>();
	
	private CommandGroup tunableAuto = new CommandGroup("TunableAuto");
	
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
		vision.setupCamera();
				
		// we try things.  some of them might work.
		// we might want to go thru the list of every SmartDashboard key, and remove/removePersistant
		SmartDashboard board = new SmartDashboard();
		for (String key : board.getKeys()) {
			board.clearPersistent(key);
			board.delete(key);
		}
		
		// TURN 1
		// turn direction		{left, right }
		turn1Direction.addObject("Left",  -1.0);
		turn1Direction.addObject("Right", 1.0);
		// turn time			{.25, .5, .75, 1.0 }
		turn1TurnTime.addObject("0.25 sec",  .25);
		turn1TurnTime.addObject("0.5 sec",   .5);
		turn1TurnTime.addObject("0.75 sec",  .75);
		turn1TurnTime.addObject("1.0 sec", 1.0);
		// forward time			{.25, .5, .75, 1.0 }
		turn1ForwardTime.addObject("0.25 sec",  .25);
		turn1ForwardTime.addObject("0.5 sec",   .5);
		turn1ForwardTime.addObject("0.75 sec",  .75);
		turn1ForwardTime.addObject("1.0 sec", 1.0);
			
		// TURN 2
		// turn direction		{left, right }
		turn2Direction.addObject("Left",  -1.0);
		turn2Direction.addObject("Right", 1.0);
		// turn time			{.25, .5, .75, 1.0 }
		turn2TurnTime.addObject("0.25 sec",  .25);
		turn2TurnTime.addObject("0.5 sec",   .5);
		turn2TurnTime.addObject("0.75 sec",  .75);
		turn2TurnTime.addObject("1.0 sec", 1.0);
		// forward time			{.25, .5, .75, 1.0 }
		turn2ForwardTime.addObject("0.25 sec",  .25);
		turn2ForwardTime.addObject("0.5 sec",   .5);
		turn2ForwardTime.addObject("0.75 sec",  .75);
		turn2ForwardTime.addObject("1.0 sec", 1.0);
		
		// TURN 3
		// turn direction		{left, right, none }
		turn3Direction.addObject("Left",  -1.0);
		turn3Direction.addObject("Right", 1.0);
		turn3Direction.addObject("None",  0.0);
		// turn time			{.25, .5, .75, 1.0 }
		turn3TurnTime.addObject("0.25 sec",  .25);
		turn3TurnTime.addObject("0.5 sec",   .5);
		turn3TurnTime.addObject("0.75 sec",  .75);
		turn3TurnTime.addObject("1.0 sec", 1.0);
		// forward timed		{1.0, 1.5, 2.0, 3.0 }
		turn3ForwardTime.addObject("1.0 sec", 1.0);
		turn3ForwardTime.addObject("1.5 sec", 1.5);
		turn3ForwardTime.addObject("2.0 sec", 2.0);
		turn3ForwardTime.addObject("3.0 sec", 3.0);
		
		SmartDashboard.putData("Turn 1 Direction", turn1Direction);
		SmartDashboard.putData("Turn 1 Turn Time", turn1TurnTime);
		SmartDashboard.putData("Turn 1 Forward Time", turn1ForwardTime);
		SmartDashboard.putData("Turn 2 Direction", turn2Direction);
		SmartDashboard.putData("Turn 2 Turn Time", turn2TurnTime);
		SmartDashboard.putData("Turn 2 Forward Time", turn2ForwardTime);
		SmartDashboard.putData("Turn 3 Direction", turn3Direction);
		SmartDashboard.putData("Turn 3 Turn Time", turn3TurnTime);
		SmartDashboard.putData("Turn 3 Forward Time", turn3ForwardTime);
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
		//seek.start(); // hack code!!!  real to follow:
		
		//if(true) return;

//		tunableAuto.addSequential(new DriveAndSeek());  // requires DriveTrain
//		tunableAuto.addSequential(new TimedDrive(.5, -.25));  // requires DriveTrain, takes (seconds, power)
//		tunableAuto.addSequential(new PassiveOpen());
		tunableAuto.addSequential(new TimedDrive(.75, .75));  // requires DriveTrain, takes (seconds, power)
		tunableAuto.addSequential(new TunableTurn(turn1TurnTime.getSelected(), turn1Direction.getSelected()));  // requires DriveTrain, takes (seconds, power)
//		tunableAuto.addSequential(new TimedDrive(turn1ForwardTime.getSelected(), -.10));  // requires DriveTrain, takes (seconds, power)
//		tunableAuto.addSequential(new TunableTurn(turn2TurnTime.getSelected(), turn2Direction.getSelected()));  // requires DriveTrain, takes (seconds, power)
//		tunableAuto.addParallel(new PassiveClose());
//		tunableAuto.addSequential(new TimedDrive(turn2ForwardTime.getSelected(), -.10));  // requires DriveTrain, takes (seconds, power)
//		tunableAuto.addSequential(new TunableTurn(turn3TurnTime.getSelected(), turn3Direction.getSelected()));  // requires DriveTrain, takes (seconds, power)
//		tunableAuto.addSequential(new TimedDrive(turn3ForwardTime.getSelected(), -.10));  // requires DriveTrain, takes (seconds, power)		
		
		tunableAuto.start();
		
		
/*		autonomousCommand = chooser.getSelected();

		
		 * String autoSelected = SmartDashboard.getString("Auto Selector",
		 * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
		 * = new MyAutoCommand(); break; case "Default Auto": default:
		 * autonomousCommand = new ExampleCommand(); break; }
		 

		autonomousCommand = seek;
		
		// schedule the autonomous command (example)
		if (autonomousCommand != null)
			autonomousCommand.start();
*/	
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
//		if (autonomousCommand != null)
//			autonomousCommand.cancel();
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

		
		SmartDashboard.putData("Flip", flip);
		SmartDashboard.putBoolean("Drive Forward", driveTrain.isForward());
/*		SmartDashboard.putData("Nav Subsystem", navigation);
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
		
*/

		
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
