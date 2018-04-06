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

/**
 * Created by apomazkin on 04.04.2018.
 * k.kilg.creditapp.model
 * 04.04.2018
 * 14:51
 */
public class CreditAppModel implements CreditAppModelInterface {

    private List<Credit> credits;
    private FirebaseUser currentUser;
    private DatabaseReference dbRef;

    public CreditAppModel() {
        credits = new ArrayList<>();
        dbRef = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        initDbListener();
        //this.credits = new ArrayList<>();
        //credits.add(new Credit("First", true, 24, 5000000, 10));
    }


    @Override
    public List<Credit> getCredits() {
        dbRef
                .child("users")
                .child(currentUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Credit credit = dataSnapshot.getValue(Credit.class);
                        credits.add(credit);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //todo: обработать ошибку
                    }
                });
        return credits;
    }

    @Override
    public void addCredit(Credit credit) {
        dbRef
                .child("users")
                .child(currentUser.getUid())
                .push()
                .setValue(credit);
        //credits.add(credit);
    }

    private void initDbListener() {
        dbRef
                .child("users")
                .child(currentUser.getUid())
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Credit credit = dataSnapshot.getValue(Credit.class);
                        credits.add(credit);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        Log.d("###", "onChildChanged:");
                        //todo обработать все варианты
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        Log.d("###", "onChildRemoved:");
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
