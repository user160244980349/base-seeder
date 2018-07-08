package com.core;

import com.core.utilities.Base;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.sdb.XOfficeDatabaseDocument;

public class DBManager {

	private String filename = null;
	private XComponentLoader xComponentLoader = null;
	private XOfficeDatabaseDocument odbDoc = null;

	public DBManager(XComponentLoader loader, String filename) {
		this.filename = filename;
		xComponentLoader = loader;
	}

	public void open() {
		odbDoc = Base.openBaseDoc(filename, xComponentLoader);
	}

	public void close() {
		Base.closeBaseDoc(odbDoc);
	}
}
