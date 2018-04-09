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
import k.kilg.creditapp.view.fragments.CreditFragment;

/**
 * Created by apomazkin on 04.04.2018.
 * k.kilg.creditapp.model
 * 04.04.2018
 * 14:51
 */
public class CreditAppModel implements CreditAppModelInterface {

    private static final String USERS = "users";
    List<Credit> credits;
    private FirebaseUser currentUser;
    private DatabaseReference dbRef;
    private CreditFragment fragment;

    public CreditAppModel(CreditFragment fragment) {
        this.fragment = fragment;
        credits = new ArrayList<>();
        dbRef = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        initDbListener();
    }


    @Override
    public List<Credit> getCredits() {
        Log.d("###", ">>" + getClass().getSimpleName() + ":getCredits");
       /* dbRef
                .child(USERS)
                .child(currentUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("###", ">>" + getClass().getSimpleName() + ":getCredits onDataChange: credits.size = " + credits.size());
                        for (DataSnapshot creditSnapshot : dataSnapshot.getChildren()) {
                            Credit credit = creditSnapshot.getValue(Credit.class);
                            credits.add(credit);
                            Log.d("###", ">>" + getClass().getSimpleName() + ":getCredits onDataChange iter: credits.size = " + credits.size());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });*/
        //Log.d("###", ">>" + getClass().getSimpleName() + ":getCredits after callback: credits.size = " + credits.size());
       /*if (credits.size() == 0) {
           return null;
       }*/

        Log.d("###", ">>" + getClass().getSimpleName() + ":getCredits size = " + credits.size());
       return credits;
    }

    @Override
    public void addCredit(Credit credit) {
        dbRef
                .child(USERS)
                .child(currentUser.getUid())
                .push()
                .setValue(credit);
    }

    @Override
    public void removeCredit(Credit credit) {
        Log.d("###", ">>" + getClass().getSimpleName() + ":removeCredit: " + credit.getName() + ":" + credit.getKey());
        dbRef
                .child("users")
                .child(currentUser.getUid())
                .child(credit.getKey())
                .removeValue();
    }

    @Override
    public void updateCredit(Credit credit) {
        Log.d("###", ">>" + getClass().getSimpleName() + ":updateCredit: " + credit.getName() + ":" + credit.getKey());
    }

    public void delCredit(Credit credit) {
        boolean deleted = credits.remove(credit);
        Log.d("###", ">>" + getClass().getSimpleName() + "is deleted in method:" + deleted);
    }

    private void initDbListener() {
        dbRef
                .child("users")
                .child(currentUser.getUid())
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Log.d("###", "onChildAdded:" + dataSnapshot.getKey());
                        Credit credit = dataSnapshot.getValue(Credit.class);
                        credit.setKey(dataSnapshot.getKey());
                        credits.add(credit);
                        fragment.getPresenter().loadCredits();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        Log.d("###", "onChildChanged:");
                        //todo обработать все варианты
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        Log.d("###", "onChildRemoved:");
                        /*Credit credit = dataSnapshot.getValue(Credit.class);
                        Log.d("###", ">>" + getClass().getSimpleName() + "remove credit name is: " + credit.getName());
                        Log.d("###", ">>" + getClass().getSimpleName() + "before list credits:");

                        for (Credit credit1: credits) {
                            Log.d("###", ">>" + getClass().getSimpleName() + "before list: " + credit1.getName());
                        }

                        Log.d("###", ">>" + getClass().getSimpleName() + "credits size:" + credits.size());

                        boolean deleted = credits.remove(credit);
                        Log.d("###", ">>" + getClass().getSimpleName() + "is deleted:" + deleted);


                        Log.d("###", ">>" + getClass().getSimpleName() + "after list credits:");
                        for (Credit credit1: credits) {
                            Log.d("###", ">>" + getClass().getSimpleName() + "after list: " + credit1.getName());
                        }*/
                        fragment.getPresenter().loadCredits();
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
