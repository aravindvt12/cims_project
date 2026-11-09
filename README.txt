CYBERSECURITY INCIDENT MANAGEMENT SYSTEM
Java Console + MySQL + JDBC

FEATURES
- Report a new incident
- View all incidents
- Search incidents by severity
- Update incident status
- Assign an analyst
- Delete an incident
- View analysts
- Data is stored in MySQL, so it remains after the program exits

OOP CONCEPTS USED
- Encapsulation: Incident fields are private and exposed through getters.
- Classes and objects: Main, Incident, IncidentDAO, DBConnection.
- Separation of responsibilities: Incident stores data; IncidentDAO handles SQL; Main handles console interaction.
- Reusable methods and abstraction through DAO operations.

REQUIREMENTS
1. JDK installed (Java 8 or newer).
2. MySQL Server and MySQL Command Line Client or MySQL Workbench.
3. MySQL Connector/J JDBC driver JAR.

STEP 1: CREATE THE DATABASE
Open MySQL Workbench, open schema.sql, and execute the whole script.
Alternatively, from CMD in the project folder:
    mysql -u root -p < schema.sql

STEP 2: CONFIGURE MYSQL PASSWORD
Open DBConnection.java and replace:
    YOUR_MYSQL_PASSWORD
with the password for your MySQL root user.
If your MySQL user is not root, change USER as well.

STEP 3: GET MYSQL CONNECTOR/J
Download MySQL Connector/J from the official MySQL website:
    https://dev.mysql.com/downloads/connector/j/
Extract the archive and locate the JAR, for example:
    mysql-connector-j-9.x.x.jar
Copy that JAR into this project folder and rename it:
    mysql-connector-j.jar
(Use the actual JAR file you downloaded; do not create an empty JAR.)

STEP 4: OPEN CMD IN THE PROJECT FOLDER
Example:
    cd %USERPROFILE%\Desktop\CybersecurityIncidentManagementSystem
Use the actual folder where you extracted this project.

STEP 5: COMPILE
Run:
    javac -cp ".;mysql-connector-j.jar" Main.java DBConnection.java Incident.java IncidentDAO.java

STEP 6: RUN
Run:
    java -cp ".;mysql-connector-j.jar" Main

IMPORTANT WINDOWS CMD NOTE
The semicolon in the classpath is correct for Windows. On macOS/Linux, use a colon instead.

SAMPLE ANALYSTS
1. Asha Nair (Network Security)
2. Rahul Menon (Malware Analysis)
3. Meera Das (Incident Response)

SAMPLE INCIDENTS
- Suspicious login attempts (MEDIUM, OPEN)
- Unusual endpoint activity (HIGH, IN_PROGRESS)

TROUBLESHOOTING
- "Access denied": check USER and PASSWORD in DBConnection.java.
- "Unknown database": execute schema.sql.
- "Communications link failure": ensure MySQL Server is running.
- "No suitable driver": ensure the connector JAR is in the folder and its name matches the classpath.
- "javac is not recognized": install a JDK and configure PATH, or run javac using the full JDK path.
- If your MySQL server uses a port other than 3306, update the JDBC URL in DBConnection.java.

SECURITY NOTE
This is a student demo project. It records incident reports; it does not scan devices or detect live attacks. Do not put real credentials or sensitive incident details into a class demo database.
