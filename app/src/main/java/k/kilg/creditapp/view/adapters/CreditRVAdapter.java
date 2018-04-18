package k.kilg.creditapp.view.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_credit_item_nextpayout, parent, false);
        return new CreditHolder(v);
    }

    @Override
    public void onBindViewHolder(final CreditRVAdapter.CreditHolder holder, int position) {

        holder.bind(mCredits.get(position));
        holder.mCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mModeEnabled) {
                    holder.changeSelectedList(getCreditByTitle(holder.getCreditName()), holder.getAdapterPosition());
                } else {
                    ((CreditFragment)mListener).startDiagramForCredit(getCreditByTitle(holder.getCreditName()));
                }
            }
        });

        holder.mCV.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mListener.showActionMode(callback);
                if(mModeEnabled) {
                    holder.changeSelectedList(getCreditByTitle(holder.getCreditName()), holder.getAdapterPosition());
                }
                return true;
            }
        });
        if (mModeEnabled && mSelectedList.contains(getCreditByTitle(holder.getCreditName()))) {
            holder.setSelectedBackground(true);
        } else {
            holder.setSelectedBackground(false);
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
            return mCredits.size();
        }
        return 0;
    }

    public class CreditHolder extends RecyclerView.ViewHolder {

        private CardView mCV;
        private TextView mCreditName;
        private TextView mNextPayoutDate;
        private TextView mNextMonthPayout;
        private TextView mBallance;
        private ConstraintLayout mCL;

        CreditHolder(View itemView) {
            super(itemView);
            mCV = (CardView) itemView.findViewById(R.id.cardview);
            mCL = (ConstraintLayout) itemView.findViewById(R.id.CL);
            mCreditName = (TextView) itemView.findViewById(R.id.tvCreditName);
            mNextPayoutDate = (TextView) itemView.findViewById(R.id.tvNextPayoutDate);
            mNextMonthPayout = (TextView) itemView.findViewById(R.id.tvNextMonthPayout);
            mBallance = (TextView) itemView.findViewById(R.id.tvBallance);
        }

        public String getCreditName() {
            return mCreditName.getText().toString();
        }

        void bind(Credit credit) {
            mCreditName.setText(credit.getName());
            mNextPayoutDate.setText(credit.getNextPayoutDate());
            mNextMonthPayout.setText(credit.getNextMonthPayout());
            mBallance.setText(credit.getBallance());
        }

        public void changeSelectedList(Credit selectedCredit, int position) {
            if (mSelectedList.contains(selectedCredit)) {
                mSelectedList.remove(selectedCredit);
            } else {
                mSelectedList.add(selectedCredit);
            }
            notifyItemChanged(position);
        }

        void setSelectedBackground(boolean selected) {
            if (selected) {
                mCL.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.background_dark));
            } else {
                mCL.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.white));
            }
        }
    }

    public void setListener(final CreditRVAdapterListener listener) {
        mListener = listener;
    }

    private ActionMode.Callback callback = new ActionMode.Callback() {
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
        public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
            Context context = ((CreditFragment)mListener).getContext();
            switch (item.getItemId()) {
                case R.id.menu_remove:
                    if (mSelectedList.size() == 0) {
                        ((CreditFragment)mListener)
                                .showSnackbar(context.getString(R.string.credit_adapter_no_element_to_remove));
                        break;
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    String alertMessage = context.getResources().getString(R.string.alert_confirm_deletion_message);
                    if (mSelectedList.size() == 1) {
                        alertMessage = alertMessage + " " + mSelectedList.size()  + " " + context.getResources().getString(R.string.alert_confirm_deletion_credit);
                    } else if (mSelectedList.size() > 1) {
                        alertMessage = alertMessage + " " + mSelectedList.size()  + " " + context.getResources().getString(R.string.alert_confirm_deletion_credits);
                    }
                    builder
                            .setTitle(R.string.alert_confirm_deletion_title)
                            .setCancelable(true)
                            .setMessage(alertMessage)
                            .setPositiveButton(R.string.btnYes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    for (Credit credit : mSelectedList) {
                                        mListener.removeCredit(credit);
                                    }
                                    mSelectedList.clear();
                                    notifyDataSetChanged();
                                    mode.finish();
                                }
                            })
                            .setNegativeButton(R.string.btnNo, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mSelectedList.clear();
                                    notifyDataSetChanged();
                                    mode.finish();
                                }
                            })
                            .create()
                            .show();
                    break;
                case R.id.menu_edit:
                    if (mSelectedList.size() == 0 || mSelectedList.size() > 1) {
                        ((CreditFragment)mListener).showSnackbar(context.getString(R.string.credit_adapter_choose_one_element));
                        break;
                    } else {
                        ((CreditFragment)mListener).startEditCredit(mSelectedList.get(0));
                    }
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
