package com.example.samsung.qiwi_users_balances.presentation.presenter;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.view.View;
import android.widget.Toast;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.samsung.qiwi_users_balances.R;
import com.example.samsung.qiwi_users_balances.model.App;
import com.example.samsung.qiwi_users_balances.model.Balance;
import com.example.samsung.qiwi_users_balances.model.ControllerAPI;
import com.example.samsung.qiwi_users_balances.model.ControllerDB;
import com.example.samsung.qiwi_users_balances.model.JsonQiwisUsers;
import com.example.samsung.qiwi_users_balances.model.JsonQiwisUsersBalances;
import com.example.samsung.qiwi_users_balances.model.QiwiUsers;
import com.example.samsung.qiwi_users_balances.model.QiwiUsersBalances;
import com.example.samsung.qiwi_users_balances.model.QiwisUsersAPI;
import com.example.samsung.qiwi_users_balances.model.exceptions.CreateListQiwiUsersException;
import com.example.samsung.qiwi_users_balances.model.exceptions.DBCursorIsNullException;
import com.example.samsung.qiwi_users_balances.model.exceptions.DBIsNotDeletedException;
import com.example.samsung.qiwi_users_balances.presentation.view.ServiceView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Response;

import static com.example.samsung.qiwi_users_balances.model.ManagerControllerDB.getControllerDB;

@InjectViewState
public class ServicePresenter extends MvpPresenter<ServiceView> {

    private LoadingUsersTask mLoadingUsersTask;
    private ExchangeUsersTask mExchangeUsersTask;
    private LoadingBalancesTask mLoadingBalancesTask;
    private ExchangeBalancesTask mExchangeBalancesTask;
    private MessageTask mMessageTask;
    private Bundle mArgs;

    public ServicePresenter() {

        mArgs= App.getServicesArguments();

        switch (mArgs.getInt(App.SERV_VERSION)) {

            case App.SERV_VERSION_LOAD_FRAG:

                switch (mArgs.getInt(App.CALL_FROM)) {

                    case App.CALL_FROM_PRIM_FRAGMENT:
                        mLoadingUsersTask = new LoadingUsersTask();
                        mLoadingUsersTask.execute();
                        break;
                    case App.CALL_FROM_SECOND_FRAGMENT:
                        mLoadingBalancesTask = new LoadingBalancesTask();
                        mLoadingBalancesTask.execute();
                        break;
                    case App.CALL_FROM_BTN_PRIM_FRAG:
                        mExchangeUsersTask = new ExchangeUsersTask();
                        mExchangeUsersTask.execute();
                        break;
                    case App.CALL_FROM_BTN_SEC_FRAG:
                        mExchangeBalancesTask = new ExchangeBalancesTask();
                        mExchangeBalancesTask.execute();
                        break;
                    default:
                        break;
                }
                break;
            case App.SERV_VERSION_MESS_FRAG:
                mMessageTask = new MessageTask();
                mMessageTask.execute();
                break;
            default:
                break;
        }

    }

    public List<QiwiUsers> createListQiwiUsers() throws DBCursorIsNullException {

        boolean isRun = false;
        App.setQiwiUsersListCreated(isRun);

        List<QiwiUsers> qiwiUsersList = new ArrayList<>();
        ControllerDB controllerDB = getControllerDB(App.getPrimDbName()
        );
        //Создаём списк из БД
        int couner = 0, maxTrys = 2;
        do {
            if (!controllerDB.DBisOpen()) {
                controllerDB.openWritableDatabase();
            }
            Cursor cursor = controllerDB.getCursor();

            if (cursor != null) {

                if (cursor.moveToFirst()) {
                    do {
                        qiwiUsersList.add(new QiwiUsers(cursor.getInt(0), cursor.getString(1)));
                    } while (cursor.moveToNext());
                    isRun = true;
                } else {
                    if (cursor.getCount() == 0) {
                        try {
                            controllerDB.downloadData(ControllerAPI.getAPI().getUsers().execute());
                        } catch (Exception e) {
                            e.printStackTrace();
                            cursor.close();
                            String msg = App.getApp().getString(R.string.error_loading_response_in_db)
                                    + e.getMessage() + ": more 100 iterations";
                            throw new CreateListQiwiUsersException(msg);
                        }
                    }
                }
            } else {
                if (!cursor.isClosed()) {
                    cursor.close();
                }
                throw new DBCursorIsNullException(
                        App.getApp().getString(R.string.error_when_writing_data_from_the_response_db)
                                + " " + App.getApp().getString(R.string.db_cursor_is_null)
                );
            }
            if (!cursor.isClosed()) {
                cursor.close();
            }
            if (couner > maxTrys) {
                String msg = App.getApp().getString(R.string.error_loading_response_in_db)
                        + ": run more " + maxTrys + " trys";
                App.getDequeMsg().pushMsg(msg);
                throw new CreateListQiwiUsersException(msg);
            }
            couner++;

        } while (!isRun);

        controllerDB.close();
        App.setQiwiUsersListCreated(isRun);

        return qiwiUsersList;
    }

