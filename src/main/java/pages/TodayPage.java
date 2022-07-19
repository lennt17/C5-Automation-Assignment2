package pages;

import WebKeywords.WebKeywords;
import components.todayPage.HandleMenu;

public class TodayPage {
    public WebKeywords action;
    public HandleMenu handleMenu;
    public TodayPage(WebKeywords action){
        this.action = action;
        this.handleMenu = new HandleMenu(this.action);
    }
}
