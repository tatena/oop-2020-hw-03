
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MetropolisesModel {
    private Connection connection;
    private List<String> headers;
    private List<List<String>> data;

    public MetropolisesModel(Connection connection) {
        this.connection = connection;
        headers = new ArrayList<>(Arrays.asList("Metropolis", "City", "Population"));
        data = new ArrayList<>();
    }

    public boolean addRow(MetropolisDao input) {
        if (!input.isAddable()) {
            System.out.println("Please fill all fields to add metropolis or check if population field contains only digits");
            return false;
        }

        try {
            Statement reqStm = connection.createStatement();
            String request = input.getInsertQuery();
            reqStm.execute(request);
            data.clear();
            data.add(getRow(input.name, input.continent, input.population));
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private List<String> getRow(String name, String continent, String population) {
        return new ArrayList<>(Arrays.asList(name, continent, population));
    }

    public boolean searchRows(MetropolisDao input) {
        if (!input.isSearchable()) {
            System.out.println("Please check if population field contains only digits");
            return false;
        }
        try {
            Statement reqStm = connection.createStatement();
            String request = input.getSelectQuery();
            ResultSet searchData = reqStm.executeQuery(request);
            data.clear();
            while (searchData.next()) {
                data.add(getRow(searchData.getString(1), searchData.getString(2), searchData.getString(3)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public List<List<String>> getData() {
        return data;
    }
}
