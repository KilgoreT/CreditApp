package k.kilg.creditapp.presenter;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import k.kilg.creditapp.entities.Credit;
import k.kilg.creditapp.view.AddCreditAppViewInterface;

/**
 * Created by apomazkin on 05.04.2018.
 * k.kilg.creditapp.presenter
 * 05.04.2018
 * 10:53
 */
public interface AddCreditAppPresenterInterface extends MvpPresenter<AddCreditAppViewInterface> {
    void loadCredit();
    void setCredit(Credit credit);
    Credit getCredit();
}
