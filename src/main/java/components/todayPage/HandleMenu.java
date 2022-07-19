package components.todayPage;

import WebKeywords.WebKeywords;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import pages.ProjectPage;

import java.util.List;

public class HandleMenu {
    public WebKeywords action;
    private String GROUP_A_PROJECT = "//ul[@id='projects_list']/li//div/a";
    private String GROUP_NAME_PROJECT = "//ul[@id='projects_list']/li//div/a/span[2]";

    public HandleMenu(WebKeywords action) {
        this.action = action;
    }

    @Step("Click project")
    public ProjectPage clickProject(String nameProject) {
        List<WebElement> els = action.findWebElements(GROUP_NAME_PROJECT);
        List<WebElement> elsa = action.findWebElements(GROUP_A_PROJECT);
        for (int i = 0; i < els.size(); i++) {
            String name = action.getText(els.get(i));
            if (name.equals(nameProject) && i == (els.size()) - 1) {
                action.click(elsa.get(i));
            }
        }
        action.takeScreenshot();
        return new ProjectPage(action);
    }
}
