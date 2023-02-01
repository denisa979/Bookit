package com.bookit.pages;

import com.bookit.utilities.BrowserUtils;
import com.bookit.utilities.ConfigurationReader;
import com.bookit.utilities.Driver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.lang.module.Configuration;

public class SignInPage {
	
	public SignInPage() {
		PageFactory.initElements(Driver.get(), this);
	}	
	
	@FindBy(name="email")
	public WebElement emailField;

	@FindBy(name = "password")
	public WebElement passwordField;
	
	@FindBy(xpath = "//button[.='sign in']")
	public WebElement signInButton;


	public void login(String role) {
		String email = "";
		String password = "";

		switch (role) {
			case "teacher":
				email = ConfigurationReader.getProperty("teacher_email");
				password = ConfigurationReader.getProperty("teacher_password");
				break;

			case "team-member":
				email = ConfigurationReader.getProperty("team_member_email");
				password = ConfigurationReader.getProperty("team_member_password");
				break;
			case "team-leader":
				email = ConfigurationReader.getProperty("team_leader_email");
				password = ConfigurationReader.getProperty("team_leader_password");
				break;
			default:

				throw new RuntimeException("Invalid Role Entry : >> " + role + " <<");
		}
		emailField.sendKeys(email);
		passwordField.sendKeys(password);
		BrowserUtils.waitFor(1);
		signInButton.click();

	}
	
}
