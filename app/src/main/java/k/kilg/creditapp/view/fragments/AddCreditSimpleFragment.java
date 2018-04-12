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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.math.BigDecimal;

import k.kilg.creditapp.R;
import k.kilg.creditapp.entities.Credit;
import k.kilg.creditapp.view.dialogs.DatePickerFragment;


public class AddCreditSimpleFragment extends Fragment {

    private static final String DATE_PICKER_TAG = "DatePickerTag";

    private static final String CREDIT_NAME_KEY = "CreditNameKey";
    private static final String CREDIT_AMOUNT_KEY = "CreditAmountKey";
    private static final String CREDIT_MONTH_COUNT_KEY = "CreditMonthCountKey";
    private static final String CREDIT_RATE_KEY = "CreditRateKey";
    private static final String CREDIT_DATE_KEY = "CreditDateKey";
    private static final String CREDIT_DATABASE_KEY = "CreditDatabaseKey";
    private static final String CREDIT_TYPE_KEY = "CreditTypeKey";

    private EditText mEtCreditName;
    private EditText mEtCreditAmount;
    private RadioGroup mRgCreditType;
    private EditText mEtCreditRate;
    private EditText mEtCreditMonthCount;
    private TextView mTvCreditDate;


    private boolean mEditMode = false;
    private String mCreditDatabaseKey;
    private OnAddCreditSimpleFragmentInteractionListener mListener;

    public AddCreditSimpleFragment() {
        // Required empty public constructor
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
                datePickerDialog
                        .show(getFragmentManager(), DATE_PICKER_TAG);
            }
        });
        if (getArguments() != null) {
            Log.d("###", getClass().getSimpleName() + ":bundle not null");
            mEditMode = true;
            Log.d("###", getClass().getSimpleName() + ":CREDIT_DATABASE_KEY:"+getArguments().getString(CREDIT_DATABASE_KEY));
            mCreditDatabaseKey = getArguments().getString(CREDIT_DATABASE_KEY);
            Log.d("###", getClass().getSimpleName() + ":CREDIT_NAME_KEY:"+getArguments().getString(CREDIT_NAME_KEY));
            mEtCreditName.setText(getArguments().getString(CREDIT_NAME_KEY));
            Log.d("###", getClass().getSimpleName() + ":mEtCreditName.getText:"+ mEtCreditName.getText().toString());
            mEtCreditAmount.setText(getArguments().getString(CREDIT_AMOUNT_KEY));
            mEtCreditMonthCount.setText(getArguments().getString(CREDIT_MONTH_COUNT_KEY));
            mEtCreditRate.setText(getArguments().getString(CREDIT_RATE_KEY));
            mTvCreditDate.setText(getArguments().getString(CREDIT_DATE_KEY));
            mRgCreditType
                    .check(getArguments()
                            .getBoolean(CREDIT_TYPE_KEY)? R.id.addCredit_rgCreditAnnuity : R.id.addCredit_rgCreditDifferential);
        } else {
            Log.d("###", getClass().getSimpleName() + ":bundle is null");
        }
        return v;
    }


    public void onFabPressed() {
        if (mListener != null) {
            Credit credit = createCredit();
            if (credit != null) {
                //clearFields();
                if (mEditMode) {
                    if (mListener != null) {
                        mEditMode = false;
                        mListener.onUpdateCreditSimpleFragmentClose(credit);
                    }
                } else {
                    if (mListener != null) {
                        mEditMode = false;
                        mListener.onAddCreditSimpleFragmentClose(credit);
                    }
                }
            }
        }
    }

    private Credit createCredit() {
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
                credit.setRate(mEtCreditRate.getText().toString());
                credit.setDate(mTvCreditDate.getText().toString());
                if (mCreditDatabaseKey != null) {
                    credit.setKey(mCreditDatabaseKey);
                }

            } catch (IllegalArgumentException e) {
                showSnackbar(e.getMessage());
                return null;
            }

        } else {
            showSnackbar(getString(R.string.add_credit_empty_fields));
            return null;
        }
        return credit;
    }

    private void showSnackbar(String message) {
        if (getView() != null) {
            Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
        }
    }

    public void clearFields() {
        mEtCreditName.setText("");
        mEtCreditAmount.setText("");
        mEtCreditRate.setText("");
        mEtCreditMonthCount.setText("");
        //todo: подумать, как оформить дату
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
        clearFields();
        mListener = null;
    }

    public interface OnAddCreditSimpleFragmentInteractionListener {
        void onAddCreditSimpleFragmentClose(Credit credit);
        void onUpdateCreditSimpleFragmentClose(Credit credit);
    }
}
