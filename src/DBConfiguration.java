import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * A class to store all your DB Related parameters.
 *
 * If you adjust this for you specific database all the examples should work
 *
 * Note: This is for ease of examples. In production code you would NEVER
 * store a root password like this!
 *
 * @author Josh
 */
public class DBConfiguration {

    protected static final String MARIA_DB_DRIVER_NAME = "org.mariadb.jdbc.Driver";
    protected static final String DB_URL = "jdbc:mariadb://localhost:3306";
    protected static final String DB_USER = "root";
    protected static final String DB_PASSWORD = "qwerty";
    protected static final String DB_BOOKS = "/books";

    protected static final String DB_BOOKS_TITLES_TABLENAME = "titles";
    protected static final String DB_BOOKS_TITLES_ISBN = "isbn";
    protected static final String DB_BOOKS_TITLES_TITLE = "title";
    protected static final String DB_BOOKS_TITLES_EDITION_NUMBER = "editionNumber";
    protected static final String DB_BOOKS_TITLES_COPYRIGHT = "copyright";

    protected static final String DB_BOOKS_AUTHORS_TABLENAME = "authors";
    protected static final String DB_BOOKS_AUTHORS_AUTHORID = "authorID";
    protected static final String DB_BOOKS_AUTHORS_FIRSTNAME = "firstName";
    protected static final String DB_BOOKS_AUTHORS_LASTNAME = "lastName";

    protected static final String DB_BOOKS_AUTHORISBN_TABLENAME = "authorisbn";

    /**
     * Register a driver using Java's Class.forName() method
     * to dynamically load the driver's class file into memory
     * which automatically registers it.
     */
    public static void doClassForNameRegistration(){
        try {
            Class.forName(DBConfiguration.MARIA_DB_DRIVER_NAME);
            System.out.println("DB Driver Registration using Class.forName() worked! (Option 1)");
        }
        catch(ClassNotFoundException ex) {
            System.err.println("ClassNotFoundException: unable to load MariaDB driver class!");
            System.err.println("Driver name: " + DBConfiguration.MARIA_DB_DRIVER_NAME);
            System.exit(1);
        }
    }

    /**
     * Register the driver using registerDriver() method. This is the 2nd and less preferred option
     * noted in the Unit 2 notes.
     */
    public static void doRegisterDriverMethodRegistration(){
        try {
            Driver myDriver = new org.mariadb.jdbc.Driver();
            DriverManager.registerDriver( myDriver );
            System.out.println("DB Driver Registration using registerDriver() worked! (Option 2)");
        }
        catch (SQLException e) {
            System.err.println("SQL Exception: trying to register MariaDB Driver");
            e.printStackTrace();
        }
    }

    public static Connection getBookDBConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DBConfiguration.DB_URL + DBConfiguration.DB_BOOKS, DBConfiguration.DB_USER, DBConfiguration.DB_PASSWORD);
        } catch (SQLException e) {
            System.out.println("SQL EXCEPTION");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("EXCEPTION");
            e.printStackTrace();
        }
        return connection;
    }

}