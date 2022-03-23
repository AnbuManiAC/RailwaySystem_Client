package com.railwayres.app;

import java.util.Scanner;

import com.railway.db.UserTable;
import com.railwayres.service.FunctionalitiesProvider;
import com.railwayres.service.LoginDataPrinterProvider;

public class Menu {

	private final Scanner sc = new Scanner(System.in);
	boolean loop = true;
	String choice;
	
	private final FunctionalitiesProvider func;
	private UserTable users;
	private LoginDataPrinterProvider printer;
	
	public Menu(){
		func = new FunctionalitiesProvider();
		users = UserTable.getInstance();
		printer = new LoginDataPrinterProvider();
		
	}
	void printMenu() {
		System.out.println("\n----- MENU -----\n");
		System.out.println("1. Show list of Trains");
		System.out.println("2. Search Train");
		System.out.println("3. Book Ticket");
		System.out.println("4. Cancel Ticket");
		System.out.println("5. Check Ticket Status");
		System.out.println("6. Print Booked Tickets");
		System.out.println("7. Show all tickets");
		System.out.println("8. Logout");
		
		System.out.print("Choose any of the above options : ");
	}
	void showMenu() {
		loop = true;
		while(loop)
		{
			printMenu();
			choice = sc.next();
			switch(choice) {
			case "1":
				func.showTrains();
				break;
				
			case "2":
				func.findTrain();
				break;
				
			case "3":
				func.ticketBooker();
				break;
			case "4":
				func.cancelBookedTicket();
				break;
				
			case "5":
				func.showTicketStatus();
				break;
				
			case "6":
				func.showBookedTickets();
				break;
				
			case "7":
				func.printAllTickets();
				break;
				
			case "8":
				loop = false;
				users.changeUserStatus(users.getCurrentUser());
				printer.showLogoutMessage();
				return;				
			default:
				System.out.println("\nEnter valid option!\n");
				break;
		
			}
		}
	}

	void printTitle() {

		String title = "Railway Reservation System";
		func.hasStar();
		System.out.print(title);
		func.hasStar();
	}
	
}
