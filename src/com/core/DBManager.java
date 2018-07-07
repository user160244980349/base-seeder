package com.core;

import com.core.Utils.Base;
import com.core.Utils.Lo;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.sdb.XOfficeDatabaseDocument;

public class DBManager {

	public static void plug() {

		Lo.officePath = "C:/Program Files/LibreOffice/program/";
		XComponentLoader xComponentLoader = Lo.loadOffice();
		XOfficeDatabaseDocument odbDoc = Base.openBaseDoc("./liangTables.odb", xComponentLoader);
		Base.closeBaseDoc(odbDoc);
		Lo.closeOffice();

	}

}
