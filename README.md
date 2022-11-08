# ElevatorSystem
Simple mobile application to simulate Elevator working with using Android (SDK 32), Java and Gradle
## How to open it?  
User can use Android emulator to run this application. 
## First Window (MainActivity)
In the first window user need to select the number of elevators which want to support.  
- The maximum number is 16. One elevator is embedded by default.  
- User can delete elevators. 
- User can also select the number of floors that want the elevator to serve. 
- The minimum number of floors to be handled is 3 and the maximum is 30. 
- Floors are counted from 0, so if user select an elevator with 10 floors, the maximum floor will be 9.
- When User finish setting all Elevators System then can innstall it by "install elevator" button and will be redirected to ElevatorSystemActivity.

## Second Window (ElevatorSystemActivity)
- In the second window of the application user can choose the elevator that want to monitor and give it tasks.  
- At the top of the window a spinner is used to select the elevator from a list.  
- Each elevator has its own id for easier identification and number of floors. 
- Below, by the NumberPicker user can select the floor that he want the elevator to reach. 
- Use the 'pickup' button to start the elevator. When elevator is started on the right side it will simulate its operation. 
- All the time the elevator is running the right arrow will appear and the current floor will be shown. When the arrow goes off, it means that the elevator has reached the required level and the door has been opened. 
- The door can be closed by pressing the "Close doors" button. At this point the elevator will check if we have given it another task in the meantime. If so, it will perform it after the door is closed.
- While the elevator is working, user can dynamically change to another elevator and give it a task as well. 
- Thanks to the use of multithreading, this should not interfere with the work of any elevator.
- When user close the window. The elevator will be uninstalled and the tasks will end.

