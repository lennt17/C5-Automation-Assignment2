package drivers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.safari.SafariDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class SafariDriverManager extends DriverManager {

	@Override
	public void createDriver() {
		WebDriverManager.safaridriver().setup();
		WebDriver driver = new SafariDriver();
		driverThreadLocal.set(driver);
	}

}