package k.kilg.creditapp.presenter;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import k.kilg.creditapp.view.DiagramViewInterface;

/**
 * Created by apomazkin on 18.04.2018.
 * k.kilg.creditapp.presenter
 * 18.04.2018
 * 12:07
 */
public interface DiagramPresenterInterface extends MvpPresenter<DiagramViewInterface> {
    void loadData();
}
