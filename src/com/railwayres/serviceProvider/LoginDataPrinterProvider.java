package com.railwayres.serviceProvider;

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

}
