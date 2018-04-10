package k.kilg.creditapp.presenter;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import k.kilg.creditapp.view.PayoutViewInterface;

/**
 * Created by apomazkin on 10.04.2018.
 * k.kilg.creditapp.presenter
 * 10.04.2018
 * 11:53
 */
public interface PayoutPresenterInterface extends MvpPresenter<PayoutViewInterface>{
    void loadData();
}
