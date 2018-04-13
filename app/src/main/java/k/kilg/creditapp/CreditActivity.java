package k.kilg.creditapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import k.kilg.creditapp.entities.Credit;
import k.kilg.creditapp.view.fragments.AddCreditFragment;
import k.kilg.creditapp.view.fragments.CreditFragment;
import k.kilg.creditapp.view.fragments.PayoutFragment;

public class CreditActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        CreditFragment.OnCreditFragmentInteractionListener,
        AddCreditFragment.OnAddCreditFragmentInteractionListener {

    private static final String CREDIT_FRAGMENT_TAG = "CreditFragmentTag";
    private static final String ADD_CREDIT_SIMPLE_FRAGMENT_TAG = "AddCreditSimpleFragmentTag";
    private static final String CURRENT_FRAGMENT_KEY = "CurrentFragmentKey";
    private static final String PAYOUT_FRAGMENT_TAG = "PayoutFragmentTag";

    private static final String CREDIT_NAME_KEY = "CreditNameKey";
    private static final String CREDIT_AMOUNT_KEY = "CreditAmountKey";
    private static final String CREDIT_MONTH_COUNT_KEY = "CreditMonthCountKey";
    private static final String CREDIT_RATE_KEY = "CreditRateKey";
    private static final String CREDIT_DATE_KEY = "CreditDateKey";
    private static final String CREDIT_DATABASE_KEY = "CreditDatabaseKey";
    private static final String CREDIT_TYPE_KEY = "CreditTypeKey";

    CreditFragment creditFragment = new CreditFragment();
    AddCreditFragment addCreditFragment = new AddCreditFragment();
    PayoutFragment payoutFragment = new PayoutFragment();


    private FloatingActionButton fab;
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            setFragment(creditFragment, CREDIT_FRAGMENT_TAG);
        }

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.creditFragment);
                if (fragment instanceof CreditFragment) {
                    setFragment(addCreditFragment, ADD_CREDIT_SIMPLE_FRAGMENT_TAG);
                } else if (fragment instanceof AddCreditFragment) {
                    ((AddCreditFragment) fragment).onFabPressed();
                }
            }
        });

        updateHeaderUI();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.creditFragment);
        if (fragment instanceof CreditFragment) {
            outState.putString(CURRENT_FRAGMENT_KEY, CREDIT_FRAGMENT_TAG);
        }
        if (fragment instanceof AddCreditFragment) {
            outState.putString(CURRENT_FRAGMENT_KEY, ADD_CREDIT_SIMPLE_FRAGMENT_TAG);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String currentFragmentTag = savedInstanceState.getString(CURRENT_FRAGMENT_KEY);
        if (currentFragmentTag != null && currentFragmentTag.equals(CREDIT_FRAGMENT_TAG)) {
            setFragment(creditFragment, CREDIT_FRAGMENT_TAG);
        } else if (currentFragmentTag != null && currentFragmentTag.equals(ADD_CREDIT_SIMPLE_FRAGMENT_TAG)) {
            setFragment(addCreditFragment, ADD_CREDIT_SIMPLE_FRAGMENT_TAG);
        }
    }

    private void updateHeaderUI() {
        View header = mNavigationView.getHeaderView(0);
        TextView mHeaderEmail = (TextView) header.findViewById(R.id.header_tvEmail);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String emailWithStatus = currentUser.getEmail() + "(" + (currentUser.isEmailVerified()
                    ? getString(R.string.ac_credit_header_verifyed)
                    :getString(R.string.ac_credit_header_noverify));
            mHeaderEmail.setText(emailWithStatus);
        }
    }



    private void setFragment(Fragment fragment, String tag) {
        updateFab(fragment);
        Fragment currentFragment = getSupportFragmentManager()
                .findFragmentById(R.id.creditFragment);
        if (currentFragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.creditFragment, fragment, tag)
                        .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.creditFragment, fragment)
                    .commit();
        }
    }

    private void updateFab(Fragment fragment) {
        if (fab == null || fragment == null) {
            return;
        } else if (fragment instanceof CreditFragment) {
            setFabVisible(true);
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_add));
        } else if (fragment instanceof AddCreditFragment) {
            setFabVisible(true);
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_done));
        } else if (fragment instanceof PayoutFragment) {
            setFabVisible(false);
        }
    }

    public void setFabVisible(boolean visible) {
        if (visible) {
            fab.setVisibility(View.VISIBLE);
        } else {
            fab.setVisibility(View.GONE);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_credits) {
            setFragment(creditFragment, CREDIT_FRAGMENT_TAG);
        } else if (id == R.id.menu_payout) {
            setFragment(payoutFragment, PAYOUT_FRAGMENT_TAG);
        } else if (id == R.id.menu_signout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, LaunchActivity.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onAddCreditFragmentClose(Credit credit) {
        if (credit != null) {
            setFragment(creditFragment, CREDIT_FRAGMENT_TAG);
            creditFragment.addCredit(credit);
        }
    }

    @Override
    public void onUpdateCreditFragmentClose(Credit credit) {
        if (credit != null && credit.getKey() != null) {
            setFragment(creditFragment, CREDIT_FRAGMENT_TAG);
            creditFragment.updateCredit(credit);
        }
    }

    @Override
    public void startEditCredit(Credit credit) {
        if (addCreditFragment !=  null && credit != null) {
            Bundle bundle = new Bundle();
            bundle.putString(CREDIT_NAME_KEY, credit.getName());
            bundle.putString(CREDIT_AMOUNT_KEY, String.valueOf(credit.getAmount()));
            bundle.putString(CREDIT_MONTH_COUNT_KEY, String.valueOf(credit.getMonthCount()));
            bundle.putString(CREDIT_RATE_KEY, String.valueOf(credit.getRate()));
            bundle.putString(CREDIT_DATE_KEY, credit.getDate());
            bundle.putString(CREDIT_DATABASE_KEY, credit.getKey());
            bundle.putBoolean(CREDIT_TYPE_KEY, credit.isAnnuity());
            addCreditFragment.setArguments(bundle);
            setFragment(addCreditFragment, ADD_CREDIT_SIMPLE_FRAGMENT_TAG);
        }
    }

}
