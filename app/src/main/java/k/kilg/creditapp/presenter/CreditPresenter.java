package k.kilg.creditapp.presenter;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import k.kilg.creditapp.entities.Credit;
import k.kilg.creditapp.model.CreditModelInterface;
import k.kilg.creditapp.view.CreditViewInterface;

/**
 * Created by apomazkin on 04.04.2018.
 * k.kilg.creditapp.presenter
 * 04.04.2018
 * 14:49
 */
public class CreditPresenter extends MvpBasePresenter<CreditViewInterface> implements CreditPresenterInterface {

    private CreditModelInterface model;


    public CreditPresenter(CreditModelInterface model) {
        this.model = model;
    }

    public void sendQueryForData() {
        if (isViewAttached()) {
            getView().showLoading(false);
            model.getInitialData();
        }
    }


    @Override
    public void loadCredits() {

        if (isViewAttached()) {
            getView().setData(model.getCreditsList());
            getView().showContent();
        }
    }


    @Override
    public void addCredit(Credit credit) {
        model.addCredit(credit);
    }

    @Override
    public void removeCredit(Credit credit) {
        model.removeCredit(credit);
    }

    @Override
    public void updateCredit(Credit credit) {
        model.updateCredit(credit);
    }
}
