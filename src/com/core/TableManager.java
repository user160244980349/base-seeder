package com.core;

import com.core.utilities.Lo;
import com.sun.star.container.XNameAccess;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.lang.XComponent;
import com.sun.star.table.XTableColumns;
import com.sun.star.table.XTableRows;
import com.sun.star.text.*;
import com.sun.star.uno.UnoRuntime;

public class TableManager {

	public String GetURL(String sUrl) {
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
				XTextTablesSupplier xTextTablesSupplier = UnoRuntime.queryInterface(XTextTablesSupplier.class, xComp);
				XNameAccess xNameAccess = xTextTablesSupplier.getTextTables();
				String[] tableElements = xNameAccess.getElementNames();
				Object table = xNameAccess.getByName(tableElements[0]);
				XTextTable aTextTable = UnoRuntime.queryInterface(XTextTable.class, table);
				XTextTableCursor xTableCursor = aTextTable.createCursorByCellName("A2");
				XTableRows rows = aTextTable.getRows();
				XTableColumns cols = aTextTable.getColumns();

				for (int j = 1; j <= cols.getCount(); j++) {
					for (int i = 1; i < rows.getCount(); i++) {
						String sCellName = xTableCursor.getRangeName();
						XTextRange xTextRange = UnoRuntime.queryInterface(XText.class, aTextTable.getCellByName(sCellName));
						System.out.print(xTextRange.getString() + " ");

                        xTableCursor.goDown((short) 1, false);
					}
                    System.out.println();
                    xTableCursor.gotoCellByName("A2", false);
                    for (int k = 0; k < j; k++) {
                        xTableCursor.goRight((short) 1, false);
                    }
				}
				Lo.closeDoc(xComp);
			} else
				System.exit(1);
		} catch (java.lang.Exception e) {
			e.printStackTrace(System.err);
			System.exit(1);
		}
	}
}
