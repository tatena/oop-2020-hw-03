package Database;
import java.sql.*;

public class Main {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost",
                "root",
                "nininono447978");

            Statement useDbStm = connection.createStatement();
            useDbStm.execute("USE metropolises_scm;");
            MetropolisesModel model = new MetropolisesModel(connection);
            MetropolisesViewGUI view = new MetropolisesViewGUI();
            MetropolisesController controller = new MetropolisesController(view, model);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
