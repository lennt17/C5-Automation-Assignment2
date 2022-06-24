package pages;

import WebKeywords.WebKeywords;
import components.todayPage.HandleMenu;
import components.todayPage.PopupSettings;
import io.qameta.allure.Step;

public class TodayPage {
    public WebKeywords action;
    public TodayPage todayPage;
    public HandleMenu handleMenu;
    private String BTN_ACCOUNT = "//button[@aria-label='Settings']";
    private String SPAN_SETTINGS = "//span[normalize-space()='Settings']";
    private String GROUP_LI_PROJECT = "//ul[@id='projects_list']/li";
    private String GROUP_A_PROJECT = "//ul[@id='projects_list']/li//div/a";
    private String GROUP_NAME_PROJECT = "//ul[@id='projects_list']/li//div/a/span[2]";


    public PopupSettings popupSettings;
    public TodayPage(WebKeywords action){
        this.action = action;
        this.popupSettings = new PopupSettings(this.action);
        this.handleMenu = new HandleMenu(this.action);
    }

    @Step("Should to be show today page")
    public boolean shouldToBeShowTodayPage(){
        action.takeScreenshot();
        return (action.isDisplayed(BTN_ACCOUNT));
    }

    @Step("click settings")
    public void clickSettings(){
        action.click(BTN_ACCOUNT);
        action.click(SPAN_SETTINGS);
        action.takeScreenshot();
    }
}
