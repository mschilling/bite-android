package com.move4mobile.apps.bite;

import android.app.Application;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by casvd on 28-9-2016.
 */

public class BiteApplication extends Application {

    private static final String TAG = ".BiteApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseAnalytics.getInstance(this);

        initFonts();
        initEmojis();
    }

    public static class Fonts {
        public static Typeface COMPASSE, COMPASSE_BOLD, COMPASSE_EXTRA_BOLD, COMPASSE_EXTRA_BOLD_ITALIC;

        public static Typeface getFont(String asset){
            switch (asset) {
                case "compasse":
                    return COMPASSE;
                case "compasse_bold":
                    return COMPASSE_BOLD;
                case "compasse_extra_bold":
                    return COMPASSE_EXTRA_BOLD;
                case "compasse_extra_bold_italic":
                    return COMPASSE_EXTRA_BOLD_ITALIC;
                default:
                    return COMPASSE;
            }
        }
    }

    private void initFonts() {
        Fonts.COMPASSE = Typeface.createFromAsset(getAssets(), "fonts/Compasse.otf");
        Fonts.COMPASSE_BOLD = Typeface.createFromAsset(getAssets(), "fonts/Compasse-Bold.otf");
        Fonts.COMPASSE_EXTRA_BOLD = Typeface.createFromAsset(getAssets(), "fonts/Compasse-ExtraBold.otf");
        Fonts.COMPASSE_EXTRA_BOLD_ITALIC = Typeface.createFromAsset(getAssets(), "fonts/Compasse-ExtraBoldItalic.otf");
    }

    public static class Emojis {
        public static Drawable DESSERT, DRINKS, FISH, FRIES, HAMBURGER, KEBAB, MEAT, PIZZA, SAUCE, SNACKS;

        public static Drawable getEmoji(int id){
            switch (id) {
                case 0:
                    return DESSERT;
                case 1:
                    return DRINKS;
                case 2:
                    return FISH;
                case 3:
                    return FRIES;
                case 4:
                    return HAMBURGER;
                case 5:
                    return KEBAB;
                case 6:
                    return MEAT;
                case 7:
                    return PIZZA;
                case 8:
                    return SAUCE;
                case 9:
                    return SNACKS;
                default:
                    return null;
            }
        }
    }

    private void initEmojis() {
        Emojis.DESSERT = getResources().getDrawable(R.drawable.ic_dessert, getTheme());
        Emojis.DRINKS = getResources().getDrawable(R.drawable.ic_drinks, getTheme());
        Emojis.FISH = getResources().getDrawable(R.drawable.ic_fish, getTheme());
        Emojis.FRIES = getResources().getDrawable(R.drawable.ic_fries, getTheme());
        Emojis.HAMBURGER = getResources().getDrawable(R.drawable.ic_hamburger, getTheme());
        Emojis.KEBAB = getResources().getDrawable(R.drawable.ic_kebab, getTheme());
        Emojis.MEAT = getResources().getDrawable(R.drawable.ic_meat, getTheme());
        Emojis.PIZZA = getResources().getDrawable(R.drawable.ic_pizza, getTheme());
        Emojis.SAUCE = getResources().getDrawable(R.drawable.ic_sauce, getTheme());
        Emojis.SNACKS = getResources().getDrawable(R.drawable.ic_snacks, getTheme());
    }
}
