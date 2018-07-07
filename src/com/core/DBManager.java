package com.core;

import com.core.Utils.Base;
import com.core.Utils.Lo;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.sdb.XOfficeDatabaseDocument;

public class DBManager {

	private String fileName = null;
	XOfficeDatabaseDocument odbDoc = null;

	public DBManager(String fileName) {
		this.fileName = fileName;
	}

	public void open(XComponentLoader loader) {
		odbDoc = Base.openBaseDoc(fileName, loader);
	}

	public void close() {
		Base.closeBaseDoc(odbDoc);
	}
}
