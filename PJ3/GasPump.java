// DO NOT ADD NEW METHODS OR DATA FIELDS!
// DO NOT MODIFY METHODS OR DATA FIELDS!

package PJ3;

class GasPump {


   // pump id and current car which is served by this gas pump 
   private int pumpId;
   private Car currentCar;

   // start time and end time of current interval
   private int startIntervalTime;
   private int endIntervalTime;

   // for keeping statistical data
   private int totalFreeTime;
   private int totalBusyTime;
   private int totalCars;


   // Constructor
   GasPump()
   {
       this(1);
       this.currentCar = null;
       this.startIntervalTime = 0;
       this.endIntervalTime = 0;
       this.totalFreeTime = 0;
       this.totalBusyTime = 0;
       this.totalCars = 0;
   }


   // Constructor with gas pump id
   GasPump(int gasPumpId)
   {
       this.pumpId = gasPumpId;
       this.currentCar = null;
       this.startIntervalTime = 0;
       this.endIntervalTime = 0;
       this.totalFreeTime = 0;
       this.totalBusyTime = 0;
       this.totalCars = 0;
   }

  //--------------------------------
   // accessor methods
   //--------------------------------

   int getPumpId () 
   {
	return pumpId;
   }

   Car getCurrentCar() 
   {
	return currentCar;
   }

   int getEndIntervalTime() 
   {
	return endIntervalTime;
   }


   //---------------------------------
   // GasPump State Transition methods
   //---------------------------------

   // State transition : FREE interval -> BUSY interval:
   void switchFreeToBusy (Car currentCar, int currentTime)
   {
  	// Main goal  : switch from free interval to busy interval
  	//
  	// steps : end Free interval - set endIntervalTime, update totalFreeTime
  	//    	   start Busy interval - set startIntervalTime , endIntervalTime, currentCar, 
  	// 	   update totalCars

	// add statements

       endIntervalTime = currentTime;
       totalFreeTime += (endIntervalTime - startIntervalTime);
       startIntervalTime = currentTime;
       this.currentCar = currentCar;
       endIntervalTime = startIntervalTime + currentCar.getServiceDuration();
       totalCars++;
   }

   // State transition : BUSY interval -> FREE interval:
   Car switchBusyToFree ()
   {
   	// Main goal : switch from busy interval to free interval
   	// 
  	// steps : end Busy interval - update totalBusyTime 
  	// 	   start Free interval - set startIntervalTime 
  	//         return currentCar (just done with service)

	// add statements
       totalBusyTime += (endIntervalTime - startIntervalTime);
       startIntervalTime = endIntervalTime;
       Car temp = currentCar;
       this.currentCar = null;
       return temp;
   }



   //-------------------------------------------------------------------
   // Update totalBusyTime and totalFreeTime at the end of simulation
   //
   // use these method at the end of simulation to update gas pump 
   // totalFreeTime or totalBusyTime data in free and busy queues
   //------------------------------------------------------------------


   void updateTotalFreeTimeEndSimulationTime(int endsimulationtime)
   {
	// add statements

       totalFreeTime += (endsimulationtime - startIntervalTime);

   }

   void updateTotalBusyTimeEndSimulationTime(int endsimulationtime)
   {
	// add statements
       int arrival = currentCar.getArrivalTime();
       totalBusyTime += (endsimulationtime - arrival);
   }


   //--------------------------------
   // Print statistical data
   //--------------------------------
   void printStatistics () 
   {
  	// print gasPump statistics, see project statement

  	System.out.println("\t\tGasPump ID           : "+pumpId);
  	System.out.println("\t\tTotal free time      : "+totalFreeTime);
  	System.out.println("\t\tTotal service time   : "+totalBusyTime);
  	System.out.println("\t\tTotal # of cars      : "+totalCars);
  	if (totalCars > 0)
  	    System.out.format("\t\tAverage service time : %.2f%n\n",(totalBusyTime*1.0)/totalCars);
   }

   public String toString()
   {
	return "GasPumpID:"+pumpId+
	       ":startIntervalTime="+startIntervalTime+
	       ":endIntervalTime="+endIntervalTime+
  	       ":total free time="+totalFreeTime+
  	       ":total service time="+totalBusyTime+
	       ":Car:"+currentCar;
   }

   public static void main(String[] args) {
        Car mycar = new Car(1,5,15);
        System.out.println(mycar);
	GasPump mypump = new GasPump(5);
        System.out.println(mypump);
        System.out.println("\nStart service car:");
        mypump.switchFreeToBusy (mycar, 20);
        System.out.println(mypump);
        System.out.println("\nEnd service car:");
        mypump.switchBusyToFree();
        System.out.println(mypump);
        System.out.println("\nGas pump statistics data:");
        mypump.printStatistics();

   }

}

