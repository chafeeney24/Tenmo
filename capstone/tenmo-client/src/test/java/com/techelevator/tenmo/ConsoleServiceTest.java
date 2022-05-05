package com.techelevator.tenmo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import com.techelevator.tenmo.services.ConsoleService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;



@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConsoleServiceTest {

    private ByteArrayOutputStream output;

    @Before
    public void setup() {
        output = new ByteArrayOutputStream();
    }

    @Test
    public void displays_menu_if_user_enters_bad_data() {
        Object[] options = new Object[] { "Larry", "Curly", "Moe" };
        ConsoleService console = getServiceForTestingWithUserInput("Mickey Mouse\n1\n");

        console.promptForMenuSelection(String.valueOf(options));

        String menuDisplay = "\n" + "1) " + options[0].toString() + "\n" + "2) " + options[1].toString() + "\n" + "3) "
                + options[2].toString() + "\n\n" + "Please choose an option >>> ";

        String expected = menuDisplay + "\n*** Mickey Mouse is not a valid option ***\n\n" + menuDisplay + "\n";

        Assert.assertEquals(expected, output.toString());
    }

    @Test
    public void displays_prompt_for_user_input() {
        ConsoleService console = getServiceForTesting();
        String prompt = "Your Name";
        String expected = "Your Name: ";
        console.promptForString(prompt);
        Assert.assertEquals(expected, output.toString());
    }

    @Test
    public void returns_user_input() {
        String expected = "Juan";
        ConsoleService console = getServiceForTestingWithUserInput(expected);
        String result = console.promptForString("Your Name");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void displays_prompt_for_user_input_integer() {
        ConsoleService console = getServiceForTesting();
        String prompt = "Your Age";
        String expected = "Your Age: ";
        console.promptForInt(prompt);
        Assert.assertEquals(expected, output.toString());
    }

    @Test
    public void returns_user_input_for_integer() {
        Integer expected = 27;
        ConsoleService console = getServiceForTestingWithUserInput(expected.toString());
        Integer result = console.promptForInt("Enter a number");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void shows_error_and_redisplays_prompt_if_user_enters_invalid_integer() {
        ConsoleService console = getServiceForTestingWithUserInput("bogus\n1\n");
        String prompt = "Your Age";
        String expected = "Your Age: " + "\n*** bogus is not valid ***\n\nYour Age: ";
        console.promptForInt(prompt);
        Assert.assertEquals(expected, output.toString());
    }

    private ConsoleService getServiceForTestingWithUserInput(String userInput) {
        ByteArrayInputStream input = new ByteArrayInputStream(String.valueOf(userInput).getBytes());
        return new ConsoleService();
    }

    private ConsoleService getServiceForTesting() {
        return getServiceForTestingWithUserInput("1\n");
    }
}
