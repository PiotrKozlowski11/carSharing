package carsharing;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * class used to manage connection with H2DataBase with use of H2DattaBase class and store all queries.
 */
public class DataAccess {

    //Username
    private static final String User = "sa";
    //password
    private static final String password = "";
    //folder where database will be stored
    private static final String FOLDerName = "jdbc:h2:file:./src/carsharing/db/";
    //set autocommit to true
    private static final boolean autoCommit = true;
    private final H2DataBase h2DataBase;

    /**
     * Constructor used to establish connection with database and create h2database object
     * @param dataBaseName String name of database
     * @throws SQLException Thrown by h2DataBase. Exception is thrown when connections cannot be established
     * @throws ClassNotFoundException Thrown by h2DataBase. Exception is thrown when driver is not found
     */
    public DataAccess(String dataBaseName) throws SQLException, ClassNotFoundException {
        String URL = FOLDerName.concat(dataBaseName);

        h2DataBase = new H2DataBase(URL, User, password, autoCommit);

    }

    /**
     * Method to Create Company Table
     * @throws SQLException Exception is thrown when database access error occurs
     */
    public void createTableCompany() throws SQLException {
        h2DataBase.executeStatement(Queries.createTableCompany);

    }
    /**
     * Method to Create Car Table
     * @throws SQLException Exception is thrown when database access error occurs
     */
    public void createTableCar() throws SQLException {
        h2DataBase.executeStatement(Queries.createTableCar);
        h2DataBase.executeStatement(Queries.TableCarAddForeignKey);
    }
    /**
     * Method to Create Customer Table
     * @throws SQLException Exception is thrown when database access error occurs
     */
    public void createTableCustomer() throws SQLException {
        h2DataBase.executeStatement(Queries.createTableCustomer);
        h2DataBase.executeStatement(Queries.TableCustomerAddForeignKey);
    }

    /**
     * Method to Drop Company Table
     * @throws SQLException Exception is thrown when database access error occurs
     */
    public void dropTableCompany() throws SQLException {
        h2DataBase.executeStatement(Queries.dropTableCompany);
    }

    /**
     * Method to Drop Car Table
     * @throws SQLException Exception is thrown when database access error occurs
     */
    public void dropTableCar() throws SQLException {
        h2DataBase.executeStatement(Queries.dropTableCar);
    }
    /**
     * Method to Drop Customer Table
     * @throws SQLException Exception is thrown when database access error occurs
     */
    public void dropTableCustomer() throws SQLException {
        h2DataBase.executeStatement(Queries.dropTableCustomer);
    }

    /**
     * Method to restart incrementing from 1 in company and car tables
     * @throws SQLException Exception is thrown when database access error occurs
     */
    public void restartAutoIncrement() throws SQLException {
        h2DataBase.executeStatement("ALTER TABLE COMPANY ALTER COLUMN ID RESTART WITH 1");
        h2DataBase.executeStatement("ALTER TABLE CAR ALTER COLUMN ID RESTART WITH 1");
    }

    /**
     * Add new company to database
     * @param companyName String - Name of a new company
     * @throws SQLException Exception is thrown when database access error occurs
     */
    public void createCompany(String companyName) throws SQLException {
        h2DataBase.executeStatement(String.format(Queries.InsertIntoCompany, companyName));
    }

    /**
     * Get company from database by it ID number.
     * @param companyID int - This is company's id number
     * @return Company object
     * @throws SQLException Exception is thrown when database access error occurs
     */
    public Company getCompany(int companyID) throws SQLException {
        ResultSet resultSet = h2DataBase.executeQuery(String.format(Queries.getCompany, companyID));
        resultSet.next();
        int id = resultSet.getInt("ID");
        String companyName = resultSet.getString("NAME");
        return new Company(id, companyName);
    }

    /**
     * Get all companies from database
     * @return List<Company>
     * @throws SQLException Exception is thrown when database access error occurs
     */
    public List<Company> getAllCompanies() throws SQLException {

        ResultSet resultSet = h2DataBase.executeQuery(Queries.getAllCompanies);
        List<Company> list = new ArrayList<>();

        while (resultSet.next()) {
            int id = resultSet.getInt("ID");
            String companyName = resultSet.getString("NAME");
            list.add(new Company(id, companyName));

        }
        return list;
    }

    /**
     * Add new car to database
     * @param carName String - new Car name
     * @param companyID int - company ID of a car
     * @throws SQLException Exception is thrown when database access error occurs
     */
    public void createCar(String carName, int companyID) throws SQLException {
        h2DataBase.executeStatement(String.format(Queries.InsertIntoCar, carName, companyID));
    }

    /**
     * Get company from database by it ID number.
     * @param carID int - This is car's id number
     * @return Car object
     * @throws SQLException Exception is thrown when database access error occurs
     */
    public Car getCar(int carID) throws SQLException {
        ResultSet resultSet = h2DataBase.executeQuery(String.format(Queries.getCar, carID));
        resultSet.next();
        int id = resultSet.getInt("ID");
        String carName = resultSet.getString("NAME");
        int companyID = resultSet.getInt("COMPANY_ID");
        return new Car(id, carName, companyID);
    }

