/*

  Copyright (C) 2003-2009 Smardec. All rights reserved.

  http://www.smardec.com

*/

package com.smardec.license4j.demo;

import com.smardec.license4j.LicenseUtil;


/**
 * Creates key pair in the specified file.
 */
public class CreateKeyPair {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("You must specify output file name.");
            System.exit(0);
        }
        try {
            LicenseUtil.createKeyPair(args[0]);
            System.out.println("Keys are written to file \"" + args[0] + "\"");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
