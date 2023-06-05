package org.nvip.plugfest.tooling.translator;

import org.nvip.plugfest.tooling.Debug;

/**
 * File: TranslatorException.java
 *
 * An exception to be used by the translators to enhance control flow and provide detailed output on translation
 * failure. It also logs the error to System.out as well.
 *
 * @author Ian Dunn
 */
public class TranslatorException extends Exception {
    public TranslatorException(String message) {
        super(message);
        Debug.log(Debug.LOG_TYPE.ERROR, message);
    }
}