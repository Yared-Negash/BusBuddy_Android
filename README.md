# BusBuddy
BusBuddy analyzes New Jersey Transit Bus Data for over 250+ routes, allowing users to favorite stops and track buses in real time. 

1. **Favoriting Bus Stops:** Upon Installtion, the user is prompted to search for a New Jersey Transit Bus Stop. The following steps are required for sending the proper GET request to the NJ Transit Website. (NOTE: All data is dynamically retrieved from the NJ Transit Offical Website. **No routes, stops, or schedules are hardcoded into the application. This is done in the event NJ transit adds or deletes a route on their network**)

    * Enter a Bus Route: Must be an existing Bus line on the NJ Transit Network. Automatically checks if the user's input is currently operating bus before advancing to the next step. 
    * Choose a Direction: Select one of two directions in which the respective bus route travels to. 
    * Select Stop: User will be presented with all the active stops the Bus will pass through. 
    * After selcecting a stop, the App will store the Bus Stop's street name and ID number into an SQLite Database stored locally on the device. 

2. **Home Screen:** Displays all saved stops in the SQLite Database. Refreshes every minuite to retreive data. Other features include: 

   *

