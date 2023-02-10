import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AddAuthorTest {

    public static void main(String[] args) throws SQLException {

        Connection connection = DriverManager.getConnection(DBConfiguration.DB_URL + DBConfiguration.DB_BOOKS, DBConfiguration.DB_USER, DBConfiguration.DB_PASSWORD);
        BookDatabaseManager.addAuthor(connection, 111, "Hubert", "Humphrey");

    }

}
