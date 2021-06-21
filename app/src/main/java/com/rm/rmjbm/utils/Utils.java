package com.rm.rmjbm.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class Utils {

    public static void showCustomToast(String message, Context context) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        View toastView = toast.getView();
        TextView toastMessage = toastView.findViewById(android.R.id.message);
        toastMessage.setTextSize(20);
//        toastMessage.setCompoundDrawablePadding(16);
//        toastMessage.setTextColor(Color.RED);
//        toastMessage.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_fly, 0, 0, 0);
//        toastMessage.setGravity(Gravity.CENTER);
//        toastView.setBackgroundColor(Color.CYAN);
        toast.show();
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static String getOnlyNumerics(String str) {

        if (str == null) {
            return null;
        }

        StringBuffer strBuff = new StringBuffer();
        char c;

        for (int i = 0; i < str.length(); i++) {
            c = str.charAt(i);

            if (Character.isDigit(c)) {
                strBuff.append(c);
            }
        }
        return strBuff.toString();
    }

    public static boolean isTimeWith_in_Interval(String valueToCheck, String endTime, String startTime) {
        boolean isBetween = false;
        try {
            Date endTime1 = new SimpleDateFormat("HH:mm:ss").parse(endTime);
            Date startTime1 = new SimpleDateFormat("HH:mm:ss").parse(startTime);
            Date currentTime = new SimpleDateFormat("HH:mm:ss").parse(valueToCheck);
            if ((endTime1.equals(currentTime) || endTime1.after(currentTime)) && (startTime1.equals(currentTime) || startTime1.before(currentTime))) {
                isBetween = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isBetween;
    }

    public static boolean isTime_is_not_in_Interval(String valueToCheck, String endTime, String startTime) {
        boolean isBetween = false;
        try {
            Date endTime1 = new SimpleDateFormat("HH:mm:ss").parse(endTime);
            Date startTime1 = new SimpleDateFormat("HH:mm:ss").parse(startTime);
            Date currentTime = new SimpleDateFormat("HH:mm:ss").parse(valueToCheck);
//            System.out.println("isTime:: " + endTime1 + " :: " + currentTime);
//            System.out.println("isTime:: " + startTime1 + " :: " + currentTime);
//            System.out.println("isTime:: " + endTime1.equals(currentTime) + " :: " + endTime1.after(currentTime) + " :: " + startTime1.equals(currentTime) + " :: " + startTime1.before(currentTime));

            if ((endTime1.after(currentTime)) && (startTime1.equals(currentTime) || startTime1.before(currentTime))) {
                isBetween = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isBetween;
    }

    public static boolean isStopTime_is_not_in_Interval(String valueToCheck, String endTime, String startTime) {
        boolean isBetween = false;
        try {
            Date endTime1 = new SimpleDateFormat("HH:mm:ss").parse(endTime);
            Date startTime1 = new SimpleDateFormat("HH:mm:ss").parse(startTime);
            Date currentTime = new SimpleDateFormat("HH:mm:ss").parse(valueToCheck);

//            System.out.println("isTime:: " + endTime1 + " :: " + currentTime);
//            System.out.println("isTime:: " + startTime1 + " :: " + currentTime);

            System.out.println("isTime:: " + endTime1.equals(currentTime) + " :: " + endTime1.after(currentTime) + " :: " + startTime1.equals(currentTime) + " :: " + startTime1.before(currentTime));

            if ((endTime1.equals(currentTime) || endTime1.after(currentTime)) && (startTime1.before(currentTime))) {
                isBetween = true;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isBetween;
    }

    public static long convertTimeInMills(String strTime) {
        long timeInMilliseconds = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        try {
            Date mDate = sdf.parse(strTime);
            timeInMilliseconds = mDate.getTime();
            System.out.println("Date in milli :: " + timeInMilliseconds);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeInMilliseconds;
    }

    public static String totalBookedTime(String strTotalBookedTime, String strBookedTime) throws ParseException {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date date1 = timeFormat.parse(strTotalBookedTime);
        Date date2 = timeFormat.parse(strBookedTime);

        long sum = date1.getTime() + date2.getTime();

        String date3 = timeFormat.format(new Date(sum));
        System.out.println("The sum is " + date3);
        return date3;
    }

    public static String totalBookedDiffTime(String strTotalBookedTime, String strBookedTime, String selectedBookedTime) throws ParseException {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date1 = timeFormat.parse(strTotalBookedTime);
        Date date2 = timeFormat.parse(strBookedTime);
        Date date3 = timeFormat.parse(selectedBookedTime);
        long millis = date1.getTime() + date2.getTime() - date3.getTime();
        String diff = timeFormat.format(new Date(millis));
        System.out.println("The sum is " + diff);
        return diff;
    }

    public static String formatTimeUnit(int hourOfDay, int minute, int mSecond) {
        StringBuilder b = new StringBuilder();
        b.append(hourOfDay == 0 ? "00" : hourOfDay < 10 ? String.valueOf("0" + hourOfDay) :
                String.valueOf(hourOfDay));
        b.append(":");
        b.append(minute == 0 ? "00" : minute < 10 ? String.valueOf("0" + minute) :
                String.valueOf(minute));
        b.append(":");
        b.append(mSecond == 0 ? "00" : mSecond < 10 ? String.valueOf("0" + mSecond) :
                String.valueOf(mSecond));
        return b.toString();
    }

    public static String formatTimeAMPM(int hourOfDay, int minute) {
        String AM_PM = " AM", finalTime;
        String mm_precede = "";
        if (hourOfDay >= 12) {
            AM_PM = " PM";
            if (hourOfDay >= 13 && hourOfDay < 24) {
                hourOfDay -= 12;
            } else {
                hourOfDay = 12;
            }
        } else if (hourOfDay == 0) {
            hourOfDay = 12;
        }
        if (minute < 10) {
            mm_precede = "0";
        }
        finalTime = hourOfDay + ":" + mm_precede + minute + AM_PM;
        return finalTime;
    }

    public static String formatDuration(long bookedTime) {

        long ss = (bookedTime / 1000) % 60;
        long mm = (bookedTime / (1000 * 60)) % 60;
        long hh = bookedTime / (1000 * 60 * 60);

        StringBuilder b = new StringBuilder();
        b.append(hh == 0 ? "00" : hh < 10 ? String.valueOf("0" + hh) :
                String.valueOf(hh));
        b.append(":");
        b.append(mm == 0 ? "00" : mm < 10 ? String.valueOf("0" + mm) :
                String.valueOf(mm));
        b.append(":");
        b.append(ss == 0 ? "00" : ss < 10 ? String.valueOf("0" + ss) :
                String.valueOf(ss));
        return b.toString();
    }


    public static String combinationFormatter(final long millis) {
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis)
                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis));
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis)
                - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis));
        long hours = TimeUnit.MILLISECONDS.toHours(millis);

        StringBuilder b = new StringBuilder();
        b.append(hours == 0 ? "00" : hours < 10 ? String.valueOf("0" + hours) :
                String.valueOf(hours));
        b.append(":");
        b.append(minutes == 0 ? "00" : minutes < 10 ? String.valueOf("0" + minutes) :
                String.valueOf(minutes));
        b.append(":");
        b.append(seconds == 0 ? "00" : seconds < 10 ? String.valueOf("0" + seconds) :
                String.valueOf(seconds));
        return b.toString();
    }

    public static String getTimeFormatted(String time) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("hh:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm");
        Date convertedDate = null;
        try {
            convertedDate = inputFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outputFormat.format(convertedDate);
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    public static Typeface getTypeface(Context context) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "fonts/Shruti.ttf");
        return tf;
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sbuf = new StringBuilder();
        for (int idx = 0; idx < bytes.length; idx++) {
            int intVal = bytes[idx] & 0xff;
            if (intVal < 0x10) sbuf.append("0");
            sbuf.append(Integer.toHexString(intVal).toUpperCase());
        }
        return sbuf.toString();
    }

    /**
     * Get utf8 byte array.
     *
     * @param str which to be converted
     * @return array of NULL if error was found
     */
    public static byte[] getUTF8Bytes(String str) {
        try {
            return str.getBytes("UTF-8");
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Load UTF8withBOM or any ansi text file.
     *
     * @param filename which to be converted to string
     * @return String value of File
     * @throws java.io.IOException if error occurs
     */
    public static String loadFileAsString(String filename) throws java.io.IOException {
        final int BUFLEN = 1024;
        BufferedInputStream is = new BufferedInputStream(new FileInputStream(filename), BUFLEN);
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(BUFLEN);
            byte[] bytes = new byte[BUFLEN];
            boolean isUTF8 = false;
            int read, count = 0;
            while ((read = is.read(bytes)) != -1) {
                if (count == 0 && bytes[0] == (byte) 0xEF && bytes[1] == (byte) 0xBB && bytes[2] == (byte) 0xBF) {
                    isUTF8 = true;
                    baos.write(bytes, 3, read - 3); // drop UTF8 bom marker
                } else {
                    baos.write(bytes, 0, read);
                }
                count += read;
            }
            return isUTF8 ? new String(baos.toByteArray(), "UTF-8") : new String(baos.toByteArray());
        } finally {
            try {
                is.close();
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * Returns MAC address of the given interface name.
     *
     * @param interfaceName eth0, wlan0 or NULL=use first interface
     * @return mac address or empty string
     */
    public static String getMACAddress(String interfaceName) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (interfaceName != null) {
                    if (!intf.getName().equalsIgnoreCase(interfaceName)) continue;
                }
                byte[] mac = intf.getHardwareAddress();
                if (mac == null) return "";
                StringBuilder buf = new StringBuilder();
                for (byte aMac : mac) buf.append(String.format("%02X:", aMac));
                if (buf.length() > 0) buf.deleteCharAt(buf.length() - 1);
                return buf.toString();
            }
        } catch (Exception ignored) {
        } // for now eat exceptions
        return "";
        /*try {
            // this is so Linux hack
            return loadFileAsString("/sys/class/net/" +interfaceName + "/address").toUpperCase().trim();
        } catch (IOException ex) {
            return null;
        }*/
    }

    /**
     * Get IP address from first non-localhost interface
     *
     * @param useIPv4 true=return ipv4, false=return ipv6
     * @return address or empty string
     */
    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':') < 0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        } // for now eat exceptions
        return "";
    }

    public static String get3DecimalValue(String value) {
        Float litersOfPetrol = Float.parseFloat(value);
        DecimalFormat df = new DecimalFormat("0.000");
        df.setMaximumFractionDigits(3);
        String result = df.format(litersOfPetrol);
        return result;
    }

    public static String get2DecimalValue(String value) {
        Float litersOfPetrol = Float.parseFloat(value);
        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);
        String result = df.format(litersOfPetrol);
        return result;
    }

    /*    public static void showNetworkAlert(CoordinatorLayout coordinatorLayout) {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_LONG)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                        }
                    });

            // Changing message text color
            snackbar.setActionTextColor(Color.RED);

            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.des);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
        }*/
    public static String trimLeadingZeros(String source) {
        for (int i = 0; i < source.length(); ++i) {
            char c = source.charAt(i);
            if (c != '0') {
                return source.substring(i);
            }
        }
        return ""; // or return "0";
    }

    public static void showNetworkAlert(Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(ConstantsUtils.TAG_NETWORK_ALERT_TITLE);
        builder.setMessage(ConstantsUtils.TAG_NETWORK_ALERT_MSG);
        builder.setNeutralButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    public static String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public static int setListViewHeightBasedOnChildren(ListView list) {
        int height = 0;
        for (int i = 0; i < list.getCount(); i++) {
            View childView = list.getAdapter().getView(i, null, list);
            childView.measure(
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            height += childView.getMeasuredHeight();
        }
        // dividers height
        height += list.getDividerHeight() * list.getCount();
        return height;
    }
}