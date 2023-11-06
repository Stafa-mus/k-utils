package variable;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author MustafaMohamed
 */
public class VariablesTableModel extends AbstractTableModel {
    private final List<Variable> variables = new ArrayList<>();
    
    private final String[] columnNames = {
        "ID", "Name", "Value", "Description"
    };

    public void addCommand(Variable v) {
        variables.add(v);
    }
    
    public Variable getCommandAt(int index) {
        return variables.get(index);
    }
    
    public void clear() {
        variables.clear();
    }
    
    @Override
    public int getRowCount() {
        return variables.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }
    
    @Override
    public String getColumnName(int index) {
        return columnNames[index];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Variable v = variables.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> v.id;
            case 1 -> v.name;
            case 2 -> v.value;
            case 3 -> v.description;
            default -> null;
        };
    }
}
