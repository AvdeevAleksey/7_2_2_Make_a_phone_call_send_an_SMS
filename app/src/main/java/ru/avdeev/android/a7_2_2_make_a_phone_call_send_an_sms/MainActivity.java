package ru.avdeev.android.a7_2_2_make_a_phone_call_send_an_sms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText collEditText;
    private EditText sendEditText;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE=11;
    private static final int MY_PERMISSIONS_REQUEST_SEND_MESSAGE=12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        collEditText = findViewById(R.id.collNumberEditText);
        sendEditText = findViewById(R.id.sendingMessageEditText);
        final Button collBtn = findViewById(R.id.btnColl);
        final Button sendBtn = findViewById(R.id.btnSend);
        collBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (collEditText.getText().toString().length()==12) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED) {
                        // Разрешение не получено
                        // Делаем запрос на добавление разрешения звонка
                        ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CALL_PHONE},MY_PERMISSIONS_REQUEST_CALL_PHONE);
                    } else {
                        // Разрешение уже получено
                        Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:" + collEditText.getText().toString()));
                        // Звоним
                        startActivity(intent);
                    }
                } else {
                    showMyMessage(R.string.No_phone_number, MainActivity.this);
                }
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sendEditText.getText().toString().length()>0 && collEditText.getText().toString().length()==12) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.SEND_SMS},MY_PERMISSIONS_REQUEST_SEND_MESSAGE);
                    } else {
                        SmsManager smgr = SmsManager.getDefault();
                        smgr.sendTextMessage(collEditText.getText().toString(), null, sendEditText.getText().toString(), null, null);
                    }
                } else {
                    showMyMessage(R.string.There_is_no_text_to_send, MainActivity.this);
                }
            }
        });
    }

    public static void showMyMessage(int massage, Context context) {
        String text = context.getString(massage);
        SpannableStringBuilder biggerText = new SpannableStringBuilder(text);
        biggerText.setSpan(new RelativeSizeSpan(1.35f), 0, text.length(), 0);
        Toast toast = Toast.makeText(context, biggerText, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }
}