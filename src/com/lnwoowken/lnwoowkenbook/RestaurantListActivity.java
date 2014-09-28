package com.lnwoowken.lnwoowkenbook;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cncom.app.base.database.BjnoteContent;
import com.cncom.app.base.ui.PullToRefreshListPageActivity;
import com.cncom.app.base.util.ShopInfoObject;
import com.lnwoowken.lnwoowkenbook.ServiceObject.ServiceResultObject;
import com.lnwoowken.lnwoowkenbook.view.ProgressDialog;
import com.shwy.bestjoy.utils.AdapterWrapper;
import com.shwy.bestjoy.utils.ComConnectivityManager;
import com.shwy.bestjoy.utils.InfoInterface;
import com.shwy.bestjoy.utils.NetworkUtils;
import com.shwy.bestjoy.utils.PageInfo;
import com.shwy.bestjoy.utils.Query;

/**
 * 店家信息的界面
 * 
 * @author chs
 * 
 */
public class RestaurantListActivity extends PullToRefreshListPageActivity {
	private static final String TAG = "RestaurantListActivity";
	private AnimationDrawable draw;
    private	LinearLayout progress;
	private Dialog dialog;
	private ShopListAdapter mShopListAdapter;
	private List <ShopInfoObject> mShopsList;
	
	private boolean mFirstStart = true;
	
	private EditText mSearchInput;
	private Button mSearchBtn;
	
	private Query mQuery;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isFinishing()) {
			return;
		}
		findViewById(R.id.button_sort).setOnClickListener(this);
		
		mSearchInput = (EditText) findViewById(R.id.editText_search);
		
		mSearchInput.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() == 0) {
					mQuery.qServiceUrl = ServiceObject.getAllShopInfoUrl();
					forceRefresh();
				}
				
			}
			
		});
		mSearchBtn = (Button) findViewById(R.id.button_search);
		mSearchBtn.setOnClickListener(this);
	}
	
	@Override
	protected boolean isNeedForceRefreshOnResume() {
		if (mFirstStart) {
			mFirstStart = false;
			return true;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.button_sort:
			TabHostActivity.startActivity(mContext);
			break;
		case R.id.button_search:
			String filter = mSearchInput.getText().toString().trim();
			if (!TextUtils.isEmpty(filter)) {
				
				try {
					JSONObject queryJsonObject = new JSONObject();
					queryJsonObject.put("condition", filter);
					mQuery.qServiceUrl = ServiceObject.getAllShopInfoUrl("para", queryJsonObject.toString());
					forceRefresh();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			break;
		}
	}

	private void dismissProgressDialog(){
		if(dialog != null && dialog.isShowing()) {
			dialog.dismiss();
			dialog = null;
		}
	}
	
	private void showProgressDialog(){
		if(dialog != null && dialog.isShowing()) return; 
		dialog = new ProgressDialog(RestaurantListActivity.this,
				R.style.ProgressDialog);
		dialog.show();
		progress = (LinearLayout) dialog.findViewById(R.id.imageView_progress);
	
		progress.setBackgroundResource(R.anim.animition_progress); 
        draw = (AnimationDrawable)progress.getBackground(); 
        draw.start();
        dialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface arg0) {
				draw.stop();
			}
		});
