import org.sqlite.SQLiteConfig;

import java.sql.*;

public class SQLiteJDBC {

    /**
     * Select all vehicles from the table
     *
     * @param connection a non null conneciton to the database
     * @return The result set (sql cursor) with all the vehicles regarding the databse
     * @throws SQLException if any occurred regarding the database
     */
    public static ResultSet selectAllVehicles(Connection connection) throws SQLException {
        assert null != connection;

        System.out.println("Get all vehicles");

        PreparedStatement statement = connection.prepareStatement("SELECT * FROM vehicle");
        ResultSet resultSet = statement.executeQuery();
        return resultSet;
    }

    /**
     * Inserts a new vehicle into the database
     *
     * @param connection a non-null connection to the database
     * @param plate      the vehicles plate (may not be null)
     * @param make       the make of the vehicle (may not be null)
     * @param model      the model of the vehicle (may not be null)
     */
    public static void insertVehicle(Connection connection, String plate, String make, String model) throws SQLException {
        assert null != connection && null != plate && null != make && null != model;

        String checkExist = "SELECT * FROM vehicle WHERE plate = (?)";
        PreparedStatement checkStatement = connection.prepareStatement(checkExist);
        checkStatement.setString(1, plate);
        ResultSet resultSet = checkStatement.executeQuery();
        if(resultSet.next()) {
            System.out.println("A vehicle with plate " + plate + " already exists");
            return;
        }

        System.out.println("Added a new vehicle: " + make + " " + model + " with a plate " + plate);

        String insert = "INSERT into vehicle(plate, make, model) values (?,?,?)";
        PreparedStatement statement = connection.prepareStatement(insert);

        statement.setString(1, plate);
        statement.setString(2, make);
        statement.setString(3, model);

        System.out.println("Rows added: " + statement.executeUpdate());
    }

    /**
     * Prints the set of vehicles in the database
     *
     * @param set a non null resultset
     * @throws SQLException if any error occurred regarding the database
     */
    public static void printVehicles(ResultSet set) throws SQLException {
        assert null != set;

        System.out.println(set.getString("plate") + ", ");
        System.out.println(set.getString("make") + ", ");
        System.out.println(set.getString("model") + ", ");

        System.out.println();
    }

    /**
     * Create an account for the owner
     *
     * @param connection a non-null connection to the database
     * @param email      The users email
     * @param firstNames The users first name(s)
     * @param lastName   The users last name
     * @param password   The users password
     * @throws SQLException if any error occurred regarding the database
     */
    public static void createUser(Connection connection, String email, String firstNames, String lastName, String password) throws SQLException {
        assert null != connection && null != email && null != firstNames && null != lastName && password != null;

        String checkExist = "SELECT * FROM owner WHERE email = (?)";
        PreparedStatement checkStatement = connection.prepareStatement(checkExist);
        checkStatement.setString(1, email);
        ResultSet resultSet = checkStatement.executeQuery();
        if(resultSet.next()) {
            System.out.println("A user with email " + email + " already exists");
            return;
        }

        String insert = "INSERT into owner(email, firstNames, lastName, password) values (?,?,?,?)";
        PreparedStatement statement = connection.prepareStatement(insert);

        statement.setString(1, email);
        statement.setString(2, firstNames);
        statement.setString(3, lastName);
        statement.setString(4, password);

        //TODO: Make password only print out the number of characters (eg: abc123 = ******)
        System.out.println("Added a new user: \n Email: " + email + "\n Name " + firstNames
                + " " + lastName + "\n Password: " + password);
        System.out.println("Rows added: " + statement.executeUpdate());
    }

    /**
     * Register a vehicle for the owner for an existing vehicle
     *
     * @param connection a non-null connection to the database
     * @param email The owner's email we want to register the vehicle to
     * @param plate The plate number of the vehicle if it already exists in the database
     * @throws SQLException if any error occurred regarding the database
     */

