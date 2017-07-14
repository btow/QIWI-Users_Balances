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
            RECYCL_VERSION = "recycl_version",
            SERV_VERSION = "serv_version",
            FRAG_LAY_NUMBER = "frag_lay_number",
            FRAGMENT_NUMBER = "frag_lay_number",
            CALL_FROM = "call_from";

    public static final int
            SERV_VERSION_LOAD_FRAG = R.layout.fragment_loading,
            SERV_VERSION_MESS_FRAG = R.layout.fragment_message,
            RECYCL_VERSION_FRAG = R.layout.fragment_recycler_list,
            USERS_FRAGMENT_NUMBER = 0,
            BALANCES_FRAGMENT_NUMBER = 1,
            LOADING_FRAGMENT_NUMBER = 2,
            MESSAGE_FRAGMENT_NUMBER = 3,
            CALL_FROM_USERS_LIST = 4,
            CALL_FROM_BALANCES_LIST = 5,
            CALL_FROM_BTN_USERS_LIST = 6,
            CALL_FROM_BTN_BALANCES_LIST = 7;

    private static App mApp;
    private static ManagerControllerDB mManagerControllerDB;
    private static String mPrimDbName;
    private static ManagerMessagersDialogs mDequeMsg;
    private static List<QiwiUsers> mQiwiUsersList;
    private static List<QiwiUsersBalances> mQiwiUsersBalancesList;
    private static FragmentManager mFragmentManager;
    private static boolean mQiwiUsersListCreated;
    private static boolean mUsedTwoFragmentLayout;
    private static int mNextPrimUsedFragmentsNumber;
    private static int mNextSecondUsedFragmentsNumber;
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

    private static void validationParameterFragmentsNuber(int fragmentsNumber) throws IllegalArgumentException {

        if (fragmentsNumber != USERS_FRAGMENT_NUMBER
                || fragmentsNumber != BALANCES_FRAGMENT_NUMBER
                || fragmentsNumber != LOADING_FRAGMENT_NUMBER
                || fragmentsNumber != MESSAGE_FRAGMENT_NUMBER) {
            throw new IllegalArgumentException("Attempt to pass an invalid parameter [" + fragmentsNumber);
        }
    }

    public static int getNextPrimUsedFragmentsNumber() {
        return mNextPrimUsedFragmentsNumber;
    }

    /**
     * @param nextPrimUsedFragmentsNumber - ожидается одно из возможных значений:
     *                                    USERS_FRAGMENT_NUMBER = 0,
     *                                    BALANCES_FRAGMENT_NUMBER = 1,
     *                                    LOADING_FRAGMENT_NUMBER = 2,
     *                                    MESSAGE_FRAGMENT_NUMBER = 3.
     */
    public static void setNextPrimUsedFragmentsNumber(final int nextPrimUsedFragmentsNumber) throws IllegalArgumentException {

        try {
            validationParameterFragmentsNuber(nextPrimUsedFragmentsNumber);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage() + "] in the method setNextPrimUsedFragmentsNumber");
        }
        mNextPrimUsedFragmentsNumber = nextPrimUsedFragmentsNumber;
    }

    public static int getNextSecondUsedFragmentsNumber() {
        return mNextSecondUsedFragmentsNumber;
    }

    /**
     * @param nextSecondUsedFragmentsNumber - ожидается одно из возможных значений:
     *                                    USERS_FRAGMENT_NUMBER = 0,
     *                                    BALANCES_FRAGMENT_NUMBER = 1,
     *                                    LOADING_FRAGMENT_NUMBER = 2,
     *                                    MESSAGE_FRAGMENT_NUMBER = 3.
     */
    public static void setNextSecondUsedFragmentsNumber(final int nextSecondUsedFragmentsNumber) throws IllegalArgumentException {

        try {
            validationParameterFragmentsNuber(nextSecondUsedFragmentsNumber);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage() + "] in the method setNextPrimUsedFragmentsNumber");
        }
        mNextSecondUsedFragmentsNumber = nextSecondUsedFragmentsNumber;
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
