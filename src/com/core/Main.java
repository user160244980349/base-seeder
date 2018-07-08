package com.core;

import com.core.utilities.Lo;
import com.sun.star.frame.XComponentLoader;

public class Main {

	public static void main(String args[]) {

		Lo.officePath = args[0];
		String odtDoc = args[1];
		String odbDoc = args[2];

		XComponentLoader xComponentLoader = Lo.loadOffice();

		TableManager tableManager = new TableManager();
		DBManager dbManager = new DBManager(xComponentLoader, odbDoc);

		tableManager.Run(xComponentLoader, odtDoc);

		dbManager.open();
		dbManager.doSmth();
		dbManager.querySmth();
		dbManager.close();

		Lo.closeOffice();
	}
}
