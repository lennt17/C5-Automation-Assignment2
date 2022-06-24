package components.todayPage;

import WebKeywords.WebKeywords;
import io.qameta.allure.Step;
import pages.TodayPage;

public class PopupSettings {
    public WebKeywords action;
    private String A_INTERGRATION = "//div[@role='menu']//a[10]";
    private String INPUT_TOKEN = "//input[@name='api_token']";
    private String BUTTON_EXIT = "//button[@aria-label='Close settings']";
    public PopupSettings(WebKeywords action){
        this.action = action;
    }

    @Step("Get access token")
    public String getAccessToken(){
        action.click(A_INTERGRATION);
        action.scrollToElement(INPUT_TOKEN);
        String accessToken = action.getAttribute(INPUT_TOKEN, "value");
        action.takeScreenshot();
        return accessToken;
    }

    @Step("Click button exit")
    public TodayPage clickExit(){
        action.click(BUTTON_EXIT);
        return new TodayPage(action);
    }
}
