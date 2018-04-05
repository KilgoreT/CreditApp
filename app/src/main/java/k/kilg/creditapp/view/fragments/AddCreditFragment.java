package k.kilg.creditapp.view.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.hannesdorfmann.mosby.mvp.viewstate.lce.LceViewState;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.MvpLceViewStateFragment;
import com.hannesdorfmann.mosby.mvp.viewstate.lce.data.RetainingLceViewState;

import k.kilg.creditapp.R;
import k.kilg.creditapp.entities.Credit;
import k.kilg.creditapp.model.AddCreditAppModel;
import k.kilg.creditapp.model.AddCreditAppModelInterface;
import k.kilg.creditapp.presenter.AddCreditAppPresenter;
import k.kilg.creditapp.presenter.AddCreditAppPresenterInterface;
import k.kilg.creditapp.view.AddCreditAppViewInterface;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddCreditFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddCreditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddCreditFragment extends MvpLceViewStateFragment<LinearLayout, Credit, AddCreditAppViewInterface, AddCreditAppPresenterInterface> implements
        AddCreditAppViewInterface{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private AddCreditAppModelInterface mModel;
    private AddCreditAppPresenterInterface mPresenter;
    private EditText mEtCreditName;
    private EditText mEtCreditAmount;
    private Button mBtnOk;
    private Credit mCredit;

    private OnAddCreditFragmentInteractionListener mListener;

    public AddCreditFragment() {
        // Required empty public constructor
    }

    /* ---------------- Mosby library -------------- */

    @Override
    public AddCreditAppPresenterInterface createPresenter() {
        mModel = new AddCreditAppModel();
        mPresenter = new AddCreditAppPresenter(mModel);
        return mPresenter;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //todo: мутно как то через чилд
        mEtCreditName = (EditText) contentView.getChildAt(0);
        mEtCreditAmount = (EditText) contentView.getChildAt(1);
        mBtnOk = (Button) contentView.getChildAt(2);
        mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(getView(), "Closing fragment", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                getPresenter().setCredit(getData());
                mListener.onAddCreditFragmentClose(getPresenter().getCredit());

            }
        });
    }



    @Override
    public Credit getData() {
        Credit credit = new Credit();

        if (!TextUtils.isEmpty(mEtCreditName.getText())) {
            try {
                credit.setName(mEtCreditName.getText().toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        if (!TextUtils.isEmpty(mEtCreditAmount.getText())) {
            try {
                credit.setAmount(Integer.valueOf(mEtCreditAmount.getText().toString()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        //if (TextUtils.isEmpty(mEtCreditName.getText()) && TextUtils.isEmpty(mEtCreditName.getText())) {

        //}
        return credit;
    }

    @Override
    public void setData(Credit credit) {
        mCredit = credit;
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        getPresenter().loadCredit();
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        String message = e.getMessage();
        return message == null ? "Unknown error" : message;
    }

    @Override
    public LceViewState<Credit, AddCreditAppViewInterface> createViewState() {
        return new RetainingLceViewState<Credit, AddCreditAppViewInterface>();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddCreditFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddCreditFragment newInstance(String param1, String param2) {
        AddCreditFragment fragment = new AddCreditFragment();
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
        View view = inflater.inflate(R.layout.fragment_add_credit, container, false);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onAddCreditFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAddCreditFragmentInteractionListener) {
            mListener = (OnAddCreditFragmentInteractionListener) context;
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
    public interface OnAddCreditFragmentInteractionListener {
        // TODO: Update argument type and name
        void onAddCreditFragmentClose(Credit credit);
        void onAddCreditFragmentInteraction(Uri uri);
    }
}
