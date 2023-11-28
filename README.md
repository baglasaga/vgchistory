# Pokemon VGC Stats & Match History Tracker

## Background

- **What will the application do?**

This application will allow users to keep track of their competitive Pokemon matches,
and provides information such as which games they've won or lost, which Pokemon were
brought on each team, Pokemon usage rates, the user's overall win-rate and ranking, 
and their overall win-rate with or against individual Pokemon.

- **Who will use it?**

This tool will be used by competitive Pokemon players wanting to track their own growth 
and improve at the game. Since they are able to see exactly which Pokemon they struggle 
the most against, and what Pokemon they win the most with, they will be able to know 
where their strengths and weaknesses lie. 

- **Why is this project of interest to you?**

This project is of interest to me because I have recently been getting into the *VGC*
format of competitive Pokemon, and have not been able to find a match history tracking 
tool that would usually exist for other competitive games. 

## User Stories

- As a user, I want to be able to add a match to a list of matches
- As a user, I want to be able to view a list of my matches
- As a user, I want to be able to see statistics on my overall match 
win-rate
- As a user, I want to be able to see statistics on which Pokemon have
the highest/lowest win-rates on/against my team
- As a user, I want to be able to see my current in-game rating
- As a user, I want to be able to search for a Pokemon in my match history
to find its statistics
- As a user, I want to have the option to save my entire match history to file
- As a user, I want to have the option to reload a pre-saved match history from file to resume 
exactly where I left off

## Instructions for Grader

- You can generate the first required action related to the user story "adding multiple Xs to a Y" (adding multiple matches to the match history) by: 
  - Clicking "Add match" in the first menu upon opening the application
  - Entering an integer to represent how much elo was gained/lost in the match
  - Selecting whether the match was won or lost on the shown drop-down menu
  - Entering eight names that represent names of Pokemon used in that match
- You can generate the second required action related to the user story "adding multiple Xs" by:
    - Clicking "View Match History" in the first menu
    - If this is done after matches were added, they will be visible
- To perform the other related actions related to the user story "adding multiple Xs to a Y":
  - Clicking "View Match History" in the first menu after matches are added
  - Click "Filter Pokemon" and answer the prompted questions
  - In the same menu, perform the other related action by clicking "Search Pokemon" and typing in a name
- You can locate my visual component at the top of the screen that appears after clicking "View Match History"
- You can save the state of my application by clicking "Save match history" in the first menu
  - If you were previously in the screen for viewing the match history, simply return to the first menu by clicking 
"Return to Menu"
- You can reload the state of my application by clicking "Load match history" in the first menu
