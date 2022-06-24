package pages;

import WebKeywords.WebKeywords;
import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.testng.Assert.assertFalse;

public class ProjectPage {
    public WebKeywords action;
    public ProjectPage projectPage;

    public String BTN_ADD_NEW_TASK = "//button[normalize-space()='Add task']";
    private String SPAN_INPUT_TASK = "//div[@class='task_editor__input_fields']//span";
    private String BTN_ADD_TASK = "//button[@type='submit']";
    private String GROUP_DIV_NAME_TASK = "//ul[@class='items']/li//div[@class='markdown_content task_content']";
    private String GROUP_BTN_CHECKBOX_TASK = "//ul[@class='items']/li//button[@role='checkbox']";
    public ProjectPage(WebKeywords action){
        this.action = action;
    }

    @Step("Click button add new task")
    public void clickBtnAddNewTask(){
        action.click(BTN_ADD_NEW_TASK);
    }
    @Step("Input name task")
    public void inputNameTask(String nameTask) {
        action.click(SPAN_INPUT_TASK);
        action.setText(SPAN_INPUT_TASK, nameTask);
    }
    @Step("Click button add task")
    public void clickBtnAddTask(){
        action.click(BTN_ADD_TASK);
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
        return bl;
    }

    @Step("Checkbox task")
    public void checkboxTask() {
        List<WebElement> els = action.findWebElements(GROUP_BTN_CHECKBOX_TASK);
        action.click(els.get(0));
    }

    @Step("Should to be not display task when click checkbox")
    public void shouldToBeNotDisplayTask() {
        List<WebElement> els = action.findWebElements(GROUP_DIV_NAME_TASK);
        assertFalse(action.isDisplayed(els.get(0)));
    }
}
