package k.kilg.creditapp.view.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.lce.MvpLceFragment;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import k.kilg.creditapp.R;
import k.kilg.creditapp.entities.Credit;
import k.kilg.creditapp.entities.Payout;
import k.kilg.creditapp.entities.Resume;
import k.kilg.creditapp.presenter.DiagramPresenter;
import k.kilg.creditapp.presenter.DiagramPresenterInterface;
import k.kilg.creditapp.view.DiagramViewInterface;


public class DiagramFragment extends MvpLceFragment<TableLayout, List<Object>, DiagramViewInterface, DiagramPresenterInterface> implements
        DiagramViewInterface {

    private static final String CREDIT_KEY = "CreditKey";

    private TableLayout mTableLayout;
    private Credit mCredit;
    private List<Object> mPayoutList;

    public DiagramFragment() {
    }

    @NonNull
    @Override
    public DiagramPresenterInterface createPresenter() {
        return new DiagramPresenter(mCredit);
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        String message = e.getMessage();
        return message == null ? getString(R.string.unknown_error_CS) : message;
    }


    @Override
    public void setData(List<Object> data) {
        mPayoutList = data;
        for (Object o : mPayoutList) {
            if (o instanceof  Payout) {
                addRow((Payout) o);
            } else if (o instanceof Resume) {
                addRowResume(getContext().getResources().getString(R.string.item_resume_all_payments),((Resume) o).getAllPaymentAmount());
                addRowResume(getContext().getResources().getString(R.string.item_resume_overpayments),((Resume) o).getAllOverpayment());
            }
        }
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        getPresenter().loadData();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTableLayout = view.findViewById(R.id.contentView);
        addRowTitle();
        loadData(false);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_diagram, container, false);
        if (getArguments() != null) {
            mCredit = getArguments().getParcelable(CREDIT_KEY);
        }
        return v;
    }

    public void addRowTitle() {
        TableRow tr = (TableRow) LayoutInflater.from(getContext()).inflate(R.layout.item_title_diagram, null);
        mTableLayout.addView(tr);
    }
    public void addRowResume(String title, String value) {
        TableRow tr = (TableRow) LayoutInflater.from(getContext()).inflate(R.layout.itemr_resume_diagram, null);
        TextView tvTitle = (TextView) tr.getChildAt(0);
        TextView tvValue = (TextView) tr.getChildAt(1);
        tvTitle.setText(title);
        tvValue.setText(value);
        mTableLayout.addView(tr);
    }

    public void addRow(Payout payout) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        TableRow tr = (TableRow) LayoutInflater.from(getContext()).inflate(R.layout.item_payout_diagram, null);
        TextView tvDate = (TextView) tr.getChildAt(0);
        TextView tvPayout = (TextView) tr.getChildAt(1);
        TextView tvBalance = (TextView) tr.getChildAt(2);
        tvDate.setText(sdf.format(payout.getDate()));
        tvPayout.setText(payout.getAmount());
        tvBalance.setText(payout.getBalance());

        mTableLayout.addView(tr);
    }
}
