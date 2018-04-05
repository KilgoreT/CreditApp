package k.kilg.creditapp.view;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import k.kilg.creditapp.entities.Credit;

/**
 * Created by apomazkin on 05.04.2018.
 * k.kilg.creditapp.view
 * 05.04.2018
 * 10:53
 */
public interface AddCreditAppViewInterface extends MvpLceView<Credit> {
    Credit getData();
}
