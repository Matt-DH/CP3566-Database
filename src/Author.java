import java.util.LinkedList;
import java.util.List;

public class Author {

    // VARIABLES

    private int authorID;
    private String firstName;
    private String lastName;
    private List<Book> bookList;

    // CONSTRUCTORS

    public Author() {
        this.authorID = -1;
        this.firstName = "";
        this.lastName = "";
        this.bookList = new LinkedList<>();
    }

    public Author(int authorID, String firstName, String lastName) {
        this.authorID = authorID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.bookList = new LinkedList<>();
    }

    // SETTERS

    public void setAuthorID(int authorID) {
        this.authorID = authorID;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }

    // GETTERS


    public int getAuthorID() {
        return authorID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<Book> getBookList() {
        return bookList;
    }

    // METHODS

    public void addBook(Book book) {
        this.bookList.add(book);
    }

}
