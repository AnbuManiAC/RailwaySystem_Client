package com.railwayres;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import com.railway.db.PassengerTable;
import com.railway.db.StationTable;
import com.railway.db.TicketTable;
import com.railway.db.TrainTable;
import com.railway.db.UserTable;
import com.railway.train.Train;
import com.railway.train.booking.Booking;
import com.railway.train.booking.Cancellation;
import com.railway.train.booking.Passenger;
import com.railway.train.booking.Ticket;

public class Menu {

	
	boolean innerloop = true;
	String choice;
	Scanner sc = new Scanner(System.in);
	TrainTable trains = TrainTable.getInstance();
	StationTable stations = StationTable.getInstance();
	PassengerTable passengers = PassengerTable.getInstance();
	TicketTable tickets = TicketTable.getInstance();
	UserTable users = UserTable.getInstance();

	public void showTitle() {
		System.out.println("\n******************************");
		System.out.println("  Railway Reservation System ");
		System.out.println("******************************");
	}
	
	public void showMenu(int i) {
		System.out.println("\n-----MENU-----");
		System.out.println("1. Show list of Trains");
		System.out.println("2. Search Train");
		System.out.println("3. Book Ticket");
		System.out.println("4. Cancel Ticket");
		System.out.println("5. Check Ticket Status");
		System.out.println("6. Print Tickets");
		System.out.println("7. Show all tickets");
		System.out.println("8. Logout");
		
		System.out.print("Choose any of the above options : ");
	}
	public void showMenu() {
		menu:
			while(innerloop)
			{
				System.out.println("\n-----MENU-----");
				System.out.println("\n1. Show all trains\n2. Search Train\n3. Book Ticket\n4. Cancel Ticket\n5. Check Ticket Status\n6. Show user tickets\n7. Show all tickets\n8. Logout\n\n");
				System.out.println("Enter your choice : ");
				choice = sc.next();
				switch(choice) {
				case "1":
					List<Train> trainList = trains.getTrains();
					if(trainList.size()>0) {
						System.out.println("\nAvailable Trains : \n");
						for(Train train: trainList) {
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
						source = sc.nextLine();
						if(!stations.isStationAvailable(source)) {
							System.out.println("\nEnter valid station\n");
							continue;
						}
						break;
					}
					
					while(true) {
						System.out.println("\nEnter destination : ");
						destination = sc.nextLine();
						if(!stations.isStationAvailable(destination)) {
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
						String _date = sc.nextLine();
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
								
					ArrayList<Train> filteredTrains =  trains.searchTrain(source,destination,date);
					if(filteredTrains.size()>0) {
						for(Train train: filteredTrains) {
							System.out.println(train.getTrainInfo() + "Available seats : " +trains.getAvailableSeatCount(train, date));
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
							train = trains.getTrainFromNumber(trainNumber);
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
							int seatCount = trains.getAvailableSeatCount(train, bookingDate);
							if(seatCount<0) {
								System.out.println("\n# Ticket booking not opened for that date #\n");
								continue menu;
							}
							break;
							
						}
					
					while(true) {
						boolean isInt = false;
						while(!isInt) {
								System.out.println("\nEnter number of tickets : ");

								numberOfTicket = sc.nextInt();
								isInt = true;
								
								if(numberOfTicket<1) {
									System.out.println("\n# Enter valid number of tickets #\n");
									continue;
								}
								int seatCount = trains.getAvailableSeatCount(train, bookingDate);
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
												sc.nextLine();
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
											
											
											Passenger passenger = passengers.createPassenger(name, age, gender, berthPreference);
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
												passengers.insertPassenger(passenger);
												passengerTickets.add(passengerTicket);
											}
									}
									System.out.println("\n----------Here's your ticket\n");
									for(Ticket ticket : passengerTickets)
										ticket.generateTicket();
								}
						}
						
						break;

					}
					break;
					
				case "4":
					System.out.println("\nEnter PNR to cancle ticket : ");
					String pnr = sc.next();
					if(Cancellation.cancelTicket(pnr)) {
						System.out.println("\nTicket cancelled successfully\n");
					}
					else {
						System.out.println("\nInvalid PNR! Enter valid PNR.\n");
					}
		
					break;
					
				case "5":
					System.out.println("\nEnter PNR : \n");
					String PNR = sc.next();
					Ticket ticket = tickets.getTicketfromPnr(PNR);
					if(ticket==null) {
						System.out.println("\nTicket not found\n");
					}
					else {
						ticket.generateTicket();
					}
					break;
					
				case "6":
					List<Ticket> userTickets = users.getUserAccessMapping();
					if(userTickets!=null && userTickets.size()>0) {
						for(Ticket t:userTickets)
							t.generateTicket();
					}
//					if(userTickets!=null)
//							System.out.println("not null");
//					if(userTickets.size()>0)
//							System.out.println("greater than 0");
					else {
						System.out.println("\nNo ticktes found.");
					}
					break;
					
				case "7":
					List<Ticket> berthTicket = tickets.getBerthTicket();
					LinkedList<Ticket> racTicket = tickets.getRacTicket();
					LinkedList<Ticket> waitingList = tickets.getWaitingList();
					
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
					
				case "8":
					innerloop = false;
					users.getUsers().put(users.getCurrentUser(), false);
					System.out.println("\nSuccessfully logged out\n");
					return;				
				default:
					System.out.println("\nEnter valid choice\n");
					break;
			
				}
			}
	}
	private static boolean isValidName(String name) {
		if(name.length()>2 && name.matches("[a-zA-z]+"))
			return true;
		return false;
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
