package com.staser.fragmentswithnotifications.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.staser.fragmentswithnotifications.R;
import com.staser.fragmentswithnotifications.notifications.BaseNotification;

import java.util.ArrayList;


import static com.staser.fragmentswithnotifications.MainActivity.notificationId;
import static com.staser.fragmentswithnotifications.MainActivity.notificationMap;

import static com.staser.fragmentswithnotifications.common.Common.APPLOG;
import static com.staser.fragmentswithnotifications.common.Common.ARG_PARAM_FR_ID;
import static com.staser.fragmentswithnotifications.common.Common.NOTI_THREAD_NAME;


public class BaseFragment extends Fragment implements View.OnClickListener {

    private int frgNum;

    private OnFragmentInteractionListener mListener;

    private TextView tvNumber;
    private Button btnPlus;
    private Button btnMinus;
    private Button btnNotif;

    public BaseFragment() {
    }

    /**
     * standard new Instance getting
     * @param frNum the counted fragment Number
     * @return
     */
    public static BaseFragment newInstance(int frNum) {
        BaseFragment _newFragment = new BaseFragment();
        Bundle _args = new Bundle();
        _args.putInt(ARG_PARAM_FR_ID, frNum);
        _newFragment.setArguments(_args);

        Log.d(APPLOG, "...FRA...newInstance was created: frNumer:" + frNum);
        return _newFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.frgNum = getArguments().getInt(ARG_PARAM_FR_ID);
        }

        Log.d(APPLOG, "...FRA...onCreate at: " + this.frgNum);
    }

    /**
     * creating view and setting element visibility logic
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_base, null);

        this.btnPlus = (Button) fragmentView.findViewById(R.id.btnPlus);
        this.btnPlus.setOnClickListener(this);
        this.btnMinus = (Button) fragmentView.findViewById(R.id.btnMinus);
        this.btnMinus.setOnClickListener(this);
        this.btnNotif = (Button) fragmentView.findViewById(R.id.btnNotifi);
        this.btnNotif.setOnClickListener(this);

        this.tvNumber = (TextView) fragmentView.findViewById(R.id.tvNumber);
        this.tvNumber.setText(String.valueOf(this.frgNum));

        switch (this.frgNum) {
            case 1:
                this.btnMinus.setVisibility(View.INVISIBLE);
                break;
            case 2:
                this.btnMinus.setVisibility(View.VISIBLE);
                break;
            case 3:
                this.btnPlus.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }

        Log.d(APPLOG, "...FRA...onCreateView at: " + this.frgNum );
        return fragmentView;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
           this.mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        Log.d(APPLOG, "...FRA...onAttach");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mListener = null;
        Log.d(APPLOG, "...FRA...onDetach at: " + this.frgNum );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPlus:
                this.mListener.onBtnPlus();
                break;
            case R.id.btnMinus:
                this.mListener.onBtnMinus(this.frgNum);
                break;
            case R.id.btnNotifi:
                this.doNotification();
                break;
            default:
                break;
        }

    }

    /**
     * doing notification into new threads
     */
    private void doNotification() {
        Thread notifyThread = new Thread(new Runnable() {
            @Override
            public void run() {
                notificationId++;
                new BaseNotification(getActivity(), frgNum, notificationId);
                putNotificationIdToMap();
            }
        });
        notifyThread.setName(NOTI_THREAD_NAME + String.valueOf(this.frgNum));
        notifyThread.start();

        Log.d(APPLOG, "...FRA...doNotification at: " + this.frgNum );
    }

    /**
     * putting all notifications List
     */
    private void putNotificationIdToMap() {
        ArrayList<Integer> _tempNotiIdArray;
        if (notificationMap.get(frgNum) != null) {
            _tempNotiIdArray = notificationMap.get(frgNum);
        }else {
            _tempNotiIdArray = new ArrayList<>();
        }
        _tempNotiIdArray.add(notificationId);
        notificationMap.put(frgNum, _tempNotiIdArray);
    }


    public interface OnFragmentInteractionListener {
        void onBtnPlus();
        void onBtnMinus(int frgNum);
    }
}
