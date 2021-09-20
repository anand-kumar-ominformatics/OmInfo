package com.ominfo.app.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ominfo.app.BuildConfig;
import com.ominfo.app.R;
import com.ominfo.app.interfaces.Constants;
import com.ominfo.app.interfaces.CustomDialogHelper;
import com.ominfo.app.interfaces.SharedPrefKey;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

/*
 * AppUtils is used for all common methods
 * */
public class AppUtils {
    private static final String TAG = AppUtils.class.getSimpleName();
    public static String mDate;
    /**
     * this method return whole device info
     *
     * @param context context
     * @return device info
     */
    /*public static String getDeviceType(Context context) {

        boolean isTablet = context.getResources().getBoolean(R.bool.isTablet);
        String deviceType = Constants.ANDROID + "-" + Constants.DEVICE_OS_VALUE + "-" + (isTablet ? Constants.TABLET : Constants.PHONE) + "-" + Build.MANUFACTURER + " " + Build.MODEL.replace("-", " ");
        try {
            return URLEncoder.encode(deviceType, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LogUtil.printLog(TAG, e.toString());
            return deviceType.replace(" ", "%20");
        }
    }
*/
    /**
     * this method return device model like Galaxy 9
     *
     * @return device model
     */
    public static String getDeviceModel() {
        return Build.MODEL;
    }

    /**
     * this method return device type PHONE, TABLET
     *
     * @param context context
     * @return device type
     */
    /*public static String getDeviceTypePhoneOrTablet(Context context) {
        //boolean isTablet = context.getResources().getBoolean(R.bool.isTablet);
        return (isTablet ? Constants.TABLET : Constants.PHONE).toUpperCase();
    }*/


    /**
     * this method return current app version
     *
     * @param context context
     * @return app version
     */
    public static String getAppVersion(Context context) {
       /* PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo != null ? packageInfo.versionName : "";*/
        return BuildConfig.VERSION_NAME;

    }

    public static boolean isInteger(String str) {
        boolean result;
        try {
            Integer.parseInt(str);
            result = true;
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if(inputMethodManager.isAcceptingText()){
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(),
                    0
            );
        }
    }

