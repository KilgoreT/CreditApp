package k.kilg.creditapp.view.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.MvpLceViewStateFragment;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;

import java.util.List;

import k.kilg.creditapp.R;
import k.kilg.creditapp.entities.Payout;
import k.kilg.creditapp.model.PayoutModel;
import k.kilg.creditapp.model.PayoutModelInterface;
import k.kilg.creditapp.presenter.PayoutPresenter;
import k.kilg.creditapp.presenter.PayoutPresenterInterface;
import k.kilg.creditapp.view.PayoutViewInterface;
import k.kilg.creditapp.view.adapters.PayoutRVAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnPayoutFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PayoutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PayoutFragment extends MvpLceViewStateFragment<RecyclerView, List<Payout>, PayoutViewInterface, PayoutPresenterInterface> implements
        PayoutViewInterface  {

    private OnPayoutFragmentInteractionListener mListener;

    private PayoutRVAdapter mAdapter;
    private RecyclerView mRV;

    public PayoutFragment() {
        // Required empty public constructor
    }


    /* ---------------- Mosby library -------------- */

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRV = (RecyclerView) view.findViewById(R.id.contentView);
        mAdapter = new PayoutRVAdapter();
        mRV.setAdapter(mAdapter);
        mRV.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    @NonNull
    @Override
    public PayoutPresenterInterface createPresenter() {
        PayoutModelInterface mModel = new PayoutModel();
        return new PayoutPresenter(mModel);
    }

    @Override
    public List<Payout> getData() {
        return mAdapter == null ? null : mAdapter.getData();
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        getPresenter().loadData();
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        String message = e.getMessage();
        return message == null ? "Unknown error" : message;
    }

    @NonNull
    @Override
    public LceViewState<List<Payout>, PayoutViewInterface> createViewState() {
        return new RetainingLceViewState<>();
    }

    @Override
    public  void setData(List<Payout> data) {
        mAdapter.setData(data);
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
        return inflater.inflate(R.layout.fragment_payout, container, false);
    }
    // TODO: Rename method, update argument and hook method into UI event

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPayoutFragmentInteractionListener) {
            mListener = (OnPayoutFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPayoutFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void listLog(List<Payout> payoutList) {
        for (Payout payout : payoutList) {
            Log.d("###", getClass().getSimpleName() + ":credit name is " + payout.getCreditName());
            Log.d("###", getClass().getSimpleName() + ":credit name is " + payout.getAmount());
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
    public interface OnPayoutFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
