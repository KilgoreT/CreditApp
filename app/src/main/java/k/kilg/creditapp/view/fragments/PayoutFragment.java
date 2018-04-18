package k.kilg.creditapp.view.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceFragment;
import java.util.List;
import k.kilg.creditapp.R;
import k.kilg.creditapp.model.PayoutModel;
import k.kilg.creditapp.model.PayoutModelInterface;
import k.kilg.creditapp.presenter.PayoutPresenter;
import k.kilg.creditapp.presenter.PayoutPresenterInterface;
import k.kilg.creditapp.view.PayoutViewInterface;
import k.kilg.creditapp.view.adapters.PayoutRVAdapter;

public class PayoutFragment extends MvpLceFragment<RecyclerView, List<Object>, PayoutViewInterface, PayoutPresenterInterface> implements
            PayoutViewInterface  {

    private PayoutRVAdapter mAdapter;

    public PayoutFragment() {
        // Required empty public constructor
    }


    /* ---------------- Mosby library -------------- */

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView mRV = (RecyclerView) view.findViewById(R.id.contentView);
        mAdapter = new PayoutRVAdapter();
        mRV.setAdapter(mAdapter);
        mRV.setLayoutManager(new LinearLayoutManager(view.getContext()));
        loadData(false);
    }

    @NonNull
    @Override
    public PayoutPresenterInterface createPresenter() {
        PayoutModelInterface mModel = new PayoutModel();
        return new PayoutPresenter(mModel);
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        getPresenter().loadData();
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        String message = e.getMessage();
        return message == null ? getString(R.string.unknown_error_CS) : message;
    }

    @Override
    public  void setData(List<Object> data) {
        mAdapter.setData(data);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_payout, container, false);
    }
}
