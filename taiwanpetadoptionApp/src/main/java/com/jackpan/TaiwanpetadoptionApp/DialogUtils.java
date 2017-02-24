package com.jackpan.TaiwanpetadoptionApp;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.jackpan.TaiwanpetadoptionApp.R;

public class DialogUtils {
	/**
     * 得到自定义的progressDialog
     * @param context
     * @return
     */
    public static Dialog createLoadingDialog(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.layout_loading_dialog, null);        // 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);  // 加载布局
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context,R.anim.loading_animation); // 加载动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);             // 使用ImageView显示动画
        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog); // 创建自定义样式dialog
        
        //loadingDialog.setCancelable(false);// 不可以用"返回键"取消
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        return loadingDialog;
    }
	
}
