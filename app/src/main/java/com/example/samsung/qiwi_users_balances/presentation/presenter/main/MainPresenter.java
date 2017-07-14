package com.example.samsung.qiwi_users_balances.presentation.presenter.main;


import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

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

                private String tMsg = App.getApp().getString(R.string.error_in_the_constructor_main_presenter);

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    try {
                        App.setNextSecondUsedFragmentsNumber(App.LOADING_FRAGMENT_NUMBER);
                    } catch (IllegalArgumentException e) {
                        tMsg += e.getMessage();
                        Toast.makeText(App.getApp(), tMsg, Toast.LENGTH_LONG);
                        e.printStackTrace();
                    }
                    selectUsedFragmentsType(R.id.flSecFragment);
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
        getViewState().showLoadingFragment(args);
    }

    public void showMsgFragment(final Bundle args) {
        getViewState().showMessageFragment(args);
    }

    public void selectUsedFragmentsType(final int frameLayout) {

        Bundle args = new Bundle();

        int usedFragmentsVersion = 0;

        if (frameLayout == flPrimFragment) {

            usedFragmentsVersion = App.getNextPrimUsedFragmentsNumber();
        } else {

            usedFragmentsVersion = App.getNextSecondUsedFragmentsNumber();
        }

        switch (usedFragmentsVersion) {

            case App.USERS_FRAGMENT_NUMBER:
                showUsersFragment();
                break;
            case App.BALANCES_FRAGMENT_NUMBER:
                args.clear();
                args.putInt(App.FRAG_LAY_NUMBER, frameLayout);
                args.putInt(App.USER_ID, App.getCurUserID());
                showBalancesFragment(args);
                break;
            case App.LOADING_FRAGMENT_NUMBER:
                args.clear();

                if (frameLayout == flPrimFragment) {

                    args.putInt(App.CALL_FROM, App.CALL_FROM_USERS_LIST);
                } else {

                    args.putInt(App.CALL_FROM, App.CALL_FROM_BALANCES_LIST);
                }
                args.putInt(App.FRAG_LAY_NUMBER, frameLayout);
                args.putInt(App.SERV_VERSION, App.SERV_VERSION_LOAD_FRAG);
                showLoadFragment(args);
                break;
            case App.MESSAGE_FRAGMENT_NUMBER:
                args.clear();

                if (frameLayout == flPrimFragment) {

                    args.putInt(App.CALL_FROM, App.CALL_FROM_USERS_LIST);
                } else {

                    args.putInt(App.CALL_FROM, App.CALL_FROM_BALANCES_LIST);
                }
                args.putInt(App.FRAG_LAY_NUMBER, frameLayout);
                args.putInt(App.SERV_VERSION, App.SERV_VERSION_MESS_FRAG);
                showMsgFragment(args);
                break;
            default:
                break;
        }
    }
}
