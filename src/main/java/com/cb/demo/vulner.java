import java.sql.*;
import java.io.*;

public class VulnerableApp {

    // Hardcoded credentials (Sensitive Data Exposure)
    private static final String DB_URL = "jdbc:mysql://localhost:3306/testdb";
    private static final String USER = "admin";
    private static final String PASS = "password123";

    public static void main(String[] args) {
        String userInput = args.length > 0 ? args[0] : "defaultUser";

        // SQL Injection vulnerability
        String query = "SELECT * FROM users WHERE username = '" + userInput + "'";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                System.out.println("User: " + rs.getString("username"));
            }

        } catch (SQLException e) {
            // Information disclosure
            e.printStackTrace();
        }

        // Command Injection vulnerability
        try {
            Runtime.getRuntime().exec("ping " + userInput);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Insecure file handling (Path Traversal)
        try {
            File file = new File("/tmp/" + userInput);
            BufferedReader br = new BufferedReader(new FileReader(file));
            System.out.println(br.readLine());
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
