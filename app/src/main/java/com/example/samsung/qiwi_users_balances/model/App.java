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
            FRAGMENT_NUMBER = "fragment_number",
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
    private static FragmentManager mSupportFragmentManager;
    private static FragmentManager mChildFragmentManager;
    private static boolean mQiwiUsersListCreated;
    private static boolean mUsedTwoFragmentLayout;
    private static int mNextPrimUsedFragmentsNumber;
    private static int mNextSecondUsedFragmentsNumber;
    private static int mCurUserID;
    private static Bundle mServicesArguments;
    private static Bundle mRecyclersArguments;
    private static boolean mQiwiUsersBalancesListCreated;

    public static FragmentManager getSupportFragmentManager() {
        return mSupportFragmentManager;
    }

    public static void setSupportFragmentManager(FragmentManager supportFragmentManager) {
        mSupportFragmentManager = supportFragmentManager;
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

    public static void validationParameterFragmentsNuber(int fragmentsNumber) throws IllegalArgumentException {

        if (fragmentsNumber == USERS_FRAGMENT_NUMBER
                || fragmentsNumber == BALANCES_FRAGMENT_NUMBER
                || fragmentsNumber == LOADING_FRAGMENT_NUMBER
                || fragmentsNumber == MESSAGE_FRAGMENT_NUMBER) {
            return;
        }
        throw new IllegalArgumentException("Attempt to pass an invalid parameter [" + fragmentsNumber);
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
     *                                      USERS_FRAGMENT_NUMBER = 0,
     *                                      BALANCES_FRAGMENT_NUMBER = 1,
     *                                      LOADING_FRAGMENT_NUMBER = 2,
     *                                      MESSAGE_FRAGMENT_NUMBER = 3.
     */
    public static void setNextSecondUsedFragmentsNumber(final int nextSecondUsedFragmentsNumber) throws IllegalArgumentException {

        try {
            validationParameterFragmentsNuber(nextSecondUsedFragmentsNumber);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage() + "] in the method setNextPrimUsedFragmentsNumber");
        }
        mNextSecondUsedFragmentsNumber = nextSecondUsedFragmentsNumber;
    }

    /**
     * Извлекает список аргументов соответствующего параметру фрагмента, упакованный в экземпляр
     * класса Bundle.
     *
     * @param fragmentsNumber - должен содержать одно из двух значений:
     *                        LOADING_FRAGMENT_NUMBER = 2,
     *                        MESSAGE_FRAGMENT_NUMBER = 3.
     * @return
     */
    public static Bundle getServicesArguments(final int fragmentsNumber) throws IllegalArgumentException {

        String excMsg = "Error in the method App.getServicesArguments(): ";

        if (fragmentsNumber != LOADING_FRAGMENT_NUMBER && fragmentsNumber != MESSAGE_FRAGMENT_NUMBER) {

            excMsg += "value of parameter \"fragmentNumber\" is not illegal";
            throw new IllegalArgumentException(excMsg);
        }

        return mServicesArguments.getBundle(FRAGMENT_NUMBER + fragmentsNumber);
    }

    /**
     * Помещает в экземпляр класса Bundle множество аргументов для класса ServiceFragment,
     * также упакованное в экземпляр класса Bundle, и помеченное одним из значений парметра
     *
     * @param fragmentsNumber   - должен содержать одно из двух значений:
     *                         LOADING_FRAGMENT_NUMBER = 2,
     *                         MESSAGE_FRAGMENT_NUMBER = 3.
     * @param serviceArguments - должен содержать все или все, кроме USER_ID, из следующих меткок:
     *                         USER_ID = "user_id",
     *                         SERV_VERSION = "serv_version",
     *                         FRAG_LAY_NUMBER = "frag_lay_number",
     *                         CALL_FROM = "call_from".
     * @<code>fragmentNumber</code>.
     */
    public static void setServicesArguments(final int fragmentsNumber, Bundle serviceArguments) throws IllegalArgumentException {

        boolean isException = false;
        String excMsg = "Error in the method App.setServicesArguments(): ";
        int excCount = 0;

        if (fragmentsNumber != LOADING_FRAGMENT_NUMBER && fragmentsNumber != MESSAGE_FRAGMENT_NUMBER) {

            excMsg += "value of parameter \"fragmentsNumber\" is not illegal";
            throw new IllegalArgumentException(excMsg);
        }

        if (serviceArguments.size() == 0) {

            excMsg += ("all nessesery parameters are absent.");
            isException = true;

        } else if (serviceArguments.size() < 4) {

            if (!serviceArguments.containsKey(SERV_VERSION)) {
                excMsg += ("nessesery parameter \"" + SERV_VERSION + "\" is absent; ");
                isException = true;
                excCount++;
            }
            if (!serviceArguments.containsKey(FRAG_LAY_NUMBER)) {

                if (excCount > 0) {
                    excMsg += "\n";
                }
                excMsg += ("nessesery parameter \"" + FRAG_LAY_NUMBER + "\" is absent; ");
                isException = true;
                excCount++;
            }
            if (!serviceArguments.containsKey(CALL_FROM)) {

                if (excCount > 0) {
                    excMsg += "\n";
                }
                excMsg += ("nessesery parameter \"" + CALL_FROM + "\" is absent; ");
                isException = true;
            }
        } else if (serviceArguments.size() == 4 && !serviceArguments.containsKey(USER_ID)) {
            excMsg += ("nessesery parameter \"" + USER_ID + "\" is absent.");
            isException = true;
        }

        if (isException) throw new IllegalArgumentException(excMsg);

        mServicesArguments.putBundle(FRAGMENT_NUMBER + fragmentsNumber, serviceArguments);
    }

    public static int getCurUserID() {
        return mCurUserID;
    }

    public static void setCurUserID(int curUserID) {
        App.mCurUserID = curUserID;
    }

    /**
     * Извлекает список аргументов соответствующего параметру фрагмента, упакованный в экземпляр
     * класса Bundle.
     *
     * @param fragmentsNumber - должен содержать одно из двух значений:
     *                           USERS_FRAGMENT_NUMBER = 0,
     *                           BALANCES_FRAGMENT_NUMBER = 1.
     * @return
     */
    public static Bundle getRecyclersArguments(final int fragmentsNumber) {

        String excMsg = "Error in the method App.getRecyclersArguments(): ";

        if (fragmentsNumber != USERS_FRAGMENT_NUMBER && fragmentsNumber != BALANCES_FRAGMENT_NUMBER) {

            excMsg += "value of parameter \"fragmentsNumber\" is not illegal";
            throw new IllegalArgumentException(excMsg);
        }

        return mServicesArguments.getBundle(FRAGMENT_NUMBER + fragmentsNumber);
    }

    /**
     * Помещает в экземпляр класса Bundle множество аргументов для класса RecyclerListFragment,
     * также упакованное в экземпляр класса Bundle, и помеченное одним из значений парметра
     *
     * @param fragmentsNumber     - должен содержать одно из двух значений:
     *                           USERS_FRAGMENT_NUMBER = 0,
     *                           BALANCES_FRAGMENT_NUMBER = 1.
     * @param recyclersArguments - может содержать все или ниодного из следующих меток:
     *                           USER_ID = "user_id",
     *                           RECYCL_VERSION = "recycl_version",
     *                           FRAG_LAY_NUMBER = "frag_lay_number".
     * @<code>fragmentNumber</code>.
     */
    public static void setRecyclersArguments(final int fragmentsNumber, Bundle recyclersArguments) throws IllegalArgumentException {

        boolean isException = false;
        String excMsg = "Error in the method App.setRecyclersArguments(): ";
        int excCount = 0;

        if (recyclersArguments.size() > 0 && recyclersArguments.size() < 3) {

            if (!recyclersArguments.containsKey(RECYCL_VERSION)) {
                excMsg += ("nessesery parameter \"" + RECYCL_VERSION + "\" is absent; ");
                isException = true;
                excCount++;
            }
            if (!recyclersArguments.containsKey(FRAG_LAY_NUMBER)) {

                if (excCount > 0) {
                    excMsg += "\n";
                }
                excMsg += ("nessesery parameter \"" + FRAG_LAY_NUMBER + "\" is absent; ");
                isException = true;
                excCount++;
            }
            if (!recyclersArguments.containsKey(USER_ID)) {

                if (excCount > 0) {
                    excMsg += "\n";
                }
                excMsg += ("nessesery parameter \"" + USER_ID + "\" is absent; ");
                isException = true;
            }
        }

        if (isException) throw new IllegalArgumentException(excMsg);

        mRecyclersArguments.putBundle(FRAGMENT_NUMBER + fragmentsNumber, recyclersArguments);
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

    public static FragmentManager getChildFragmentManager() {
        return mChildFragmentManager;
    }

    public static void setChildFragmentManager(FragmentManager childFragmentManager) {
        mChildFragmentManager = childFragmentManager;
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
        mRecyclersArguments = new Bundle();
        mServicesArguments = new Bundle();
    }
}
