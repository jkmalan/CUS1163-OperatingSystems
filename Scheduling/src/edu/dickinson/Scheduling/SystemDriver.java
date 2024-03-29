package edu.dickinson.Scheduling;

import java.util.*;
import java.io.*;

/**
 * The SystemDriver class reads the data files that describe the processes
 * executing on the system and simulate the system-calls and interrupts
 * generated by the actions of those processes. The SystemDriver class requires
 * three command line arguments:
 * 
 * <pre>
 *       processFilename
 *       deviceFilename
 *       schedulerType
 * </pre>
 * 
 * <p>
 * The file indicated by the <code>processFilename</code> must contains a list
 * of the data data files for the processes. Each process will contains its own
 * data file. The format for a data file describing a process is as follows:
 * 
 * <pre>
 *      &lt;Process Name&gt;
 *      &lt;start time&gt;
 *      CPU &lt;time&gt;
 *      ...
 *      IO &lt;device ID&gt; &lt;time&gt;
 *      CPU &lt;time&gt;
 *      ...
 *      WAIT &lt;queue ID&gt;
 *      CPU &lt;time&gt;
 *      ...
 *      NOTIFY &lt;queue ID&gt;
 *      CPU &lt;time&gt;
 *      ...
 *      NOTIFY_ALL &lt;queue ID&gt;
 *      CPU &lt;time&gt;
 *      ...
 *      EXIT
 * </pre>
 * 
 * <p>
 * The time for CPU and IO operations are specified in virtual time units. All
 * "device ID"s and "queue ID"s must be listed in the file indicated by
 * <code>devicesFilename</code> command line argument as described below.
 * 
 * <p>
 * The WAIT, NOTIFY and NOTIFY_ALL commands apply to queues. The WAIT operation
 * blocks the invoking process on the queue. The NOTIFY operation wakes a single
 * process that is waiting on the queue. The NOTIFY_ALL operation wakes all
 * processes that are waiting on the queue. The times specified for a CPU and IO
 * operations are specified in ms.
 * 
 * <p>
 * The file indicated by the <code>devicesFilename</code> command line
 * argument must contain a list of the devices in the system. There are only 2
 * types of devices supported: I/O Devices and Queues. I/O devices coorespond to
 * any device that generates an interrupt when an operation is complete. Thus, a
 * process must actively wait on a I/O Device but the device will generate and
 * interrupt when the process is to be woken up. A Queue is also a device on
 * which a process may wait. However, another process must take an action to
 * wake up a process waiting on a Queue.
 * 
 * <p>
 * The format of the <code>devicesFilename</code> file is as follows:
 * 
 * <pre>
 * 
 *  
 *      &lt;device type&gt; &lt;device ID&gt;
 *   
 *  
 * </pre>
 * 
 * <p>
 * A "Device ID" may be any String. The "device type" must be either "I/O" or
 * "Queue".
 * 
 * <p>
 * In all of these files, lines begining with # as the first character are
 * treated as comments and blank lines are ignored.
 */
public class SystemDriver {

