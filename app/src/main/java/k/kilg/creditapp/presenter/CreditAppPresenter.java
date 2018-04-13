package k.kilg.creditapp.presenter;

import android.util.Log;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import k.kilg.creditapp.entities.Credit;
import k.kilg.creditapp.model.CreditAppModelInterface;
import k.kilg.creditapp.view.CreditViewInterface;

/**
 * Created by apomazkin on 04.04.2018.
 * k.kilg.creditapp.presenter
 * 04.04.2018
 * 14:49
 */
public class CreditAppPresenter extends MvpBasePresenter<CreditViewInterface> implements CreditAppPresenterInterface{

    private CreditAppModelInterface model;


    public CreditAppPresenter(CreditAppModelInterface model) {
        this.model = model;
    }

    public void sendQueryForData() {
        Log.d("###", "sendQueryForData starting");
        if (isViewAttached()) {
            getView().showLoading(false);
            Log.d("###", "sendQueryForData starting initial data");
            model.getInitialData();
        }
    }


    @Override
    public void loadCredits() {

        if (isViewAttached()) {
            getView().setData(model.getCreditsList());
            getView().showContent();
            //model.initDbListener();
        }
    }


    @Override
    public void addCredit(Credit credit) {
        Log.d("###", "Presenter addCredit");
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
