package com.sanm.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Author: Sanm
 * since: v1.0
 * description:
 **/
public class ExceptionUtil {

    public static String getStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        try {
            t.printStackTrace(pw);
            return sw.toString();
        } finally {
            pw.close();
        }
    }

}
