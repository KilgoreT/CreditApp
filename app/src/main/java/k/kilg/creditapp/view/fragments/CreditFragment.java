package k.kilg.creditapp.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
import k.kilg.creditapp.model.CreditAppModel;
import k.kilg.creditapp.model.CreditAppModelInterface;
import k.kilg.creditapp.presenter.CreditAppPresenter;
import k.kilg.creditapp.presenter.CreditAppPresenterInterface;
import k.kilg.creditapp.view.CreditAppViewInterface;
import k.kilg.creditapp.view.adapters.CreditRVAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreditFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreditFragment extends MvpLceViewStateFragment<RecyclerView, List<Credit>, CreditAppViewInterface, CreditAppPresenterInterface> implements
        CreditAppViewInterface,
        CreditRVAdapter.CreditRVAdapterListener{
        //, RestorableViewState {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private CreditAppModelInterface mModel;
    private CreditAppPresenterInterface mPresenter;

    private RecyclerView mRv;
    private CreditRVAdapter mAdapter;

    private OnCreditFragmentInteractionListener mListener;

    public CreditFragment() {
        // Required empty public constructor
    }


    /* ---------------- Mosby library -------------- */

    @Override
    public CreditAppPresenterInterface createPresenter() {
        mModel = new CreditAppModel(this);
        mPresenter = new CreditAppPresenter(mModel);
        return mPresenter;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRv = (RecyclerView) view.findViewById(R.id.contentView);
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
        getPresenter().loadCredits();
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        String message = e.getMessage();
        return message == null ? "Unknown error" : message;
    }

    @NonNull
    @Override
    public LceViewState<List<Credit>, CreditAppViewInterface> createViewState() {
        return new RetainingLceViewState<List<Credit>, CreditAppViewInterface>();
    }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreditFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreditFragment newInstance(String param1, String param2) {
        CreditFragment fragment = new CreditFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_credit, container, false);

    }

    // TODO: Rename method, update argument and hook method into UI event

    public void onButtonPressed(Credit credit) {
        if (mListener != null) {
            mListener.startEditCredit(credit);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCreditFragmentInteractionListener) {
            mListener = (OnCreditFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAddCreditSimpleFragmentInteractionListener");
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
        mAdapter.removeCredit(credit);
        getPresenter().removeCredit(credit);

    }


    @Override
    public void showActionMode(ActionMode.Callback callback) {
        ((CreditActivity)getActivity()).startSupportActionMode(callback);
        ((CreditActivity) getActivity()).setFabVisible(false);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnCreditFragmentInteractionListener {
        // TODO: Update argument type and name
        void startEditCredit(Credit credit);
    }
}
