// background blur
// seek-bar minimum state
// reset all buttons after approve

package com.example.anujdawar.slicendice;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    protected RelativeLayout popUpRelativeLayout;
    protected PopupWindow popupWindow;

    protected Button flavor1Button, flavor2Button, flavor3Button, flavor4Button, flavor5Button, flavor6Button,
            flavor7Button, flavor8Button, flavor9Button, flavor10Button, flavor11Button, flavor12Button;

    protected String desc1, desc2, desc3, desc4, desc5, desc6, desc7, desc8, desc9, desc10, desc11, desc12;

    protected TextView syrupSelectedTV1, syrupSelectedTV2;
    protected SeekBar syrupSelectedSeek1, syrupSelectedSeek2;

    private TextView progressTV;

    protected Button approveButton;

    protected RadioGroup myRadioGroup;
    protected RadioButton smallRadio, bigRadio;

    protected int CountSelectedSyrups = 0;
    public static DatabaseClassCommon db;

    protected static int popupBox = 0;

    long upTime, downTime, diff;

    BluetoothAdapter bluetoothAdapter;
    Set<BluetoothDevice> pairedDevices;
    int REQUEST_ENABLE = 1;
    private static final String TAG = "MY_APP_DEBUG_TAG";
    static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private BluetoothSocket mmSocket;
    private InputStream mmInStream;
    private OutputStream mmOutStream;
    private byte[] mmBuffer; // mmBuffer store for the stream
    private BluetoothDevice mmDevice;
    public static final int MESSAGE_READ = 0;
    public static final int MESSAGE_WRITE = 1;
    public static final int MESSAGE_TOAST = 2;
    BluetoothDevice tempDevice;
    ProgressBar progressBar;
    boolean getOuttaLoop = false;
    MenuItem playMenu;
    Thread connectedThread, tempthread;
    boolean manualDisconnection = false;

    int tempVariable = 0;

    String toSend = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        popUpRelativeLayout = (RelativeLayout) findViewById(R.id.activity_fullscreen);
        db = new DatabaseClassCommon(this);
        Cursor res = db.viewAllData();

        if (res.getCount() == 0) {
            db.insertData(1, "Tiger Blood", "y", "Desc 1");
            db.insertData(2, "Strawberry", "y", "Desc 2");
            db.insertData(3, "Blue Coconut", "y", "Desc 3");
            db.insertData(4, "Lemon Lime", "y", "Desc 4");
            db.insertData(5, "Cherry", "y", "Desc 5");
            db.insertData(6, "Wedding Cake", "y", "Desc 6");
            db.insertData(7, "Dreamscile", "y", "Desc 7");
            db.insertData(8, "Blue Raspberry", "y", "Desc 8");
            db.insertData(9, "Root Beer", "y", "Desc 9");
            db.insertData(10, "Pink Colada", "y", "Desc 10");
            db.insertData(11, "Pine Apple", "y", "Desc 11");
            db.insertData(12, "Pink Lemonade", "y", "Desc 12");
        }

        res.close();
        setRadioButtons();
        setFlavorButtons();
        setNamesOfButtons();
        defineOnCLickFlavorListeners();
        setOnApproveButtonListener();
        setProgressBar();

        progressTV.setVisibility(View.VISIBLE);
        syrupSelectedSeek1.setEnabled(false);
        syrupSelectedSeek2.setEnabled(false);
        flavor1Button.setEnabled(false);
        flavor2Button.setEnabled(false);
        flavor3Button.setEnabled(false);
        flavor4Button.setEnabled(false);
        flavor5Button.setEnabled(false);
        flavor6Button.setEnabled(false);
        flavor7Button.setEnabled(false);
        flavor8Button.setEnabled(false);
        flavor9Button.setEnabled(false);
        flavor10Button.setEnabled(false);
        flavor11Button.setEnabled(false);
        flavor12Button.setEnabled(false);
        smallRadio.setEnabled(false);
        bigRadio.setEnabled(false);
        approveButton.setEnabled(false);

        init();
    }

    private void setRadioButtons(){
        myRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        smallRadio = (RadioButton) findViewById(R.id.small);
        bigRadio = (RadioButton) findViewById(R.id.big);

        smallRadio.setEnabled(false);
        bigRadio.setEnabled(false);

        smallRadio.setVisibility(View.INVISIBLE);
        bigRadio.setVisibility(View.INVISIBLE);

        smallRadio.setText("Medium");
        bigRadio.setText("Large");
    }

    private void setFlavorButtons() {
        flavor1Button = (Button) findViewById(R.id.flav1ID);
        flavor2Button = (Button) findViewById(R.id.flav2ID);
        flavor3Button = (Button) findViewById(R.id.flav3ID);
        flavor4Button = (Button) findViewById(R.id.flav4ID);
        flavor5Button = (Button) findViewById(R.id.flav5ID);
        flavor6Button = (Button) findViewById(R.id.flav6ID);
        flavor7Button = (Button) findViewById(R.id.flav7ID);
        flavor8Button = (Button) findViewById(R.id.flav8ID);
        flavor9Button = (Button) findViewById(R.id.flav9ID);
        flavor10Button = (Button) findViewById(R.id.flav10ID);
        flavor11Button = (Button) findViewById(R.id.flav11ID);
        flavor12Button = (Button) findViewById(R.id.flav12ID);

        syrupSelectedTV1 = (TextView) findViewById(R.id.syrup1selectedID);
        syrupSelectedTV2 = (TextView) findViewById(R.id.syrup2selectedID);

        syrupSelectedSeek1 = (SeekBar) findViewById(R.id.syrup1seekID);
        syrupSelectedSeek2 = (SeekBar) findViewById(R.id.syrup2seekID);

        syrupSelectedSeek1.setProgress(1);
        syrupSelectedSeek2.setProgress(1);

        approveButton = (Button) findViewById(R.id.approveSyrupsID);
    }

    private void setNamesOfButtons() {
        Cursor res = db.viewAllData();

        while (res.moveToNext()) {
            String x = res.getString(0);
            String name = res.getString(1);
            String availability = res.getString(2);
            String desc = res.getString(3);

            switch (x) {
                case "1":
                    flavor1Button.setText(name);
                    desc1 = desc;
                    if (availability.equals("n")) {
                        flavor1Button.setBackgroundColor(getResources().getColor(R.color.disabled));
                        flavor1Button.setEnabled(false);
                    }

                    break;

                case "2":

                    flavor2Button.setText(name);
                    desc2 = desc;

                    if (availability.equals("n")) {
                        flavor2Button.setBackgroundColor(getResources().getColor(R.color.disabled));
                        flavor2Button.setEnabled(false);
                    }

                    break;

                case "3":

                    flavor3Button.setText(name);
                    desc3 = desc;

                    if (availability.equals("n")) {
                        flavor3Button.setBackgroundColor(getResources().getColor(R.color.disabled));
                        flavor3Button.setEnabled(false);
                    }

                    break;

                case "4":

                    flavor4Button.setText(name);
                    desc4 = desc;

                    if (availability.equals("n")) {
                        flavor4Button.setBackgroundColor(getResources().getColor(R.color.disabled));
                        flavor4Button.setEnabled(false);
                    }

                    break;

                case "5":

                    flavor5Button.setText(name);
                    desc5 = desc;

                    if (availability.equals("n")) {
                        flavor5Button.setBackgroundColor(getResources().getColor(R.color.disabled));
                        flavor5Button.setEnabled(false);
                    }

                    break;

                case "6":

                    flavor6Button.setText(name);
                    desc6 = desc;

                    if (availability.equals("n")) {
                        flavor6Button.setBackgroundColor(getResources().getColor(R.color.disabled));
                        flavor6Button.setEnabled(false);
                    }

                    break;

                case "7":

                    flavor7Button.setText(name);
                    desc7 = desc;

                    if (availability.equals("n")) {
                        flavor7Button.setBackgroundColor(getResources().getColor(R.color.disabled));
                        flavor7Button.setEnabled(false);
                    }

                    break;

                case "8":

                    flavor8Button.setText(name);
                    desc8 = desc;

                    if (availability.equals("n")) {
                        flavor8Button.setBackgroundColor(getResources().getColor(R.color.disabled));
                        flavor8Button.setEnabled(false);
                    }

                    break;

                case "9":

                    flavor9Button.setText(name);
                    desc9 = desc;

                    if (availability.equals("n")) {
                        flavor9Button.setBackgroundColor(getResources().getColor(R.color.disabled));
                        flavor9Button.setEnabled(false);
                    }

                    break;

                case "10":

                    flavor10Button.setText(name);
                    desc10 = desc;

                    if (availability.equals("n")) {
                        flavor10Button.setBackgroundColor(getResources().getColor(R.color.disabled));
                        flavor10Button.setEnabled(false);
                    }

                    break;

                case "11":

                    flavor11Button.setText(name);
                    desc11 = desc;

                    if (availability.equals("n")) {
                        flavor11Button.setBackgroundColor(getResources().getColor(R.color.disabled));
                        flavor11Button.setEnabled(false);
                    }

                    break;

                case "12":

                    flavor12Button.setText(name);
                    desc12 = desc;

                    if (availability.equals("n")) {
                        flavor12Button.setBackgroundColor(getResources().getColor(R.color.disabled));
                        flavor12Button.setEnabled(false);
                    }

                    break;
            }
        }

        res.close();
    }

    private void defineOnCLickFlavorListeners() {
        smallRadio.setChecked(true);

        flavor1Button.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    downTime = System.currentTimeMillis();

                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);

                        View customView = inflater.inflate(R.layout.pop_up, null);

                        popupWindow = new PopupWindow(
                                customView,
                                400,
                                400
                        );

                        ((TextView) popupWindow.getContentView().findViewById(R.id.theGreatPopUp)).setText(desc1);

                        popupWindow.showAtLocation(popUpRelativeLayout, Gravity.RIGHT, 100, 0);
                    }
                }

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    upTime = System.currentTimeMillis();
                    popupWindow.dismiss();
                    diff = upTime - downTime;

                    if (diff < 300) {
                        if (flavor1Button.getCurrentTextColor() == getResources().getColor(R.color.green)) {

                            popupBox = 1;

                            if (CountSelectedSyrups < 2) {
                                flavor1Button.setTextColor(getResources().getColor(R.color.white));
                                flavor1Button.setBackgroundColor(getResources().getColor(R.color.green));

                                smallRadio.setVisibility(View.VISIBLE);
                                bigRadio.setVisibility(View.VISIBLE);
                                smallRadio.setEnabled(true);
                                bigRadio.setEnabled(true);

                                CountSelectedSyrups++;

                                if (CountSelectedSyrups == 1) {
                                    syrupSelectedTV1.setText(String.valueOf(flavor1Button.getText()));
                                    syrupSelectedTV1.setVisibility(View.VISIBLE);
                                    syrupSelectedSeek1.setVisibility(View.VISIBLE);
                                    syrupSelectedSeek1.setProgress(0);
                                } else if (CountSelectedSyrups == 2) {
                                    syrupSelectedTV2.setText(String.valueOf(flavor1Button.getText()));
                                    syrupSelectedTV2.setVisibility(View.VISIBLE);
                                    syrupSelectedSeek2.setVisibility(View.VISIBLE);
                                    syrupSelectedSeek2.setProgress(0);
                                }
                            }

                        } else {
                            flavor1Button.setTextColor(getResources().getColor(R.color.green));
                            flavor1Button.setBackgroundColor(getResources().getColor(R.color.white));
                            CountSelectedSyrups--;

                            if (CountSelectedSyrups == 1) {
                                if (flavor1Button.getText() == syrupSelectedTV1.getText()) {
                                    syrupSelectedTV1.setText(String.valueOf(syrupSelectedTV2.getText()));
                                    syrupSelectedSeek1.setProgress(syrupSelectedSeek2.getProgress());
                                    syrupSelectedSeek2.setVisibility(View.INVISIBLE);
                                    syrupSelectedTV2.setVisibility(View.INVISIBLE);
                                } else {
                                    syrupSelectedTV2.setVisibility(View.INVISIBLE);
                                    syrupSelectedSeek2.setVisibility(View.INVISIBLE);
                                }

                            } else if (CountSelectedSyrups == 0) {
                                syrupSelectedTV1.setVisibility(View.INVISIBLE);
                                syrupSelectedSeek1.setVisibility(View.INVISIBLE);
                                syrupSelectedSeek2.setVisibility(View.INVISIBLE);
                                syrupSelectedTV2.setVisibility(View.INVISIBLE);

                                smallRadio.setChecked(true);

                                smallRadio.setEnabled(false);
                                smallRadio.setVisibility(View.INVISIBLE);
                                bigRadio.setEnabled(false);
                                bigRadio.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                }
                return true;
            }
        });

        flavor2Button.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    downTime = System.currentTimeMillis();

                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);

                        View customView = inflater.inflate(R.layout.pop_up, null);

                        popupWindow = new PopupWindow(
                                customView,
                                400,
                                400
                        );

                        ((TextView) popupWindow.getContentView().findViewById(R.id.theGreatPopUp)).setText(desc2);

                        popupWindow.showAtLocation(popUpRelativeLayout, Gravity.RIGHT, 100, 0);
                    }
                }

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    upTime = System.currentTimeMillis();
                    popupWindow.dismiss();
                    diff = upTime - downTime;

                    if (diff < 300) {
                        if (flavor2Button.getCurrentTextColor() == getResources().getColor(R.color.green)) {

                            popupBox = 2;

                            if (CountSelectedSyrups < 2) {
                                flavor2Button.setTextColor(getResources().getColor(R.color.white));
                                flavor2Button.setBackgroundColor(getResources().getColor(R.color.green));

                                smallRadio.setVisibility(View.VISIBLE);
                                bigRadio.setVisibility(View.VISIBLE);
                                smallRadio.setEnabled(true);
                                bigRadio.setEnabled(true);

                                CountSelectedSyrups++;

                                if (CountSelectedSyrups == 1) {
                                    syrupSelectedTV1.setText(String.valueOf(flavor2Button.getText()));
                                    syrupSelectedTV1.setVisibility(View.VISIBLE);
                                    syrupSelectedSeek1.setVisibility(View.VISIBLE);
                                    syrupSelectedSeek1.setProgress(0);
                                } else if (CountSelectedSyrups == 2) {
                                    syrupSelectedTV2.setText(String.valueOf(flavor2Button.getText()));
                                    syrupSelectedTV2.setVisibility(View.VISIBLE);
                                    syrupSelectedSeek2.setVisibility(View.VISIBLE);
                                    syrupSelectedSeek2.setProgress(0);
                                }
                            }
                        } else {
                            flavor2Button.setTextColor(getResources().getColor(R.color.green));
                            flavor2Button.setBackgroundColor(getResources().getColor(R.color.white));
                            CountSelectedSyrups--;

                            if (CountSelectedSyrups == 1) {
                                if (flavor2Button.getText() == syrupSelectedTV1.getText()) {
                                    syrupSelectedTV1.setText(String.valueOf(syrupSelectedTV2.getText()));
                                    syrupSelectedSeek1.setProgress(syrupSelectedSeek2.getProgress());
                                    syrupSelectedSeek2.setVisibility(View.INVISIBLE);
                                    syrupSelectedTV2.setVisibility(View.INVISIBLE);
                                } else {
                                    syrupSelectedTV2.setVisibility(View.INVISIBLE);
                                    syrupSelectedSeek2.setVisibility(View.INVISIBLE);
                                }
                            } else if (CountSelectedSyrups == 0) {
                                syrupSelectedTV1.setVisibility(View.INVISIBLE);
                                syrupSelectedSeek1.setVisibility(View.INVISIBLE);
                                syrupSelectedSeek2.setVisibility(View.INVISIBLE);
                                syrupSelectedTV2.setVisibility(View.INVISIBLE);

                                smallRadio.setChecked(true);

                                smallRadio.setEnabled(false);
                                smallRadio.setVisibility(View.INVISIBLE);
                                bigRadio.setEnabled(false);
                                bigRadio.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                }
                return true;
            }
        });

        flavor3Button.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    downTime = System.currentTimeMillis();

                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);

                        View customView = inflater.inflate(R.layout.pop_up, null);

                        popupWindow = new PopupWindow(
                                customView,
                                400,
                                400
                        );

                        ((TextView) popupWindow.getContentView().findViewById(R.id.theGreatPopUp)).setText(desc3);

                        popupWindow.showAtLocation(popUpRelativeLayout, Gravity.RIGHT, 100, 0);
                    }
                }

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    upTime = System.currentTimeMillis();
                    popupWindow.dismiss();
                    diff = upTime - downTime;

                    if (diff < 300) {
                        if (flavor3Button.getCurrentTextColor() == getResources().getColor(R.color.green)) {

                            popupBox = 3;

                            if (CountSelectedSyrups < 2) {
                                flavor3Button.setTextColor(getResources().getColor(R.color.white));
                                flavor3Button.setBackgroundColor(getResources().getColor(R.color.green));

                                smallRadio.setVisibility(View.VISIBLE);
                                bigRadio.setVisibility(View.VISIBLE);
                                smallRadio.setEnabled(true);
                                bigRadio.setEnabled(true);

                                CountSelectedSyrups++;

                                if (CountSelectedSyrups == 1) {
                                    syrupSelectedTV1.setText(String.valueOf(flavor3Button.getText()));
                                    syrupSelectedTV1.setVisibility(View.VISIBLE);
                                    syrupSelectedSeek1.setVisibility(View.VISIBLE);
                                    syrupSelectedSeek1.setProgress(0);
                                } else if (CountSelectedSyrups == 2) {
                                    syrupSelectedTV2.setText(String.valueOf(flavor3Button.getText()));
                                    syrupSelectedTV2.setVisibility(View.VISIBLE);
                                    syrupSelectedSeek2.setVisibility(View.VISIBLE);
                                    syrupSelectedSeek2.setProgress(0);
                                }
                            }
                        } else {
                            flavor3Button.setTextColor(getResources().getColor(R.color.green));
                            flavor3Button.setBackgroundColor(getResources().getColor(R.color.white));
                            CountSelectedSyrups--;

                            if (CountSelectedSyrups == 1) {
                                if (flavor3Button.getText() == syrupSelectedTV1.getText()) {
                                    syrupSelectedTV1.setText(String.valueOf(syrupSelectedTV2.getText()));
                                    syrupSelectedSeek1.setProgress(syrupSelectedSeek2.getProgress());
                                    syrupSelectedSeek2.setVisibility(View.INVISIBLE);
                                    syrupSelectedTV2.setVisibility(View.INVISIBLE);
                                } else {
                                    syrupSelectedTV2.setVisibility(View.INVISIBLE);
                                    syrupSelectedSeek2.setVisibility(View.INVISIBLE);
                                }
                            } else if (CountSelectedSyrups == 0) {
                                syrupSelectedTV1.setVisibility(View.INVISIBLE);
                                syrupSelectedSeek1.setVisibility(View.INVISIBLE);
                                syrupSelectedSeek2.setVisibility(View.INVISIBLE);
                                syrupSelectedTV2.setVisibility(View.INVISIBLE);

                                smallRadio.setChecked(true);

                                smallRadio.setEnabled(false);
                                smallRadio.setVisibility(View.INVISIBLE);
                                bigRadio.setEnabled(false);
                                bigRadio.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                }
                return true;
            }
        });

        flavor4Button.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    downTime = System.currentTimeMillis();

                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);

                        View customView = inflater.inflate(R.layout.pop_up, null);

                        popupWindow = new PopupWindow(
                                customView,
                                400,
                                400
                        );

                        ((TextView) popupWindow.getContentView().findViewById(R.id.theGreatPopUp)).setText(desc4);

                        popupWindow.showAtLocation(popUpRelativeLayout, Gravity.RIGHT, 100, 0);
                    }
                }

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    upTime = System.currentTimeMillis();
                    popupWindow.dismiss();
                    diff = upTime - downTime;

                    if (diff < 300) {
                        if (flavor4Button.getCurrentTextColor() == getResources().getColor(R.color.green)) {

                            popupBox = 4;

                            if (CountSelectedSyrups < 2) {
                                flavor4Button.setTextColor(getResources().getColor(R.color.white));
                                flavor4Button.setBackgroundColor(getResources().getColor(R.color.green));

                                smallRadio.setVisibility(View.VISIBLE);
                                bigRadio.setVisibility(View.VISIBLE);
                                smallRadio.setEnabled(true);
                                bigRadio.setEnabled(true);

                                CountSelectedSyrups++;

                                if (CountSelectedSyrups == 1) {
                                    syrupSelectedTV1.setText(String.valueOf(flavor4Button.getText()));
                                    syrupSelectedTV1.setVisibility(View.VISIBLE);
                                    syrupSelectedSeek1.setVisibility(View.VISIBLE);
                                    syrupSelectedSeek1.setProgress(0);
                                } else if (CountSelectedSyrups == 2) {
                                    syrupSelectedTV2.setText(String.valueOf(flavor4Button.getText()));
                                    syrupSelectedTV2.setVisibility(View.VISIBLE);
                                    syrupSelectedSeek2.setVisibility(View.VISIBLE);
                                    syrupSelectedSeek2.setProgress(0);
                                }
                            }
                        } else {
                            flavor4Button.setTextColor(getResources().getColor(R.color.green));
                            flavor4Button.setBackgroundColor(getResources().getColor(R.color.white));
                            CountSelectedSyrups--;

                            if (CountSelectedSyrups == 1) {
                                if (flavor4Button.getText() == syrupSelectedTV1.getText()) {
                                    syrupSelectedTV1.setText(String.valueOf(syrupSelectedTV2.getText()));
                                    syrupSelectedSeek1.setProgress(syrupSelectedSeek2.getProgress());
                                    syrupSelectedSeek2.setVisibility(View.INVISIBLE);
                                    syrupSelectedTV2.setVisibility(View.INVISIBLE);
                                } else {
                                    syrupSelectedTV2.setVisibility(View.INVISIBLE);
                                    syrupSelectedSeek2.setVisibility(View.INVISIBLE);
                                }
                            } else if (CountSelectedSyrups == 0) {
                                syrupSelectedTV1.setVisibility(View.INVISIBLE);
                                syrupSelectedSeek1.setVisibility(View.INVISIBLE);
                                syrupSelectedSeek2.setVisibility(View.INVISIBLE);
                                syrupSelectedTV2.setVisibility(View.INVISIBLE);

                                smallRadio.setChecked(true);

                                smallRadio.setEnabled(false);
                                smallRadio.setVisibility(View.INVISIBLE);
                                bigRadio.setEnabled(false);
                                bigRadio.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                }
                return true;
            }
        });

        flavor5Button.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    downTime = System.currentTimeMillis();

                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);

                        View customView = inflater.inflate(R.layout.pop_up, null);

                        popupWindow = new PopupWindow(
                                customView,
                                400,
                                400
                        );

                        ((TextView) popupWindow.getContentView().findViewById(R.id.theGreatPopUp)).setText(desc5);

                        popupWindow.showAtLocation(popUpRelativeLayout, Gravity.RIGHT, 100, 0);
                    }
                }

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    upTime = System.currentTimeMillis();
                    popupWindow.dismiss();
                    diff = upTime - downTime;

                    if (diff < 300) {
                        if (flavor5Button.getCurrentTextColor() == getResources().getColor(R.color.green)) {

                            popupBox = 5;

                            if (CountSelectedSyrups < 2) {
                                flavor5Button.setTextColor(getResources().getColor(R.color.white));
                                flavor5Button.setBackgroundColor(getResources().getColor(R.color.green));

                                smallRadio.setVisibility(View.VISIBLE);
                                bigRadio.setVisibility(View.VISIBLE);
                                smallRadio.setEnabled(true);
                                bigRadio.setEnabled(true);

                                CountSelectedSyrups++;

                                if (CountSelectedSyrups == 1) {
                                    syrupSelectedTV1.setText(String.valueOf(flavor5Button.getText()));
                                    syrupSelectedTV1.setVisibility(View.VISIBLE);
                                    syrupSelectedSeek1.setVisibility(View.VISIBLE);
                                    syrupSelectedSeek1.setProgress(0);
                                } else if (CountSelectedSyrups == 2) {
                                    syrupSelectedTV2.setText(String.valueOf(flavor5Button.getText()));
                                    syrupSelectedTV2.setVisibility(View.VISIBLE);
                                    syrupSelectedSeek2.setVisibility(View.VISIBLE);
                                    syrupSelectedSeek2.setProgress(0);
                                }
                            }
                        } else {
                            flavor5Button.setTextColor(getResources().getColor(R.color.green));
                            flavor5Button.setBackgroundColor(getResources().getColor(R.color.white));
                            CountSelectedSyrups--;

                            if (CountSelectedSyrups == 1) {
                                if (flavor5Button.getText() == syrupSelectedTV1.getText()) {
                                    syrupSelectedTV1.setText(String.valueOf(syrupSelectedTV2.getText()));
                                    syrupSelectedSeek1.setProgress(syrupSelectedSeek2.getProgress());
                                    syrupSelectedSeek2.setVisibility(View.INVISIBLE);
                                    syrupSelectedTV2.setVisibility(View.INVISIBLE);
                                } else {
                                    syrupSelectedTV2.setVisibility(View.INVISIBLE);
                                    syrupSelectedSeek2.setVisibility(View.INVISIBLE);
                                }
                            } else if (CountSelectedSyrups == 0) {
                                syrupSelectedTV1.setVisibility(View.INVISIBLE);
                                syrupSelectedSeek1.setVisibility(View.INVISIBLE);
                                syrupSelectedSeek2.setVisibility(View.INVISIBLE);
                                syrupSelectedTV2.setVisibility(View.INVISIBLE);

                                smallRadio.setChecked(true);

                                smallRadio.setEnabled(false);
                                smallRadio.setVisibility(View.INVISIBLE);
                                bigRadio.setEnabled(false);
                                bigRadio.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                }
                return true;
            }
        });

        flavor6Button.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    downTime = System.currentTimeMillis();

                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);

                        View customView = inflater.inflate(R.layout.pop_up, null);

                        popupWindow = new PopupWindow(
                                customView,
                                400,
                                400
                        );

                        ((TextView) popupWindow.getContentView().findViewById(R.id.theGreatPopUp)).setText(desc6);

                        popupWindow.showAtLocation(popUpRelativeLayout, Gravity.RIGHT, 100, 0);
                    }
                }

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    upTime = System.currentTimeMillis();
                    popupWindow.dismiss();
                    diff = upTime - downTime;

                    if (diff < 300) {
                        if (flavor6Button.getCurrentTextColor() == getResources().getColor(R.color.green)) {

                            popupBox = 6;

                            if (CountSelectedSyrups < 2) {
                                flavor6Button.setTextColor(getResources().getColor(R.color.white));
                                flavor6Button.setBackgroundColor(getResources().getColor(R.color.green));

                                smallRadio.setVisibility(View.VISIBLE);
                                bigRadio.setVisibility(View.VISIBLE);
                                smallRadio.setEnabled(true);
                                bigRadio.setEnabled(true);

                                CountSelectedSyrups++;

                                if (CountSelectedSyrups == 1) {
                                    syrupSelectedTV1.setText(String.valueOf(flavor6Button.getText()));
                                    syrupSelectedTV1.setVisibility(View.VISIBLE);
                                    syrupSelectedSeek1.setVisibility(View.VISIBLE);
                                    syrupSelectedSeek1.setProgress(0);
                                } else if (CountSelectedSyrups == 2) {
                                    syrupSelectedTV2.setText(String.valueOf(flavor6Button.getText()));
                                    syrupSelectedTV2.setVisibility(View.VISIBLE);
                                    syrupSelectedSeek2.setVisibility(View.VISIBLE);
                                    syrupSelectedSeek2.setProgress(0);
                                }
                            }
                        } else {
                            flavor6Button.setTextColor(getResources().getColor(R.color.green));
                            flavor6Button.setBackgroundColor(getResources().getColor(R.color.white));
                            CountSelectedSyrups--;

                            if (CountSelectedSyrups == 1) {
                                if (flavor6Button.getText() == syrupSelectedTV1.getText()) {
                                    syrupSelectedTV1.setText(String.valueOf(syrupSelectedTV2.getText()));
                                    syrupSelectedSeek1.setProgress(syrupSelectedSeek2.getProgress());
                                    syrupSelectedSeek2.setVisibility(View.INVISIBLE);
                                    syrupSelectedTV2.setVisibility(View.INVISIBLE);
                                } else {
                                    syrupSelectedTV2.setVisibility(View.INVISIBLE);
                                    syrupSelectedSeek2.setVisibility(View.INVISIBLE);
                                }
                            } else if (CountSelectedSyrups == 0) {
                                syrupSelectedTV1.setVisibility(View.INVISIBLE);
                                syrupSelectedSeek1.setVisibility(View.INVISIBLE);
                                syrupSelectedSeek2.setVisibility(View.INVISIBLE);
                                syrupSelectedTV2.setVisibility(View.INVISIBLE);

                                smallRadio.setChecked(true);

                                smallRadio.setEnabled(false);
                                smallRadio.setVisibility(View.INVISIBLE);
                                bigRadio.setEnabled(false);
                                bigRadio.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                }
                return true;
            }
        });

        flavor7Button.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    downTime = System.currentTimeMillis();

                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);

                        View customView = inflater.inflate(R.layout.pop_up, null);

                        popupWindow = new PopupWindow(
                                customView,
                                400,
                                400
                        );

                        ((TextView) popupWindow.getContentView().findViewById(R.id.theGreatPopUp)).setText(desc7);

                        popupWindow.showAtLocation(popUpRelativeLayout, Gravity.RIGHT, 100, 0);
                    }
                }

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    upTime = System.currentTimeMillis();
                    popupWindow.dismiss();
                    diff = upTime - downTime;

                    if (diff < 300) {
                        if (flavor7Button.getCurrentTextColor() == getResources().getColor(R.color.green)) {

                            popupBox = 7;

                            if (CountSelectedSyrups < 2) {
                                flavor7Button.setTextColor(getResources().getColor(R.color.white));
                                flavor7Button.setBackgroundColor(getResources().getColor(R.color.green));

                                smallRadio.setVisibility(View.VISIBLE);
                                bigRadio.setVisibility(View.VISIBLE);
                                smallRadio.setEnabled(true);
                                bigRadio.setEnabled(true);

                                CountSelectedSyrups++;

                                if (CountSelectedSyrups == 1) {
                                    syrupSelectedTV1.setText(String.valueOf(flavor7Button.getText()));
                                    syrupSelectedTV1.setVisibility(View.VISIBLE);
                                    syrupSelectedSeek1.setVisibility(View.VISIBLE);
                                    syrupSelectedSeek1.setProgress(0);
                                } else if (CountSelectedSyrups == 2) {
                                    syrupSelectedTV2.setText(String.valueOf(flavor7Button.getText()));
                                    syrupSelectedTV2.setVisibility(View.VISIBLE);
                                    syrupSelectedSeek2.setVisibility(View.VISIBLE);
                                    syrupSelectedSeek2.setProgress(0);
                                }
                            }
                        } else {
                            flavor7Button.setTextColor(getResources().getColor(R.color.green));
                            flavor7Button.setBackgroundColor(getResources().getColor(R.color.white));
                            CountSelectedSyrups--;

                            if (CountSelectedSyrups == 1) {
                                if (flavor7Button.getText() == syrupSelectedTV1.getText()) {
                                    syrupSelectedTV1.setText(String.valueOf(syrupSelectedTV2.getText()));
                                    syrupSelectedSeek1.setProgress(syrupSelectedSeek2.getProgress());
                                    syrupSelectedSeek2.setVisibility(View.INVISIBLE);
                                    syrupSelectedTV2.setVisibility(View.INVISIBLE);
                                } else {
                                    syrupSelectedTV2.setVisibility(View.INVISIBLE);
                                    syrupSelectedSeek2.setVisibility(View.INVISIBLE);
                                }
                            } else if (CountSelectedSyrups == 0) {
                                syrupSelectedTV1.setVisibility(View.INVISIBLE);
                                syrupSelectedSeek1.setVisibility(View.INVISIBLE);
                                syrupSelectedSeek2.setVisibility(View.INVISIBLE);
                                syrupSelectedTV2.setVisibility(View.INVISIBLE);

                                smallRadio.setChecked(true);

                                smallRadio.setEnabled(false);
                                smallRadio.setVisibility(View.INVISIBLE);
                                bigRadio.setEnabled(false);
                                bigRadio.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                }
                return true;
            }
        });

        flavor8Button.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    downTime = System.currentTimeMillis();

                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);

                        View customView = inflater.inflate(R.layout.pop_up, null);

                        popupWindow = new PopupWindow(
                                customView,
                                400,
                                400
                        );

                        ((TextView) popupWindow.getContentView().findViewById(R.id.theGreatPopUp)).setText(desc8);

                        popupWindow.showAtLocation(popUpRelativeLayout, Gravity.RIGHT, 100, 0);
                    }
                }

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    upTime = System.currentTimeMillis();
                    popupWindow.dismiss();
                    diff = upTime - downTime;

                    if (diff < 300) {
                        if (flavor8Button.getCurrentTextColor() == getResources().getColor(R.color.green)) {

                            popupBox = 8;

                            if (CountSelectedSyrups < 2) {
                                flavor8Button.setTextColor(getResources().getColor(R.color.white));
                                flavor8Button.setBackgroundColor(getResources().getColor(R.color.green));

                                smallRadio.setVisibility(View.VISIBLE);
                                bigRadio.setVisibility(View.VISIBLE);
                                smallRadio.setEnabled(true);
                                bigRadio.setEnabled(true);

                                CountSelectedSyrups++;

                                if (CountSelectedSyrups == 1) {
                                    syrupSelectedTV1.setText(String.valueOf(flavor8Button.getText()));
                                    syrupSelectedTV1.setVisibility(View.VISIBLE);
                                    syrupSelectedSeek1.setVisibility(View.VISIBLE);
                                    syrupSelectedSeek1.setProgress(0);
                                } else if (CountSelectedSyrups == 2) {
                                    syrupSelectedTV2.setText(String.valueOf(flavor8Button.getText()));
                                    syrupSelectedTV2.setVisibility(View.VISIBLE);
                                    syrupSelectedSeek2.setVisibility(View.VISIBLE);
                                    syrupSelectedSeek2.setProgress(0);
                                }
                            }
                        } else {
                            flavor8Button.setTextColor(getResources().getColor(R.color.green));
                            flavor8Button.setBackgroundColor(getResources().getColor(R.color.white));
                            CountSelectedSyrups--;

                            if (CountSelectedSyrups == 1) {
                                if (flavor8Button.getText() == syrupSelectedTV1.getText()) {
                                    syrupSelectedTV1.setText(String.valueOf(syrupSelectedTV2.getText()));
                                    syrupSelectedSeek1.setProgress(syrupSelectedSeek2.getProgress());
                                    syrupSelectedSeek2.setVisibility(View.INVISIBLE);
                                    syrupSelectedTV2.setVisibility(View.INVISIBLE);
                                } else {
                                    syrupSelectedTV2.setVisibility(View.INVISIBLE);
                                    syrupSelectedSeek2.setVisibility(View.INVISIBLE);
                                }
                            } else if (CountSelectedSyrups == 0) {
                                syrupSelectedTV1.setVisibility(View.INVISIBLE);
                                syrupSelectedSeek1.setVisibility(View.INVISIBLE);
                                syrupSelectedSeek2.setVisibility(View.INVISIBLE);
                                syrupSelectedTV2.setVisibility(View.INVISIBLE);

                                smallRadio.setChecked(true);

                                smallRadio.setEnabled(false);
                                smallRadio.setVisibility(View.INVISIBLE);
                                bigRadio.setEnabled(false);
                                bigRadio.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                }
                return true;
            }
        });

        flavor9Button.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    downTime = System.currentTimeMillis();

                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);

                        View customView = inflater.inflate(R.layout.pop_up, null);

                        popupWindow = new PopupWindow(
                                customView,
                                400,
                                400
                        );

                        ((TextView) popupWindow.getContentView().findViewById(R.id.theGreatPopUp)).setText(desc9);

                        popupWindow.showAtLocation(popUpRelativeLayout, Gravity.RIGHT, 100, 0);
                    }
                }

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    upTime = System.currentTimeMillis();
                    popupWindow.dismiss();
                    diff = upTime - downTime;

                    if (diff < 300) {
                        if (flavor9Button.getCurrentTextColor() == getResources().getColor(R.color.green)) {

                            popupBox = 9;

                            if (CountSelectedSyrups < 2) {
                                flavor9Button.setTextColor(getResources().getColor(R.color.white));
                                flavor9Button.setBackgroundColor(getResources().getColor(R.color.green));

                                smallRadio.setVisibility(View.VISIBLE);
                                bigRadio.setVisibility(View.VISIBLE);
                                smallRadio.setEnabled(true);
                                bigRadio.setEnabled(true);

                                CountSelectedSyrups++;

                                if (CountSelectedSyrups == 1) {
                                    syrupSelectedTV1.setText(String.valueOf(flavor9Button.getText()));
                                    syrupSelectedTV1.setVisibility(View.VISIBLE);
                                    syrupSelectedSeek1.setVisibility(View.VISIBLE);
                                    syrupSelectedSeek1.setProgress(0);
                                } else if (CountSelectedSyrups == 2) {
                                    syrupSelectedTV2.setText(String.valueOf(flavor9Button.getText()));
                                    syrupSelectedTV2.setVisibility(View.VISIBLE);
                                    syrupSelectedSeek2.setVisibility(View.VISIBLE);
                                    syrupSelectedSeek2.setProgress(0);
                                }
                            }
                        } else {
                            flavor9Button.setTextColor(getResources().getColor(R.color.green));
                            flavor9Button.setBackgroundColor(getResources().getColor(R.color.white));
                            CountSelectedSyrups--;

                            if (CountSelectedSyrups == 1) {
                                if (flavor9Button.getText() == syrupSelectedTV1.getText()) {
                                    syrupSelectedTV1.setText(String.valueOf(syrupSelectedTV2.getText()));
                                    syrupSelectedSeek1.setProgress(syrupSelectedSeek2.getProgress());
                                    syrupSelectedSeek2.setVisibility(View.INVISIBLE);
                                    syrupSelectedTV2.setVisibility(View.INVISIBLE);
                                } else {
                                    syrupSelectedTV2.setVisibility(View.INVISIBLE);
                                    syrupSelectedSeek2.setVisibility(View.INVISIBLE);
                                }
                            } else if (CountSelectedSyrups == 0) {
                                syrupSelectedTV1.setVisibility(View.INVISIBLE);
                                syrupSelectedSeek1.setVisibility(View.INVISIBLE);
                                syrupSelectedSeek2.setVisibility(View.INVISIBLE);
                                syrupSelectedTV2.setVisibility(View.INVISIBLE);

                                smallRadio.setChecked(true);

                                smallRadio.setEnabled(false);
                                smallRadio.setVisibility(View.INVISIBLE);
                                bigRadio.setEnabled(false);
                                bigRadio.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                }
                return true;
            }
        });

        flavor10Button.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    downTime = System.currentTimeMillis();

                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);

                        View customView = inflater.inflate(R.layout.pop_up, null);

                        popupWindow = new PopupWindow(
                                customView,
                                400,
                                400
                        );

                        ((TextView) popupWindow.getContentView().findViewById(R.id.theGreatPopUp)).setText(desc10);

                        popupWindow.showAtLocation(popUpRelativeLayout, Gravity.RIGHT, 100, 0);
                    }
                }

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    upTime = System.currentTimeMillis();
                    popupWindow.dismiss();
                    diff = upTime - downTime;

                    if (diff < 300) {
                        if (flavor10Button.getCurrentTextColor() == getResources().getColor(R.color.green)) {

                            popupBox = 10;

                            if (CountSelectedSyrups < 2) {
                                flavor10Button.setTextColor(getResources().getColor(R.color.white));
                                flavor10Button.setBackgroundColor(getResources().getColor(R.color.green));

                                smallRadio.setVisibility(View.VISIBLE);
                                bigRadio.setVisibility(View.VISIBLE);
                                smallRadio.setEnabled(true);
                                bigRadio.setEnabled(true);

                                CountSelectedSyrups++;

                                if (CountSelectedSyrups == 1) {
                                    syrupSelectedTV1.setText(String.valueOf(flavor10Button.getText()));
                                    syrupSelectedTV1.setVisibility(View.VISIBLE);
                                    syrupSelectedSeek1.setVisibility(View.VISIBLE);
                                    syrupSelectedSeek1.setProgress(0);
                                } else if (CountSelectedSyrups == 2) {
                                    syrupSelectedTV2.setText(String.valueOf(flavor10Button.getText()));
                                    syrupSelectedTV2.setVisibility(View.VISIBLE);
                                    syrupSelectedSeek2.setVisibility(View.VISIBLE);
                                    syrupSelectedSeek2.setProgress(0);
                                }
                            }
                        } else {
                            flavor10Button.setTextColor(getResources().getColor(R.color.green));
                            flavor10Button.setBackgroundColor(getResources().getColor(R.color.white));
                            CountSelectedSyrups--;

                            if (CountSelectedSyrups == 1) {
                                if (flavor10Button.getText() == syrupSelectedTV1.getText()) {
                                    syrupSelectedTV1.setText(String.valueOf(syrupSelectedTV2.getText()));
                                    syrupSelectedSeek1.setProgress(syrupSelectedSeek2.getProgress());
                                    syrupSelectedSeek2.setVisibility(View.INVISIBLE);
                                    syrupSelectedTV2.setVisibility(View.INVISIBLE);
                                } else {
                                    syrupSelectedTV2.setVisibility(View.INVISIBLE);
                                    syrupSelectedSeek2.setVisibility(View.INVISIBLE);
                                }
                            } else if (CountSelectedSyrups == 0) {
                                syrupSelectedTV1.setVisibility(View.INVISIBLE);
                                syrupSelectedSeek1.setVisibility(View.INVISIBLE);
                                syrupSelectedSeek2.setVisibility(View.INVISIBLE);
                                syrupSelectedTV2.setVisibility(View.INVISIBLE);

                                smallRadio.setChecked(true);

                                smallRadio.setEnabled(false);
                                smallRadio.setVisibility(View.INVISIBLE);
                                bigRadio.setEnabled(false);
                                bigRadio.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                }
                return true;
            }
        });

        flavor11Button.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    downTime = System.currentTimeMillis();

                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);

                        View customView = inflater.inflate(R.layout.pop_up, null);

                        popupWindow = new PopupWindow(
                                customView,
                                400,
                                400
                        );

                        ((TextView) popupWindow.getContentView().findViewById(R.id.theGreatPopUp)).setText(desc11);

                        popupWindow.showAtLocation(popUpRelativeLayout, Gravity.RIGHT, 100, 0);
                    }
                }

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    upTime = System.currentTimeMillis();
                    popupWindow.dismiss();
                    diff = upTime - downTime;

                    if (diff < 300) {
                        if (flavor11Button.getCurrentTextColor() == getResources().getColor(R.color.green)) {

                            popupBox = 11;

                            if (CountSelectedSyrups < 2) {
                                flavor11Button.setTextColor(getResources().getColor(R.color.white));
                                flavor11Button.setBackgroundColor(getResources().getColor(R.color.green));

                                smallRadio.setVisibility(View.VISIBLE);
                                bigRadio.setVisibility(View.VISIBLE);
                                smallRadio.setEnabled(true);
                                bigRadio.setEnabled(true);

                                CountSelectedSyrups++;

                                if (CountSelectedSyrups == 1) {
                                    syrupSelectedTV1.setText(String.valueOf(flavor11Button.getText()));
                                    syrupSelectedTV1.setVisibility(View.VISIBLE);
                                    syrupSelectedSeek1.setVisibility(View.VISIBLE);
                                    syrupSelectedSeek1.setProgress(0);
                                } else if (CountSelectedSyrups == 2) {
                                    syrupSelectedTV2.setText(String.valueOf(flavor11Button.getText()));
                                    syrupSelectedTV2.setVisibility(View.VISIBLE);
                                    syrupSelectedSeek2.setVisibility(View.VISIBLE);
                                    syrupSelectedSeek2.setProgress(0);
                                }
                            }
                        } else {
                            flavor11Button.setTextColor(getResources().getColor(R.color.green));
                            flavor11Button.setBackgroundColor(getResources().getColor(R.color.white));
                            CountSelectedSyrups--;

                            if (CountSelectedSyrups == 1) {
                                if (flavor11Button.getText() == syrupSelectedTV1.getText()) {
                                    syrupSelectedTV1.setText(String.valueOf(syrupSelectedTV2.getText()));
                                    syrupSelectedSeek1.setProgress(syrupSelectedSeek2.getProgress());
                                    syrupSelectedSeek2.setVisibility(View.INVISIBLE);
                                    syrupSelectedTV2.setVisibility(View.INVISIBLE);
                                } else {
                                    syrupSelectedTV2.setVisibility(View.INVISIBLE);
                                    syrupSelectedSeek2.setVisibility(View.INVISIBLE);
                                }
                            } else if (CountSelectedSyrups == 0) {
                                syrupSelectedTV1.setVisibility(View.INVISIBLE);
                                syrupSelectedSeek1.setVisibility(View.INVISIBLE);
                                syrupSelectedSeek2.setVisibility(View.INVISIBLE);
                                syrupSelectedTV2.setVisibility(View.INVISIBLE);

                                smallRadio.setChecked(true);

                                smallRadio.setEnabled(false);
                                smallRadio.setVisibility(View.INVISIBLE);
                                bigRadio.setEnabled(false);
                                bigRadio.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                }
                return true;
            }
        });

        flavor12Button.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    downTime = System.currentTimeMillis();

                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);

                        View customView = inflater.inflate(R.layout.pop_up, null);

                        popupWindow = new PopupWindow(
                                customView,
                                400,
                                400
                        );

                        ((TextView) popupWindow.getContentView().findViewById(R.id.theGreatPopUp)).setText(desc12);

                        popupWindow.showAtLocation(popUpRelativeLayout, Gravity.RIGHT, 100, 0);
                    }
                }

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    upTime = System.currentTimeMillis();
                    popupWindow.dismiss();
                    diff = upTime - downTime;

                    if (diff < 300) {
                        if (flavor12Button.getCurrentTextColor() == getResources().getColor(R.color.green)) {

                            popupBox = 12;

                            if (CountSelectedSyrups < 2) {
                                flavor12Button.setTextColor(getResources().getColor(R.color.white));
                                flavor12Button.setBackgroundColor(getResources().getColor(R.color.green));

                                smallRadio.setVisibility(View.VISIBLE);
                                bigRadio.setVisibility(View.VISIBLE);
                                smallRadio.setEnabled(true);
                                bigRadio.setEnabled(true);

                                CountSelectedSyrups++;

                                if (CountSelectedSyrups == 1) {
                                    syrupSelectedTV1.setText(String.valueOf(flavor12Button.getText()));
                                    syrupSelectedTV1.setVisibility(View.VISIBLE);
                                    syrupSelectedSeek1.setVisibility(View.VISIBLE);
                                    syrupSelectedSeek1.setProgress(0);
                                } else if (CountSelectedSyrups == 2) {
                                    syrupSelectedTV2.setText(String.valueOf(flavor12Button.getText()));
                                    syrupSelectedTV2.setVisibility(View.VISIBLE);
                                    syrupSelectedSeek2.setVisibility(View.VISIBLE);
                                    syrupSelectedSeek2.setProgress(0);
                                }
                            }
                        } else {
                            flavor12Button.setTextColor(getResources().getColor(R.color.green));
                            flavor12Button.setBackgroundColor(getResources().getColor(R.color.white));
                            CountSelectedSyrups--;

                            if (CountSelectedSyrups == 1) {
                                if (flavor12Button.getText() == syrupSelectedTV1.getText()) {
                                    syrupSelectedTV1.setText(String.valueOf(syrupSelectedTV2.getText()));
                                    syrupSelectedSeek1.setProgress(syrupSelectedSeek2.getProgress());
                                    syrupSelectedSeek2.setVisibility(View.INVISIBLE);
                                    syrupSelectedTV2.setVisibility(View.INVISIBLE);
                                } else {
                                    syrupSelectedTV2.setVisibility(View.INVISIBLE);
                                    syrupSelectedSeek2.setVisibility(View.INVISIBLE);
                                }
                            } else if (CountSelectedSyrups == 0) {
                                syrupSelectedTV1.setVisibility(View.INVISIBLE);
                                syrupSelectedSeek1.setVisibility(View.INVISIBLE);
                                syrupSelectedSeek2.setVisibility(View.INVISIBLE);
                                syrupSelectedTV2.setVisibility(View.INVISIBLE);

                                smallRadio.setChecked(true);

                                smallRadio.setEnabled(false);
                                smallRadio.setVisibility(View.INVISIBLE);
                                bigRadio.setEnabled(false);
                                bigRadio.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                }
                return true;
            }
        });

        syrupSelectedSeek1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (seekBar.getProgress() < 1)
                    seekBar.setProgress(1);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (seekBar.getProgress() < 1)
                    seekBar.setProgress(1);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (seekBar.getProgress() < 1)
                    seekBar.setProgress(1);
            }
        });

        syrupSelectedSeek2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (seekBar.getProgress() < 1)
                    seekBar.setProgress(1);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (seekBar.getProgress() < 1)
                    seekBar.setProgress(1);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (seekBar.getProgress() < 1)
                    seekBar.setProgress(1);
            }
        });
    }

    private void setOnApproveButtonListener() {
        approveButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (CountSelectedSyrups == 0)
                            Toast.makeText(MainActivity.this, "Select At Least 1 Flavor", Toast.LENGTH_SHORT).show();

                        else if (CountSelectedSyrups == 1) {
                            AlertDialog.Builder a_builder = new AlertDialog.Builder(MainActivity.this);
                            a_builder.setMessage("Continue With 1 Syrup Only?").setCancelable(true)
                                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            if (!(flavor1Button.getCurrentTextColor() == getResources().getColor(R.color.green))) {
                                                toSend = "01";
                                                toSend += String.valueOf(syrupSelectedSeek1.getProgress());
                                            }

                                            if (!(flavor2Button.getCurrentTextColor() == getResources().getColor(R.color.green))) {
                                                toSend = "02";
                                                toSend += String.valueOf(syrupSelectedSeek1.getProgress());
                                            }

                                            if (!(flavor3Button.getCurrentTextColor() == getResources().getColor(R.color.green))) {
                                                toSend = "03";
                                                toSend += String.valueOf(syrupSelectedSeek1.getProgress());
                                            }

                                            if (!(flavor4Button.getCurrentTextColor() == getResources().getColor(R.color.green))) {
                                                toSend = "04";
                                                toSend += String.valueOf(syrupSelectedSeek1.getProgress());
                                            }

                                            if (!(flavor5Button.getCurrentTextColor() == getResources().getColor(R.color.green))) {
                                                toSend = "05";
                                                toSend += String.valueOf(syrupSelectedSeek1.getProgress());
                                            }

                                            if (!(flavor6Button.getCurrentTextColor() == getResources().getColor(R.color.green))) {
                                                toSend = "06";
                                                toSend += String.valueOf(syrupSelectedSeek1.getProgress());
                                            }

                                            if (!(flavor7Button.getCurrentTextColor() == getResources().getColor(R.color.green))) {
                                                toSend = "07";
                                                toSend += String.valueOf(syrupSelectedSeek1.getProgress());
                                            }

                                            if (!(flavor8Button.getCurrentTextColor() == getResources().getColor(R.color.green))) {
                                                toSend = "08";
                                                toSend += String.valueOf(syrupSelectedSeek1.getProgress());
                                            }

                                            if (!(flavor9Button.getCurrentTextColor() == getResources().getColor(R.color.green))) {
                                                toSend = "09";
                                                toSend += String.valueOf(syrupSelectedSeek1.getProgress());
                                            }

                                            if (!(flavor10Button.getCurrentTextColor() == getResources().getColor(R.color.green))) {
                                                toSend = "10";
                                                toSend += String.valueOf(syrupSelectedSeek1.getProgress());
                                            }

                                            if (!(flavor11Button.getCurrentTextColor() == getResources().getColor(R.color.green))) {
                                                toSend = "11";
                                                toSend += String.valueOf(syrupSelectedSeek1.getProgress());
                                            }

                                            if (!(flavor12Button.getCurrentTextColor() == getResources().getColor(R.color.green))) {
                                                toSend = "12";
                                                toSend += String.valueOf(syrupSelectedSeek1.getProgress());
                                            }

                                            if (smallRadio.isChecked())
                                                toSend += "1";
                                            else
                                                toSend += "2";

                                            ApproveCalled();
                                            write(toSend.getBytes());

                                        }
                                    })
                                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    });

                            AlertDialog alert = a_builder.create();
                            alert.setTitle("Only 1 Syrup Selected");
                            alert.show();
                        } else {
                            if (!(flavor1Button.getCurrentTextColor() == getResources().getColor(R.color.green))) {
                                toSend += "01";

                                if (toSend.length() == 2)
                                    toSend += String.valueOf(syrupSelectedSeek1.getProgress());
                                else
                                    toSend += String.valueOf(syrupSelectedSeek2.getProgress());
                            }

                            if (!(flavor2Button.getCurrentTextColor() == getResources().getColor(R.color.green))) {
                                toSend += "02";

                                if (toSend.length() == 2)
                                    toSend += String.valueOf(syrupSelectedSeek1.getProgress());
                                else
                                    toSend += String.valueOf(syrupSelectedSeek2.getProgress());
                            }

                            if (!(flavor3Button.getCurrentTextColor() == getResources().getColor(R.color.green))) {
                                toSend += "03";

                                if (toSend.length() == 2)
                                    toSend += String.valueOf(syrupSelectedSeek1.getProgress());
                                else
                                    toSend += String.valueOf(syrupSelectedSeek2.getProgress());
                            }

                            if (!(flavor4Button.getCurrentTextColor() == getResources().getColor(R.color.green))) {
                                toSend += "04";

                                if (toSend.length() == 2)
                                    toSend += String.valueOf(syrupSelectedSeek1.getProgress());
                                else
                                    toSend += String.valueOf(syrupSelectedSeek2.getProgress());
                            }

                            if (!(flavor5Button.getCurrentTextColor() == getResources().getColor(R.color.green))) {
                                toSend += "05";

                                if (toSend.length() == 2)
                                    toSend += String.valueOf(syrupSelectedSeek1.getProgress());
                                else
                                    toSend += String.valueOf(syrupSelectedSeek2.getProgress());
                            }

                            if (!(flavor6Button.getCurrentTextColor() == getResources().getColor(R.color.green))) {
                                toSend += "06";

                                if (toSend.length() == 2)
                                    toSend += String.valueOf(syrupSelectedSeek1.getProgress());
                                else
                                    toSend += String.valueOf(syrupSelectedSeek2.getProgress());
                            }

                            if (!(flavor7Button.getCurrentTextColor() == getResources().getColor(R.color.green))) {
                                toSend += "07";

                                if (toSend.length() == 2)
                                    toSend += String.valueOf(syrupSelectedSeek1.getProgress());
                                else
                                    toSend += String.valueOf(syrupSelectedSeek2.getProgress());
                            }

                            if (!(flavor8Button.getCurrentTextColor() == getResources().getColor(R.color.green))) {
                                toSend += "08";

                                if (toSend.length() == 2)
                                    toSend += String.valueOf(syrupSelectedSeek1.getProgress());
                                else
                                    toSend += String.valueOf(syrupSelectedSeek2.getProgress());
                            }

                            if (!(flavor9Button.getCurrentTextColor() == getResources().getColor(R.color.green))) {
                                toSend += "09";

                                if (toSend.length() == 2)
                                    toSend += String.valueOf(syrupSelectedSeek1.getProgress());
                                else
                                    toSend += String.valueOf(syrupSelectedSeek2.getProgress());
                            }

                            if (!(flavor10Button.getCurrentTextColor() == getResources().getColor(R.color.green))) {
                                toSend += "10";

                                if (toSend.length() == 2)
                                    toSend += String.valueOf(syrupSelectedSeek1.getProgress());
                                else
                                    toSend += String.valueOf(syrupSelectedSeek2.getProgress());
                            }

                            if (!(flavor11Button.getCurrentTextColor() == getResources().getColor(R.color.green))) {
                                toSend += "11";

                                if (toSend.length() == 2)
                                    toSend += String.valueOf(syrupSelectedSeek1.getProgress());
                                else
                                    toSend += String.valueOf(syrupSelectedSeek2.getProgress());
                            }

                            if (!(flavor12Button.getCurrentTextColor() == getResources().getColor(R.color.green))) {
                                toSend += "12";

                                if (toSend.length() == 2)
                                    toSend += String.valueOf(syrupSelectedSeek1.getProgress());
                                else
                                    toSend += String.valueOf(syrupSelectedSeek2.getProgress());
                            }

                            if (smallRadio.isChecked())
                                toSend += "1";
                            else
                                toSend += "2";

                            ApproveCalled();
                            write(toSend.getBytes());
                        }
                    }
                });
    }

    private void setProgressBar() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        progressTV = (TextView) findViewById(R.id.textViewForProgress);
        progressTV.setVisibility(View.INVISIBLE);
    }

    private void init() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            Toast.makeText(this, "This Device Doesn't Support Bluetooth", Toast.LENGTH_SHORT).show();
            finish();
        } else if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        } else
            goFurther();
    }

    private void ApproveCalled() {
        progressBar.setVisibility(View.VISIBLE);
        progressTV.setVisibility(View.VISIBLE);
        syrupSelectedSeek1.setEnabled(false);
        syrupSelectedSeek2.setEnabled(false);
        flavor1Button.setEnabled(false);
        flavor2Button.setEnabled(false);
        flavor3Button.setEnabled(false);
        flavor4Button.setEnabled(false);
        flavor5Button.setEnabled(false);
        flavor6Button.setEnabled(false);
        flavor7Button.setEnabled(false);
        flavor8Button.setEnabled(false);
        flavor9Button.setEnabled(false);
        flavor10Button.setEnabled(false);
        flavor11Button.setEnabled(false);
        flavor12Button.setEnabled(false);
        smallRadio.setEnabled(false);
        bigRadio.setEnabled(false);
        approveButton.setEnabled(false);
    }

    private void ProcessCompleted() {
        progressTV.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        syrupSelectedSeek1.setEnabled(true);
        syrupSelectedSeek2.setEnabled(true);
        flavor1Button.setEnabled(true);
        flavor2Button.setEnabled(true);
        flavor3Button.setEnabled(true);
        flavor4Button.setEnabled(true);
        flavor5Button.setEnabled(true);
        flavor6Button.setEnabled(true);
        flavor7Button.setEnabled(true);
        flavor8Button.setEnabled(true);
        flavor9Button.setEnabled(true);
        flavor10Button.setEnabled(true);
        flavor11Button.setEnabled(true);
        flavor12Button.setEnabled(true);
        smallRadio.setEnabled(true);
        bigRadio.setEnabled(true);
        approveButton.setEnabled(true);

        if (!smallRadio.isChecked()) {
            bigRadio.setChecked(false);
            smallRadio.setChecked(true);
        }

        syrupSelectedSeek1.setVisibility(View.INVISIBLE);
        syrupSelectedSeek2.setVisibility(View.INVISIBLE);
        syrupSelectedTV1.setVisibility(View.INVISIBLE);
        syrupSelectedTV2.setVisibility(View.INVISIBLE);
        smallRadio.setVisibility(View.INVISIBLE);
        bigRadio.setVisibility(View.INVISIBLE);

        syrupSelectedSeek1.setProgress(1);
        syrupSelectedSeek2.setProgress(1);

        CountSelectedSyrups = 0;
        toSend = "";

        flavor1Button.setTextColor(getResources().getColor(R.color.green));
        flavor1Button.setBackgroundColor(getResources().getColor(R.color.white));

        flavor2Button.setTextColor(getResources().getColor(R.color.green));
        flavor2Button.setBackgroundColor(getResources().getColor(R.color.white));

        flavor3Button.setTextColor(getResources().getColor(R.color.green));
        flavor3Button.setBackgroundColor(getResources().getColor(R.color.white));

        flavor4Button.setTextColor(getResources().getColor(R.color.green));
        flavor4Button.setBackgroundColor(getResources().getColor(R.color.white));

        flavor5Button.setTextColor(getResources().getColor(R.color.green));
        flavor5Button.setBackgroundColor(getResources().getColor(R.color.white));

        flavor6Button.setTextColor(getResources().getColor(R.color.green));
        flavor6Button.setBackgroundColor(getResources().getColor(R.color.white));

        flavor7Button.setTextColor(getResources().getColor(R.color.green));
        flavor7Button.setBackgroundColor(getResources().getColor(R.color.white));

        flavor8Button.setTextColor(getResources().getColor(R.color.green));
        flavor8Button.setBackgroundColor(getResources().getColor(R.color.white));

        flavor9Button.setTextColor(getResources().getColor(R.color.green));
        flavor9Button.setBackgroundColor(getResources().getColor(R.color.white));

        flavor10Button.setTextColor(getResources().getColor(R.color.green));
        flavor10Button.setBackgroundColor(getResources().getColor(R.color.white));

        flavor11Button.setTextColor(getResources().getColor(R.color.green));
        flavor11Button.setBackgroundColor(getResources().getColor(R.color.white));

        flavor12Button.setTextColor(getResources().getColor(R.color.green));
        flavor12Button.setBackgroundColor(getResources().getColor(R.color.white));

        setNamesOfButtons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.fullscreen_menu, menu);
        playMenu = menu.findItem(R.id.connection_id);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.settings_id:

                manualDisconnection = true;

                try {
                    mmOutStream.close();
                    mmInStream.close();
                    mmSocket.close();
                    connectedThread.interrupt();
                    connectedThread.destroy();
                    tempthread.interrupt();
                    tempthread.destroy();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent SettingsIntent = new Intent("com.example.anujdawar.slicendice.loginActivity");
                        startActivity(SettingsIntent);
                        finish();
                    }
                }, 0);

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //      bluetooth wala part :

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_OK)
            goFurther();

        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Bluetooth must be enabled to continue", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void goFurther() {
        pairedDevices = bluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0)
            for (final BluetoothDevice device : pairedDevices)
                if (device.getAddress().equals("B8:27:EB:99:69:2D")) //|| device.getName().contains("berry"))
                {
                    tempDevice = device;
                    callThread(device);
                }
    }

    private void callThread(BluetoothDevice device)
    {
        BluetoothSocket tmp = null;
        mmDevice = device;

        try {
            tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (Exception e) {
            Log.e(TAG, "Socket's create() method failed", e);
            Toast.makeText(MainActivity.this, "Socket's create() method failed", Toast.LENGTH_SHORT).show();
        }

        mmSocket = tmp;

        tempthread = new Thread(new Runnable() {
            @Override
            public void run() {

                bluetoothAdapter.cancelDiscovery();

                tempVariable++;
                getOuttaLoop = false;

                while (!getOuttaLoop) {
                    try {
                        mmSocket.connect();
                    } catch (IOException connectException) {
                        try {
                            Log.e("", "trying fallback...");
                            mmSocket = (BluetoothSocket) mmDevice.getClass().getMethod("createRfcommSocket", new Class[]{int.class}).invoke(mmDevice, 1);
                            mmSocket.connect();
                            Log.e("", "Connected");
                            getOuttaLoop = true;
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            try {
                                getOuttaLoop = false;
                                mmSocket.close();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            e.printStackTrace();
                        }
                        manageMyConnectedSocket(mmSocket);
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        tempthread.start();
    }

    private void manageMyConnectedSocket(BluetoothSocket mmSocket)
    {
        if(getOuttaLoop)
        {
            handler1.sendEmptyMessage(0);
            connectedThreadFunction(mmSocket);
        }

        else
            handler2.sendEmptyMessage(0);
    }

    //         MANAGING      --------------

    Handler handler1 = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            playMenu.setIcon(R.drawable.greencircle);

            // set all buttons as enabled, except for the outta stock buttons..
            // i.e starting me saare disabled honge, fir jb connect ho jayenge toh yaha se enable disable honge..

            progressTV.setVisibility(View.INVISIBLE);
            syrupSelectedSeek1.setEnabled(true);
            syrupSelectedSeek2.setEnabled(true);
            flavor1Button.setEnabled(true);
            flavor2Button.setEnabled(true);
            flavor3Button.setEnabled(true);
            flavor4Button.setEnabled(true);
            flavor5Button.setEnabled(true);
            flavor6Button.setEnabled(true);
            flavor7Button.setEnabled(true);
            flavor8Button.setEnabled(true);
            flavor9Button.setEnabled(true);
            flavor10Button.setEnabled(true);
            flavor11Button.setEnabled(true);
            flavor12Button.setEnabled(true);
            smallRadio.setEnabled(true);
            bigRadio.setEnabled(true);
            approveButton.setEnabled(true);

            setNamesOfButtons();

            getOuttaLoop = true;
        }
    };

    Handler handler2 = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            playMenu.setIcon(R.drawable.redcircle);
            // set all buttons as disabled, alpha to low, if possible

            progressTV.setVisibility(View.VISIBLE);
            syrupSelectedSeek1.setEnabled(false);
            syrupSelectedSeek2.setEnabled(false);
            flavor1Button.setEnabled(false);
            flavor2Button.setEnabled(false);
            flavor3Button.setEnabled(false);
            flavor4Button.setEnabled(false);
            flavor5Button.setEnabled(false);
            flavor6Button.setEnabled(false);
            flavor7Button.setEnabled(false);
            flavor8Button.setEnabled(false);
            flavor9Button.setEnabled(false);
            flavor10Button.setEnabled(false);
            flavor11Button.setEnabled(false);
            flavor12Button.setEnabled(false);
            smallRadio.setEnabled(false);
            bigRadio.setEnabled(false);
            approveButton.setEnabled(false);

            getOuttaLoop = false;

        }
    };

    private interface MessageConstants {
        public static final int MESSAGE_READ = 0;
        public static final int MESSAGE_WRITE = 1;
        public static final int MESSAGE_TOAST = 2;

        // ... (Add other message types here as needed.)
    }

    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            String data = msg.obj.toString();
//            Toast.makeText(MainActivity.this, data, Toast.LENGTH_SHORT).show();
            byte[] readBuf = (byte[])msg.obj;
            String string = new String(readBuf);

            if(string.contains("DONE"))
                string = "ThankYou";

            Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();

            ProcessCompleted();
        }
    };

    public void connectedThreadFunction(BluetoothSocket socket)
    {
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        try {
            tmpIn = socket.getInputStream();
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when creating input stream", e);
        }
        try {
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when creating output stream", e);
        }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;

        connectedThread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                mmBuffer = new byte[1024];
                int numBytes; // bytes returned from read()

                while (true) {
                    try {
                        numBytes = mmInStream.read(mmBuffer);

                        Message readMsg = mHandler.obtainMessage(
                                MessageConstants.MESSAGE_READ, numBytes, -1,
                                mmBuffer);
                        readMsg.sendToTarget();
                    } catch (IOException e) {
                        Log.d(TAG, "Input stream was disconnected", e);
                        handler2.sendEmptyMessage(0);
                        if(!manualDisconnection)
                            goFurther();
                        break;
                    }
                }
            }
        });
        connectedThread.start();
    }

    public void write(byte[] bytes) {
        try {
            mmOutStream.write(bytes);

//            Message writtenMsg = mHandler.obtainMessage(
//                    MessageConstants.MESSAGE_WRITE, -1, -1, mmBuffer);
//            writtenMsg.sendToTarget();
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when sending data", e);

//            Message writeErrorMsg =
//                    mHandler.obtainMessage(MessageConstants.MESSAGE_TOAST);
//            Bundle bundle = new Bundle();
//            bundle.putString("toast",
//                    "Couldn't send data to the other device");
//            writeErrorMsg.setData(bundle);
//            mHandler.sendMessage(writeErrorMsg);
        }
    }
}