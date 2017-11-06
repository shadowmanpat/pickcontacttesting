package com.example.sm.pickcontacttesting;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {


    int PICK_CONTACT=1001;

     ArrayList<String> nameList ;
    ArrayList<String> phoneList;
    ArrayList<String>  emailList ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button general =(Button) findViewById(R.id.general);
        general.setVisibility(View.VISIBLE);
        general.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //send sms
                //    sendSmsSmsManagerAPI(phoneNo, "hello, ooo nice app http://rocket.social/ ");
                generalShare("hello, ooo nice app http://rocket.social/ ","Rocket Social");
            }
        });
;
    }

    public void coodeEmail(View v){


        methodRequiresPermission() ;
//        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
//        startActivityForResult(intent, REQUEST_CODE);
    }
    private final int REQUEST_CODE=99;
    private final int REQUEST_CODE2=992;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_CONTACTS && resultCode == RESULT_OK) {
            Log.d(TAG, "Response: " + data.toString());

            uriContact = data.getData();

            retrieveContactName();
            retrieveContactNumber();
            retrieveContactPhoto();
            retrieveContactEmail();

        }


    }
    public void generalShare(String extra_text, String extra_subject){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, extra_text);
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, extra_subject);
        startActivity(Intent.createChooser(intent, "Share via"));
    }

