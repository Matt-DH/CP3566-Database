import java.util.LinkedList;
import java.util.List;

/**
Library class to keep a list of all the books, and a list of all the authors in the database.
 **/

public class Library {

    // VARIABLES

    private List<Book> bookList;
    private List<Author> authorList;

    // CONSTRUCTORS

    public Library() {
        this.bookList = new LinkedList<>();
        this.authorList = new LinkedList<>();
    }

    // SETTERS

    public void setBookList(List<Book> books) {
        this.bookList = books;
    }

    public void setAuthorList(List<Author> authors) {
        this.authorList = authors;
    }

    // GETTERS

    public List<Book> getBookList() {
        return this.bookList;
    }

    public List<Author> getAuthorList() {
        return this.authorList;
    }

}