    public static void main(String[] args) {
                    
        if (args.length == 3) {

            String processesFilename = args[0];
            String devicesFilename = args[1];
            String schedulerType = args[2];

            Vector ioDevices = new Vector(20, 20);
            Vector queueDevices = new Vector(20, 20);
            Vector processList = new Vector(20, 20);
            Vector processes = new Vector(20, 20);

            EventQueue events = new EventQueue();

            SystemTimer timer = new SystemTimer(processes, events);
                
            // Dynamically load the correct Kernel class based on the name
            // of the class specified in the schedulerType parameter.
            ClassLoader cl = ClassLoader.getSystemClassLoader();
            Class c = null;
            
            try {
                c = cl.loadClass(schedulerType);
            }
            catch (ClassNotFoundException e) {
                System.out.println("The specified kernel " + schedulerType + 
                        " could not be found.");
                System.out.println(e);
                System.exit(-1);
            }

            Kernel osKernel = null;
            try {
                osKernel = (Kernel)c.newInstance();
            }
            catch (Exception e) {
                System.out.println("Unable to instantiate kernel class: " +
                        schedulerType + ".");
                System.out.println(e);
                System.exit(-1);
            }
            
            timer.setKernel(osKernel);

            readDeviceList(devicesFilename, ioDevices, queueDevices);
            readProcessList(processesFilename, processList);
            readProcessData(processList, processes, ioDevices, queueDevices,
                    osKernel);
            createDevicesAndQueues(ioDevices, queueDevices, osKernel, timer);

            scheduleProcessArrivals(processes, events);

            timer.processEvents();
        }
        else {
            System.out
                    .println("Usage: java SystemDriver <processes> <devices> <sched>\n"
                            + "\t <processes> - Processes data file.\n"
                            + "\t <devices> - Devices data file.\n"
                            + "\t <sched> - Scheduler type.");
        }
    }

    /**
     * Schedule an event to occurr at the arrival time for each of the
     * processes.
     * 
     * @param processes the Vector of all of the Processes.
     * @param systemTimer the Timer that controlls the sequence of events in the
     *            simulation.
     */
    public static void scheduleProcessArrivals(Vector processes,
            EventQueue events) {
        ListIterator procIt = processes.listIterator();
        while (procIt.hasNext()) {
            Process p = (Process) procIt.next();
            Event e = new Event(p.getArrivalTime(), p.getOp());
            p.advanceOpCounter();
            events.add(e);
        }

        //events.printEventQueue();
    }

    /**
     * Make the system calls to the kernel to request that the I/O devices and
     * the queues be created.
     * 
     * @param ioDevices a Vector of all of the device names.
     * @param queueDevices a Vector of all of the queue names.
     * @param osKernel a reference to the Kernel object.
     */
    public static void createDevicesAndQueues(Vector ioDevices,
            Vector queueDevices, Kernel osKernel, SystemTimer timer) {

        // Make a system call to the Kernel requesting that each of the
        // I/O devices be created.
        ListIterator devIt = ioDevices.listIterator();
        while (devIt.hasNext()) {
            osKernel.systemCall(Kernel.MAKE_DEVICE, (String) devIt.next(), timer);
        }

        // Make a system call to the Kernel requesting that each of the
        // Queues be created.
//        ListIterator queueIt = queueDevices.listIterator();
//        while (queueIt.hasNext()) {
//            osKernel.systemCall(Kernel.MAKE_QUEUE, (String) queueIt.next(), timer);
//        }
    }

    /**
     * Read the list of devices from the specified file.
     * 
     * @param devicesFilename the file from which the list of devices is to be
     *            read.
     * @param ioDevices a Vector of all of the device names.
     * @param queueDevices a Vector of all of the queue names.
     */
    public static void readDeviceList(String devicesFilename, Vector ioDevices,
            Vector queueDevices) {
        try {
            FileInputStream fStream = new FileInputStream(devicesFilename);
            InputStreamReader inReader = new InputStreamReader(fStream);
            BufferedReader bReader = new BufferedReader(inReader);

            while (bReader.ready()) {
                String line = bReader.readLine();
                if (!line.trim().equals("") && !line.startsWith("#")) {
                    line = line.toUpperCase();
                    if (line.startsWith("I/O")) {
                        ioDevices.add(line.substring(3).trim());
                    }
                    else if (line.startsWith("QUEUE")) {
                        queueDevices.add(line.substring(5).trim());
                    }
                    else {
                        throw new UnsupportedOperationException(
                                "Undefined device type: " + line);
                    }
                }
            }
            fStream.close();
            inReader.close();
            bReader.close();
        }
        catch (IOException e) {
            System.err.println("Error reading devices.dat file.");
            System.err.println(e);
            System.exit(-1);
        }
    }

