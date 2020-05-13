package Database;
import org.junit.jupiter.api.*;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MetropolisesModelTest {
    private Connection conn;
    private MetropolisDao metDao;
    private Statement stm;
    private MetropolisesModel model;

    @BeforeAll
    void setUp() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection(
                "jdbc:mysql://localhost",
                "root",
                "nininono447978");
        Statement stm = conn.createStatement();
        stm.execute("CREATE DATABASE metropolis_test_db");
        stm.execute("USE metropolis_test_db");
        createTable();
        model = new MetropolisesModel(conn);
    }


    @AfterAll
    void tearDown() throws SQLException {
        dropTable();
        Statement stm = conn.createStatement();
        stm.execute("DROP DATABASE metropolis_test_db");
        conn.close();
    }


    @Test
    @Order(1)
    public void testEmpty() {
        metDao = new MetropolisDao("", "", "", "", "");
        assertTrue(model.searchRows(metDao));
        assertTrue(model.getData().isEmpty());
    }

    @Test
    @Order(2)
    public void testAdd() {
        stm = null;
        metDao = new MetropolisDao("Tbilisi", "Europe", "1000000", "", "");
        assertTrue(model.addRow(metDao));
        assertTrue(model.searchRows(new MetropolisDao("Tbilisi", "Europe", "", "Larger than", "Exact Search")));
        assertEquals(1, model.getData().size());
        assertTrue(model.addRow(metDao));
        assertTrue(model.searchRows(new MetropolisDao("Tbili", "pe", "5", "Larger than", "Partial Search")));
        assertEquals(2, model.getData().size());

    }


    @Test
    @Order(3)
    public void testAddInvalid() throws SQLException {
        assertFalse(model.addRow(new MetropolisDao("", "Europe", "1000000", "", "")));
        assertFalse(model.addRow(new MetropolisDao("blaa", "", "1000000", "", "")));
        assertFalse (model.addRow(new MetropolisDao("blaa", "Europe", "", "", "")));
        //MetropolisesModel  badModel = new MetropolisesModel(new Co);
        Connection badCon = DriverManager.getConnection(
                "jdbc:mysql://localhost",
                "root",
                "nininono447978");
        badCon.close();
        MetropolisesModel badModel = new MetropolisesModel(badCon);
        badModel.searchRows(new MetropolisDao("Tbilisi", "Europe", "1000000", "", ""));
        badModel.addRow(new MetropolisDao("Tbilisi", "Europe", "1000000", "", ""));
    }


    @Test
    @Order(4)
    public void testSearch() {
        //bad Search
        assertFalse(model.searchRows(new MetropolisDao("bla", "blu", "blablu", "", "")));
        assertTrue(model.searchRows(new MetropolisDao("", "", "", "", "")));

        model.addRow(new MetropolisDao("London", "Europe", "1000000", "", ""));
        model.addRow(new MetropolisDao("Tokyo", "Asia", "1000000", "", ""));

        //partial search
        model.searchRows(new MetropolisDao("", "Europe", "5000000", "Smaller Than", "Exact Match"));
        assertEquals(3, model.getData().size());

        model.searchRows(new MetropolisDao("yo", "", "0", "Larger Than", "Partial Match"));
        assertEquals(1, model.getData().size());

        model.searchRows(new MetropolisDao("s", "Europe", "", "Smaller Than", "Exact Match"));
        assertEquals(0, model.getData().size());

        model.searchRows(new MetropolisDao("", "", "6000000", "Smaller Than", "Exact Match"));
        assertEquals(4, model.getData().size());

        assertEquals(3, model.getHeaders().size());
    }


    private void dropTable() throws SQLException {
        Statement stm = conn.createStatement();
        stm.execute("DROP  TABLE metropolises");
    }

    private void createTable() throws SQLException {
        Statement stm = conn.createStatement();
        stm.execute("CREATE TABLE metropolises (\n" +
                "    metropolis CHAR(64),\n" +
                "    continent CHAR(64),\n" +
                "    population BIGINT\n" +
                ");");
    }
}