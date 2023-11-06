package command;
import database.Database;
import java.io.IOException;
import java.sql.*;


/**
 *
 * @author MustafaMohamed
 */
public class Command {
    public int id;
    public String name;
    public String command;
    public String description;
    
    public static String getCommandValue(String commandName) throws IOException, SQLException, ClassNotFoundException{
        String sql = "SELECT command FROM commands WHERE name = ? LIMIT 1";
        PreparedStatement stmt = Database.getConnection().prepareStatement(sql);
        stmt.setString(1, commandName);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) return rs.getString("command");
        throw new SQLException("command " + commandName + " not found");
    }
    
    public static void listCommands() throws IOException, SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM commands ORDER BY name ASC ";
        PreparedStatement stmt = Database.getConnection().prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            System.out.print(String.format("%-15s", rs.getString("name")));
            System.out.print(String.format("%-15s", rs.getString("command")));
            System.out.println();
        }   
    }
    
    
}
