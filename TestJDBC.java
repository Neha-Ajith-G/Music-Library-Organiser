import java.sql.*;

public class TestJDBC {
    public static void main(String[] args) {
        Connection connection = null;
        try {
            // Load the JDBC driver for MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish connection to your MySQL database
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/MusicLibrary", "root", "r4r84848");
            System.out.println("Connected to database.");

            // Create a statement to execute SQL queries
            Statement statement = connection.createStatement();

            // Execute a SQL query to retrieve song names
            ResultSet resultSet = statement.executeQuery("SELECT Song_Name FROM Song");

            // Process the result set
            while (resultSet.next()) {
                String songName = resultSet.getString("Song_Name");
                System.out.println("Song Name: " + songName);
            }

            // Close the result set, statement, and connection
            resultSet.close();
            statement.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            try {
                // Close the connection if not already closed
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}
