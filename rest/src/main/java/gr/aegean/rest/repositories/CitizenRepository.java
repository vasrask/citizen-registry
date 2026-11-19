package gr.aegean.rest.repositories;

import gr.aegean.domain.Citizen;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class CitizenRepository {

    private static final String DB_URL = System.getenv()
            .getOrDefault("DB_URL", "jdbc:mysql://localhost:3306/citizen_registry" + "?useSSL=false&serverTimezone=UTC");

    private static final String DB_USER = System.getenv()
            .getOrDefault("DB_USER", "citizen_app");

    private static final String DB_PASSWORD = System.getenv()
            .getOrDefault("DB_PASSWORD", "citizen_pass");

    private static Connection getConnection() throws SQLException {
    String url = System.getenv()
	    .getOrDefault("DB_URL", "jdbc:mysql://localhost:3306/citizen_registry?useSSL=false&serverTimezone=UTC");

    String user = System.getenv().getOrDefault("DB_USER", "citizen_app");

    String password = System.getenv().getOrDefault("DB_PASSWORD", "citizen_pass");

    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException e) {
        throw new SQLException(
            "MySQL JDBC Driver could not be loaded", e
        );
    }

    return DriverManager.getConnection(url, user, password);
    }

    public static Citizen save(Citizen citizen) {

        String sql = """
            INSERT INTO citizens
            (registry_number, first_name, middle_name,
             last_name, date_of_birth, place_of_birth)
            VALUES (?, ?, ?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE
                first_name = VALUES(first_name),
                middle_name = VALUES(middle_name),
                last_name = VALUES(last_name),
                date_of_birth = VALUES(date_of_birth),
                place_of_birth = VALUES(place_of_birth)
            """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, citizen.getRegistryNumber());
            stmt.setString(2, citizen.getFirstName());
            stmt.setString(3, citizen.getMiddleName());
            stmt.setString(4, citizen.getLastName());
            stmt.setString(5, citizen.getDateOfBirth());
            stmt.setString(6, citizen.getPlaceOfBirth());

            stmt.executeUpdate();

            return citizen;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to save citizen", e);
        }
    }

    public static Citizen findByRegistryNumber(String registryNumber) {

        String sql = """
            SELECT * FROM citizens
            WHERE registry_number = ?
            """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, registryNumber);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return mapCitizen(rs);
                }

                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to find citizen", e);
        }
    }

    public static Map<String, Citizen> findAll() {

        Map<String, Citizen> citizens = new LinkedHashMap<>();

        String sql = "SELECT * FROM citizens";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Citizen citizen = mapCitizen(rs);

                citizens.put(
                        citizen.getRegistryNumber(),
                        citizen
                );
            }

            return citizens;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve citizens", e);
        }
    }

    public static boolean delete(String registryNumber) {

        String sql = """
            DELETE FROM citizens
            WHERE registry_number = ?
            """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, registryNumber);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete citizen", e);
        }
    }

    private static Citizen mapCitizen(ResultSet rs) throws SQLException {

        Citizen citizen = new Citizen();
        citizen.setRegistryNumber(rs.getString("registry_number"));
        citizen.setFirstName(rs.getString("first_name"));
        citizen.setMiddleName(rs.getString("middle_name"));
        citizen.setLastName(rs.getString("last_name"));
        citizen.setDateOfBirth(rs.getString("date_of_birth"));
        citizen.setPlaceOfBirth(rs.getString("place_of_birth"));

        return citizen;
    }
}
