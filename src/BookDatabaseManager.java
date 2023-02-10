import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * This class contains all the Database queries and all the methods to retrieve what you need
 *
 * THIS IS THE MOST IMPORTANT PART OF THE ASSIGNMENT
 *
 * @author Josh
 */
public class BookDatabaseManager {

    // Loading the library class with the book and author lists

    public static Library loadLibrary() {
        Connection connection = DBConfiguration.getBookDBConnection();
        Library library = new Library();
        library.setBookList(loadBookList(connection));
        library.setAuthorList(loadAuthorList(connection));

        buildRelationships(connection, library.getBookList(), library.getAuthorList());

        return library;
    }



    public static List<Book> loadBookList(Connection connection){

        LinkedList<Book> bookLinkedList = new LinkedList<>();

        try {
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM " + DBConfiguration.DB_BOOKS_TITLES_TABLENAME + ";";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                bookLinkedList.add(new Book(
                        resultSet.getString(DBConfiguration.DB_BOOKS_TITLES_ISBN),
                        resultSet.getString(DBConfiguration.DB_BOOKS_TITLES_TITLE),
                        resultSet.getInt(DBConfiguration.DB_BOOKS_TITLES_EDITION_NUMBER),
                        resultSet.getString(DBConfiguration.DB_BOOKS_TITLES_COPYRIGHT)
                ));
            }
            statement.close();
        } catch (SQLException e) {
            System.out.println("SQL EXCEPTION");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Exception");
            e.printStackTrace();
        }
        return bookLinkedList;
    }

    public static List<Author> loadAuthorList(Connection connection) {

        LinkedList<Author> authorLinkedList = new LinkedList<>();
        try{
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM " + DBConfiguration.DB_BOOKS_AUTHORS_TABLENAME + ";";
            ResultSet resultSet = statement.executeQuery(sql);


            while(resultSet.next()){

                authorLinkedList.add(new Author(
                        resultSet.getInt(DBConfiguration.DB_BOOKS_AUTHORS_AUTHORID),
                        resultSet.getString(DBConfiguration.DB_BOOKS_AUTHORS_FIRSTNAME),
                        resultSet.getString(DBConfiguration.DB_BOOKS_AUTHORS_LASTNAME)
                ));
            }
            statement.close();
        }
        catch (SQLException sqlException){
            sqlException.printStackTrace();;
        }
        return authorLinkedList;
    }

    private static void buildRelationships(Connection connection, List<Book> bookList, List<Author> authorList) {
        try {
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM " + DBConfiguration.DB_BOOKS_AUTHORISBN_TABLENAME + ";";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                int authorID = resultSet.getInt(DBConfiguration.DB_BOOKS_AUTHORS_AUTHORID);
                String isbn = resultSet.getString(DBConfiguration.DB_BOOKS_TITLES_ISBN);
                Author author = null;
                Book book = null;

                for (Author a : authorList) {
                    if (a.getAuthorID() == authorID) {
                        author = a;
                    }
                }

                for (Book b : bookList) {
                    if (b.getIsbn().equals(isbn)) {
                        book = b;
                    }
                }

                author.addBook(book);
                book.addAuthor(author);

            }
        } catch (SQLException e) {
            System.out.println("SQLEXCEPTION");
            e.printStackTrace();
        }
    }

    /**
     * Returns an array that contains information about a record from the authors table that corresponds with the given authorID
     * @author mattdh
     * @param authorID
     * @return
     */
    public static Object[] authorSearch(int authorID) {
        boolean returnValue = false;
        String authorFirstName = null;
        String authorLastName = null;
        for (Author a : loadLibrary().getAuthorList()) {
            if (authorID == (a.getAuthorID())) {
                returnValue = true;
                authorFirstName = a.getFirstName();
                authorLastName = a.getLastName();
            }
        }
        Object[] returnArray = new Object[] {returnValue, authorFirstName, authorLastName};
        return returnArray;
    }

    // retrieves the authorid, firstname, and lastname records for an author from the bridge table
    // given an isbn

    /**
     * Returns a list of Strings, each of which containing information about a record from the author table, for each record that corresponds with the given authorID value.
     * The authorID values are retrieved from the authorisbn table for each record that corresponds with the given isbn value.
     * @author mattdh
     * @param connection
     * @param isbn
     * @return
     */
    public static LinkedList<String> getAuthors(Connection connection, String isbn) {
        LinkedList<String> returnList = new LinkedList<>();
        LinkedList<Integer> authorIds = new LinkedList<Integer>();
        int authorId = 0;
        String fName = "";
        String lName = "";
        try {
            String sql1 =
                    "SELECT " + DBConfiguration.DB_BOOKS_AUTHORS_AUTHORID +
                    " FROM " + DBConfiguration.DB_BOOKS_AUTHORISBN_TABLENAME +
                    " WHERE isbn = ?;";
            PreparedStatement pst1 = connection.prepareStatement(sql1);
            pst1.setString(1, isbn);
            ResultSet resultSet1 = pst1.executeQuery();
            while (resultSet1.next()) {
                authorIds.add(resultSet1.getInt(DBConfiguration.DB_BOOKS_AUTHORS_AUTHORID));
            }
            for (int id : authorIds) {
                String sql2 =
                        "SELECT " + DBConfiguration.DB_BOOKS_AUTHORS_FIRSTNAME +
                        ", " + DBConfiguration.DB_BOOKS_AUTHORS_LASTNAME +
                        ", " + DBConfiguration.DB_BOOKS_AUTHORS_AUTHORID +
                        " FROM " + DBConfiguration.DB_BOOKS_AUTHORS_TABLENAME +
                        " WHERE " + DBConfiguration.DB_BOOKS_AUTHORS_AUTHORID +
                        " = ?;";
                PreparedStatement pst2 = connection.prepareStatement(sql2);
                pst2.setInt(1, id);
                ResultSet resultSet2 = pst2.executeQuery();
                while (resultSet2.next()) {
                    authorId = resultSet2.getInt(DBConfiguration.DB_BOOKS_AUTHORS_AUTHORID);
                    fName = resultSet2.getString(DBConfiguration.DB_BOOKS_AUTHORS_FIRSTNAME);
                    lName = resultSet2.getString(DBConfiguration.DB_BOOKS_AUTHORS_LASTNAME);
                    returnList.add(authorId + ", " + fName + " " + lName);
                }
            }
        } catch (SQLException e) {
            System.out.println("SQLEXCEPTION");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("EXCEPTION");
            e.printStackTrace();
        }
        return returnList;
    }

    /**
     * Returns a string containing information about all records from the book table that correspond with the given authorID
     * @author mattdh
     * @param connection
     * @param authorID
     * @return
     */
    public static String getBooks(Connection connection, int authorID) {
        String isbn = "";
        String title = "";
        int editionNum = 0;
        String copyright = "";
        String returnString = "";
        try {
            String sql1 =
                    "SELECT " + DBConfiguration.DB_BOOKS_TITLES_ISBN +
                    " FROM " + DBConfiguration.DB_BOOKS_AUTHORISBN_TABLENAME +
                    " WHERE " + DBConfiguration.DB_BOOKS_AUTHORS_AUTHORID +
                    "= ?;";
            PreparedStatement pst1 = connection.prepareStatement(sql1);
            pst1.setInt(1, authorID);
            ResultSet resultSet1 = pst1.executeQuery();
            LinkedList<String> isbnList = new LinkedList<>();
            while (resultSet1.next()) {
                isbnList.add(resultSet1.getString(DBConfiguration.DB_BOOKS_TITLES_ISBN));
            }
            for (String isbnElement : isbnList) {
                String sql2 =
                        "SELECT " + DBConfiguration.DB_BOOKS_TITLES_TITLE +
                        ", " + DBConfiguration.DB_BOOKS_TITLES_EDITION_NUMBER +
                        ", " + DBConfiguration.DB_BOOKS_TITLES_COPYRIGHT +
                        " FROM " + DBConfiguration.DB_BOOKS_TITLES_TABLENAME +
                        " WHERE " + DBConfiguration.DB_BOOKS_TITLES_ISBN +
                        "=" + "\"" + isbnElement + "\"" + ";";
                Statement statement = connection.createStatement();
                ResultSet resultSet2 = statement.executeQuery(sql2);
                while (resultSet2.next()) {
                    title = resultSet2.getString(DBConfiguration.DB_BOOKS_TITLES_TITLE);
                    editionNum = resultSet2.getInt(DBConfiguration.DB_BOOKS_TITLES_EDITION_NUMBER);
                    copyright = resultSet2.getString(DBConfiguration.DB_BOOKS_TITLES_COPYRIGHT);
                }
                returnString += isbnElement + ", " + title + ", Edition " + editionNum + ", Copyright " + copyright + "\n";
            }

        } catch (SQLException e) {
            System.out.println("SQLEXCEPTION");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("EXCEPTION");
            e.printStackTrace();
        }
        return  returnString;
    }

    /**
     * Adds a record to the author table containing values for authorID, firstName, and lastName fields
     * @author mattdh
     * @param connection
     * @param authorID
     * @param firstName
     * @param lastName
     */
    public static void addAuthor(Connection connection, int authorID, String firstName, String lastName) {
        try {
            String sql =
                    "INSERT INTO " + DBConfiguration.DB_BOOKS_AUTHORS_TABLENAME +
                    " VALUES (?, ?, ?);";
            PreparedStatement pst1 = connection.prepareStatement(sql);
            pst1.setInt(1, authorID);
            pst1.setString(2, firstName);
            pst1.setString(3, lastName);
            pst1.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQLEXCEPTION");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("EXCEPTION");
            e.printStackTrace();
        }
    }

    // Adds a record to the titles table, with the isbn, title, edition number, and copyright fields,
    // and also adds a record to the bridge table with authorID and isbn fields

    /**
     * Adds a record to the titles table containing values for isbn, title, edition number, and copyright fields.
     * Adds a record to the authorisbn table containing values for isbn, and authorID fields.
     * @author mattdh
     * @param connection
     * @param authorID
     * @param newBookIsbn
     * @param newBookTitle
     * @param newBookEditionNum
     * @param newBookCopyright
     */
    public static void addBook(Connection connection, int authorID, String newBookIsbn, String newBookTitle, int newBookEditionNum, String newBookCopyright) {
        try {
            String sql1 =
                    "INSERT INTO " + DBConfiguration.DB_BOOKS_TITLES_TABLENAME +
                    " VALUES (?, ?, ?, ?);";
            PreparedStatement pst1  = connection.prepareStatement(sql1);
            pst1.setString(1, newBookIsbn);
            pst1.setString(2, newBookTitle);
            pst1.setInt(3, newBookEditionNum);
            pst1.setString(4, newBookCopyright);
            String sql2 =
                    "INSERT INTO " + DBConfiguration.DB_BOOKS_AUTHORISBN_TABLENAME +
                    " VALUES (?, ?);";
            PreparedStatement pst2 = connection.prepareStatement(sql2);
            pst2.setInt(1, authorID);
            pst2.setString(2, newBookIsbn);
            pst1.executeQuery();
            pst2.executeQuery();
        } catch (SQLException e) {
            System.out.println("SQLEXCEPTION");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("EXCEPTION");
            e.printStackTrace();
        }
    }
}