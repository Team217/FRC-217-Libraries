# Team 217 - FRC Libraries

Classes currently include:
- `Converter` (converts between encoder ticks and either angles or distances)
- `Miscellaneous` (other functions; currently just a deadband function)
- `ctre.WPI_TalonSRX`
- `ctre.PigeonIMU`
- `pid.PID`

## Installation
1) Download the `.jar` files for the latest release [here](https://github.com/Team217/217-Libraries/releases)
2) Create a new `libs` folder in the project directory (NOT within the `src` folder), and put the `.jar` files there.
3) In `build.gradle`, find `dependencies {` (near the bottom) and add the following line to the end of the list: `compile fileTree(include: ['*.jar'], dir: 'libs')`
