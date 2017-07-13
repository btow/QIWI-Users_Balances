package com.example.samsung.qiwi_users_balances.presentation.view.recycler;

import android.os.Bundle;

import com.arellomobile.mvp.MvpView;

public interface RecyclerListView extends MvpView {

    public void showProgressBar(Bundle args);

    public void showMsg(Bundle args);

    public void showUsersBalances(final int userId);
}
