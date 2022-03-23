package com.railwayres.app;

import java.io.IOException;
import com.railway.db.PropertyReader;

public class MainClass {

	public static void main(String[] args) throws IOException {
		
		final UserLogin userLogin = new UserLogin();
		final Menu menu = new Menu();
		final PropertyReader reader = PropertyReader.getInstance();
		reader.loadData(MainClass.class.getProtectionDomain().getCodeSource().getLocation().getPath() +"resources/config.properties");

		userLogin.showUserLogin(menu);

	}
	

}


