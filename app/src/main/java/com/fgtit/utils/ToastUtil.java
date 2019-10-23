package com.fgtit.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToastUtil {
	
	//Ĭ��
	public static void showToast(Context context, String msg) {
         Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
	
	
	public static void showToast(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
	}
	
	//����λ��
	public static void showToastTop(Context context, String msg) {
		Toast toast= Toast.makeText(context, msg, Toast.LENGTH_SHORT/*2000 */);
		toast.setGravity(Gravity.TOP| Gravity.CENTER, 0, 120);
		toast.show();
	}

	public static void showToastDef(Context context, String msg, int pos, int tm) {
		Toast toast= Toast.makeText(context, msg, tm);
		toast.setGravity(Gravity.TOP| Gravity.CENTER, 0, pos);
		toast.show();
	}
	
	public static void showToastEx(Context context, String msg, int tm) {
		Toast toast= Toast.makeText(context, msg, tm);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
	
	/*
	//��ͼƬ
	Toast toast=Toast.makeText(getApplicationContext(), "��ʾ��ͼƬ��toast", 3000); 
	toast.setGravity(Gravity.CENTER, 0, 0);  
	//����ͼƬ��ͼ���� 
	ImageView imageView= new ImageView(getApplicationContext()); 
	//����ͼƬ 
	imageView.setImageResource(R.drawable.ic_launcher); 
	//���toast�Ĳ��� 
	LinearLayout toastView = (LinearLayout) toast.getView(); 
	//���ô˲���Ϊ����� 
	toastView.setOrientation(LinearLayout.HORIZONTAL); 
	//��ImageView�ڼ��뵽�˲����еĵ�һ��λ�� 
	toastView.addView(imageView, 0); 
	toast.show();
	*/
	
	//��ȫ�Զ���
	/*
	private void showToast() {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.toast, null);
        image = (ImageView) view.findViewById(R.id.image);
        title = (TextView) view.findViewById(R.id.title);
        content = (TextView) view.findViewById(R.id.content);
        image.setBackgroundResource(R.drawable.ic_launcher);
        title.setText("�Զ���toast");
        content.setText("hello,self toast");
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }
	*/

}
