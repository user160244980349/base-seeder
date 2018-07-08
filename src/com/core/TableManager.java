package com.core;

import com.core.utils.Lo;
import com.sun.star.container.XNameAccess;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.lang.XComponent;
import com.sun.star.table.XTableColumns;
import com.sun.star.table.XTableRows;
import com.sun.star.text.*;
import com.sun.star.uno.UnoRuntime;

public class TableManager {

	public String GetURL(String sUrl) {
		com.sun.star.uno.XComponentContext xContext = null;
		try {
			if (sUrl.indexOf("private:") != 0) {
				java.io.File sourceFile = new java.io.File(sUrl);
				StringBuffer sbTmp = new StringBuffer("file:///");
				sbTmp.append(sourceFile.getCanonicalPath().replace('\\', '/'));
				sUrl = sbTmp.toString();
			}
			return sUrl;
		} catch (java.lang.Exception e) {
			e.printStackTrace(System.err);
			System.exit(1);
			return null;
		}
	}

	public void Run(XComponentLoader xCompLoader, String filename) {
		try {
			XComponent xComp = Lo.openDoc(filename, xCompLoader);
			if (xComp != null) {
				XTextDocument aTextDocument = null;
				aTextDocument = UnoRuntime.queryInterface(XTextDocument.class, xComp);
				XTextTablesSupplier xTextTablesSupplier = UnoRuntime.queryInterface(XTextTablesSupplier.class, xComp);
				XNameAccess xNameAccess = xTextTablesSupplier.getTextTables();
				String[] tableElements = xNameAccess.getElementNames();
				Object table = xNameAccess.getByName(tableElements[0]);
				XTextTable aTextTable = UnoRuntime.queryInterface(XTextTable.class, table);
				XTextTableCursor xTableCursor = aTextTable.createCursorByCellName("A1");

				XTableRows rows = aTextTable.getRows();
				XTableColumns cols = aTextTable.getColumns();
				System.out.println(rows.getCount() + " " + cols.getCount());

				for (int i = 1; i < rows.getCount(); i++) {
					xTableCursor.goDown((short) 1, false);
					String sCellName = xTableCursor.getRangeName();

					XTextRange xTextRange = UnoRuntime.queryInterface(XText.class, aTextTable.getCellByName(sCellName));
					xTextRange.getString();

					System.out.println(xTextRange.getString());
				}

				System.out.println(tableElements[0]);

				System.exit(0);
			} else
				System.exit(1);
		} catch (java.lang.Exception e) {
			e.printStackTrace(System.err);
			System.exit(1);
		}
	}
}
