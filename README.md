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

The group worked with a Unix-first mindset, the game is fully reliable on Linux and MacOS but for W*ndows it need a little more of a setup.
### Requirements:
to work correctly the project needs:  
- `Java 22` or above  
- `mvn` and `shade` for compiling
- the `trattatello` font
  
### Windows pre-setup:
to run on Windows you should use the new [windows terminal](https://github.com/microsoft/terminal), default terminal from windows 11.  
to run the TUI you should:
- enable UNICODE characters in the terminal
- adjust the font size depending on your screen

the fastest way to enable UTF-8 on your windows terminal is to enable `Use Unicode UTF-8 for worldwide language support` in your OS language settings.  
before running the game typing 
```
chcp 65001
```
should also work.  
Alternatively you could use WSL using Ubuntu.

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


