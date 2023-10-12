
## Features

### For unregistered users
- Several general information pages, such as the "Weekly Menu" page displaying the real menus entered into the database for the current week, and the "Contact" page allowing users to send emails to Child Kitchen's Gmail address. (Configuration settings are also included for using MailHog for testing purposes.)
- The "About Us" page utilizes information caching for the service points of the kitchen to optimize database access, as these rarely change from the kitchen's inception to the present day.
- Registration page and login page for the system.

### For registered users:
-	Upon logging into the system, the user is automatically redirected to the "E-Kitchen" page, where the rules for using the system are described, and where users can perform essential activities required by a user, including purchasing coupons and making food requests.
-	Before using the system, it is essential for users to register the children who will use the kitchen's services. To enhance the user experience, the system automatically populates the registered children for the user.

- Child registration:
    -	Child registration occurs through the user profile on the "Profile" page, in the "Children" section, using the "Add Child" button. Clicking the button redirects the user to a form for adding a child. According to the terms of Child Kitchen Pleven, the user is required to provide information about the child, including the birth certificate and a note from the child's personal doctor. Both documents are uploaded to a test profile using Cloudinary.
    -	During registration, the system automatically determines the child's age group, either 10 months to 1 year or 1 year to 3 years (these age groups correspond to the real groups the kitchen serves).
    -	The system will not allow child registration unless the user consents to personal data processing by checking a checkbox.

-  Working with "E-kitchen":
    -	Once at least one child is registered to use the services of Child Kitchen, the user can purchase coupons and make food requests on the "E-kitchen" page.
    -	If an attempt is made to purchase coupons for an age group that does not match the child's age, the system does not permit the purchase and alerts the user.
    -	Upon successful coupon purchase, the system displays information about the action.
    -	Making a food request (validating a coupon) can be done from the "Validate Coupon" section.
    -	If an attempt is made to make a request without available coupons for the child, the system reminds the user to purchase them.
    -	If an attempt is made to validate a coupon for a past date, a weekend, or a date that does not meet Child Kitchen's operational conditions (at least 2 days in advance), the system does not allow the coupon validation.


- Profile Page:
    -	In the profile page, users can view and/or modify their information.
    -	Users can see the registered children, along with general information about them and add more children.
    -	Users can track their future requests with relevant information.
    -	If there is no menu entered for the request date, the system informs the user that it is missing. When a menu is added to the database, the profile information is updated.

### Users with administrator privileges:
-	The system offers a wide range of activities that can be performed by users with administrator privileges. Many of these activities are necessary for the real operation of the system, including:
-	User interaction with the website, such as coupon purchase, request validation, changing personal information, adding children, and monitoring requests.
-	Specialized system tasks:

    -	Continuous monitoring of registered children with allergies to ensure their safe service – "Administrator" page, "List of Allergic Children" section.
    -	Generating reports on requests made for specific time periods, considering service points and age groups of children for statistics and reporting, including showing allergic children in these requests – "Administrator" page, "Requests by Points and by Age" and "Information about Requests Made" sections.
    -	Adding and editing daily menus – "Add or Edit Menu" page in the administrator's action panel. When attempting to add a menu for a day for which one already exists, the system alerts the administrator and suggests editing it.
    -   Checking added menus for a specific day – "View Menu" page in the administrator's action panel. If there is no menu added, the system suggests adding it by redirecting to the appropriate page.
    -	Adding recipes/food – "Add Recipe" page in the administrator's action panel. Administrators can add food for a specific age group in the respective food categories provided by the kitchen. They can also choose from a list of commonly encountered allergens or add new ones related to the food they are adding.
    -	The system is configured to annually delete children's coupons from the database.
    -	Adding or deleting a user request – "Add or Delete Request" page in the administrator's action panel. When entering the entire email or parts of it, the system checks the database for matches and automatically populates the forms on the page. The administrator selects which user the request is for, which of their children, the date, and the point at which the food will be delivered.
    - Editing user information, including granting and revoking administrator privileges.

###	Test Data: When the application starts, the database is populated with:
-	Addition of an administrator (access data: email: admin@test.com, password: admin).
-	Several regular users with their children (access data is visible in the "InitDB" methods in the "Command Line Runner").
-	All service points of Child Kitchen.
-	Commonly encountered food allergens.
-	Common allergies of children.
-	Several test requests, including coupons for some of the children.
-	An example weekly menu for the two age categories.
