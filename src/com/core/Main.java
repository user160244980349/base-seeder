package com.core;

import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.Exception;
import com.sun.star.comp.helper.BootstrapException;

public class Main {

    public static void main(String args[]) {
        // You need the desktop to create a document
        // The getDesktop method does the UNO bootstrapping, gets the
        // remote servie manager and the desktop object.
        com.sun.star.frame.XDesktop xDesktop = null;
        xDesktop = getDesktop();

        com.sun.star.text.XTextDocument xTextDocument =
                createTextdocument( xDesktop );

        createExampleData( xTextDocument );

        String mBritishWords[] = {"colour", "neighbour", "centre", "behaviour",
                "metre", "through" };
        String mUSWords[] = { "color", "neighbor", "center", "behavior",
                "meter", "thru" };

        com.sun.star.util.XReplaceDescriptor xReplaceDescr = null;
        com.sun.star.util.XReplaceable xReplaceable = null;

        xReplaceable = UnoRuntime.queryInterface(
                com.sun.star.util.XReplaceable.class, xTextDocument);

        // You need a descriptor to set properies for Replace
        xReplaceDescr = xReplaceable.createReplaceDescriptor();

        System.out.println("Change all occurrences of ...");
        for( int iArrayCounter = 0; iArrayCounter < mBritishWords.length;
             iArrayCounter++ )
        {
            System.out.println(mBritishWords[iArrayCounter] +
                    " -> " + mUSWords[iArrayCounter]);
            // Set the properties the replace method need
            xReplaceDescr.setSearchString(mBritishWords[iArrayCounter] );
            xReplaceDescr.setReplaceString(mUSWords[iArrayCounter] );

            // Replace all words
            xReplaceable.replaceAll( xReplaceDescr );
        }

        System.out.println("Done");
        System.exit(0);
    }

    private static void createExampleData(
            com.sun.star.text.XTextDocument xTextDocument)
    {
        // Create textdocument and insert example text
        com.sun.star.text.XTextCursor xTextCursor = null;

        try {
            xTextCursor = xTextDocument.getText().createTextCursor();
            com.sun.star.text.XText xText = xTextDocument.getText();

            xText.insertString( xTextCursor,
                    "He nervously looked all around. Suddenly he saw his ", false );

            xText.insertString( xTextCursor, "neighbour ", true );
            com.sun.star.beans.XPropertySet xCPS = UnoRuntime.queryInterface(
                    com.sun.star.beans.XPropertySet.class, xTextCursor);
            // Set the word blue
            xCPS.setPropertyValue( "CharColor", 255);
            // Go to last character
            xTextCursor.gotoEnd(false);
            xCPS.setPropertyValue( "CharColor", 0);

            xText.insertString( xTextCursor, "in the alley. Like lightening he darted off to the left and disappeared between the two warehouses almost falling over the trash can lying in the ", false  );

            xText.insertString( xTextCursor, "centre ", true );
            xCPS = UnoRuntime.queryInterface(
                    com.sun.star.beans.XPropertySet.class, xTextCursor);
            // Set the word blue
            xCPS.setPropertyValue( "CharColor", 255);
            // Go to last character
            xTextCursor.gotoEnd(false);
            xCPS.setPropertyValue( "CharColor", 0);

            xText.insertString( xTextCursor, "of the sidewalk.", false );

            xText.insertControlCharacter( xTextCursor,
                    com.sun.star.text.ControlCharacter.PARAGRAPH_BREAK, false );
            xText.insertString( xTextCursor, "He tried to nervously tap his way along in the inky darkness and suddenly stiffened: it was a dead-end, he would have to go back the way he had come.", false );

            xTextCursor.gotoStart(false);
        }
        catch( Exception e) {
            e.printStackTrace(System.err);
        }

    }

    private static com.sun.star.frame.XDesktop getDesktop() {
        com.sun.star.frame.XDesktop xDesktop = null;
        com.sun.star.lang.XMultiComponentFactory xMCF = null;

        try {
            com.sun.star.uno.XComponentContext xContext = null;

            // get the remote office component context
            xContext = com.sun.star.comp.helper.Bootstrap.bootstrap();

            // get the remote office service manager
            xMCF = xContext.getServiceManager();
            if( xMCF != null ) {
                System.out.println("Connected to a running office ...");

                Object oDesktop = xMCF.createInstanceWithContext(
                        "com.sun.star.frame.Desktop", xContext);
                xDesktop = UnoRuntime.queryInterface(
                        com.sun.star.frame.XDesktop.class, oDesktop);
            }
            else
                System.out.println( "Can't create a desktop. No connection, no remote office servicemanager available!" );
        } catch( Exception | BootstrapException e ) {
            e.printStackTrace(System.err);
            System.exit(1);
        }


        return xDesktop;
    }

    private static com.sun.star.text.XTextDocument createTextdocument(
            com.sun.star.frame.XDesktop xDesktop)
    {
        com.sun.star.text.XTextDocument aTextDocument = null;

        com.sun.star.lang.XComponent xComponent = CreateNewDocument(xDesktop,
                "swriter");
        aTextDocument = UnoRuntime.queryInterface(
                com.sun.star.text.XTextDocument.class, xComponent);

        return aTextDocument;
    }


    private static com.sun.star.lang.XComponent CreateNewDocument(
            com.sun.star.frame.XDesktop xDesktop,
            String sDocumentType)
    {
        String sURL = "private:factory/" + sDocumentType;

        com.sun.star.lang.XComponent xComponent = null;
        com.sun.star.frame.XComponentLoader xComponentLoader = null;
        com.sun.star.beans.PropertyValue xEmptyArgs[] =
                new com.sun.star.beans.PropertyValue[0];

        try {
            xComponentLoader = UnoRuntime.queryInterface(
                    com.sun.star.frame.XComponentLoader.class, xDesktop);

            xComponent  = xComponentLoader.loadComponentFromURL(
                    sURL, "_blank", 0, xEmptyArgs);
        }
        catch( Exception e) {
            e.printStackTrace(System.err);
        }

        return xComponent ;
    }
}
