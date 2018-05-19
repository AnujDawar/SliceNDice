package com.example.anujdawar.slicendice;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class configClass extends AppCompatActivity {

    private Button flavor1Button, flavor2Button, flavor3Button, flavor4Button, flavor5Button, flavor6Button,
            flavor7Button, flavor8Button, flavor9Button, flavor10Button, flavor11Button, flavor12Button;

    private EditText popupEditText;

    protected EditText displaySyrupSelected;
    protected CheckBox EnableDisableCheckBox;
    protected boolean foundInDataBaseFlag = false;

    protected int whichButtonSelectedToUpdate = 0;

    protected Button approveButton;
    protected boolean isSelectedAlreadySyrupButton = false;

    public DatabaseClassCommon db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config_activity);

        db = new DatabaseClassCommon(this);

        setButtons();
        setNamesOfButtons();
        onApproveButton();
    }

    private void setNamesOfButtons() {

        Cursor res = db.viewAllData();

        while(res.moveToNext())
        {
            String x = res.getString(0);
            String name = res.getString(1);

            switch (x)
            {
                case "1":
                    flavor1Button.setText(name);
                    break;
                case "2":
                    flavor2Button.setText(name);
                    break;
                case "3":
                    flavor3Button.setText(name);
                    break;
                case "4":
                    flavor4Button.setText(name);
                    break;
                case "5":
                    flavor5Button.setText(name);
                    break;
                case "6":
                    flavor6Button.setText(name);
                    break;
                case "7":
                    flavor7Button.setText(name);
                    break;
                case "8":
                    flavor8Button.setText(name);
                    break;
                case "9":
                    flavor9Button.setText(name);
                    break;
                case "10":
                    flavor10Button.setText(name);
                    break;
                case "11":
                    flavor11Button.setText(name);
                    break;
                case "12":
                    flavor12Button.setText(name);
                    break;
            }
        }
        res.close();
    }

    private void onApproveButton() {

        approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(whichButtonSelectedToUpdate != 0)
                {
                    String x = "";
                    if(EnableDisableCheckBox.isChecked())
                        x = "n";
                    else
                        x = "y";
                    db.updateData(String.valueOf(whichButtonSelectedToUpdate),
                            displaySyrupSelected.getText().toString(), x,
                            popupEditText.getText().toString());

                    Toast.makeText(configClass.this, "Updated", Toast.LENGTH_SHORT).show();
                    setNamesOfButtons();
                    clearAllButtonColors();
                }
            }
        });
    }

    private void clearAllButtonColors() {

        flavor1Button.setBackgroundColor(getResources().getColor(R.color.white));
        flavor2Button.setBackgroundColor(getResources().getColor(R.color.white));
        flavor3Button.setBackgroundColor(getResources().getColor(R.color.white));
        flavor4Button.setBackgroundColor(getResources().getColor(R.color.white));
        flavor5Button.setBackgroundColor(getResources().getColor(R.color.white));
        flavor6Button.setBackgroundColor(getResources().getColor(R.color.white));
        flavor7Button.setBackgroundColor(getResources().getColor(R.color.white));
        flavor8Button.setBackgroundColor(getResources().getColor(R.color.white));
        flavor9Button.setBackgroundColor(getResources().getColor(R.color.white));
        flavor10Button.setBackgroundColor(getResources().getColor(R.color.white));
        flavor11Button.setBackgroundColor(getResources().getColor(R.color.white));
        flavor12Button.setBackgroundColor(getResources().getColor(R.color.white));

        displaySyrupSelected.setEnabled(false);
        displaySyrupSelected.setVisibility(View.INVISIBLE);
        EnableDisableCheckBox.setEnabled(false);
        EnableDisableCheckBox.setVisibility(View.INVISIBLE);

        whichButtonSelectedToUpdate = 0;
        popupEditText.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.config_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.logOut_id:

                Intent i=new Intent(this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setButtons() {

        flavor1Button = (Button) findViewById(R.id.flav1Button);
        flavor2Button = (Button) findViewById(R.id.flav2Button);
        flavor3Button = (Button) findViewById(R.id.flav3Button);
        flavor4Button = (Button) findViewById(R.id.flav4Button);
        flavor5Button = (Button) findViewById(R.id.flav5Button);
        flavor6Button = (Button) findViewById(R.id.flav6Button);
        flavor7Button = (Button) findViewById(R.id.flav7Button);
        flavor8Button = (Button) findViewById(R.id.flav8Button);
        flavor9Button = (Button) findViewById(R.id.flav9Button);
        flavor10Button = (Button) findViewById(R.id.flav10Button);
        flavor11Button = (Button) findViewById(R.id.flav11Button);
        flavor12Button = (Button) findViewById(R.id.flav12Button);
        popupEditText = (EditText) findViewById(R.id.popUpEditText);
        popupEditText.setVisibility(View.INVISIBLE);

        approveButton = (Button) findViewById(R.id.approveSyrupsID);
        EnableDisableCheckBox = (CheckBox) findViewById(R.id.enabledisablecheckBox);
        displaySyrupSelected = (EditText) findViewById(R.id.selectedSyrupToEdit);

        EnableDisableCheckBox.setVisibility(View.INVISIBLE);
        EnableDisableCheckBox.setEnabled(false);
        displaySyrupSelected.setEnabled(false);
        displaySyrupSelected.setVisibility(View.INVISIBLE);

        setOnButtonSyrupClickListener();

        flavor1Button.setBackgroundColor(getResources().getColor(R.color.white));
        flavor2Button.setBackgroundColor(getResources().getColor(R.color.white));
        flavor3Button.setBackgroundColor(getResources().getColor(R.color.white));
        flavor4Button.setBackgroundColor(getResources().getColor(R.color.white));
        flavor5Button.setBackgroundColor(getResources().getColor(R.color.white));
        flavor6Button.setBackgroundColor(getResources().getColor(R.color.white));
        flavor7Button.setBackgroundColor(getResources().getColor(R.color.white));
        flavor8Button.setBackgroundColor(getResources().getColor(R.color.white));
        flavor9Button.setBackgroundColor(getResources().getColor(R.color.white));
        flavor10Button.setBackgroundColor(getResources().getColor(R.color.white));
        flavor11Button.setBackgroundColor(getResources().getColor(R.color.white));
        flavor12Button.setBackgroundColor(getResources().getColor(R.color.white));
    }

    private void setOnButtonSyrupClickListener() {

        flavor1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(whichButtonSelectedToUpdate == 0)
                {
                    whichButtonSelectedToUpdate = 1;
                    displaySyrupSelected.setVisibility(View.VISIBLE);
                    displaySyrupSelected.setText(flavor1Button.getText().toString());
                    displaySyrupSelected.setEnabled(true);
                    flavor1Button.setBackgroundColor(getResources().getColor(R.color.black_overlay));
                    EnableDisableCheckBox.setVisibility(View.VISIBLE);
                    EnableDisableCheckBox.setEnabled(true);
                    Cursor res = db.viewAllData();
                    res.moveToPosition(whichButtonSelectedToUpdate - 1);

                    popupEditText.setText(res.getString(3));
                    popupEditText.setVisibility(View.VISIBLE);

                    if(res.getString(2).equals("n"))
                        EnableDisableCheckBox.setChecked(true);
                    else
                        EnableDisableCheckBox.setChecked(false);
                    res.close();
                }

                else if(whichButtonSelectedToUpdate == 1)
                {
                    popupEditText.setVisibility(View.INVISIBLE);
                    flavor1Button.setBackgroundColor(getResources().getColor(R.color.white));
                    whichButtonSelectedToUpdate = 0;
                    displaySyrupSelected.setText("");
                    displaySyrupSelected.setEnabled(false);
                    displaySyrupSelected.setVisibility(View.INVISIBLE);
                    EnableDisableCheckBox.setVisibility(View.INVISIBLE);
                    EnableDisableCheckBox.setEnabled(false);
                }
            }
        });

        flavor2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(whichButtonSelectedToUpdate == 0)
                {
                    whichButtonSelectedToUpdate = 2;
                    displaySyrupSelected.setVisibility(View.VISIBLE);
                    displaySyrupSelected.setText(flavor2Button.getText().toString());
                    displaySyrupSelected.setEnabled(true);
                    flavor2Button.setBackgroundColor(getResources().getColor(R.color.black_overlay));
                    EnableDisableCheckBox.setVisibility(View.VISIBLE);
                    EnableDisableCheckBox.setEnabled(true);

                    Cursor res = db.viewAllData();
                    res.moveToPosition(whichButtonSelectedToUpdate - 1);

                    popupEditText.setText(res.getString(3));
                    popupEditText.setVisibility(View.VISIBLE);

                    if(res.getString(2).equals("n"))
                        EnableDisableCheckBox.setChecked(true);
                    else
                        EnableDisableCheckBox.setChecked(false);
                    res.close();
                }

                else if(whichButtonSelectedToUpdate == 2)
                {
                    flavor2Button.setBackgroundColor(getResources().getColor(R.color.white));
                    whichButtonSelectedToUpdate = 0;
                    displaySyrupSelected.setText("");
                    displaySyrupSelected.setEnabled(false);
                    displaySyrupSelected.setVisibility(View.INVISIBLE);
                    EnableDisableCheckBox.setVisibility(View.INVISIBLE);
                    EnableDisableCheckBox.setEnabled(false);
                    popupEditText.setVisibility(View.INVISIBLE);
                }
            }
        });

        flavor3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(whichButtonSelectedToUpdate == 0)
                {
                    whichButtonSelectedToUpdate = 3;
                    displaySyrupSelected.setVisibility(View.VISIBLE);
                    displaySyrupSelected.setText(flavor3Button.getText().toString());
                    displaySyrupSelected.setEnabled(true);
                    flavor3Button.setBackgroundColor(getResources().getColor(R.color.black_overlay));

                    EnableDisableCheckBox.setVisibility(View.VISIBLE);
                    EnableDisableCheckBox.setEnabled(true);

                    Cursor res = db.viewAllData();
                    res.moveToPosition(whichButtonSelectedToUpdate - 1);

                    popupEditText.setText(res.getString(3));
                    popupEditText.setVisibility(View.VISIBLE);

                    if(res.getString(2).equals("n"))
                        EnableDisableCheckBox.setChecked(true);
                    else
                        EnableDisableCheckBox.setChecked(false);
                    res.close();
                }

                else if(whichButtonSelectedToUpdate == 3)
                {
                    flavor3Button.setBackgroundColor(getResources().getColor(R.color.white));
                    whichButtonSelectedToUpdate = 0;
                    displaySyrupSelected.setText("");
                    displaySyrupSelected.setEnabled(false);
                    displaySyrupSelected.setVisibility(View.INVISIBLE);
                    EnableDisableCheckBox.setVisibility(View.INVISIBLE);
                    EnableDisableCheckBox.setEnabled(false);
                    popupEditText.setVisibility(View.INVISIBLE);
                }
            }
        });

        flavor4Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(whichButtonSelectedToUpdate == 0)
                {
                    whichButtonSelectedToUpdate = 4;
                    displaySyrupSelected.setVisibility(View.VISIBLE);
                    displaySyrupSelected.setText(flavor4Button.getText().toString());
                    displaySyrupSelected.setEnabled(true);
                    flavor4Button.setBackgroundColor(getResources().getColor(R.color.black_overlay));
                    EnableDisableCheckBox.setVisibility(View.VISIBLE);
                    EnableDisableCheckBox.setEnabled(true);

                    Cursor res = db.viewAllData();
                    res.moveToPosition(whichButtonSelectedToUpdate - 1);

                    popupEditText.setText(res.getString(3));
                    popupEditText.setVisibility(View.VISIBLE);

                    if(res.getString(2).equals("n"))
                        EnableDisableCheckBox.setChecked(true);
                    else
                        EnableDisableCheckBox.setChecked(false);
                    res.close();
                }

                else if(whichButtonSelectedToUpdate == 4)
                {
                    flavor4Button.setBackgroundColor(getResources().getColor(R.color.white));
                    whichButtonSelectedToUpdate = 0;
                    displaySyrupSelected.setText("");
                    displaySyrupSelected.setEnabled(false);
                    displaySyrupSelected.setVisibility(View.INVISIBLE);
                    EnableDisableCheckBox.setVisibility(View.INVISIBLE);
                    EnableDisableCheckBox.setEnabled(false);
                    popupEditText.setVisibility(View.INVISIBLE);
                }
            }
        });

        flavor5Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(whichButtonSelectedToUpdate == 0)
                {
                    whichButtonSelectedToUpdate = 5;
                    displaySyrupSelected.setVisibility(View.VISIBLE);
                    displaySyrupSelected.setText(flavor5Button.getText().toString());
                    displaySyrupSelected.setEnabled(true);
                    flavor5Button.setBackgroundColor(getResources().getColor(R.color.black_overlay));
                    EnableDisableCheckBox.setVisibility(View.VISIBLE);
                    EnableDisableCheckBox.setEnabled(true);

                    Cursor res = db.viewAllData();
                    res.moveToPosition(whichButtonSelectedToUpdate - 1);

                    popupEditText.setText(res.getString(3));
                    popupEditText.setVisibility(View.VISIBLE);

                    if(res.getString(2).equals("n"))
                        EnableDisableCheckBox.setChecked(true);
                    else
                        EnableDisableCheckBox.setChecked(false);
                    res.close();
                }

                else if(whichButtonSelectedToUpdate == 5)
                {
                    flavor5Button.setBackgroundColor(getResources().getColor(R.color.white));
                    whichButtonSelectedToUpdate = 0;
                    displaySyrupSelected.setText("");
                    displaySyrupSelected.setEnabled(false);
                    displaySyrupSelected.setVisibility(View.INVISIBLE);
                    EnableDisableCheckBox.setVisibility(View.INVISIBLE);
                    EnableDisableCheckBox.setEnabled(false);
                    popupEditText.setVisibility(View.INVISIBLE);
                }
            }
        });

        flavor6Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(whichButtonSelectedToUpdate == 0)
                {
                    whichButtonSelectedToUpdate = 6;
                    displaySyrupSelected.setVisibility(View.VISIBLE);
                    displaySyrupSelected.setText(flavor6Button.getText().toString());
                    displaySyrupSelected.setEnabled(true);
                    flavor6Button.setBackgroundColor(getResources().getColor(R.color.black_overlay));
                    EnableDisableCheckBox.setVisibility(View.VISIBLE);
                    EnableDisableCheckBox.setEnabled(true);

                    Cursor res = db.viewAllData();
                    res.moveToPosition(whichButtonSelectedToUpdate - 1);

                    popupEditText.setText(res.getString(3));
                    popupEditText.setVisibility(View.VISIBLE);

                    if(res.getString(2).equals("n"))
                        EnableDisableCheckBox.setChecked(true);
                    else
                        EnableDisableCheckBox.setChecked(false);
                    res.close();
                }

                else if(whichButtonSelectedToUpdate == 6)
                {
                    flavor6Button.setBackgroundColor(getResources().getColor(R.color.white));
                    whichButtonSelectedToUpdate = 0;
                    displaySyrupSelected.setText("");
                    displaySyrupSelected.setEnabled(false);
                    displaySyrupSelected.setVisibility(View.INVISIBLE);
                    EnableDisableCheckBox.setVisibility(View.INVISIBLE);
                    EnableDisableCheckBox.setEnabled(false);
                    popupEditText.setVisibility(View.INVISIBLE);
                }
            }
        });

        flavor7Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(whichButtonSelectedToUpdate == 0)
                {
                    whichButtonSelectedToUpdate = 7;
                    displaySyrupSelected.setVisibility(View.VISIBLE);
                    displaySyrupSelected.setText(flavor7Button.getText().toString());
                    displaySyrupSelected.setEnabled(true);
                    flavor7Button.setBackgroundColor(getResources().getColor(R.color.black_overlay));
                    EnableDisableCheckBox.setVisibility(View.VISIBLE);
                    EnableDisableCheckBox.setEnabled(true);

                    Cursor res = db.viewAllData();
                    res.moveToPosition(whichButtonSelectedToUpdate - 1);

                    popupEditText.setText(res.getString(3));
                    popupEditText.setVisibility(View.VISIBLE);

                    if(res.getString(2).equals("n"))
                        EnableDisableCheckBox.setChecked(true);
                    else
                        EnableDisableCheckBox.setChecked(false);
                    res.close();
                }

                else if(whichButtonSelectedToUpdate == 7)
                {
                    flavor7Button.setBackgroundColor(getResources().getColor(R.color.white));
                    whichButtonSelectedToUpdate = 0;
                    displaySyrupSelected.setText("");
                    displaySyrupSelected.setEnabled(false);
                    displaySyrupSelected.setVisibility(View.INVISIBLE);
                    EnableDisableCheckBox.setVisibility(View.INVISIBLE);
                    EnableDisableCheckBox.setEnabled(false);
                    popupEditText.setVisibility(View.INVISIBLE);
                }
            }
        });

        flavor8Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(whichButtonSelectedToUpdate == 0)
                {
                    whichButtonSelectedToUpdate = 8;
                    displaySyrupSelected.setVisibility(View.VISIBLE);
                    displaySyrupSelected.setText(flavor8Button.getText().toString());
                    displaySyrupSelected.setEnabled(true);
                    flavor8Button.setBackgroundColor(getResources().getColor(R.color.black_overlay));
                    EnableDisableCheckBox.setVisibility(View.VISIBLE);
                    EnableDisableCheckBox.setEnabled(true);

                    Cursor res = db.viewAllData();
                    res.moveToPosition(whichButtonSelectedToUpdate - 1);

                    popupEditText.setText(res.getString(3));
                    popupEditText.setVisibility(View.VISIBLE);

                    if(res.getString(2).equals("n"))
                        EnableDisableCheckBox.setChecked(true);
                    else
                        EnableDisableCheckBox.setChecked(false);
                    res.close();
                }

                else if(whichButtonSelectedToUpdate == 8)
                {
                    flavor8Button.setBackgroundColor(getResources().getColor(R.color.white));
                    whichButtonSelectedToUpdate = 0;
                    displaySyrupSelected.setText("");
                    displaySyrupSelected.setEnabled(false);
                    displaySyrupSelected.setVisibility(View.INVISIBLE);
                    EnableDisableCheckBox.setVisibility(View.INVISIBLE);
                    EnableDisableCheckBox.setEnabled(false);
                    popupEditText.setVisibility(View.INVISIBLE);
                }
            }
        });

        flavor9Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(whichButtonSelectedToUpdate == 0)
                {
                    whichButtonSelectedToUpdate = 9;
                    displaySyrupSelected.setVisibility(View.VISIBLE);
                    displaySyrupSelected.setText(flavor9Button.getText().toString());
                    displaySyrupSelected.setEnabled(true);
                    flavor9Button.setBackgroundColor(getResources().getColor(R.color.black_overlay));
                    EnableDisableCheckBox.setVisibility(View.VISIBLE);
                    EnableDisableCheckBox.setEnabled(true);

                    Cursor res = db.viewAllData();
                    res.moveToPosition(whichButtonSelectedToUpdate - 1);

                    popupEditText.setText(res.getString(3));
                    popupEditText.setVisibility(View.VISIBLE);

                    if(res.getString(2).equals("n"))
                        EnableDisableCheckBox.setChecked(true);
                    else
                        EnableDisableCheckBox.setChecked(false);
                    res.close();
                }

                else if(whichButtonSelectedToUpdate == 9)
                {
                    flavor9Button.setBackgroundColor(getResources().getColor(R.color.white));
                    whichButtonSelectedToUpdate = 0;
                    displaySyrupSelected.setText("");
                    displaySyrupSelected.setEnabled(false);
                    displaySyrupSelected.setVisibility(View.INVISIBLE);
                    EnableDisableCheckBox.setVisibility(View.INVISIBLE);
                    EnableDisableCheckBox.setEnabled(false);
                    popupEditText.setVisibility(View.INVISIBLE);
                }
            }
        });

        flavor10Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(whichButtonSelectedToUpdate == 0)
                {
                    whichButtonSelectedToUpdate = 10;
                    displaySyrupSelected.setVisibility(View.VISIBLE);
                    displaySyrupSelected.setText(flavor10Button.getText().toString());
                    displaySyrupSelected.setEnabled(true);
                    flavor10Button.setBackgroundColor(getResources().getColor(R.color.black_overlay));
                    EnableDisableCheckBox.setVisibility(View.VISIBLE);
                    EnableDisableCheckBox.setEnabled(true);

                    Cursor res = db.viewAllData();
                    res.moveToPosition(whichButtonSelectedToUpdate - 1);

                    popupEditText.setText(res.getString(3));
                    popupEditText.setVisibility(View.VISIBLE);

                    if(res.getString(2).equals("n"))
                        EnableDisableCheckBox.setChecked(true);
                    else
                        EnableDisableCheckBox.setChecked(false);
                    res.close();
                }

                else if(whichButtonSelectedToUpdate == 10)
                {
                    flavor10Button.setBackgroundColor(getResources().getColor(R.color.white));
                    whichButtonSelectedToUpdate = 0;
                    displaySyrupSelected.setText("");
                    displaySyrupSelected.setEnabled(false);
                    displaySyrupSelected.setVisibility(View.INVISIBLE);
                    EnableDisableCheckBox.setVisibility(View.INVISIBLE);
                    EnableDisableCheckBox.setEnabled(false);
                    popupEditText.setVisibility(View.INVISIBLE);
                }
            }
        });

        flavor11Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(whichButtonSelectedToUpdate == 0)
                {
                    whichButtonSelectedToUpdate = 11;
                    displaySyrupSelected.setVisibility(View.VISIBLE);
                    displaySyrupSelected.setText(flavor11Button.getText().toString());
                    displaySyrupSelected.setEnabled(true);
                    flavor11Button.setBackgroundColor(getResources().getColor(R.color.black_overlay));
                    EnableDisableCheckBox.setVisibility(View.VISIBLE);
                    EnableDisableCheckBox.setEnabled(true);

                    Cursor res = db.viewAllData();
                    res.moveToPosition(whichButtonSelectedToUpdate - 1);

                    popupEditText.setText(res.getString(3));
                    popupEditText.setVisibility(View.VISIBLE);

                    if(res.getString(2).equals("n"))
                        EnableDisableCheckBox.setChecked(true);
                    else
                        EnableDisableCheckBox.setChecked(false);
                    res.close();
                }

                else if(whichButtonSelectedToUpdate == 11)
                {
                    flavor11Button.setBackgroundColor(getResources().getColor(R.color.white));
                    whichButtonSelectedToUpdate = 0;
                    displaySyrupSelected.setText("");
                    displaySyrupSelected.setEnabled(false);
                    displaySyrupSelected.setVisibility(View.INVISIBLE);
                    EnableDisableCheckBox.setVisibility(View.INVISIBLE);
                    EnableDisableCheckBox.setEnabled(false);
                    popupEditText.setVisibility(View.INVISIBLE);
                }
            }
        });

        flavor12Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(whichButtonSelectedToUpdate == 0)
                {
                    whichButtonSelectedToUpdate = 12;
                    displaySyrupSelected.setVisibility(View.VISIBLE);
                    displaySyrupSelected.setText(flavor12Button.getText().toString());
                    displaySyrupSelected.setEnabled(true);
                    flavor12Button.setBackgroundColor(getResources().getColor(R.color.black_overlay));
                    EnableDisableCheckBox.setVisibility(View.VISIBLE);
                    EnableDisableCheckBox.setEnabled(true);

                    Cursor res = db.viewAllData();
                    res.moveToPosition(whichButtonSelectedToUpdate - 1);

                    popupEditText.setText(res.getString(3));
                    popupEditText.setVisibility(View.VISIBLE);

                    if(res.getString(2).equals("n"))
                        EnableDisableCheckBox.setChecked(true);
                    else
                        EnableDisableCheckBox.setChecked(false);
                    res.close();
                }

                else if(whichButtonSelectedToUpdate == 12)
                {
                    flavor12Button.setBackgroundColor(getResources().getColor(R.color.white));
                    whichButtonSelectedToUpdate = 0;
                    displaySyrupSelected.setText("");
                    displaySyrupSelected.setEnabled(false);
                    displaySyrupSelected.setVisibility(View.INVISIBLE);
                    EnableDisableCheckBox.setVisibility(View.INVISIBLE);
                    EnableDisableCheckBox.setEnabled(false);
                    popupEditText.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
}
