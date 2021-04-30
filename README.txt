Created by Zachary McNamara
    WGU student #001182706
Email: zmcnama@my.wgu.edu
April 30, 2021


ConsultingApp provides an efficient GUI for user to interact with customer data and appointments stored in the database.

Built using
    IntelliJ IDEA 2020.2.1 (Ultimate Edition)
    JDK-11.0.8-x64
    JavaFX-SDK-11.0.2
    mysql-connector-java-8.0.21

To run this program, Login with correct Username + Password combination
Arrive at 'main menu' with a view of every Appointment in the upcoming Week or Month, toggle via RadioButton.
    Add Appointment, edit Appointment are both available for this screen.
        Selecting either Button will bring up a different screen for user's to input data.
    To delete an Appointment, you must be in the edit Appointment screen, with all relevant information, before you can attempt to delete.

The 'main menu' also has a "View All Customers" Button, which has similar functionality to Appointments from 'main menu'
    You can add or edit an individual Customer but you must be in the edit a Customer screen to attempt a delete.

Additional report:
    Additional report is counting Appointments by Country.
    Knowing the company's market share in one Country relation to each other active Country may be relevant in this business case.

Lambda expressions and explanations are in Javadoc comments of AppointmentAlterController.java.
