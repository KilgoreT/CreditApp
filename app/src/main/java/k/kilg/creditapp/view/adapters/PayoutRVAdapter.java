package k.kilg.creditapp.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import k.kilg.creditapp.R;
import k.kilg.creditapp.entities.Payout;

/**
 * Created by apomazkin on 10.04.2018.
 * k.kilg.creditapp.view.adapters
 * 10.04.2018
 * 12:30
 */
public class PayoutRVAdapter extends RecyclerView.Adapter<PayoutRVAdapter.PayoutHolder> {
    private List<Payout> mPayout;
    private PayoutRVAdapterListener mListener;

    @Override
    public PayoutRVAdapter.PayoutHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_payout_item, parent, false);
        PayoutHolder holder = new PayoutHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(PayoutRVAdapter.PayoutHolder holder, int position) {
        holder.bind(mPayout.get(position));
    }

    @Override
    public int getItemCount() {
        if (mPayout != null) {
            return mPayout.size();
        }
        return 0;
    }

    public List<Payout> getData() {
        return mPayout;
    }

    public void setData(List<Payout> data) {
        this.mPayout = data;
    }

    public class PayoutHolder extends RecyclerView.ViewHolder{
        private TextView mCreditName;
        private TextView mPayoutAmount;
        private TextView mPayoutDate;

        public PayoutHolder(View itemView) {
            super(itemView);
            mCreditName = (TextView) itemView.findViewById(R.id.payoutCreditName);
            mPayoutAmount = (TextView) itemView.findViewById(R.id.payoutAmount);
            mPayoutDate = (TextView) itemView.findViewById(R.id.payoutDate);
        }

        public void bind(Payout payout) {
            mCreditName.setText(payout.getCreditName());
            mPayoutAmount.setText(payout.getAmount());
            mPayoutDate.setText(payout.getDate());
        }
    }

    private interface PayoutRVAdapterListener {
    }
}
