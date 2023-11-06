package command;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author MustafaMohamed
 */
public class CommandsTableModel extends AbstractTableModel{
    
    private final List<Command> commands = new ArrayList<>();
    
    private final String[] columnNames = {
        "ID", "Name", "Command", "Description"
    };

    public void addCommand(Command c) {
        commands.add(c);
    }
    
    public Command getCommandAt(int index) {
        return commands.get(index);
    }
    
    public void clear() {
        commands.clear();
    }
    
    @Override
    public int getRowCount() {
        return commands.size();
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
        Command c = commands.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> c.id;
            case 1 -> c.name;
            case 2 -> c.command;
            case 3 -> c.description;
            default -> null;
        };
    }
    
}
