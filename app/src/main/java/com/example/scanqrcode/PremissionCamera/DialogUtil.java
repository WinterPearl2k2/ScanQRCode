package com.example.scanqrcode.PremissionCamera;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogUtil {
    /**
     * Tạo dialog lựa chọn
     * @param context
     * @param pContent
     * @param dialogClickListener
     * @return
     */
    public static Dialog showSelectDiaLog(Context context, String title, String pContent,
                                          String pLefBtnStr, String pRightBtnStr,
                                          final DialogClickListener dialogClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog dialog = builder.setTitle(title)
                .setMessage(pContent)
                .setPositiveButton(pRightBtnStr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogClickListener.confirm();
                        dialogInterface.dismiss();
                    }
                }).setPositiveButton(pRightBtnStr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogClickListener.cancel();
                    }
                }).create();

        return dialog;
    }

    public interface DialogClickListener {

        public abstract void confirm();

        public abstract void cancel();
    }
}
