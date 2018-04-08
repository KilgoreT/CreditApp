package k.kilg.creditapp.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import k.kilg.creditapp.R;
import k.kilg.creditapp.entities.Credit;
import k.kilg.creditapp.model.AddCreditAppModelInterface;
import k.kilg.creditapp.presenter.AddCreditAppPresenterInterface;
import k.kilg.creditapp.view.dialogs.DatePickerFragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnAddCreditSimpleFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddCreditSimpleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddCreditSimpleFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
  /*  private static final int CREDIT_NAME_INDEX = 0;
    private static final int CREDIT_TYPE_INDEX = 1;
    private static final int CREDIT_AMOUNT_INDEX = 2;
    private static final int CREDIT_RATE_INDEX = 3;
    private static final int CREDIT_MONTH_COUNT_INDEX = 4;*/

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private AddCreditAppModelInterface mModel;
    private AddCreditAppPresenterInterface mPresenter;
    private LinearLayout mLLNameAndType;
    private LinearLayout mLLAmountAndRate;
    private LinearLayout mLLMonthCountAndDate;
    private EditText mEtCreditName;
    private EditText mEtCreditAmount;
    private RadioGroup mRgCreditType;
    private EditText mEtCreditRate;
    private EditText mEtCreditMonthCount;
    private TextView mTvCreditDate;
    //private Credit mCredit;


    private OnAddCreditSimpleFragmentInteractionListener mListener;

    public AddCreditSimpleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddCreditSimpleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddCreditSimpleFragment newInstance(String param1, String param2) {
        AddCreditSimpleFragment fragment = new AddCreditSimpleFragment();
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
        View v = inflater.inflate(R.layout.fragment_add_credit_simple, container, false);
        mEtCreditName = (EditText) v.findViewById(R.id.addCredit_etName);
        mRgCreditType = (RadioGroup) v.findViewById(R.id.addCredit_rgCreditType);
        mEtCreditAmount = (EditText) v.findViewById(R.id.addCredit_etAmount);
        mEtCreditRate = (EditText) v.findViewById(R.id.addCredit_etRate);
        mEtCreditMonthCount = (EditText) v.findViewById(R.id.addCredit_etMonthCount);
        mTvCreditDate = (TextView) v.findViewById(R.id.addCredit_tvDate);
        mTvCreditDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePickerDialog = new DatePickerFragment();
                datePickerDialog.show(getFragmentManager(), "DatePickerTag");
            }
        });
        return v;
    }


    public void onFabPressed() {
        Log.d("###", ">>" + getClass().getSimpleName() + "onFabPressed");
        if (mListener != null) {
            Credit credit = createCredit();
            if (credit != null) {
                clearFields();
                mListener.onAddCreditSimpleFragmentClose(credit);
            }
        }
    }

    private Credit createCredit() {
        Log.d("###", ">>" + getClass().getSimpleName() + ":createClass");
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
                credit.setDate(mTvCreditDate.getText().toString());

            } catch (IllegalArgumentException e) {
                showSnackbar(e.getMessage());
                return null;
            }

        } else {
            //todo: edit message
            showSnackbar("Fill filds!!!");
            return null;
        }
        return credit;
    }

    private void showSnackbar(String message) {
        if (getView() != null) {
            Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
        }
    }

    public void clearFields()  {
        mEtCreditName.setText("");
        mEtCreditAmount.setText("");
        mEtCreditRate.setText("");
        mEtCreditMonthCount.setText("");
        mTvCreditDate.setText("EditDate");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAddCreditSimpleFragmentInteractionListener) {
            mListener = (OnAddCreditSimpleFragmentInteractionListener) context;
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
    public interface OnAddCreditSimpleFragmentInteractionListener {
        void onAddCreditSimpleFragmentClose(Credit credit);
    }
}
