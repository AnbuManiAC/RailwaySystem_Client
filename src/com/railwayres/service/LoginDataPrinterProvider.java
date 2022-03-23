package com.railwayres.service;

import com.railway.service.LoginDataPrinter;

public class LoginDataPrinterProvider implements LoginDataPrinter{
	
	public void showLoginMessage() {
		System.out.println("\nSuccessfully logged in!");
	}
	
	public void showLogoutMessage() {
		System.out.println("\nSuccessfully logged out!");
	}
	
	public void showSignupMessage() {
		System.out.println("\nSuccessfully registered!");
	}
	public void showLoginTOContinue() {
		System.out.println("\nLogin to proceed.");
	}

}
