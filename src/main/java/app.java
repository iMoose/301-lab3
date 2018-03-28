import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class app {

    // Note: The reason a user should be able to register a vehicle then register themselves to it AND also register a new vehicle and assign themself to it is to make sure that a user can
    // register a new vehicle into the new system but also to register an old vehicle into the system that just wasn't assigned to them
    public static void walkthrough() {
        // Create user

        // Insert new vehicle

        // Register themselves to the new vehicle

        // Insert new vehicle that the user wants to register themselves to

        // Remove a vehicle that a user has assigned themselves to -> Sold vehicle
    }

    // TODO: Make this an actual help string
    private static final String helpString = "To run a command enter the name of the command followed by the appropriate number of characters:";

    // TODO: Make the parameter length forced
    public static void main(String[] args) {
        String url = "jdbc:sqlite:lab3.sqlite";
        Scanner in = new Scanner(System.in);
        System.out.println("Opened connection to " + url);
        try (Connection connection = DriverManager.getConnection(url)) {
            boolean loop = true;
            while(loop) {
                System.out.print("> ");
                String[] command = in.nextLine().split(" ");
                String commandName = command[0];
                switch(commandName) {
                    case "help":
                        System.out.println(helpString);
                        break;
                    case "addVehicle":
                        SQLiteJDBC.insertVehicle(connection,command[1], command[2], command[3]);
                        break;
                    case "addOwner":
                        SQLiteJDBC.createUser(connection, command[1], command[2], command[3], command[4]);
                        break;
                    case "registerVehicle":
                        SQLiteJDBC.registerExistingVehicle(connection, command[1], command[2]);
                        break;
                    case "registerNewVehicle":
                        SQLiteJDBC.registerAndInsertVehicle(connection, command[1], command[2], command[3], command[4]);
                        break;
                    case "walkthrough": // A full automated sequence of commands to register a vehicle,
                        walkthrough();
                        break;          // register an owner, perform an inspection and get the list of inspections
                    case "exit":
                        loop = false;
                        System.out.println("Exiting...");
                }
            }
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}