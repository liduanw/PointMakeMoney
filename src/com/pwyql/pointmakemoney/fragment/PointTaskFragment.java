package com.pwyql.pointmakemoney.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.aow.android.DAOW;
import cn.aow.android.DCloseListener;
import cn.guomob.android.intwal.OpenIntegralWall;
import cn.waps.AppConnect;
import cn.waps.AppListener;

import com.ZMAD.conne.SetScoreWall;
import com.ZMAD.score.GetScoreWall;
import com.bb.dd.BeiduoPlatform;
import com.dc.wall.DianCai;
import com.dlnetwork.DevInit;
import com.eadver.offer.scorewall.ScoreWallSDK;
import com.pwyql.pointmakemoney.MyApplication;
import com.pwyql.pointmakemoney.R;
import com.pwyql.pointmakemoney.ad.AdsManager;
import com.pwyql.pointmakemoney.list.ViewHolder;
import com.pwyql.pointmakemoney.service.AppConstants;
import com.pwyql.pointmakemoney.service.AppService;
import com.pwyql.pointmakemoney.service.AppService.AdRewardParams;
import com.pwyql.pointmakemoney.service.impl.AppServiceImpl;
import com.pwyql.pointmakemoney.ui.AdsActivity;
import com.pwyql.pointmakemoney.util.ActivityUtil;
import com.pwyql.pointmakemoney.util.FileUtils;
import com.pwyql.pointmakemoney.util.PreferencesUtil;
import com.pwyql.pointmakemoney.util.StringUtils;

public class PointTaskFragment extends BaseFragment {
    private static final String TAG = "TestFragment";

