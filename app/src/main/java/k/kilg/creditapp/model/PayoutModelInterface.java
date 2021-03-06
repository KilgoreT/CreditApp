package k.kilg.creditapp.model;

import java.util.List;

import io.reactivex.Observable;
import k.kilg.creditapp.entities.Credit;
import k.kilg.creditapp.presenter.PayoutPresenterInterface;

/**
 * Created by apomazkin on 10.04.2018.
 * k.kilg.creditapp.model
 * 10.04.2018
 * 11:56
 */
public interface PayoutModelInterface {
    List<Credit> getCreditList();
    Observable<List<Credit>> getPayouts();
    void loadCredits();
}
