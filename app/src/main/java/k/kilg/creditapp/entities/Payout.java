package k.kilg.creditapp.entities;

import android.support.annotation.NonNull;

import java.util.Date;

/**
 * Created by apomazkin on 10.04.2018.
 * k.kilg.creditapp.entities
 * 10.04.2018
 * 11:52
 */


public class Payout{
    private String mCreditName;
    private String mAmount;
    private Date date;
    private String mBalance;

    public String getCreditName() {
        return mCreditName;
    }

    public void setCreditName(String creditName) {
        this.mCreditName = creditName;
    }

    public String getAmount() {
        return mAmount;
    }

    public void setMonthPayout(String amount) {
        this.mAmount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getBalance() {
        return mBalance;
    }

    public void setBalance(String balance) {
        this.mBalance = balance;
    }

}
