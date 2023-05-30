// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;

public class SwerveSubsystem extends SubsystemBase {

  private final XboxController xbox = Robot.xbox;

  private final SwerveModule frontLeft = new SwerveModule(
    0, 
    0,
    false, 
    false
  );

  private final SwerveModule frontRight = new SwerveModule(
    1, 
    1,
    false, 
    false
  );

  private final SwerveModule backLeft = new SwerveModule(
    2, 
    2,
    false, 
    false
  );


  private final SwerveModule backRight = new SwerveModule(
    3, 
    3,
    false, 
    false
  );


  public SwerveSubsystem() {
    // Reset Encoders
    frontLeft.resetEncoder();
    frontRight.resetEncoder();
    backLeft.resetEncoder();
    backRight.resetEncoder();
  }

  public void resetEncoders(){
    // Reset encoders. 
    frontLeft.resetEncoder();
    frontRight.resetEncoder();
    backLeft.resetEncoder();
    backRight.resetEncoder();
  }

  public void runMotors(){
    frontLeft.runDrive(-xbox.getLeftY());
  }

  public void goToPos(double radians){
    frontLeft.setTurningPID(radians);
  }

  @Override
  public void periodic() {
    frontLeft.updateShuffleboard();
    // This method will be called once per scheduler run
  }
}
