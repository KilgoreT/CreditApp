package k.kilg.creditapp.presenter;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import k.kilg.creditapp.entities.Credit;
import k.kilg.creditapp.model.CreditAppModelInterface;
import k.kilg.creditapp.view.CreditAppViewInterface;

/**
 * Created by apomazkin on 04.04.2018.
 * k.kilg.creditapp.presenter
 * 04.04.2018
 * 14:49
 */
public class CreditAppPresenter extends MvpBasePresenter<CreditAppViewInterface> implements CreditAppPresenterInterface{

    private CreditAppModelInterface model;


    public CreditAppPresenter(CreditAppModelInterface model) {
        this.model = model;
    }

    @Override
    public void loadCredits() {
        getView().showLoading(false);
        getView().setData(model.getCredits());
        getView().showContent();
        //model.getCredits();
    }

    @Override
    public void addCredit(Credit credit) {
        model.addCredit(credit);
    }
}
