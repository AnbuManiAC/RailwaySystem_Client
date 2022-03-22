package com.railwayres;

import java.io.IOException;
import com.railway.db.PropertyReader;


public class DriverClass {

	public static void main(String[] args) throws IOException {
		
		UserLogin userLogin = new UserLogin();
		Menu menu = new Menu();
		PropertyReader reader = PropertyReader.getInstance();
		reader.loadData(DriverClass.class.getProtectionDomain().getCodeSource().getLocation().getPath() +"resources/config.properties");
		
		menu.showTitle();
		userLogin.showUserLogin(menu);
		
		
	}
	

}


