package k.kilg.creditapp.presenter;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import k.kilg.creditapp.entities.Credit;
import k.kilg.creditapp.view.CreditAppViewInterface;

/**
 * Created by apomazkin on 04.04.2018.
 * k.kilg.creditapp.presenter
 * 04.04.2018
 * 14:40
 */
public interface CreditAppPresenterInterface extends MvpPresenter<CreditAppViewInterface> {
    void sendQueryForData();
    void loadCredits();
    void addCredit(Credit credit);
    void removeCredit(Credit credit);
    void updateCredit(Credit credit);
}
