package pages;

import WebKeywords.WebKeywords;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import utils.log.LogHelper;

public class HomePage {
    private static Logger logger = LogHelper.getLogger();
    public WebKeywords action;

    private String A_LOGIN = "//a[@class='_1vrBX-JExqmMRnkPWrYFN9'][normalize-space()='Log in']";

    public HomePage(WebKeywords action) {
        this.action = action;
    }

    @Step("Go to login page")
    public LoginPage clickLogin(){
        action.click(A_LOGIN);
        return new LoginPage(action);
    }
}
