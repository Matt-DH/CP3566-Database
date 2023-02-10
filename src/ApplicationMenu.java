import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Simple console Menu Application to use in CP2280
 *
 * @author Josh
 */
public class ApplicationMenu {

    public static void main(String[] args) {
        System.out.println("Doing something fun on a Console!");

        //TODO use this as a guide for the menu in Book Application

        Scanner input = new Scanner(System.in);
        char c; //Char to drive menu choice

        do{
            printMenu();
            //Grab the user input
            c = Character.toUpperCase(input.next().charAt(0));

            System.out.printf("\nEcho: %c", c);

            switch (c) {
                case 'A':
                    System.out.printf("\nIn Option %c", c);
                    try {
                        Connection connection = DriverManager.getConnection(DBConfiguration.DB_URL + DBConfiguration.DB_BOOKS, DBConfiguration.DB_USER, DBConfiguration.DB_PASSWORD);
                        List<Book> bookList = BookDatabaseManager.loadBookList(connection);
                        for (Book book : bookList) {
                            LinkedList<String> authorsStrList = BookDatabaseManager.getAuthors(connection, book.getIsbn());
                            System.out.printf("\n%s, %s, %d, %S",
                                    book.getIsbn(), book.getTitle(), book.getEditionNumber(), book.getCopyright());
                            System.out.println();
                            for (String authorStr : authorsStrList) {
                                System.out.println(authorStr);
                            }
                        }
                    } catch (SQLException e) {
                        System.out.println("SQL ERROR");
                        e.printStackTrace();
                    }
                    break;
                case 'B':
                    System.out.printf("\nIn Option %c", c);
                    try {
                        Connection connection = DriverManager.getConnection(DBConfiguration.DB_URL + DBConfiguration.DB_BOOKS, DBConfiguration.DB_USER, DBConfiguration.DB_PASSWORD);
                        List<Author> authorList = BookDatabaseManager.loadAuthorList(connection);
                        for (Author author : authorList) {
                            String bookDetails = BookDatabaseManager.getBooks(connection, author.getAuthorID());
                            System.out.printf("\n%d, %s, %s,\n%s",
                                    author.getAuthorID(), author.getFirstName(), author.getLastName(), bookDetails);
                        }
                    }
                    catch (SQLException e) {
                        System.out.println("SQL ERROR");
                        e.printStackTrace();
                    }
                    break;
                case 'C':
                    try {
                        Connection connection = DriverManager.getConnection(DBConfiguration.DB_URL + DBConfiguration.DB_BOOKS, DBConfiguration.DB_USER, DBConfiguration.DB_PASSWORD);
                        Scanner sc = new Scanner(System.in);
                        System.out.println("Enter the ID of the author you'd like to add a book for:");
                        int authorIDInput = sc.nextInt();
                        Object[] returnArray = BookDatabaseManager.authorSearch(authorIDInput);
                        boolean isMatch = (boolean) returnArray[0];
                        String authorFirstName = (String) returnArray[1];
                        String authorLastName = (String) returnArray[2];
                        if (isMatch) {
                            sc.nextLine();
                            System.out.println("MATCH FOUND: " + authorFirstName + " " + authorLastName);
                            System.out.println("Enter book details:");
                            System.out.println("ISBN:");
                            String newBookIsbn = sc.nextLine();
                            // TODO: Check if book with that ISBN already exists
                            // If so:
                            // TODO: Add code for adding another author to an existing book
                            // If not:
                            System.out.println("Title:");
                            String newBookTitle = sc.nextLine();
                            System.out.println("Edition number");
                            String newBookEditionStr = sc.nextLine();
                            System.out.println("Copyright");
                            String newBookCopyright = sc.nextLine();
                            BookDatabaseManager.addBook(connection, authorIDInput, newBookIsbn, newBookTitle, Integer.parseInt(newBookEditionStr), newBookCopyright);
                            System.out.println("Book successfully added to database");
                        } else {
                            System.out.println("Author not found");
                        }
                        break;
                    } catch (SQLException e) {
                        System.out.println("SQL ERROR");
                        e.printStackTrace();
                    }
                case 'D':
                    try {
                        System.out.printf("\nIn Option %c", c);
                        System.out.println("\nEnter the values for adding a author");

                        Scanner myObj6 = new Scanner(System.in);
                        System.out.println("Enter First Name");
                        String firstNameVar = myObj6.nextLine();

                        Scanner myObj7 = new Scanner(System.in);  // Create a Scanner object
                        System.out.println("Enter Last Name");
                        String lastNameVar = myObj7.nextLine();

                        Connection connection = DriverManager.getConnection(DBConfiguration.DB_URL + DBConfiguration.DB_BOOKS, DBConfiguration.DB_USER, DBConfiguration.DB_PASSWORD);
                        Statement statement = connection.createStatement();
                        String SQL = "INSERT INTO authors VALUES ( authorID, ?, ?)";
                        PreparedStatement pst = connection.prepareStatement(SQL);
                        pst.setString(1, firstNameVar);
                        pst.setString(2, lastNameVar);
                        pst.executeUpdate();
                    } catch (SQLException e) {
                        System.out.println("SQL ERROR");
                        e.printStackTrace();
                    }
                    break;
                case 'Q':
                    break;
                default:
                    System.out.println("try again bozo");
                }
            }
        while(c!='Q');
        System.out.println("\nGOODBYE!");
        }

    public static void printMenu(){
        System.out.println("\n\nMake a choice");
        System.out.println("(A) Option A - Print all the books in the database");
        System.out.println("(B) Option B - Print all authors in the database");
        System.out.println("(C) Option C - Add a book to the database for an existing author");
        System.out.println("(C) Option D - Add a new author to the database");
        System.out.println("(Q) Quit");
    }
}