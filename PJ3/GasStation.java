// DO NOT ADD NEW METHODS OR NEW DATA FIELDS!
// DO NOT MODIFY METHODS OR NEW DATA FIELDS!

package PJ3;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

//--------------------------------------------------------------------------
//
// Define simulation queues in a gas station. Queues hold references to Car &
// GasPump objects
//
// Car (FIFO) queue is used to hold waiting cars. If the queue is too long
// (i.e. >  carQSizeLimit), car goes away without entering car queue
//
// There are several gas pumps in a gas station. Use PriorityQueue to 
// hold BUSY gas pumps and FIFO queue to hold FREE gas pumps, 
// i.e. a pump that is FREE for the longest time should start be used first.
//
// To handle gasPump in PriorityQueue, we need to define comparator 
// for comparing 2 gasPump objects. Here is a constructor from Java API:
//
// 	PriorityQueue(int initialCapacity, Comparator<? super E> comparator) 
//
// For priority queue, the default compare function is "natural ordering"
// i.e. for numbers, minimum value is returned first
//
// User can define own comparator class for PriorityQueue.
// For GasPump objects, we like to have smallest end busy interval time first.
//
// The following class define compare() for two busy gas pumps :

class BusyGasPumpComparator implements Comparator<GasPump> {
    // overide compare() method
    public int compare(GasPump o1, GasPump o2) {
        return o1.getEndIntervalTime() - o2.getEndIntervalTime();
    }
}

class GasStation {


    // Private data fields:

    // define one priority queue
    private PriorityQueue<GasPump> busyGasPumpQ;

    // define two FIFO queues
    private Queue<Car> carQ;
    private Queue<GasPump> freeGasPumpQ;

    // define car queue size limit
    private int carQSizeLimit;


    // Constructor
    public GasStation() {
        // add statements
        this.carQSizeLimit = 50;
        this.carQ = new ArrayDeque<>();
        this.freeGasPumpQ = new ArrayDeque<>();

    }

    // Constructor
    public GasStation(int numGasPumps, int carQlimit) {
        // add additional statements

        // use ArrayDeque to construct FIFO queue objects
        this.carQ = new ArrayDeque<Car>();
        this.freeGasPumpQ = new ArrayDeque<GasPump>();

        // construct PriorityQueue object
        // overide compare() in Comparator to compare busy GasPump objects
        this.busyGasPumpQ = new PriorityQueue<GasPump>(numGasPumps, new BusyGasPumpComparator());

        // initialize carQlimit
        this.carQSizeLimit = carQlimit;

        // Construct GasPump objects and insert into FreeGasPumpQ
        // assign gas pump ID from 1, 2,..., numGasPumps
        for (int i = 0; i < numGasPumps; i++) {
            this.freeGasPumpQ.add(new GasPump(i + 1));
        }
    }

    // -------------------------------------------------
    // freeGasPumpQ methods: remove, insert, empty, size
    // -------------------------------------------------

    public GasPump removeFreeGasPumpQ() {
        // remove and return a free gasPump
        // Add statetments
        return freeGasPumpQ.poll();
    }

    public void insertFreeGasPumpQ(GasPump gasPump) {
        // insert a free gasPump
        // Add statetments
        freeGasPumpQ.add(gasPump);
    }


    public boolean emptyFreeGasPumpQ() {
        // is freeGasPumpQ empty?
        // Add statetments
        return freeGasPumpQ.isEmpty();
    }


    public int numFreeGasPumps() {
        // get number of free gasPumps
        // Add statetments
        if (emptyFreeGasPumpQ()) {return 0;}

        return freeGasPumpQ.size();
    }

    // --------------------------------------------------------
    // busyGasPumpQ methods: remove, insert, empty, size, peek
    // --------------------------------------------------------


    public GasPump removeBusyGasPumpQ() {
        // remove and return a busy gasPump
        // Add statetments
        return busyGasPumpQ.poll();
    }

    public void insertBusyGasPumpQ(GasPump gasPump) {
        // insert a busy gasPump
        // Add statetments
        busyGasPumpQ.add(gasPump);
    }

    public boolean emptyBusyGasPumpQ() {
        // is busyGasPumpQ empty?
        // Add statetments
        return busyGasPumpQ.isEmpty();

    }

    public int numBusyGasPumps() {
        // get number of busy gasPumps
        // Add statetments
        if (emptyBusyGasPumpQ()) {return 0;}

        return busyGasPumpQ.size();
    }

