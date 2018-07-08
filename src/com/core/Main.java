package com.core;

import com.core.Utils.Lo;
import com.sun.star.frame.XComponentLoader;

public class Main {

	public static void main(String args[]) {
        String sUrl = "C:\\Users\\Aleksej\\Desktop\\Storage\\_Practice\\kek3.odt";
        TableManager tableManager = new TableManager();
        tableManager.Run(tableManager.GetLoader(), tableManager.GetPath(sUrl));
		Lo.officePath = args[0];
		XComponentLoader xComponentLoader = Lo.loadOffice();

		DBManager dbManager = new DBManager(args[1]);

		dbManager.open(xComponentLoader);
		dbManager.close();

		Lo.closeOffice();
	}
}
