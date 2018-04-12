package k.kilg.creditapp.model;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
import k.kilg.creditapp.tools.CreditTools;

/**
 * Created by apomazkin on 10.04.2018.
 * k.kilg.creditapp.model
 * 10.04.2018
 * 11:57
 */
public class PayoutModel implements PayoutModelInterface {
    private List<Payout> mPayouts = new ArrayList<>();
    private List<Credit> mCredits = new ArrayList<>();
    boolean loadedData = false;

    public PayoutModel() {

    }


    public void loadCredits() {
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
                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            Credit credit = snapshot.getValue(Credit.class);
                            credit.setKey(snapshot.getKey());
                            Log.d("###", getClass().getCanonicalName() + ":onDataChange: credit = " + credit.getName());
                            mCredits.add(credit);
                            Log.d("###", getClass().getCanonicalName() + ":onDataChange: credit.size = " + mCredits.size());
                        }
                        loadedData = true;
                        Log.d("###", getClass().getCanonicalName() + ":onDataChange: loadedData = " + loadedData);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("###", getClass().getCanonicalName() + ":onCancelled: " + databaseError.getMessage());
                    }
                });
    }

    public Observable<List<Object>> getPayouts() {
        Observable<Boolean> tratata = Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                while (!loadedData) {
                    emitter.onNext(loadedData);
                }
                emitter.onComplete();
            }
        });
        return tratata
                .skipWhile(new Predicate<Boolean>() {
                    @Override
                    public boolean test(Boolean aBoolean) throws Exception {
                        return !loadedData;
                    }
                })
                .flatMap(new Function<Boolean, ObservableSource<List<Credit>>>() {
                    @Override
                    public ObservableSource<List<Credit>> apply(Boolean aBoolean) throws Exception {
                        return Observable.fromArray(mCredits);
                    }
                })
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<List<Credit>, ObservableSource<List<Object>>>() {
                    @Override
                    public ObservableSource<List<Object>> apply(List<Credit> credits) throws Exception {
                        List<Payout> payoutList = new ArrayList<>();
                        for (Credit credit: credits) {
                            if (credit.isAnnuity()) {
                                payoutList.addAll(CreditTools.getAnnuityMonthPayouts(credit));
                            } else {
                                payoutList.addAll(CreditTools.getDifferentialMonthPayouts(credit));
                            }
                        }
                        Collections.sort(payoutList, new Comparator<Payout>() {
                            @Override
                            public int compare(Payout o1, Payout o2) {
                                Date firstDate = o1.getDate();
                                Date secondDate = o2.getDate();
                                int sComp = firstDate.compareTo(secondDate);
                                if (sComp != 0) {
                                    return sComp;
                                }
                                String firstName = o1.getCreditName();
                                String secondName = o2.getCreditName();
                                return firstName.compareTo(secondName);
                            }
                        });
                        //Collections.sort(payoutList);

                        return Observable.fromArray((List<Object>) new ArrayList<Object>(prepareList(payoutList)) {
                        });
                    }
                });
    }
    public List<Object> prepareList (List<Payout> payoutList) {
        List<Object> preparedList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        int month = -1;
        for (Payout payout: payoutList) {
            Date date = payout.getDate();
            calendar.setTime(date);
            if (month != calendar.get(Calendar.MONTH)) {
                month = calendar.get(Calendar.MONTH);
                preparedList.add(CreditTools.getMonthName(month));
            }
            preparedList.add(payout);

        }
        return preparedList;
    }
}
