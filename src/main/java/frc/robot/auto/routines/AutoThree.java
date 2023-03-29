// FRC2106 Junkyard Dogs - Swerve Drive Base Code

package frc.robot.auto.routines;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.auto.commands.TrajectoryRunner;
import frc.robot.auto.manuals.Backwards;
import frc.robot.commands.elevator.ElevatorSolenoid;
import frc.robot.commands.elevator.ElevatorZero;
import frc.robot.commands.grabber.GrabberSolenoid;
import frc.robot.commands.routines.scoring.ConeTop;
import frc.robot.subsystems.ElevatorSubsystem;
import frc.robot.subsystems.GrabberSubsystem;
import frc.robot.subsystems.SwerveSubsystem;



// Run multiple commands in a routine
public class AutoThree extends SequentialCommandGroup{

    // Routine command constructor
    public AutoThree(SwerveSubsystem swerveSubsystem, ElevatorSubsystem elevatorSubsystem, GrabberSubsystem grabberSubsystem, PIDController xController,
    PIDController yController,  ProfiledPIDController thetaController){

        // Add commands to run
        addCommands(
        // Bring elevator to top
        new ConeTop(elevatorSubsystem, grabberSubsystem),
        new WaitCommand(2.5),
        // Change elevator angle
        new ElevatorSolenoid(elevatorSubsystem),
        new WaitCommand(2),
        // Open grabber
        new GrabberSolenoid(grabberSubsystem),
        new WaitCommand(1),
        // Change elevator angle
        new ElevatorSolenoid(elevatorSubsystem),
        new WaitCommand(1.5),
        // Bring elevator down
        new ElevatorZero(elevatorSubsystem, grabberSubsystem),
        new WaitCommand(1),
        // Set backwards value to 1 meter
       // new SetBackwardsValue(1.0),
        // Start driving onto charge station
        new TrajectoryRunner(swerveSubsystem, xController, yController, thetaController, Backwards.getTrajectory(), Backwards.getTrajectoryConfig()),
        new WaitCommand(2)

        );
    }
}