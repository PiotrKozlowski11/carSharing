package carsharing.business;

/**
 * Class which represents company
 */
public class Company {
    // id of a company
    private final int id;
    // name of a company
    private final String name;

    /**
     * @param id   ID of a company
     * @param name Name of a company
     */
    public Company(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * @return String with company ID and Name
     */
    @Override
    public String toString() {
        return id + ". " + name;
    }

    /**
     * @return ID getter
     */
    public int getId() {
        return id;
    }

    /**
     * @return Name getter
     */
    public String getName() {
        return name;
    }
}
