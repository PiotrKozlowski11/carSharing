package carsharing;

/**
 * Class which represents Customer
 */
public class Customer {
    // id of a customer
    private final int id;
    // name of a customer
    private final String name;
    // id of customer's rented car
    private final Integer rentedCarId;

    /**
     * Simple constructor with all the required parameters
     * @param id customer ID
     * @param name customer Name
     * @param rentedCarId ID of customer's rented car
     */
    public Customer(int id, String name, Integer rentedCarId) {
        this.id = id;
        this.name = name;
        this.rentedCarId = rentedCarId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getRentedCarId() {
        return rentedCarId;
    }

    /**
     *
     * @return String with all class parameters
     */
    @Override
    public String toString() {
        return "ID: " + id + " name: " + name + " rented car ID: " + rentedCarId;
    }
}
