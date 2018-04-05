package k.kilg.creditapp.presenter;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import k.kilg.creditapp.entities.Credit;
import k.kilg.creditapp.model.AddCreditAppModelInterface;
import k.kilg.creditapp.view.AddCreditAppViewInterface;

/**
 * Created by apomazkin on 05.04.2018.
 * k.kilg.creditapp.presenter
 * 05.04.2018
 * 10:57
 */
public class AddCreditAppPresenter  extends MvpBasePresenter<AddCreditAppViewInterface> implements AddCreditAppPresenterInterface {
    private AddCreditAppModelInterface model;

    public AddCreditAppPresenter(AddCreditAppModelInterface model) {
        this.model = model;
    }

    @Override
    public void loadCredit() {
        getView().showLoading(false);
        if (!model.isModelEmpty()) {
            getView().setData(model.getCredit());
        }
        getView().showContent();
    }

    @Override
    public void setCredit(Credit credit) {
        model.setModel(credit);
    }

    @Override
    public Credit getCredit() {
        return model.getCredit();
    }


}
