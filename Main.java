import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final IncidentDAO dao = new IncidentDAO();

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println(" CYBERSECURITY INCIDENT MANAGEMENT SYSTEM");
        System.out.println("========================================");

        try {
            DBConnection.getConnection().close();
            System.out.println("Database connection successful.");
        } catch (SQLException e) {
            System.out.println("Could not connect to MySQL.");
            System.out.println("Check DBConnection.java, MySQL service, and schema.sql.");
            System.out.println("Details: " + e.getMessage());
            return;
        }

        while (true) {
            showMenu();
            int choice = readInt("Enter your choice: ");
            try {
                switch (choice) {
                    case 1: addIncident(); break;
                    case 2: showAllIncidents(); break;
                    case 3: searchBySeverity(); break;
                    case 4: updateStatus(); break;
                    case 5: assignAnalyst(); break;
                    case 6: deleteIncident(); break;
                    case 7: showAnalysts(); break;
                    case 8:
                        System.out.println("Exiting. Stay secure!");
                        sc.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Select 1-8.");
                }
            } catch (SQLException e) {
                System.out.println("Database operation failed: " + e.getMessage());
            }
        }
    }

    private static void showMenu() {
        System.out.println("\n--------------- MAIN MENU ---------------");
        System.out.println("1. Report a new incident");
        System.out.println("2. View all incidents");
        System.out.println("3. Search incidents by severity");
        System.out.println("4. Update incident status");
        System.out.println("5. Assign an analyst");
        System.out.println("6. Delete an incident");
        System.out.println("7. View analysts");
        System.out.println("8. Exit");
    }

    private static void addIncident() throws SQLException {
        String title = readNonEmpty("Incident title: ");
        String description = readNonEmpty("Description: ");
        String severity = readChoice("Severity (LOW/MEDIUM/HIGH/CRITICAL): ",
            new String[]{"LOW", "MEDIUM", "HIGH", "CRITICAL"});
        String reportedBy = readNonEmpty("Reported by: ");

        dao.displayAnalysts();
        String answer = readLine("Assign analyst now? Enter analyst ID or 0 for unassigned: ");
        int chosen = parseInt(answer, -1);
        if (chosen < 0) {
            System.out.println("Please enter a valid analyst ID or 0.");
            return;
        }
        Integer analystId = chosen == 0 ? null : chosen;
        if (analystId != null && !analystExists(analystId)) {
            System.out.println("Analyst ID not found. Incident was not added.");
            return;
        }

        int id = dao.addIncident(title, description, severity, reportedBy, analystId);
        System.out.println(id > 0 ? "Incident reported. New ID: " + id : "Incident could not be added.");
    }

    private static void showAllIncidents() throws SQLException {
        List<Incident> incidents = dao.getAllIncidents();
        if (incidents.isEmpty()) {
            System.out.println("No incidents found.");
            return;
        }
        for (Incident incident : incidents) incident.display();
        System.out.println("----------------------------------------");
        System.out.println("Total incidents: " + incidents.size());
    }

    private static void searchBySeverity() throws SQLException {
        String severity = readChoice("Search severity (LOW/MEDIUM/HIGH/CRITICAL): ",
            new String[]{"LOW", "MEDIUM", "HIGH", "CRITICAL"});
        List<Incident> incidents = dao.searchBySeverity(severity);
        if (incidents.isEmpty()) {
            System.out.println("No incidents found for " + severity + ".");
            return;
        }
        for (Incident incident : incidents) incident.display();
    }

    private static void updateStatus() throws SQLException {
        int id = readInt("Incident ID: ");
        if (dao.getIncidentById(id) == null) {
            System.out.println("Incident ID not found.");
            return;
        }
        String status = readChoice("New status (OPEN/IN_PROGRESS/RESOLVED): ",
            new String[]{"OPEN", "IN_PROGRESS", "RESOLVED"});
        System.out.println(dao.updateStatus(id, status) ?
            "Incident status updated." : "No changes made.");
    }

    private static void assignAnalyst() throws SQLException {
        int id = readInt("Incident ID: ");
        if (dao.getIncidentById(id) == null) {
            System.out.println("Incident ID not found.");
            return;
        }
        dao.displayAnalysts();
        int analystId = readInt("Analyst ID to assign: ");
        if (!analystExists(analystId)) {
            System.out.println("Analyst ID not found.");
            return;
        }
        System.out.println(dao.assignAnalyst(id, analystId) ?
            "Analyst assigned." : "Assignment could not be completed.");
    }

    private static void deleteIncident() throws SQLException {
        int id = readInt("Incident ID to delete: ");
        Incident incident = dao.getIncidentById(id);
        if (incident == null) {
            System.out.println("Incident ID not found.");
            return;
        }
        String confirm = readChoice("Delete '" + incident.getTitle() + "'? (YES/NO): ",
            new String[]{"YES", "NO"});
        if (confirm.equals("YES")) {
            System.out.println(dao.deleteIncident(id) ?
                "Incident deleted." : "Incident could not be deleted.");
        } else {
            System.out.println("Delete cancelled.");
        }
    }

    private static void showAnalysts() throws SQLException {
        dao.displayAnalysts();
    }

    private static boolean analystExists(int id) throws SQLException {
        // Reuse the incident DAO's analyst list display for users; validate using a direct query.
        try (java.sql.Connection con = DBConnection.getConnection();
             java.sql.PreparedStatement ps =
                 con.prepareStatement("SELECT analyst_id FROM analysts WHERE analyst_id = ?")) {
            ps.setInt(1, id);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private static String readChoice(String prompt, String[] allowed) {
        while (true) {
            String value = readLine(prompt).trim().toUpperCase();
            for (String option : allowed) {
                if (option.equals(value)) return value;
            }
            System.out.println("Invalid option. Allowed values: " + String.join(", ", allowed));
        }
    }

    private static String readNonEmpty(String prompt) {
        while (true) {
            String value = readLine(prompt).trim();
            if (!value.isEmpty()) return value;
            System.out.println("This field cannot be empty.");
        }
    }

    private static int readInt(String prompt) {
        while (true) {
            String value = readLine(prompt).trim();
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    private static int parseInt(String value, int fallback) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    private static String readLine(String prompt) {
        System.out.print(prompt);
        return sc.nextLine();
    }
}
