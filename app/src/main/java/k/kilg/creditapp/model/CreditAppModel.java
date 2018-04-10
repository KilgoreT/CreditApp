package k.kilg.creditapp.model;

import android.app.Fragment;
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
import k.kilg.creditapp.presenter.CreditAppPresenter;
import k.kilg.creditapp.view.fragments.CreditFragment;

/**
 * Created by apomazkin on 04.04.2018.
 * k.kilg.creditapp.model
 * 04.04.2018
 * 14:51
 */
public class CreditAppModel implements CreditAppModelInterface {

    private static final String USERS = "users";
    private List<Credit> credits;
    private FirebaseUser currentUser;
    private DatabaseReference dbRef;
    private CreditFragment fragment;

    public CreditAppModel(CreditFragment fragment) {
        this.fragment = fragment;
        credits = new ArrayList<>();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        dbRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(USERS)
                .child(currentUser.getUid());
        initDbListener();
    }


    @Override
    public List<Credit> getCredits() {
       return credits;
    }

    @Override
    public void addCredit(Credit credit) {
        dbRef
                .push()
                .setValue(credit);
    }

    @Override
    public void removeCredit(Credit credit) {
        Log.d("###", ">>" + getClass().getSimpleName() + ":removeCredit: " + credit.getName() + ":" + credit.getKey());
        dbRef
                .child(credit.getKey())
                .removeValue();
    }

    @Override
    public void updateCredit(Credit credit) {
        Log.d("###", ">>" + getClass().getSimpleName() + ":updateCredit: " + credit.getName() + ":" + credit.getKey());
        dbRef
                .child(credit.getKey())
                .updateChildren(credit.toMap());
    }

    @Override
    public void setData(List<Credit> data) {
        credits = data;
    }

    private void initDbListener() {
        dbRef
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Log.d("###", "onChildAdded:" + dataSnapshot.getKey());
                        //Credit credit = dataSnapshot.getValue(Credit.class);
                        //credit.setKey(dataSnapshot.getKey());
                        //credits.add(credit);
                        //fragment.getPresenter().loadCredits();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        Log.d("###", "onChildChanged:");
                        //todo обработать все варианты
                        Integer index = null;
                        Credit credit = dataSnapshot.getValue(Credit.class);
                        credit.setKey(dataSnapshot.getKey());
                        for (Credit c : credits) {
                            if (c.getKey().equals(dataSnapshot.getKey())) {
                                index = credits.indexOf(c);
                            }
                        }
                        if (index != null) {
                            credits.remove(index);
                            credits.add(index, credit);
                        }
                        fragment.getPresenter().loadCredits();
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        Log.d("###", "onChildRemoved:");
                        Credit credit = dataSnapshot.getValue(Credit.class);
                        credit.setKey(dataSnapshot.getKey());
                        credits.remove(credit);
                        ((CreditAppPresenter) fragment.getPresenter()).setCreditFromDB(credits);
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                        Log.d("###", "onChildMoved:");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("###", "onCancel");
                    }
                });
    }
}
