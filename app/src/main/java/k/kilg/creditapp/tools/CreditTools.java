package k.kilg.creditapp.tools;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

    //todo: В графике платежей должны перечисляться все месяцы, начиная со СЛЕДУЮЩЕГО после
    //выдачи кредита.

    private static final int MONTHS_IN_YEAR = 12;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    public static List<Payout> getAnnuityMonthPayouts(Credit credit) {

        BigDecimal persent = new BigDecimal(credit.getRate());
        BigDecimal amount = new BigDecimal(credit.getAmount());
        Integer monthCount = credit.getMonthCount();

        List<Payout> payoutList = new ArrayList<>();

        BigDecimal monthPersent = (persent.divide(new BigDecimal(MONTHS_IN_YEAR), MathContext.DECIMAL128)).divide(new BigDecimal(100), MathContext.DECIMAL128);

        BigDecimal annuityCoefficientFirst = monthPersent
                .multiply((BigDecimal.ONE.add(monthPersent))
                        .pow(monthCount));

        BigDecimal annuityCoefficientSecond = ((BigDecimal.ONE.add(monthPersent)).pow(monthCount)).subtract(BigDecimal.ONE);
        BigDecimal annuityCoefficient = annuityCoefficientFirst.divide(annuityCoefficientSecond, RoundingMode.HALF_UP);
        BigDecimal result = annuityCoefficient.multiply(amount).setScale(2, RoundingMode.HALF_UP);


        Calendar calendar = Calendar.getInstance(Locale.US);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        try {
            calendar.setTime(sdf.parse(credit.getDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < credit.getMonthCount(); i++) {
            Payout payout = new Payout();
            try {
                calendar.setTime(sdf.parse(credit.getDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            BigDecimal balance = amount.subtract(result.multiply(new BigDecimal(i)));
            calendar.add(Calendar.MONTH, i);
            payout.setCreditName(credit.getName());
            payout.setAmount(String.valueOf(result));
            payout.setDate(calendar.getTime());
            payout.setBalance(String.valueOf(balance));
            payoutList.add(payout);
        }

        return  payoutList;
    }

    public static List<Payout> getDifferentialMonthPayouts(Credit credit) {

        BigDecimal persent = new BigDecimal(credit.getRate());
        BigDecimal amount = new BigDecimal(credit.getAmount());
        Integer monthCount = credit.getMonthCount();
        List<Payout> payoutList = new ArrayList<>();
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar calendar = Calendar.getInstance(Locale.US);
        try {
            calendar.setTime(sdf.parse(credit.getDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        BigDecimal staticMonthAmount = amount.divide(new BigDecimal(monthCount), 2, RoundingMode.HALF_UP);

        for (int i = 0; i < monthCount; i++) {

            calendar.add(Calendar.MONTH, i);

            int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            int daysInYear = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);

            BigDecimal restAmouth = amount.subtract(staticMonthAmount.multiply(new BigDecimal(i)));
            BigDecimal monthPercent = (restAmouth.multiply(persent.divide(new BigDecimal(100)), MathContext.DECIMAL128).multiply(new BigDecimal(daysInMonth)))
                    .divide(new BigDecimal(daysInYear), 2, RoundingMode.HALF_UP);
            BigDecimal monthAmountWithPercent = staticMonthAmount.add(monthPercent);


            Payout payout = new Payout();
            payout.setCreditName(credit.getName());
            payout.setAmount(String.valueOf(monthAmountWithPercent));
            payout.setDate(calendar.getTime());
            payout.setBalance(String.valueOf(restAmouth));


            payoutList.add(payout);

            try {
                calendar.setTime(sdf.parse(credit.getDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return payoutList;
    }

    public BigDecimal getStaticPayoutForDifferentialPayout(Credit credit) {
        BigDecimal amount = new BigDecimal(credit.getAmount());
        BigDecimal monthCount = new BigDecimal(credit.getMonthCount());
        return amount.divide(monthCount, 2, RoundingMode.HALF_UP);
    }

    public BigDecimal getMonthPercentForDifferentialPayout(Credit credit, Integer month) {
        BigDecimal amount = new BigDecimal(credit.getAmount());
        BigDecimal monthCount = new BigDecimal(credit.getMonthCount());
        BigDecimal persent = new BigDecimal(credit.getRate());
        Date date = null;
        try {
            date = sdf.parse(credit.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance(Locale.US);
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, month);

        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int daysInYear = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);

        BigDecimal staticMonthAmount = amount.divide(monthCount, 2, RoundingMode.HALF_UP);
        BigDecimal restAmouth = amount.subtract(staticMonthAmount.multiply(new BigDecimal(month)));
        BigDecimal monthPercent = (restAmouth.multiply(persent.divide(new BigDecimal(100)), MathContext.DECIMAL128).multiply(new BigDecimal(daysInMonth)))
                    .divide(new BigDecimal(daysInYear), 2, RoundingMode.HALF_UP);
        //BigDecimal monthAmountWithPercent = staticMonthAmount.add(monthPercent);
        return monthPercent;
    }

    public BigDecimal getCreditOverpayment(Credit credit) {
        if (credit.isAnnuity()) {
            Integer monthCount = credit.getMonthCount();
            BigDecimal persent = new BigDecimal(credit.getRate());
            BigDecimal monthPersent = (persent.divide(new BigDecimal(MONTHS_IN_YEAR), MathContext.DECIMAL128)).divide(new BigDecimal(100), MathContext.DECIMAL128);
            BigDecimal amount = new BigDecimal(credit.getAmount());
            BigDecimal annuityCoefficientFirst = monthPersent
                    .multiply((BigDecimal.ONE.add(monthPersent))
                            .pow(monthCount));

            BigDecimal annuityCoefficientSecond = ((BigDecimal.ONE.add(monthPersent)).pow(monthCount)).subtract(BigDecimal.ONE);
            BigDecimal annuityCoefficient = annuityCoefficientFirst.divide(annuityCoefficientSecond, RoundingMode.HALF_UP);
            BigDecimal result = annuityCoefficient.multiply(amount).setScale(2, RoundingMode.HALF_UP);
            return result.multiply(new BigDecimal(monthCount));
        } else {
            BigDecimal monthPayout = new BigDecimal(0);
            BigDecimal staticPayout = getStaticPayoutForDifferentialPayout(credit);
            for (int i = 0; i < credit.getMonthCount(); i++) {
                BigDecimal percentPayout = getMonthPercentForDifferentialPayout(credit, i);
                monthPayout = monthPayout.add(staticPayout.add(percentPayout));
            }
            return monthPayout;
        }
    }

    public static String getMonthName(int month) {
        switch (month + 1) {
            case 1:
                return "В январе ";

            case 2:
                return "В феврале ";

            case 3:
                return "В марте ";

            case 4:
                return "В апреле ";

            case 5:
                return "В мае ";

            case 6:
                return "В июне ";

            case 7:
                return "В июле ";

            case 8:
                return "В августе ";

            case 9:
                return "В сентябре ";

            case 10:
                return "В октябре ";

            case 11:
                return "В ноябре ";

            case 12:
                return "В декабре ";
        }

        return "";
    }
}
