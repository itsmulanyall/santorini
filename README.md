# Santorini


![alt text](https://www.ultraboardgames.com/img/slideshow/santorini.jpg)








## Description
The project consists of making a digital adaptation of Santorini, a strategy game developed by Gordon Hamilton. 

## Requirements

### Game-specific requirements

It is given the chance of choosing between a simple set of rules and a complete one. This project implements the latter: a match can be played by 2 or 3 players who each chooses a simple card apart from Hermes. Each player is identified by a univocal username.


### Game-agnostic requirements

It implements a distributed system with a single server which is able to manage one match at a time played by multiple players. The requested software design pattern is MVC(Model–View–Controller).


### Advanced functionalities

* 5 Advanced gods
1. Hera
2. Hestia
3. Poseidon
4. Triton
5. Zeus

* Undo function which lets the player undo the action within 5 seconds, implemented on the server side


## Communication

The communication between client and server is established through Socket.


## Inferface

Upon starting, the client can choose to use either CLI(Command-line interface) or GUI(Graphical user interface).More particularly, the CLI interface of the client uses ANSI escape codes and Unicode characters.


## Configuration

An artifact has been generated according to artifact configurations and added to the before launch task list. In order to start the game, these steps have to be followed:
1. Build
2. Build Artifacts
3. All Artifacts
4. Rebuild
5. Server_jar's launchJAR.bat
6. Client_jar's launchJAR.bat
7. Go to regedit to visualize the colors on the cmd:
create a DWORD in 
HKEY_CURRENT_USER\Console\
named "VirtualTerminalLevel" and set to 1




## Developpers
* Perez Jasmine G
* Romano Gabriele
* Samr Jihane
