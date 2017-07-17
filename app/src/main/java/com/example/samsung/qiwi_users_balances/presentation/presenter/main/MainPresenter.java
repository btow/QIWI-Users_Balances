package com.example.samsung.qiwi_users_balances.presentation.presenter.main;


import android.os.AsyncTask;
import android.os.Bundle;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.samsung.qiwi_users_balances.R;
import com.example.samsung.qiwi_users_balances.model.App;
import com.example.samsung.qiwi_users_balances.presentation.view.main.MainView;

import static com.example.samsung.qiwi_users_balances.R.id.flPrimFragment;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {

    public MainPresenter() {

        if (App.getUsedTwoFragmentLayout()) {

            AsyncTask<Void, Void, Void> twoFragmentLayoutTask = new AsyncTask<Void, Void, Void>() {

                private String tMsg = App.getApp().getString(R.string.error_in_the_constructor_main_presenter);

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    try {
                        App.setNextSecondUsedFragmentsNumber(App.LOADING_FRAGMENT_NUMBER);
                    } catch (IllegalArgumentException e) {
                        tMsg += e.getMessage();
                        throw new IllegalArgumentException(tMsg);
                    }
                    selectUsedFragmentsType(R.id.flSecFragment);
                }

                @Override
                protected Void doInBackground(Void... params) {
                    return null;
                }
            }.execute();
        }

    }

    public void showUsersFragment() {
        getViewState().showUsersFragment();
    }

    public void showBalancesFragment(Bundle args) {
        getViewState().showBalancesFragment(args);
    }

    public void showLoadFragment(Bundle args) {
        getViewState().showLoadingFragment(args);
    }

    public void showMsgFragment(Bundle args) {
        getViewState().showMessageFragment(args);
    }

    public void selectUsedFragmentsType(final int frameLayout) {

        Bundle fragmentsArgs = new Bundle();

        int usedFragmentsVersion = 0;

        usedFragmentsVersion = frameLayout == flPrimFragment ? App.getNextPrimUsedFragmentsNumber() : App.getNextSecondUsedFragmentsNumber();

        switch (usedFragmentsVersion) {

            case App.USERS_FRAGMENT_NUMBER:
                showUsersFragment();
                break;
            case App.BALANCES_FRAGMENT_NUMBER:
                fragmentsArgs.clear();
                fragmentsArgs.putInt(App.FRAG_LAY_NUMBER, frameLayout);
                fragmentsArgs.putInt(App.USER_ID, App.getCurUserID());
                showBalancesFragment(fragmentsArgs);
                break;
            case App.LOADING_FRAGMENT_NUMBER:
                fragmentsArgs.clear();

                if (frameLayout == flPrimFragment) {

                    fragmentsArgs.putInt(App.CALL_FROM, App.CALL_FROM_USERS_LIST);
                } else {

                    fragmentsArgs.putInt(App.CALL_FROM, App.CALL_FROM_BALANCES_LIST);
                }
                fragmentsArgs.putInt(App.FRAG_LAY_NUMBER, frameLayout);
                fragmentsArgs.putInt(App.SERV_VERSION, App.SERV_VERSION_LOAD_FRAG);
                showLoadFragment(fragmentsArgs);
                break;
            case App.MESSAGE_FRAGMENT_NUMBER:
                fragmentsArgs.clear();

                if (frameLayout == flPrimFragment) {

                    fragmentsArgs.putInt(App.CALL_FROM, App.CALL_FROM_USERS_LIST);
                } else {

                    fragmentsArgs.putInt(App.CALL_FROM, App.CALL_FROM_BALANCES_LIST);
                }
                fragmentsArgs.putInt(App.FRAG_LAY_NUMBER, frameLayout);
                fragmentsArgs.putInt(App.SERV_VERSION, App.SERV_VERSION_MESS_FRAG);
                showMsgFragment(fragmentsArgs);
                break;
            default:
                break;
        }
    }
}
