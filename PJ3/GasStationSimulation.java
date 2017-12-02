package PJ3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

// You may add new functions or data in this class 
// You may modify any functions or data members here
// You must use Car, GasPump and GasStation classes
// to implement your simulator

class GasStationSimulation {

    // input parameters
    private int numGasPumps, carQSizeLimit;
    private int simulationTime, dataSource;
    private int chancesOfArrival, maxDuration;

    // statistical data
    private int numGoAway, numServed, totalWaitingTime;

    // internal data
    private int carIdCounter;        // car ID counter
    private GasStation gasStationObj; // Gas station object
    private Scanner dataFile;        // get car data from file
    private Random dataRandom;        // get car data using random function
    Scanner reader = new Scanner(System.in);
    private int carDataCounter;

    // most recent car arrival info, see getCarData()
    private boolean anyNewArrival;
    private int serviceDuration;

    // initialize data fields
    private GasStationSimulation() {
        // add statements
        this.numGasPumps = 0;
        this.carQSizeLimit = 0;
        this.simulationTime = 0;
        this.dataSource = 0;
        this.chancesOfArrival = 0;
        this.maxDuration = 0;
        this.numGoAway = 0;
        this.numServed = 0;
        this.totalWaitingTime = 0;
        this.carIdCounter = 0;
        this.gasStationObj = null;
        this.dataFile = null;
        this.anyNewArrival = false;
        this.serviceDuration = 0;
        this.carDataCounter = 0;
    }

    private void getUserParameters() {
        // read input parameters
        // setup dataFile or dataRandom
        // add statements

        //create exceptions to make sure that the limits are accounted for
        System.out.print("Enter the max simulation time: ");
        simulationTime = reader.nextInt();
        if (simulationTime > 10000) {throw new ArithmeticException("Simulation time is too big");}

        System.out.print("Enter the max duration for car service: ");
        maxDuration = reader.nextInt();
        if (maxDuration > 500) {throw new ArithmeticException("Max duration is too big");}

        System.out.print("Enter the chance arrival of a car: ");
        chancesOfArrival = reader.nextInt();
        if (chancesOfArrival < 1 || chancesOfArrival > 100) {throw new ArithmeticException("Chances of Arrival is too big");}

        System.out.print("Enter the # of gas pumps: ");
        numGasPumps = reader.nextInt();
        if (numGasPumps > 10) {throw new ArithmeticException("Too many gas pumps");}

        System.out.print("Enter the car queue size limit: ");
        carQSizeLimit = reader.nextInt();
        if (carQSizeLimit > 50) {throw new ArithmeticException("Car queue limit is too big");}

        System.out.print("Would you like to get car data from a file or random number generator?" +
                " Choose 1 for file or 0 for generator: ");
        dataSource = reader.nextInt();

        if (dataSource == 1)
        {
            System.out.print("What's the file name: ");
            Scanner reader2 = new Scanner(System.in);
            String input = reader2.nextLine();
            try {
                dataFile = new Scanner(new File(input));
            } catch (FileNotFoundException e)
            {
                System.out.println(input + " is not found.");
            }

        }
    }

    // Refer to step 1 in doSimulation
    // this method is called for each unit simulation time
    private void getCarData() {
        // get next car data : from file or random number generator
        // set anyNewArrival and serviceDuration
        // add statements

        if (dataSource == 1)
        {
            //make sure that there's still info in the data
            carDataCounter++;
            if (carDataCounter > 9) {return;}
            int first = dataFile.nextInt();
            int second = dataFile.nextInt();

            //then make the percentage for an arrival and the duration of service
            anyNewArrival = (((first % 100)+ 1) <= chancesOfArrival);
            serviceDuration = (second % maxDuration) + 1;
        }
        else
        {
            dataRandom = new Random();
            anyNewArrival = ((dataRandom.nextInt(100) + 1) <= chancesOfArrival);
            serviceDuration = dataRandom.nextInt(maxDuration) + 1;
        }

    }

