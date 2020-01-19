# Team 217 - FRC Libraries

Classes currently include:

- `Converter`
  - converts between encoder ticks and either angles or distances
- `Num`
  - numerical operations, such as `deadband()` and `isWithinRange()`
- `Logger`
  - logs data
- `AccelController`
  - Applies acceleration control to velocity
- `ctre.WPI_TalonSRX`
  - adds extra functions to the TalonSRX motor controller
- `ctre.PigeonIMU`
  - adds extra functions to the PigeonIMU
- `rev.CANSparkMax`
  - adds extra functions to the SparkMax motor controller
- `wpi.AnalogGyro`
  - adds extra functions to the Analog Gyro

## Installation

1) Download the `.jar` files for the latest release [here](https://gitlab.com/team217/traj-217-libraries/-/releases). If you do not download both files, then the Java documentation (information on how to use the classes and methods) will be unavailable.
2) If it does not exist, reate a new `libs` folder in the project directory (NOT within the `src` folder). Put the `.jar` files there.
3) In `build.gradle`, find `dependencies { }` (near the bottom) and add the following line to the end of the list if it does not exist: `compile fileTree(include: ['*.jar'], dir: 'libs')`
