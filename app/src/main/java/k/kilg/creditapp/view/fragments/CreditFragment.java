package k.kilg.creditapp.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.MvpLceViewStateFragment;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;

import java.util.List;

import k.kilg.creditapp.CreditActivity;
import k.kilg.creditapp.R;
import k.kilg.creditapp.entities.Credit;
import k.kilg.creditapp.model.CreditModel;
import k.kilg.creditapp.model.CreditModelInterface;
import k.kilg.creditapp.presenter.CreditPresenter;
import k.kilg.creditapp.presenter.CreditPresenterInterface;
import k.kilg.creditapp.view.CreditViewInterface;
import k.kilg.creditapp.view.adapters.CreditRVAdapter;


public class CreditFragment extends MvpLceViewStateFragment<RecyclerView, List<Credit>, CreditViewInterface, CreditPresenterInterface> implements
        CreditViewInterface,
        CreditRVAdapter.CreditRVAdapterListener{


    private CreditRVAdapter mAdapter;
    private OnCreditFragmentInteractionListener mListener;

    public CreditFragment() {
        // Required empty public constructor
    }


    /* ---------------- Mosby library -------------- */

    @NonNull
    @Override
    public CreditPresenterInterface createPresenter() {
        CreditModelInterface mModel = new CreditModel(this);
        return new CreditPresenter(mModel);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView mRv = (RecyclerView) view.findViewById(R.id.contentView);
        mAdapter = new CreditRVAdapter();
        mAdapter.setListener(this);
        mRv.setAdapter(mAdapter);
        mRv.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    @Override
    public List<Credit> getData() {
        return mAdapter == null ? null : mAdapter.getData();
    }

    @Override
    public void setData(List<Credit> data) {
        mAdapter.setData(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        getPresenter().sendQueryForData();
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        String message = e.getMessage();
        return message == null ? getString(R.string.unknown_error_CS) : message;
    }

    @NonNull
    @Override
    public LceViewState<List<Credit>, CreditViewInterface> createViewState() {
        return new RetainingLceViewState<>();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_credit, container, false);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCreditFragmentInteractionListener) {
            mListener = (OnCreditFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAddCreditFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void addCredit(Credit credit) {
        if (credit != null) {
            getPresenter().addCredit(credit);
        }
    }

    @Override
    public void updateCredit(Credit credit) {
        getPresenter().updateCredit(credit);
    }

    @Override
    public void removeCredit(Credit credit) {
        getPresenter().removeCredit(credit);
    }


    @Override
    public void showActionMode(ActionMode.Callback callback) {
        ((CreditActivity)getActivity()).startSupportActionMode(callback);
        ((CreditActivity)getActivity()).setFabVisible(false);
    }

    public void startEditCredit(Credit credit) {
        if (credit != null) {
            mListener.startEditCredit(credit);
        }
    }

    public void showSnackbar(String message) {
        if (getView() != null) {
            Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
        }
    }


    public interface OnCreditFragmentInteractionListener {
        void startEditCredit(Credit credit);
    }
}
