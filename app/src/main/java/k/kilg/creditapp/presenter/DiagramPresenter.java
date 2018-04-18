package k.kilg.creditapp.presenter;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import k.kilg.creditapp.entities.Credit;
import k.kilg.creditapp.entities.Payout;
import k.kilg.creditapp.entities.Resume;
import k.kilg.creditapp.tools.CreditTools;
import k.kilg.creditapp.view.DiagramViewInterface;

/**
 * Created by apomazkin on 18.04.2018.
 * k.kilg.creditapp.presenter
 * 18.04.2018
 * 12:10
 */
public class DiagramPresenter extends MvpBasePresenter<DiagramViewInterface> implements DiagramPresenterInterface {
    private Credit mCredit;

    public DiagramPresenter(Credit mCredit) {
        this.mCredit = mCredit;
    }

    @Override
    public void loadData() {
        if (isViewAttached()) {
            getView().showLoading(false);
            Observable
                    .just(mCredit)
                    .subscribeOn(Schedulers.io())
                    .flatMap(new Function<Credit, ObservableSource<List<Object>>>() {
                        @Override
                        public ObservableSource<List<Object>> apply(Credit credit) throws Exception {
                            List<Payout> payoutList = credit.isAnnuity() ? CreditTools.getAnnuityMonthPayouts(credit) : CreditTools.getDifferentialMonthPayouts(credit);
                            Collections.sort(payoutList, new Comparator<Payout>() {
                                @Override
                                public int compare(Payout o1, Payout o2) {
                                    Date firstDate = o1.getDate();
                                    Date secondDate = o2.getDate();
                                    return firstDate.compareTo(secondDate);
                                }
                            });

                            return Observable.fromArray((List<Object>) new ArrayList<>(prepareList(payoutList)));
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<Object>>() {
                                   @Override
                                   public void accept(List<Object> objects) throws Exception {
                                       getView().setData(objects);
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
        preparedList.addAll(payoutList);

        Resume resume = new Resume();
        BigDecimal allPaymentAmount = CreditTools.getAllPaymentAmount(payoutList);
        BigDecimal allOverpayment = allPaymentAmount.subtract(CreditTools.getCreditAmount(mCredit));
        resume.setAllPaymentAmount(String.valueOf(allPaymentAmount));
        resume.setAllOverpayment(String.valueOf(allOverpayment));
        preparedList.add(resume);

        return preparedList;
    }
}
