import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;
    private List<Quote> favoriteQuotes;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.favoriteQuotes = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void addFavoriteQuote(Quote quote) {
        favoriteQuotes.add(quote);
    }

    public List<Quote> getFavoriteQuotes() {
        return favoriteQuotes;
    }
}
