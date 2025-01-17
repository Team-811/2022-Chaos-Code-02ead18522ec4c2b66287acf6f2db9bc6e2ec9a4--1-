package frc.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import frc.robot.VisionProcessing.VisionTargeting.CatFollow;
import frc.robot.VisionProcessing.VisionTargeting.Hub.LimelightAim;
import frc.robot.VisionProcessing.VisionTargeting.Hub.LimelightAimY;
// import frc.robot.VisionProcessing.Limelight;
import frc.robot.commands.DrivingCommand;
import frc.robot.commands.Auto.BackwardsAuto;
import frc.robot.commands.Auto.BackwardsLOneBallAuto;
import frc.robot.commands.Auto.BackwardsOneBallAuto;
import frc.robot.commands.Auto.ForwardsAuto;
import frc.robot.commands.Climber.BackClimberStep;
import frc.robot.commands.Climber.ClimberCommand;
import frc.robot.commands.Climber.ClimberStep;
import frc.robot.commands.Intake.Motors.IntakeForward;
// import frc.robot.commands.Intake.Motors.IntakeReverse;
import frc.robot.commands.Intake.Motors.IntakeStop;
import frc.robot.commands.Intake.Pneumatics.IntakeToggle;
import frc.robot.commands.Shooter.Shoot;
import frc.robot.commands.Shooter.ShooterStop;
import frc.robot.commands.Shooter.SlowShooter;
import frc.robot.controllers.BobXboxController;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj2.command.Command;


import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  private static final Drivetrain drivetrain = new Drivetrain(); 
  private static final Intake intake = new Intake();
  private static final Shooter shooter = new Shooter();
  private static final Climber climber = new Climber();

  Compressor pcmCompressor = new Compressor(PneumaticsModuleType.CTREPCM);
  public static BobXboxController driveController;
  public static BobXboxController operatorController;

  SendableChooser<Command> m_chooser = new SendableChooser<>();


  
  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    drivetrain.setDefaultCommand(new DrivingCommand(drivetrain));
    climber.setDefaultCommand(new ClimberCommand(climber));
    configureButtonBindings();
    
    //Autonomus selection group 
    SmartDashboard.putData("Auto mode", m_chooser);

    m_chooser.setDefaultOption("back and shoot", new BackwardsOneBallAuto(drivetrain, intake, shooter));
    m_chooser.addOption("🍆 lower hub and back", new BackwardsLOneBallAuto(drivetrain, intake, shooter));
    m_chooser.addOption("go back", new BackwardsAuto(drivetrain));
    m_chooser.addOption("go forward", new ForwardsAuto(drivetrain));
    m_chooser.addOption("Experimenal Pathweaver", drivetrain.generateAutoPath("paths/test_path.wpilib.json"));
    m_chooser.addOption("do nothing :(", null);

  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    driveController = new BobXboxController(0, .3, .3);
    driveController.aButton.whenPressed(new IntakeToggle(intake));
    driveController.xButton.whileHeld(new LimelightAim(drivetrain));
    driveController.yButton.whileHeld(new LimelightAimY(drivetrain));
    driveController.bButton.whileHeld(new CatFollow(drivetrain));

    operatorController = new BobXboxController(1, .3, .3);
    operatorController.xButton.whenPressed(new Shoot(shooter, intake));
    operatorController.yButton.whileHeld( new IntakeForward(intake));
    operatorController.yButton.whenReleased( new IntakeStop(intake));
    operatorController.bButton.whenPressed( new ClimberStep(climber));
    // operatorController.aButton.whileHeld(new SlowShooter(shooter));
    // operatorController.aButton.whenReleased(new ShooterStop(shooter));
    operatorController.aButton.whenPressed(new BackClimberStep(climber));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public void resetClimber(){
    climber.resetStep();
  }
  
  public Command getAutonomousCommand() {
    return m_chooser.getSelected();
  }

  public void zeroSensors(){
    drivetrain.zeroSensors();
  }

  public static void updateSmartdashboard() {
    drivetrain.outputSmartdashboard();
    shooter.outputSmartdashboard();
    intake.outputSmartdashboard();
  }
}