    public static String dateFormate(String sDate) {
        String sDateFormate = "";
        try {
            String pattern = "dd MMM, yyyy";
            String inputPattern = "yyyy-MM-dd";
            /*SimpleDateFormat simpleDateFormat = new SimpleDateFormat(inputPattern);
            Date date = simpleDateFormat.parse(sDate);
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            sDateFormate = sdf.format(date.getTime());
            System.out.println(sDateFormate);*/

            SimpleDateFormat fmt = new SimpleDateFormat(inputPattern);
            Date date = fmt.parse(sDate);

            SimpleDateFormat fmtOut = new SimpleDateFormat(pattern);
            sDateFormate = fmtOut.format(date);

            LogUtil.printLog(TAG, "sDateFormate: " + sDateFormate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sDateFormate;
    }


    public static String getOSVersion() {

        String os = "";
        try {
            os = Build.VERSION.RELEASE;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return os;

    }

    //date formatter for Activity log
    public static String splitDateUser(String date, String Zone) throws ParseException {

        String format = "yyyy-MM-dd'T'HH:mm:ss";
        SimpleDateFormat estFormatter = new SimpleDateFormat(format);
        estFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date1 = estFormatter.parse(date);

        SimpleDateFormat utcFormatter = new SimpleDateFormat(format);
        utcFormatter.setTimeZone(TimeZone.getTimeZone(Zone));

        String strDate = utcFormatter.format(date1);

        String[] separated = strDate.split("T");
        mDate =separated[0];
        //mZone = Zone;
        String val =separated[1]; // this will contain " they taste good"

        return getHoursWelcome(val);
    }

    //date formatter for Activity log
    public static String getHoursWelcome(String hours){ //"2021-07-19T12:01:32",
        String currentString = hours;
        String[] separated = currentString.split(":");
        String val =separated[0]+":"+separated[1]; // this will contain " they taste good"
        return dateConvertWelcome(val);
    }

    //date formatter for Activity log
    public static String dateConvertWelcome(String val){
        try {
            String _24HourTime = val;
            SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
            SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
            //_12HourSDF.setTimeZone(TimeZone.getTimeZone("IST"));
            //_24HourSDF.setTimeZone(TimeZone.getTimeZone("IST"));
            Date _24HourDt = _24HourSDF.parse(_24HourTime);
            return splitsFormatWelcome(mDate)+" "+_12HourSDF.format(_24HourDt);
            //System.out.println(_24HourDt);
            //System.out.println(_12HourSDF.format(_24HourDt));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //date formatter for Activity log
    public static String splitsFormatWelcome(String Date){
        String currentString = Date;
        String[] separated = currentString.split("-");
        String YYYY =separated[0];
        String MM = separated[1]; String DD = separated[2];
        return MM+"-"+DD+"-"+YYYY.substring(2,4);
    }


    /**
     * Load image with URL
     *
     * @param context    context
     * @param url        target URL
     * @param mImageView target view
     */
    public static void loadImage(Context context, String url, AppCompatImageView mImageView, ProgressBar mProgressBar) {
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.ic_launcher_background)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .fitCenter()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        mProgressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        mProgressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(mImageView);
    }



    /**
     * Returns the consumer friendly device name
     */
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return makeWordFirstLetterCapital(model);
        }
        return makeWordFirstLetterCapital(manufacturer) + " " + model;
    }

    /**
     * this method make per word first letter capital
     *
     * @param s
     * @return
     */
    public static String makeWordFirstLetterCapital(String s) {
        final String DELIMITERS = " '-/";

        StringBuilder sb = new StringBuilder();
        boolean capNext = true;

        for (char c : s.toCharArray()) {
            c = (capNext)
                    ? Character.toUpperCase(c)
                    : Character.toLowerCase(c);
            sb.append(c);
            capNext = (DELIMITERS.indexOf((int) c) >= 0);
        }
        return sb.toString();
    }

    public static int convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return (int) px;
    }

    public static String saveImage(Context context, Uri uriPath) {
        String imagePath = "";
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uriPath);
            if (bitmap != null) {
                bitmap = checkExif(bitmap, uriPath.getPath());
                imagePath = saveToInternalStorage(bitmap, context);
            }

            //imageViewProfileImage.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
            bitmap = BitmapFactory.decodeFile(uriPath.getPath());
            if (bitmap != null) {
                bitmap = checkExif(bitmap, uriPath.getPath());
                imagePath = saveToInternalStorage(bitmap, context);
            }
        }

