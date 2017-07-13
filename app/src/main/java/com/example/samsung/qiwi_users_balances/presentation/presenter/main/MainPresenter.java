package com.example.samsung.qiwi_users_balances.presentation.presenter.main;


import android.os.AsyncTask;
import android.os.Bundle;

import com.example.samsung.qiwi_users_balances.R;
import com.example.samsung.qiwi_users_balances.model.App;
import com.example.samsung.qiwi_users_balances.presentation.view.main.MainView;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import static com.example.samsung.qiwi_users_balances.R.id.flPrimFragment;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {

    public MainPresenter() {

        if (App.getUsedTwoFragmentLayout()) {

            AsyncTask<Void, Void, Void> twoFragmentLayoutTask = new AsyncTask<Void, Void, Void>() {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    App.setUsedSecondFragmentsVersion(App.BALANCES_FRAGMENT);
                    selectUsedNumFragmentsVersion(R.id.flSecFragment);
                }

                @Override
                protected Void doInBackground(Void... params) {
                    return null;
                }
            };
            twoFragmentLayoutTask.execute();
        }

    }

    public void showUsersFragment() {
        getViewState().showUsersFragment();
    }

    public void showBalancesFragment(final Bundle args) {
//        getViewState().setTypeActivityLayout();
        getViewState().showBalancesFragment(args);
    }

    public void showLoadFragment(final Bundle args) {
        getViewState().showProgressBar(args);
    }

    public void showMsgFragment(final Bundle args) {
        getViewState().showMessageFragment(args);
    }

    public void selectUsedNumFragmentsVersion(final int frameLayout) {

        Bundle args = new Bundle();

        int usedFragmentsVersion = 0;

        if (frameLayout == flPrimFragment) {

            usedFragmentsVersion = App.getUsedPrimFragmentsVersion();
        } else {

            usedFragmentsVersion = App.getUsedSecondFragmentsVersion();
        }

        switch (usedFragmentsVersion) {

            case App.USERS_FRAMENT:
                showUsersFragment();
                break;
            case App.BALANCES_FRAGMENT:
                args.clear();
                args.putInt(App.FRAG_NUMBER, frameLayout);
                args.putInt(App.USER_ID, App.getCurUserID());
                showBalancesFragment(args);
                break;
            case App.LOADING_FRAGMENT:
                args.clear();

                if (frameLayout == flPrimFragment) {

                    args.putInt(App.CALL_FROM, App.CALL_FROM_PRIM_FRAGMENT);
                } else {

                    args.putInt(App.CALL_FROM, App.CALL_FROM_SECOND_FRAGMENT);
                }
                args.putInt(App.FRAG_NUMBER, frameLayout);
                args.putInt(App.SERV_VERSION, App.SERV_VERSION_LOAD_FRAG);
                showLoadFragment(args);
                break;
            case App.MESSAGE_FRAGMENT:
                args.clear();

                if (frameLayout == flPrimFragment) {

                    args.putInt(App.CALL_FROM, App.CALL_FROM_PRIM_FRAGMENT);
                } else {

                    args.putInt(App.CALL_FROM, App.CALL_FROM_SECOND_FRAGMENT);
                }
                args.putInt(App.FRAG_NUMBER, frameLayout);
                args.putInt(App.SERV_VERSION, App.SERV_VERSION_MESS_FRAG);
                showMsgFragment(args);
                break;
            default:
                break;
        }
    }
}
