package k.kilg.creditapp.view.adapters;

import android.graphics.Color;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import k.kilg.creditapp.CreditActivity;
import k.kilg.creditapp.R;
import k.kilg.creditapp.entities.Credit;
import k.kilg.creditapp.view.fragments.CreditFragment;

/**
 * Created by apomazkin on 04.04.2018.
 * k.kilg.creditapp.view.adapters
 * 04.04.2018
 * 15:18
 */

//todo: не вводятся проценты с точкой

public class CreditRVAdapter extends RecyclerView.Adapter<CreditRVAdapter.CreditHolder> {

    private List<Credit> mCredits;
    private List<Credit> mSelectedList = new ArrayList<>();
    private Boolean mModeEnabled = false;
    private CreditRVAdapterListener mListener;

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
    public void onBindViewHolder(final CreditRVAdapter.CreditHolder holder, final int position) {

        holder.bind(mCredits.get(position));
        holder.mCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mModeEnabled) {
                    holder.changeSelected(getCreditByTitle(holder.getCreditName()), position);
                }
            }
        });

        holder.mCV.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mListener.showActionMode(callback);
                if(mModeEnabled) {
                    holder.changeSelected(getCreditByTitle(holder.getCreditName()), position);
                }
                return true;
            }
        });
        if (mModeEnabled && mSelectedList.contains(getCreditByTitle(holder.getCreditName()))) {
            holder.setSelected(true);
        } else {
            holder.setSelected(false);
        }
    }

    private Credit getCreditByTitle(String name) {
        for (Credit credit : mCredits) {
            if (credit.getName().equals(name)) {
                return credit;
            }
        }
        return null;
    }

    @Override
    public int getItemCount() {
        if (mCredits != null) {
            Log.d("###", ">>" + getClass().getSimpleName() + "getItemCount mCredit.size() = " + mCredits.size());
            return mCredits.size();
        }
        return 0;
    }

    public void removeCredit(Credit credit) {
        mCredits.remove(credit);
        notifyDataSetChanged();
    }

    public class CreditHolder extends RecyclerView.ViewHolder {

        private CardView mCV;
        private TextView mCreditName;
        private TextView mCreditType;
        private TextView mCreditMonthCount;
        private TextView mCreditAmount;
        private TextView mCreditRate;
        private TextView mCreditDate;

        public CreditHolder(View itemView) {
            super(itemView);
            mCV = (CardView) itemView.findViewById(R.id.cardview);
            mCreditName = (TextView) itemView.findViewById(R.id.tvCreditName);
            mCreditType = (TextView) itemView.findViewById(R.id.tvCreditType);
            mCreditMonthCount = (TextView) itemView.findViewById(R.id.tvCreditMonthCount);
            mCreditAmount = (TextView) itemView.findViewById(R.id.tvCreditAmount);
            mCreditRate = (TextView) itemView.findViewById(R.id.tvCreditRate);
            mCreditDate = (TextView) itemView.findViewById(R.id.tvCreditDate);
        }

        public String getCreditName() {
            return mCreditName.getText().toString();
        }

        public void bind(Credit credit) {
            mCreditName.setText(credit.getName());
            mCreditType.setText(credit.isAnnuity() ? "Annuity" : "Differential");
            mCreditMonthCount.setText("" + credit.getMonthCount() + " months");
            mCreditAmount.setText("Amount: " + credit.getAmount());
            mCreditRate.setText("Rate: " + credit.getRate());
            mCreditDate.setText("Date:" + credit.getDate());
        }

        public void changeSelected(Credit selectedCredit, int position) {
            if (mSelectedList.contains(selectedCredit)) {
                mSelectedList.remove(selectedCredit);
            } else {
                mSelectedList.add(selectedCredit);
            }
            notifyItemChanged(position);
        }

        public void setSelected(boolean selected) {
            if (selected) {
                mCV.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.colorPrimaryDark));
            } else {
                mCV.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.et_background));
            }
        }
    }

    public void setListener(final CreditRVAdapterListener listener) {
        mListener = listener;
    }

    ActionMode.Callback callback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.action_mode_menu, menu);
            mModeEnabled = true;
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_remove:
                    for (Credit credit : mSelectedList) {
                        mListener.removeCredit(credit);
                    }
                    mSelectedList.clear();
                    notifyDataSetChanged();
                    mode.finish();
                    break;
                case R.id.menu_edit:
                    mSelectedList.clear();
                    mode.finish();
                    break;
            }
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mModeEnabled = false;
            mSelectedList.clear();
            notifyDataSetChanged();
            ((CreditActivity)((CreditFragment) mListener).getActivity()).setFabVisible(true);
        }
    };

    public interface CreditRVAdapterListener {
        void removeCredit(Credit credit);
        void showActionMode(ActionMode.Callback callback);
    }


}
