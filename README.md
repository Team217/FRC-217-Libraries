# Team 217 - FRC Libraries

## Installation

1) Download the `.jar` files for the latest release [here](https://github.com/Team217/FRC-217-Libraries/releases/latest/). If you do not download both files, then the Java documentation (information on how to use the classes and methods) will be unavailable.
2) If it does not exist, reate a new `libs` folder in the project directory (NOT within the `src` folder). Put the `.jar` files there.
3) In `build.gradle`, find `dependencies { }` (near the bottom) and add the following line to the end of the list if it does not exist: `compile fileTree(dir: 'libs', include: ['*.jar'])`

## Current Classes

Classes currently include:

- `Converter`
  - converts between encoder ticks and either angles or distances
- `Num`
  - numerical operations, such as `deadband()` and `isWithinRange()`
- `Logger`
  - logs data
- `BooleanOneShot`
  - manages a boolean one-shot, which flips a boolean flag when a trigger switches from low to high
- `motion.AccelController`
  - controls acceleration of a velocity
- `motion.MotionProfiler`
  - applies motion profiling to a velocity
- `motion.MotionController`
  - applies PID and motion profiling to control motion
- `ctre.WPI_TalonSRX`
  - adds extra functions to the TalonSRX motor controller
- `ctre.WPI_TalonFX`
  - adds extra functions to the TalonFX motor controller
- `ctre.PigeonIMU`
  - adds extra functions to the PigeonIMU
- `ctre.JohnsonPLGEncoder`
  - manages pulses of the Johnson Electric PLG Hall Sensors as a relative encoder
- `rev.CANSparkMax`
  - adds extra functions to the SparkMax motor controller
- `wpi.AnalogGyro`
  - adds extra functions to the Analog Gyro