        return imagePath;
    }

    /*created by kalyani*/
    /*get date and time in 2021-04-08 16:45:14.084445 format*/
    public static String getDateTime(){
        Calendar c = Calendar.getInstance();
        //System.out.println("Current time => "+c.getTime());
        //2021-04-08 16:45:14.084445
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String formattedDate = df.format(c.getTime());
        // formattedDate have current date/time
        return formattedDate;
    }

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static Bitmap checkExif(Bitmap scaledBitmap, String filePath) {
        //check the rotation of the image and display it properly
        ExifInterface exif;
        Bitmap bitmap = null;
        try {
            exif = new ExifInterface(filePath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            bitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(),
                    matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static String saveToInternalStorage(Bitmap bitmapImage, Context context) {

        Bitmap converetdImage = getResizedBitmap(bitmapImage, Constants.DEFAULT_IMAGE_SIZE);
        ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir(Constants.INTERNAL_FOLDER_NAME, Context.MODE_PRIVATE);
        // Create imageDir
        String imageFileName = "picture_" + System.currentTimeMillis() + ".jpg";
        File mypath = new File(directory, imageFileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            converetdImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mypath.getAbsolutePath();
    }

    // To hide soft keyboard
    public static void hideKeyBoard(Activity activity) {

        // Check if no view has focus:
        View view = activity.getCurrentFocus();
        if (view != null) {
            try {

                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // To show soft keyboard
    public static void showKeyBoard(Activity activity) {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static boolean isValidDate(String pDateString) throws ParseException {
        Date date = null;
        try {
            if (pDateString.contains("/")) {
                String spilt[] = pDateString.split("/");
                if (spilt.length > 1 && Integer.parseInt(spilt[0]) < 13) {
                    String lastDayOfMonth = getDate(spilt[0], spilt[1]);
                    pDateString = lastDayOfMonth + "/" + spilt[0] + "/20" + spilt[1];
                    date = new SimpleDateFormat("dd/MM/yyyy").parse(pDateString);
                    return new Date().before(date);
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static String getDate(String sMonth, String sYear) {
        Date date = null;
        DateFormat DATE_FORMAT = null;
        try {
            int month = Integer.parseInt(sMonth);
            int year = Integer.parseInt("20" + sYear);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.MONTH, month - 1);
            calendar.set(Calendar.YEAR, year);
            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.add(Calendar.DATE, -1);

            calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
            date = calendar.getTime();
            DATE_FORMAT = new SimpleDateFormat("dd");
            LogUtil.printLog("last day of month ", DATE_FORMAT.format(date));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return DATE_FORMAT.format(date);
    }


    // For formatting amount according country
    public static String getFormattedAmount(String amount, EditText editTextAmount) {
        String formattedString = null;
        if (amount.length() == 0) {
            amount = "0.00";
        }
        try {
            String cleanString = amount.toString().replaceAll("\\D", "");

            double parsed = Double.parseDouble(cleanString);
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("en", "US"));
            String formatted = numberFormat.format((parsed / 100)).replace(numberFormat.getCurrency().getSymbol(), "");
            String formattedsymbol = formatted.replace(((DecimalFormat) numberFormat).getDecimalFormatSymbols().getCurrencySymbol(), "");
            //  formattedString = formatted.substring(1).trim();
            formattedString = formattedsymbol.replaceAll("\\s+", "");
            if (parsed == 0.0) {
                editTextAmount.setText("");
            } else {
                editTextAmount.setText(formattedString);
                editTextAmount.setSelection(editTextAmount.getText().length());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return formattedString;
    }

    /**
     * this method format the Amount in decimal and ,
     *
     * @param amount amount
     * @return formatted amount
     */
    public static String formatAmountInDecimal(String amount) {
        String formattedAmount = amount;
        try {
            formattedAmount = String.format(Locale.US, "%,.2f", Double.parseDouble(formattedAmount));
        } catch (Exception e) {
            e.printStackTrace();
            return amount;
        }
        return formattedAmount;
    }


    // For formatting amount according country
    public static String getFormattedAmountTextView(String amount, TextView editTextAmount) {
        String formattedString = null;
        try {
            String cleanString = amount.toString().replaceAll("\\D", "");
            double parsed = Double.parseDouble(cleanString);
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
            String formatted = numberFormat.format((parsed / 100)).replace(numberFormat.getCurrency().getSymbol(), "");
            String formattedsymbol = formatted.replace(((DecimalFormat) numberFormat).getDecimalFormatSymbols().getCurrencySymbol(), "");

            formattedString = formattedsymbol.replaceAll("\\s+", "");

            if (parsed == 0.0) {
                editTextAmount.setText("");
            } else {
                editTextAmount.setText(formattedString);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return formattedString;
    }


    public static String getSpaceInAcNumber(String data) {
        StringBuilder sb = new StringBuilder(data);
        for (int i = sb.length() - 4; i > 0; i -= 4)
            sb.insert(i, ' ');
        data = sb.toString();
        System.out.println(data);
        return data;

    }

    /**
     * convert millis to Date
     *
     * @param milliSeconds millis
     * @param dateFormat   date format
     * @return formatted  date
     */
    public static String getDateByMillis(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.getDefault());
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Date date = new Date(milliSeconds);
        return formatter.format(date.getTime());
    }

    /**
     * convert date string to time millis
     *
     * @param dateString date t
     * @param dateFormat date format
     * @return millis  time millis
     */
    public static long getMillisByDate(String dateString, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.getDefault());
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        try {
            Date date = formatter.parse(dateString);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }


    // Create a DateFormatter object for displaying date in specified format.
    public static String getDateByMillis(String milliSeconds, String dateFormat) {
        try {
            long lMilliSeconds = Long.parseLong(milliSeconds);
            SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.getDefault());
            // Create a calendar object that will convert the date and time value in milliseconds to date.
            Date date = new Date(lMilliSeconds);
            return formatter.format(date.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    //Days left from cureent datre
    public static String finddateDifference(String inputDateString) {
//        String inputDateString = "09/03/2019";
        Calendar calCurr = Calendar.getInstance();
        Calendar day = Calendar.getInstance();
        try {
            day.setTime(new SimpleDateFormat("MM/dd/yyyy").parse(inputDateString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (day.after(calCurr)) {
            System.out.println("Days Left: " + (day.get(Calendar.DAY_OF_MONTH) - (calCurr.get(Calendar.DAY_OF_MONTH))));
        }
        return String.valueOf(day.get(Calendar.DAY_OF_MONTH) - (calCurr.get(Calendar.DAY_OF_MONTH)));
    }

    public static String getCurrentDate(String format) {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat(format, Locale.getDefault());
        return df.format(c);
    }

    /**
     * @param days 1= last 30 days, 2 = last 60 days 3 = last 90 days
     * @return
     */
    public static long getPreviousDateByDay(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -(30 * days));//
        return calendar.getTime().getTime();
    }


    public static long getCurrentDateMillis() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime().getTime();
    }

    public static String takeScreenshot(Activity context) {
        String sharePath = "";
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpeg";

            // create bitmap screen capture
            View v1 = context.getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            //setting screenshot in imageview
            String filePath = imageFile.getPath();

            Bitmap ssbitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
//            iv.setImageBitmap(ssbitmap);
            sharePath = filePath;

        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
        return sharePath;
    }

    public static void shareOptions(String sharePath, Activity activity) {

        LogUtil.printLog("File path ", sharePath);
        File file = new File(sharePath);
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        activity.startActivity(intent);

    }

    public static boolean isNumberSequential(String str) {

        boolean result = true;
        List<Integer> list = new ArrayList<>();
        try {

            for (int i = 0; i < str.length(); i++) {
                list.add(Integer.parseInt(str.charAt(i) + ""));
            }

            for (int i = 0; i < list.size() - 1; i++) {
                if (list.get(i + 1) - list.get(i) == 1)
                    result = true;
                else {
                    result = false;
                    break;
                }
            }
        } catch (Exception ex) {
            result = false;
        }
        return result;
    }

    public static boolean isNumberRepeative(String str) {

        Set<String> set = new HashSet<>();

        for (int i = 0; i < str.length(); i++) {
            set.add(str.charAt(i) + "");
        }

        if (set.size() == 1)
            return true;
        else
            return false;

    }

    /**
     * this method hash the account number with XXXX XXXX 1234
     *
     * @param accountNumber to be hash
     * @return hashed account number
     */
    public static String getHashAccountNumber(String accountNumber) {
        if (TextUtils.isEmpty(accountNumber)) {
            return "";
        }
        String a4;
        String last;
        if (accountNumber.length() > 4) {
            a4 = accountNumber.substring(accountNumber.length() - 4, accountNumber.length());
            last = accountNumber.substring(0, accountNumber.length() - 4);
            last = last.replaceAll("\\S", "X");
            last = last.replaceAll("....", "$0 ");
        } else {
            a4 = accountNumber;
            last = "";
        }
        return last + " " + a4;
    }

    /**
     * this method hash the account number with XXXX XXXX XX34
     *
     * @param accountNumber to be hash
     * @return hashed account number
     */
    public static String getHashAccountNumber_two(String accountNumber) {
        if (TextUtils.isEmpty(accountNumber)) {
            return "";
        }
        String a4;
        String last;
        if (accountNumber.length() > 4) {
            a4 = accountNumber.substring(accountNumber.length() - 2, accountNumber.length());
            last = accountNumber.substring(0, accountNumber.length() - 2);
            last = last.replaceAll("\\S", "X");
            last = last.replaceAll("....", "$0 ");
        } else {
            a4 = accountNumber;
            last = "";
        }
        return last + " " + a4;
    }

    /**
     * return first last name first character
     *
     * @param name name
     * @return char
     */
    public static String getFirstLastNameFirstChar(String name) {
        String SinglePrefix = "";
        String splitindex[] = name.split(" ");

        try {
            if (splitindex.length >= 2) {
                char Prefix = splitindex[0].charAt(0);
                char Prefix2 = (!splitindex[1].isEmpty() ? splitindex[1].charAt(0) : ' ');
                SinglePrefix = ("" + Prefix + Prefix2).toUpperCase();
            } else {
                SinglePrefix = "" + splitindex[0].charAt(0);
            }
            SinglePrefix = SinglePrefix.toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SinglePrefix;
    }

    public static String parseDate(String inputDateString, SimpleDateFormat inputDateFormat, SimpleDateFormat outputDateFormat) {
        Date date = null;
        String outputDateString = null;
        try {
            date = inputDateFormat.parse(inputDateString);
            outputDateString = outputDateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputDateString;
    }

    public static String DateToMilise(String inputDateString) {
        String milies = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        try {
            Date mDate = sdf.parse(inputDateString);
            long timeInMilliseconds = mDate.getTime();
            milies = String.valueOf(timeInMilliseconds);
            System.out.println("Date in milli :: " + timeInMilliseconds);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return milies;
    }

    public static String getContactName(final String phoneNumber, Context context) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));

        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};

        String contactName = "";
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                contactName = cursor.getString(0);
            }
            cursor.close();
        }

        return contactName;
    }

    public static String Base64Encoded(String base64String) {
        String base64 = base64String;
        try {
            byte[] data = base64String.getBytes("UTF-8");
            base64 = Base64.encodeToString(data, Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return base64;
    }

    public static String Base64Decoded(String base64String) {
        String text = "";
        try {
            byte[] data = Base64.decode(base64String, Base64.DEFAULT);
            text = new String(data, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }

    public static String getLangId(String lang) {
        if (lang.equalsIgnoreCase("en")) {
            return "0";
        } else if (lang.equalsIgnoreCase("fr")) {
            return "1";
        }
        return "0";
    }

    /**
     * navigate user to app store
     *
     * @param context context
     */
    public static void openAppMarketLink(Context context) {
        final String appPackageName = context.getPackageName();
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    /**
     * This method generates a unique merchant TransactionID using the
     * UUID/emcertID and the current timestamp. This is required for miggs.
     *
     * @param context context
     * @return a unique merchantTrx ref.
     */
    public static String generateMerchantTrxRef(Context context) {
        String UUID = SharedPref.getInstance(context.getApplicationContext()).read(SharedPrefKey.DEVICE_ID, "");
        UUID = UUID.substring(0, 9);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return UUID + "-" + timestamp.getTime();
        //  return "12345";
    }

    public static String generateRandomString() {
        SecureRandom secureRandom = new SecureRandom();
        /** Length of password. @see #generateRandomPassword() */
        int PASSWORD_LENGTH = 15;
        String letters = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ23456789+@";

        String pw = "";
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int index = (int) (secureRandom.nextDouble() * letters.length());
            pw += letters.substring(index, index + 1);
        }
        LogUtil.printLog(TAG, "String str=" + pw);
        return pw;
    }

    public static void showCustomAlertDialog(Context mContext, String title, String msg, String yesBTNTxt
            , String noBTNTxt, final CustomDialogHelper customDialogHelper) {


        new AlertDialog.Builder(mContext)
                .setTitle(title)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(yesBTNTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        customDialogHelper.onYesButtonClick();
                    }
                })
                .setNegativeButton(noBTNTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        customDialogHelper.onNoButtonClick();
                    }
                })
                .show();
    }

    /*change date format dd-MM-yyyy to yyyy-MM-dd*/
    public static String changeDateFormat(String mStringDate) {
        String mStringChangedDate = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT_dd_MM_yyyy);
            Date newDate = format.parse(mStringDate);

            format = new SimpleDateFormat(Constants.DATE_FORMAT_YYYY_MM_DD);
            mStringChangedDate = format.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return mStringChangedDate;
    }

 /*change date format dd-MM-yyyy to yyyy-MM-dd*/
    public static String changeDateFormatDDMMYYYY(String mStringDate) {
        String mStringChangedDate = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT_dd_mm_yyyy_);
            Date newDate = format.parse(mStringDate);

            format = new SimpleDateFormat(Constants.DATE_FORMAT_YYYY_MM_DD);
            mStringChangedDate = format.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return mStringChangedDate;
    }

    // For animation of views in list
    public static void runLayoutAnimation(Context context, final RecyclerView recyclerView) {
      /*  final LayoutAnimationController controller =
                //AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_bottom);

        //recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();*/
    }


    public static Uri getUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }

    public static Bitmap getBitmap(Context context, Uri uri) throws IOException {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        return bitmap;
    }

    public static Bitmap ContactImages(Context context, String number)
    {
        ContentResolver contentResolver = context.getContentResolver();
        String contactId = null;
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));

        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID};

        Cursor cursor =
                contentResolver.query(
                        uri,
                        projection,
                        null,
                        null,
                        null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID));
            }
            cursor.close();
        }

        Bitmap photo = null;

        try {
            if(contactId != null) {
                InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(),
                        ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(contactId)));

                if (inputStream != null) {
                    photo = BitmapFactory.decodeStream(inputStream);
                }

                assert inputStream != null;
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return photo;

    }
}