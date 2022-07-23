import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class LoginPage {
    WebDriver uiDriver;
    By usernameElem = By.name("username");
    By passwordElem = By.name("password");
    By loginButton = By.xpath("//button[contains(text(),'Login')]");
    By stayLoggedIn = By.cssSelector("div label[for='stay-logged-in']");
    By errorElem = By.cssSelector("div[ng-if='vm.err.unauthorised']");

    public LoginPage(WebDriver driver) {
        this.uiDriver = driver;
    }

    public void login(String uname, String passwd) {
        uiDriver.findElement(usernameElem).clear();
        uiDriver.findElement(usernameElem).sendKeys(uname);
        uiDriver.findElement(passwordElem).clear();
        uiDriver.findElement(passwordElem).sendKeys(passwd);
        uiDriver.findElement(loginButton).click();
    }

    public String getPageTitle() {
        return uiDriver.getTitle();
    }

    public boolean isUsernameDisplayed() {
        WebDriverWait wait = new WebDriverWait(uiDriver, Duration.ofSeconds(15));
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameElem));
        return element.isDisplayed();
    }

    public boolean isStayLoggedInDisplayed() {
        WebDriverWait wait = new WebDriverWait(uiDriver, Duration.ofSeconds(15));
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(stayLoggedIn));
        return element.isDisplayed();
    }

    public boolean isErrorDisplayed() {
        WebDriverWait wait = new WebDriverWait(uiDriver, Duration.ofSeconds(15));
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(errorElem));
        String errorText = element.getText();
        System.out.println("Error message displayed -" +errorText);
        return element.isDisplayed();
    }
}
