package com.core;

public class Main {

	public static void main(String args[]) {
        String sUrl = "C:\\Users\\Aleksej\\Desktop\\Storage\\_Practice\\kek3.odt";
        TableManager tableManager = new TableManager();
        tableManager.Run(tableManager.GetLoader(), tableManager.GetPath(sUrl));
		System.exit(0);
	}
}
