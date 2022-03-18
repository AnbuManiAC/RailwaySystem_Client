package com.railwayres;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import com.railway.db.*;
import com.railway.train.*;
import com.railway.train.booking.*;

public class Menu {

	public static void main(String[] args) {
		
		Database db = Database.getInstance();
		
		Scanner sc = new Scanner(System.in);
		String choice;
		boolean outerloop = true;
		while(outerloop) {
			home: 	
			while(true){
				System.out.println("\n******************************");
				System.out.println("  Railway Reservation System");
				System.out.println("******************************");
				System.out.println("\n1. Register\n2. Login\n3. Exit\n");
				System.out.println("\nEnter your choice : ");
				choice = sc.next();
				switch(choice) {
				case "1":
					System.out.println("\n---Signup Page---\n");
					
					String username, pwd;
					while(true) {
						System.out.println("\nEnter username : ");
						username = sc.next();
						if(!isValidName(username)) {
							System.out.println("\n# Username must be 3 character long and must only have letters #\n");
							continue;
						}
						if(db.checkUser(username)) {
							System.out.println("\n# User already exists. #\n");
							continue home;
						}
						break;
					}	
					
					while(true) {
						System.out.println("\nEnter password : ");
						pwd = sc.next();
						if(pwd.length()<5) {
							System.out.println("\n# Password must be 5 characters long #\n");
							continue;
						}
						break;
					}
					User user = new User(username,pwd);
					db.insertUser(user);
					System.out.println("\nSuccessfully registered.\nLogin to continue\n");
					
				case "2":
					System.out.println("\n---Login Page---\n");
					while(true) {
						System.out.println("\nEnter Username : ");
						username = sc.next();
						while(true) {
							if(db.checkUser(username)) {
								System.out.println("\nEnter Password : ");
								pwd = sc.next();
								if(db.isExistingUser(username, pwd)) {
									System.out.println("\nSuccessfully logged in\n");
									showMenu();
									break home;
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
	}
					

	private static boolean isValidName(String name) {
		if(name.length()>2 && name.matches("[a-zA-z]+"))
			return true;
		return false;
	}
	
	private static void showMenu() {
		Database db = Database.getInstance();
		boolean innerloop = true;
		String choice;
		Scanner sc = new Scanner(System.in);
		menu:
		while(innerloop)
		{
			System.out.println("\n-----MENU-----");
			System.out.println("\n1. Show all trains\n2. Search Train\n3. Book Ticket\n4. Cancel Ticket\n5. Check Ticket Status\n6. Show tickets\n7. Logout\n\n");
			System.out.println("Enter your choice : ");
			choice = sc.next();
			switch(choice) {
			case "1":
				List<Train> trains = db.getTrains();
				if(trains.size()>0) {
					System.out.println("\nAvailable Trains : \n");
					for(Train train: trains) {
						System.out.println("["+train.getTrainInfo()+"]\n");
					}
				}
				else {
					System.out.println("\n----------No train available\n");
				}
				break;
				
			case "2":
				String source, destination;
				LocalDate date;
				
				while(true) {
					System.out.println("\nEnter source : ");
					source = sc.next();
					if(!db.isStationAvailable(source)) {
						System.out.println("\nEnter valid station\n");
						continue;
					}
					break;
				}
				
				while(true) {
					System.out.println("\nEnter destination : ");
					destination = sc.next();
					if(!db.isStationAvailable(destination)) {
						System.out.println("\nEnter valid station\n");
						continue;
					}
					if(source.equalsIgnoreCase(destination)){
						System.out.println("\nSource and destination must not be same\nEnter valid destination\n");
						continue;
					}
					
					break;
				}
				
				while(true) {
					System.out.println("\nEnter Date(yyyy-mm-dd) : ");
					String _date = sc.next();
					if(!isValidDate(_date)) {
						System.out.println("\n# Enter valid date / Enter date in specified format #");
						continue;
					}
					if(!isLogicalDate(_date)) {
						System.out.println("\n# Date must not be in the past #");
						continue;
					}
					date = LocalDate.parse(_date);
					break ;
					
				}
							
				ArrayList<Train> filteredTrains =  db.searchTrain(source,destination,date);
				if(filteredTrains.size()>0) {
					for(Train train: filteredTrains) {
						System.out.println(train.getTrainInfo() + "Available seats : " +db.getAvailableSeatCount(train, date));
					}
				}
				else {
					System.out.println("\nNo Trains availaible.");
				}
				
				
				break;
				
			case "3":
				String name, trainNumber, berthPreference, gender, bookingdate;
				int age,numberOfTicket;
				LocalDate bookingDate;
				Train train;
				
					while(true) {
						System.out.println("\nEnter train number : ");
						trainNumber = sc.next();
						train = db.getTrainFromNumber(trainNumber);
						if(train==null) {
							System.out.println("\n# Enter valid train number or use search train option #");
							continue;
						}
						break;
					}
				
					while(true) {
						System.out.println("\nEnter date of journey (yyyy-mm-dd) : ");
						bookingdate = sc.next();
						if(!isValidDate(bookingdate)) {
							System.out.println("\n# Enter valid date #");
							continue;
						}
						if(!isLogicalDate(bookingdate)) {
							System.out.println("\n# Date must not be in the past #");
							continue;
						}
						bookingDate = LocalDate.parse(bookingdate);
						break;
						
					}
				
				while(true) {
					boolean isInt = false;
					while(!isInt) {
						try {
							System.out.println("\nEnter number of tickets : ");

							numberOfTicket = sc.nextInt();
							isInt = true;
							
							if(numberOfTicket<1) {
								System.out.println("\n# Enter valid number of tickets #\n");
								continue;
							}
							int seatCount = db.getAvailableSeatCount(train, bookingDate);
							if(seatCount<0) {
								System.out.println("\n# Ticket booking not opened for that date #\n");
								continue menu;
							}
							if(seatCount<numberOfTicket) {
								System.out.println("\n# Only " +seatCount+ " tickets available #\nPress 1 to continue ticket booking or press 2 to goto Menu :");
								int opt = sc.nextInt();
								if(opt == 1)
									continue;
								if(opt == 2)
									continue menu;
								else {
									System.out.println("\n# Invalid input #\n");
									continue menu;
								}
									
							}
							else {
								int i=1;
								List<Ticket> passengerTickets = new ArrayList<>();

								while(i<=numberOfTicket){
									if(numberOfTicket==1)
										System.out.println("\nPassenger details\n");
									else
										System.out.println("\nPassenger " +i+ " details\n");
									i++;
										while(true) {
											System.out.println("\nEnter name : ");
											name = sc.next();
											if(!isValidName(name)) {
												System.out.println("\n# Enter valid name #");
												continue;
											}
											break;
										}
										
										while(true) {
											System.out.println("\nEnter age : ");
											age = sc.nextInt();
											if(age<=0 || age>125) {
												System.out.println("\n# Enter valid age #");
												continue;
											}
											break;
										}
										
										while(true) {
											System.out.println("\nGender(Male/Female/Other/NA(prefer not to say) : ");
											gender = sc.next();
											if(!(gender.equals("Male") || gender.equals("Female") || gender.equals("Other") || gender.equals("NA"))) {
												System.out.println("\n# Enter Gender as mentioned #");
												continue;
											}
											break;
										}
										
										while(true) {
												System.out.println("\nEnter berth preference(L for Lower/M for Middle/U for upper) : ");
												berthPreference = sc.next();
												if(!(berthPreference.equals("L") || berthPreference.equals("M") || berthPreference.equals("U"))) {
													System.out.println("\n# Enter valid berth / Enter as specified #");
													continue;
												}
												break;
												
											}
										
										
										Passenger passenger = new Passenger(name, age, gender, berthPreference);
										Ticket passengerTicket = Booking.bookTicket(passenger,train,bookingDate);
										if(passengerTicket!=null)
										{
											if(passengerTicket.getBerthAlloted().equals(passenger.getBerthPreference()))
												System.out.println("\nPreferred berth available\nSuccessfully booked\n");
											else if(passengerTicket.getBerthAlloted().equals("WL")){
												System.out.println("\nNo tickets available... You are in waiting list.\n");
											}
											else {
												System.out.println(String.format("\nPreferred berth unavailable...\nAlloted berth : %s\n",passengerTicket.getBerthAlloted()));

											}
											db.getPassenger().add(passenger);
											passengerTickets.add(passengerTicket);
											
										}
								}
								System.out.println("\n----------Here's your ticket\n");
								for(Ticket ticket : passengerTickets)
									ticket.generateTicket();
							}
							
						}
						catch(Exception e) {
							isInt = false;
							System.out.println("\nEnter valid ticket count \n");
							sc.nextLine();
						}
					}
					
					break;

				}
				break;
				
			case "4":
				System.out.println("\nEnter PNR to cancle ticket : ");
				String pnr = sc.next();
				if(Booking.cancelTicket(pnr)) {
					System.out.println("\nTicket cancelled successfully\n");
				}
				else {
					System.out.println("\nInvalid PNR! Enter valid PNR.\n");
				}
				break;
				
			case "5":
				System.out.println("\nEnter PNR : \n");
				String PNR = sc.next();
				Ticket ticket = db.getTicketfromPnr(PNR);
				if(ticket==null) {
					System.out.println("\nTicket not found\n");
				}
				else {
					ticket.generateTicket();
				}
				break;
				
			case "6":
				List<Ticket> berthTicket = db.getBerthTicket();
				LinkedList<Ticket> racTicket = db.getRacTicket();
				LinkedList<Ticket> waitingList = db.getWaitingList();
				
				if(berthTicket.size()>0 || racTicket.size()>0 || waitingList.size()>0) {
				
					for(Ticket t:berthTicket) {
						t.generateTicket();
					}
					for(Ticket t:racTicket) {
						t.generateTicket();
					}
					for(Ticket t:waitingList) {
						t.generateTicket();
					}
					
				}
				else {
					System.out.println("\nNo ticktes found.");
				}
				break;
				
			case "7":
				innerloop = false;
				return;				
			default:
				System.out.println("\nEnter valid choice\n");
				break;
		
			}
		}
	}
	
	private static boolean isValidDate(String date) {
		
		final Pattern DATE_PATTERN = Pattern.compile(
			      "^((2000|2400|2800|(2[0-9](0[48]|[2468][048]|[13579][26])))-02-29)$" 
			      + "|^((2[0-9]{3})-02-(0[1-9]|1[0-9]|2[0-8]))$"
			      + "|^((2[0-9]{3})-(0[13578]|10|12)-(0[1-9]|[12][0-9]|3[01]))$" 
			      + "|^((2[0-9]{3})-(0[469]|11)-(0[1-9]|[12][0-9]|30))$");

			    
		return DATE_PATTERN.matcher(date).matches();
	}
	private static boolean isLogicalDate(String date) {
		LocalDate _date = LocalDate.parse(date);
		LocalDate presentDate = LocalDate.now();
		if(_date.compareTo(presentDate)>=0) {
			return true;
		}
		return false;
	}
}


