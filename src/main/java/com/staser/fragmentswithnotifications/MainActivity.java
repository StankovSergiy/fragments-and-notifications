package com.staser.fragmentswithnotifications;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.staser.fragmentswithnotifications.fragments.BaseFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.staser.fragmentswithnotifications.common.Common.APPLOG;
import static com.staser.fragmentswithnotifications.common.Common.FR_STACK;
import static com.staser.fragmentswithnotifications.common.Common.INTENT_EXTRAS;
import static com.staser.fragmentswithnotifications.common.Common.TAG_FR;

public class MainActivity extends AppCompatActivity implements BaseFragment.OnFragmentInteractionListener {

    public static volatile int notificationId;
    public static volatile int fragmentId;
    public static volatile Map<Integer, ArrayList<Integer>> notificationMap;
    private FragmentTransaction fragmentTransaction;
    private int currentFragmentId;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notificationId = 0;
        fragmentId = 0;
        notificationMap = new HashMap<>();
        this.currentFragmentId = 1;
        this.createNewFragment();

        Log.d(APPLOG, "...ACT... onCreate");
    }


    /**
     * getting new Intent when notification was pushed
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int _fragNumber = intent.getIntExtra(INTENT_EXTRAS, -1);
        FragmentManager _fragmentManager = getFragmentManager();
        this.fragmentTransaction = _fragmentManager.beginTransaction();
        if (_fragNumber != -1) {
            BaseFragment _baseFragment = (BaseFragment) _fragmentManager
                    .findFragmentByTag(TAG_FR.trim() + String.valueOf(_fragNumber).trim());
            if (_baseFragment != null) {
                this.fragmentTransaction.replace(R.id.fragmentsContainer, _baseFragment);
                this.fragmentTransaction.commit();
                this.currentFragmentId = _fragNumber;
                Log.d(APPLOG, "...ACT... onNewIntent _fragNember: " + _fragNumber);
            } else {
                Toast.makeText(this, "My pardon! Fragment #" + _fragNumber + "was deleted before!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.d(APPLOG, "...ACT... onNewIntent _fragNember (Intent) is empty ");
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.remoteAssosiatedNitifications(-2);
        Toast.makeText(this, "SEE YOU SOON! ))", Toast.LENGTH_LONG).show();
        Log.d(APPLOG, "...ACT... onDestroy");
    }


    /**
     * creating new fragment
     */
    private void createNewFragment() {

        if (fragmentId < 3) {
            fragmentId++;
        } else {
            fragmentId = this.currentFragmentId;
        }
        BaseFragment _baseFragment = BaseFragment.newInstance(fragmentId);
        this.fragmentTransaction = getFragmentManager().beginTransaction();
        this.fragmentTransaction.replace(R.id.fragmentsContainer, _baseFragment, TAG_FR.trim() + fragmentId);
        this.fragmentTransaction.addToBackStack(FR_STACK);
        this.fragmentTransaction.commit();
    }

    /**
     * removing fragment by number
     * @param frgNum
     */
    private void removeExistingFragment(int frgNum) {
        fragmentId--;
        FragmentManager _fragmentManager = getFragmentManager();
        BaseFragment _currentFragment = (BaseFragment) _fragmentManager.findFragmentByTag(TAG_FR + String.valueOf(frgNum).trim());
        this.fragmentTransaction = _fragmentManager.beginTransaction();
        this.fragmentTransaction.remove(_currentFragment);
        this.fragmentTransaction.commit();
        _fragmentManager.popBackStack();

        this.currentFragmentId = fragmentId;
    }


    /**
     * remove all notifications associated with fragment by number
     * @param frgNum
     */
    private void remoteAssosiatedNitifications(int frgNum) {
        NotificationManager _notificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
        if (frgNum == -2) {
            _notificationManager.cancelAll();
        } else {
            ArrayList<Integer> _tempFragmNotiIdArray = notificationMap.get(frgNum);
            if (_tempFragmNotiIdArray != null) {
                for (int _notifyId : _tempFragmNotiIdArray) {
                    _notificationManager.cancel(String.valueOf(frgNum), _notifyId);
                    addLog("was REMOVED notification msg number: " + _notifyId + " from fragment: " + frgNum);
                }
            } else {
                addLog("_tempFragmNotiIdArray is NULL");
            }
            _notificationManager.cancel(String.valueOf(frgNum), -1);
        }
    }

    /**
     * just adding log with type .i
     * @param logMsg
     */
    private void addLog(String logMsg) {
        Log.i(APPLOG, getClass().getName() + "-----:" + logMsg);
    }


    /**
     * interface BaseFragment.OnFragmentInteractionListener method
     */
    @Override
    public void onBtnPlus() {
        this.createNewFragment();
    }

    /**
     * interface BaseFragment.OnFragmentInteractionListener method
     */
    @Override
    public void onBtnMinus(int frgNum) {
        this.removeExistingFragment(frgNum);
        this.remoteAssosiatedNitifications(frgNum);
    }


}
