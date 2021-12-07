package org.hexanome.data;

/**
 * File that contains a template for Exceptions that are thrown while parsing a malformed xml file
 *
 * @author Gastronom'if
 */
public class ExceptionXML extends Exception {

    private static final long serialVersionUID = 1L;

    public ExceptionXML(String message) {
        super(message);
    }

}