    GridView mGvGrids;
    PointTasksAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	Log.d(TAG, "TestFragment-----onCreate");
	// Bundle args = getArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	Log.d(TAG, "TestFragment-----onCreateView");
	View view = inflater.inflate(R.layout.fragment_pointtasklist, container, false);
	init(view);
	return view;
    }

    private void init(View v) {

	mGvGrids = (GridView) v.findViewById(R.id.gv_tasklist); // 菜单格子

	mAdapter = new PointTasksAdapter(this.getActivity(), new ArrayList<Item>());
	mGvGrids.setAdapter(mAdapter);
	initGrids(); // 初始化 菜单格子
	initAds(v); // 创建1个百度广告条(banner)

    }

    /**
     * 
     * @param v
     */
    private void initAds(View v) {
	// TODO Auto-generated method stub

	// 添加一个banner广告
	// 帮助清理百度广告app下载目录
	new Thread() {
	    public void run() {
		String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/bddownload";
		FileUtils.deleteDir(dir);
	    };
	}.start();

	RelativeLayout layout = (RelativeLayout) v;
	RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
	params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

	com.baidu.mobads.AdView adView = AdsManager.getInstance(getActivity()).createAdViewBaidu(layout, params);
	adView.setListener(new AdsManager.BaiduAdAdapter() {
	    @Override
	    public void onAdClick(JSONObject arg0) {
		// TODO Auto-generated method stub

		//adView.setVisibility(View.GONE);
		// 请求奖励积分
		final ProgressDialog progressDialog = new ProgressDialog(MyApplication.getInstance().getTopActivity());
		progressDialog.setMessage("点击广告条,等待系统积分奖励...");
		progressDialog.show();

		AdRewardParams postParams = new AdRewardParams();
		postParams.model = ActivityUtil.getModel();
		postParams.time = System.currentTimeMillis() + "";
		postParams.sign = StringUtils.md5(postParams.model + postParams.time + AppConstants.SIGN_KEY);
		AppServiceImpl.getInstance().adReward(postParams, new AppService.OnAdRewardResponseListener() {

		    @Override
		    public void onSuccess(int reward, int lastRewardTime, int nextRewardDelay) {
			// TODO Auto-generated method stub
			AlertDialog.Builder builder = new AlertDialog.Builder(MyApplication.getInstance().getTopActivity());
			builder.setMessage("恭喜,获得奖励积分" + reward);
			builder.setPositiveButton("确定", null);
			builder.create().show();

			progressDialog.dismiss();

			// 记录下获得奖励的时间
			System.out.println((int) (System.currentTimeMillis() / 1000L) + nextRewardDelay + "");
			//showToast(ActivityUtil.getDateStr((int) (System.currentTimeMillis() / 1000L) + nextRewardDelay, "yyyy-MM-dd HH:mm"));
			PreferencesUtil.save(getActivity(), MyApplication.getInstance().getCurrentLoginedUser().getUserId() + "_next_reward_time", (int) (System.currentTimeMillis() / 1000L) + nextRewardDelay + "");
		    }

		    @Override
		    public void onFailure(String errorMsg, int status) {
			// TODO Auto-generated method stub
			Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
			AlertDialog.Builder builder = new AlertDialog.Builder(MyApplication.getInstance().getTopActivity());
			builder.setMessage(errorMsg);
			builder.setPositiveButton("确定", null);
			builder.create().show();
			progressDialog.dismiss();
		    }
		});
	    }
	});
    }

    private void initGrids() {
	Item item = null;
	item = new Item(R.drawable.task, "百度积分通道");
	mAdapter.addItem(item);
	item = new Item(R.drawable.task, "有米积分通道");
	mAdapter.addItem(item);
	item = new Item(R.drawable.task, "多盟积分通道");
	mAdapter.addItem(item);
	item = new Item(R.drawable.task, "万普积分通道");
	mAdapter.addItem(item);
	item = new Item(R.drawable.task, "点乐积分通道");
	mAdapter.addItem(item);
	item = new Item(R.drawable.task, "点财积分通道");
	mAdapter.addItem(item);
	item = new Item(R.drawable.task, "贝多积分通道");
	mAdapter.addItem(item);
	item = new Item(R.drawable.task, "果盟积分通道");
	mAdapter.addItem(item);
	item = new Item(R.drawable.task, "快速积分通道");
	mAdapter.addItem(item);

	mAdapter.setOnGridItemClickListener(new OnGridItemClickListener() {

	    @Override
	    public void onClick(int index, Item item) {
		// TODO Auto-generated method stub
		if ("易积分积分通道".equals(item.title)) {
		    showOffersWallYijifen();
		} else if ("有米积分通道".equals(item.title)) {
		    showOffersWallYoumi();
		} else if ("多盟积分通道".equals(item.title)) {
		    showOffersWallDomob();
		} else if ("万普积分通道".equals(item.title)) {
		    showOffersWallWaps();
		} else if ("点乐积分通道".equals(item.title)) {
		    showOffersWallDianjoy();
		} else if ("点财积分通道".equals(item.title)) {
		    showOffersWallDiancai();
		} else if ("贝多积分通道".equals(item.title)) {
		    showOffersWallBeiduo();
		} else if ("果盟积分通道".equals(item.title)) {
		    showOffersWallGuomob();
		} else if ("快速积分通道".equals(item.title)) {
		    Intent intent = new Intent(getActivity(), AdsActivity.class);
		    startActivity(intent);
		} else if ("百度积分通道".equals(item.title)) {
		    showOffersWallBaidu();
		}
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

    public static class PointTasksAdapter extends BaseAdapter {

	List<Item> items;
	Context context;
	OnGridItemClickListener onGridItemClickListener;

	public PointTasksAdapter(Context context, List<Item> items) {
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
		convertView = LayoutInflater.from(context).inflate(R.layout.item_task, null);
	    }

	    final Item item = items.get(pos);
	    // convertView.setBackgroundColor(item.color);

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

    }

    public static interface OnGridItemClickListener {
	void onClick(int index, Item item);
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

    // 百度
    private void showOffersWallBaidu() {
	//	showToast("该通道暂未开放");
	com.baidu.mobads.appoffers.OffersManager.setAppSid(getActivity(), "ef8bb5a9");
	com.baidu.mobads.appoffers.OffersManager.setUserName(getActivity(), MyApplication.getInstance().getCurrentLoginedUser().getUserId());
	com.baidu.mobads.appoffers.OffersManager.showOffers(getActivity());

    }

    // 有米
    private void showOffersWallYoumi() {
	net.youmi.android.offers.OffersManager.getInstance(getActivity()).showOffersWall();
    }

    // 多盟
    private void showOffersWallDomob() {
	// 关闭墙 监听
	DAOW.getInstance(getActivity()).setCloseListener(new DCloseListener() {

	    @Override
	    public void onClose() {
		//Toast.makeText(MainActivity.this, "积分墙已退出!!", Toast.LENGTH_SHORT).show();
	    }
	});
	DAOW.getInstance(this.getActivity()).show(this.getActivity());
    }

    // 万普
    private void showOffersWallWaps() {

	// 关闭积分墙事件 监听接口
	AppConnect.getInstance(getActivity()).setOffersCloseListener(new AppListener() {
	    @Override
	    public void onOffersClose() {
		// TODO Auto-generated method stub
		super.onOffersClose();
	    }

	});
	String key = MyApplication.getInstance().getCurrentLoginedUser().getUserId(); // 可以为 当前登录用户的ID
	AppConnect.getInstance(getActivity()).showOffers(getActivity(), key);
    }

    // 果盟
    private void showOffersWallGuomob() {
	String userId = MyApplication.getInstance().getCurrentLoginedUser().getUserId();
	OpenIntegralWall.getInstance().show(userId);
    }

    // 指盟
    private void showOffersMobsmar() {
	SetScoreWall sw = new SetScoreWall();
	boolean s = sw.ScoreWallManager(getActivity());
	if (s) {//如果为true调用积分墙，false则不调用
	    GetScoreWall its = new GetScoreWall();//调用积分墙
	    its.get(getActivity());
	}
    }

    // 易积分
    private void showOffersWallYijifen() {
	ScoreWallSDK.getInstance(getActivity()).showScoreWall();
    }

    // 点乐
    private void showOffersWallDianjoy() {
	DevInit.showOffers(getActivity());
    }

    // 点财
    private void showOffersWallDiancai() {
	DianCai.showOfferWall();
    }

    // 贝多
    private void showOffersWallBeiduo() {
	BeiduoPlatform.showOfferWall(getActivity());
    }

}
