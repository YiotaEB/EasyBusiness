package com.easybusiness.eb_androidapp.UI;

import android.app.Activity;
import android.content.DialogInterface;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import com.easybusiness.eb_androidapp.AsyncTask.DeleteAsyncTask;
import com.easybusiness.eb_androidapp.AsyncTask.GetEmployeesAsyncTask;
import com.easybusiness.eb_androidapp.R;
import com.easybusiness.eb_androidapp.UI.Fragments.ViewEmployeesFragment;

public class Dialogs {

    public static AlertDialog createDeleteDialog(final Activity activity, final View view, final String entityName, final int itemID, final String itemName, final Fragment sender) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setIcon(R.drawable.ic_delete_black_24dp);
        builder.setTitle(R.string.delete_item);
        builder.setMessage(activity.getString(R.string.are_you_sure_delete) + " '" + itemName + "'");
        builder.setPositiveButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new DeleteAsyncTask(entityName, itemID, itemName, activity, view, dialogInterface).execute();
                if (sender instanceof ViewEmployeesFragment) {
                    final String sessionID = PreferenceManager.getDefaultSharedPreferences(activity).getString(MainActivity.PREFERENCE_SESSIONID, null);
                    new GetEmployeesAsyncTask("SessionID=" + sessionID, activity, view);
                }
            }
        });
        return builder.create();
    }

}
