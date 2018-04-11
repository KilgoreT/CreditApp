package k.kilg.creditapp.presenter;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

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
            //getView().setData(mModel.getPayouts());
            mModel.loadCredits();
            mModel
                    .getPayouts()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<Payout>>() {
                        @Override
                        public void accept(List<Payout> payoutList) throws Exception {
                            //getView().listLog(payoutList);
                            getView().setData(payoutList);
                            getView().showContent();
                        }
                    });
            //getView().showContent();
        }
    }
}
