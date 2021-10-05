package carsharing.persistence;

import java.sql.*;

/**
 * Class used to connect to h2 Database and handle queries
 */
public class H2DataBase {

    //name of database driver
    private static final String JDBC_DRIVER = "org.h2.Driver";

    private final Connection connection;
    private final Statement statement;

    /**
     * Constructor for establishing connection between database.
     *
     * @param URL        Database URL
     * @param USER       Username
     * @param PASS       password
     * @param autoCommit whether database should work in autocommit mode
     * @throws ClassNotFoundException Exception is thrown when driver is not found
     * @throws SQLException           Exception is thrown when connections cannot be established
     */
    public H2DataBase(String URL, String USER, String PASS, boolean autoCommit)
            throws ClassNotFoundException, SQLException {
        Class.forName(JDBC_DRIVER);
        System.out.println("Connecting to database... ");
        connection = DriverManager.getConnection(URL, USER, PASS);
        connection.setAutoCommit(autoCommit);
        statement = connection.createStatement();
    }

    /**
     * Method used to execute queries which does not return any data
     *
     * @param query Query in string form
     * @throws SQLException Exception is thrown when database access error occurs
     */
    public void executeStatement(String query) throws SQLException {
        statement.execute(query);
    }

    /**
     * Method used to execute queries which returns ResultSet
     *
     * @param query Query in string form
     * @return ResultSet, never null
     * @throws SQLException Exception is thrown when database access error occurs
     */
    public ResultSet executeQuery(String query) throws SQLException {
        return statement.executeQuery(query);
    }

    /**
     * Method used to close connection and statement to database
     *
     * @throws SQLException Exception is thrown when database access error occurs
     */
    public void closeBase() throws SQLException {
        statement.close();
        connection.close();
    }

}


