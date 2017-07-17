package com.example.samsung.qiwi_users_balances.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.samsung.qiwi_users_balances.R;
import com.example.samsung.qiwi_users_balances.model.App;
import com.example.samsung.qiwi_users_balances.presentation.presenter.ServicePresenter;
import com.example.samsung.qiwi_users_balances.presentation.view.ServiceView;
import com.example.samsung.qiwi_users_balances.ui.fragment.recycler.RecyclerListFragment;

public class ServiceFragment extends MvpAppCompatFragment implements ServiceView {

    public static final String TAG = "ServiceFragment";

    @InjectPresenter
    ServicePresenter mServicePresenter;

    private ProgressBar pbLoading;
    private LinearLayout llMsg;
    private TextView tvMsg;
    private Button btnRepeat;
    private Button btnContinue;

    public static ServiceFragment newInstance(Bundle args) {

        ServiceFragment fragment = new ServiceFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        int servicesVersion = getArguments().getInt(App.SERV_VERSION);
        // Inflate the layout for this fragment
        View fragmentService = inflater.inflate(servicesVersion, container, false);

        if (servicesVersion == App.SERV_VERSION_LOAD_FRAG) {

            pbLoading = (ProgressBar) fragmentService.findViewById(R.id.pbLoading);
            pbLoading.setIndeterminate(true);

        } else if (servicesVersion == App.SERV_VERSION_MESS_FRAG) {

            llMsg = (LinearLayout) fragmentService.findViewById(R.id.llMsg);
            llMsg.setVisibility(LinearLayout.VISIBLE);
            tvMsg = (TextView) fragmentService.findViewById(R.id.tvMsg);
            btnRepeat = (Button) fragmentService.findViewById(R.id.btnRepeat);
            btnRepeat.setOnClickListener(mServicePresenter.onClickMsgBtn(fragmentService));
            btnContinue = (Button) fragmentService.findViewById(R.id.btnContinue);
            btnContinue.setOnClickListener(mServicePresenter.onClickMsgBtn(fragmentService));

            if (!App.getDequeMsg().isEmpty()) {
                tvMsg.setText(App.getDequeMsg().outMsg());
            }
        }

        return fragmentService;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String excMsg = App.getApp().getString(R.string.error_in_the_method_on_view_created_of_service_fragment);

        try {
            mServicePresenter.serviceExecute(getArguments());
        } catch (InterruptedException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), (excMsg + " " + e.getMessage()), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showCallingScreen() {

        int users_id = getArguments().getInt(App.USER_ID);
        int call_from = getArguments().getInt(App.CALL_FROM);
        RecyclerListFragment users;

        switch (call_from) {

            case App.CALL_FROM_USERS_LIST:
            case App.CALL_FROM_BTN_USERS_LIST:

                users = null;
                try {
                    users = (RecyclerListFragment) App.getSupportFragmentManager().findFragmentById(R.id.flPrimFragment);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }
                if (users == null) {
                    users = RecyclerListFragment.newInstance();
                }
                App.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flPrimFragment, users)
                        .addToBackStack(null)
                        .commit();
                break;

            case App.CALL_FROM_BALANCES_LIST:
            case App.CALL_FROM_BTN_BALANCES_LIST:

                RecyclerListFragment balances = null;
                try {
                    balances = (RecyclerListFragment) App.getSupportFragmentManager().findFragmentById(R.id.flSecFragment);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }
                if (balances == null) {
                    balances = RecyclerListFragment.newInstance(users_id);
                }
                App.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flSecFragment, balances)
                        .addToBackStack(null)
                        .commit();
                break;

            default:
                break;
        }
    }

    @Override
    public void showNewMsg() {
        tvMsg.setText(App.getDequeMsg().outMsg());
        onResume();
    }
}