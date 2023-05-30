package frc.robot.subsystems;
import com.ctre.phoenix.motorcontrol.ControlMode;
// import com.ctre.phoenix.motorcontrol.TalonSRXSimCollection;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SwerveModule {

    private final Spark driveMotor; // Change to Spark
    private final TalonSRX turningMotor; // Change to Talon

    private final PIDController turningPidController; // Add a controller for our turning motor. 
    
    
//   private final double kClicks2RotationPure = 1656.66667; // 7 * 71 * (40/48) * 4;
//   private final double kRadians = 2 * Math.PI;
//   private final double kClicks2RotationRad = kClicks2RotationPure/kRadians; // 7 * 71 * (40/48) * 4;
//   
    public static final double kTurnEncoderRatio = 1656.66667; // 7 * 71 * (40/48) * 4;
    public static final double kTurningEncoderRot2Rad = kTurnEncoderRatio / Units.degreesToRadians(360);
    public static final double kTurningEncoderUnitP100ms2RadPerSec = kTurningEncoderRot2Rad * 10; // Talon SRX reads velocity in units/100ms. https://v5.docs.ctr-electronics.com/en/stable/ch14_MCSensor.html 

    // public static final double kPhysicalMaxDriveSpeedMetersPerSecond = Units.feetToMeters(11.5); // According to https://www.andymark.com/products/swerve-and-steer
    
    // Constructor where we update everything we need to create a module. 
    public SwerveModule(int SparkPort, int TalonId, boolean driveMotorReversed, boolean turningMotorReversed){
        // Define motors to be those we created with passed in ports. 
        driveMotor = new Spark(SparkPort);
        turningMotor = new TalonSRX(TalonId);

        // Set motors to be inversed if we told it to. 
        driveMotor.setInverted(driveMotorReversed);
        turningMotor.setInverted(turningMotorReversed);
    
        turningPidController = new PIDController(0.3373, 0, 0); 
        turningPidController.enableContinuousInput(-Math.PI, Math.PI); // Basically controller moves to find the shortest path to a target in a circle - a circle's diameter is 2pi. 
        
        resetEncoder(); // Call our method to reset our encoder when we create the module. 
    };

    // Reset Encoders
    public void resetEncoder() {
        // NOTE: The ms means that the code will throw an error after desired ms - at 100 ms in this example. 
        turningMotor.setSelectedSensorPosition(0, 0,100);  // pidId 0 is simply the quadratic encoder - can be set with the phoenix tuner. 
    }


    // Return Turning Motor Pos
    public double getTurningPosition(){
        // Return Talon Turning Position
        return (turningMotor.getSelectedSensorPosition(0) / kTurningEncoderRot2Rad); 
    }

    // Turning Velocity
    public double getTurningVelocity(){
        // Return Talon Turning Position
        // Watch the time. 
        // pidId 0 is simply the quadratic encoder - can be set with the phoenix tuner. 
        return (turningMotor.getSelectedSensorVelocity(0) / kTurningEncoderUnitP100ms2RadPerSec);    // Bozo watch 
    }

    // Create a rotation 2d that symbolizes the 2d rotation state of the wheel. 
    public Rotation2d rotationState(){
        // We nmay need a drive encoder
        return new Rotation2d(getTurningPosition());
    }


    public void setTurningPID(double radians){
        double turnOutput = turningPidController.calculate(getTurningPosition(), radians);
        turnOutput = turnOutput > 1 ? 1 : turnOutput;
        turningMotor.set(ControlMode.PercentOutput, turnOutput);
        SmartDashboard.putNumber("Swerve[" + driveMotor.getChannel() + "] Turn Input", turnOutput); 
    }

    public void runDrive(double speed){        
        driveMotor.set(speed);
        SmartDashboard.putNumber("Swerve[" + driveMotor.getChannel() + "] Drive Input", speed); 
    }

    public void updateShuffleboard(){
        // Send SmartDashboard info. 
        SmartDashboard.putNumber("Swerve[" + driveMotor.getChannel() + "] Encoder Reading ", getTurningPosition());
        // SmartDashboard.putData("Swerve[" + driveMotor.getChannel() + "] PID Controller ", turningPidController); 

    }

    // public void setDesiredState(SwerveModuleState state){
    //     //  Make sure that we're actually wanting to change the speed - if we're just letting go of the controller, we don't need to get to zero
    //     if(Math.abs(state.speedMetersPerSecond) < 0.001){
    //         // If the speed is too insignificant - don't bother
    //         stopMotors();
    //         return; // Exit function
    //     }
    //     state = SwerveModuleState.optimize(state, rotationState()); // Have the passed in state get translated so we now just need the shortest possible path for the wheel to rotate. 
       
    //     // NOT USED
    //     // driveMotor.set(state.speedMetersPerSecond / kPhysicalMaxDriveSpeedMetersPerSecond); // Give us a percentage speed for the spark to go to.  - Make sure it doesn't cross 1!
      

    //     double turningSpeed = turningPidController.calculate(getTurningPosition(), state.angle.getRadians());
    //     turningMotor.set(ControlMode.PercentOutput, turningSpeed > 1 ? 1.0 : turningSpeed); // If turning speed is greater than 1, set it to 1, if not, set to turning speed. 
    //     turningSpeed =  turningSpeed > 1 ? 1.0 : turningSpeed;
        
    //     SmartDashboard.putString("Swerve[" + driveMotor.getChannel() + "] state:", state.toString()); // Give us the module debug info. .
    //     SmartDashboard.putNumber("Swerve[" + driveMotor.getChannel() + "] Speed ", state.speedMetersPerSecond / kPhysicalMaxDriveSpeedMetersPerSecond); 
    //     SmartDashboard.putNumber("Swerve[" + driveMotor.getChannel() + "] turn", state.angle.getDegrees()); 

        
    // }

    public void stopMotors(){
        // driveMotor.set(0);
        driveMotor.stopMotor();
        turningMotor.set(ControlMode.PercentOutput, 0);
    }


}
