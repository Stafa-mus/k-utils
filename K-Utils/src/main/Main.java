
package main;

import com.formdev.flatlaf.FlatLightLaf;
import command.Command;
import database.Database;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.*;

/**
 *
 * @author MustafaMohamed
 */
public class Main {
    
    private static String getCurrentDir() {
        boolean isWindows = System.getProperty("os.name")
                .toLowerCase().startsWith("windows");
        String command = isWindows ? "get-location" : "pwd";
        Runtime rt = Runtime.getRuntime();
        String[] commands = {isWindows ? "powershell.exe" : "sh", command};
        Process proc;
        try {
            proc = rt.exec(commands);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
        BufferedReader stdInput = new BufferedReader(new
                                     InputStreamReader(proc.getInputStream()));
        String s = "";
        try {
            String x;
            while ((x = stdInput.readLine()) != null) {
                if (x != null && !x.isBlank()) {
                    s = x;
                }
            }
        } catch (IOException ex) {
            return "";
        }
        return s;
    }
    
    public static void execute(String command) throws IOException, InterruptedException, ExecutionException, TimeoutException {
        boolean isWindows = System.getProperty("os.name")
                .toLowerCase().startsWith("windows");
        // double quotes misbehaving with process builder
        command = command.replace("\"", "\\\"");
        ProcessBuilder builder = new ProcessBuilder();
        if (isWindows) {
            builder.command("powershell.exe", "/c", command);
        } else {
            builder.command("sh", "-c", command);
        }
        builder.directory(new File(getCurrentDir()));
        builder.redirectErrorStream(true);
        builder.redirectInput(ProcessBuilder.Redirect.INHERIT);
        builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        Process process = builder.start();
        ExecutorService service = Executors.newSingleThreadExecutor();
        process.waitFor();
        service.shutdown();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Logger rootLogger = Logger.getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        for(Handler handler : handlers) {
            rootLogger.removeHandler(handler);
        }
        try {
            FileHandler handler = new FileHandler("%h/k--utils%u.log", 5000, 10, true);
            handler.setFormatter(new SimpleFormatter());
            rootLogger.addHandler(handler);
            
        } catch (IOException | SecurityException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            Database.getConnection();
        } catch (IOException | SQLException | ClassNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (args.length == 0 || args[0].equals("--ui")) {
            FlatLightLaf.setup();
            Home home = new Home();
            home.setVisible(true);
        }
        else if (args.length == 1 && args[0].equals("--list")) {
            try {
                Command.listCommands();
            } catch (IOException | SQLException | ClassNotFoundException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else {
            String commandName = args[0];
            String restCommand = "";
            for (int i = 1; i < args.length; i++) {
                restCommand += args[i] + " ";
            }
            try {
                String commandValue = Command.getCommandValue(commandName);
                System.out.println(String.format("%s %s", commandValue, restCommand));
                Database.getConnection().close();
                execute(commandValue + " " + restCommand);
            } catch (IOException | SQLException | ClassNotFoundException ex) {
                System.out.println(String.format("Command %s not found", commandName));
            } catch (InterruptedException | ExecutionException | TimeoutException ex) {
                System.out.println("error");
            }
        }
    }
    
}
