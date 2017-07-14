package com.example.samsung.qiwi_users_balances.presentation.view.main;

import android.os.Bundle;

import com.arellomobile.mvp.MvpView;

public interface MainView extends MvpView {

    public void showUsersFragment();

    public void showBalancesFragment(final Bundle args);

    public void showLoadingFragment(final Bundle args);

    public void showMessageFragment(final Bundle args);

    public void setUserID(final int userID);

}
