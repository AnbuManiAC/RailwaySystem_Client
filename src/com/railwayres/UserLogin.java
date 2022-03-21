package com.railwayres;

import java.util.Scanner;

import com.railway.db.PassengerTable;
import com.railway.db.StationTable;
import com.railway.db.TicketTable;
import com.railway.db.TrainTable;
import com.railway.db.User;
import com.railway.db.UserTable;

public class UserLogin {
	
	TrainTable trains = TrainTable.getInstance();
	StationTable stations = StationTable.getInstance();
	PassengerTable passengers = PassengerTable.getInstance();
	TicketTable tickets = TicketTable.getInstance();
	UserTable users = UserTable.getInstance();
	User user;
	
	
	Scanner sc = new Scanner(System.in);
	String choice;
	boolean outerloop = true;

	public void showUserLogin() {
		home:
		while(outerloop) {
				System.out.println("\n******************************");
				System.out.println("  Railway Reservation System");
				System.out.println("******************************");
				System.out.println("\n1. Register\n2. Login\n3. Exit\n");
				System.out.println("\nEnter your choice : ");
				choice = sc.nextLine();
				switch(choice) {
				case "1":
					System.out.println("\n---Signup Page---\n");
					
					String username, pwd;
					while(true) {
						System.out.println("\nEnter username : ");
						username = sc.nextLine();
						if(!isValidName(username)) {
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
						System.out.println("\nEnter password : ");
						pwd = sc.nextLine();
						if(pwd.length()<5) {
							System.out.println("\n# Password must be 5 characters long #\n");
							continue;
						}
						break;
					}
					user = users.createUser(username,pwd);
					users.insertUser(user);
					System.out.println("\nSuccessfully registered.\nLogin to continue\n");
					
				case "2":
					System.out.println("\n---Login Page---\n");
					while(true) {
						System.out.println("\nEnter Username : ");
						username = sc.nextLine();
						while(true) {
							if(users.checkUser(username)) {
								System.out.println("\nEnter Password : ");
								pwd = sc.nextLine();
								if(users.isExistingUser(username, pwd)) {
									System.out.println("\nSuccessfully logged in\n");
									Menu menu = new Menu();
									menu.showMenu();
									continue home;
								}
								else {
									System.out.println("\nIncorrect password. Enter correct password to proceed #\n");
									continue;
								}
							}
							else {
								System.out.println("\nUser doesn't exists.");
								continue home;		
							}
						}
					}
				case "3":
					outerloop = false;
					System.exit(0);
					sc.close();
					break;
					
				default:
					System.out.println("\nEnter valid choice\n");
					break;
				}
		
		}
	}
	private static boolean isValidName(String name) {
		if(name.length()>2 && name.matches("^[a-zA-Z]+$"))
			return true;
		return false;
	}
	
}
