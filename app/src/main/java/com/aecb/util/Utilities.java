package com.aecb.util;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.provider.Settings;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;

import com.aecb.App;
import com.aecb.AppConstants;
import com.aecb.R;
import com.aecb.base.mvp.BaseActivity;
import com.aecb.data.preference.PrefHelperImpl;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Years;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import timber.log.Timber;

import static com.aecb.AppConstants.DISPLAY_DATE_FORMAT;
import static com.aecb.AppConstants.DateFormats.DEFAULT_API_FORMAT;
import static com.aecb.AppConstants.dfs;
import static com.aecb.AppConstants.dls;
import static com.aecb.AppConstants.fs;

public class Utilities {

    public static StringBuilder getStringFromErrorBody(byte[] bytes) {
        Charset charset = StandardCharsets.UTF_8;
        CharsetDecoder decoder = charset.newDecoder();
        ByteBuffer srcBuffer = ByteBuffer.wrap(bytes);
        CharBuffer resBuffer = null;
        try {
            resBuffer = decoder.decode(srcBuffer);
        } catch (CharacterCodingException e) {
            Timber.d("Exception : " + e.toString());
        }
        return new StringBuilder(resBuffer);
    }

    public static String after(String value, String a) {
        // Returns a substring containing all characters after a string.
        try {
            int posA = value.indexOf(a);
            if (posA == -1) {
                return "";
            }
            int adjustedPosA = posA + a.length();
            if (adjustedPosA >= value.length()) {
                return "";
            }
            return value.substring(adjustedPosA);
        } catch (Exception e) {
            return "";
        }

    }

    public static String getStringFromErrorBody(InputStream byteStream) {
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        reader = new BufferedReader(new InputStreamReader(byteStream));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            Timber.d("Exception : " + e.toString());
        }
        return sb.toString();
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static void setWindowFlag(Context context, final int bits, boolean on) {
        if (context != null && context instanceof BaseActivity) {
            Window win = ((BaseActivity) context).getWindow();

            WindowManager.LayoutParams winParams = win.getAttributes();
            if (on) {
                winParams.flags |= bits;
            } else {
                winParams.flags &= ~bits;
            }
            win.setAttributes(winParams);
        }
    }

