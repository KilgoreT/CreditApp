package k.kilg.creditapp.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import k.kilg.creditapp.R;
import k.kilg.creditapp.entities.Payout;

/**
 * Created by apomazkin on 10.04.2018.
 * k.kilg.creditapp.view.adapters
 * 10.04.2018
 * 12:30
 */
public class PayoutRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int PAYOUT = 0;
    private static final int MONTH = 1;
    private List<Object> mData;
    private List<Payout> mPayout;
    private PayoutRVAdapterListener mListener;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case MONTH:
                View vMonth = inflater.inflate(R.layout.rv_month_item, parent, false);
                viewHolder = new MonthHolder(vMonth);
                break;
            case PAYOUT:
                View vPayout = inflater.inflate(R.layout.rv_payout_item, parent, false);
                viewHolder = new PayoutHolder(vPayout);
                break;
             default:
                 View v = inflater.inflate(R.layout.rv_default_item, parent, false);
                 viewHolder = new DefaultHolder(v);
                 break;

        }
        //View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_payout_item, parent, false);
        //PayoutHolder holder = new PayoutHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case PAYOUT:
                PayoutHolder payoutHolder = (PayoutHolder) holder;
                payoutHolder.bind((Payout) mData.get(position));
                break;
            case MONTH:
                MonthHolder monthHolder = (MonthHolder) holder;
                monthHolder.bind((String) mData.get(position));
                break;
        }
        //holder.bind(mPayout.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        if (mData.get(position) instanceof Payout) {
            return PAYOUT;
        } else if (mData.get(position) instanceof String) {
            return MONTH;
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        }
        return 0;
    }

    public List<Object> getData() {
        return mData;
    }

    public void setData(List<Object> data) {
        this.mData = data;
    }

    public class PayoutHolder extends RecyclerView.ViewHolder{
        private TextView mCreditName;
        private TextView mPayoutAmount;
        private TextView mPayoutDate;
        private TextView mPayoutBalance;

        public PayoutHolder(View itemView) {
            super(itemView);
            mCreditName = (TextView) itemView.findViewById(R.id.tvPayoutCreditName);
            mPayoutDate = (TextView) itemView.findViewById(R.id.tvPayoutDate);
            mPayoutAmount = (TextView) itemView.findViewById(R.id.tvPayoutAmount);
            mPayoutBalance = (TextView) itemView.findViewById(R.id.tvPayoutBalance);
        }

        public void bind(Payout payout) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            mCreditName.setText(payout.getCreditName());
            mPayoutAmount.setText(String.valueOf(payout.getAmount()));
            mPayoutDate.setText(sdf.format(payout.getDate()));
            mPayoutBalance.setText(payout.getBalance());
        }
    }

    public class MonthHolder extends RecyclerView.ViewHolder {
        private TextView mTvMonth;

        public MonthHolder(View itemView) {
            super(itemView);
            mTvMonth = (TextView) itemView.findViewById(R.id.tvItemMonth);
        }
        public void bind(String month) {
            mTvMonth.setText(month);
        }
    }

    public class DefaultHolder extends RecyclerView.ViewHolder {
        private TextView mTvDefault;
        public DefaultHolder(View itemView) {
            super(itemView);
            mTvDefault = (TextView) itemView.findViewById(R.id.tvDefault);
        }
    }


    private interface PayoutRVAdapterListener {
    }
}
