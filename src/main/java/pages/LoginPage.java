package pages;

import WebKeywords.WebKeywords;
import io.qameta.allure.Step;

public class LoginPage {
    public WebKeywords action;
    private String INPUT_EMAIL = "//input[@id='labeled-input-1']";
    private String INPUT_PASSWORD = "//input[@id='labeled-input-3']";
    private String BTN_SUBMIT_LOGIN = "//button[@type='submit']";

    public LoginPage(WebKeywords action){
        this.action = action;
    }

    @Step("Input email")
    public void inputMail(String mail) {
        action.setText(INPUT_EMAIL, mail);
        action.takeScreenshot();
    }

    @Step("Input password")
    public void inputPassword(String password){
        action.setText(INPUT_PASSWORD, password);
        action.takeScreenshot();
    }

    @Step("CLick button login")
    public TodayPage clickBtnLogin(){
        action.click(BTN_SUBMIT_LOGIN);
        return new TodayPage(action);
    }

    @Step("Login with account")
    public TodayPage loginAccount(String mail, String password){
        inputMail(mail);
        inputPassword(password);
        clickBtnLogin();
        return new TodayPage(action);
    }
    }
