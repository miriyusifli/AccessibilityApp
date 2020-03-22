package com.example.accessibilityapp.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.example.accessibilityapp.app.models.App;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utils {

    public static List<App> getInstalledApps(Context context) {
        PackageManager pm = context.getPackageManager();
        List<App> apps = new ArrayList<>();
        List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            if ((!isSystemPackage(p)) && !isMyself(p)) {
                String appName = p.applicationInfo.loadLabel(context.getPackageManager()).toString();
                Drawable icon = p.applicationInfo.loadIcon(context.getPackageManager());
                String packages = p.applicationInfo.packageName;
                apps.add(new App(appName, icon, packages));
            }
        }
        return apps;
    }
    private static boolean isSystemPackage(PackageInfo pkgInfo) {
        return (pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
    }
    public static boolean isMyself(PackageInfo pkgInfo){
        return pkgInfo.applicationInfo.packageName.equals("com.example.accessibilityapp");
    }

    public static String getRandomASCString(int n)
    {

        // lower limit for LowerCase Letters
        int lowerLimit = 32;

        // lower limit for LowerCase Letters
        int upperLimit = 126;

        Random random = new Random();

        // Create a StringBuffer to store the result
        StringBuilder r = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // take a random value between 97 and 122
            int nextRandomChar = lowerLimit
                    + (int)(random.nextFloat()
                    * (upperLimit - lowerLimit + 1));

            // append a character at the end of bs
            r.append((char)nextRandomChar);
        }

        // return the resultant string
        return r.toString();
    }


    public static String[] mergeListToArray(String[] arr, List <String> elements){
        String[] tempArr = new String[arr.length+elements.size()];
        System.arraycopy(arr, 0, tempArr, 0, arr.length);

        for(int i=0; i < elements.size(); i++)
            tempArr[arr.length+i] = elements.get(i);
        return tempArr;

    }

}