    /**
     * Get all not rented cars by selected company ID
     * @param companyID int - ID of selected company
     * @return List<Car>
     * @throws SQLException Exception is thrown when database access error occurs
     */
    public List<Car> getALLCarsNotRented(int companyID) throws SQLException {
        ResultSet resultSet = h2DataBase.executeQuery(String.format(Queries.getAllCarsNotRented, companyID));
        List<Car> list = new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("ID");
            String carName = resultSet.getString("NAME");
            int company_ID = resultSet.getInt("company_ID");
            list.add(new Car(id, carName, company_ID));
        }
        return list;

    }


    /**
     * Get all cars by selected company ID
     * @param companyID int - ID of selected company
     * @return List<Car>
     * @throws SQLException Exception is thrown when database access error occurs
     */
    public List<Car> getALLCars(int companyID) throws SQLException {
        ResultSet resultSet = h2DataBase.executeQuery(String.format(Queries.getAllCars, companyID));
        List<Car> list = new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("ID");
            String carName = resultSet.getString("NAME");
            int company_ID = resultSet.getInt("company_ID");
            list.add(new Car(id, carName, company_ID));
        }
        return list;

    }

    /**
     * Add new customer to database. Sets id of rented car to "NULL"
     * @param customerName String - new Car name
     * @throws SQLException Exception is thrown when database access error occurs
     */
    public void createCustomer(String customerName) throws SQLException {
        h2DataBase.executeStatement(String.format(Queries.InsertIntoCustomer, customerName, "NULL"));
    }

    /**
     * Get customer from database by it ID
     * @param customerID int - Customer's id
     * @return Customer object
     * @throws SQLException Exception is thrown when database access error occurs
     */
    public Customer getCustomer(int customerID) throws SQLException {
        ResultSet resultSet = h2DataBase.executeQuery(String.format(Queries.getCustomer, customerID));
        resultSet.next();
        int id = resultSet.getInt("ID");
        String customerName = resultSet.getString("NAME");
        //Integer car_ID = resultSet.getInt("RENTED_CAR_ID");
        Integer car_ID = (Integer) resultSet.getObject("RENTED_CAR_ID");
        return new Customer(id, customerName, car_ID);
    }

    /**
     * Get all customers from database
     * @return List of Customers
     * @throws SQLException Exception is thrown when database access error occurs
     */
    public List<Customer> getALLCustomers() throws SQLException {
        ResultSet resultSet = h2DataBase.executeQuery(Queries.getAllCustomers);
        List<Customer> list = new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("ID");
            String customerName = resultSet.getString("NAME");
            int car_ID = resultSet.getInt("RENTED_CAR_ID");
            list.add(new Customer(id, customerName, car_ID));
        }
        return list;

    }

    /**
     * Update customer table - allows renting car (by its ID) by specific customer(by it's ID)
     * @param carID int - id of a car which will be rented
     * @param customerID int - id of a customer who is renting a car
     * @throws SQLException Exception is thrown when database access error occurs
     */
    public void customerRentCar(int carID, int customerID) throws SQLException {
        h2DataBase.executeStatement(String.format(Queries.updateCustomer, carID, customerID));

    }

    /**
     * Update customer table - return rented car by setting rented_car_id to null
     * @param customerID int - id of a customer
     * @throws SQLException Exception is thrown when database access error occurs
     */
    public void customerReturnCar(int customerID) throws SQLException {
        h2DataBase.executeStatement(String.format(Queries.updateCustomer, "NULL", customerID));
    }

    /**
     * closing h2database
     * @throws SQLException Exception is thrown when database access error occurs
     */
    public void closeBase() throws SQLException {
        h2DataBase.closeBase();
    }

    /**
     * class stores all queries in string format
     */
    static class Queries {

        /**
         * Query to create COMPANY table with following parameters:
         * ID int NOT NULL AUTO_INCREMENT -> ID is primary key
         * NAME varchar(30) NOT NULL -> Name is UNIQUE
         */
        private static final String createTableCompany = "CREATE TABLE " +
                "IF NOT EXISTS " +
                "COMPANY (" +
                "    ID int NOT NULL AUTO_INCREMENT," +
                "    NAME varchar(30) NOT NULL," +
                "    UNIQUE INDEX company_ak_1 (NAME)," +
                "    CONSTRAINT COMPANY_pk PRIMARY KEY (ID)" +
                ");";

        /**
         * Query to create CAR table with following parameters:
         * ID int NOT NULL AUTO_INCREMENT -> ID is primary key
         * NAME varchar(30) NOT NULL -> Name is UNIQUE
         * company_ID int NOT NULL
         */
        private static final String createTableCar = "CREATE TABLE " +
                "IF NOT EXISTS " +
                "CAR " +
                "(ID int NOT NULL AUTO_INCREMENT," +
                "    NAME varchar(30) NOT NULL," +
                "    company_ID int NOT NULL," +
                "    UNIQUE INDEX car_ak_1 (NAME)," +
                "    CONSTRAINT CAR_pk PRIMARY KEY (id)" +
                ");";

        /**
         * Query to add foreign key to table car which references company table (ID parameter)
         */
        private static final String TableCarAddForeignKey = "ALTER TABLE CAR " +
                "ADD CONSTRAINT car_company FOREIGN KEY  (COMPANY_ID) " +
                "REFERENCES COMPANY (ID);";

        /**
         * Query to create CUSTOMER table with following parameters:
         * ID int NOT NULL AUTO_INCREMENT -> ID is primary key
         * NAME varchar(30) NOT NULL -> Name is UNIQUE
         * RENTED_CAR_ID int NULL -> id can be null when customer does not have rented car
         */
        private static final String createTableCustomer = "CREATE TABLE" +
                " IF NOT EXISTS" +
                " CUSTOMER (" +
                "    ID int NOT NULL AUTO_INCREMENT," +
                "    NAME varchar(30) NOT NULL," +
                "    RENTED_CAR_ID int NULL," +
                "    CONSTRAINT CUSTOMER_pk PRIMARY KEY (ID)" +
                ");";

        /**
         * Query to add foreign key to table customer which references car table (ID parameter)
         */
        private static final String TableCustomerAddForeignKey = "ALTER TABLE CUSTOMER" +
                " ADD CONSTRAINT CUSTOMER_CAR FOREIGN KEY (RENTED_CAR_ID)" +
                "    REFERENCES CAR (id);";

        /**
         * Query to drop company table if it exists
         */
        private static final String dropTableCompany = "DROP TABLE " +
                "IF EXISTS " +
                "COMPANY";

        /**
         * Query to drop car table if it exists
         */
        private static final String dropTableCar = "DROP TABLE " +
                "IF EXISTS " +
                "CAR";

        /**
         * Query to drop customer table if it exists
         */
        private static final String dropTableCustomer = "DROP TABLE " +
                "IF EXISTS " +
                "CUSTOMER";

        /**
         * SELECT Query from company table. Returns ID, Name ordered by id
         */
        private static final String getAllCompanies = "SELECT ID, NAME, FROM company ORDER BY ID;";

        /**
         * Query to insert into company table. Parameter is only name id will be automatically granted
         */
        private static final String InsertIntoCompany = "INSERT INTO COMPANY(NAME) VALUES (%s);";


        /**
         * Get id, name and companyID from car table. Parameter is required company ID
         */
        private static final String getAllCars = "SELECT ID, NAME, company_ID" +
                " FROM car" +
                " WHERE company_ID=%s" +
                " ORDER BY ID;";


        /**
         * returns ID of Car, Name of Car, ID of car's company and ID of customer who rented the car.
         * Query returns only not rented car (customer ID is null)
         */
        private static final String getAllCarsNotRented = "SELECT CAR.ID, CAR.NAME, CAR.COMPANY_ID,CUSTOMER.RENTED_CAR_ID" +
                " FROM CAR" +
                " LEFT JOIN CUSTOMER ON CAR.ID=CUSTOMER.RENTED_CAR_ID" +
                " WHERE CAR.COMPANY_ID=%s" +
                " AND CUSTOMER.RENTED_CAR_ID IS NULL";

        /**
         * Insert into car table. Parameters: name and company_ID
         */
        private static final String InsertIntoCar = "INSERT INTO CAR(NAME,company_ID) VALUES (%s, %s);";

        /**
         * Insert into customer table. Parameters: customer name, rented_car_id
         */
        private static final String InsertIntoCustomer = "INSERT INTO CUSTOMER(NAME,RENTED_CAR_ID) VALUES (%s, %s);";

        /**
         * Get all customers from customer table. Return parameters ID int, name String, rented_car_id int.
         * Ordered by ID
         */
        private static final String getAllCustomers = "SELECT ID, NAME,RENTED_CAR_ID FROM CUSTOMER ORDER BY ID;";

        /**
         * Update customer table. Parameters: rented_car_id (can be null) and id of a customer
         */
        private static final String updateCustomer = "UPDATE CUSTOMER SET RENTED_CAR_ID = %s" +
                " WHERE ID=%s";

        /**
         * get customer's id, name, and rented_car_id by customer name
         */
        private static final String getCustomer = "SELECT ID,NAME,RENTED_CAR_ID " +
                "FROM CUSTOMER " +
                "WHERE ID=%s";

        /**
         * Ge car id,name,company_id by car id
         */
        private static final String getCar = "SELECT ID,NAME,COMPANY_ID " +
                "FROM CAR " +
                "WHERE ID=%s";

        /**
         * Get company id,name by company id
         */
        private static final String getCompany = "SELECT ID,NAME " +
                "FROM COMPANY " +
                "WHERE ID=%s";

    }


}


