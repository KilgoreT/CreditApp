package k.kilg.creditapp.tools;

import android.util.Log;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import k.kilg.creditapp.entities.Credit;
import k.kilg.creditapp.entities.Payout;

/**
 * Created by apomazkin on 10.04.2018.
 * k.kilg.creditapp.tools
 * 10.04.2018
 * 14:33
 */
public class CreditTools {

    private static final BigDecimal MONTHS_IN_YEAR = new BigDecimal(12);
    private static final BigDecimal HUNDRED = new BigDecimal(100);
    private static final int SCALE_FOR_CURRENCY = 2;



    //**********Annuity calculation**********//

    public static List<Payout> getAnnuityMonthPayouts(Credit credit) {
        List<Payout> payoutList = new ArrayList<>();
        for (int i = 0; i < credit.getMonthCount(); i++) {
            Calendar calendar = getCalendar(credit);
            calendar.add(Calendar.MONTH, i + 1);
            Payout payout = new Payout();
            payout.setCreditName(credit.getName());
            payout.setMonthPayout(String.valueOf(getAnnuityMonthPayoutAmount(credit)));
            payout.setDate(calendar.getTime());
            payout.setBalance(String.valueOf(getAnnuityCreditBalance(credit, i)));
            payoutList.add(payout);
        }
        return  payoutList;
    }


    private static BigDecimal getAnnuityCreditBalance(Credit credit, Integer paidMonths) {
        return getFullCreditPayment(credit).subtract(getAnnuityMonthPayoutAmount(credit).multiply(new BigDecimal(paidMonths)));
    }

    private static BigDecimal getAnnuityCoeffitient(Credit credit) {
        return getAnnuityFirstCoeffitient(credit).divide(getAnnuitySecondCoeffitient(credit), RoundingMode.HALF_UP);
    }

    private static BigDecimal getAnnuityFirstCoeffitient(Credit credit) {
        return getMonthPercent(credit)
                .multiply((BigDecimal.ONE.add(getMonthPercent(credit)))
                        .pow(credit.getMonthCount()));
    }

    private static BigDecimal getAnnuitySecondCoeffitient(Credit credit) {
        return ((BigDecimal.ONE.add(getMonthPercent(credit))).pow(credit.getMonthCount())).subtract(BigDecimal.ONE);
    }

    private static BigDecimal getMonthPercent(Credit credit) {
        return (getCreditPercent(credit)
                .divide(MONTHS_IN_YEAR, MathContext.DECIMAL128))
                .divide(HUNDRED, MathContext.DECIMAL128);
    }

    private static BigDecimal getAnnuityMonthPayoutAmount(Credit credit) {
        return getAnnuityCoeffitient(credit)
                .multiply(new BigDecimal(credit.getAmount()))
                .setScale(SCALE_FOR_CURRENCY, RoundingMode.HALF_UP);
    }




    //**********Differential calculation**********//

    public static List<Payout> getDifferentialMonthPayouts(Credit credit) {

        List<Payout> payoutList = new ArrayList<>();
        Calendar calendar = getCalendar(credit);
        for (int i = 0; i < credit.getMonthCount(); i++) {
            calendar.add(Calendar.MONTH, i+1);
            Payout payout = new Payout();
            payout.setCreditName(credit.getName());
            payout.setMonthPayout(String.valueOf(getDifferentialFullMonthPayout(credit, i+1)));
            payout.setDate(calendar.getTime());
            payout.setBalance(String.valueOf(getDifferentialCreditBalance(credit, i+1)));
            payoutList.add(payout);
            calendar = getCalendar(credit);
        }
        return payoutList;
    }

    private static BigDecimal getDifferentialFullMonthPayout(Credit credit, Integer paidMonths) {
        return getDifferentialStaticMonthPayout(credit).add(getDifferentialPercentMonthPayout(credit, paidMonths));
    }

    private static BigDecimal getDifferentialPercentMonthPayout(Credit credit, Integer paidMonths) {

        Calendar calendar = getCalendar(credit);
        calendar.add(Calendar.MONTH, paidMonths);

        return getDifferentialCreditBalance(credit, paidMonths)
                .multiply(getCreditPercent(credit).
                        divide(HUNDRED, MathContext.DECIMAL128)
                        .multiply(new BigDecimal(calendar.getActualMaximum(Calendar.DAY_OF_MONTH))))
                .divide(new BigDecimal(calendar.getActualMaximum(Calendar.DAY_OF_YEAR)), SCALE_FOR_CURRENCY, RoundingMode.HALF_UP);
    }

    private static BigDecimal getDifferentialCreditBalance(Credit credit, Integer paidMonths) {
        return getCreditAmount(credit).subtract(getDifferentialStaticMonthPayout(credit).multiply(new BigDecimal(paidMonths)));
    }

    private static BigDecimal getDifferentialStaticMonthPayout(Credit credit) {
        return getCreditAmount(credit).divide(getCreditMonthCount(credit), SCALE_FOR_CURRENCY, RoundingMode.HALF_UP);
    }



    private static BigDecimal getFullCreditPayment(Credit credit) {
        if (credit.isAnnuity()) {
            return getAnnuityMonthPayoutAmount(credit).multiply(getCreditMonthCount(credit));
        } else {
            BigDecimal overpaymentDifferentialCredit = new BigDecimal(0);
            for (int i = 0; i < credit.getMonthCount(); i++) {
                overpaymentDifferentialCredit = overpaymentDifferentialCredit.add(getDifferentialStaticMonthPayout(credit).add(getDifferentialPercentMonthPayout(credit, i)));
            }
            return overpaymentDifferentialCredit;
        }
    }

    public static BigDecimal getAllPaymentAmount(List<Payout> payoutList) {
        BigDecimal allPayoutAmount = new BigDecimal(0);
        for (Payout payout: payoutList) {
            allPayoutAmount = allPayoutAmount.add(new BigDecimal(payout.getAmount()));
        }
        return allPayoutAmount;
    }
    public static BigDecimal getAllCreditAmount(List<Credit> creditList) {
        BigDecimal allCreditAmount = new BigDecimal(0);
        for (Credit credit : creditList) {
            allCreditAmount = allCreditAmount.add(new BigDecimal(credit.getAmount()));
        }
        return allCreditAmount;
    }

    //**********Credit fields as BigDecimal**********//

    private static BigDecimal getCreditMonthCount(Credit credit) {
        return new BigDecimal(credit.getMonthCount());
    }

    private static BigDecimal getCreditAmount(Credit credit) {
        return new BigDecimal(credit.getAmount());
    }

    private static BigDecimal getCreditPercent(Credit credit) {
        return new BigDecimal(credit.getRate());
    }


    //**********Calendar for credit date**********//

    private static Calendar getCalendar(Credit credit) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Calendar calendar = Calendar.getInstance(Locale.US);
        try {
            calendar.setTime(sdf.parse(credit.getDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }

    public static String getMonthName(int month) {
        switch (month + 1) {
            case 1:
                return "Январь ";

            case 2:
                return "Февраль ";

            case 3:
                return "Март ";

            case 4:
                return "Апрель ";

            case 5:
                return "Май ";

            case 6:
                return "Июнь ";

            case 7:
                return "Июль ";

            case 8:
                return "Август ";

            case 9:
                return "Сентябрь ";

            case 10:
                return "Октябрь ";

            case 11:
                return "Ноябрь ";

            case 12:
                return "Декабрь ";
        }

        return "";
    }
}
