import lombok.Data;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.Scanner;

public class SeleniumUITest {
    private WebDriver driver;

    @BeforeTest
    @Parameters({"baseUrl"})
    public void init(String url) {
//        System.setProperty("webdriver.chrome.driver", "C:\\Users\\arunb\\IdeaProjects\\SeleniumUi\\chromedriver.exe");
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        driver.manage().window().maximize();
        driver.get(url);
    }


    @AfterTest
    public void quit() {
        driver.quit();
    }
/*
    @DataProvider(name = "test-data-provider")
    public Object[][] dpMethod() {
        return new Object[][]{{"admin1", "password1"}, {"admin2", "password2"}};
    }
 */

    @DataProvider(name = "test-data-provider")
    public Object[][] dp() throws IOException {
        //Object[][] data = new Object[2][2];
        String[][] data = new String[2][2];
        File myObj = new File("testFile.txt");
        Scanner myReader = new Scanner(myObj);
        int dataNum = 0;
        while (myReader.hasNextLine()) {
            String line = myReader.nextLine();
            System.out.println("Line value - " + line);
            data[dataNum][0] = line.split(",")[0];
            data[dataNum][1] = line.split(",")[1];
            dataNum++;
        }
        myReader.close();
        System.out.println("Data Returned is " + data);
        return data;
    }

    @Test(dataProvider = "test-data-provider")
    public void login(String username, String password) {
        LoginPage loginPage = new LoginPage(driver);
        Assert.assertTrue(loginPage.isUsernameDisplayed());
        Assert.assertTrue(loginPage.isStayLoggedInDisplayed());
        int datanum = 0;
        // Perform login action
        System.out.println("Data Provider Data is ==> " + username);
        System.out.println("Data Provider Data is ==> " + password);
        loginPage.login(username, password);
        Assert.assertTrue(loginPage.isErrorDisplayed());
        driver.navigate().refresh();
    }

}
