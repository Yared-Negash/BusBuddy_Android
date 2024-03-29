# BusBuddy
BusBuddy is a native Android Application that analyzes New Jersey Transit Bus Data for over 250+ routes, allowing users to favorite stops and track buses in real time. 
<div>
   <img src="/app_demo.png" style="max-width:100%;" width="800">
</div>

1. **Favoriting Bus Stops:** Upon Installtion, the user is prompted to search for a New Jersey Transit bus stop. The following steps are required for sending the proper GET request to the NJ Transit website. (NOTE: All data is dynamically retrieved from the NJ Transit offical website. **No routes, stops, or schedules are hardcoded into the application. This is done in the event NJ Transit adds or deletes a route on their network**)

    * Enter a Bus Route: Must be an existing bus line on the NJ Transit network. Automatically checks if the user input is a currently operating route before advancing to the next step. 
    * Choose a Direction: Select one of two directions in which the respective bus route travels to. 
    * Select Stop: User will be presented with all the active stops the bus will pass through. 
    * After selcecting a stop, the app will store the street name and a unique ID number into an SQLite Database stored locally on the device. 

2. **Home Screen:** Displays all saved stops in the SQLite Database. Every record in the database will have its own respective card on the homescreen, each containing up to five incoming buses (each entry will display the bus route , direction, and estimated time of arrival. For example: #21 - Newark Penn Station - 10 Min). The ability to add another bus stop is also possible via the Floating Action Button. 

3. **Track Buses:** To track a bus, click on any of the cards on the home screen. The follwing activity will display all incoming buses for that bus stop. Clicking any of the buses will add it to a seperate table in the SQLite DB. Clicking the bus icon on the left side of the header will display all active tracked buses. The app (regardless if the app is on screen or off) will refresh every minuite and update the ETA of each bus in the database. Will sound a customized tone when the bus is 5, 3, or 1 minute(s) away. 

