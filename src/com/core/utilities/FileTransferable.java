package com.core.utilities;
// FileTransferable.java
// Andrew Davison, ad@fivedots.coe.psu.ac.th, July 2016

/* An Office transferable for reading/writing the contents
   of a file to the clipboard as a byte array.

   The mime type for the array is obtained by calling
   Info.getMIMEType() using the file's name.
*/


import com.sun.star.datatransfer.DataFlavor;
import com.sun.star.datatransfer.UnsupportedFlavorException;
import com.sun.star.datatransfer.XTransferable;
import com.sun.star.uno.Type;

import java.nio.file.Files;
import java.nio.file.Paths;


public class FileTransferable implements XTransferable {
	private String mimeType = "application/octet-stream";   // good default
	private byte[] fileData = null;


	public FileTransferable(String fnm) {
		mimeType = Info.getMIMEType(fnm);
		// System.out.println("MIME Type: " + mimeType);
		try {
			fileData = Files.readAllBytes(Paths.get(fnm));
			// System.out.println("Length of byte array for " + fnm + ": " + fileData.length);
		} catch (java.lang.Exception e) {
			System.out.println("Could not read bytes from " + fnm);
		}
	}  // end of FileTransferable()


	public Object getTransferData(DataFlavor df) throws UnsupportedFlavorException {
		if (!df.MimeType.equalsIgnoreCase(mimeType))
			throw new UnsupportedFlavorException();
		return fileData;
	}  // end of getTransferData()


	public DataFlavor[] getTransferDataFlavors() {
		DataFlavor[] flavors = new DataFlavor[1];
		flavors[0] = new DataFlavor(mimeType, mimeType, new Type(byte[].class));
		return flavors;
	}


	public boolean isDataFlavorSupported(DataFlavor df) {
		return df.MimeType.equalsIgnoreCase(mimeType);
	}

}  // end of FileTransferable class

