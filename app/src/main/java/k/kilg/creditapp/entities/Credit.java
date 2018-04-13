package k.kilg.creditapp.entities;

import android.util.Log;

import com.google.firebase.database.Exclude;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import k.kilg.creditapp.R;

/**
 * Created by apomazkin on 04.04.2018.
 * k.kilg.creditapp.entities
 * 04.04.2018
 * 14:28
 */
public class Credit {

    private static final int MIN_LENGTH_NAME = 1;
    private static final int MAX_LENGTH_NAME = 64;
    private static final int MIN_AMOUNT = 1;
    private static final int MAX_AMOUNT = 1_000_000_000;
    private static final BigDecimal MIN_RATE = new BigDecimal(1);
    private static final BigDecimal MAX_RATE = new BigDecimal(99);

    private String name; //наименование (обязательно, макс.длина 64 символа)
    private boolean annuity; //тип кредита (обязательно, допустимые значения: с аннуитетными платежами, с дифференциированноми платежами)
    private String date; //дату выдачи (обязательно, дата не должна быть больше текущей, и недолжна быть меньше 2000 года)
    private Integer monthCount = 0; //срок в месяцах
    private Integer amount = 0; //сумму в рублях (обязательно, минимум 1 рубль, максимум 1 000 000 000)
    private String rate; //процентную ставку (обязательно, минимум 1%, максимум 99%). Ставка может быть дробной (например 13.5%)

    private String key;


    public Credit() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws IllegalArgumentException {
        if (name.length() >= MIN_LENGTH_NAME && name.length() <= MAX_LENGTH_NAME) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("Name too long (max length: 64)!");
        }
    }

    public boolean isAnnuity() {
        return annuity;
    }

    public void setAnnuity(boolean annuity) {
        this.annuity = annuity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getMonthCount() {
        return monthCount;
    }

    public void setMonthCount(int monthCount) {
        this.monthCount = monthCount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) throws IllegalArgumentException {
        if (amount >= MIN_AMOUNT && amount <= MAX_AMOUNT) {
            this.amount = amount;
        } else {
            throw new IllegalArgumentException("Amount range: 1 - 1 000 000 000!");
        }
    }


    public String getRate() {
        return rate;
    }

    public void setRate(String rate) throws IllegalArgumentException {
        BigDecimal r = new BigDecimal(rate);
        if (r.compareTo(MIN_RATE) > 0 && r.compareTo(MAX_RATE) < 0) {
            this.rate = rate;
        } else {
            throw new IllegalArgumentException("Wrong credit rate(Range: 1 - 99%)!");
        }
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Credit credit = (Credit) o;

        return key.equals(credit.key);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("annuity", annuity);
        result.put("date", date);
        result.put("monthCount", monthCount);
        result.put("amount", amount);
        result.put("rate", rate);
        return result;
    }

}
