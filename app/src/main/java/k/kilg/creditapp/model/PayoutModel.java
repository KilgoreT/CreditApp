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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import k.kilg.creditapp.entities.Credit;
import k.kilg.creditapp.entities.Payout;
import k.kilg.creditapp.entities.Resume;
import k.kilg.creditapp.presenter.PayoutPresenterInterface;
import k.kilg.creditapp.tools.CreditTools;

/**
 * Created by apomazkin on 10.04.2018.
 * k.kilg.creditapp.model
 * 10.04.2018
 * 11:57
 */
public class PayoutModel implements PayoutModelInterface {

    private static final String USERS_CHILD = "users";
    private List<Credit> mCreditList = new ArrayList<>();
    private boolean loadedData;
    private DatabaseReference dbRef;

    public PayoutModel() {
        loadedData = false;
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        dbRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(USERS_CHILD)
                .child(currentUser.getUid());

    }

    public List<Credit> getCreditList() {
        return mCreditList;
    }

    public void loadCredits() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            return;
        }
        dbRef
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mCreditList.clear();
                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            Credit credit = snapshot.getValue(Credit.class);
                            credit.setKey(snapshot.getKey());
                            mCreditList.add(credit);
                        }
                        loadedData = true;
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    public Observable<List<Credit>> getPayouts() {
        Observable<Boolean> loadingDataStatus = Observable
                .create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                loadCredits();
                while (!loadedData) {
                    emitter.onNext(loadedData);
                }
                emitter.onComplete();
            }
        });
        return loadingDataStatus
                .subscribeOn(Schedulers.io())
                .skipWhile(new Predicate<Boolean>() {
                    @Override
                    public boolean test(Boolean aBoolean) throws Exception {
                        return !loadedData;
                    }
                })
                .flatMap(new Function<Boolean, ObservableSource<List<Credit>>>() {
                    @Override
                    public ObservableSource<List<Credit>> apply(Boolean aBoolean) throws Exception {
                        return Observable.fromArray(mCreditList);
                    }
                });
    }
}
