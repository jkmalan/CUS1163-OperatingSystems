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
package com.jkmalan.MemoryPaging;

import java.util.Scanner;

/**
 * @author jkmalan (John Malandrakis)
 */
public class MemoryPaging {

    // The base page size, 2^10 or 1024 bytes, or 1 kilobyte
    private static final int BASE_PAGESIZE = (int) Math.pow(2, 10);

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        boolean cont = true;
        while (cont) {

            // The page size to be used for calculation in kilobytes, which is multiplied by the base page size
            print("Input the desired page size in kilobytes (1, 2, 4, 8, 16): ");
            int page_size = BASE_PAGESIZE * Integer.parseInt(input.nextLine());

            // The virtual address to test against the page size
            print("Input the desired virtual address in bytes: ");
            int virt_address = Integer.parseInt(input.nextLine());

            int addr_offset = virt_address % page_size;
            int page_number = (virt_address - addr_offset) / page_size;

            print("The address " + virt_address + " contains:");
            print("page_number = " + page_number);
            print("noffset = " + addr_offset);

            // Decide if the program is started over
            print("Start over (y/n)?");
            String response = input.nextLine().toLowerCase();
            if (response.startsWith("n")) {
                cont = false;
            }
        }
    }

    /*
     * A simple assistance method to prevent repeated use of System.print.out()
     */
    private static void print(Object o) {
        System.out.println(o);
    }

}
