# WackyMole  
https://github.com/pstorli/WackyMole  
  
Android Software Engineer      
Pete Storli    
2/18/2022  
  
----------------------------------------------------------------------------------------------------  
Recognitions  
----------------------------------------------------------------------------------------------------  
Special Thanks to Matthew from CNB for the game inspiration.  
Extra special thanks to 123rf for images and sound effects (https://www.123rf.com)  
  
----------------------------------------------------------------------------------------------------  
Description  
----------------------------------------------------------------------------------------------------  
  Classic whack a mole game with a few twists.  
  https://en.wikipedia.org/wiki/Whac-A-Mole 
  (Note the 0000's below would normally be actual feature or defect tracking numbers. :)  
  
----------------------------------------------------------------------------------------------------    
Rules  
----------------------------------------------------------------------------------------------------  
  1) If you hit an empty hole, you lose a point.  
  2) If you hit the grass, you lose two points.  
  2) If you whack a mole, you get:  
    2.1) 1 point for a regular mole. IE Mole1  
    2.2) 2 points for a super mole.  IE Mole2 
    2.3) 5 points for a wacky mole.  IE Mole3
       
  3) Game speed increases the longer game is played.

----------------------------------------------------------------------------------------------------
Resolved issues in Version 1002
----------------------------------------------------------------------------------------------------
  0000 Added BDD - Cucumber / Gherki9n tests

----------------------------------------------------------------------------------------------------     
Resolved issues in Version 1001
----------------------------------------------------------------------------------------------------
  // This version will add the game play logic and the app should be completed.
  0000 Added effect of changing the play button to pause and the pause button to play when pressed.
  0000 Added shared preferences to save/restore the current score, level and time.
  0000 Added snack bar, cuz I was hungry. :)
  0000 Added click listener to board and added click sound effect.
  0000 Fixed colors for light/dark mode by adding ?attr/ instead of @color/
  0000 Added countdown timer
  0000 Fixed score and level load, save and restore
  0000 Made moles pop up and down randomly!

----------------------------------------------------------------------------------------------------    
Resolved issues in Version 1000       
----------------------------------------------------------------------------------------------------  
  // In this version, the MVVM architecture was created along with the user interface. 
  // The images and sound effects for the app were also created.
  0000 Created initial project and github repo.  
       https://github.com/pstorli/WackyMole.git
  0000 Created main screen layout  
  0000 Created view model  
  0000 Set versionMajor versionMinor versionPatch in app build.gradle  
  0000 Added app icon (mipmap/ic_launcher.png)  
  0000 Added pngs for various game icons, such as hole, grass and mole in hole.    
       Created grassy hole image    
       Created mole image    
       Created super mole image    
       Created wacky mole image    
  0000 Added sound effects for hammer and explosion    
       Created hammer sound  (res/raw/smack.wav)   
       Created explosion sound (res/raw/explosion.wav)   
    
----------------------------------------------------------------------------------------------------      
TODO, Future Features and Unresolved Issues:        
----------------------------------------------------------------------------------------------------  
  // Known Bugs TOO-DOO
  0000 Initial game startup too slow.
  0000 Toast messages may be too short

  // Fun Future Features
  0000 Add Help menu
    
  
  
  
  
  



