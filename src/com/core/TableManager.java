package com.core;

import com.core.utilities.Lo;
import com.sun.star.container.XNameAccess;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.lang.XComponent;
import com.sun.star.table.XTableColumns;
import com.sun.star.table.XTableRows;
import com.sun.star.text.*;
import com.sun.star.uno.UnoRuntime;
import java.util.ArrayList;
import java.util.Vector;

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

	public ArrayList<Vector<String>> GetTable(XComponentLoader xCompLoader, String filename) {
		try {
			XComponent xComp = Lo.openDoc(filename, xCompLoader);
			if (xComp != null) {
				XTextTablesSupplier xTextTablesSupplier = UnoRuntime.queryInterface(XTextTablesSupplier.class, xComp);
				XNameAccess xNameAccess = xTextTablesSupplier.getTextTables();
				String[] tableElements = xNameAccess.getElementNames();
				Object tableObject = xNameAccess.getByName(tableElements[0]);
				XTextTable aTextTable = UnoRuntime.queryInterface(XTextTable.class, tableObject);
				XTextTableCursor xTableCursorRow = aTextTable.createCursorByCellName("A2");
				XTableRows rows = aTextTable.getRows();
				XTextTableCursor xTableCursorCol = aTextTable.createCursorByCellName("A2");
				XTableColumns cols = aTextTable.getColumns();

                ArrayList<Vector<String>> table = new ArrayList<>();
                for (int i = 1; i < rows.getCount(); i++) {
                    Vector<String> rowCells = new Vector<>();
                    for (int j = 1; j <= cols.getCount(); j++) {
                        String sCellName = xTableCursorRow.getRangeName();
                        XTextRange xTextRange = UnoRuntime.queryInterface(XText.class, aTextTable.getCellByName(sCellName));
                        rowCells.add(xTextRange.getString());
                        xTableCursorRow.goRight((short) 1, false);
                    }
                    table.add(rowCells);
                    xTableCursorCol.goDown((short) 1, false);
                    xTableCursorRow.gotoCellByName(xTableCursorRow.getRangeName(), false);
                }

                // output
                for (Vector<String> row: table) {
                    for (String cell: row) {
                        System.out.print(cell + " ");
                    }
                    System.out.println();
                }

				Lo.closeDoc(xComp);
                return table;
			} else {
                System.exit(1);
                return null;
            }
		} catch (java.lang.Exception e) {
			e.printStackTrace(System.err);
			System.exit(1);
			return null;
		}
	}
}
