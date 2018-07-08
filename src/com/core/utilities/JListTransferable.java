package com.core.utilities;
// JListTransferable.java
// Andrew Davison, ad@fivedots.coe.psu.ac.th, July 2016

/* Use Java's clipboard API to store an ArrayList of Objects
   on the clipboard.
*/

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;


public class JListTransferable implements Transferable {

	private ArrayList vals;    // no use made of generics
	private DataFlavor listDF;

	public JListTransferable(ArrayList vals) {
		this.vals = vals;
		listDF = new DataFlavor(ArrayList.class, "List");
	}


	public Object getTransferData(DataFlavor df)
			throws UnsupportedFlavorException, IOException {
		if (df.equals(listDF) && vals != null)
			return vals;
		else
			throw new UnsupportedFlavorException(df);
	}


	public DataFlavor[] getTransferDataFlavors() {
		DataFlavor[] dfs = new DataFlavor[1];
		dfs[0] = listDF;
		return dfs;
	}


	public boolean isDataFlavorSupported(DataFlavor df) {
		DataFlavor[] dfs = getTransferDataFlavors();
		for (int i = 0; i < dfs.length; i++) {
			if (df.equals(dfs[i]))
				return true;
		}
		return false;
	}

}  // end of JListTransferable class
