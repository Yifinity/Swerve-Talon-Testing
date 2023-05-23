// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

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

  private final TalonSRX module0TurnMotor = new TalonSRX(0);
  private final Spark module0DriveMotor = new Spark(0);

  private final TalonSRX module1TurnMotor = new TalonSRX(1);
  private final Spark module1DriveMotor = new Spark(1);
    
  private final TalonSRX module2TurnMotor = new TalonSRX(2);
  private final Spark module2DriveMotor = new Spark(2);

  private final TalonSRX module3TurnMotor = new TalonSRX(3);
  private final Spark module3DriveMotor = new Spark(3);
  

  private final double gearMotorRatio = (71); // Gear ratio for PG71 ~ 71
  private final double tooth40GearRatio = 1/40;

  private final double wheelDiameterMeters = Units.inchesToMeters(4); // Convert 4 inches to meters. 

  private final double turnEncoderRatio = gearMotorRatio * tooth40GearRatio;
  private final double clicks2Rotation = turnEncoderRatio / 4096; // 4096 clicks is registered as a rotation. 
  private final double clicks2RotationPure = 1/71 * 1/40 * 1/4096;
  private final double turnMaxRPM = 75;


  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    
    SmartDashboard.putData("Auto choices", m_chooser);
    System.out.println(clicks2RotationPure);

    module0DriveMotor.set(0);
    module1DriveMotor.set(0);
    module2DriveMotor.set(0);
    module3DriveMotor.set(0);

    // module1TurnMotor.configClearPositionOnQuadIdx(true, 100);
    module0TurnMotor.set(ControlMode.PercentOutput, 0);
    module1TurnMotor.set(ControlMode.PercentOutput, 0);
    module2TurnMotor.set(ControlMode.PercentOutput, 0);
    module3TurnMotor.set(ControlMode.PercentOutput, 0);

    module3TurnMotor.setSelectedSensorPosition(0, 0, 100);
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
    // module0DriveMotor.set(-xbox.getLeftY());
    // module1DriveMotor.set(-xbox.getLeftY());
    // module2DriveMotor.set(-xbox.getLeftY());
    module3DriveMotor.set(-xbox.getLeftY());

    // module0TurnMotor.set(ControlMode.PercentOutput, xbox.getLeftX());
    // module1TurnMotor.set(ControlMode.PercentOutput, xbox.getLeftX());
    // module2TurnMotor.set(ControlMode.PercentOutput, xbox.getLeftX());
    module3TurnMotor.set(ControlMode.PercentOutput, xbox.getLeftX());

    SmartDashboard.putNumber("Mod 0 Quad/MagEnc", module0TurnMotor.getSelectedSensorPosition(0));
    SmartDashboard.putNumber("Mod 0 Quad/MagEnc Rotation", module0TurnMotor.getSelectedSensorPosition(0));
   
   
    SmartDashboard.putNumber("Mod 1 Velocity", module1TurnMotor.getSelectedSensorVelocity(0));
    SmartDashboard.putNumber("Mod 1 Quad/MagEnc", module1TurnMotor.getSelectedSensorPosition(0));
    SmartDashboard.putNumber("Mod 1 Quad/MagEnc Rotation", module1TurnMotor.getSelectedSensorPosition(0));
    
    SmartDashboard.putNumber("Mod 2 Quad/MagEnc", module2TurnMotor.getSelectedSensorPosition(0));
    SmartDashboard.putNumber("Mod 2 Quad/MagEnc Rotation", module2TurnMotor.getSelectedSensorPosition(0));

    SmartDashboard.putNumber("Mod 3 Quad/MagEnc", module3TurnMotor.getSelectedSensorPosition(0));
    SmartDashboard.putNumber("Mod 3 Quad/MagEnc Rotation", (module3TurnMotor.getSelectedSensorPosition(0) * (71 * 0.125)));


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
