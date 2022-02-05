package frc.robot.commands.Drivetrain;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;

public class DriveStop extends CommandBase {
    private Drivetrain requiredSubsystem;
  
  public DriveStop(Drivetrain m_SubsystemBase) {
    requiredSubsystem = m_SubsystemBase;
    addRequirements(requiredSubsystem);

  }

  @Override
  public void initialize() {}

  @Override
  public void execute() { 
       requiredSubsystem.stopRobot();

  }

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return true;
  }

}
