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
    private List<Credit> mCreditList;
    private FirebaseUser currentUser;
    private DatabaseReference dbRef;
    private CreditFragment fragment;

    public CreditAppModel(CreditFragment fragment) {
        this.fragment = fragment;
        mCreditList = new ArrayList<>();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        dbRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(USERS)
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
    public void initDbListener() {
        dbRef
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Log.d("###", "onChildAdded:" + dataSnapshot.getKey());
                        Credit credit = dataSnapshot.getValue(Credit.class);
                        credit.setKey(dataSnapshot.getKey());
                        Log.d("###", "Model:Listener find credit " + credit.getName());
                        mCreditList.add(credit);
                        fragment.getPresenter().loadCredits();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        Log.d("###", "onChildChanged:");
                        //todo обработать все варианты
                        Integer index = null;
                        Credit credit = dataSnapshot.getValue(Credit.class);
                        credit.setKey(dataSnapshot.getKey());
                        for (Credit c : mCreditList) {
                            if (c.getKey().equals(dataSnapshot.getKey())) {
                                index = mCreditList.indexOf(c);
                            }
                        }
                        if (index != null) {
                            mCreditList.remove(index);
                            mCreditList.add(index, credit);
                        }
                        fragment.getPresenter().loadCredits();
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        Log.d("###", "onChildRemoved:");
                        Credit credit = dataSnapshot.getValue(Credit.class);
                        credit.setKey(dataSnapshot.getKey());
                        Log.d("###", "Model:Listener remove credit " + credit.getName());
                        Log.d("###", "Model:Listener remove list size = " + mCreditList.size());
                        //todo: не удаляется  объект из листа
                        mCreditList.remove(credit);
                        Log.d("###", "Model:Listener remove list size = " + mCreditList.size());
                        fragment.getPresenter().loadCredits();
                        //((CreditAppPresenter) fragment.getPresenter()).setCreditFromDB(mCreditList);
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