//    @Override
//    public void onActivityResult(int reqCode, int resultCode, Intent data) {
//        super.onActivityResult(reqCode, resultCode, data);
//        switch (reqCode) {
//            case (REQUEST_CODE):
//                if (resultCode == Activity.RESULT_OK) {
//                    Uri contactData = data.getData();
//                    Cursor c = getContentResolver().query(contactData, null, null, null, null);
//                    if (c.moveToFirst()) {
//                        String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
//                        String hasNumber = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
//                        String num = "";
//                        if (Integer.valueOf(hasNumber) == 1) {
//                            Cursor numbers = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
//                            while (numbers.moveToNext()) {
//                                num = numbers.getString(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                                Toast.makeText(MainActivity.this, "Number="+num, Toast.LENGTH_LONG).show();
//                            }
//                        }
//
//
//                    }
//                    break;
//                }
//
//        }
//    }



    private static final String TAG =MainActivity.class.getSimpleName();
    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    private Uri uriContact;
    private String contactID;     // contacts unique ID

    private void retrieveContactPhoto() {

        Bitmap photo = null;

        try {
            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(),
                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(contactID)));

            if (inputStream != null) {
                photo = BitmapFactory.decodeStream(inputStream);
                ImageView imageView = (ImageView) findViewById(R.id.imageView);
                imageView.setImageBitmap(photo);
            }

            assert inputStream != null;
            if(inputStream!=null){
                inputStream.close();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void retrieveContactNumber() {

        String contactNumber = null;

        // getting contacts ID
        Cursor cursorID = getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);

        if (cursorID.moveToFirst()) {

            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();

        Log.d(TAG, "Contact ID: " + contactID);
        tv.setText(tv.getText().toString()+"\n"+"Contact ID: " + contactID);
        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                new String[]{contactID},
                null);

        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }

        cursorPhone.close();

        Log.d(TAG, "Contact Phone Number: " + contactNumber);
        tv.setText(tv.getText().toString()+"\n"+"Contact Phone Number: " + contactNumber);

        if(!contactNumber.equals("")){
        phoneNo=contactNumber;

            Button sms =(Button) findViewById(R.id.smsmanager);
            sms.setVisibility(View.VISIBLE);
            sms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //send sms
                //    sendSmsSmsManagerAPI(phoneNo, "hello, ooo nice app http://rocket.social/ ");
                    sendSmsAskPermissionSmsMAnager();
                }
            });

            Button sms2 =(Button) findViewById(R.id.smsbuiltin);
            sms2.setVisibility(View.VISIBLE);
            sms2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //send sms
                //    sendSmsSmsManagerAPI(phoneNo, "hello, ooo nice app http://rocket.social/ ");
                    sendSmsAskPermissionBuiltInSms();
                }
            });


        }
    }
     String phoneNo="";

    private void retrieveContactEmail() {

        String contactNumber = null;

        // getting contacts ID
        Cursor cursorID = getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);

        if (cursorID.moveToFirst()) {

            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();

        String emailStr = "";
        final String[] projection = new String[]{ContactsContract.CommonDataKinds.Email.DATA,
                ContactsContract.CommonDataKinds.Email.TYPE};

        Cursor emailq = managedQuery(ContactsContract.CommonDataKinds.Email.CONTENT_URI, projection, ContactsContract.Data.CONTACT_ID + "=?", new String[]{contactID}, null);

        if (emailq.moveToFirst()) {
            final int contactEmailColumnIndex = emailq.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);

            while (!emailq.isAfterLast()) {
                emailStr = emailStr + emailq.getString(contactEmailColumnIndex);
                emailq.moveToNext();
            }
        }
        Log.d(TAG, "Contact Email: " + emailStr);
        tv.setText(tv.getText().toString()+"\n"+"Contact Email: " + emailStr);
        final String emailS=emailStr;

        if(!emailStr.equals("")){
            Button email =(Button) findViewById(R.id.email);
            email.setVisibility(View.VISIBLE);
            email.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendEmail(emailS,"Nice App Rocket Social","hello, ooo nice app http://rocket.social/ ");
                }
            });
        }
    }
    public String getEmail(String contactId) {
        String emailStr = "";
        final String[] projection = new String[]{ContactsContract.CommonDataKinds.Email.DATA,
                ContactsContract.CommonDataKinds.Email.TYPE};

        Cursor emailq = managedQuery(ContactsContract.CommonDataKinds.Email.CONTENT_URI, projection, ContactsContract.Data.CONTACT_ID + "=?", new String[]{contactId}, null);

        if (emailq.moveToFirst()) {
            final int contactEmailColumnIndex = emailq.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);

            while (!emailq.isAfterLast()) {
                emailStr = emailStr + emailq.getString(contactEmailColumnIndex) + ";";
                emailq.moveToNext();
            }
        }
        return emailStr;
    }

    private void retrieveContactName() {

        String contactName = null;

        // querying contact data store
        Cursor cursor = getContentResolver().query(uriContact, null, null, null, null);

        if (cursor.moveToFirst()) {

            // DISPLAY_NAME = The display name for the contact.
            // HAS_PHONE_NUMBER =   An indicator of whether this contact has at least one phone number.

            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }

        cursor.close();

        Log.d(TAG, "Contact Name: " + contactName);
        tv=(TextView) findViewById(R.id.textView);
        tv.setText("Contact Name: " + contactName);

    }
    TextView tv;
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
    @AfterPermissionGranted(READ_CONTACTS)
    public void methodRequiresPermission() {
        String[] perms = {Manifest.permission.READ_CONTACTS};
        if (EasyPermissions.hasPermissions(this, perms)) {

            startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);

            // Already have permission, do the thing
            // ...
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "Let me read Contacts",
                    READ_CONTACTS, perms);
        }
    }
    private static final int READ_CONTACTS=1;
    private static final int SEND_SMS=3;

    @AfterPermissionGranted(SEND_SMS)
    public void sendSmsAskPermissionSmsMAnager(){

        String sms="hello, ooo nice app http://rocket.social/ ";
        String[] perms = {Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS};
        if (EasyPermissions.hasPermissions(this, perms)) {

            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNo, null, sms, null, null);
                Toast.makeText(getApplicationContext(), "SMS Sent!",
                        Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),
                        "SMS faild, please try again later!",
                        Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "Let me Send Sms",SEND_SMS, perms);
        }


    }
    @AfterPermissionGranted(SEND_SMS)
    public void sendSmsAskPermissionBuiltInSms(){

        String sms="hello, ooo nice app http://rocket.social/ ";
        String[] perms = {Manifest.permission.SEND_SMS};
        if (EasyPermissions.hasPermissions(this, perms)) {

            try {
                Uri sms_uri = Uri.parse("smsto:+" + phoneNo);
                Intent sendIntent = new Intent(Intent.ACTION_VIEW,sms_uri);
                //sendIntent.setType("vnd.android-dir/mms-sms");
                sendIntent.putExtra("address", phoneNo);
                sendIntent.putExtra("sms_body", sms);

                startActivity(sendIntent);

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),
                        "SMS failed, please try again later!",
                        Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "Let me Send Sms",SEND_SMS, perms);
        }


    }
    protected void sendEmail(String email,String subject,String text) {
        Log.i("Send email", "");
        String[] TO = {email};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);

       // emailIntent.setType("message/rfc822");
        emailIntent.setData(Uri.parse("mailto:"+email));

        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, text);
        emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.

        try {
            startActivity(emailIntent);

            Log.i("State", "Finished sending email...");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
}


