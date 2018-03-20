package com.easybusiness.eb_androidapp.UI;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import com.easybusiness.eb_androidapp.AsyncTask.DeleteAsyncTask;
import com.easybusiness.eb_androidapp.R;

public class Dialogs {

    public static AlertDialog createDeleteDialog(final Activity activity, final View view, final String entityName, final int itemID, final String itemName) {
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
            }
        });
        return builder.create();
    }

}
