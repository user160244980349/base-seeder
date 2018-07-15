package com.core;

import com.core.utilities.Base;
import com.core.utilities.BaseTablePrinter;
import com.core.utilities.Lo;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.sdb.XOfficeDatabaseDocument;
import com.sun.star.sdbc.*;

import java.util.ArrayList;
import java.util.Vector;

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

	public void doSmth() { //ArrayList<Vector<String>> table
        XDataSource dataSource = _odbDoc.getDataSource();
        try {
            XConnection connection = dataSource.getConnection("", "");

            int result1 = addUnique(connection, "*", "developer", "name", "kek9");
            System.out.println("ID: " + result1);

//            Base.exec("INSERT INTO \"program\" (\"name\",\"id_developer\",\"id_profile\",\"id_department\",\"id_direction\") VALUES (value, result1, result2, result3, result4)", connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}

    public int addUnique(XConnection connection, String select, String from, String where, String value) {
        StringBuilder query = new StringBuilder().append("SELECT " ).append(select)
                                                 .append(" FROM ").append(from)
                                                 .append(" WHERE ").append(where)
                                                 .append(" = '").append(value).append("'");
        XResultSet rs = Base.executeQuery(query.toString(), connection);
        if (rs != null) {
            try {
                rs.next();
                if (rs.getRow() == 0) {
                    System.out.println("Нет записи, добавление");
                    insertOne(connection, from, where, value);
                    return addUnique(connection, select, from, where, value);
                } else {
                    XRow xRow = Lo.qi(XRow.class, rs);
                    return xRow.getInt(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    protected void insertOne(XConnection connection, String into, String field, String value) {
        StringBuilder query = new StringBuilder().append("INSERT INTO \"").append(into).append("\"")
                                                 .append(" (\"").append(field).append("\")")
                                                 .append(" VALUES('").append(value).append("')");
        Base.exec(query.toString(), connection);
    }

	public void close() {
		Base.closeBaseDoc(_odbDoc);
	}
}
