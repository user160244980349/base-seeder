package com.core;

import com.core.utilities.Base;
import com.core.utilities.Lo;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.sdb.XOfficeDatabaseDocument;
import com.sun.star.sdbc.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

public class DBManager {

	private String _filename = null;
	private XComponentLoader _loader = null;
	private XOfficeDatabaseDocument _odbDoc = null;
	private StringBuilder _query = null;

	public DBManager(XComponentLoader loader, String filename) {
		_filename = filename;
		_loader = loader;
        _query = new StringBuilder();
	}

	public void open() {
		_odbDoc = Base.openBaseDoc(_filename, _loader);
	}

	public void doSmth() { //ArrayList<Vector<String>> table
        XDataSource dataSource = _odbDoc.getDataSource();
        try {
            XConnection connection = dataSource.getConnection("", "");

            Vector<String> data = new Vector<>(Arrays.asList(   "example2_program",
                                                                "direction", "name", "example_direction",
                                                                "profile", "name", "example_profile",
                                                                "department", "name", "example_department",
                                                                "developer", "name", "example_developer"));

            Vector<String> attrs = new Vector<>(Arrays.asList(  "name", "id_direction", "id_profile",
                                                                "id_department", "id_developer"));
            addIfNotExists(connection, data, attrs);

        } catch (SQLException e) {
            e.printStackTrace();
        }
	}

	public void addIfNotExists(XConnection connection, Vector<String> data, Vector<String> attrs) {
	    Vector<String> results = new Vector<>();
	    results.add(data.elementAt(0));
	    int temp = 1;
        for (int i = 1; i <= 4; i++) {
            Integer currentId = addUnique(connection, data.elementAt(temp), data.elementAt(temp + 1), data.elementAt(temp + 2));
            results.add(currentId.toString());
            temp += 3;
        }
        if (multiCheck(connection, "program", attrs, results) == -1) {
            insert(connection, "program", attrs, results);
        }
    }

    public int multiCheck(XConnection connection, String from, Vector<String> attrs, Vector<String> values) {
        _query.append("SELECT " ).append("*")
                .append(" FROM ").append(from)
                .append(" WHERE ");
        for (int i = 0; i < attrs.size(); i++) {
            _query.append(attrs.elementAt(i)).append(" = '").append(values.elementAt(i)).append("'").append(" AND ");
        }
        _query.setLength(_query.length() - " AND ".length());
        XResultSet rs = Base.executeQuery(_query.toString(), connection);
        _query.setLength(0);
        if (rs != null) {
            try {
                rs.next();
                if (rs.getRow() == 0) {
                    System.out.println("Нет записи program");
                    return -1;
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

    public int addUnique(XConnection connection, String from, String where, String value) {
        _query.append("SELECT " ).append("*")
             .append(" FROM ").append(from)
             .append(" WHERE ").append(where)
             .append(" = '").append(value).append("'");
        XResultSet rs = Base.executeQuery(_query.toString(), connection);
        _query.setLength(0);
        if (rs != null) {
            try {
                rs.next();
                if (rs.getRow() == 0) {
                    System.out.println("Нет записи, добавление");

                    Vector<String> w = new Vector<>(Collections.singletonList(where));
                    Vector<String> v = new Vector<>(Collections.singletonList(value));
                    insert(connection, from, w, v);
                    return addUnique(connection, from, where, value);
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

    public void insert(XConnection connection, String into, Vector<String> attrs, Vector<String> values) {
        _query.append("INSERT INTO \"").append(into).append("\" (");
        for (String attr : attrs) {
            _query.append("\"").append(attr).append("\",");
        }
        _query.setLength(_query.length() - ",".length());
        _query.append(") VALUES (");
        for (String value : values) {
            if (value.matches("\\d+")) {
                _query.append(value).append(",");
            } else {
                _query.append("'").append(value).append("',");
            }
        }
        _query.setLength(_query.length() - 1);
        _query.append(")");
        Base.exec(_query.toString(), connection);
        _query.setLength(0);
    }

	public void close() {
		Base.closeBaseDoc(_odbDoc);
	}
}
