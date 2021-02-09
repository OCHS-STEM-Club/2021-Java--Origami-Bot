/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
//import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
//import edu.wpi.first.wpilibj.SpeedControllerGroup;
import com.kauailabs.navx.frc.AHRS;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";

  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  private XboxController controller = new XboxController(0);

  private WPI_TalonSRX driveMotorLeft = new WPI_TalonSRX(1);
  private WPI_TalonSRX driveMotorRight = new WPI_TalonSRX(4);

  private WPI_TalonSRX driveMotorLeft1 = new WPI_TalonSRX(2);
  private WPI_TalonSRX driveMotorLeft2 = new WPI_TalonSRX(3);

  private WPI_TalonSRX driveMotorRight1 = new WPI_TalonSRX(5);
  private WPI_TalonSRX driveMotorRight2 = new WPI_TalonSRX(6);

  private SpeedControllerGroup leftGroup = new SpeedControllerGroup(driveMotorLeft, driveMotorLeft1, driveMotorLeft2);
  private SpeedControllerGroup rightGroup = new SpeedControllerGroup(driveMotorRight, driveMotorRight1,
      driveMotorRight2);

  private RadialDrive radialDrive = new RadialDrive(leftGroup, rightGroup);
  private Path path = new Path(radialDrive);

  private AHRS ahrs = new AHRS();

  public Robot() {

    driveMotorLeft.getSensorCollection().setQuadraturePosition(0, 10);
    driveMotorRight.getSensorCollection().setQuadraturePosition(0, 10);
    driveMotorLeft1.follow(driveMotorLeft);
    driveMotorLeft2.follow(driveMotorLeft);
    driveMotorRight1.follow(driveMotorRight);
    driveMotorRight2.follow(driveMotorRight);
    SmartDashboard.putNumber("TurnScaleFactor1", 200);
    SmartDashboard.putNumber("TurnScaleFactor2", 1.05);
    path.addSegments(GeneratedPath.MAIN);
  }

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    ahrs.resetDisplacement();

  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for
   * items like diagnostics that you want ran during disabled, autonomous,
   * teleoperated and test.
   *
   * <p>
   * This runs after the mode specific periodic functions, but before LiveWindow
   * and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {

  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable chooser
   * code works with the Java SmartDashboard. If you prefer the LabVIEW Dashboard,
   * remove all of the chooser code and uncomment the getString line to get the
   * auto name from the text box below the Gyro
   *
   * <p>
   * You can add additional auto modes by adding additional comparisons to the
   * switch structure below with additional strings. If using the SendableChooser
   * make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {

    path.initDrive();

  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {

    path.autoDrive();

    /*
     * double angle = Utils.boundedAngle(ahrs);
     * 
     * double diff = Utils.minAngleDiff(angle, 180);
     * 
     * SmartDashboard.putNumber("diff", diff);
     * 
     * double sign = Math.signum(diff);
     * 
     * double speed = Math.min(Math.abs(diff) / 180 * 3, 0.5);
     * 
     * radialDrive.radialDrive(sign * 1e-8, speed, false);
     * 
     * SmartDashboard.putNumber("speed", speed);
     * 
     * // radialDrive.radialDrive(RadialDrive.STRAIGHT_RADIUS, 0.25, false);
     */
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {

    double forwardAxis = controller.getRawAxis(3) - controller.getRawAxis(2);
    forwardAxis = Utils.scaleAxis(forwardAxis);

    if (controller.getRawButton(1)) {
      forwardAxis = 0.5 * forwardAxis;
    }

    double turnAxis = controller.getRawAxis(0);
    double radius;

    radius = Utils.getRadius(turnAxis);

    if (controller.getPOV() == 270) {

      radius = -24;

    } else if (controller.getPOV() == 90) {

      radius = 24;

    }

    SmartDashboard.putNumber("forwardAxis", forwardAxis);
    SmartDashboard.putNumber("turnAxis", turnAxis);
    SmartDashboard.putNumber("radius", radius);

    SmartDashboard.putNumber("heading", Utils.boundedAngle(ahrs));

    SmartDashboard.putNumber("x", ahrs.getDisplacementX());
    SmartDashboard.putNumber("y", ahrs.getDisplacementY());
    SmartDashboard.putNumber("x", ahrs.getDisplacementZ());

    radialDrive.radialDrive(radius, forwardAxis);

  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