//		progress.setIndeterminate(true);
		//setProgressBarIndeterminateVisibility(true); 
		dialog.setCanceledOnTouchOutside(false);
		// TableListDialog dialog = new
		// TableListDialog(BookTableActivity.this);
		// dialog.show();
	}
	


	public class ShopListAdapter extends CursorAdapter {
		public ShopListAdapter(Context context, Cursor c, boolean autoRequery) {
			super(context, c, autoRequery);
		}
		

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			View convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_shop_item, parent, false);
			ViewHolder holder = new ViewHolder();
			holder._name = (TextView) convertView.findViewById(R.id.textView_storename);
			holder._tablenum = (TextView) convertView.findViewById(R.id.textView_tablenumber);
			holder._favorate = (TextView) convertView.findViewById(R.id.textView_favorate);
			holder._price = (TextView) convertView.findViewById(R.id.textView_price);
			holder._detail = (TextView) convertView.findViewById(R.id.textView_position);
			holder._distance = (TextView) convertView.findViewById(R.id.textView_distance);
			holder._youhui = (ImageView) convertView.findViewById(R.id.imageView_hui);
			holder._tuangou = (ImageView) convertView.findViewById(R.id.imageView_tuan);
			holder._diancan = (ImageView) convertView.findViewById(R.id.imageView_dian);
			holder._maidian = (ImageView) convertView.findViewById(R.id.imageView_mai);
			convertView.setTag(holder);
			return convertView;
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			ViewHolder holder = (ViewHolder) view.getTag();
			holder._shopInfoObject = ShopInfoObject.getShopInfoObject(cursor);
			holder._name.setText(holder._shopInfoObject.getShopName());
			holder._tablenum.setText(holder._shopInfoObject.getShopDeskCount());
			holder._price.setText(holder._shopInfoObject.getShopRenJun());
			holder._detail.setText(holder._shopInfoObject.getShopBrief());
			String n = holder._shopInfoObject.getShopName();
			String a = holder._shopInfoObject.getShopTuanGou();
			if(!TextUtils.isEmpty(holder._shopInfoObject.getShopYouHui())) holder._youhui.setVisibility(View.VISIBLE); else holder._youhui.setVisibility(View.GONE);
			if(!TextUtils.isEmpty(holder._shopInfoObject.getShopTuanGou())) holder._tuangou.setVisibility(View.VISIBLE); else holder._tuangou.setVisibility(View.GONE);
			if(!TextUtils.isEmpty(holder._shopInfoObject.getShopDianCan())) holder._diancan.setVisibility(View.VISIBLE); else holder._diancan.setVisibility(View.GONE);
			if(!TextUtils.isEmpty(holder._shopInfoObject.getShopMaiDian())) holder._maidian.setVisibility(View.VISIBLE); else holder._maidian.setVisibility(View.GONE);
		}

	}
	
	private class ViewHolder {
		private TextView _name, _tablenum, _favorate, _price, _detail, _distance;
		private ImageView _youhui, _tuangou, _diancan, _maidian;
		private ShopInfoObject _shopInfoObject;
	}
	
	@Override
	public void onItemClick(AdapterView<?> listView, View view, int pos, long arg3) {
		Bundle bundle = new Bundle();
		ViewHolder viewHodler = (ViewHolder) view.getTag();
		bundle.putString("shop_id", viewHodler._shopInfoObject.getShopID());
		RestuarantInfoActivity.startIntent(mContext, bundle);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected boolean checkIntent(Intent intent) {
		return true;
	}

	@Override
	protected AdapterWrapper<? extends BaseAdapter> getAdapterWrapper() {
		mShopListAdapter = new ShopListAdapter(mContext, null, false);
		return new AdapterWrapper<CursorAdapter>(mShopListAdapter);
	}

	@Override
	protected Cursor loadLocal(ContentResolver contentResolver) {
		return contentResolver.query(BjnoteContent.Shops.CONTENT_URI, ShopInfoObject.SHOP_PROJECTION, null, null, null);
	}

	@Override
	protected int savedIntoDatabase(ContentResolver cr, List<? extends InfoInterface> infoObjects) {
		int insertOrUpdateCount = 0;
		if (infoObjects != null) {
			for(InfoInterface object:infoObjects) {
				if (object.saveInDatebase(cr, null)) {
					insertOrUpdateCount++;
				}
			}
		}
		return insertOrUpdateCount;
	}

	@Override
	protected List<? extends InfoInterface> getServiceInfoList(InputStream is, PageInfo pageInfo) {
		ServiceResultObject serviceResultObject = ServiceResultObject.parseShops(NetworkUtils.getContentFromInput(is));
		List<ShopInfoObject> shopsList = new ArrayList<ShopInfoObject>();
		if (serviceResultObject.isOpSuccessfully()) {
			for(int i = 0; i < serviceResultObject.mJsonArrayData.length(); i++) {
				try {
					ShopInfoObject shopInfoObject = ShopInfoObject.getShopInfoObjectFromJsonObject(serviceResultObject.mJsonArrayData.getJSONObject(i));
					shopsList.add(shopInfoObject);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
		}
		return shopsList;
	}

	@Override
	protected Query getQuery() {
		mQuery = new Query();
		mQuery.qServiceUrl = ServiceObject.getAllShopInfoUrl();
		return mQuery;
	}

	@Override
	protected void onRefreshStart() {
	}
	
	protected void onRefreshLoadEnd() {
		if (ComConnectivityManager.getInstance().isConnected()) {
			BjnoteContent.delete(getContentResolver(), BjnoteContent.Shops.CONTENT_URI, null, null);
		}
	}

	@Override
	protected void onRefreshEnd() {
		
	}

	@Override
	protected int getContentLayout() {
		return R.layout.activity_restaurant_list;
	}
}
