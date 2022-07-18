package WebKeywords;

import static org.testng.Assert.fail;

import java.text.MessageFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;

import utils.configs.ConfigSettings;
import utils.log.LogHelper;

import drivers.DriverManager;
import drivers.DriverManagerFactory;
import drivers.DriverType;
import io.qameta.allure.Attachment;

public class WebKeywords {

    private static Logger logger = LogHelper.getLogger();

    private ConfigSettings configSettings;

    private static String browser;

    private static int defaultTimeout;

    private static DriverManager driverManager;

    public WebKeywords() {
        configSettings = new ConfigSettings(System.getProperty("user.dir"));
        browser = configSettings.getBrowser();
        defaultTimeout = Integer.valueOf(configSettings.getDefaultTimeout());
    }

    public WebDriver getDriver() {
        return driverManager.getDriver();
    }

    // 1. open Browser
    public void openBrowser(String... url) {
        if (System.getenv("browser") != null && !System.getenv("browser").isEmpty()) {
            browser = System.getenv("browser");
        }
        try {
            logger.info(MessageFormat.format("Opening ''{0}'' browser", browser.toUpperCase()));
            driverManager = DriverManagerFactory.getManager(DriverType.valueOf(browser.toUpperCase()));
            WebDriver driver = driverManager.getDriver();
            logger.info(MessageFormat.format("Openned ''{0}'' browser successfully", browser.toUpperCase()));
            String rawUrl = url.length > 0 ? url[1] : "";
            if (rawUrl != null && !rawUrl.isEmpty()) {
                logger.info(MessageFormat.format("Navigating to url ''{0}''", rawUrl));
                driver.get(rawUrl);
                driver.manage().window().maximize();
                logger.info(MessageFormat.format("Navigated to url ''{0}'' successfully", rawUrl));
            }
        } catch (Exception e) {
            logger.error(MessageFormat.format("Cannot openBrowser. Root cause is: ''{0}''", e.getMessage()));
        }
    }

    public void openMultiBrowser(String browser, String... url) {
        try {
            logger.info(MessageFormat.format("Opening ''{0}'' browser", browser.toUpperCase()));
            driverManager = DriverManagerFactory.getManager(DriverType.valueOf(browser.toUpperCase()));
            WebDriver driver = driverManager.getDriver();
            logger.info(MessageFormat.format("Openned ''{0}'' browser successfully", browser.toUpperCase()));
            String rawUrl = url.length > 0 ? url[1] : "";
            if (rawUrl != null && !rawUrl.isEmpty()) {
                logger.info(MessageFormat.format("Navigating to url ''{0}''", rawUrl));
                driver.get(rawUrl);
                logger.info(MessageFormat.format("Navigated to url ''{0}'' successfully", rawUrl));
            }
        } catch (Exception e) {
            logger.error(MessageFormat.format("Cannot openBrowser. Root cause is: ''{0}''", e.getMessage()));
        }
    }

    // 2. Navigate to url
    public void navigateToUrl(String url) {
        logger.info(MessageFormat.format("Navigating to ''{0}''", url));
        try {
            WebDriver driver = driverManager.getDriver();
            driver.navigate().to(url);
            logger.info(MessageFormat.format("Navigated to ''{0}'' in 'successfully", url));
        } catch (Exception e) {
            logger.error(MessageFormat.format("Cannot navigate brower ''{0}''. Root cause is: ''{1}''", url,
                    e.getMessage()));
        }
    }

    // 3. Close Browser
    public void closeBrowser(String url) {
        logger.info(MessageFormat.format("Closing ''{0}'' browser", url));
        try {
            driverManager.quitDriver();
            logger.info(MessageFormat.format("Closed ''{0}'' browser successfully", url));
        } catch (Exception e) {
            logger.info(
                    MessageFormat.format("Cannot close browser ''{0}''. Root cause is: ''{1}''", url, e.getMessage()));
        }
    }

    // 4. Quit browser
    public void quitBrowser() {
        logger.info("Quiting browser");
        driverManager.quitDriver();
        logger.info("Quited browser successfully");
    }

    // 5. Switch to frame
    public void switchToFrame(String idFrame) {
        logger.info("Switching to iframe");
        try {
            WebDriver driver = driverManager.getDriver();
            WebElement frame = driver.findElement(By.id(idFrame));
            driver.switchTo().frame(frame);
            logger.info(MessageFormat.format("Switched to frame ''{0}'' sucessfully", frame));
        } catch (Exception e) {
            logger.info(MessageFormat.format("Cannot switch To iFrame ''{0}''. Root cause is: ''{1}''", idFrame,
                    e.getMessage()));
        }
    }

    // 6. get Title
    public String getTitle() {
        logger.info("Getting title of the page");
        String title = null;
        try {
            WebDriver driver = driverManager.getDriver();
            title = driver.getTitle();
            logger.info(MessageFormat.format("Title of the page is ''{0}''", title));

        } catch (Exception e) {
            logger.error(MessageFormat.format("Cannot get title. Root cause is: ''{0}''", e.getMessage()));
        }
        return title;
    }

