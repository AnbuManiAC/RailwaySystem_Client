package com.railwayres;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import com.railway.db.*;
import com.railway.train.*;
import com.railway.train.booking.*;

public class DriverClass {

	public static void main(String[] args) throws IOException {
		
		PropertyReader reader = PropertyReader.getInstance();
		reader.loadData(DriverClass.class.getProtectionDomain().getCodeSource().getLocation().getPath() +"resources/config.properties");
		UserLogin ul = new UserLogin();
		ul.showUserLogin();

	}
	

}


