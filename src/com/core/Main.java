package com.core;

import com.core.Utils.Lo;
import com.sun.star.frame.XComponentLoader;

public class Main {

	public static void main(String args[]) {
		Lo.officePath = args[0];
		XComponentLoader xComponentLoader = Lo.loadOffice();

		DBManager dbManager = new DBManager(args[1]);

		dbManager.open(xComponentLoader);
		dbManager.close();

		Lo.closeOffice();
	}
}
