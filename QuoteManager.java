import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuoteManager {
    private List<Quote> quotes;

    public QuoteManager() {
        quotes = new ArrayList<>();
        quotes.add(new Quote("The only way to do great work is to love what you do.", "Steve Jobs"));
        quotes.add(new Quote("In the middle of every difficulty lies opportunity.", "Albert Einstein"));
        quotes.add(new Quote("Success is not final, failure is not fatal: It is the courage to continue that counts.", "Winston Churchill"));
    }

    public Quote getRandomQuote() {
        Random rand = new Random();
        return quotes.get(rand.nextInt(quotes.size()));
    }

    public List<Quote> getAllQuotes() {
        return quotes;
    }
}
