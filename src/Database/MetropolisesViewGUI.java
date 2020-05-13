package Database;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionListener;

public class MetropolisesViewGUI {
    private JFrame frame;
    private JTextField metField;
    private JTextField contField;
    private JTextField popField;
    private JButton add;
    private JButton search;
    private JComboBox popSearch;
    private JComboBox exactSearch;
    private JTable table;
    private TableModel tableModel;

    public MetropolisesViewGUI() {
        frame = new JFrame("Metropolises");
        frame.setSize(800, 600);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        //top
        JPanel upperPanel = new JPanel();
        upperPanel.setLayout(new FlowLayout(FlowLayout.LEADING));

        upperPanel.add(new JLabel("Metropolis: "));
        metField = new JTextField(15);
        upperPanel.add(metField);

        upperPanel.add(new JLabel("Continent: "));
        contField = new JTextField(15);
        upperPanel.add(contField);

        upperPanel.add(new JLabel("Population: "));
        popField = new JTextField(15);
        upperPanel.add(popField);

        // JPanel middlePanel = new JPanel();
        //middlePanel.setBorder(new TitledBorder(""));
        table = new JTable();
        JScrollPane tablePanel = new JScrollPane(table);
        tablePanel.setPreferredSize(new Dimension(table.getWidth(), table.getHeight()));
        // middlePanel.add(tablePanel);


        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));


        leftPanel.add(Box.createRigidArea(new Dimension(10, 15)));
        add = new JButton("Add");
        search = new JButton("Search");
        Dimension d = search.getMaximumSize();
        add.setMaximumSize(d);
        leftPanel.add(add);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(search);


        leftPanel.add(Box.createVerticalStrut(15));

        JPanel searchOptions = new JPanel();
        searchOptions.setLayout(new BoxLayout(searchOptions, BoxLayout.Y_AXIS));
        searchOptions.setBorder(new TitledBorder("Search Options"));
        String[] arrPop = new String[]{"Population Larger Than", "Population Smaller Than"};
        popSearch = new JComboBox(arrPop);
        searchOptions.add(popSearch);
        searchOptions.add(Box.createVerticalStrut(5));

        String[] arrMatch = new String[]{"Exact Match", "Partial Match"};
        exactSearch = new JComboBox(arrMatch);
        searchOptions.add(exactSearch);

        leftPanel.add(searchOptions);
        leftPanel.add(Box.createVerticalStrut(350));

        mainPanel.add(upperPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(leftPanel, BorderLayout.EAST);

        frame.add(mainPanel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }

    public void registerListeners(ActionListener addListener, ActionListener searchListener) {
        add.addActionListener(addListener);
        search.addActionListener(searchListener);
    }

    public void setTableModel(AbstractTableModel tableModel) {
        table.setModel(tableModel);
    }

    public MetropolisDao getRequestInfo() {
        String name = metField.getText();
        String cont = contField.getText();
        String pop = popField.getText();
        String popSrch = popSearch.getSelectedItem().toString();
        String exSrch = exactSearch.getSelectedItem().toString();
        MetropolisDao res = new MetropolisDao(name, cont, pop, popSrch, exSrch);
        return res;
    }
}
