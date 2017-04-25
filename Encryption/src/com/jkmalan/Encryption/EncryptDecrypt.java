/*
* This file is part of Adopt-A-Friend, licensed under the MIT License (MIT).
*
* Copyright (c) Adopt-A-Friend
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
* THE SOFTWARE.
*/
package com.jkmalan.Encryption;

import java.io.*;
import java.util.Scanner;

/**
 * @author jkmalan (aka John Malandrakis)
 */
public class EncryptDecrypt {

    private static String messageSource = "D:\\Development\\IntelliJ IDEA\\CUS1163\\Encryption\\source.txt";
    private static String messageDestination = "D:\\Development\\IntelliJ IDEA\\CUS1163\\Encryption\\destination.txt";

    public static void main(String[] args) {

        Scanner input = null;
        PrintWriter writer = null;
        try {
            input = new Scanner(new FileReader(new File(messageSource)));

            writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File(messageDestination)), "UTF-8"));

            String line = "";
            while (input.hasNextLine() && (line = input.nextLine()) != null) {
                if (line.startsWith("E")) {
                    int columns = Character.getNumericValue(line.charAt(1));
                    int endpoint = line.indexOf("*");
                    String message = line.substring(2, endpoint);
                    String encryptedMessage = encryptString(message, columns);
                    writer.write("D" + columns + encryptedMessage + "*" + "\n");
                } else if (line.startsWith("D")) {
                    int columns = Character.getNumericValue(line.charAt(1));
                    int endpoint = line.indexOf("*");
                    String message = line.substring(2, endpoint);
                    String decryptedMessage = decryptString(message, columns);
                    writer.write("E" + columns + decryptedMessage + "*" + "\n");
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                input.close();
            }

            if (writer != null) {
                writer.close();
            }
        }

    }

    public static String encryptString(String message, int columns) {
        int rows = (int) Math.ceil((double) message.length() / (double) columns);
        char[][] array = new char[columns][rows];
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (index < message.length()) {
                    char c = message.charAt(index);
                    if (c == ' ') {
                        array[j][i] = '/';
                    } else {
                        array[j][i] = c;
                    }
                    index++;
                }
            }
        }

        String encryptedMessage = "";
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                encryptedMessage += array[i][j];
            }
        }

        return encryptedMessage;
    }

    public static String decryptString(String message, int columns) {
        int rows = (int) Math.ceil((double) message.length() / (double) columns);
        char[][] array = new char[columns][rows];
        int index = 0;
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                if (index < message.length()) {
                    char c = message.charAt(index);
                    if (c == '/') {
                        array[i][j] = ' ';
                    } else {
                        array[i][j] = c;
                    }
                    index++;
                }
            }
        }

        String decryptedMessage = "";
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                decryptedMessage += array[j][i];
            }
        }

        return decryptedMessage;
    }

}
