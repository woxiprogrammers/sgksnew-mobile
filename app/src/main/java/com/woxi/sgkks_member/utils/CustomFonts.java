package com.woxi.sgkks_member.utils;

import android.content.Context;
import android.graphics.Typeface;

import java.lang.reflect.Field;

/**
 * <b><b>public class CustomFonts </b></b>
 * <p>This class is used to override default fonts with custom font's asset</p>
 * Created by Sharvari.
 */
public class CustomFonts {
    public static void overrideFont(Context context, String defaultFontNameToOverride, String customFontFileNameInAssets) {
        try {
            final Typeface customFontTypeface = Typeface.createFromAsset(context.getAssets(), customFontFileNameInAssets);
            final Field defaultFontTypefaceField = Typeface.class.getDeclaredField(defaultFontNameToOverride);
            defaultFontTypefaceField.setAccessible(true);
            defaultFontTypefaceField.set(null, customFontTypeface);
        } catch (Exception e) {
            new AppCommonMethods().LOG(0, "CustomFonts", "Can not set custom font");
        }
    }
}
