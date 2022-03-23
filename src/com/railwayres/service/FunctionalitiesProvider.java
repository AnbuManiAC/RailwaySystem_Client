package com.railwayres.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.railway.db.PassengerTable;
import com.railway.db.StationTable;
import com.railway.db.TicketTable;
import com.railway.db.TrainTable;
import com.railway.db.UserTable;
import com.railway.model.Passenger;
import com.railway.model.Ticket;
import com.railway.model.Train;
import com.railway.service.Booking;
import com.railway.service.Cancellation;
import com.railway.util.Validator;

public class FunctionalitiesProvider {
	
	private TrainTable trains;
	private StationTable stations;
	private PassengerTable passengers;
	private TicketTable tickets;
	private UserTable users;
	private Cancellation cancellation;
	private final Scanner sc;
	
	public FunctionalitiesProvider(){
		trains = TrainTable.getInstance();
		stations = StationTable.getInstance();
		passengers = PassengerTable.getInstance();
		tickets = TicketTable.getInstance();
		users = UserTable.getInstance();
		sc = new Scanner(System.in);
		
	}
	
	public void showTrains() {
		hasStar();
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
		hasStar();
	}
	public void findTrain() {
		String source, destination;
		LocalDate date;
		
		while(true) {
			System.out.println("\nEnter source : ");
			source = sc.next();
			if(!stations.isStationAvailable(source)) {
				System.out.println("\nEnter valid station\n");
				continue;
			}
			break;
		}
		
		while(true) {
			System.out.println("\nEnter destination : ");
			destination = sc.next();
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
			String _date = sc.next();
			if(!Validator.isValidDate(_date)) {
				System.out.println("\n# Enter valid date / Enter date in specified format #");
				continue;
			}
			if(!Validator.isLogicalDate(_date)) {
				System.out.println("\n# Date must not be in the past #");
				continue;
			}
			date = LocalDate.parse(_date);
			break ;
			
		}
		hasStar();		
		ArrayList<Train> filteredTrains =  trains.searchTrain(source,destination,date);
		if(filteredTrains.size()>0) {
			for(Train train: filteredTrains) {
				System.out.println(train.getTrainInfo() + "Available seats : " +trains.getAvailableSeatCount(train, date));
			}
		}
		else {
			System.out.println("\nNo Trains availaible.");
		}
		hasStar();
	}

	
	public void showBookedTickets() {
		hasStar();
		List<Ticket> userTickets = users.getUserAccessMapping();
		if(userTickets!=null && userTickets.size()>0) {
			for(Ticket t:userTickets)
				t.generateTicket();
		}
		else {
			System.out.println("\nNo ticktes found.");
		}
		hasStar();

	}

	public void showTicketStatus() {

		System.out.println("\nEnter PNR : \n");
		String PNR = sc.next().trim();
		Ticket ticket = tickets.getTicketfromPnr(PNR);
		hasStar();
		if(ticket==null) {
			System.out.println("\nTicket not found\n");
		}
		else {
			ticket.generateTicket();
		}
		hasStar();
	}

	public void cancelBookedTicket() {
		
		cancellation = new Cancellation();
		System.out.println("\nEnter PNR to cancle ticket : ");
		String pnr = sc.next();
		hasStar();
		if(cancellation.cancelTicket(pnr)) {
			System.out.println("\nTicket cancelled successfully\n");
		}
		else {
			System.out.println("\nInvalid PNR! Enter valid PNR.\n");
		}
		hasStar();

	}

	public void ticketBooker() {

		Booking booking = new Booking();
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
				if(!Validator.isValidDate(bookingdate)) {
					System.out.println("\n# Enter valid date #");
					continue;
				}
				if(!Validator.isLogicalDate(bookingdate)) {
					System.out.println("\n# Date must not be in the past #");
					continue;
				}
				bookingDate = LocalDate.parse(bookingdate);
				int seatCount = trains.getAvailableSeatCount(train, bookingDate);
				if(seatCount<0) {
					System.out.println("\n# Ticket booking not opened for that date #\n");
					return;
				}
				break;
				
			}
		
		while(true) {
				System.out.println("\nEnter number of tickets : ");

				numberOfTicket = Validator.validateInt(sc.next().trim());
				if(numberOfTicket==-1)
					continue;
				
				
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
						return;
					else {
						System.out.println("\n# Invalid input #\n");
						return;
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
								if(!Validator.isValidName(name)) {
									System.out.println("\n# Enter valid name #");
									continue;
								}
								break;
							}
							
							while(true) {
								System.out.println("\nEnter age : ");
								age = Validator.validateInt(sc.next().trim());
								if(age==-1)
									continue;
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
							Ticket passengerTicket = booking.bookTicket(passenger,train,bookingDate);
							if(passengerTicket!=null)
							{
								if(passengerTicket.getBerthAlloted().equals(passenger.getBerthPreference()))
									System.out.println("\nPreferred berth available\nSuccessfully booked\n");
								else if(passengerTicket.getBerthAlloted().equals("WL")){
									System.out.println("\nPreferred berth unavailable! You are in waiting list.\n");
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
				break;
			}
		return;
	}
	
	
	public void hasStar() {
		System.out.println("\n");
		String star = "";
		for(int i=0;i<170;i++)
				star+="*";
		System.out.print(star);
		System.out.println("\n");		
	}

	public void printAllTickets() {
		hasStar();
		List<Ticket> berthTicket = tickets.getBerthTicket();
		List<Ticket> racTicket = tickets.getRacTicket();
		List<Ticket> waitingList = tickets.getWaitingList();
		
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
		hasStar();
		return;
	}
}
