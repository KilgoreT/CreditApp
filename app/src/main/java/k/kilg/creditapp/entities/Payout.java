package k.kilg.creditapp.entities;

/**
 * Created by apomazkin on 10.04.2018.
 * k.kilg.creditapp.entities
 * 10.04.2018
 * 11:52
 */
public class Payout {
    private String creditName;
    private String amount;
    private String date;

    public String getCreditName() {
        return creditName;
    }

    public String getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public void setCreditName(String creditName) {
        this.creditName = creditName;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
