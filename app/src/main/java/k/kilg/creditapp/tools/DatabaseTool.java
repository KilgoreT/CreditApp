package k.kilg.creditapp.tools;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import k.kilg.creditapp.entities.Credit;
import k.kilg.creditapp.presenter.CreditAppPresenter;
import k.kilg.creditapp.presenter.CreditAppPresenterInterface;

/**
 * Created by apomazkin on 10.04.2018.
 * k.kilg.creditapp.tools
 * 10.04.2018
 * 17:28
 */
public class DatabaseTool {
    public static void startLoadingData(final CreditAppPresenterInterface presenter) {
        Log.d("###", "Tools: starting...");
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            return;
        }
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child("users")
                .child(currentUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<Credit> credits = new ArrayList<>();
                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            Credit credit = snapshot.getValue(Credit.class);
                            credit.setKey(snapshot.getKey());
                            Log.d("###", "Tools: credit = " + credit.getName());
                            credits.add(credit);
                            ((CreditAppPresenter)presenter).setCreditFromDB(credits);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("###", "Tools: canseled");
                    }
                });
    }
}
