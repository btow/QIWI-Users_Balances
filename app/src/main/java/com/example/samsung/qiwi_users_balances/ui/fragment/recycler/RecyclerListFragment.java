package com.example.samsung.qiwi_users_balances.ui.fragment.recycler;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.samsung.qiwi_users_balances.R;
import com.example.samsung.qiwi_users_balances.model.App;
import com.example.samsung.qiwi_users_balances.model.ListQiwiUsersAdapter;
import com.example.samsung.qiwi_users_balances.model.ListQiwiUsersBalancesAdapter;
import com.example.samsung.qiwi_users_balances.model.exceptions.DBCursorIsNullException;
import com.example.samsung.qiwi_users_balances.model.exceptions.DBIsNotDeletedException;
import com.example.samsung.qiwi_users_balances.presentation.presenter.recycler.RecyclerListPresenter;
import com.example.samsung.qiwi_users_balances.presentation.view.recycler.RecyclerListView;
import com.example.samsung.qiwi_users_balances.ui.fragment.ServiceFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.samsung.qiwi_users_balances.R.id.flPrimFragment;

public class RecyclerListFragment extends MvpAppCompatFragment implements RecyclerListView {
    public static final String TAG = "RecyclerListFragment";
    @InjectPresenter
    RecyclerListPresenter mRecyclerListPresenter;

    @BindView(R.id.btnExcheng)
    Button btnExcheng;
    @BindView(R.id.rvList)
    android.support.v7.widget.RecyclerView rvUsers;

    private boolean mDualPlane;
    private int mUserID = 0;
    private String mMsg = "";


    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Bundle args = new Bundle();
            args.putInt(App.CALL_FROM, App.CALL_FROM_BTN_PRIM_FRAG);
            args.putInt(App.FRAG_NUMBER, R.id.flPrimFragment);
            args.putInt(App.SERV_VERSION, App.SERV_VERSION_LOAD_FRAG);

            try {
                mRecyclerListPresenter.onClicExcheng();
            } catch (DBIsNotDeletedException | DBCursorIsNullException e) {
                e.printStackTrace();
                App.getDequeMsg().pushMsg(e.getMessage());
            } catch (Exception e2) {
                e2.printStackTrace();
                App.getDequeMsg().pushMsg(e2.getMessage());
            }
            args.putInt(App.SERV_VERSION, App.SERV_VERSION_MESS_FRAG);
            showMsg(args);
            rvUsers.getAdapter().notifyDataSetChanged();
        }
    };

    public static RecyclerListFragment newInstance() {
        RecyclerListFragment fragment = new RecyclerListFragment();

        Bundle args = new Bundle();
        App.setRecyclersArguments(args);
        fragment.setArguments(args);

        return fragment;
    }

    public static RecyclerListFragment newInstance(int userId) {
        RecyclerListFragment fragment = new RecyclerListFragment();

        Bundle args = new Bundle();
        args.putInt(App.USER_ID, userId);
        App.setCurUserID(userId);
        fragment.setArguments(args);
        App.setRecyclersArguments(args);

        return fragment;
    }

    public int getUsersId() {
        return mUserID;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_recycler_list, container, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            mUserID = savedInstanceState.getInt(App.USER_ID, 0);
        }

        ButterKnife.bind(this, view);

        rvUsers.setLayoutManager(new LinearLayoutManager(getContext()));

        if (getArguments().containsKey(App.USER_ID)) {

            ListQiwiUsersBalancesAdapter listQiwiUsersBalancesAdapter =
                    new ListQiwiUsersBalancesAdapter(mRecyclerListPresenter.getQiwiUsersBalancesListDataset());
            listQiwiUsersBalancesAdapter.SetOnItemClickListener(new ListQiwiUsersBalancesAdapter.OnItemClickListener() {

                @Override
                public void onItemClick(View view, int position) {

                    Toast.makeText(view.getContext(), mMsg, Toast.LENGTH_SHORT).show();
                }
            });
            rvUsers.setAdapter(listQiwiUsersBalancesAdapter);
            rvUsers.setHasFixedSize(true); //Фиксируем размер списка
        } else {

            ListQiwiUsersAdapter listQiwiUsersAdapter =
                    new ListQiwiUsersAdapter(mRecyclerListPresenter.getQiwiUsersListDataset());
            listQiwiUsersAdapter.SetOnItemClickListener(new ListQiwiUsersAdapter.OnItemClickListener() {

                @Override
                public void onItemClick(View view, int position) {

                    mMsg = "Balances of user \"" + mRecyclerListPresenter.getQiwiUsersListDataset().get(position).getName() + "\'";
                    Toast.makeText(view.getContext(), mMsg, Toast.LENGTH_SHORT).show();

                    mUserID = mRecyclerListPresenter.getQiwiUsersListDataset().get(position).getId();
                    App.getRecyclersArguments().putInt(App.USER_ID, mUserID);
                    Bundle args = new Bundle();
                    App.setCurUserID(mUserID);
                    args.putInt(App.USER_ID, App.getCurUserID());
                    args.putInt(App.FRAG_NUMBER, flPrimFragment);
                    args.putInt(App.CALL_FROM, App.CALL_FROM_PRIM_FRAGMENT);
                    showProgressBar(args);
                    //Обновить экран
                    showUsersBalances(mUserID);
                }
            });
            rvUsers.setAdapter(listQiwiUsersAdapter);
            rvUsers.setHasFixedSize(true); //Фиксируем размер списка
        }


        btnExcheng.setOnClickListener(mOnClickListener);

//        if (mDualPlane) {
//            showUsersBalances(mUserID);
//        }
    }

    @Override
    public void showProgressBar(Bundle args) {

        args.putInt(App.SERV_VERSION, App.SERV_VERSION_LOAD_FRAG);
        openServiceFragment(args);
    }

    @Override
    public void showMsg(Bundle args) {

        if (App.getDequeMsg().isEmpty()) {
            return;
        } else {
            args.putInt(App.SERV_VERSION, App.SERV_VERSION_MESS_FRAG);
            openServiceFragment(args);
        }
    }

    private void openServiceFragment(Bundle args) {

        int fragmentsVersion = R.id.flPrimFragment;

        if (args.getInt(App.CALL_FROM) == App.CALL_FROM_BTN_SEC_FRAG) {

            fragmentsVersion = R.id.flSecFragment;
        }

        ServiceFragment serviceFragment = null;
        try {
            serviceFragment = (ServiceFragment) App.getFragmentManager().findFragmentById(fragmentsVersion);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        if (serviceFragment == null) {

            serviceFragment = ServiceFragment.newInstance(args);
        }
        App.setUsedPrimFragmentsVersion(App.MESSAGE_FRAGMENT);
        args.putInt(App.SERV_VERSION, App.SERV_VERSION_MESS_FRAG);
        App.getFragmentManager().beginTransaction().replace(fragmentsVersion, serviceFragment).commit();
    }

    @Override
    public void showUsersBalances(int userId) {

        mUserID = userId;

//        if (mDualPlane) {
//        } else {
            App.setUsedPrimFragmentsVersion(App.BALANCES_FRAGMENT);

            int fragmentsVersion = R.id.flPrimFragment;

            RecyclerListFragment recyclerListFragment = null;
            try {
                recyclerListFragment = (RecyclerListFragment) App.getFragmentManager().findFragmentById(fragmentsVersion);
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
            if (recyclerListFragment == null) {

                recyclerListFragment = RecyclerListFragment.newInstance(userId);
            }
            App.getFragmentManager().beginTransaction().replace(fragmentsVersion, recyclerListFragment).commit();
//        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(App.USER_ID, mUserID);
    }
}
