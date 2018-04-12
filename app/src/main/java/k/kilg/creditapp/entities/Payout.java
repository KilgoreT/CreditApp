package k.kilg.creditapp.entities;

import android.support.annotation.NonNull;

import java.util.Date;

/**
 * Created by apomazkin on 10.04.2018.
 * k.kilg.creditapp.entities
 * 10.04.2018
 * 11:52
 */

// 1. дата и сумма очередного платежа.
//     По дате очередного платежа: если в месяце нет такого дня, в который выдан кредит, то выводить последний день месяца (например кредит выдан 31.01.2017, значит в апреле дата будет 30.04)
// 2. наименование (которое пользователь задает при добавлении/модификации кредита)
// 3. остаток основного долга (считаем, что пользователь всегда платит по графику без просрочек и досрочных гашений)
// 4. Список должен быть отсортирован по дате очередного платежа: чем раньше, тем выше.
//    Если несколько кредитов имеют одну и ту же дату гашения в текущем месяце, то сортировать по алфавиту по наименованию

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

    /* @Override
    public int compareTo(@NonNull Payout payout) {
        //return payout.getDate().compareTo(getDate());
        return this.getDate().compareTo(payout.getDate());
    }*/
}
