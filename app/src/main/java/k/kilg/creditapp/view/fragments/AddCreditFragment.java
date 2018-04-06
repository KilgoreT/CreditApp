package k.kilg.creditapp.view.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

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
 * {@link AddCreditFragment.OnAddCreditFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddCreditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

//todo: пусть после бэкстека поля очищались.
//todo: нужна ли в добавлении кредита fab? заменить button на fab?
public class AddCreditFragment extends MvpLceViewStateFragment<LinearLayout, Credit, AddCreditAppViewInterface, AddCreditAppPresenterInterface> implements
        AddCreditAppViewInterface{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int CREDIT_NAME_INDEX = 0;
    private static final int CREDIT_TYPE_INDEX = 1;
    private static final int CREDIT_AMOUNT_INDEX = 2;
    private static final int CREDIT_RATE_INDEX = 3;
    private static final int CREDIT_MONTH_COUNT_INDEX = 4;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private AddCreditAppModelInterface mModel;
    private AddCreditAppPresenterInterface mPresenter;
    private EditText mEtCreditName;
    private EditText mEtCreditAmount;
    private RadioGroup mRgCreditType;
    private EditText mEtCreditRate;
    private EditText mEtCreditMonthCount;
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
        mEtCreditName = (EditText) contentView.getChildAt(CREDIT_NAME_INDEX);
        mRgCreditType = (RadioGroup) contentView.getChildAt(CREDIT_TYPE_INDEX);
        mEtCreditAmount = (EditText) contentView.getChildAt(CREDIT_AMOUNT_INDEX);
        mEtCreditRate = (EditText) contentView.getChildAt(CREDIT_RATE_INDEX);
        mEtCreditMonthCount = (EditText) contentView.getChildAt(CREDIT_MONTH_COUNT_INDEX);
        /*mBtnOk = (Button) contentView.getChildAt(CREDIT_BUTTON_OK_INDEX);
        mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo: тут получается дважды валидация, тут и в getData. избавиться от одного
                if(validateForm()) {
                    getPresenter().setCredit(createClass());
                    clearFields();
                    mListener.onAddCreditFragmentClose(getPresenter().getCredit());
                }
            }
        });*/
    }




    @Override
    public Credit getData(){
        /*Log.d("###", "getData");
        Credit credit = new Credit();
        if(validateForm()) {
            credit.setName(mEtCreditName.getText().toString());
            credit.setAmount(Integer.valueOf(mEtCreditAmount.getText().toString()));
            credit.setAnnuity(mRgCreditType.getCheckedRadioButtonId() == R.id.addCredit_rgCreditAnnuity);
            credit.setMonthCount(Integer.valueOf(mEtCreditMonthCount.getText().toString()));
            credit.setRate(Float.valueOf(mEtCreditRate.getText().toString()));
            return credit;
        }*/
       return null;
    }

    @Override
    public void setData(Credit credit) {
        Log.d("###", "setData");
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
        return inflater.inflate(R.layout.fragment_add_credit, container, false);
    }


    public void onFabPressed() {
        if (mListener != null) {
            if(validateForm()) {
                getPresenter().setCredit(createClass());
                clearFields();
                mListener.onAddCreditFragmentClose(getPresenter().getCredit());
            }
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
        Log.d("###", "Detaching...");
        super.onDetach();
        mListener = null;
    }



    private boolean validateForm() {
        Credit credit = new Credit();
        if (!TextUtils.isEmpty(mEtCreditName.getText())
                && !TextUtils.isEmpty(mEtCreditAmount.getText().toString())
                && !TextUtils.isEmpty(mEtCreditMonthCount.getText().toString())
                && !TextUtils.isEmpty(mEtCreditRate.getText().toString())
                ) {
            try {
                credit.setName(mEtCreditName.getText().toString());
                credit.setAmount(Integer.valueOf(mEtCreditAmount.getText().toString()));
                credit.setAnnuity(mRgCreditType.getCheckedRadioButtonId() == R.id.addCredit_rgCreditAnnuity);
                credit.setMonthCount(Integer.valueOf(mEtCreditMonthCount.getText().toString()));
                credit.setRate(Float.valueOf(mEtCreditRate.getText().toString()));
            } catch (IllegalArgumentException e) {
                showSnackbar(e.getMessage());
                return false;
            }

        } else {
            //todo: edit message
            showSnackbar("Fill filds!!!");
            return false;
        }
        return true;
    }

    private Credit createClass() {
        Log.d("###", "createClass");
        Credit credit = new Credit();
        if(validateForm()) {
            credit.setName(mEtCreditName.getText().toString());
            credit.setAmount(Integer.valueOf(mEtCreditAmount.getText().toString()));
            credit.setAnnuity(mRgCreditType.getCheckedRadioButtonId() == R.id.addCredit_rgCreditAnnuity);
            credit.setMonthCount(Integer.valueOf(mEtCreditMonthCount.getText().toString()));
            credit.setRate(Float.valueOf(mEtCreditRate.getText().toString()));
            return credit;
        }
        return null;
    }

    public void clearFields()  {
        mEtCreditName.setText("");
        mEtCreditAmount.setText("");
        mEtCreditRate.setText("");
        mEtCreditMonthCount.setText("");
    }

    private void showSnackbar(String msg) {
        Snackbar.make(getView(), msg, Snackbar.LENGTH_LONG).show();
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
        void onAddCreditFragmentClose(Credit credit);
    }
}
