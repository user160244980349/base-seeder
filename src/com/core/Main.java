package com.core;

import com.core.utilities.Lo;
import com.sun.star.frame.XComponentLoader;

public class Main {

	public static void main(String args[]) {

		String odtDoc = args[0];
		String odbDoc = args[1];

		XComponentLoader xComponentLoader = Lo.loadSocketOffice();

		TableManager tableManager = new TableManager();
		DBManager dbManager = new DBManager(xComponentLoader, odbDoc);

		//tableManager.GetTable(xComponentLoader, odtDoc);

		dbManager.open();
		dbManager.runOneToMany();
		dbManager.close();

		Lo.closeOffice();
	}
}
