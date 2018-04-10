package k.kilg.creditapp.tools;

import android.util.Log;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by apomazkin on 10.04.2018.
 * k.kilg.creditapp.tools
 * 10.04.2018
 * 14:33
 */
public class CreditTools {

    private static final int MONTHS_IN_YEAR = 12;

    public static BigDecimal getAnnuityMonthPayout(BigDecimal persent, Integer amount, Integer monthCount) {

        BigDecimal monthPersent = (persent.divide(new BigDecimal(MONTHS_IN_YEAR), MathContext.DECIMAL128)).divide(new BigDecimal(100), MathContext.DECIMAL128);
        //Log.d("###", "CreditTools : monthPersent = " + monthPersent);

        BigDecimal annuityCoefficientFirst = monthPersent
                .multiply((BigDecimal.ONE.add(monthPersent))
                        .pow(monthCount));
        //Log.d("###", "CreditTools : annuityCoefficientFirst = " + annuityCoefficientFirst);
        BigDecimal annuityCoefficientSecond = ((BigDecimal.ONE.add(monthPersent)).pow(monthCount)).subtract(BigDecimal.ONE);
        //Log.d("###", "CreditTools : annuityCoefficientSecond = " + annuityCoefficientSecond);
        BigDecimal annuityCoefficient = annuityCoefficientFirst.divide(annuityCoefficientSecond, RoundingMode.HALF_UP);
        //Log.d("###", "CreditTools : annuityCoefficient = " + annuityCoefficient);

        BigDecimal result = annuityCoefficient.multiply(new BigDecimal(amount)).setScale(2, RoundingMode.HALF_UP);
        //Log.d("###", "CreditTools : result = " + result);
        return  result;
    }

    public static List<BigDecimal> getDifferentialMonthPayout(BigDecimal persent, Integer amount, Integer monthCount) {
        List<BigDecimal> result = new ArrayList<>();
        BigDecimal staticMonthAmount = new BigDecimal(amount).divide(new BigDecimal(monthCount), 2, RoundingMode.HALF_UP);
        Log.d("###", "CreditTools:getDifferentialMonthPayout: staticMonthAmount = " + staticMonthAmount);
        for (int i = 0; i < monthCount; i++) {
            BigDecimal monthAmountWithPercent = (new BigDecimal(amount).subtract(staticMonthAmount.multiply(new BigDecimal(i)))).multiply((persent.divide(new BigDecimal(100), MathContext.DECIMAL128)).divide(new BigDecimal(MONTHS_IN_YEAR), MathContext.DECIMAL128))
                    .add(staticMonthAmount).setScale(2, RoundingMode.HALF_UP);
            Log.d("###", "CreditTools:getDifferentialMonthPayout:" + i + ")" +"monthAmountWithPercent = " + monthAmountWithPercent);
            result.add(monthAmountWithPercent);
        }

        return result;
    }
    public static List<BigDecimal> getDifferentialMonthPayoutWithDate(BigDecimal persent, Integer amount, Integer monthCount) {
        List<BigDecimal> result = new ArrayList<>();
        Calendar calendar = Calendar.getInstance(Locale.US);
        BigDecimal staticMonthAmount = new BigDecimal(amount).divide(new BigDecimal(monthCount), 2, RoundingMode.HALF_UP);
        //result.add(staticMonthAmount);
        for (int i = 0; i < monthCount; i++) {
            //calendar.after(Calendar.MONTH + 1);
            Log.d("###", "CreditTools:getDifferentialMonthPayoutWithDate:month is = " + calendar.get(Calendar.MONTH));
            int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            Log.d("###", "CreditTools:getDifferentialMonthPayoutWithDate:daysInMonth = " + daysInMonth);
            int daysInYear = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
            Log.d("###", "CreditTools:getDifferentialMonthPayoutWithDate:daysInYear = " + daysInYear);
            BigDecimal restAmouth = new BigDecimal(amount).subtract(staticMonthAmount.multiply(new BigDecimal(i)));
            Log.d("###", "CreditTools:getDifferentialMonthPayoutWithDate:restAmouth = " + restAmouth);
            BigDecimal monthPercent = (restAmouth.multiply(persent.divide(new BigDecimal(100)), MathContext.DECIMAL128).multiply(new BigDecimal(daysInMonth))).divide(new BigDecimal(daysInYear), 2, RoundingMode.HALF_UP);
            Log.d("###", "CreditTools:getDifferentialMonthPayoutWithDate:monthPercent = " + monthPercent);
            BigDecimal monthAmountWithPercent = staticMonthAmount.add(monthPercent);
            Log.d("###", "CreditTools:getDifferentialMonthPayoutWithDate:monthAmountWithPercent = " + monthAmountWithPercent);
            result.add(monthAmountWithPercent);
            calendar.add(Calendar.MONTH, 1);
        }
        return result;
    }
}
