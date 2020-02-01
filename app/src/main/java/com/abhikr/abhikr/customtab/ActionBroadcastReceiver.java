// Copyright 2015 Google Inc. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.abhikr.abhikr.customtab;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.abhikr.abhikr.R;

/**
 * A BroadcastReceiver that handles the Action Intent from the Custom Tab and shows the Url
 * in a Toast.
 */
public class ActionBroadcastReceiver extends BroadcastReceiver {
    public static final String KEY_ACTION_SOURCE = "com.abhikr.abhikr.ACTION_SOURCE";
    public static final int ACTION_ACTION_BUTTON = 1;
    public static final int ACTION_MENU_ITEM = 2;
    public static final int ACTION_TOOLBAR = 3;

    @Override
    public void onReceive(Context context, Intent intent) {
        String url = intent.getDataString();
        if (url != null) {
            String toastText =
                    getToastText(context, intent.getIntExtra(KEY_ACTION_SOURCE, -1), url);

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, url);

                Intent chooserIntent = Intent.createChooser(shareIntent, "Share to Your Friend");
                chooserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(chooserIntent);
        }
    }
    //Sharing content from CustomTabs with on a BroadcastReceiver

    private String getToastText(Context context, int actionId, String url) {
        switch (actionId) {
            case ACTION_ACTION_BUTTON:
                return context.getString(R.string.action_button_toast_text, url);
               /* Intent ak=new Intent(context,Home.class);
                context.startActivity(ak);*/
            case ACTION_MENU_ITEM:
                //return context.getString(R.string.menu_item_toast_text, url);
                //Intent ak=new Intent(context,Home.class);
               /* Intent iak=new Intent(context.getApplicationContext(),Home.class);
                iak.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(iak);*/
                return context.getString(R.string.toolbar_toast_text, url);
            case ACTION_TOOLBAR:
                return context.getString(R.string.toolbar_toast_text, url);
            default:
                return context.getString(R.string.unknown_toast_text, url);
        }
    }
}
