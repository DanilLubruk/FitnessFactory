package com.example.fitnessfactory.utils;

import android.content.Intent;
import android.net.Uri;

public class IntentUtils {

    public static Intent getEmailIntent(String email, String subject, String text) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {email});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, text);

        return emailIntent;
    }
}
