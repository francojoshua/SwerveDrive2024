// FRC2106 Junkyard Dogs - Swerve Drive Base Code

package frc.robot.commands.elevator;
import frc.robot.subsystems.ElevatorSubsystem;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.CommandBase;

/** An example command that uses an example subsystem. */
public class ElevatorUpSetpoint extends CommandBase {

  private ElevatorSubsystem subsystem;
  private Supplier<Double> input;

  public ElevatorUpSetpoint(ElevatorSubsystem subsystem, Supplier<Double> input) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.subsystem = subsystem;
    this.input = input;
    addRequirements(subsystem);

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    // Set the elevator to the desired meters
    if(input.get() > 0.1){
      subsystem.addElevatorSetpoint(0.125);
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
