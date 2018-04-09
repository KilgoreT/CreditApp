package k.kilg.creditapp;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import k.kilg.creditapp.entities.Credit;
import k.kilg.creditapp.view.fragments.AddCreditFragment;
import k.kilg.creditapp.view.fragments.AddCreditSimpleFragment;
import k.kilg.creditapp.view.fragments.CreditFragment;

public class CreditActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        CreditFragment.OnCreditFragmentInteractionListener,
        AddCreditFragment.OnAddCreditFragmentInteractionListener,
        AddCreditSimpleFragment.OnAddCreditSimpleFragmentInteractionListener {

    private static final String CREDIT_FRAGMENT_TAG = "CreditFragmentTag";
    private static final String ADD_CREDIT_FRAGMENT_TAG = "AddCreditFragmentTag";
    private static final String ADD_CREDIT_SIMPLE_FRAGMENT_TAG = "AddCreditSimpleFragmentTag";
    private static final String CURRENT_FRAGMENT_KEY = "CurrentFragmentKey";
    CreditFragment creditFragment = new CreditFragment();
    AddCreditFragment addCreditFragment = new AddCreditFragment();
    AddCreditSimpleFragment addCreditSimpleFragment = new AddCreditSimpleFragment();


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
                    setFragment(addCreditSimpleFragment, ADD_CREDIT_SIMPLE_FRAGMENT_TAG);
                } else if (fragment instanceof AddCreditFragment) {
                    ((AddCreditFragment) fragment).onFabPressed();
                } else if (fragment instanceof AddCreditSimpleFragment) {
                    ((AddCreditSimpleFragment) fragment).onFabPressed();
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
            outState.putString(CURRENT_FRAGMENT_KEY, ADD_CREDIT_FRAGMENT_TAG);
        }
    }

    /**
     * This method is called after {@link #onStart} when the activity is
     * being re-initialized from a previously saved state, given here in
     * <var>savedInstanceState</var>.  Most implementations will simply use {@link #onCreate}
     * to restore their state, but it is sometimes convenient to do it here
     * after all of the initialization has been done or to allow subclasses to
     * decide whether to use your default implementation.  The default
     * implementation of this method performs a restore of any view state that
     * had previously been frozen by {@link #onSaveInstanceState}.
     * <p>
     * <p>This method is called between {@link #onStart} and
     * {@link #onPostCreate}.
     *
     * @param savedInstanceState the data most recently supplied in {@link #onSaveInstanceState}.
     * @see #onCreate
     * @see #onPostCreate
     * @see #onResume
     * @see #onSaveInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String currentFragmentTag = savedInstanceState.getString(CURRENT_FRAGMENT_KEY);
        if (currentFragmentTag != null && currentFragmentTag.equals(CREDIT_FRAGMENT_TAG)) {
            setFragment(creditFragment, CREDIT_FRAGMENT_TAG);
        } else if (currentFragmentTag != null && currentFragmentTag.equals(ADD_CREDIT_FRAGMENT_TAG)) {
            setFragment(addCreditFragment, ADD_CREDIT_FRAGMENT_TAG);
        }
    }

    private void updateHeaderUI() {
        View header = mNavigationView.getHeaderView(0);
        TextView mHeaderEmail = (TextView) header.findViewById(R.id.header_tvEmail);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String emailWithStatus = currentUser.getEmail() + "(" + (currentUser.isEmailVerified()? "verified)":"no verify)");
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
        if (fab == null || fragment != null) {
            return;
        } else if (fragment instanceof CreditFragment) {
            setFabVisible(true);
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_add));
        } else if (fragment instanceof AddCreditFragment) {
            setFabVisible(true);
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_done));
        } else if (fragment instanceof AddCreditSimpleFragment) {
            fab.setVisibility(View.VISIBLE);
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_done));
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
    public void onCreditFragmentInteraction(Uri uri) {

    }

    @Override
    public void onAddCreditFragmentClose(Credit credit) {
        if (credit != null) {
            setFragment(creditFragment, CREDIT_FRAGMENT_TAG);
            creditFragment.addCredit(credit);
        } else {
            Log.d("###", ">>" + getClass().getSimpleName() + ":onAddCreditFragmentClose credit == null");
        }
    }

    @Override
    public void onAddCreditSimpleFragmentClose(Credit credit) {
        if (credit != null) {
            setFragment(creditFragment, CREDIT_FRAGMENT_TAG);
            creditFragment.addCredit(credit);
        } else {
            Log.d("###", ">>" + getClass().getSimpleName() + ":onAddCreditSimpleFragmentClose credit == null");
        }
    }
}
