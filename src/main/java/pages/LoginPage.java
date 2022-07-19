package pages;

import WebKeywords.WebKeywords;
import io.qameta.allure.Step;

public class LoginPage {
    public WebKeywords action;
    public final String INPUT_EMAIL = "//input[@type='email']";
    public final String INPUT_PASSWORD = "//input[@type='password']";
    public final String BTN_SUBMIT_LOGIN = "//button[@type='submit']";

    public LoginPage(WebKeywords action) {
        this.action = action;
    }

    @Step("Input email")
    public void inputMail(String mail) {
        action.setText(INPUT_EMAIL, mail);
        action.takeScreenshot();
    }

    @Step("Input password")
    public void inputPassword(String password) {
        action.setText(INPUT_PASSWORD, password);
        action.takeScreenshot();
    }

    @Step("CLick button login")
    public void clickBtnLogin() {
        action.click(BTN_SUBMIT_LOGIN);
    }

    @Step("Login with account")
    public TodayPage loginAccount(String mail, String password) {
        inputMail(mail);
        inputPassword(password);
        clickBtnLogin();
        return new TodayPage(action);
    }
}
