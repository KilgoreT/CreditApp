package k.kilg.creditapp.presenter;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

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
            getView().setData(mModel.getPayouts());
            getView().showContent();
        }
    }
}
