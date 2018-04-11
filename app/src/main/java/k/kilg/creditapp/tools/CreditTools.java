package k.kilg.creditapp.tools;

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

    private static final int MONTHS_IN_YEAR = 12;

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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

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
            calendar.add(Calendar.MONTH, i);
            payout.setCreditName(credit.getName());
            payout.setAmount(String.valueOf(result));
            payout.setDate(calendar.getTime());
            payoutList.add(payout);
        }

        return  payoutList;
    }

    public static List<Payout> getDifferentialMonthPayouts(Credit credit) {

        BigDecimal persent = new BigDecimal(credit.getRate());
        BigDecimal amount = new BigDecimal(credit.getAmount());
        Integer monthCount = credit.getMonthCount();
        List<Payout> payoutList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

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


            payoutList.add(payout);

            try {
                calendar.setTime(sdf.parse(credit.getDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return payoutList;
    }
}
