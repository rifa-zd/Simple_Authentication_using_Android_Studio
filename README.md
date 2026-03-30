# Simple Login & Signup System (Android Studio)

 - Langugae: JAVA
 - Minimum SDK: 28 (android 9)
> **Note:** The remote database URL used in this project was provided by the course instructor.


### Successful login leads to an Attendance page, where 
- user’s email address is displayed.
- can input attendance and see a report on a particular day's attendance
   
### Implemented Logics

- **Remember User**: If the user checks *Remember User*, the email field will be pre-filled with the *signed-in email* when navigating to the Login page. Otherwise, the field remains empty.

- **Account Creation Redirection**: After successful account creation, the user will not be able to navigate back to the Sign-up page again. Toast messages to indicate the transition.

- **Persistent Login (with “Remember Login”)**: Once logged in with the *Remember Login* option checked, the user stays logged in until explicitly logged out (or the app rebuilds in a real case)
>in checked *Remember Login* case, user will land directly on the **Attendance page** as there is no explicit *logout* option.

### additional features

- All validation or login/signup errors are shown together as a single list (not separate alerts)
- Appropriate toast Messages
- Clicking the reset button clears the password field and unchecks the login-related checkbox
