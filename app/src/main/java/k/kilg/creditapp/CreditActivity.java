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
import k.kilg.creditapp.view.fragments.CreditFragment;

public class CreditActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        CreditFragment.OnCreditFragmentInteractionListener,
        AddCreditFragment.OnAddCreditFragmentInteractionListener{

    private static final String CREDIT_FRAGMENT_TAG = "CreditFragmentTag";
    private static final String ADD_CREDIT_FRAGMENT_TAG = "AddCreditFragmentTag";
    CreditFragment creditFragment = new CreditFragment();
    AddCreditFragment addCreditFragment = new AddCreditFragment();

    private FloatingActionButton fab;
    private NavigationView mNavigationView;
    private TextView mHeaderEmail;

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

        setFragment(creditFragment, CREDIT_FRAGMENT_TAG);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.creditFragment);
                if (fragment instanceof CreditFragment) {
                    setFragment(addCreditFragment, ADD_CREDIT_FRAGMENT_TAG);
                }
                if (fragment instanceof AddCreditFragment) {
                    ((AddCreditFragment) fragment).onFabPressed();
                }
            }
        });

        updateHeaderUI();
    }

    private void updateHeaderUI() {
        View header = mNavigationView.getHeaderView(0);
        mHeaderEmail = (TextView) header.findViewById(R.id.header_tvEmail);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        mHeaderEmail.setText(currentUser.getEmail());
    }



    private void setFragment(Fragment fragment, String tag) {
        updateFab(fragment);
        FragmentTransaction frTrans;
        Fragment currentFragment = getSupportFragmentManager()
                .findFragmentById(R.id.creditFragment);
        if (currentFragment != null) {
            frTrans = getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.creditFragment, fragment, tag);
            if (currentFragment instanceof AddCreditFragment) {
                frTrans.commit();
            } else {
                frTrans.addToBackStack(null)
                        .commit();
            }
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.creditFragment, fragment)
                    .commit();
        }
    }

    private void updateFab(Fragment fragment) {
        //todo: обработать null
        if (fab == null) {
            return;
        }
        if (fragment instanceof CreditFragment) {
            //fab.setVisibility(View.VISIBLE);
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_add));
        }
        if (fragment instanceof AddCreditFragment) {
            //fab.setVisibility(View.VISIBLE);
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_done));
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_credits) {
            setFragment(creditFragment, CREDIT_FRAGMENT_TAG);
        } else if (id == R.id.menu_payout) {
            //setFragment(ordersFragment, ORDERS_FRAGMENT_TAG);
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
        setFragment(creditFragment, CREDIT_FRAGMENT_TAG);
        creditFragment.addCredit(credit);
    }
}
