package Database;
public class MetropolisDao {
    public String name;
    public String continent;
    public String population;
    public String popSearch;
    public String exactSearch;

    public MetropolisDao(String name, String continent, String population, String popSearch, String exactSearch) {
        this.name = name;
        this.continent = continent;
        this.population = population;
        this.popSearch = popSearch;
        this.exactSearch = exactSearch;
    }

    private boolean hasFilter() {
        return !name.equals("") || !continent.equals("") || !population.equals("");
    }

    public boolean isAddable() {
        return !name.equals("") && !continent.equals("") && !population.equals("");
    }

    public boolean isSearchable() {
        return population.chars().allMatch(Character::isDigit);
    }

    public String getInsertQuery() {
        return "INSERT INTO metropolises VALUES" +
                "(\"" + name + "\", \"" + continent + "\", " + population + ");";
    }


    public String getSelectQuery() {
        String res = "SELECT * FROM metropolises ";
        if(!hasFilter())
            return res + ";";
        res += "WHERE ";
        res += getName();
        res += getContinent();
        res += getPopulation();
        res += ";";
        return res;
    }

    private String getPopulation() {
        String res = "";
        if(!population.equals("")) {
            if (!name.equals("") || !continent.equals(""))
                res+= " AND ";
            if(popSearch.contains("Larger")) {
                res += "population > "  + population;
            } else {
                res += "population < " + population;
            }
        }
        return res;
    }

    private String getContinent() {
        String res = "";
        if(!continent.equals("")) {
            if (!name.equals(""))
                res+= " AND ";
            if(exactSearch.contains("Exact")) {
                res += "continent = " + "'" + continent + "' ";
            } else {
                res += "continent LIKE " + "'%" + continent + "%' ";
            }
        }
        return res;
    }

    private String getName() {
        String res = "";
        if(!name.equals("")) {
            if(exactSearch.contains("Exact")) {
                res += "metropolis = " + "'" + name + "' ";
            } else {
                res += "metropolis LIKE " + "'%" + name + "%' ";
            }
        }
        return res;
    }
}
