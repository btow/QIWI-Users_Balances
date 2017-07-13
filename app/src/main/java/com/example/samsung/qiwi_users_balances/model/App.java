package com.example.samsung.qiwi_users_balances.model;

import android.app.Application;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.example.samsung.qiwi_users_balances.R;

import java.util.ArrayList;
import java.util.List;

public class App extends Application {

    public static final String
            USER_ID = "user_id",
            FRAG_NUMBER = "frag_number",
            SERV_VERSION = "serv_version",
            CALL_FROM = "call_from";

    public static final int
            CALL_FROM_PRIM_FRAGMENT = 0,
            CALL_FROM_SECOND_FRAGMENT = 1,
            CALL_FROM_BTN_PRIM_FRAG = 2,
            CALL_FROM_BTN_SEC_FRAG = 3,
            SERV_VERSION_LOAD_FRAG = R.layout.fragment_loading,
            SERV_VERSION_MESS_FRAG = R.layout.fragment_message,
            USERS_FRAMENT = 4,
            BALANCES_FRAGMENT = 5,
            LOADING_FRAGMENT = 6,
            MESSAGE_FRAGMENT = 7;

    private static App mApp;
    private static ManagerControllerDB mManagerControllerDB;
    private static String mPrimDbName;
    private static ManagerMessagersDialogs mDequeMsg;
    private static List<QiwiUsers> mQiwiUsersList;
    private static List<QiwiUsersBalances> mQiwiUsersBalancesList;
    private static FragmentManager mFragmentManager;
    private static boolean mQiwiUsersListCreated;
    private static boolean mUsedTwoFragmentLayout;
    private static int mUsedPrimFragmentsVersion;
    private static int mUsedSecondFragmentsVersion;
    private static int mCurUserID;
    private static Bundle mServicesArguments;
    private static Bundle mRecyclersArguments;
    private static boolean mQiwiUsersBalancesListCreated;

    public static FragmentManager getFragmentManager() {
        return mFragmentManager;
    }

    public static void setFragmentManager(FragmentManager fragmentManager) {
        App.mFragmentManager = fragmentManager;
    }

    public static void createControllerDB(final String dbName) {
        ControllerDB controllerDB = new ControllerDB(getApp().getBaseContext(), dbName);
        controllerDB.openWritableDatabase();
        mManagerControllerDB.putControllerDB(controllerDB);
    }

    public static App getApp() {
        return mApp;
    }

    public static ManagerControllerDB getManagerControllerDB() {
        return mManagerControllerDB;
    }

    public static String getPrimDbName() {
        return mPrimDbName;
    }

    public static ManagerMessagersDialogs getDequeMsg() {
        return mDequeMsg;
    }

    public static List<QiwiUsers> getQiwiUsersList() {
        return mQiwiUsersList;
    }

    public static void setQiwiUsersList(List<QiwiUsers> qiwiUsersList) {
        mQiwiUsersList = qiwiUsersList;
    }

    public static boolean getQiwiUsersListCreated() {
        return mQiwiUsersListCreated;
    }

    public static void setQiwiUsersListCreated(final boolean state) {
        mQiwiUsersListCreated = state;
    }

    public static boolean getUsedTwoFragmentLayout() {
        return mUsedTwoFragmentLayout;
    }

    public static void setUsedTwoFragmentLayout(final boolean usedTwoFragmentLayout) {
        mUsedTwoFragmentLayout = usedTwoFragmentLayout;
    }

    public static int getUsedPrimFragmentsVersion() {
        return mUsedPrimFragmentsVersion;
    }

    public static void setUsedPrimFragmentsVersion(final int fragmentsVersion) {
        mUsedPrimFragmentsVersion = fragmentsVersion;
    }

    public static int getUsedSecondFragmentsVersion() {
        return mUsedSecondFragmentsVersion;
    }

    public static void setUsedSecondFragmentsVersion(final int fragmentsVersion) {
        mUsedSecondFragmentsVersion = fragmentsVersion;
    }

    public static Bundle getServicesArguments() {
        return mServicesArguments;
    }

    public static void setServicesArguments(Bundle arguments) {
        mServicesArguments = arguments;
    }

    public static int getCurUserID() {
        return mCurUserID;
    }

    public static void setCurUserID(int curUserID) {
        App.mCurUserID = curUserID;
    }

    public static Bundle getRecyclersArguments() {
        return mRecyclersArguments;
    }

    public static void setRecyclersArguments(Bundle recyclersArguments) {
        mRecyclersArguments = recyclersArguments;
    }

    public static List<QiwiUsersBalances> getQiwiUsersBalancesList() {
        return mQiwiUsersBalancesList;
    }

    public static void setQiwiUsersBalancesList(List<QiwiUsersBalances> qiwiUsersBalancesList) {
        mQiwiUsersBalancesList = qiwiUsersBalancesList;
    }

    public static boolean getQiwiUsersBalancesListCreated() {
        return mQiwiUsersBalancesListCreated;
    }

    public static void setQiwiUsersBalancesListCreated(boolean qiwiUsersBalancesListCreated) {
        mQiwiUsersBalancesListCreated = qiwiUsersBalancesListCreated;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;

        ControllerDB controllerDB = new ControllerDB(getBaseContext());
        controllerDB.openWritableDatabase();
        mManagerControllerDB.putControllerDB(controllerDB);
        mPrimDbName = controllerDB.getDbName();
        mQiwiUsersList = new ArrayList<>();
        mQiwiUsersBalancesList = new ArrayList<>();
        mQiwiUsersListCreated = false;
        mUsedTwoFragmentLayout =
                (getApp().getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4;
    }
}
