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
package com.jkmalan.Scheduling;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author jkmalan (aka John Malandrakis)
 */
public class Scheduler {

    private List<Process> processList;

    public Scheduler(List<Process> processList) {
        this.processList = processList;
    }

    public void firstComeFirstServe() {
        List<Process> processes = new ArrayList<>();
        for (Process p : processList) {
            processes.add(p);
        }

        processes.sort(new Comparator<Process>() {
            @Override
            public int compare(Process one, Process two) {
                return one.getArrivalTime() > two.getArrivalTime() ? -1 : (one.getArrivalTime() < two.getArrivalTime()) ? 1 : 0;
            }
        });


    }

    public void shortestJobFirst() {
        List<Process> processes = new ArrayList<>();
        for (Process p : processList) {
            processes.add(p);
        }

        processes.sort(new Comparator<Process>() {
            @Override
            public int compare(Process one, Process two) {
                return one.getBurstTime() > two.getBurstTime() ? -1 : (one.getBurstTime() < two.getBurstTime()) ? 1 : 0;
            }
        });


    }

    public void priorityScheduling() {
        List<Process> processes = new ArrayList<>();
        for (Process p : processList) {
            processes.add(p);
        }

        processes.sort(new Comparator<Process>() {
            @Override
            public int compare(Process one, Process two) {
                return one.getPriority() > two.getPriority() ? -1 : (one.getPriority() < two.getPriority()) ? 1 : 0;
            }
        });


    }

}