    public View.OnClickListener onClickRepeat(View view) {

        View.OnClickListener onClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                switch (v.getId()) {

                    case R.id.btnRepeat:

                        if (App.getDequeMsg().isEmpty()) {
                            App.setUsedPrimFragmentsVersion(App.LOADING_FRAGMENT);
                            getViewState().showCallingScreen();
                        } else {
                            getViewState().showNewMsg();
                        }
                        break;
                    case R.id.btnContinue:
                        App.setUsedPrimFragmentsVersion(App.USERS_FRAMENT);
                        getViewState().showCallingScreen();
                    default:
                        break;
                }
            }
        };
        return onClickListener;
    }

    private void setUsedNumberFragmentVersion(final int fragmentVersion) {

        switch (mArgs.getInt(App.CALL_FROM)) {

            case App.CALL_FROM_PRIM_FRAGMENT:
            case App.CALL_FROM_BTN_PRIM_FRAG:
                App.setUsedPrimFragmentsVersion(fragmentVersion);
                break;
            case App.CALL_FROM_SECOND_FRAGMENT:
            case App.CALL_FROM_BTN_SEC_FRAG:
                App.setUsedSecondFragmentsVersion(fragmentVersion);
                break;
            default:
                break;
        }
    }

    public void createListQiwiUsersBalances(int userId) {

        App.setCurUserID(userId);

        Response<JsonQiwisUsersBalances> response = null;

        try {
            response = ControllerAPI.getAPI().getBalancesById(userId).execute();
        } catch (IOException | NetworkOnMainThreadException e) {
            e.printStackTrace();
            App.getDequeMsg().pushMsg(
                    App.getApp().getResources().getString(R.string.error_loading_response_from_uri) + " "
                            + e.getMessage());
        }

        if (response != null) {

            if (response.body() != null) {

                App.setQiwiUsersBalancesList(responseHandler(response.body()));

                if (App.getQiwiUsersBalancesList().size() == 0) {

                    App.getDequeMsg().pushMsg(
                            App.getApp().getResources().getString(R.string.error_loading_response_from_uri) + " " +
                                    App.getApp().getResources().getString(R.string.the_dataset_is_null)
                    );
                }
            } else {

                App.getDequeMsg().pushMsg(
                        App.getApp().getResources().getString(R.string.error_loading_response_from_uri) + " " +
                                App.getApp().getResources().getString(R.string.responses_body_is_null)
                );
            }
        } else {
            App.getDequeMsg().pushMsg(
                    App.getApp().getResources().getString(R.string.error_loading_response_from_uri) + " " +
                    App.getApp().getResources().getString(R.string.response_is_null)
            );
        }

    }

    private List<QiwiUsersBalances> responseHandler(JsonQiwisUsersBalances responsesBody) {

        App.setQiwiUsersBalancesListCreated(false);
        List<QiwiUsersBalances> result = new ArrayList<>();

        int resultCode = responsesBody.getResultCode();

        if (resultCode != 0) {
            App.getDequeMsg().pushMsg(
                    "result code: " + resultCode);
        } else {

            for (Balance balance :
                    responsesBody.getBalances()) {
                result.add(new QiwiUsersBalances(balance.getCurrency(), balance.getAmount()));
            }
            App.setQiwiUsersBalancesListCreated(true);
        }

        return result;
    }

    private class LoadingUsersTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            String msg = "";
            Map<String, ControllerDB> allConrollersBDs = App.getManagerControllerDB().getAllControllerDBs();
            ControllerDB controllerDB = allConrollersBDs.get(App.getPrimDbName());
            Response<JsonQiwisUsers> response = null;
            try {
                response = ControllerAPI.getAPI().getUsers().execute();
                controllerDB.downloadData(response);
            } catch (Exception e) {
                e.printStackTrace();
                App.getDequeMsg().pushMsg(e.getMessage());
            }

            App.setQiwiUsersList(createListQiwiUsers());
            try {
                if (App.getQiwiUsersList() == null) {
                    msg = App.getApp().getString(R.string.the_dataset_is_null);
                }
            } catch (DBCursorIsNullException e) {
                e.printStackTrace();
                msg = e.getMessage();
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String aMsg) {
            super.onPostExecute(aMsg);

            if (!aMsg.equals("")) {
                Toast.makeText(App.getApp().getBaseContext(), aMsg, Toast.LENGTH_SHORT).show();
            }
            setUsedNumberFragmentVersion(App.MESSAGE_FRAGMENT);
            getViewState().showCallingScreen();
        }
    }

    private class ExchangeUsersTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            String msg = "";
            //onClickExcheng()
            ControllerDB controllerDB = getControllerDB(
                    App.getPrimDbName()
            );
            //Создаём резервную копию БД
            try {
                String dbName = "copy_db";
                App.createControllerDB(dbName);
                ControllerDB copyControllerDB = getControllerDB(dbName);
                copyControllerDB.openWritableDatabase();
                try {
                    controllerDB.copyDB(dbName);
                } catch (DBIsNotDeletedException e) {
                    e.printStackTrace();
                    msg = App.getApp().getString(R.string.error_while_backing_up_database)
                                    + e.getMessage();
                } catch (Exception e) {
                    e.printStackTrace();
                    msg = App.getApp().getString(R.string.error_while_backing_up_database)
                                    + e.getMessage();
                }
                //Обновляем список
                try {
                    App.setQiwiUsersList(createListQiwiUsers());
                    if (!App.getQiwiUsersListCreated()) {
                        msg = App.getApp().getString(R.string.error_when_updating_database) + " " +
                                        App.getApp().getString(R.string.create_list_of_qiwis_users_is_not_performed);
                    }
                } catch (DBCursorIsNullException e) {
                    e.printStackTrace();
                    //Восттанавливаем в случае неудачного обновления
                    try {
                        copyControllerDB.copyDB(App.getPrimDbName());
                    } catch (DBIsNotDeletedException e1) {
                        e1.printStackTrace();
                        msg = App.getApp().getString(R.string.error_restoring_db_from_backup)
                                        + e1.getMessage();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        msg = App.getApp().getString(R.string.error_restoring_db_from_backup)
                                        + e1.getMessage();
                    }
                    App.getDequeMsg().pushMsg(
                            App.getApp().getString(R.string.error_when_updating_database)
                                    + e.getMessage()
                    );
                }
            } catch (DBCursorIsNullException e) {
                e.printStackTrace();
                msg = App.getApp().getString(R.string.error_while_backing_up_database)
                                + e.getMessage();
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String aMsg) {
            super.onPostExecute(aMsg);

            if (!aMsg.equals("")) {
                Toast.makeText(App.getApp().getBaseContext(), aMsg, Toast.LENGTH_SHORT).show();
            }
            setUsedNumberFragmentVersion(App.MESSAGE_FRAGMENT);
            getViewState().showCallingScreen();
        }
    }

    private class LoadingBalancesTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            //onLoadBalancesDataset()

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setUsedNumberFragmentVersion(App.MESSAGE_FRAGMENT);
            getViewState().showCallingScreen();
        }
    }

    private class ExchangeBalancesTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            //onExchengBalancesDataset()

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setUsedNumberFragmentVersion(App.MESSAGE_FRAGMENT);
            getViewState().showCallingScreen();
        }
    }

    private class MessageTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            //onShowMessage()

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (App.getRecyclersArguments() == null) {

                    setUsedNumberFragmentVersion(App.USERS_FRAMENT);
            } else {

                setUsedNumberFragmentVersion(App.BALANCES_FRAGMENT);
            }
            getViewState().showCallingScreen();
        }
    }
}
