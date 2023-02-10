import java.util.LinkedList;
import java.util.List;

public class Book {

    // VARIABLES

    private String isbn;
    private String title;
    private int editionNumber;
    private String copyright;

    private List<Author> authorList;

    // CONSTRUCTORS

    public Book() {
        this.isbn = "";
        this.title = "";
        this.editionNumber = -1;
        this.copyright = "";
        authorList = new LinkedList<>();
    }

    public Book(String isbn, String title, int editionNumber, String copyright) {
        this.isbn = isbn;
        this.title = title;
        this.editionNumber = editionNumber;
        this.copyright = copyright;
        authorList = new LinkedList<>();
    }

    // SETTERS

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setEditionNumber(int editionNumber) {
        this.editionNumber = editionNumber;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public void setAuthorList(List<Author> authorList) {
        this.authorList = authorList;
    }

    // GETTERS

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public int getEditionNumber() {
        return editionNumber;
    }

    public String getCopyright() {
        return copyright;
    }

    public List<Author> getAuthorList() {
        return authorList;
    }

    // METHODS

    public void addAuthor(Author author) {
        this.authorList.add(author);
    }

}
