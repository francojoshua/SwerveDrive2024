// FRC2106 Junkyard Dogs - Swerve Drive Base Code

package frc.robot;
import com.pathplanner.lib.PathConstraints;
import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.filter.Debouncer.DebounceType;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.auto.commands.TrajectoryRunner;
import frc.robot.auto.commands.TrajectoryWeaver;
import frc.robot.auto.manuals.Forward2M;
import frc.robot.auto.routines.AutoOne;
import frc.robot.auto.routines.TestRoutine;
import frc.robot.commands.ElevatorApriltag;
import frc.robot.commands.ElevatorHome;
import frc.robot.commands.ElevatorManual;
import frc.robot.commands.ElevatorMeters;
import frc.robot.commands.GrabberSolenoid;
import frc.robot.commands.ResetOdometry;
import frc.robot.commands.SwerveAlignBasic;
import frc.robot.commands.SwerveJoystick;
import frc.robot.commands.SwerveRotator;
import frc.robot.commands.SwerveThrottledJoystick;
import frc.robot.subsystems.ElevatorSubsystem;
import frc.robot.subsystems.GrabberSubsystem;
import frc.robot.subsystems.SwerveSubsystem;
import frc.robot.subsystems.VisionSubsystem;
import frc.robot.util.Constants;
import frc.robot.util.Transmitter;
import frc.robot.util.Constants.AutoConstants;
import frc.robot.util.Constants.IOConstants;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

// Ignore unused variable warnings
@SuppressWarnings("unused")

public class RobotContainer {

  // Converted to 2023 wpiblib

  //------------------------------------O-B-J-E-C-T-S-----------------------------------//

  // Create joysticks
  //private final Joystick leftJoystick = new Joystick(IOConstants.kLeftJoystick);
  //private final Joystick rightJoystick = new Joystick(IOConstants.kRightJoystick);

  // Create swerve subsystem
  private final SwerveSubsystem swerveSubsystem = new SwerveSubsystem();
  
  // Create vision subsystem
  private final VisionSubsystem visionSubsystem = new VisionSubsystem();

  // Create grabber subsystem
  private final GrabberSubsystem grabberSubsystem = new GrabberSubsystem();

  // Create elevator subsystem
  private final ElevatorSubsystem elevatorSubsystem = new ElevatorSubsystem();

  // Create PID controllers for trajectory tracking
  public final PIDController xController = new PIDController(AutoConstants.kPXController, 0, 0);
  public final PIDController yController = new PIDController(AutoConstants.kPYController, 0, 0);
  public final ProfiledPIDController thetaController = new ProfiledPIDController(AutoConstants.kPThetaController, AutoConstants.kIThetaController, AutoConstants.kDThetaController, AutoConstants.kThetaControllerConstraints);

  // Create a non profiled PID controller for path planner
  private final PIDController ppThetaController = new PIDController(AutoConstants.kPThetaController, 0, 0);

  // Create xbox controller
  private final XboxController xbox = new XboxController(3);

  // Create tx16s transmitter
  private final Joystick tx16s = new Joystick(4);

  //--------------------------P-A-T-H-S----------------------------//

  // Manual path 
  private final PathPlannerTrajectory pathOne = PathPlanner.loadPath("forward1M", new PathConstraints(0.25, 0.25)); 
  private Command autoForward = new TrajectoryWeaver(swerveSubsystem,xController,yController,ppThetaController, pathOne, true);

  // Routine
  private Command autoOne = new AutoOne(swerveSubsystem, xController, yController, ppThetaController);

  private SendableChooser<Command> autoChooser = new SendableChooser<>();


  //------------------------------------C-O-N-S-T-R-U-C-T-O-R----------------------------//

  public RobotContainer(){

    // Set swerve subsystem default command to swerve joystick with respective joystick inputs
    // Joystick Numbers 0 = LEFT : 1 = RIGHT
    // Joystick Axises: 0 = left/right : 1 = forward/backwards : 2 = dial
    // OLD-> Transmitter Axises: 0 = roll : 1 = pitch : 2 = throttle : 3 = yaw : 4 = analog1 : 5 = analog2

  //>--------------T-R-A-N-S-----------------//
    
    swerveSubsystem.setDefaultCommand(new SwerveJoystick(swerveSubsystem,
    () -> tx16s.getRawAxis(0), // X-Axis
    () -> -tx16s.getRawAxis(1), // Y-Axis
    () -> tx16s.getRawAxis(3), // R-Axis
    () -> tx16s.getRawButton(0), // Field oriented -does nothing right now
    () -> swerveSubsystem.getHeading(), // Navx heading
    () -> tx16s.getRawButton(4))); // Flick offset button, should be toggle!
    
    elevatorSubsystem.setDefaultCommand(new ElevatorManual(elevatorSubsystem,
    () -> xbox.getRawAxis(5)));

  //>----------S-E-N-D-E-R----------<//

  // Add auto commands to selector
    autoChooser.setDefaultOption("Forward", autoForward);
    autoChooser.addOption("AutoOne", autoOne);

  // Add auto chooser to smart dashboard
    SmartDashboard.putData(autoChooser);
  
  //>------------------------------<//

    // Run button binding method
    configureButtonBindings();

  }

  //------------------------------------D-E-B-U-G------------------------------------//

  private double zeroFunct(){return 0;}

  private boolean trueFunct(){return true;}

  //------------------------------------B-U-T-T-O-N-S------------------------------------//

  // Create button bindings
  private void configureButtonBindings(){

    //--------------// Grabber Bindings

    // Open
    new JoystickButton(tx16s, 2).onTrue(new GrabberSolenoid(grabberSubsystem));
    // Close
    new JoystickButton(tx16s, 2).onFalse(new GrabberSolenoid(grabberSubsystem));

    //--------------// Elevator Bindings

    // Homing
    new JoystickButton(xbox, 1).onTrue(new ElevatorHome(elevatorSubsystem));
    // Apriltag
    new JoystickButton(xbox, 2).onTrue(new ElevatorApriltag(elevatorSubsystem, visionSubsystem));
    // Meters
    new JoystickButton(xbox, 3).onTrue(new ElevatorMeters(elevatorSubsystem, 1.0));

    //--------------// Auto Bindings

    // Apriltag
    new JoystickButton(tx16s, 8).onTrue(new SwerveAlignBasic(swerveSubsystem, visionSubsystem,
      () -> swerveSubsystem.getHeading(), () -> tx16s.getRawButton(8), () -> tx16s.getRawAxis(5)));
    
    // Run autonmous command during teleop
    //new JoystickButton(tx16s, 3).onTrue(new TrajectoryWeaver(swerveSubsystem,xController,yController,ppThetaController, pathOne, true));

  }

  //------------------------------------R-E-F-E-R-R-E-R-S------------------------------------//

    public void containerResetAllEncoders(){ swerveSubsystem.resetAllEncoders();}

  //------------------------------------A-U-T-O-N-O-M-O-U-S------------------------------------//
  
  // Return the command to run during auto
  public Command getAutonomousCommand(){

    // Command to run
    Command autoCommand = null;

    return autoChooser.getSelected();
  }

}