    public static void registerExistingVehicle(Connection connection, String email, String plate) throws SQLException{
        assert null != connection && null != email && null != plate;

        String checkExistOwner = "SELECT * FROM owner WHERE email = (?)";
        PreparedStatement checkOwnerStatement  =connection.prepareStatement(checkExistOwner);
        checkOwnerStatement.setString(1,email);
        ResultSet emailResultSet = checkOwnerStatement.executeQuery();
        if (!emailResultSet.next()) {
            System.out.println("No owner exists with that email");
            return;
        }

        // TODO: Check this check
        String checkExist = "SELECT * FROM vehicle WHERE plate = (?) AND NOT email = NULL";
        PreparedStatement checkStatement = connection.prepareStatement(checkExist);
        checkStatement.setString(1, plate);
        ResultSet resultSet = checkStatement.executeQuery();
        if (resultSet.next()) {
            System.out.println("The vehicle with plate " + plate + " has already been registered to someone else");
            return;
        }

        String registerVehicle = "UPDATE vehicle SET owner = (?) WHERE plate = (?)";
        PreparedStatement statement = connection.prepareStatement(registerVehicle);
        statement.setString(1,email);
        statement.setString(2,plate);

        System.out.println("Registered vehicle " + plate + " to user " + email);
        System.out.println("Rows added: " + statement.executeUpdate());
    }

    /**
     * Inserts a new vehicle into the database
     *
     * @param connection a non-null connection to the database
     * @param email      the owners email
     * @param plate      the vehicles plate (may not be null)
     * @param make       the make of the vehicle (may not be null)
     * @param model      the model of the vehicle (may not be null)
     * @throws SQLException if any error occurred regarding the database
     */
    public static void registerAndInsertVehicle(Connection connection, String email, String plate, String make, String model) throws SQLException {
        assert null != connection && null!= email && null != plate && null != make && null != model;

        String checkExistVehicle = "SELECT * FROM vehicle WHERE plate = (?)";
        PreparedStatement checkStatement = connection.prepareStatement(checkExistVehicle);
        checkStatement.setString(1, plate);
        ResultSet resultSet = checkStatement.executeQuery();
        if(resultSet.next()) {
            System.out.println("A vehicle with plate " + plate + " already exists");
            return;
        }

        String checkExistOwner = "SELECT * FROM owner WHERE email = (?)";
        PreparedStatement checkOwnerStatement  =connection.prepareStatement(checkExistOwner);
        checkOwnerStatement.setString(1,email);
        ResultSet emailResultSet = checkOwnerStatement.executeQuery();
        if (!emailResultSet.next()) {
            System.out.println("No owner exists with that email");
            return;
        }

        String insert = "INSERT into vehicle(plate, make, model) values (?,?,?)";
        PreparedStatement statement = connection.prepareStatement(insert);

        statement.setString(1, plate);
        statement.setString(2, make);
        statement.setString(3, model);

        System.out.println("Added a new vehicle: " + make + " " + model + " with a plate " + plate);
        System.out.println("Rows added: " + statement.executeUpdate());
        System.out.println("====================");

        // This seems incredibly bad since I'm calling the sql method from within the sql method
        // Seems acceptable since the function will handle any error when setting the vehicle owner to the owner
        // that created this
        registerExistingVehicle(connection, email, plate);
    }

    /**
     * Let's show how to connect to a sqlite database, retrieve data from it and insert some row
     *
     * @param args none
     */
//    public static void main(String[] args) {
//        String url = "jdbc:sqlite:lab3.sqlite";
//        System.out.println("Opened connection to " + url);
//
//        try (Connection connection = DriverManager.getConnection(url)) {
//            ResultSet set = selectAllInspections(connection);
//            while(set.next()) {
//                printInspections(set);
//            }
//
//             insertInspection(connection,20180302, "HVJ150");
//
//            set = selectAllInspections(connection);
//            while(set.next()) {
//                printInspections(set);
//            }
//
//            ResultSet set = selectAllVehicles(connection);
//            while (set.next()) {
//                printVehicles(set);
//            }
//
//            insertVehicle(connection, "INE1999", "Nissan", "March");
//
//            set = selectAllVehicles(connection);
//            while (set.next()) {
//                printVehicles(set);
//            }
//            insertVehicle(connection, "INE19959", "Nissan", "March");
//            insertInspection(connection,20180301, "HVJ150");
//            set = getVehicleInspections(connection, "MYBABE");
//            while(set.next()) {
//                printInspections(set);
//            }
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
//    }
}
