# WackyMole by   
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
Resolved issues in Version 1001
----------------------------------------------------------------------------------------------------
  // This version will add the game play logic and the app should be completed.
  0000  
    
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
  0000 Fix colors for dark mode.  
  0000 Add effect of changing the play button to pause and the pause button to play when pressed.  
  0000 Add shared preferences to save/restore the current score, level and time.  
  0000 Add snack bar, cuz I was hungry. :)  
  0000 Change cursor to hammer when over game board. (android.view.PointerIcon)   
    
  
  
  
  
  



