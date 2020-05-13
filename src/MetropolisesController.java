
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MetropolisesController {
    private final MetropolisesViewGUI view;
    private final MetropolisesModel model;
    private ActionListener addListener;
    private ActionListener searchListener;

    private AbstractTableModel tableModel;
    List<String> headers;
    List<List<String>> data;


    public MetropolisesController(MetropolisesViewGUI view, MetropolisesModel model) {
        this.view = view;
        this.model = model;
        createListeners();
        view.registerListeners(addListener, searchListener);
        getTableModel();
        view.setTableModel(tableModel);
    }

    private void getTableModel() {
        headers = model.getHeaders();
        data = model.getData();
        tableModel = new AbstractTableModel() {
            @Override
            public int getRowCount() {
                return data.size();
            }

            @Override
            public int getColumnCount() {
                return headers.size();
            }

            @Override
            public Object getValueAt(int i, int j) {
                return data.get(i).get(j);
            }

            @Override
            public String getColumnName(int column) {
                return headers.get(column);
            }
        };
    }

    private void createListeners() {
        addListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MetropolisDao curr = view.getRequestInfo();
                if (model.addRow(curr))
                    tableModel.fireTableDataChanged();
            }
        };

        searchListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MetropolisDao curr = view.getRequestInfo();
                if (model.searchRows(curr))
                    tableModel.fireTableDataChanged();
            }
        };
    }
}
