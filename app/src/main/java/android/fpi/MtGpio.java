package android.fpi;

import android.util.Log;
import android.zyapi.CommonApi;

/**
 * @author 1
 * @hide
 */
public class MtGpio {

	private boolean mOpen = false;
	private CommonApi mCommonApi = new CommonApi();
	;
	private static MtGpio mMe = null;

	private MtGpio() {
		mOpen = openDev() >= 0 ? true : false;
		Log.d("MtGpio", "openDev->ret:" + mOpen);
	}

	public static MtGpio getInstance() {
		if (mMe == null) {
			mMe = new MtGpio();
			mMe.InitGpio();
		}
		return mMe;
	}

	public void FPPowerSwitch(boolean bonoff) {
		if (bonoff) {
			//FP Power
			mCommonApi.setGpioMode(14, 0);
			mCommonApi.setGpioDir(14, 1);
			mCommonApi.setGpioOut(14, 1);
		} else {
			mCommonApi.setGpioMode(14, 0);
			mCommonApi.setGpioDir(14, 1);
			mCommonApi.setGpioOut(14, 0);
		}
	}

	public void wiegandSwitch(boolean bonoff) {
		if (bonoff) {
			mCommonApi.setGpioMode(65, 0);
			mCommonApi.setGpioDir(65, 1);
			mCommonApi.setGpioOut(65, 1);
			mCommonApi.setGpioMode(69, 0);
			mCommonApi.setGpioDir(69, 1);
			mCommonApi.setGpioOut(69, 1);
		} else {
			mCommonApi.setGpioMode(65, 0);
			mCommonApi.setGpioDir(65, 1);
			mCommonApi.setGpioOut(65, 0);
			mCommonApi.setGpioMode(69, 0);
			mCommonApi.setGpioDir(69, 1);
			mCommonApi.setGpioOut(69, 0);
		}
	}

	public void InitGpio() {
		sGpioMode(19, 0);
		sGpioDir(19, 0);

		mCommonApi.setGpioMode(21, 0);
		mCommonApi.setGpioDir(21, 1);

		mCommonApi.setGpioMode(34, 0);
		mCommonApi.setGpioDir(34, 1);
	}

	public void LockSwitch(boolean bonoff) {
		if (bonoff) {
			mCommonApi.setGpioOut(21, 1);
//            mCommonApi.setGpioOut(34, 1);
		} else {
			mCommonApi.setGpioOut(21, 0);
//            mCommonApi.setGpioOut(34, 0);
		}
	}

	public void AlarmSwitch(boolean bonoff) {
		if (bonoff) {
			sGpioOut(13, 1);
		} else {
			sGpioOut(13, 0);
		}
	}

	public boolean ButtonIsPress() {
		mCommonApi.setGpioMode(0,0);
		mCommonApi.setGpioDir(0,0);
		Log.d("MtGpio", "getGpioIn(19):" + getGpioIn(0));
		if (mCommonApi.getGpioIn(0) == 0) {
			return true;
		}else {
			return false;
		}
//        if (getGpioIn(19) == 1) return true;
//        else return false;
	}

	public boolean isOpen() {
		return mOpen;
	}

	public void sGpioDir(int pin, int dir) {
		setGpioDir(pin, dir);
	}

	public void sGpioOut(int pin, int out) {
		setGpioOut(pin, out);
	}

	public int getGpioPinState(int pin) {
		return getGpioIn(pin);
	}

	public void sGpioMode(int pin, int mode) {
		setGpioMode(pin, mode);
	}

	// JNI
	private native int openDev();

	public native void closeDev();

	private native int setGpioMode(int pin, int mode);

	private native int setGpioDir(int pin, int dir);

	private native int setGpioPullEnable(int pin, int enable);

	private native int setGpioPullSelect(int pin, int select);

	public native int setGpioOut(int pin, int out);

	private native int getGpioIn(int pin);

	static {
		System.loadLibrary("mtgpio");
	}
}
