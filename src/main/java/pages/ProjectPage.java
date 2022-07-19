package pages;

import WebKeywords.WebKeywords;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ProjectPage {
    public WebKeywords action;
    public final String GROUP_DIV_NAME_TASK = "//ul[@class='items']/li//div[@class='markdown_content task_content']";
    public final String GROUP_BTN_CHECKBOX_TASK = "//ul[@class='items']/li//button[@role='checkbox']";

    public ProjectPage(WebKeywords action) {
        this.action = action;
    }

    @Step("Should to be have task")
    public boolean shouldToBeHaveTask(String nameTaskExpected) {
        boolean bl = false;
        List<WebElement> els = action.findWebElements(GROUP_DIV_NAME_TASK);
        for (WebElement el : els) {
            String actualName = action.getText(el);
            if (actualName.equals(nameTaskExpected)) {
                bl = true;
            }
        }
        action.takeScreenshot();
        return bl;
    }

    @Step("Click checkbox task")
    public void clickCheckboxTask(String nameTask) {
        List<WebElement> els = action.findWebElements(GROUP_BTN_CHECKBOX_TASK);
        List<WebElement> elsNameTask = action.findWebElements(GROUP_DIV_NAME_TASK);
        for (int i = 0; i < els.size(); i++) {
            if (action.getText(elsNameTask.get(i)).equals(nameTask)) {
                action.click(els.get(i));
            }
        }
        action.takeScreenshot();
    }

    @Step("Should to be not display task when click checkbox")
    public boolean shouldToBeNotDisplayTask(String nameTask) {
        List<WebElement> els = action.findWebElements(GROUP_DIV_NAME_TASK);
        action.takeScreenshot();
        boolean bl = false;
        if (action.isVisible(GROUP_DIV_NAME_TASK)) {
            for (WebElement el : els) {
                if (action.getText(el).equals(nameTask)) {
                    bl = false;
                } else {
                    bl = true;
                }
            }
        } else {
            bl = true;
        }
        return bl;
    }
}
