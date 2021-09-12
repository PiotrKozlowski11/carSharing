package carsharing;

import java.sql.SQLException;

public class Main {


    /**
     * Main
     * @param args String[] - if args are given and first one is '-databaseFileName' the second one is database name
     */
    public static void main(String[] args) {

        String databaseName = "carshare";
        if (args.length > 1 && args[0].equals("-databaseFileName")) {
            databaseName = args[1];
        }

        try {
            Controller controller = new Controller(databaseName);
            controller.run();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }



    }


}







