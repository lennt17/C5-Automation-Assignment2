package pages;

import WebKeywords.WebKeywords;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.testng.Assert.assertFalse;

public class ProjectPage {
    public WebKeywords action;
    public ProjectPage projectPage;

    private String SPAN_INPUT_TASK = "//div[@class='task_editor__input_fields']//span";
    private String GROUP_DIV_NAME_TASK = "//ul[@class='items']/li//div[@class='markdown_content task_content']";
    private String GROUP_BTN_CHECKBOX_TASK = "//ul[@class='items']/li//button[@role='checkbox']";
    public ProjectPage(WebKeywords action){
        this.action = action;
    }
    @Step("Should to be have task")
    public boolean shouldToBeHaveTask(String nameTaskExpected){
        boolean bl = false;
        List<WebElement> els = action.findWebElements(GROUP_DIV_NAME_TASK);
        for(int i = 0; i < els.size(); i++){
            String actualName = action.getText(els.get(i));
            if(actualName.equals(nameTaskExpected)){
                bl = true;
            }
        }
        action.takeScreenshot();
        return bl;
    }

    @Step("Click checkbox task")
    public void clickCheckboxTask() {
        List<WebElement> els = action.findWebElements(GROUP_BTN_CHECKBOX_TASK);
        action.click(els.get(0));
        action.takeScreenshot();
    }

    @Step("Should to be not display task when click checkbox")
    public boolean shouldToBeNotDisplayTask() {
        List<WebElement> els = action.findWebElements(GROUP_DIV_NAME_TASK);
        action.takeScreenshot();
        return action.isVisible(GROUP_DIV_NAME_TASK);
    }
}
