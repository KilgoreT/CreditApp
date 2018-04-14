package k.kilg.creditapp.presenter;

import android.content.res.Resources;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Observable;

import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import k.kilg.creditapp.R;
import k.kilg.creditapp.entities.Credit;
import k.kilg.creditapp.entities.Payout;
import k.kilg.creditapp.entities.Resume;
import k.kilg.creditapp.model.PayoutModel;
import k.kilg.creditapp.model.PayoutModelInterface;
import k.kilg.creditapp.tools.CreditTools;
import k.kilg.creditapp.view.PayoutViewInterface;
import k.kilg.creditapp.view.fragments.PayoutFragment;

/**
 * Created by apomazkin on 10.04.2018.
 * k.kilg.creditapp.presenter
 * 10.04.2018
 * 11:54
 */
public class PayoutPresenter extends MvpBasePresenter<PayoutViewInterface> implements PayoutPresenterInterface {

    private PayoutModelInterface mModel;

    public PayoutPresenter(PayoutModelInterface mModel) {
        this.mModel = mModel;
    }

    @Override
    public void loadData() {
        if (isViewAttached()) {
            getView().showLoading(false);
            mModel
                    .getPayouts()
                    .subscribeOn(Schedulers.computation())
                    .flatMap(new Function<List<Credit>, ObservableSource<List<Object>>>() {
                        @Override
                        public ObservableSource<List<Object>> apply(List<Credit> credits) throws Exception {
                            List<Payout> payoutList = new ArrayList<>();
                            for (Credit credit: credits) {
                                if (credit.isAnnuity()) {
                                    payoutList.addAll(CreditTools.getAnnuityMonthPayouts(credit));
                                } else {
                                    payoutList.addAll(CreditTools.getDifferentialMonthPayouts(credit));
                                }
                            }
                            Collections.sort(payoutList, new Comparator<Payout>() {
                                @Override
                                public int compare(Payout o1, Payout o2) {
                                    Date firstDate = o1.getDate();
                                    Date secondDate = o2.getDate();
                                    int sComp = firstDate.compareTo(secondDate);
                                    if (sComp != 0) {
                                        return sComp;
                                    }
                                    String firstName = o1.getCreditName();
                                    String secondName = o2.getCreditName();
                                    return firstName.compareTo(secondName);
                                }
                            });
                            return io.reactivex.Observable
                                    .fromArray((List<Object>) new ArrayList<Object>(prepareList(payoutList)));
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<Object>>() {
                                   @Override
                                   public void accept(List<Object> payoutList) throws Exception {
                                       getView().setData(payoutList);
                                       getView().showContent();
                                   }
                               },
                            new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    getView().showError(throwable, false);
                                }
                            });

        }
    }

    private List<Object> prepareList(List<Payout> payoutList) {
        List<Object> preparedList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        int month = -1;
        for (Payout payout: payoutList) {
            calendar.setTime(payout.getDate());
            if (month != calendar.get(Calendar.MONTH)) {
                month = calendar.get(Calendar.MONTH);
                String monthString = getMonthName(month);
                String yearString = String.valueOf(calendar.get(Calendar.YEAR));
                preparedList.add(monthString + " " + yearString);
            }
            preparedList.add(payout);
        }
        Resume resume = new Resume();
        BigDecimal allPaymentAmount = CreditTools.getAllPaymentAmount(payoutList);
        BigDecimal allOverpayment = allPaymentAmount.subtract(CreditTools.getAllCreditAmount(mModel.getCreditList()));
        resume.setAllPaymentAmount(String.valueOf(allPaymentAmount));
        resume.setAllOverpayment(String.valueOf(allOverpayment));
        preparedList.add(resume);
        return preparedList;
    }

    private String getMonthName(int month) {
        Resources res = ((PayoutFragment)getView()).getResources();
        switch (month + 1) {
            case 1:
                return res.getString(R.string.month_january);

            case 2:
                return res.getString(R.string.month_february);

            case 3:
                return res.getString(R.string.month_march);

            case 4:
                return res.getString(R.string.month_april);

            case 5:
                return res.getString(R.string.month_may);

            case 6:
                return res.getString(R.string.month_june);

            case 7:
                return res.getString(R.string.month_july);

            case 8:
                return res.getString(R.string.month_august);

            case 9:
                return res.getString(R.string.month_september);

            case 10:
                return res.getString(R.string.month_october);

            case 11:
                return res.getString(R.string.month_november);

            case 12:
                return res.getString(R.string.month_december);
        }

        return "";
    }
}
