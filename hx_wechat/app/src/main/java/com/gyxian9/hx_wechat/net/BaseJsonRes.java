package com.gyxian9.hx_wechat.net;

import com.gyxian9.hx_wechat.App;
import com.gyxian9.hx_wechat.Constant;
import com.gyxian9.hx_wechat.common.Utils;
import com.juns.health.net.loopj.android.http.JsonHttpResponseHandler;


import org.json.JSONException;
import org.json.JSONObject;

public abstract class BaseJsonRes extends JsonHttpResponseHandler {

	@Override
	public void onSuccess(JSONObject response) {
		try {
			String result = response.getString(Constant.Result);
			// System.out.println("返回的值" + response);
			if (result == null) {
				Utils.showLongToast(App.getInstance(), Constant.NET_ERROR);
			} else if (result.equals("1")) {
				String str_data = response.getString(Constant.Value);
				onMySuccess(str_data);
			} else {
				String str = response.getString(Constant.Info);
				Utils.showLongToast(App.getInstance(), str);
				onMyFailure();
			}
		} catch (JSONException e) {
			e.printStackTrace();
			onMyFailure();
		}
	}

	public abstract void onMySuccess(String data);

	public abstract void onMyFailure();
}
