package com.example.samsung.qiwi_users_balances.presentation.presenter.recycler;

import android.os.Bundle;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.samsung.qiwi_users_balances.model.App;
import com.example.samsung.qiwi_users_balances.model.QiwiUsers;
import com.example.samsung.qiwi_users_balances.model.QiwiUsersBalances;
import com.example.samsung.qiwi_users_balances.presentation.view.recycler.RecyclerListView;

import java.util.List;

@InjectViewState
public class RecyclerListPresenter extends MvpPresenter<RecyclerListView> {

    public List<QiwiUsers> getQiwiUsersListDataset() {

        return App.getQiwiUsersList();
    }

    public List<QiwiUsersBalances> getQiwiUsersListBalancesDataset() {

        return null;
    }

    public void onClicExcheng() throws Exception {

        Bundle args = new Bundle();
        args.putInt(App.CALL_FROM, App.CALL_FROM_BTN_PRIM_FRAG);
        getViewState().showProgressBar(args);
    }

    public void showProgressBar(Bundle args) {

        getViewState().showProgressBar(args);
    }

    public List<QiwiUsersBalances> getQiwiUsersBalancesListDataset() {
        return App.getQiwiUsersBalancesList();
    }
}
