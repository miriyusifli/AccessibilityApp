package com.example.accessibilityapp;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.accessibilityapp.utils.Utils;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.nio.charset.Charset;
import java.util.Random;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class UnitTests {

    @Test
    public void isMyself_isCorrect() {
        // Succeeds only if application package is "appPackage".
        final String appPackage = "com.example.accessibilityapp";
        PackageInfo myPackage = new PackageInfo();
        ApplicationInfo applicationInfo = new ApplicationInfo();
        applicationInfo.packageName = appPackage;
        myPackage.applicationInfo = applicationInfo;
        assert Utils.isMyself(myPackage);
    }

    @Test
    public void getRandomASCString_isCorrect() {
        // Succeeds only if the output is ASCII string
        int stringLength = new Random().nextInt(1000) + 1;
        String asciiStr = Utils.getRandomASCString(stringLength);
        boolean isASCII = Charset.forName("US-ASCII").newEncoder().canEncode(asciiStr);
        assert isASCII;
    }

}