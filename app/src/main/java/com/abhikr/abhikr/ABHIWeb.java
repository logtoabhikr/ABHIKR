package com.abhikr.abhikr;


import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;

import com.abhikr.abhikr.customtab.ActionBroadcastReceiver;
import com.abhikr.abhikr.customtab.CustomTabActivityHelper;
import com.abhikr.abhikr.customtab.CustomTabsHelper;
import com.abhikr.abhikr.customtab.WebviewFallback;

/**
 * Created by com on 11/8/2017.
 */

public class ABHIWeb {

    private Context mContext;

    public ABHIWeb(Context mContext) {
        this.mContext = mContext;
    }
    private static final int TOOLBAR_SHARE_ITEM_ID = 1;
    public void redirectUsingCustomTab(String url)
    {
        //Uri uri = Uri.parse(url);
        CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();

        // set desired toolbar colors
        setToolbarColor(mContext,intentBuilder);
        setSecondaryToolbarColor(mContext,intentBuilder);
        //intentBuilder.setToolbarColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
        //intentBuilder.setSecondaryToolbarColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDarknew));

        //intentBuilder.setShowTitle(true);//show website title
        setShowTitle(intentBuilder);

        //setCloseButtonIcon(mContext, intentBuilder);
       /* final Bitmap backButton = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_arrow_back);
        intentBuilder.setCloseButtonIcon(backButton);*/
        /*intentBuilder.setCloseButtonIcon(
                BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_arrow_back));*/

        intentBuilder.setStartAnimations(mContext, R.anim.simple_grow, R.anim.simple_shrink);
        intentBuilder.setExitAnimations(mContext, R.anim.simple_grow,
                R.anim.simple_shrink);

        //intentBuilder.enableUrlBarHiding();//show url heading
        enableUrlBarHiding(intentBuilder);

        // this line of code not workng fine on samsung touchwiz ui
        String actionLabel = mContext.getString(R.string.app_name);
        Bitmap icon = BitmapFactory.decodeResource(mContext.getResources(),
                android.R.drawable.ic_menu_share);
        PendingIntent pendingIntent =
                createPendingIntent();

        intentBuilder.setActionButton(icon, actionLabel, pendingIntent);

        //setShareActionButton(mContext,intentBuilder,url);

        //intentBuilder.addDefaultShareMenuItem();
        addShareMenuItem(intentBuilder);

        addCopyMenuItem(mContext, intentBuilder);
     /*   String menuItemTitle =mContext.getString(R.string.menu_item_title);
        PendingIntent menuItemPendingIntent =
                createPendingIntent(ActionBroadcastReceiver.ACTION_MENU_ITEM);
        intentBuilder.addMenuItem(menuItemTitle, menuItemPendingIntent);*/// use to add menu title like your app nam
        /*Intent i=new Intent(getApplicationContext(),Home.class);
        startActivity(i);*/
     /*   String actionLabel1 = mContext.getString(R.string.action_button_toast_text);
        Bitmap icon1 = BitmapFactory.decodeResource(mContext.getResources(),
                android.R.drawable.ic_menu_share);
        pendingIntent =
                createPendingIntent(ActionBroadcastReceiver.ACTION_TOOLBAR);// usimg this get bottom toolbar for any purpose
        intentBuilder.addToolbarItem(TOOLBAR_ITEM_ID, icon1, actionLabel1, pendingIntent);*/


        CustomTabsIntent customTabsIntent = intentBuilder.build();

        CustomTabsHelper.addKeepAliveExtra(mContext, customTabsIntent.intent);

        //customTabsIntent.launchUrl(this, uri);
        CustomTabActivityHelper.openCustomTab(
                (Activity) mContext, customTabsIntent, Uri.parse(url), new WebviewFallback(){
                    @Override
                    public void openUri(Activity activity, Uri uri) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        activity.startActivity(intent);
                        //super.openUri(activity, uri);
                    }
                });
    }
    private PendingIntent createPendingIntent() {
        Intent actionIntent = new Intent(
                this.mContext, ActionBroadcastReceiver.class);
        actionIntent.putExtra(ActionBroadcastReceiver.KEY_ACTION_SOURCE, ActionBroadcastReceiver.ACTION_ACTION_BUTTON);
        return PendingIntent.getBroadcast(
                mContext, ActionBroadcastReceiver.ACTION_ACTION_BUTTON, actionIntent, 0);
    }


 /*   CustomTabsSession session = getSession();
    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder(session);
            builder.setToolbarColor(Color.parseColor(TOOLBAR_COLOR)).setShowTitle(true);
    prepareMenuItems(builder);
    prepareActionButton(builder);
            if (session != null) prepareBottombar(builder);
            builder.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
            builder.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right);
            builder.setCloseButtonIcon(
                    BitmapFactory.decodeResource(getResources(), R.drawable.ic_arrow_back));
    CustomTabsIntent customTabsIntent = builder.build();
            if (session != null) {
        CustomTabsHelper.addKeepAliveExtra(this, customTabsIntent.intent);
    } else {
        if (!TextUtils.isEmpty(mPackageNameToBind)) {
            customTabsIntent.intent.setPackage(mPackageNameToBind);
        }
    }
            customTabsIntent.launchUrl(this, Uri.parse(url));*/
 public static void openTabOutside(Context context, String url) {
     CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();

     enableUrlBarHiding(builder);
     setToolbarColor(context, builder);
     setSecondaryToolbarColor(context, builder);
     setCloseButtonIcon(context, builder);
     setShowTitle(builder);
     setAnimations(context, builder);
     setShareActionButton(context, builder, url);
     addToolbarShareItem(context, builder, url);
     addShareMenuItem(builder);
     addCopyMenuItem(context, builder);

     CustomTabsIntent customTabsIntent = builder.build();
     customTabsIntent.launchUrl(context, Uri.parse(url));
     //Setting a custom back button
        /*intentBuilder.setCloseButtonIcon(BitmapFactory.decodeResource(
                mContext.getResources(), R.drawable.ic_arrow_back_white));*/
 }
    /* Enables the url bar to hide as the user scrolls down on the page */
    private static void enableUrlBarHiding(CustomTabsIntent.Builder builder) {
        builder.enableUrlBarHiding();
    }

    /* Sets the toolbar color */
    private static void setToolbarColor(Context context, CustomTabsIntent.Builder builder) {
        builder.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary));
    }

    /* Sets the secondary toolbar color */
    private static void setSecondaryToolbarColor(Context context, CustomTabsIntent.Builder builder) {
        builder.setSecondaryToolbarColor(ContextCompat.getColor(context, R.color.colorPrimaryVariant));
    }

    /* Sets the Close button icon for the custom tab */
    private static void setCloseButtonIcon(Context context, CustomTabsIntent.Builder builder) {
        builder.setCloseButtonIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_arrow_back_black_24dp));
    }

    /* Sets whether the title should be shown in the custom tab */
    private static void setShowTitle(CustomTabsIntent.Builder builder) {
        builder.setShowTitle(true);
    }

    /* Sets animations */
    private static void setAnimations(Context context, CustomTabsIntent.Builder builder) {
        builder.setStartAnimations(context, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        builder.setExitAnimations(context, android.R.anim.slide_out_right, android.R.anim.slide_in_left);
    }

    /* Sets share action button that is displayed in the Toolbar */
    private static void setShareActionButton(Context context, CustomTabsIntent.Builder builder, String url) {
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), android.R.drawable.ic_menu_share);
        String label = "Share via";

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, url);
        shareIntent.setType("text/plain");

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, shareIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setActionButton(icon, label, pendingIntent);
    }

    /* Adds share item that is displayed in the secondary Toolbar */
    private static void addToolbarShareItem(Context context, CustomTabsIntent.Builder builder, String url) {
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), android.R.drawable.ic_menu_share);
        String label = "Share via";

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, url);
        shareIntent.setType("text/plain");

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, shareIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.addToolbarItem(TOOLBAR_SHARE_ITEM_ID, icon, label, pendingIntent);

    }

    /* Adds a default share item to the menu */
    private static void addShareMenuItem(CustomTabsIntent.Builder builder) {
        builder.addDefaultShareMenuItem();
    }

    /* Adds a copy item to the menu */
    private static void addCopyMenuItem(Context context, CustomTabsIntent.Builder builder) {
        String label = context.getString(R.string.menu_item_title);
        Intent intent = new Intent(context, CopyBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.addMenuItem(label, pendingIntent);
    }

    public static class CopyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
           /* String url = intent.getDataString();

            ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData data = ClipData.newPlainText("Link", url);
            clipboardManager.setPrimaryClip(data);*/

            Intent iak=new Intent(context.getApplicationContext(), Home.class);
            iak.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(iak);
            //Toast.makeText(context, "Copied " + intent.getDataString(), Toast.LENGTH_SHORT).show();
        }
    }
}
