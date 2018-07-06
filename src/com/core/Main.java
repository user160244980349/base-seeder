package com.core;

import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.XPropertySet;
import com.sun.star.comp.helper.Bootstrap;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XDesktop;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.text.ControlCharacter;
import com.sun.star.text.XText;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextDocument;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.Exception;
import com.sun.star.comp.helper.BootstrapException;
import com.sun.star.uno.XComponentContext;
import com.sun.star.util.XReplaceDescriptor;
import com.sun.star.util.XReplaceable;

public class Main {

	public static void main(String args[]) {
		// You need the desktop to create a document
		// The getDesktop method does the UNO bootstrapping, gets the
		// remote servie manager and the desktop object.
		XDesktop xDesktop = null;
		xDesktop = getDesktop();

		XTextDocument xTextDocument =
				createTextdocument(xDesktop);

		createExampleData(xTextDocument);

		String mBritishWords[] = {"colour", "neighbour", "centre", "behaviour",
				"metre", "through"};
		String mUSWords[] = {"color", "neighbor", "center", "behavior",
				"meter", "thru"};

		XReplaceDescriptor xReplaceDescr = null;
		XReplaceable xReplaceable = null;

		xReplaceable = UnoRuntime.queryInterface(
				XReplaceable.class, xTextDocument);

		// You need a descriptor to set properies for Replace
		xReplaceDescr = xReplaceable.createReplaceDescriptor();

		System.out.println("Change all occurrences of ...");
		for (int iArrayCounter = 0; iArrayCounter < mBritishWords.length;
		     iArrayCounter++) {
			System.out.println(mBritishWords[iArrayCounter] +
					" -> " + mUSWords[iArrayCounter]);
			// Set the properties the replace method need
			xReplaceDescr.setSearchString(mBritishWords[iArrayCounter]);
			xReplaceDescr.setReplaceString(mUSWords[iArrayCounter]);

			// Replace all words
			xReplaceable.replaceAll(xReplaceDescr);
		}

		System.out.println("Done");
		System.exit(0);
	}

	private static void createExampleData(
			XTextDocument xTextDocument) {
		// Create textdocument and insert example text
		XTextCursor xTextCursor = null;

		try {
			xTextCursor = xTextDocument.getText().createTextCursor();
			XText xText = xTextDocument.getText();

			xText.insertString(xTextCursor,
					"He nervously looked all around. Suddenly he saw his ", false);

			xText.insertString(xTextCursor, "neighbour ", true);
			XPropertySet xCPS = UnoRuntime.queryInterface(
					XPropertySet.class, xTextCursor);
			// Set the word blue
			xCPS.setPropertyValue("CharColor", 255);
			// Go to last character
			xTextCursor.gotoEnd(false);
			xCPS.setPropertyValue("CharColor", 0);

			xText.insertString(xTextCursor, "in the alley. Like lightening he darted off to the left and disappeared between the two warehouses almost falling over the trash can lying in the ", false);

			xText.insertString(xTextCursor, "centre ", true);
			xCPS = UnoRuntime.queryInterface(
					XPropertySet.class, xTextCursor);
			// Set the word blue
			xCPS.setPropertyValue("CharColor", 255);
			// Go to last character
			xTextCursor.gotoEnd(false);
			xCPS.setPropertyValue("CharColor", 0);

			xText.insertString(xTextCursor, "of the sidewalk.", false);

			xText.insertControlCharacter(xTextCursor,
					ControlCharacter.PARAGRAPH_BREAK, false);
			xText.insertString(xTextCursor, "He tried to nervously tap his way along in the inky darkness and suddenly stiffened: it was a dead-end, he would have to go back the way he had come.", false);

			xTextCursor.gotoStart(false);
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}

	}

	private static XDesktop getDesktop() {
		XDesktop xDesktop = null;
		XMultiComponentFactory xMCF = null;

		try {
			XComponentContext xContext = null;

			// get the remote office component context
			xContext = Bootstrap.bootstrap();

			// get the remote office service manager
			xMCF = xContext.getServiceManager();
			if (xMCF != null) {
				System.out.println("Connected to a running office ...");

				Object oDesktop = xMCF.createInstanceWithContext(
						"com.sun.star.frame.Desktop", xContext);
				xDesktop = UnoRuntime.queryInterface(
						XDesktop.class, oDesktop);
			} else
				System.out.println("Can't create a desktop. No connection, no remote office servicemanager available!");
		} catch (Exception | BootstrapException e) {
			e.printStackTrace(System.err);
			System.exit(1);
		}


		return xDesktop;
	}

	private static com.sun.star.text.XTextDocument createTextdocument(
			XDesktop xDesktop) {
		XTextDocument aTextDocument = null;

		XComponent xComponent = CreateNewDocument(xDesktop,
				"swriter");
		aTextDocument = UnoRuntime.queryInterface(
				XTextDocument.class, xComponent);

		return aTextDocument;
	}


	private static XComponent CreateNewDocument(
			XDesktop xDesktop,
			String sDocumentType) {
		String sURL = "private:factory/" + sDocumentType;

		XComponent xComponent = null;
		XComponentLoader xComponentLoader = null;
		PropertyValue xEmptyArgs[] =
				new PropertyValue[0];

		try {
			xComponentLoader = UnoRuntime.queryInterface(
					XComponentLoader.class, xDesktop);

			xComponent = xComponentLoader.loadComponentFromURL(
					sURL, "_blank", 0, xEmptyArgs);
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}

		return xComponent;
	}
}