    /**
     * Read the list of process filenames from the specified file.
     * 
     * @param processesFilename the file from which the list of process
     *            filenames is to be read.
     * @param processList a Vector to be loaded with the list of process
     *            filenames is to be stored.
     */
    public static void readProcessList(String processesFilename,
            Vector processList) {
        try {
            FileInputStream fStream = new FileInputStream(processesFilename);
            InputStreamReader inReader = new InputStreamReader(fStream);
            BufferedReader bReader = new BufferedReader(inReader);

            while (bReader.ready()) {
                String line = bReader.readLine();
                if (!line.trim().equals("") && !line.startsWith("#")) {
                    processList.add(line);
                }
            }

            fStream.close();
            inReader.close();
            bReader.close();
        }
        catch (IOException e) {
            System.err.println("Error reading processes.dat file.");
            System.err.println(e);
            System.exit(-1);
        }
    }

    /**
     * Read the process profile information for each of the processes listed in
     * the "processes.dat" file.
     * 
     * @param processList a Vector of the filenames for each of the processes.
     * @param processes a Vector to hold the Process objects for each of the
     *            processes.
     * @param ioDevices a Vector of all of the device names.
     * @param queueDevices a Vector of all of the queue names.
     * @param osKernel the kernel that will be controlling the execution of the
     *            processes.
     */
    public static void readProcessData(Vector processList, Vector processes,
            Vector ioDevices, Vector queueDevices, Kernel osKernel) {

        ListIterator procIt = processList.listIterator();
        while (procIt.hasNext()) {
            processes.add(readProcess((String) procIt.next(), ioDevices,
                    queueDevices, osKernel));
        }
    }

    /**
     * Read the information about a particular process.
     * 
     * @param procFilename the file containing the profiling information about
     *            the process.
     * @param ioDeviceList the Vector of valid I/O devices.
     * @param queueDeviceList the Vector of valid queue devices.
     * @return a Process object containg the profiling information read from the
     *         data file.
     * @param osKernel the kernel that will be controlling the execution of the
     *            processes.
     */
    public static Process readProcess(String procFilename, Vector ioDeviceList,
            Vector queueDeviceList, Kernel osKernel) {
        try {
            FileInputStream fStream = new FileInputStream(procFilename);
            InputStreamReader inReader = new InputStreamReader(fStream);
            BufferedReader bReader = new BufferedReader(inReader);

            // Read the first line of data in the file as the process name.
            String procName;
            do {
                procName = bReader.readLine().trim();
            } while (procName.equals("") || procName.startsWith("#"));

            // Read the second line of data in the file as the arrival time.
            String arrivalTime;
            do {
                arrivalTime = bReader.readLine().trim();
            } while (arrivalTime.equals("") || arrivalTime.startsWith("#"));

            Process p = new Process(procName, Integer.parseInt(arrivalTime),
                    osKernel);

            // Read all the rest of the lines of data in the file as operations.
            while (bReader.ready()) {

                String line = bReader.readLine();

                if (!line.trim().equals("") && !line.startsWith("#")) {

                    line = line.toUpperCase();

                    ProcessOperation pOp = new ProcessOperation(line, p);

                    // Make sure that if the operation uses a device it is
                    // one that was listed in the devices.dat file.
                    if (pOp.getOpDevice() == null
                            || ioDeviceList.contains(pOp.getOpDevice())
                            || queueDeviceList.contains(pOp.getOpDevice())) {

                        p.addOp(pOp);
                    }
                    else {
                        throw new UnsupportedOperationException(
                                "Unrecognized device in file: " + procFilename);
                    }
                }
            }

            fStream.close();
            inReader.close();
            bReader.close();

            return p;
        }
        catch (IOException e) {
            System.err.println("Error reading " + procFilename + "file.");
            System.err.println(e);
            System.exit(-1);
            return null; // stupid compiler!
        }
    }
}