    public static String getCurrentDateInApiFormat() {
        Date c = Calendar.getInstance().getTime();
        //System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(c);
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        if (context != null) {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = context.getResources().getDimensionPixelSize(resourceId);
            }
        }
        return result;
    }

    /**
     * @param dateStr
     * @param format
     * @return
     */
    public static java.util.Date stringToDate(String dateStr, String format) {

        if (ValidationUtil.isNullOrEmpty(dateStr) || ValidationUtil.isNullOrEmpty(format))
            return null;

        Date d = null;
        SimpleDateFormat formater = new SimpleDateFormat(format, Locale.ENGLISH);
        try {
            formater.setLenient(false);
            d = formater.parse(dateStr);
        } catch (Exception e) {
            Timber.d("stringToDate :" + e.getMessage());
            d = null;
        }
        return d;
    }

    public static java.util.Date stringToDateDubai(String dateStr, String format) {

        if (ValidationUtil.isNullOrEmpty(dateStr) || ValidationUtil.isNullOrEmpty(format))
            return new Date();

        Date d = null;
        SimpleDateFormat formater = new SimpleDateFormat(format, Locale.ENGLISH);
        try {
            formater.setLenient(false);
            // => This time is in the user phone timezone, you will maybe need to turn it in UTC!
           // TimeZone tz = TimeZone.getTimeZone("GMT");
            //formater.setTimeZone(tz);
            d = formater.parse(dateStr);
        } catch (Exception e) {
            Timber.d("stringToDate :" + e.getMessage());
            d = new Date();
        }
        return d;
    }

    public static Drawable getDrawable(Context context, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getDrawable(id);
        }
        return context.getResources().getDrawable(id);
    }

    public static boolean isRtl(Context context) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1
                && context.getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
    }

    public static int lighter(int color, float factor) {
        int red = (int) ((Color.red(color) * (1 - factor) / 255 + factor) * 255);
        int green = (int) ((Color.green(color) * (1 - factor) / 255 + factor) * 255);
        int blue = (int) ((Color.blue(color) * (1 - factor) / 255 + factor) * 255);
        return Color.argb(Color.alpha(color), red, green, blue);
    }

    public static int darker(int color, float factor) {
        return Color.argb(Color.alpha(color), Math.max((int) (Color.red(color) * factor), 0),
                Math.max((int) (Color.green(color) * factor), 0), Math.max((int) (Color.blue(color) * factor), 0));
    }

    public static Locale getCurrentLocal() {

        String mAppLang = PrefHelperImpl.getAppLanguage();
        Locale locale = new Locale(AppConstants.AppLanguage.ISO_CODE_ENG);
        if (!ValidationUtil.isNullOrEmpty(mAppLang)) {
            if (mAppLang.equalsIgnoreCase(AppConstants.AppLanguage.ISO_CODE_ENG)) {
                locale = new Locale(AppConstants.AppLanguage.ISO_CODE_ENG);
            } else if (mAppLang.equalsIgnoreCase(AppConstants.AppLanguage.ISO_CODE_ARABIC)) {
                locale = new Locale(AppConstants.AppLanguage.ISO_CODE_ARABIC);
            }
        }
        return locale;
    }

    public static String getAndroidVersion() {
        return Build.VERSION.RELEASE;
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (ValidationUtil.isNullOrEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;
        String phrase = "";
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase += Character.toUpperCase(c);
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase += c;
        }
        return phrase;
    }

    public static String getDeviceTimeZone() {
        TimeZone tz = TimeZone.getDefault();
        //System.out.println("TimeZone   "+tz.getDisplayName(false, TimeZone.SHORT)+" Timezon id :: " +tz.getID());
        return tz.getID();
    }

    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    public static String convertServerDateToDisplayFormat(String date) {
        String outputDateStr;
        DateFormat inputFormat = new SimpleDateFormat(DEFAULT_API_FORMAT);
        DateFormat outputFormat = DISPLAY_DATE_FORMAT;
        Date d = null;
        try {
            d = inputFormat.parse(date);
        } catch (ParseException e) {
            Timber.d("Exception : " + e.toString());
            return "";
        }

        if (d != null) {
            outputDateStr = outputFormat.format(d);
        } else outputDateStr = "";

        return outputDateStr;
    }

    public static String convertServerDateToDisplayFormat2(String date) {
        String outputDateStr;
        DateFormat inputFormat = new SimpleDateFormat(DEFAULT_API_FORMAT);
        DateFormat outputFormat = new SimpleDateFormat(AppConstants.DateFormats.DD_MM_YYYY_SLASH, Locale.ENGLISH);

        Date d = null;
        try {
            d = inputFormat.parse(date);
        } catch (ParseException e) {
            Timber.d("Exception : " + e.toString());
            return "";
        }

        if (d != null) {
            outputDateStr = outputFormat.format(d);
        } else outputDateStr = "";

        return outputDateStr;
    }

    public static void sendEmail(Context context, String emailAddress, String subject, String text) {

        Intent email = new Intent(Intent.ACTION_SENDTO);
        email.setData(Uri.parse("mailto:"));
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{emailAddress});
        email.putExtra(Intent.EXTRA_SUBJECT, subject);
        email.putExtra(Intent.EXTRA_TEXT, text);
        try {
            context.startActivity(Intent.createChooser(email, "Choose an Email client :"));
        } catch (ActivityNotFoundException activityNotFoundException) {
            Timber.d("Error : " + activityNotFoundException.toString());
        }
      /*  Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + emailAddress)); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }*/
       /* Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{emailAddress});
        email.putExtra(Intent.EXTRA_SUBJECT, subject);
        email.putExtra(Intent.EXTRA_TEXT, text);
        email.setType("message/rfc822");
        context.startActivity(Intent.createChooser(email, "Choose an Email client :"));*/
    }

    public static void makeCall(Context context, String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        context.startActivity(intent);
    }

    public static int monthsBetweenDates(Date startDate, Date endDate) {

        Calendar start = Calendar.getInstance();
        start.setTime(startDate);

        Calendar end = Calendar.getInstance();
        end.setTime(endDate);

        int monthsBetween = 0;
        int dateDiff = end.get(Calendar.DAY_OF_MONTH) - start.get(Calendar.DAY_OF_MONTH);

        if (dateDiff < 0) {
            int borrrow = end.getActualMaximum(Calendar.DAY_OF_MONTH);
            dateDiff = (end.get(Calendar.DAY_OF_MONTH) + borrrow) - start.get(Calendar.DAY_OF_MONTH);
            monthsBetween--;

            if (dateDiff > 0) {
                monthsBetween++;
            }
        } else {
            monthsBetween++;
        }
        monthsBetween += end.get(Calendar.MONTH) - start.get(Calendar.MONTH);
        monthsBetween += (end.get(Calendar.YEAR) - start.get(Calendar.YEAR)) * 12;
        return monthsBetween;
    }

    public static File saveBitmapToFile(File file) {
        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE = 75;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            // here i override the original image file
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            return file;
        } catch (Exception e) {
            return null;
        }
    }

    public static File createBitmapToFile(Context context, Bitmap bitmap) {
        //create a file to write bitmap data
        File file = null;
        try {
            file = new File(context.getCacheDir(), "image");
            file.createNewFile();

//Convert bitmap to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            Timber.d("Exception : " + e.toString());
        }
        return file;
    }

    public static String fullString() {
        String str = "";
        str = new String(fs, StandardCharsets.UTF_8);
        return str;
    }

    public static String dFullString() {
        byte[] fsData = Base64.decode(dfs, Base64.DEFAULT);
        String fs = new String(fsData, StandardCharsets.UTF_8);
        byte[] lsData = Base64.decode(dls, Base64.DEFAULT);
        String ls = new String(lsData, StandardCharsets.UTF_8);
        return fs + ls;
    }

    public static String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = App.getAppComponent().appContext().getAssets().open("response_messages.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static void showSSLDialog(Context mContext, SslErrorHandler handler, SslError er) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        String message = mContext.getString(R.string.ssl_error);
        switch (er.getPrimaryError()) {
            case SslError.SSL_UNTRUSTED:
                message = mContext.getString(R.string.ssl_is_untrusted);
                break;
            case SslError.SSL_EXPIRED:
                message = mContext.getString(R.string.ssl_is_expired);
                break;
            case SslError.SSL_IDMISMATCH:
                message = mContext.getString(R.string.invalid_hostname);
                break;
            case SslError.SSL_NOTYETVALID:
                message = mContext.getString(R.string.invalid_certificate);
                break;
        }
        message += mContext.getString(R.string.continue_anyway);
        builder.setTitle(R.string.error);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handler.proceed();
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handler.cancel();
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static String getOtpFromMessage(String message) {
        Pattern pattern = Pattern.compile("(|^)\\d{6}");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return null;
    }

    public static boolean isTextContainUpperCase(String text) {
        return !text.equals(text.toLowerCase());
    }

    public static boolean isTextContainLowerCase(String text) {
        return !text.equals(text.toUpperCase());
    }

    public static boolean isTextContainNumber(String text) {
        return text.matches(".*\\d.*");
    }

    public static boolean isStringContainsSpecialCharacter(String text) {
        if (text == null || text.trim().isEmpty()) {
            System.out.println("Incorrect format of string");
            return false;
        }
        Pattern pattern = Pattern.compile("[^A-Za-z0-9]");
        Matcher matcher = pattern.matcher(text);
        return matcher.find();
    }

    public static boolean isUser18Older(DateTime userDob, int minimumAge) {
        DateTime now = new DateTime();
        Years age = Years.yearsBetween(userDob, now);
        return age.getYears() >= minimumAge;
    }
}