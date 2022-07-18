package pages;

import WebKeywords.WebKeywords;
import components.todayPage.HandleMenu;
import components.todayPage.PopupSettings;

public class TodayPage {
    public WebKeywords action;
    public HandleMenu handleMenu;

    public PopupSettings popupSettings;
    public TodayPage(WebKeywords action){
        this.action = action;
        this.popupSettings = new PopupSettings(this.action);
        this.handleMenu = new HandleMenu(this.action);
    }
}
