package k.kilg.creditapp.model;

import android.util.Log;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import k.kilg.creditapp.entities.Payout;
import k.kilg.creditapp.tools.CreditTools;

/**
 * Created by apomazkin on 10.04.2018.
 * k.kilg.creditapp.model
 * 10.04.2018
 * 11:57
 */
public class PayoutModel implements PayoutModelInterface {
    List<Payout> mPayouts = new ArrayList<>();

    public PayoutModel() {
        Payout payout1 = new Payout();
        payout1.setCreditName("Tralala");
        payout1.setAmount("55000");
        payout1.setDate("This is date");

        Payout payout2 = new Payout();
        payout2.setCreditName("Tralala2");
        payout2.setAmount("222222");
        payout2.setDate("This is no date");

        mPayouts.add(payout1);
        mPayouts.add(payout2);
        Log.d("###", ">>Model: getMonthAmount = " + CreditTools.getAnnuityMonthPayout(new BigDecimal(20), 1_000_000, 36));
        Log.d("###", ">>Model: getMonthAmount = " + CreditTools.getDifferentialMonthPayout(new BigDecimal(10), 100_000, 6));
        Log.d("###", ">>Model: getMonthAmount = " + CreditTools.getDifferentialMonthPayoutWithDate(new BigDecimal(10), 100_000, 48));
    }

    public List<Payout> getPayouts() {
        return mPayouts;
    }
}
