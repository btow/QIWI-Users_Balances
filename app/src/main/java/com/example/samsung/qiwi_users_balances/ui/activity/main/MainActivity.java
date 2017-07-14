package com.example.samsung.qiwi_users_balances.ui.activity.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.example.samsung.qiwi_users_balances.R;
import com.example.samsung.qiwi_users_balances.model.App;
import com.example.samsung.qiwi_users_balances.presentation.view.main.MainView;
import com.example.samsung.qiwi_users_balances.presentation.presenter.main.MainPresenter;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.samsung.qiwi_users_balances.ui.fragment.ServiceFragment;
import com.example.samsung.qiwi_users_balances.ui.fragment.recycler.RecyclerListFragment;

import java.io.IOError;

import static com.example.samsung.qiwi_users_balances.R.id.flPrimFragment;
import static com.example.samsung.qiwi_users_balances.R.id.flSecFragment;

public class MainActivity extends MvpAppCompatActivity implements MainView {

    public static final String TAG = "MainActivity";

    private int mUserID = 0;

    @InjectPresenter
    MainPresenter mMainPresenter;

    public static Intent getIntent(final Context context) {
        Intent intent = new Intent(context, MainActivity.class);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String tMsg = App.getApp().getString(R.string.error_in_the_method_on_create_of_main_activity);

        if (savedInstanceState != null) {

            mUserID = savedInstanceState.getInt(App.USER_ID);
            App.setCurUserID(mUserID);
        }
        App.setFragmentManager(getSupportFragmentManager());

        int activity_main = R.layout.activity_main;

        if (App.getUsedTwoFragmentLayout()) {
            activity_main = R.layout.activity_main_land;
        }
        setContentView(activity_main);

        try {
            App.setNextPrimUsedFragmentsNumber(App.LOADING_FRAGMENT_NUMBER);
        } catch (IllegalArgumentException e) {
            tMsg += (" " + e.getMessage());
            Toast.makeText(App.getApp(), tMsg, Toast.LENGTH_LONG);
            throw new IOError(e);
        }
        mMainPresenter.selectUsedFragmentsType(flPrimFragment);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        outState.putInt(App.USER_ID, mUserID);
    }

    @Override
    public void showUsersFragment() {

        RecyclerListFragment users = null;
        try {
            users = (RecyclerListFragment) App.getFragmentManager().findFragmentById(flPrimFragment);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        if (users == null) {
            users = RecyclerListFragment.newInstance();
        }
        App.getFragmentManager().beginTransaction().replace(flPrimFragment, users).commit();
    }

    @Override
    public void showBalancesFragment(Bundle args) {

        RecyclerListFragment balances = null;
        try {
            balances = (RecyclerListFragment) App.getFragmentManager().findFragmentById(flSecFragment);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        if (balances == null) {
            balances = RecyclerListFragment.newInstance(args.getInt(App.USER_ID));
        }
        App.getFragmentManager().beginTransaction().add(flSecFragment, balances).commit();
    }

    @Override
    public void showLoadingFragment(Bundle args) {

        ServiceFragment loading = null;
        int fragmentLayoutsNumber = args.getInt(App.FRAG_LAY_NUMBER);
        try {
            loading = (ServiceFragment) App.getFragmentManager().findFragmentById(fragmentLayoutsNumber);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

        if (loading == null) {
            loading = ServiceFragment.newInstance(args);
            App.getFragmentManager().beginTransaction().add(fragmentLayoutsNumber, loading).commit();
        } else {

            App.getFragmentManager().beginTransaction().replace(fragmentLayoutsNumber, loading).commit();
        }
    }

    @Override
    public void showMessageFragment(Bundle args) {

        ServiceFragment messag = null;
        try {
            messag = (ServiceFragment) App.getFragmentManager().findFragmentById(args.getInt(App.FRAG_LAY_NUMBER));
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

        if (messag == null) {
            messag = ServiceFragment.newInstance(args);
        }
        App.getFragmentManager().beginTransaction().replace(args.getInt(App.FRAG_LAY_NUMBER), messag).commit();
    }

    @Override
    public void setUserID(int userID) {
        mUserID = userID;
    }
}
