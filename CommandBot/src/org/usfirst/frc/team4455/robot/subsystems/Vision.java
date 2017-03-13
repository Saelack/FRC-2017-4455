package org.usfirst.frc.team4455.robot.subsystems;

import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Range;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.AxisCamera;
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode.PixelFormat;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Vision extends Subsystem {
	CvSink cvsink;
	Mat source = new Mat();
	
	AxisCamera passiveAxis;
	AxisCamera activeAxis;
	UsbCamera passiveUSB;
	
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	public void setupCamera() {
		System.out.println("Vision.setupCamera()");
		activeAxis = CameraServer.getInstance().addAxisCamera("Active Axis", "10.44.55.51");
		activeAxis.setFPS(10);
//		passiveAxis = CameraServer.getInstance().addAxisCamera("Passive Axis", "10.44.55.26");
		
//		AxisCamera activeAxis = new AxisCamera("Active Axis Camera", "10.44.55.51");
		passiveAxis = new AxisCamera("Passive Axis Camera", "10.44.55.26");
		passiveAxis.setPixelFormat(PixelFormat.kMJPEG);
		passiveAxis.setResolution(240, 320);
		passiveAxis.setFPS(10);
		CameraServer.getInstance().startAutomaticCapture(passiveAxis);
		
//		new UsbCamera("Active", 0).setResolution(160, 120);
//		new UsbCamera("Passive", 1).setResolution(160, 120);
		
		CameraServer.getInstance().startAutomaticCapture("Active USB", 0).setFPS(10);
		(passiveUSB = CameraServer.getInstance().startAutomaticCapture("Passive USB", 1)).setFPS(10);
		
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public Point getTargetCenter() {
    	int centerX = -1;
    	int centerY = -1;
    	
    	Point center = null;
		if(cvsink == null) {	
			cvsink = CameraServer.getInstance().getVideo("Passive Axis Camera");
		}
		
        cvsink.grabFrame(source);
        
       // System.out.println("getTargetCenter().source = "+source.width()+","+source.height());
        process(source);
        System.out.println("Contour Report");
        if (!filterContoursOutput.isEmpty()) {
        	int xtotal = 0;
        	int ytotal = 0;
        	int weighttotal = 0;
        
        	for (int i=0;i<filterContoursOutput.size();i++) {
                Rect r = Imgproc.boundingRect(filterContoursOutput.get(i));
                centerX = r.x + (r.width / 2);
                centerY = r.y + (r.height / 2);
                xtotal += centerX*r.height;
                ytotal += centerY*r.height;
                weighttotal += r.height;
                
              System.out.println("Contour #"+i+" center: "+centerX);
			}
        	centerX = (xtotal/weighttotal);
        	centerY = (ytotal/weighttotal);
        	
        	center = new Point(centerX, centerY);
        	System.out.println("center " + center);
        }
        
        return center;
    }
    
	//Outputs
	private ArrayList<MatOfPoint> filterContoursOutput = new ArrayList<MatOfPoint>();

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	static double[] hslThresholdHue = {70.0, 100.0};
	static double[] hslThresholdSat = {40.0, 255.0};
	static double[] hslThresholdLum = {128.0, 255.0};

	/*  These were the values used for HSV, but we want to use HSL...
	static double[] hsvThresholdHue = {70.0, 130.0};
	static double[] hsvThresholdSaturation = {26.0, 255.0};
	static double[] hsvThresholdValue = {190.0, 255.0};
	*/
	
	public void process(Mat source0) {
		Mat hsvThresholdOutput = new Mat();
		
		System.out.println("Before HSL process");
		// Run HSL conversion on a masked out image
		Imgproc.cvtColor(new Mat(source0, new Range(200, 320)), hsvThresholdOutput, Imgproc.COLOR_BGR2HLS);
		Core.inRange(hsvThresholdOutput, new Scalar(hslThresholdHue[0], hslThresholdLum[0], hslThresholdSat[0]),
			new Scalar(hslThresholdHue[1], hslThresholdLum[1], hslThresholdSat[1]), hsvThresholdOutput);

		System.out.println("Before contour process");
		// Find Contours:
		ArrayList<MatOfPoint> findContoursOutput = new ArrayList<MatOfPoint>();
		Imgproc.findContours(hsvThresholdOutput, findContoursOutput, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
		
		System.out.println("Before contour filter process");
		// Filter Contours:
		filterContoursOutput.clear();
		double filterContoursMinArea = 2.0;
		for (int i = 0; i < findContoursOutput.size(); i++) {
			final MatOfPoint contour = findContoursOutput.get(i);
			final double area = Imgproc.contourArea(contour);
			if (area < filterContoursMinArea) continue;
			filterContoursOutput.add(contour);
		}
		
		System.out.println("Process complete");
	}
}

