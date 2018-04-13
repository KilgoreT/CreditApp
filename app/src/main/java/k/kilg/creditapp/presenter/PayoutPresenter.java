package k.kilg.creditapp.presenter;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Observable;

import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import k.kilg.creditapp.entities.Credit;
import k.kilg.creditapp.entities.Payout;
import k.kilg.creditapp.model.PayoutModelInterface;
import k.kilg.creditapp.tools.CreditTools;
import k.kilg.creditapp.view.PayoutViewInterface;

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
            mModel.loadCredits();
            mModel
                    .getPayouts()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<Object>>() {
                        @Override
                        public void accept(List<Object> payoutList) throws Exception {
                            //getView().listLog(payoutList);
                            getView().setData(payoutList);
                            getView().showContent();
                        }
                    });
            //getView().showContent();
        }
    }

/*    public List<Object> prepareList (List<Payout> payoutList) {
        List<Object> preparedList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        int month = -1;
        for (Payout payout: payoutList) {
            Date date = payout.getDate();
            calendar.setTime(date);
            if (month != calendar.get(Calendar.MONTH)) {
                month = calendar.get(Calendar.MONTH);
                preparedList.add(CreditTools.getMonthName(month));
            }
            preparedList.add(payout);

        }

        return preparedList;
    }*/

}