    private void doSimulation() {
        // add statements

        // Initialize GasStation
        gasStationObj = new GasStation(numGasPumps, carQSizeLimit);

        // Time driver simulation loop
        for (int currentTime = 1; currentTime <= simulationTime; currentTime++) {

            //print out the current time
            System.out.println("The time is: " + currentTime);
            // Step 1: any new car enters the gas station?
            getCarData();

            if (anyNewArrival) {

                // Step 1.1: setup car data
                // Step 1.2: check car waiting queue too long?
                //           if it is too long, update numGoaway
                //           else enter car queue
                if (gasStationObj.isCarQTooLong())
                {
                    carIdCounter++;
                    numGoAway++;
                    System.out.println("Car leaves because the waiting queue is too long");
                }
                else
                {
                    carIdCounter++;
                    Car newCar = new Car(carIdCounter, serviceDuration, currentTime);
                    gasStationObj.insertCarQ(newCar);
                    System.out.println("Car has arrived.");
                }

            } else {
                System.out.println("\tNo new car!");
            }

            // Step 2: free busy pumps that are done at currentTime, add to free pumpQ
            for (int i = 0; i < gasStationObj.numBusyGasPumps(); i++)
            {
                GasPump newPump = gasStationObj.getFrontBusyGasPumpQ();
                if (newPump.getEndIntervalTime() <= currentTime)
                {
                    newPump = gasStationObj.removeBusyGasPumpQ();
                    Car newCar = newPump.switchBusyToFree();
                    System.out.println("\tCar #" + newCar.getCarId() +  " is done.");
                    gasStationObj.insertFreeGasPumpQ(newPump);
                    System.out.println("\tPump #" + newPump.getPumpId() + " is free.");
                }
            }

            // Step 3: get free pumps to serve waiting cars
            for (int i = 0; i < gasStationObj.numFreeGasPumps(); i++)
            {
                if (gasStationObj.numWaitingCars() != 0)
                {
                    Car newCar = gasStationObj.removeCarQ();
                    GasPump newPump = gasStationObj.removeFreeGasPumpQ();
                    newPump.switchFreeToBusy(newCar, currentTime);
                    gasStationObj.insertBusyGasPumpQ(newPump);

                    System.out.println("\tCar #" + newCar.getCarId() + " pumps.");
                    System.out.println("\tPump #" + newPump.getPumpId() + " starts serving Car #" + newCar.getCarId());
                    numServed++;
                    totalWaitingTime += currentTime - newCar.getArrivalTime();
                }
            }

            System.out.println();
        } // end simulation loop
        // clean-up - close scanner
    }

    private void printStatistics() {
        // add statements into this method!
        // print out simulation results
        // see the given example in project statement
        // you need to display all free and busy gas pumps


        // need to free up all cars in Car queue to get extra waiting time.
        // need to free up all gas pumps in free/busy queues to get extra free & busy time

        System.out.println("\t\t**** End of simulation ****");
        System.out.println("\tCars that were served:          " + numServed);
        System.out.println("\tCars that were not served:      " + numGoAway);
        System.out.println();

        System.out.println("\t\t**** Current Gas Pump Info ****");
        gasStationObj.printStatistics();

        System.out.println("\tTotal waiting time:       " + totalWaitingTime);
        System.out.println("\tAverage waiting time:     " + (totalWaitingTime/numServed));
        System.out.println();

        System.out.println("\t\t**** Busy Gas Pump Info ****");
        while (!gasStationObj.emptyBusyGasPumpQ())
        {
            GasPump temp = gasStationObj.removeBusyGasPumpQ();
            temp.printStatistics();
        }
        System.out.println();

        System.out.println("\t\t**** Current Gas Pump Info ****");
        while (!gasStationObj.emptyFreeGasPumpQ())
        {
            GasPump temp = gasStationObj.removeFreeGasPumpQ();
            temp.printStatistics();
        }

    }

    // *** main method to run simulation ****
    public static void main(String[] args) {
        GasStationSimulation gas_station_simulator = new GasStationSimulation();
        gas_station_simulator.getUserParameters();
        gas_station_simulator.doSimulation();
        gas_station_simulator.printStatistics();
    }

}
