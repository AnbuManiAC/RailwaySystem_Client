package com.railwayres.app;

import java.util.Scanner;

import com.railway.db.UserTable;
import com.railway.model.User;
import com.railway.util.Validator;
import com.railwayres.service.LoginDataPrinterProvider;

public class UserLogin {
	
	private UserTable users;
	private LoginDataPrinterProvider printer;
	private User user;
	private final Scanner sc;

	
	public UserLogin() {
		users = UserTable.getInstance();
		printer = new LoginDataPrinterProvider();
		sc = new Scanner(System.in);
		
	}

	
	public void printOptions() {
		System.out.println("\n---User Login Menu--- ");
		System.out.println("\n1. Signup");
		System.out.println("2. Login");
		System.out.println("3. Exit");
		System.out.print("Choose any of the above options : ");
	}

	void showUserLogin(Menu menu) {
		String choice;
		boolean loop = true;
		menu.printTitle();

		home:
		while(loop) {
			printOptions();
			choice = sc.nextLine();
			switch(choice) {
			case "1":
				System.out.println("\n---Signup Page---");
				
				String username, pwd;
				while(true) {
					System.out.print("\nEnter username : ");
					username = sc.nextLine();
					if(!Validator.isValidUserName(username)) {
						System.out.println("\n# Username must be 3 character long and must only have letters #\n");
						continue;
					}
					if(users.checkUser(username)) {
						System.out.println("\n# User already exists. #\n");
						continue home;
					}
					break;
				}	
				
				while(true) {
					System.out.print("\nEnter password : ");
					pwd = sc.nextLine();
					if(pwd.length()<5) {
						System.out.println("\n# Password must be 5 characters long #\n");
						continue;
					}
					break;
				}
				user = users.createUser(username,pwd);
				users.insertUser(user);
				printer.showSignupMessage();
				printer.showLoginTOContinue();
				
			case "2":
				System.out.println("\n---Login Page---");
				while(true) {
					System.out.print("\nEnter Username : ");
					username = sc.nextLine();
					while(true) {
						if(users.checkUser(username)) {
							System.out.print("\nEnter Password : ");
							pwd = sc.nextLine();
							user = users.searchUser(username,pwd);
							if(user!=null) {
								printer.showLoginMessage();
								users.changeUserStatus(user);;
								menu.showMenu();
								continue home;
							}
							else {
								System.out.println("\nIncorrect password. Enter correct password to proceed #\n");
								continue;
							}
						}
						else {
							System.out.println("\nUser doesn't exists!");
							continue home;		
						}
					}
				}
			case "3":
				loop = false;
				System.out.println("\n----- Thank You! ------");
				sc.close();
				System.exit(0);
				break;
				
			default:
				System.out.println("\nEnter valid option!\n");
				break;
			}
		
		}
	}

	
}
