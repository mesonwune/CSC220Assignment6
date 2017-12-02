// DO NOT ADD NEW METHODS OR DATA FIELDS!
// DO NOT MODIFY METHODS OR DATA FIELDS!

package PJ3;

class Car
{
    private int carId;
    private int serviceDuration;
    private int arrivalTime;

    // default constructor
    Car()
    {
  	carId           = -1;
  	serviceDuration = -1;
  	arrivalTime     = 0;
    }

    // constructor to set carId, serviceDuration,
    // and arrivalTime 
    Car(int CID, int serviceTime, int arriveTime)
    {
  	carId           = CID;
  	serviceDuration = serviceTime;
  	arrivalTime     = arriveTime;
    }

    int getServiceDuration() 
    {
  	return serviceDuration; 
    }

    int getArrivalTime() 
    {
  	return arrivalTime; 
    }

    int getCarId() 
    {
  	return carId; 
    }

    public String toString()
    {
    	return "CarID="+carId+":ServiceDuration="+serviceDuration+":ArrivalTime="+arrivalTime;

    }

    public static void main(String[] args) {
	// quick check!
	Car mycar = new Car(1,30,40);
	System.out.println("Car Info:"+mycar);
    }
}
