# Team 217 - FRC Libraries

Classes currently include:
- `Converter` (converts between encoder ticks and either angles or distances)
- `Num` (numerical operations, such as `deadband()` and `isWithinRange()`)
- `Accel`
- `ctre.WPI_TalonSRX`
- `ctre.PigeonIMU`
- `rev.CANSparkMax`
- `pid.PID`
- `pid.APID` (applies acceleration to a `pid.PID` object)

## Installation
1) Download the `.jar` files for the latest release [here](https://github.com/Team217/FRC-217-Libraries/releases). If you do not download both files, then the Java documentation (information on how to use the classes and methods) will be unavailable.
2) Create a new `libs` folder in the project directory (NOT within the `src` folder), and put the `.jar` files there.
3) In `build.gradle`, find `dependencies { }` (near the bottom) and add the following line to the end of the list: `compile fileTree(include: ['*.jar'], dir: 'libs')`
