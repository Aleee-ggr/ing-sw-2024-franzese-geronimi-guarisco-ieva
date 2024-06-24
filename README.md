# Prova Finale Ingegneria del Software - AA 2023-2024  
### Politecnico di Milano, Group 11

## Team Members

- [Franzese Samuele](https://github.com/SamueleFranzese02)
- [Geronimi Gloria](https://github.com/gloriageronimi)
- [Guarisco Alessio](https://github.com/Aleee-ggr)
- [Ieva Daniele](https://github.com/daniele-ieva)



## Functionalities 

Game Specific:  
- Full Rule-set

Game Agnostic:  
- Server and Client implemented in JavaRMI and Socket
- TUI without external libraries
- GUI in JavaFX
  
Advanced Features: 
  - simultaneous games
  - disconnection resiliency
  - game chat

## Compile and Execute

The group worked with a Unix-first mindset, the game is fully reliable on Linux and MacOS but for Windows it may not work as expected.
### Requirements:
to work correctly the project needs:  
- `Java 22` or above with javaFX  
- `mvn` and `shade` for compiling
- the `trattatello` font
  
### Windows pre-setup:
to run on Windows you should use the new [windows terminal](https://github.com/microsoft/terminal), default terminal from windows 11.  
to run the TUI you should:
- enable UNICODE characters in the terminal
- use a font that support UTF-8
- adjust the font size depending on your screen

the safest way to enable UTF-8 on your windows terminal is to enable `Use Unicode UTF-8 for worldwide language support` in your OS language settings.  
on some systems typing on the terminal before running the game:
```
chcp 65001
```
should also work.  
Because those steps may vary depending on your configurations the safe bet is always using WSL with Ubuntu or a UNIX-based system.

### macOS apple silicon pre-setup:
If you are running a MacBook with apple silicon you should use a supported version of javaFX.  
we recomend [azul](https://www.azul.com/downloads/?version=java-22&os=macos&architecture=arm-64-bit&package=jdk-fx#zulu) sdk version 22

### Every OS:
to compile the project move into the root project folder use the command:
```
mvn package shade:shade
```
then you should be able to run the package PSP11.  
if you want to run the server you should run:
```
java -jar target/PSP11-1.0.jar server
```
for the client:
```
java -jar target/PSP11-1.0.jar client
```


