package drivers;

import org.openqa.selenium.WebDriver;

public abstract class DriverManager {

//	protected WebDriver driver;

	protected static ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<WebDriver>() {
    	@Override
    	protected WebDriver initialValue() {
    		return null;
    	}
    };

	protected abstract void createDriver();

	public void quitDriver() {
		if (null != driverThreadLocal.get()) {
			driverThreadLocal.get().quit();
			driverThreadLocal.set(null);
		}
	}

	public WebDriver getDriver() {
		if (null == driverThreadLocal.get()) {
			createDriver();
		}
		return driverThreadLocal.get();
	}
}