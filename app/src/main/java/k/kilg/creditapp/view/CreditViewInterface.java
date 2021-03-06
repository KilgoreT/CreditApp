package k.kilg.creditapp.view;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import java.util.List;
import java.util.Set;

import k.kilg.creditapp.entities.Credit;

/**
 * Created by apomazkin on 04.04.2018.
 * k.kilg.creditapp.view
 * 04.04.2018
 * 14:11
 */
public interface CreditViewInterface extends MvpLceView<List<Credit>>{
    void addCredit(Credit credit);
    void updateCredit(Credit credit);
    void removeCredit(Credit credit);
    void startDiagramForCredit(Credit credit);
}
