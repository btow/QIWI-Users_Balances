package com.example.samsung.qiwi_users_balances.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.samsung.qiwi_users_balances.R;
import com.example.samsung.qiwi_users_balances.model.App;
import com.example.samsung.qiwi_users_balances.presentation.presenter.ServicePresenter;
import com.example.samsung.qiwi_users_balances.presentation.view.ServiceView;
import com.example.samsung.qiwi_users_balances.ui.activity.main.MainActivity;
import com.example.samsung.qiwi_users_balances.ui.fragment.recycler.RecyclerListFragment;

import java.util.concurrent.TimeUnit;

public class ServiceFragment extends MvpAppCompatFragment implements ServiceView {

    public static final String TAG = "ServiceFragment";

    @InjectPresenter
    ServicePresenter mServicePresenter;

    private ProgressBar pbLoading;
    private LinearLayout llMsg;
    private TextView tvMsg;
    private Button btnRepeat;
    private Button btnContinue;

    public static ServiceFragment newInstance(final Bundle args) {
        ServiceFragment fragment = new ServiceFragment();

        App.setServicesArguments(args);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(getArguments().getInt(App.SERV_VERSION), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments().getInt(App.SERV_VERSION) == App.SERV_VERSION_LOAD_FRAG) {

            pbLoading = (ProgressBar) view.findViewById(R.id.pbLoading);
            pbLoading.setIndeterminate(true);

            if (getArguments().containsKey(App.USER_ID)) {

                mServicePresenter.createListQiwiUsersBalances(
                        getArguments().getInt(App.USER_ID, 0)
                );
                App.setUsedPrimFragmentsVersion(App.BALANCES_FRAGMENT);
            } else {

                do {
                    try {
                        TimeUnit.MILLISECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } while (!App.getQiwiUsersListCreated());
                App.setUsedPrimFragmentsVersion(App.USERS_FRAMENT);
            }

        } else if (getArguments().getInt(App.SERV_VERSION) == App.SERV_VERSION_MESS_FRAG) {

            llMsg = (LinearLayout) view.findViewById(R.id.llMsg);
            llMsg.setVisibility(LinearLayout.VISIBLE);
            tvMsg = (TextView) view.findViewById(R.id.tvMsg);
            btnRepeat = (Button) view.findViewById(R.id.btnRepeat);
            btnRepeat.setOnClickListener(mServicePresenter.onClickRepeat(view));
            btnContinue = (Button) view.findViewById(R.id.btnContinue);
            btnContinue.setOnClickListener(mServicePresenter.onClickRepeat(view));

            if (!App.getDequeMsg().isEmpty()) {
                tvMsg.setText(App.getDequeMsg().outMsg());
            } else {
                showCallingScreen();
            }
        }
    }

    @Override
    public void showCallingScreen() {

        int users_id = getArguments().getInt(App.USER_ID);
        int call_from = getArguments().getInt(App.CALL_FROM);
        RecyclerListFragment users = null;

        switch (call_from) {

            case App.CALL_FROM_PRIM_FRAGMENT:
            case App.CALL_FROM_BTN_PRIM_FRAG:

                users = null;
                try {
                    users = (RecyclerListFragment) App.getFragmentManager().findFragmentById(R.id.flPrimFragment);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }
                if (users == null) {
                    users = RecyclerListFragment.newInstance();
                }
                App.getFragmentManager().beginTransaction().replace(R.id.flPrimFragment, users).commit();
                break;
            case App.CALL_FROM_SECOND_FRAGMENT:
            case App.CALL_FROM_BTN_SEC_FRAG:

                RecyclerListFragment balances = null;
                try {
                    balances = (RecyclerListFragment) App.getFragmentManager().findFragmentById(R.id.flSecFragment);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }
                if (balances == null) {
                    balances = RecyclerListFragment.newInstance(users_id);
                }
                App.getFragmentManager().beginTransaction().replace(R.id.flSecFragment, balances).commit();
                break;
            default:
                break;
        }
    }

    @Override
    public void setAppArguments() {
        App.setServicesArguments(getArguments());
    }

    @Override
    public void showNewMsg() {
        tvMsg.setText(App.getDequeMsg().outMsg());
        onResume();
    }
}
