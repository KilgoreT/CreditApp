package k.kilg.creditapp.entities;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
    private static final int MIN_RATE = 1;
    private static final int MAX_RATE = 99;

    private String name; //наименование (обязательно, макс.длина 64 символа)
    private boolean annuity; //тип кредита (обязательно, допустимые значения: с аннуитетными платежами, с дифференциированноми платежами)
    private String date; //дату выдачи (обязательно, дата не должна быть больше текущей, и недолжна быть меньше 2000 года)
    private Integer monthCount = 0; //срок в месяцах
    private Integer amount = 0; //сумму в рублях (обязательно, минимум 1 рубль, максимум 1 000 000 000)
    private Float rate = 0f; //процентную ставку (обязательно, минимум 1%, максимум 99%). Ставка может быть дробной (например 13.5%)

    private String key;


    public Credit() {
    }

    public Credit(String name, boolean annuity, int monthCount, int amount, float rate) throws IllegalArgumentException{
            this.setName(name);
            this.setAnnuity(annuity);
            this.setMonthCount(monthCount);
            this.setAmount(amount);
            this.setRate(rate);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws IllegalArgumentException {
        if (name.length() >= MIN_LENGTH_NAME && name.length() <= MAX_LENGTH_NAME) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("Wrong credit name(max length: 64)!");
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
   /* public void setDate(String date) throws IllegalArgumentException {
        Log.d("###", ">>" + getClass().getSimpleName() + ": String date = " + date);
        try {
            this.date = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(date);
            Log.d("###", ">>" + getClass().getSimpleName() + ": Date date = " + date);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Not correct date format");
        }
    }*/

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
            throw new IllegalArgumentException("Wrong credit amount(Range: 1 - 1 000 000 000)!");
        }
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) throws IllegalArgumentException {
        if (rate >= MIN_RATE && rate <= MAX_RATE) {
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
}
