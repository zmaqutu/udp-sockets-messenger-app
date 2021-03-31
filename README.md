<h1 align="center"> Waterflow simulation</h1>

<div align="center" >
  <img src="https://img.shields.io/badge/made%20by-Zongo%20Maqutu-blue?style=for-the-badge&labelColor=20232a" />
  <img src="https://img.shields.io/badge/Java-20232a?style=for-the-badge&logo=java" />
  <img src="https://img.shields.io/badge/Java Libraries-AWT-fffe6a?style=for-the-badge&logo=java&labelColor=20232a" />
  <img src="https://img.shields.io/badge/Concurrent Programming-e34c26?style=for-the-badge&logo=github" />
  <img src="https://img.shields.io/badge/pull%20requests-welcome-green?style=for-the-badge&logo=github&labelColor=20232a" />
</div>

## Table of Contents
* [Features](#features)
* [Libraries Used](#libraries)
* [Project Setup](#license)
* [Future Scope](#license)
## Description 
*A multi-threaded implementation of a waterflow simulation that shows how water flows on a terrain flows downhill.*
## Live Demo
A live interactive demo can be found [below](https://zmaqutu.github.io/3D-Pathfinding-Visualizer/).
<img src="./readme_assets/WaterflowDemo.gif" width="100%">

## Features
* **Display** <br>
&emsp;A main display window that shows the landscape as a greyscale image, with black
representing the lowest elevation and white the highest. Overlaid on this should be an
image representing the locations of water in blue. Note that although the water will
have a depth, represented as an integer value, the colouring is the same uniform blue
for any depth value greater than zero.

* **Buttons** <br>
&emsp;A ‘reset’ button that zeroes both the water depth across the entire landscape and the
timestep count.<br>
&emsp;A ‘pause’ button that temporarily stops the simulation. <br>
&emsp;A ‘play’ button that either starts the simulation or allows it to continue running if it was previously paused.<br>
&emsp;An ‘end’ button that closes the window and exits the program.<br>


* **Multi-threaded processing** <br>
&emsp;The simulation is carried out by four threads each responsible for processing a random but final portion of the points on the list of grid positions.These threads are synchronised on each timestep. That is, no thread is ever allowed to start the next timestep of simulation before all others are complete. The program exhibits “fluid conservation”. Water can only be created through user mouse input and destroyed by reaching the boundary. The simulation itself is
only responsible for moving water over the terrain.


### Libraries Used
* Java Abstract Window Toolkit (AWT)
* Java's concurrency library


## Project setup  
To run this project make sure you have downloaded and installed java. Then run the Makefile with the commands[Download and install Java](https://www.oracle.com/java/technologies/javase-downloads.html).
```
make runLarge       // Runs simulation with large terrain (1024x1024 dimensions)
make runMedium      //Runs simulation with medium terrain (512x512 dimensions)
```

## Future Scope
* Add testing scripts to find and remove bugs
* Add a counter that displays the number of timesteps since the start of the simulation 
* Add scripts to randomly generate terrains of any size for testing

<p align="center">Made with ❤️ with good ol' vim</p>

