package k.kilg.creditapp.model;

import android.text.format.DateUtils;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import k.kilg.creditapp.R;
import k.kilg.creditapp.entities.Credit;
import k.kilg.creditapp.tools.CreditTools;
import k.kilg.creditapp.view.fragments.CreditFragment;

/**
 * Created by apomazkin on 04.04.2018.
 * k.kilg.creditapp.model
 * 04.04.2018
 * 14:51
 */
public class CreditModel implements CreditModelInterface {

    private static final String USERS_CHILD = "users";
    private List<Credit> mCreditList;
    private DatabaseReference dbRef;
    private CreditFragment fragment;

    public CreditModel(CreditFragment fragment) {
        this.fragment = fragment;
        mCreditList = new ArrayList<>();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        dbRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(USERS_CHILD)
                .child(currentUser.getUid());

}


    @Override
    public List<Credit> getCreditsList() {
       return mCreditList;
    }

    @Override
    public void addCredit(Credit credit) {
        dbRef
                .push()
                .setValue(credit);
    }
    
    public void getInitialData() {
        dbRef
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            Credit credit = snapshot.getValue(Credit.class);
                            credit.setKey(snapshot.getKey());
                            mCreditList.add(calculateNextPayment(credit));
                            sortCredits();
                        }
                        startDbListener();
                        fragment.getPresenter().loadCredits();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });
    }

    @Override
    public void removeCredit(Credit credit) {
        dbRef
                .child(credit.getKey())
                .removeValue();
    }

    @Override
    public void updateCredit(Credit credit) {
        dbRef
                .child(credit.getKey())
                .updateChildren(credit.toMap());
    }

    @Override
    public void startDbListener() {
        dbRef
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Credit credit = dataSnapshot.getValue(Credit.class);
                        credit.setKey(dataSnapshot.getKey());
                        if (!mCreditList.contains(credit)) {
                            mCreditList.add(calculateNextPayment(credit));
                            sortCredits();
                            fragment.getPresenter().loadCredits();
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        Credit credit = dataSnapshot.getValue(Credit.class);
                        credit.setKey(dataSnapshot.getKey());
                        int index = -1;
                        for (Credit c : mCreditList) {
                            if (c.getKey().equals(dataSnapshot.getKey())) {
                                index = mCreditList.indexOf(c);
                            }
                        }
                        if (index != -1) {
                            mCreditList.remove(index);
                            mCreditList.add(calculateNextPayment(credit));
                            sortCredits();
                            fragment.getPresenter().loadCredits();
                        }
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        Credit credit = dataSnapshot.getValue(Credit.class);
                        credit.setKey(dataSnapshot.getKey());
                        mCreditList.remove(credit);
                        fragment.getPresenter().loadCredits();
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                        fragment.getPresenter().loadCredits();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        fragment.getPresenter().loadCredits();
                    }
                });
    }

    private Credit calculateNextPayment(Credit credit) {


        BigDecimal ballance;
        BigDecimal nextMonthPayout;
        String nextPayoutDate;

        int paymentPeriod = CreditTools.getPaymentPeriod(credit);
        if (paymentPeriod == -1) {
            credit.setBallance(BigDecimal.ZERO);
            credit.setNextMonthPayout(BigDecimal.ZERO);
            credit.setNextPayoutDate(fragment.getActivity().getResources().getString(R.string.credit_paid));
            return credit;
        }

        nextPayoutDate = CreditTools.getNextPayoutDate(credit);
        if (credit.isAnnuity()) {
            nextMonthPayout = CreditTools.getAnnuityMonthPayoutAmount(credit);
            ballance = CreditTools.getAnnuityCreditBalance(credit, paymentPeriod);
        } else {
            nextMonthPayout = CreditTools.getDifferentialFullMonthPayout(credit, paymentPeriod);
            ballance = CreditTools.getDifferentialCreditBalance(credit, paymentPeriod);
        }
        credit.setBallance(ballance);
        credit.setNextMonthPayout(nextMonthPayout);
        credit.setNextPayoutDate(nextPayoutDate);
        return credit;
    }

    private void sortCredits() {
        Collections.sort(mCreditList, new Comparator<Credit>() {
            @Override
            public int compare(Credit o1, Credit o2) {
                String firstDate = o1.getNextPayoutDate();
                String secondDate = o2.getNextPayoutDate();
                int sComp = firstDate.compareTo(secondDate);
                if (sComp != 0) {
                    return sComp;
                }
                String firstName = o1.getName();
                String secondName = o2.getName();
                return firstName.compareTo(secondName);
            }
        });
    }
}
