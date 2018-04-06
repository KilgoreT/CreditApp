package k.kilg.creditapp.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import k.kilg.creditapp.R;
import k.kilg.creditapp.entities.Credit;

/**
 * Created by apomazkin on 04.04.2018.
 * k.kilg.creditapp.view.adapters
 * 04.04.2018
 * 15:18
 */
public class CreditRVAdapter extends RecyclerView.Adapter<CreditRVAdapter.CreditHolder> {

    private List<Credit> mCredits;

    public void setData(List<Credit> data) {
        mCredits = data;
    }

    public List<Credit> getData() {
        return mCredits;
    }


    @Override
    public CreditRVAdapter.CreditHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_credit_item, parent, false);
        CreditHolder holder = new CreditHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(CreditRVAdapter.CreditHolder holder, int position) {
        holder.bind(mCredits.get(position));
    }

    @Override
    public int getItemCount() {
        if (mCredits != null) {
            return mCredits.size();
        }
        return 0;
    }

    public class CreditHolder extends RecyclerView.ViewHolder {

        private TextView mCreditName;
        private TextView mCreditType;
        private TextView mCreditMonthCount;
        private TextView mCreditAmount;
        private TextView mCreditRate;

        public CreditHolder(View itemView) {
            super(itemView);
            mCreditName = (TextView) itemView.findViewById(R.id.tvCreditName);
            mCreditType = (TextView) itemView.findViewById(R.id.tvCreditType);
            mCreditMonthCount = (TextView) itemView.findViewById(R.id.tvCreditMonthCount);
            mCreditAmount = (TextView) itemView.findViewById(R.id.tvCreditAmount);
            mCreditRate = (TextView) itemView.findViewById(R.id.tvCreditRate);
        }

        public void bind(Credit credit) {
            mCreditName.setText(credit.getName());
            mCreditType.setText(credit.isAnnuity() ? "Annuity payout" : "Differential payout");
            mCreditMonthCount.setText("" + credit.getMonthCount() + " months");
            mCreditAmount.setText("Amount: " + credit.getAmount());
            mCreditRate.setText("Rate: " + credit.getRate());
        }
    }



}