    public GasPump getFrontBusyGasPumpQ() {
        // get front of busy gasPumps
        // "retrieve" but not "remove"
        // Add statetments
        return busyGasPumpQ.peek();
    }


    // --------------------------------------------------------
    // carQ methods: remove, insert, empty, size
    // 		   and check isCarQTooLong()
    // --------------------------------------------------------

    public Car removeCarQ() {
        // remove and return a car
        // Add statetments
        return carQ.poll();
    }

    public void insertCarQ(Car car) {
        // insert a car
        // Add statetments
        carQ.offer(car);
    }

    public boolean emptyCarQ() {
        // is carQ empty?
        // Add statetments
        return carQ.isEmpty();

    }

    public int numWaitingCars() {
        // get number of cars
        // Add statetments
        if(emptyCarQ()) {return 0;}
        return carQ.size();
    }


    public boolean isCarQTooLong() {
        // is carQ too long?
        // Add statetments
        if ((!carQ.isEmpty() == false) && (carQ.size() >= carQSizeLimit))
        {
            return true;
        }

        return false;
    }


    public void printStatistics() {
        System.out.println("\t# waiting cars        : " + numWaitingCars());
        System.out.println("\t# busy gas pumps      : " + numBusyGasPumps());
        System.out.println("\t# free gas pumps      : " + numFreeGasPumps());
    }

    public static void main(String[] args) {

        // quick check

        GasStation sc = new GasStation(4, 5);
        Car c1 = new Car(1, 18, 10);
        Car c2 = new Car(2, 33, 11);
        Car c3 = new Car(3, 21, 12);
        Car c4 = new Car(3, 37, 13);

        // insert cars into carQ
        sc.insertCarQ(c1);
        sc.insertCarQ(c2);
        sc.insertCarQ(c3);
        sc.insertCarQ(c4);
        System.out.println("" + sc.carQ);
        System.out.println("===============================================");
        System.out.println("Remove car:" + sc.removeCarQ());
        System.out.println("Remove car:" + sc.removeCarQ());
        System.out.println("Remove car:" + sc.removeCarQ());
        System.out.println("Remove car:" + sc.removeCarQ());
        System.out.println("===============================================");

        // remove gasPumps from freeGasPumpQ
        System.out.println("freeGasPumpQ:" + sc.freeGasPumpQ);
        System.out.println("===============================================");
        GasPump p1 = sc.removeFreeGasPumpQ();
        GasPump p2 = sc.removeFreeGasPumpQ();
        GasPump p3 = sc.removeFreeGasPumpQ();
        GasPump p4 = sc.removeFreeGasPumpQ();
        System.out.println("Remove free gas pump:" + p1);
        System.out.println("Remove free gas pump:" + p2);
        System.out.println("Remove free gas pump:" + p3);
        System.out.println("Remove free gas pump:" + p4);
        System.out.println("===============================================");
        System.out.println("freeGasPumpQ:" + sc.freeGasPumpQ);
        System.out.println("busyGasPumpQ:" + sc.busyGasPumpQ);
        System.out.println("===============================================");

        // insert car to gas pumps
        p1.switchFreeToBusy(c1, 13);
        p2.switchFreeToBusy(c2, 13);
        p3.switchFreeToBusy(c3, 13);
        p4.switchFreeToBusy(c4, 13);
        System.out.println("Assign cars to gas pumps");

        // insert gas pumps into busyGasPumpQ
        System.out.println("===============================================");
        System.out.println("Insert busy gas pumps to busy Q");
        sc.insertBusyGasPumpQ(p1);
        sc.insertBusyGasPumpQ(p2);
        sc.insertBusyGasPumpQ(p3);
        sc.insertBusyGasPumpQ(p4);
        System.out.println("busyGasPumpQ:" + sc.busyGasPumpQ);
        System.out.println("===============================================");

        // remove gas pumps from busyGasPumpQ
        System.out.println("Remove busy gas pumps from  busy Q");
        p1 = sc.removeBusyGasPumpQ();
        p2 = sc.removeBusyGasPumpQ();
        p3 = sc.removeBusyGasPumpQ();
        p4 = sc.removeBusyGasPumpQ();

        p1.switchBusyToFree();
        p2.switchBusyToFree();
        p3.switchBusyToFree();
        p4.switchBusyToFree();
        System.out.println("Remove busy gas pump:" + p1);
        System.out.println("Remove busy gas pump:" + p2);
        System.out.println("Remove busy gas pump:" + p3);
        System.out.println("Remove busy gas pump:" + p4);

    }


}

