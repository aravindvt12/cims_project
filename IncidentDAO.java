import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class IncidentDAO {
    private static final String SELECT_BASE =
        "SELECT i.incident_id, i.title, i.description, i.severity, i.status, " +
        "i.reported_by, i.analyst_id, a.analyst_name, i.created_at, i.updated_at " +
        "FROM incidents i LEFT JOIN analysts a ON i.analyst_id = a.analyst_id ";

    public List<Incident> getAllIncidents() throws SQLException {
        List<Incident> incidents = new ArrayList<>();
        String sql = SELECT_BASE + "ORDER BY i.created_at DESC";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) incidents.add(mapIncident(rs));
        }
        return incidents;
    }

    public List<Incident> searchBySeverity(String severity) throws SQLException {
        List<Incident> incidents = new ArrayList<>();
        String sql = SELECT_BASE + "WHERE i.severity = ? ORDER BY i.created_at DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, severity);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) incidents.add(mapIncident(rs));
            }
        }
        return incidents;
    }

    public Incident getIncidentById(int id) throws SQLException {
        String sql = SELECT_BASE + "WHERE i.incident_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapIncident(rs);
            }
        }
        return null;
    }

    public int addIncident(String title, String description, String severity,
                           String reportedBy, Integer analystId) throws SQLException {
        String sql = "INSERT INTO incidents (title, description, severity, status, reported_by, analyst_id) " +
                     "VALUES (?, ?, ?, 'OPEN', ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, severity);
            ps.setString(4, reportedBy);
            if (analystId == null) ps.setNull(5, java.sql.Types.INTEGER);
            else ps.setInt(5, analystId);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        }
        return -1;
    }

    public boolean updateStatus(int id, String status) throws SQLException {
        String sql = "UPDATE incidents SET status = ? WHERE incident_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean assignAnalyst(int incidentId, int analystId) throws SQLException {
        String sql = "UPDATE incidents SET analyst_id = ? WHERE incident_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, analystId);
            ps.setInt(2, incidentId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean deleteIncident(int id) throws SQLException {
        String sql = "DELETE FROM incidents WHERE incident_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public void displayAnalysts() throws SQLException {
        String sql = "SELECT analyst_id, analyst_name, specialization FROM analysts ORDER BY analyst_id";
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            System.out.println("\nAvailable analysts:");
            while (rs.next()) {
                System.out.println(rs.getInt("analyst_id") + ". " +
                    rs.getString("analyst_name") + " (" + rs.getString("specialization") + ")");
            }
        }
    }

    private Incident mapIncident(ResultSet rs) throws SQLException {
        int rawAnalystId = rs.getInt("analyst_id");
        Integer analystId = rs.wasNull() ? null : rawAnalystId;
        return new Incident(
            rs.getInt("incident_id"),
            rs.getString("title"),
            rs.getString("description"),
            rs.getString("severity"),
            rs.getString("status"),
            rs.getString("reported_by"),
            analystId,
            rs.getString("analyst_name"),
            rs.getTimestamp("created_at"),
            rs.getTimestamp("updated_at")
        );
    }
}