    // 7. find element smart use fluent wait
    public WebElement findWebElement(String locator, int... timeOut) {
        logger.info(MessageFormat.format("Finding web element located by ''{0}''", locator));
        long startTime = 0;
        long endTime = 0;
        WebElement we = null;
        WebDriver driver = driverManager.getDriver();
        int waitTime = timeOut.length > 0 ? timeOut[0] : defaultTimeout;
        int poolingTime = (waitTime * 1000) / 100;
        try {
            startTime = System.currentTimeMillis();
            Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(waitTime))
                    .pollingEvery(Duration.ofMillis(poolingTime)).ignoring(NoSuchElementException.class);
            we = (WebElement) wait.until(new Function<WebDriver, WebElement>() {
                @Override
                public WebElement apply(WebDriver driver) {
                    return driver.findElement(By.xpath(locator));
                }
            });
            if (we != null) {
                logger.info(MessageFormat.format("Found ''{0}'' web element located by ''{1}'' is : ''{2}''", 1,
                        locator, we));
            }
        } catch (Exception e) {
            endTime = System.currentTimeMillis();
            logger.error(MessageFormat.format("Cannot find WebElement ''{0}''. Root cause is: ''{1}''", locator,
                    e.getMessage()));
        }
        float totalTime = (float) ((endTime - startTime) / 100);
        logger.info("Total time: " + totalTime);
        return we;
    }

    // 8. find list web element
    public List<WebElement> findWebElements(String locator) {
        List<WebElement> wes = null;
        try {
            WebDriver driver = driverManager.getDriver();
            logger.info(MessageFormat.format("Finding web element located by ''{0}''", locator));
            wes = driver.findElements(By.xpath(locator));
            if (wes != null) {
                logger.info(
                        MessageFormat.format("Found ''{0}'' web element(s) located by ''{1}''", wes.size(), locator));
            } else {
                logger.warn(MessageFormat.format(
                        "Cannot find web element located by ''{0}'' because the web element list is null", locator));
            }
        } catch (Exception e) {
            logger.error(MessageFormat.format("Cannot find web element located by ''{0}''. Root cause: ''{1}''",
                    locator, e.getMessage()));
        }
        return wes;
    }

    // 9. click element by locator
    public void click(String locator) {
        WebElement we = findWebElement(locator);
        logger.info(MessageFormat.format("Clicking web element located by ''{0}''", we));
        try {
            if (we.isEnabled()) {
                we.click();
                logger.info(MessageFormat.format("Clicked web element located by''{0}'' successfully", we));
                return;
            }
            logger.error(MessageFormat.format(
                    "Cannot click web element located by ''{0}''. Root cause: the web element located by ''{0}'' is disable",
                    we));
        } catch (Exception e) {
            logger.error(MessageFormat.format("Cannot click web element located by ''{0}''. Root cause is: ''{1}''",
                    locator, e.getMessage()));
        }
    }

    // 10. click element by web element
    public void click(WebElement we) {
        logger.info(MessageFormat.format("Clicking web element by ''{0}'' ", we));
        try {
            we.click();
            logger.info(MessageFormat.format("Clicked web element ''{0}'' successfully", we));
        } catch (Exception e) {
            logger.error(MessageFormat.format("Cannot click web element ''{0}'' . Root cause is: ''{1}''", we,
                    e.getMessage()));
        }
    }

    // 11. doubleClick element by locator
    public void doubleClick(String locator) {
        logger.info(MessageFormat.format("Double clicking web element by ''{0}'' ", locator));
        try {
            WebDriver driver = driverManager.getDriver();
            Actions act = new Actions(driver);
            WebElement we = findWebElement(locator);
            act.doubleClick(we).perform();
            logger.info(MessageFormat.format("Double clicked ''{0}'' web element successfully", locator));
        } catch (Exception e) {
            logger.error(MessageFormat.format("Cannot double click web element ''{0}'' . Root cause is: ''{1}''",
                    locator, e.getMessage()));
        }
    }

    // 12. get text element by locator
    public String getText(String locator) {
        WebElement we = findWebElement(locator);
        logger.info(MessageFormat.format("Getting text of web element located by ''{0}''", locator));
        try {
            String text = we.getText();
            logger.info(MessageFormat.format("Got text of web element located by ''{0}'' successfully: ''{1}''",
                    locator, text));
            return text;
        } catch (Exception e) {
            logger.info(MessageFormat.format("Cannot get text by ''{0}''. Root cause is: ''{1}''", locator,
                    e.getMessage()));
            return null;
        }
    }

    // 13. get text by web element
    public String getText(WebElement we) {
        String text = null;
        logger.info(MessageFormat.format("Getting text of web element located by ''{0}''", we));
        try {
            text = we.getText();
            logger.info(
                    MessageFormat.format("Got text of web element located by ''{0}'' successfully: ''{1}''", we, text));
        } catch (Exception e) {
            logger.info(MessageFormat.format(
                    "Cannot get text of web element located by ''{0}''. Root cause is: ''{1}''", we, e.getMessage()));
        }
        return text;
    }

    // 14. set text element
    public void setText(String locator, String text) {
        WebElement we = findWebElement(locator);
        logger.info(MessageFormat.format("Setting text ''{0}'' to web element located by ''{1}''", text, locator));
        try {
            we.sendKeys(text);
            logger.info(MessageFormat.format("Set text ''{0}'' to web element located by ''{1}'' successfully", text,
                    locator));
        } catch (Exception e) {
            logger.error(MessageFormat.format("Cannot set text by ''{0}''. Root cause is: ''{1}''", locator,
                    e.getMessage()));
        }
    }

    // 15. get attribute element
    public String getAttribute(String locator, String attributeName) {
        WebElement el = findWebElement(locator);
        logger.info(MessageFormat.format("Getting Attribute ''{0}'' of web element located by ''{1}''", attributeName,
                locator));
        try {
            String attributeValue = el.getAttribute(attributeName);
            logger.info(MessageFormat.format(
                    "Got Attribute ''{0}'' of web element located by ''{1}'' successfully: ''{2}''", attributeName,
                    locator, attributeValue));
            return attributeValue;
        } catch (Exception e) {
            logger.info(MessageFormat.format("Cannot get attribute of ''{0}''. Root cause is: ''{1}''", locator,
                    e.getMessage()));
            return null;
        }
    }

    // 16. getCssValue of element
    public String getCssValue(String locator, String propertyName) {
        WebElement el = findWebElement(locator);
        logger.info(MessageFormat.format("Getting cssValue of element for ''{0}'' in this page", locator));
        try {
            String cssValue = el.getCssValue(propertyName);
            logger.info(MessageFormat.format("Getting cssValue successfully ! cssValue ''{0}'' of ''{1}'' is: ''{2}''",
                    propertyName, locator, cssValue));
            return cssValue;
        } catch (Exception e) {
            logger.info(MessageFormat.format("Cannot get css value of ''{0}''. Root cause is: ''{0}''", locator,
                    e.getMessage()));
            return null;
        }
    }

    // 17. scroll to web element
    public void scrollToElement(String locator) {
        logger.info("Scrolling to element :" + locator);
        try {
            WebDriver driver = driverManager.getDriver();
            WebElement element = findWebElement(locator);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
            logger.info(MessageFormat.format("Scrolled to element ''{0}'' successfully", locator));
        } catch (Exception e) {
            logger.error(MessageFormat.format("Cannot scroll to element ''{0}'' . Root cause is: ''{1}'' ", locator,
                    e.getMessage()));
        }
    }

    // 18. get url
    public String getUrl() {
        logger.info("Getting url of the page");
        String url = null;
        try {
            WebDriver driver = driverManager.getDriver();
            url = driver.getCurrentUrl();
            logger.info(MessageFormat.format("Url of the page is ''{0}''", url));
        } catch (Exception e) {
            logger.info(MessageFormat.format("Cannot get url. Root cause is: ''{0}''", e.getMessage()));
        }
        return url;
    }

    // 19. accept alert
    public void acceptAlert() {
        logger.info("Accepting alert at browser");
        try {
            WebDriver driver = driverManager.getDriver();
            driver.switchTo().alert().accept();
            logger.info("Accepted alert successfully");
        } catch (Exception e) {
            logger.error("Cannot accept alert at browser");
        }
    }

    // 20. send text at alert
    public void sendKeysAlert(String text) {
        logger.info(MessageFormat.format("Sending ''{0}'' at alert", text));
        try {
            WebDriver driver = driverManager.getDriver();
            driver.switchTo().alert().sendKeys(text);
            logger.info(MessageFormat.format("Sent ''{0}'' to alert successfully", text));
        } catch (NullPointerException e) {
            logger.error(MessageFormat.format("Cannot send ''{0}'' to the alert. Root cause is: ''{1}''", text,
                    e.getMessage()));
        }
    }

    // 20. send text at locator
    public void sendKeys(WebElement el, String text) {
        logger.info(MessageFormat.format("Sending ''{0}'' at element ''{1}''", text, el));
        try {
            el.sendKeys(text);
            logger.info(MessageFormat.format("Sent ''{0}'' to ''{1}'' successfully", text, el));
        } catch (NullPointerException e) {
            logger.error(MessageFormat.format("Cannot send ''{0}'' to ''{1}''. Root cause is: ''{2}''", text, el,
                    e.getMessage()));
        }
    }

    // 20. send text at element
    public void sendKeys(String locator, String text) {
        logger.info(MessageFormat.format("Sending ''{0}'' at locator ''{1}''", text, locator));
        try {
            WebElement el = findWebElement(locator);
            el.sendKeys(text);
            logger.info(MessageFormat.format("Sent ''{0}'' to ''{1}'' successfully", text, locator));
        } catch (NullPointerException e) {
            logger.error(MessageFormat.format("Cannot send ''{0}'' to ''{1}''. Root cause is: ''{2}''", text, locator,
                    e.getMessage()));
        }
    }

    // 21. select element by index
    public void selectElementByIndex(String locator, int index) {
        logger.info(MessageFormat.format("Selecting web element located by ''{0}''", locator));
        try {
            WebElement we = findWebElement(locator);
            Select se = new Select(we);
            se.selectByIndex(index);
            logger.info(MessageFormat.format("Selected web element by located by ''{0}'' at index ''{1}'' successfully",
                    locator, index));
        } catch (Exception e) {
            logger.error(MessageFormat.format("Cannot select element by index of ''{0}''. Root cause is: ''{1}''",
                    locator, e.getMessage()));
        }
    }

    // 22. select element by value
    public void selectOptionByValue(String locator, String value) {
        logger.info(MessageFormat.format("Selecting option by value : ''{0}'' in ''{1}''", value, locator));
        try {
            WebElement we = findWebElement(locator);
            Select se = new Select(we);
            se.selectByValue(value);
            logger.info(
                    MessageFormat.format("Selected option by value : ''{0}'' in ''{1}'' successfully", value, locator));
        } catch (Exception e) {
            logger.error(
                    MessageFormat.format("Cannot select option by value ''{0}'' in ''{1}''. Root cause is : ''{2}''",
                            value, locator, e.getMessage()));
        }
    }

    // 23. isDisplayed (Return True/False)
    public boolean isDisplayed(String locator) {
        WebElement el = findWebElement(locator);
        logger.info(MessageFormat.format("Setting IsDisplay for web element located by ''{0}''", locator));
        try {
            boolean isDisplay = el.isDisplayed();
            logger.info(MessageFormat.format(
                    "Setted IsDisplay for web element located by ''{0}'' successfully: ''{1}''", locator, isDisplay));
            return isDisplay;
        } catch (Exception e) {
            logger.info(MessageFormat.format("Cannot set web element ''{0}'' isDisplayed?. Root cause is: ''{1}'' ",
                    locator, e.getMessage()));
            return false;
        }
    }

    // 23B. isDisplayed (Return True/False)
    public boolean isDisplayed(WebElement el) {
        logger.info(MessageFormat.format("Setting IsDisplay for web element ''{0}''", el));
        try {
            boolean isDisplay = el.isDisplayed();
            logger.info(MessageFormat.format(
                    "Setted IsDisplay for web element ''{0}'' successfully: ''{1}''", el, isDisplay));
            return isDisplay;
        } catch (Exception e) {
            logger.info(MessageFormat.format("Cannot set web element ''{0}'' isDisplayed?. Root cause is: ''{1}'' ",
                    el, e.getMessage()));
            return false;
        }
    }

    public boolean isVisible(String locator) {
        logger.info(MessageFormat.format("Setting IsVisible for web element located by ''{0}''", locator));
        try {
            boolean isVisible = false;
            WebElement el = findWebElement(locator);
            if (el.isDisplayed()) {
                isVisible = true;
            }
            logger.info(MessageFormat.format(
                    "Setted IsVisible for web element ''{0}'' successfully: ''{1}''", el, isVisible));
            return isVisible;
        } catch (Exception e) {
            logger.info(MessageFormat.format("Cannot set web element located by ''{0}'' isVisible?. Root cause is: ''{1}'' ",
                    locator, e.getMessage()));
            return false;
        }
    }

    // 24. get locator of element
    public Point getLocator(String locator) {
        WebElement el = findWebElement(locator);
        logger.info(MessageFormat.format("Getting Point locator of web element located by ''{0}''", locator));
        try {
            Point point = el.getLocation();
            logger.info(MessageFormat.format(
                    "Got Point locator of web element located by ''{0}'' successfully: X cordinate is :''{1}'' and Y cordinate is : ''{2}''",
                    locator, point.x, point.y));
            return point;
        } catch (Exception e) {
            logger.info(MessageFormat.format("Cannot get locator of ''{0}''. Root cause is: ''{0}''", locator,
                    e.getMessage()));
            return null;
        }
    }

    // 25. submit button
    public void submit(String locator) {
        logger.info(MessageFormat.format("Submiting element for ''{0}'' in this page", locator));
        try {
            WebElement el = findWebElement(locator);
            el.submit();
            logger.info(MessageFormat.format("Submit ''{0}'' successfully ! ", locator));
        } catch (Exception e) {
            logger.info(MessageFormat.format("Cannot submit element ''{0}''. Root cause is: ''{1}''", locator,
                    e.getMessage()));
        }
    }

    // 26. verify Text
    public boolean verifyElementText(String locator, String expectedText) {
        WebElement el = findWebElement(locator);
        logger.info(MessageFormat.format("Verifying text in element located by ''{0}''", locator));
        try {
            String actualText = el.getText().trim();
            if (actualText.equals(expectedText)) {
                logger.info(MessageFormat.format("Actual text ''{0}'' and expected text ''{1}'' are match", actualText,
                        expectedText));
                return true;
            } else {
                logger.error(MessageFormat.format("Actual text ''{0}'' and expected text ''{1}'' are not match",
                        actualText, expectedText));
                return false;
            }
        } catch (Exception e) {
            logger.error(
                    MessageFormat.format("Cannot verify text of web element located by ''{0}''. Root cause is ''{1}''",
                            locator, e.getMessage()));
        }
        return false;
    }

    // 26b. verify Text
    public boolean verifyText(String actualText, String expectedText) {
        logger.info(MessageFormat.format("Verifying text ''{0}''", actualText));
        try {
            if (actualText.equals(expectedText)) {
                logger.info(MessageFormat.format("Actual text ''{0}'' and expected text ''{1}'' are match", actualText,
                        expectedText));
                return true;
            } else {
                logger.error(MessageFormat.format("Actual text ''{0}'' and expected text ''{1}'' are not match",
                        actualText, expectedText));
                return false;
            }
        } catch (Exception e) {
            logger.error(MessageFormat.format("Cannot verify text ''{0}''. Root cause is ''{1}''", actualText,
                    e.getMessage()));
        }
        return false;
    }

    // 27. click Offset
    public void clickOffset(String locator, int offsetX, int offsetY) {
        WebElement el = findWebElement(locator);
        logger.info(MessageFormat.format(
                "Clicking offset element by locator ''{0}'' at offsetX: ''{1}'' and offsetY: ''{2}''", locator, offsetX,
                offsetY));
        try {
            WebDriver driver = driverManager.getDriver();
            Actions act = new Actions(driver);
            act.moveToElement(el, offsetX, offsetY).click().build().perform();
            logger.info(MessageFormat.format(
                    "Clicked offset element by locator ''{0}'' at offsetX: ''{1}'' and offsetY: ''{2}'' successfully",
                    locator, offsetX, offsetY));
        } catch (Exception e) {
            logger.error(MessageFormat.format(
                    "Cannot click offset element by locator ''{0}'' at offsetX: ''{1}'' and offsetY: ''{2}''. Root cause is: ''{3}'' ",
                    locator, offsetX, offsetY, e.getMessage()));
        }
    }

    // 28. back
    public void back() {
        logger.info("Backing browser previous !");
        try {
            WebDriver driver = driverManager.getDriver();
            driver.navigate().back();
            logger.info("Backed browser successfully");
        } catch (Exception e) {
            logger.error(MessageFormat.format("Cannot back browser previous. Root cause is: ''{0}''", e.getMessage()));
        }
    }

    // 29. Check isSelected? a checkbox
    public boolean check(String locator) {
        WebElement we = findWebElement(locator);
        logger.info(MessageFormat.format("Checking checkbox at ''{0}'' !", locator));
        boolean isSelected;
        try {
            isSelected = we.isSelected();
            logger.info(
                    MessageFormat.format("Checked isSelect at ''{0}'' : ''{1}'' successfully", locator, isSelected));
            return isSelected;
        } catch (Exception e) {
            logger.error(MessageFormat.format("Cannot check. Root cause is: ''{0}''", e.getMessage()));
            return false;
        }
    }

    // 30. Deselect option by index
    public void deselectOptionByIndex(String locator, int index) {
        logger.info(MessageFormat.format("Deselecting option at ''{0}'' by index ''{1}''", locator, index));
        try {
            Select listbox = new Select(findWebElement(locator));
            listbox.deselectByIndex(5);
            logger.info(
                    MessageFormat.format("Deselected option at ''{0}'' by index ''{1}'' successfully", locator, index));
        } catch (Exception e) {
            logger.error(
                    MessageFormat.format("Cannot deselect option at ''{0}'' by index ''{1}''. Root cause is : ''{2}'' ",
                            locator, index, e.getMessage()));
        }
    }

    // 31. Deselect option by label
    public void deselectOptionByLabel(String locator, String labelText) {
        logger.info(MessageFormat.format("Deselecting option at ''{0}'' by label ''{1}''", locator, labelText));
        try {
            Select listbox = new Select(findWebElement(locator));
            listbox.deselectByVisibleText(labelText);
            logger.info(MessageFormat.format("Deselected option at ''{0}'' by label ''{1}'' successfully", locator,
                    labelText));
        } catch (Exception e) {
            logger.error(
                    MessageFormat.format("Cannot deselect option at ''{0}'' by label ''{1}''. Root cause is : ''{2}'' ",
                            locator, labelText, e.getMessage()));
        }
    }

    // 32. Deselect option by value
    public void deselectOptionByValue(String locator, String value) {
        logger.info(MessageFormat.format("Deselecting option at ''{0}'' by value ''{1}''", locator, value));
        try {
            Select listbox = new Select(findWebElement(locator));
            listbox.deselectByValue(value);
            logger.info(
                    MessageFormat.format("Deselected option at ''{0}'' by value ''{1}'' successfully", locator, value));
        } catch (Exception e) {
            logger.error(
                    MessageFormat.format("Cannot deselect option at ''{0}'' by value ''{1}''. Root cause is : ''{2}'' ",
                            locator, value, e.getMessage()));
        }
    }

    // 33. dismissAlert
    public void dismissAlert() {
        logger.info("Dismissing alert at browser !");
        try {
            WebDriver driver = driverManager.getDriver();
            driver.switchTo().alert().dismiss();
            logger.info("Dismissed alert successfully");
        } catch (Exception e) {
            logger.error(
                    MessageFormat.format("Cannot dismissing alert at browser. Root cause is: ''{0}''", e.getMessage()));
        }
    }

    // 34. get Alert Text
    public void getAlertText() {
        logger.info("Getting text alert at browser !");
        try {
            WebDriver driver = driverManager.getDriver();
            String alertText = driver.switchTo().alert().getText();
            logger.info("Got text of alert successfully. Text of alert is: " + alertText);
        } catch (Exception e) {
            logger.error(MessageFormat.format("Cannot get text of alert at browser. Root cause is: ''{0}''",
                    e.getMessage()));
        }
    }

    // 35. get height
    public void getElementHeight(String locator) {
        WebElement el = findWebElement(locator);
        logger.info("Getting height of element: " + locator);
        try {
            int height = el.getSize().height;
            logger.info(MessageFormat.format("Got height of ''{0}'' successfully. Height of ''{1}'' is ''{2}''px",
                    locator, el, height));
        } catch (Exception e) {
            logger.error(MessageFormat.format("Cannot get height of ''{0}''. Root cause is: ''{1}''", locator,
                    e.getMessage()));
        }
    }

    // 36. get width
    public void getElementWidth(String locator) {
        WebElement el = findWebElement(locator);
        logger.info("Getting width of element: " + locator);
        try {
            int width = el.getSize().width;
            logger.info(MessageFormat.format("Got width of ''{0}'' successfully. Width of ''{1}'' is ''{2}''px",
                    locator, el, width));
        } catch (Exception e) {
            logger.error(MessageFormat.format("Cannot get width of ''{0}''. Root cause is: ''{1}''", locator,
                    e.getMessage()));
        }
    }

    // 37. get getNumberOfSelectedOption
    public void getNumberOfSelectedOption(String locator) {
        logger.info("Getting number of select option: " + locator);
        try {
            Select selection = new Select(findWebElement(locator));
            int number = selection.getAllSelectedOptions().size();
            logger.info(MessageFormat.format("Got number of select option ''{0}'' successfully. Number is: ''{1}''",
                    locator, number));
        } catch (Exception e) {
            logger.error(MessageFormat.format("Cannot get number of select option ''{0}''. Root cause is: ''{1}''",
                    locator, e.getMessage()));
        }
    }

    // 38. getPageHeight
    public void getPageHeight() {
        logger.info("Getting height of page");
        try {
            WebDriver driver = driverManager.getDriver();
            int height = driver.manage().window().getSize().height;
            logger.info(MessageFormat.format("Got height of page successfully. Height is: ''{0}''px", height));
        } catch (Exception e) {
            logger.error(MessageFormat.format("Cannot get height of page. Root cause is: ''{0}''", e.getMessage()));
        }
    }

    // 38. getPageWidth
    public void getPageWidth() {
        logger.info("Getting width of page");
        try {
            WebDriver driver = driverManager.getDriver();
            int width = driver.manage().window().getSize().width;
            logger.info(MessageFormat.format("Got width of page successfully. Width is: ''{0}''px", width));
        } catch (Exception e) {
            logger.error(MessageFormat.format("Cannot get width of page. Root cause is: ''{0}''", e.getMessage()));
        }
    }

    // 39. scrollToPosition
    public void scrollToPosition(int x, int y) {
        logger.info(MessageFormat.format("Scrolling to position x: ''{0}'' and y: ''{1}''", x, y));
        try {
            WebDriver driver = driverManager.getDriver();
            Actions act = new Actions(driver);
            act.moveByOffset(x, y).click().build().perform();
            logger.info(MessageFormat.format("Scrolled to position x: ''{0}'' and y: ''{1}'' successfully ", x, y));
        } catch (Exception e) {
            logger.error(
                    MessageFormat.format("Cannot scroll to position x: ''{0}'' and y: ''{1}''. Root cause is: ''{2}''",
                            x, y, e.getMessage()));
        }
    }

    // 40. selectAllOption
    public void selectAllOption(String locator) {
        logger.info(MessageFormat.format("Selecting all option in ''{0}''", locator));
        try {
            Select selectElement = new Select(findWebElement(locator));
            if (selectElement.isMultiple()) {
                List<WebElement> selectOptions = selectElement.getOptions();
                for (WebElement option : selectOptions) {
                    selectElement.selectByVisibleText(option.getText());
                }
                logger.info(MessageFormat.format("Selected all option in ''{0}'' successfully", locator));
            }
        } catch (Exception e) {
            logger.error(MessageFormat.format("Cannot select all option in ''{0}''. Root cause is : ''{1}''", locator,
                    e.getMessage()));
        }
    }

    // 42. takeScreenshot
    @Attachment(value = "Form screenshot", type = "image/png")
    public byte[] takeScreenshot() {
        logger.info("Taking screen shot on browser");
        try {
            WebDriver driver = driverManager.getDriver();
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            logger.error(MessageFormat.format("Cannot take screen shot. Root cause is : ''{1}''", e.getMessage()));
        }
        return null;
    }

    // 43. uploadFile
    public void uploadFile(String locator, String file) {
        logger.info(MessageFormat.format("Uploading file ''{0}'' to ''{1}''", file, locator));
        try {
            WebElement we = findWebElement(locator);
            we.sendKeys(file);
            logger.info(MessageFormat.format("Uploaded file ''{0}'' to ''{1}'' successfully ", file, locator));
        } catch (Exception e) {
            logger.error(MessageFormat.format("Cannot upload file ''{0}'' to ''{1}''. Root cause is : ''{2}''", file,
                    locator, e.getMessage()));
        }
    }

    // 44. verifyOptionSelectedByLabel
    public boolean verifyOptionSelectedByLabel(String locator, String expectedLabel) {
        WebElement el = findWebElement(locator);
        logger.info(MessageFormat.format("Verifying labelText in element located by '{0}'", locator));
        try {
            String actualTextLabel = el.getText().trim();
            if (actualTextLabel.equals(expectedLabel)) {
                logger.info(MessageFormat.format("Actual labelText ''{0}'' and expected text ''{1}'' are match",
                        actualTextLabel, expectedLabel));
                return true;
            } else {
                logger.error(MessageFormat.format("Actual labelText ''{0}'' and expected text ''{1}'' are not match",
                        actualTextLabel, expectedLabel));
                return false;
            }
        } catch (Exception e) {
            logger.error(
                    MessageFormat.format("Cannot verify labelText of web element located by '{0}'. Root cause is '{1}'",
                            locator, e.getMessage()));
        }
        return false;
    }

    // 45. verifyOptionSelectedByValue
    public boolean verifyOptionSelectedByValue(String locator, String expectedValue) {
        WebElement el = findWebElement(locator);
        logger.info(MessageFormat.format("Verifying value selected in element located by '{0}'", locator));
        try {
            String actualValue = el.getText().trim();
            if (actualValue.equals(expectedValue)) {
                logger.info(MessageFormat.format("Actual value ''{0}'' and expected value ''{1}'' are match",
                        actualValue, expectedValue));
                return true;
            } else {
                logger.error(MessageFormat.format("Actual value ''{0}'' and expected value ''{1}'' are not match",
                        actualValue, expectedValue));
                return false;
            }
        } catch (Exception e) {
            logger.error(
                    MessageFormat.format("Cannot verify value of web element located by '{0}'. Root cause is '{1}'",
                            locator, e.getMessage()));
        }
        return false;
    }

    // 46. rightClick
    public void rightClick(String locator) {
        logger.info(MessageFormat.format("RightClicking element ''{0}''", locator));
        try {
            WebDriver driver = driverManager.getDriver();
            WebElement el = findWebElement(locator);
            Actions action = new Actions(driver);
            action.contextClick(el).build().perform();
        } catch (Exception e) {
            logger.error(MessageFormat.format("Cannot rightClick element ''{0}''. Root cause is: ''{1}''", locator,
                    e.getMessage()));
        }
    }

    // 47. waitForPageLoad
    public void waitForPageLoad(int timeout) {
        logger.info("Waiting for page load with time is: " + timeout);
        try {
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeout));
            wait.until(webDriver -> "complete"
                    .equals(((JavascriptExecutor) webDriver).executeScript("return document.readyState")));
            logger.info("Waited for page load successfully!");
        } catch (Exception e) {
            logger.error(MessageFormat.format("Cannot wait for page load. Root cause is ''{0}''", e.getMessage()));
        }
    }

    // 48. waitForElementVisible
    public void waitForElementVisible(int timeout) {
        logger.info("Waiting for element visible with time is: " + timeout);
        try {
//			WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeout));
//			wait.until(webDriver -> "complete"
//					.equals(((JavascriptExecutor) webDriver).executeScript("return document.readyState")));
            logger.info("Waited for element visible successfully!");
        } catch (Exception e) {
            logger.error(
                    MessageFormat.format("Cannot wait for element visible. Root cause is ''{0}''", e.getMessage()));
        }
    }

    // 49. verifyElementClass
    public boolean verifyElementClass(String locator, String expectedClass) {
        logger.info("Verifying class of element");
        List<WebElement> els = findWebElements(locator);
        boolean bl = false;
        try {
            for (WebElement el : els) {
                String actualClass = el.getAttribute("class");
                if (actualClass == expectedClass) {
                    logger.info(MessageFormat.format(
                            "actualClass ''{0}'' and expectedClass ''{1}'' are match. Verified class of element ''{2}'' successfully",
                            actualClass, expectedClass, el));
                    bl = true;
                } else {
                    logger.info(MessageFormat.format("actualClass ''{0}'' and expectedClass ''{1}'' are not match",
                            actualClass, expectedClass));
                    bl = false;
                }
            }
            return bl;
        } catch (Exception e) {
            logger.error(MessageFormat.format("Cannot verify class of element ''{0}''. Root cause is: ''{1}''", els,
                    e.getMessage()));
            return bl = false;
        }
    }

    // 50. getTextItemsOfList
    public List<String> getTextItemsOfList(String locator) {
        logger.info(MessageFormat.format("Getting text of items in array with item by ''{0}''", locator));
        List<String> listNameItems = new ArrayList<String>();
        try {
            List<WebElement> els = findWebElements(locator);
            for (WebElement el : els) {
                String item = getText(el);
                listNameItems.add(item);
            }
            logger.info(MessageFormat.format("List text of items in array are: ''{0}'' ", listNameItems));
        } catch (Exception e) {
            logger.error(MessageFormat.format("Cannot get list text of items in array. Root cause is: ''{0}''",
                    e.getMessage()));
        }
        return listNameItems;
    }

    // 51. verify title of website
    public boolean verifyTitleWebsite(String expectedTitle) {
        logger.info("Verifying title of the website");
        boolean bl = false;
        try {
            String actualTitle = getTitle();
            if (actualTitle != expectedTitle) {
                bl = false;
                logger.info(MessageFormat.format(
                        "Verify title of the website successfully. ActualTitle ''{0}'' and expectedTitle ''{1}'' are not match",
                        actualTitle, expectedTitle));
            } else {
                bl = true;
                logger.info("Verify title of the website successfully. ActualTitle and expectedTitle are match");
            }
        } catch (Exception e) {
            logger.error(
                    MessageFormat.format("Cannot verify title of the website. Root cause is: ''{0}''", e.getMessage()));
        }
        return bl;
    }

    // 52. get attribute element by element
    public String getAttribute(WebElement el, String attributeName) {
        logger.info(MessageFormat.format("Getting Attribute ''{0}'' of web element ''{1}''", attributeName, el));
        try {
            String attributeValue = el.getAttribute(attributeName);
            logger.info(MessageFormat.format("Got Attribute ''{0}'' of web element ''{1}'' successfully: ''{2}''",
                    attributeName, el, attributeValue));
            return attributeValue;
        } catch (Exception e) {
            logger.info(MessageFormat.format("Cannot get attribute of ''{0}''. Root cause is: ''{1}''", el,
                    e.getMessage()));
            return null;
        }
    }

    // 53. scroll to web element
    public void scrollToElement(WebElement element) {
        logger.info("Scrolling to element :" + element);
        try {
            WebDriver driver = driverManager.getDriver();
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
            logger.info(MessageFormat.format("Scrolled to element ''{0}'' successfully", element));
        } catch (Exception e) {
            logger.error(MessageFormat.format("Cannot scroll to element ''{0}'' . Root cause is: ''{1}'' ", element,
                    e.getMessage()));
        }
    }

    // 54. return boolean attribute element
    public boolean getAttributeBl(String locator, String attributeName) {
        WebElement el = findWebElement(locator);
        boolean bl = false;
        logger.info(MessageFormat.format("Getting Attribute ''{0}'' of web element located by ''{1}''", attributeName,
                locator));
        try {
            bl = Boolean.valueOf(el.getAttribute(attributeName));
            logger.info(MessageFormat.format(
                    "Got Attribute ''{0}'' of web element located by ''{1}'' successfully: ''{2}''", attributeName,
                    locator, bl));
        } catch (Exception e) {
            logger.info(MessageFormat.format("Cannot get attribute of ''{0}''. Root cause is: ''{1}''", locator,
                    e.getMessage()));
        }
        return bl;
    }

    // 55. verify value int in element
    public boolean verifyElementValueBl(String locator) {
        WebElement el = findWebElement(locator);
        logger.info(MessageFormat.format("Verifying value in element located by ''{0}''", locator));
        try {
            int actualValue = Integer.parseInt(el.getText().trim().replaceAll("\"", "").replaceAll(",", ""));
            logger.info(MessageFormat.format("actualValue : ''{0}'' ", actualValue));
            if (actualValue > 0) {
                logger.info(MessageFormat.format("Actual value ''{0}'' > 0", actualValue));
                return true;
            } else {
                logger.error(MessageFormat.format("Actual value ''{0}'' < 0 ", actualValue));
                return false;
            }
        } catch (Exception e) {
            logger.error(
                    MessageFormat.format("Cannot verify value of web element located by ''{0}''. Root cause is ''{1}''",
                            locator, e.getMessage()));
        }
        return false;
    }

    // 56. verify contains value int in element
    public boolean verifyElementContainsValueBl(String locator, String textSplit) {
        WebElement el = findWebElement(locator);
        logger.info(MessageFormat.format("Verifying value in element located by ''{0}''", locator));
        try {
            String[] part = el.getText().split(textSplit);
            String part2 = part[1].substring(1, 3);
            int actualValue = Integer.parseInt(part2);
            if (actualValue > 0) {
                logger.info(MessageFormat.format("Actual value ''{0}'' > 0", actualValue));
                return true;
            } else {
                logger.error(MessageFormat.format("Actual value ''{0}'' < 0 ", actualValue));
                return false;
            }
        } catch (Exception e) {
            logger.error(
                    MessageFormat.format("Cannot verify value of web element located by ''{0}''. Root cause is ''{1}''",
                            locator, e.getMessage()));
            fail(MessageFormat.format("Cannot verify value of web element located by ''{0}''. Root cause is ''{1}''",
                    locator, e.getMessage()));
        }
        return false;
    }

    public boolean waitForElementClickable(String locator, int timeOut) {
        WebElement foundElement = findWebElement(locator);
        WebDriver driver = driverManager.getDriver();
        logger.info(MessageFormat.format("Waiting for web element ''{0}'' clickable", foundElement));
        try {
            Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
            WebElement we = (WebElement) wait.until(ExpectedConditions.elementToBeClickable(foundElement));
            if (we != null) {
                logger.info(MessageFormat.format("Waited for web element ''{0}'' clickable within ''{1}'' second(s)",
                        we, timeOut));
                return true;
            }
            logger.error(MessageFormat.format(
                    "Cannot wait for web element ''{0}'' clickable. Root cause: the web element ''{0}'' is null", we));
        } catch (Exception e) {
            logger.error(MessageFormat.format("Cannot wait for web element ''{0}'' clickable. Root cause: ''{1}''",
                    foundElement, e.getMessage()));
            fail(MessageFormat.format("Cannot wait for web element ''{0}'' clickable. Root cause: ''{1}''",
                    foundElement, e.getMessage()));
        }
        return false;
    }

    public void refresh() {
        logger.info("Refreshing page");
        WebDriver driver = driverManager.getDriver();
        driver.navigate().refresh();
        logger.info("Refreshed page");
    }
}