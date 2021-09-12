package carsharing;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;


/**
 * class controlling all options and menus available in the program
 */
public class Controller {

    // Main Menu
    private static final Menu menuMain = new Menu();
    // Menu Logged as a Manager
    private static final Menu menuLoggedManager = new Menu();
    // Menu logged as a manager - submenu car handling
    private static final Menu menuCompanyCar = new Menu();
    // Menu logged as a customer
    private static final Menu menuCustomerActions = new Menu();
    // Object to handle communication with database
    private final DataAccess dataAccess;
    // Menu - company list
    private Menu menuCompany;

    /**
     * Constructor to establish connection with DataAccess and initialize menu options
     *
     * @param dataBaseName Name of a database
     * @throws SQLException           Thrown by h2DataBase. Exception is thrown when connections cannot be established
     * @throws ClassNotFoundException Thrown by h2DataBase. Exception is thrown when driver is not found
     */
    public Controller(String dataBaseName) throws SQLException, ClassNotFoundException {
        this.dataAccess = new DataAccess(dataBaseName);
        initializeMenu();

    }

    /**
     * Main program loop
     */
    public void run() {

        boolean dropBase = false;
        if (dropBase) {
            try {

                dataAccess.dropTableCustomer();
                dataAccess.dropTableCar();
                dataAccess.dropTableCompany();


            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        boolean createBase = false;
        if (createBase) {
            try {
                dataAccess.createTableCompany();
                dataAccess.createTableCar();
                dataAccess.createTableCustomer();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        /*
        try {

            dataAccess.restartAutoIncrement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

         */


        while (true) {
            switch (menuMain.getChoice()) {
                case 1 -> handleMenuManager();
                case 2 -> handleMenuChooseCustomer();
                case 3 -> CreateCustomer();
                case 0 -> Exit();
            }
        }


    }

    /**
     * Initialize all menus with all valid options
     */
    private void initializeMenu() {
        menuMain.putItemList(1, "Log in as a manager");
        menuMain.putItemList(2, "Log in as a customer");
        menuMain.putItemList(3, "Create a customer");
        menuMain.putItemList(0, "Exit");

        menuLoggedManager.putItemList(1, "Company list");
        menuLoggedManager.putItemList(2, "Create a company");
        menuLoggedManager.putItemList(0, "Back");

        menuCompanyCar.putItemList(1, "Car list");
        menuCompanyCar.putItemList(2, "Create a car");
        menuCompanyCar.putItemList(0, "Back");

        menuCustomerActions.putItemList(1, "Rent a car");
        menuCustomerActions.putItemList(2, "Return a rented car");
        menuCustomerActions.putItemList(3, "My rented car");
        menuCustomerActions.putItemList(0, "Back");
    }


    /**
     * Ask user to enter company name
     * @return String - company name
     */
    private String getCompanyName() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the company name:");
        return "'" + scanner.nextLine() + "'";
    }

    /**
     * Ask user to enter car name
     * @return String - car name
     */
    private String getCarName() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the car name:");
        return "'" + scanner.nextLine() + "'";
    }

    /**
     * Ask user to enter customer name
     * @return String - customer name
     */
    private String getCustomerName() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the customer name:");
        return "'" + scanner.nextLine() + "'";
    }

    /**
     * Method to handle Menu when Manager is logged
     */
    private void handleMenuManager() {

        while (true) {
            switch (menuLoggedManager.getChoice()) {
                case 1 -> {
                    boolean existCompany = menuCompanyList();
                    if (existCompany) {
                        handleMenuManagerChooseCompany();
                    }
                }
                case 2 -> CreateCompany();
                case 0 -> {
                    return;
                }
            }
        }

    }

    /**
     * Method to handle menu when manager is logged -> submenu choose a company or go back to previous menu
     */
    private void handleMenuManagerChooseCompany() {
        System.out.println("Choose the company:");
        int companyID = menuCompany.getChoice();
        if (companyID != 0) {
            handleMenuManagerChooseCar(companyID);
        }
    }

    /**
     * Method to handle menu when manager is logged -> submenu to display all cars of selected company,
     * create a Car of selected company
     * or go back to previous submenu
     * @param selectedCompany int - selected company by manager
     */
    private void handleMenuManagerChooseCar(int selectedCompany) {
        while (true) {
            switch (menuCompanyCar.getChoice()) {
                case 1 -> {
                    try {
                        List<Car> listCar = dataAccess.getALLCars(selectedCompany);
                        if (listCar.size() == 0) {
                            System.out.println("The car list is empty!");
                        } else {
                            /*
                            for (int i = 0; i < listCar.size(); i++) {
                                System.out.println((i + 1) + ". " + listCar.get(i).getName());
                            }

                             */
                            IntStream.range(0, listCar.size())
                                    .forEach(i -> System.out.println((i + 1) + ". " + listCar.get(i).getName()));
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                case 2 -> {
                    try {
                        dataAccess.createCar(getCarName(), selectedCompany);
                        System.out.println("The car was created!");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                case 0 -> {
                    //state = MenuStates.LOGGED_AS_MANAGER;
                    return;
                }
            }
        }

    }

    /**
     * Method to check if there are any companies in database.
     * If there are companies add them to menuCompany and add Back option
     * @return boolean - true if there are companies in database, false if there are not any
     */
    private boolean menuCompanyList() {
        try {
            List<Company> list = dataAccess.getAllCompanies();
            if (list.size() == 0) {
                System.out.println("The company list is empty!");
                //state = MenuStates.LOGGED_AS_MANAGER;
                return false;
            } else {
                menuCompany = new Menu();
                list.forEach((company -> menuCompany.putItemList(company.getId(), company.getName())));
                menuCompany.putItemList(0, "Back");

                return true;

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    /**
     * Method used to create company by manager
     */
    private void CreateCompany() {
        try {
            dataAccess.createCompany(getCompanyName());
            System.out.println("The company was created!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //state = MenuStates.LOGGED_AS_MANAGER;
    }

    /**
     * Method used to create customer in main menu
     */
    private void CreateCustomer() {
        try {
            dataAccess.createCustomer(getCustomerName());
            System.out.println("The customer was added!");
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    /**
     * Handling submenu when logging as a customer is chosen.
     * If there are not any customers in database it returns to previous menu.
     * In other case all customers are displayed and user is asked to select one or go back to previous menu.
     */
    private void handleMenuChooseCustomer() {

        try {
            List<Customer> listCustomer = dataAccess.getALLCustomers();
            if (listCustomer.size() == 0) {
                System.out.println("The customer list is empty!");
                //state = MenuStates.MAIN_MENU;
            } else {
                System.out.println("Choose a customer:");
                Menu customers = new Menu();
                listCustomer.forEach(customer -> customers.putItemList(customer.getId(), customer.getName()));
                customers.putItemList(0, "Back");

                int customerId = customers.getChoice();
                if (customerId != 0) {
                    handleCustomerMenu(customerId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Handling all menu options when user is logged as a customer.
     * There are '3' valid options
     * 1 - Rent a car. If user already has a rented car displays relevant message.
     * Else ask user for company ID and car ID to be rented.
     * 2 -  Return rented car. If no car is rented displays relevant message.
     * 3 - Display info about rented car and car's company. If no car is rented display relevant message.
     * @param customerID int - ID of selected customer
     * @throws SQLException Exception is thrown when connections cannot be established
     */
    private void handleCustomerMenu(int customerID) throws SQLException {
        Customer customer = dataAccess.getCustomer(customerID);
        while (true) {
            switch (menuCustomerActions.getChoice()) {
                case 1 -> {
                    if (customer.getRentedCarId() != null) {
                        System.out.println("You've already rented a car!");
                    } else {
                        if (menuCompanyList()) {
                            System.out.println("Choose a company: ");
                            int companyID = menuCompany.getChoice();
                            menuChooseCarCustomer(companyID, customerID);
                            customer = dataAccess.getCustomer(customerID);
                        }

                    }
                }
                case 2 -> {
                    if (customer.getRentedCarId() == null) {
                        System.out.println("You didn't rent a car!");
                    } else {
                        dataAccess.customerReturnCar(customerID);
                        System.out.println("You've returned a rented car!");
                        customer = dataAccess.getCustomer(customerID);
                    }
                }
                case 3 -> {
                    if (customer.getRentedCarId() == null) {
                        System.out.println("You didn't rent a car!");
                    } else {
                        Car car = dataAccess.getCar(customer.getRentedCarId());
                        Company company = dataAccess.getCompany(car.getCompany_ID());
                        System.out.println("Your rented car:");
                        System.out.println(car.getName());
                        System.out.println("Company:");
                        System.out.println(company.getName());

                    }
                }
                case 0 -> {
                    return;
                }
            }
        }

    }

    /**
     * Submenu when user is logged as a customer - user is selecting car to be rented by him.
     * Valid options are consecutive numbers
     * Selecting 0 moves to previous menu.
     * @param companyID int - ID of a company
     * @param customerID int - ID of a customer
     */
    private void menuChooseCarCustomer(int companyID, int customerID) {

        try {
            List<Car> listCar = dataAccess.getALLCarsNotRented(companyID);

            if (listCar.size() == 0) {
                System.out.println("The car list is empty!");
            } else {
                Menu menuChooseCar = new Menu();

                IntStream.range(0, listCar.size()).
                        forEach(i -> menuChooseCar.putItemList(i + 1, listCar.get(i).getName()));
                menuChooseCar.putItemList(0, "Back");

                System.out.println("Choose a car:");
                int selectedCar = menuChooseCar.getChoice() - 1;
                if (selectedCar != -1) {
                    dataAccess.customerRentCar(listCar.get(selectedCar).getId(), customerID);
                    System.out.println("You rented '" + listCar.get(selectedCar).getName() + "'");
                }


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void Exit() {

        try {
            dataAccess.closeBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.exit(0);

    }
}
