package k.kilg.creditapp.presenter;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import java.util.List;

import k.kilg.creditapp.entities.Credit;
import k.kilg.creditapp.model.CreditAppModelInterface;
import k.kilg.creditapp.tools.DatabaseTool;
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

    public void sendQueryForData() {
        if (isViewAttached()) {
            getView().showLoading(false);
            model.initDbListener();
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
