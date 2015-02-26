______________________________________
Invite Now V2.0
COEN 268 - Mobile Applications Development (Android)

By
- Nishant Phatangare
- Prasad Deshpande
______________________________________



-------------------------------------------
Milestone 2
--------------------------------------------

List of high-level tasks accomplished -:

1. Application Integration with Parse
	- Implemented Sign-Up, Login activities using Parse Users class
	- Implemented 'User Data' table for storing User's FirstName, LastName, EmailID and Location.
	- User have to Sign up/Login only for the first time on any device. 
		Username,Password is stored locally on SQLite and next time when user 
		opens app, this Username-Password from SQLite is used to automatically check it on Parse and Authenticate Login.

2. Implemented Google Play Service API 
	- Last Known Location Data is taken using Google Location APIs and it is written to Parse Database in User-Specific row.

3. One of the most features of the App, to locate nearby Friends is Implemented using Parse and Google APIs.
	- On start, Application takes location data of friends from Parse, checks for nearby friends and Displays them in ListView
	- On selecting any friend from ListView, an Automated SMS is sent to that friend using SmsManager.

4. Integrated contacts with Phonebook contacts by accessing Phonebook.

5. User is able to create user groups and display them.
	- This group is created as a New Contact group in phonebook using WRITE_CONTACTS permission.
	
6. Profile Photo Upload Feature
	- The user is able to take picture using camera and is able to Open Gallery to select photo
	- Feature to store filepath and acess this image in all the activities is not yet implemented.

----------------------------------------------------------------
References and Citations -:
1. Class Notes, Class Codes and HomeWorks
2. developer.android.com
3. Codesamples from stackoverflow.com and few other doubts solving websites
4. Parse Documentation and Tutorials
5. vogella.com Tutorials

----------------------------------------------------------------------------


Old Version - 
-------------------------------------------
Milestone 1 / Invite Now V1.0
--------------------------------------------

List of high-level tasks you accomplished -:

1. StartScreen Activity implemented
2. Created almost all the major activities that are required to implement the functionality
3. In SendInvitesActivity Implemented custom action bar to show Name, Status of the User and Implemented UI elements like EditText, RadioButton, Bottons CheckBox to take inputs from User
5. Implemented ListViews to select Contacts, Groups
6. In settings activity, Used List view to display all the setting Menus
7. Used Icons like - Settings, Edit Profile to go to corrosponding activity
8. Linked all the activities using Intents to implement Navigation

All the elements in activities are not functioning yet.
eg.- More than one radio button gets selected in SendInvitesActivity

Therefore, the function that any activity will perform is not yet implemented
Look and Feel of the application and Navigation between activities is accomplished at this milestone.
----------------------------------------------------------------

	