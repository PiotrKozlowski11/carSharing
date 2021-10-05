package carsharing.business;

/**
 * Class which represents Car
 */
public class Car {
    // car ID
    private final int id;
    // car Name
    private final String name;
    // ID of a company to which car belongs
    private final int company_ID;

    /**
     * Simple constructor with all the required parameters
     *
     * @param id         car ID
     * @param name       car Name
     * @param company_ID ID of car's company
     */
    public Car(int id, String name, int company_ID) {
        this.id = id;
        this.name = name;
        this.company_ID = company_ID;
    }

    /**
     * @return String with car ID and Name. Company ID is omitted
     */
    @Override
    public String toString() {
        return id + ". " + name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCompany_ID() {
        return company_ID;
    }
}

