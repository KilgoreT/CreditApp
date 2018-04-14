package k.kilg.creditapp.model;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import k.kilg.creditapp.entities.Credit;
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
                            mCreditList.add(credit);
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
                            mCreditList.add(credit);
                            fragment.getPresenter().loadCredits();
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        Credit credit = dataSnapshot.getValue(Credit.class);
                        credit.setKey(dataSnapshot.getKey());
                        Integer index = null;
                        for (Credit c : mCreditList) {
                            if (c.getKey().equals(dataSnapshot.getKey())) {
                                index = mCreditList.indexOf(c);
                            }
                        }
                        if (index != null) {
                            mCreditList.get(index).setName(credit.getName());
                            mCreditList.get(index).setAmount(credit.getAmount());
                            mCreditList.get(index).setAnnuity(credit.isAnnuity());
                            mCreditList.get(index).setDate(credit.getDate());
                            mCreditList.get(index).setMonthCount(credit.getMonthCount());
                            mCreditList.get(index).setRate(credit.getRate());
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
}
