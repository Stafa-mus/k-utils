package database;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MustafaMohamed
 */
public class Database {
    
    private static final Logger LOGGER = Logger.getLogger(Database.class.getName());
    
    /**
     * Full path to the database file.
     * Ideally, the file should be in the AppData or similar folder.
     * The database is an SQLite database
     */
    public static String DATABASE_PATH;
    
    private static final String DB_FILE = "k-utils.sqlite";
    
    private static Connection connection;
    
    /**
     * Keeps track of whether we have already setup the database
     */
    private static boolean initialized = false;
    
    private static void createDatabase() throws SQLException, IOException {
        LOGGER.log(Level.INFO, "Creating database");
        
        String createCommandsTable = """
                                             CREATE TABLE "commands" (
                                             "id"\tINTEGER,
                                             "name"\tTEXT NOT NULL,
                                             "command"\tTEXT NOT NULL,
                                             "description"\tTEXT DEFAULT NULL,
                                             PRIMARY KEY("id" AUTOINCREMENT)
                                             );""";
        String createVariablesTable = """
                                              CREATE TABLE "variables" (
                                              	"id"	INTEGER,
                                              	"name"	TEXT NOT NULL,
                                              	"value"	TEXT NOT NULL,
                                              	"description"	TEXT DEFAULT NULL,
                                              	PRIMARY KEY("id" AUTOINCREMENT)
                                              );
                                              """;
        new File(System.getenv("LOCALAPPDATA") + File.separator + "k-utils").mkdirs();
        new File(DATABASE_PATH).createNewFile();
        connection = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_PATH);
        Statement stmt = connection.createStatement();
        stmt.execute(createCommandsTable);
        stmt.execute(createVariablesTable);
    }
    
    private static boolean databaseExists() throws IOException {
        //LOGGER.log(Level.INFO, "Checking if database exists");
        var dataDir = System.getenv("LOCALAPPDATA");
        //LOGGER.log(Level.INFO, "Data dir {0}", dataDir);
        var folderName = "k-utils";
        DATABASE_PATH = dataDir + File.separator + folderName + File.separator + DB_FILE;
        //LOGGER.log(Level.INFO, "Full path: {0}", DATABASE_PATH);
        return new File(DATABASE_PATH).exists();
    }
    
    public static Connection getConnection() throws IOException, SQLException, ClassNotFoundException {
        if (!initialized) {
            if (!databaseExists()) {
                createDatabase();
            }
            initialized = true;
        }
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_PATH);
            connection.setAutoCommit(true);
        }
        return connection;
    }
}
