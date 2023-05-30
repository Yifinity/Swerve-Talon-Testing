// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";

  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  private final XboxController xbox = new XboxController(3);

  private final TalonSRX frontLeftTurn = new TalonSRX(1);
  private final Spark frontLeftDrive = new Spark(1);

  private final TalonSRX frontRightTurn = new TalonSRX(3);
  private final Spark frontRightDrive = new Spark(3);
    
  private final TalonSRX backLeftTurn = new TalonSRX(2);
  private final Spark backLeftDrive = new Spark(2);

  private final TalonSRX backRightTurn = new TalonSRX(0);
  private final Spark backRightDrive = new Spark(0);
  

  private final double kClicks2RotationPure = 1656.66667; // 7 * 71 * (40/48) * 4;
  // private final double turnMaxRPM = 75;


  private final PIDController turnController = new PIDController(0.000602, 0, 0);

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    
    SmartDashboard.putData("Auto choices", m_chooser);
    // System.out.println(kclicks2RotationPure);

    frontLeftDrive.set(0);
    frontRightDrive.set(0);
    backLeftDrive.set(0);
    backRightDrive.set(0);

    // module1TurnMotor.configClearPositionOnQuadIdx(true, 100);
    frontLeftTurn.set(ControlMode.PercentOutput, 0);
    frontRightTurn.set(ControlMode.PercentOutput, 0);
    backLeftTurn.set(ControlMode.PercentOutput, 0);
    backRightTurn.set(ControlMode.PercentOutput, 0);

    frontLeftTurn.setSelectedSensorPosition(0, 0, 100);
    frontRightTurn.setSelectedSensorPosition(0, 0, 100);
    backLeftTurn.setSelectedSensorPosition(0, 0, 100);
    backRightTurn.setSelectedSensorPosition(0, 0, 100);
   
    // frontLeftTurn.setConver
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // frontDriveMotor.set(-xbox.getLeftY());
    // module1DriveMotor.set(-xbox.getLeftY());
    // module2DriveMotor.set(-xbox.getLeftY());
    // module3DriveMotor.set(-xbox.getLeftY());

    // module0TurnMotor.set(ControlMode.PercentOutput, xbox.getLeftX());
    // module1TurnMotor.set(ControlMode.PercentOutput, xbox.getLeftX());
    // module2TurnMotor.set(ControlMode.PercentOutput, xbox.getLeftX());
    
    frontLeftTurn.set(ControlMode.PercentOutput, xbox.getLeftX());
    double leftTurnAmount = frontLeftTurn.getSelectedSensorPosition();
    leftTurnAmount /= kClicks2RotationPure;
    SmartDashboard.putNumber("Front Left", leftTurnAmount);
    SmartDashboard.putData("PID Controller", turnController);
    
    
    // SmartDashboard.putNumber("Mod 3 Quad/MagEnc Rotation", (module3TurnMotor.getSelectedSensorPosition(0) * (71 * 0.125)));

    // Reset Controller
    if(xbox.getLeftBumperPressed()){
      frontLeftTurn.setSelectedSensorPosition(0, 0, 100);
    }

    if(xbox.getYButton()){
      frontLeftTurn.set(ControlMode.PercentOutput, turnController.calculate(leftTurnAmount, leftTurnAmount));
    }

  }

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {}

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {}

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
