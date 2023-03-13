// FRC2106 Junkyard Dogs - Swerve Drive Base Code

package frc.robot.commands.routines.util;
import frc.robot.subsystems.GrabberSubsystem;
import frc.robot.util.LightStrip;
import edu.wpi.first.wpilibj2.command.CommandBase;

/** An example command that uses an example subsystem. */
public class SetLedBlue extends CommandBase {

  private LightStrip ledStrip;

  public SetLedBlue(LightStrip ledStrip) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(ledStrip);
    this.ledStrip = ledStrip;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    ledStrip.setBlue();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
