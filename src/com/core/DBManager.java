package com.core;

import com.core.utilities.Base;
import com.core.utilities.BaseTablePrinter;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.sdb.XOfficeDatabaseDocument;
import com.sun.star.sdbc.SQLException;
import com.sun.star.sdbc.XConnection;
import com.sun.star.sdbc.XDataSource;
import com.sun.star.sdbc.XResultSet;

public class DBManager {

	private String _filename = null;
	private XComponentLoader _loader = null;
	private XOfficeDatabaseDocument _odbDoc = null;

	public DBManager(XComponentLoader loader, String filename) {
		_filename = filename;
		_loader = loader;
	}

	public void open() {
		_odbDoc = Base.openBaseDoc(_filename, _loader);
	}

	public void doSmth() {
		XDataSource dataSource = _odbDoc.getDataSource();

		try {
			XConnection connection = dataSource.getConnection("", "");

			Base.exec("CREATE TABLE SPIES " +
					"( FIRSTNAME VARCHAR(50), LASTNAME VARCHAR(50), ID VARCHAR(50), " +
					"PRIMARY KEY (ID) )", connection);

			Base.exec("INSERT INTO SPIES VALUES( 'James', 'Bond', '007')", connection);
			Base.exec("INSERT INTO SPIES VALUES( 'James', 'Bond', '007')", connection);
			Base.exec("INSERT INTO SPIES VALUES( 'Johnny', 'English', '013')", connection);
			Base.exec("INSERT INTO SPIES VALUES( 'Maxwell', 'Smart', 'Agent 86')", connection);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void querySmth() {
		XDataSource dataSource = _odbDoc.getDataSource();

		try {
			XConnection connection = dataSource.getConnection("", "");

			System.out.println();
			XResultSet rs = Base.executeQuery("SELECT * FROM room", connection);
			BaseTablePrinter.printResultSet(rs);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		Base.closeBaseDoc(_odbDoc);
	}
}
