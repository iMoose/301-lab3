package steps;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

//TODO: The GenericTestRunner is just a runner object that runs the steps defined in the steps folder -> This is where we should separate the tests
// based on their scenarios? -> The checklist becomes 1: Create a feature file in the folder that defines the scenarios
// 2: Run the test and get the autogenerated code -> Refer to step 3/4 in the .pdf for lab4
// 3: Create a file in steps for the scenario and paste in the code that was spit out from above
// 4: Each test should now just complete one step in the .feature file

public class CreateOwnerSteps {
    private Connection connection;

    private String vehicle_plate = "";
    private String fakeFirstNames = "Bob";
    private String fakeLastName = "Marley";
    private String fakePassword = "bobbyM420";


    @Given("^I am connected to the WOF database$")
    public void i_am_connected_to_the_WOF_database() throws SQLException {
        String url = "jdbc:sqlite:lab3.sqlite";
        connection = DriverManager.getConnection(url);
    }

    // Add proper email regex here
    @Given("^No owner with email \"([^\"]*@[^\"]*.[co|com|us|])\" exists$")
    public void no_owner_with_email_exists(String email) throws Throwable {
        // Check if there is a user with this email
    }

    @When("^I try to register with this email$")
    public void i_try_to_register_with_this_email(String email) throws Throwable{
        createUser(connection,email,fakeFirstNames,fakeLastName,fakePassword);
    }

    @Then("^This user should be present in the list of users$")
    public void this_user_should_be_present_in_the_list_of_users(String arg1) throws Throwable {
        ResultSet allUsers = getUsers(connection);
        if (allUsers.getString(email)) {
            return;
        }
        Assert.fail();  // Fail the test if the user isn't in allUsers
    }
}
