package com.pwyql.pointmakemoney.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.pwyql.pointmakemoney.R;
import com.pwyql.pointmakemoney.adapter.WordsAdapter;
import com.pwyql.pointmakemoney.adapter.WordsAdapter.Word;
import com.pwyql.pointmakemoney.service.AppConstants;
import com.pwyql.pointmakemoney.service.AppService.OnPublishWordResponseListener;
import com.pwyql.pointmakemoney.service.AppService.OnSelectWordsResponseListener;
import com.pwyql.pointmakemoney.service.AppService.PublishWordParams;
import com.pwyql.pointmakemoney.service.AppService.SelectWordsParams;
import com.pwyql.pointmakemoney.service.impl.AppServiceImpl;
import com.pwyql.pointmakemoney.util.ActivityUtil;
import com.pwyql.pointmakemoney.util.StringUtils;
import com.pwyql.pointmakemoney.xlist.XListView.IXListViewListener;

public class RankingFragment extends BaseFragment implements OnClickListener {
    View mContainerView;

    ViewPager mViewPager;
    View mCursorLine;

    TextView[] tvTabs;
    TextView tvTab1, tvTab2, tvTab3;

    com.pwyql.pointmakemoney.xlist.XListView mListView;
    WordsAdapter mAdapter;
    int mPageIndex;

    EditText mEtWordContent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	Log.d(getClass().getSimpleName(), "->onCreateView");
	mContainerView = inflater.inflate(R.layout.fragment_ranking, container, false);
	init();
	return mContainerView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onActivityCreated(savedInstanceState);
    }

    private int mCurrentPageIndex;
    private int lineWidth;

    private void init() {
	tvTab1 = (TextView) mContainerView.findViewById(R.id.tv_tab_1);
	tvTab2 = (TextView) mContainerView.findViewById(R.id.tv_tab_2);
	tvTab3 = (TextView) mContainerView.findViewById(R.id.tv_tab_3);

	tvTabs = new TextView[] { tvTab1, tvTab2, tvTab3 };

	tvTab1.setOnClickListener(this);
	tvTab2.setOnClickListener(this);
	tvTab3.setOnClickListener(this);

	// 光标线
	mCursorLine = mContainerView.findViewById(R.id.iv_nearby_bottom_line);

	ViewGroup.LayoutParams params = mCursorLine.getLayoutParams();
	params.width = ActivityUtil.getScreenSize()[0] / 3;
	lineWidth = params.width;
	mCursorLine.setLayoutParams(params); // 设置光标线宽度

	mViewPager = (ViewPager) mContainerView.findViewById(R.id.viewpager);
	Fragment rankingPointsFragment = new RankingPointsFragment();
	Fragment rankingToadyFragment = new RankingTodayFragment();
	Fragment rankingWithdrawlogsFragment = new RankingWithdrawlogsFragment();

	final List<Fragment> fragments = new ArrayList<Fragment>();

	fragments.add(rankingToadyFragment);
	fragments.add(rankingPointsFragment);
	fragments.add(rankingWithdrawlogsFragment);

	mViewPager.setAdapter(new RankingFragmentPagerAdapter(getActivity().getSupportFragmentManager(), fragments));
	mViewPager.setCurrentItem(0); // 默认页面为第1个
	tvTabs[0].setTextColor(getResources().getColor(R.color.cursor_line));

	//mViewPager.requestDisallowInterceptTouchEvent(true);
//	mViewPager.setOnTouchListener(new OnTouchListener() {
//
//	    @Override
//	    public boolean onTouch(View arg0, MotionEvent arg1) {
//		// TODO Auto-generated method stub
//		return true;
//	    }
//	});

	mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

	    @Override
	    public void onPageSelected(int arg0) {

		TranslateAnimation anim = null;
		switch (arg0) {
		case 0:
		    anim = new TranslateAnimation(mCurrentPageIndex * lineWidth, 0, 0, 0);
		    break;
		case 1:
		    anim = new TranslateAnimation(mCurrentPageIndex * lineWidth, lineWidth, 0, 0);
		    break;
		case 2:
		    anim = new TranslateAnimation(mCurrentPageIndex * lineWidth, 2 * lineWidth, 0, 0);
		    break;
		}
		tvTabs[mCurrentPageIndex].setTextColor(getResources().getColor(R.color.base_color_text_black));
		//		anim.setInterpolator(new CycleInterpolator(0.2f));
		anim.setDuration(300);
		anim.setFillAfter(true); // 停留在最后一帧
		mCursorLine.startAnimation(anim);
		mCurrentPageIndex = arg0;

		tvTabs[mCurrentPageIndex].setTextColor(getResources().getColor(R.color.cursor_line));
	    }

	    @Override
	    public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	    }

	    @Override
	    public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	    }
	});

	/**
	 * 
	 */
	mListView = (com.pwyql.pointmakemoney.xlist.XListView) mContainerView.findViewById(R.id.listview);
	final List<Word> words = new ArrayList<WordsAdapter.Word>();
	//	words.add(new Word("1", "大木头", "积分墙应用应该是双赢的吧", 0));
	//	words.add(new Word("2", "大木头", "喜欢的人是不是同样喜欢", 0));
	//	words.add(new Word("3", "大木头", "我爱的人是不是对我有一样的感受", 0));
	mAdapter = new WordsAdapter(words, getActivity());
	mListView.setAdapter(mAdapter);

	mListView.setPullLoadEnable(true);
	mListView.setPullRefreshEnable(true);
	mListView.setOnItemClickListener(new OnItemClickListener() {

	    @Override
	    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
		Word word = mAdapter.getList().get(position - 1);
		showToast(word.content);
	    }

	});

	// 刷新
	mListView.setXListViewListener(new IXListViewListener() {

	    @Override
	    public void onRefresh() {
		// TODO Auto-generated method stub
		mPageIndex = 1;
		loadWords();
	    }

	    @Override
	    public void onLoadMore() {
		// TODO Auto-generated method stub
		loadWords();
	    }
	});

	mEtWordContent = (EditText) mContainerView.findViewById(R.id.et_words);

	// 发布留言
	mContainerView.findViewById(R.id.btn_word_submit).setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		// TODO Auto-generated method stub
		String content = mEtWordContent.getText().toString().trim();
		if (content.length() == 0) {
		    showToast("请输入内容");
		    mEtWordContent.requestFocus();
		    return;
		}
		publishWord(content);
	    }
	});

	mPageIndex = 1;
	// 加载 words
	loadWords();

    }

    /**
     * 发留言
     * @param trim
     */
    private void publishWord(String content) {
	final ProgressDialog dialog = new ProgressDialog(getActivity());
	dialog.setMessage("提交中...");
	dialog.show();
	dialog.setCancelable(false);
	dialog.setCanceledOnTouchOutside(false);

	// TODO Auto-generated method stub
	PublishWordParams postParams = new PublishWordParams();
	postParams.content = content;
	postParams.sign = StringUtils.md5(postParams.content + AppConstants.SIGN_KEY);
	AppServiceImpl.getInstance().publishWord(postParams, new OnPublishWordResponseListener() {

	    @Override
	    public void onSuccess(Word word) {
		// TODO Auto-generated method stub
		showToast("发布成功! 花费5积分");
		mEtWordContent.setText("");
		dialog.dismiss();

		mAdapter.getList().add(0, word);
		mAdapter.notifyDataSetChanged();
	    }

	    @Override
	    public void onFailure(String errorMsg, int status) {
		// TODO Auto-generated method stub
		showToast(errorMsg);
		dialog.dismiss();
	    }
	});
    }

    private boolean isRefreshing;

    public void loadWords() {
	if (isRefreshing) {
	    return;
	}
	isRefreshing = true;
	//	final ProgressDialog progressDialog = new ProgressDialog(getActivity());
	//	progressDialog.setMessage("加载中...");
	//	progressDialog.show();

	SelectWordsParams postParams = new SelectWordsParams();
	postParams.page = mPageIndex;
	postParams.pageSize = 20;
	AppServiceImpl.getInstance().selectWords(postParams, new OnSelectWordsResponseListener() {

	    @Override
	    public void onSuccess(List<Word> words) {
		// TODO Auto-generated method stub
		//		progressDialog.dismiss();
		if (words.size() != 0) {
		    if (mPageIndex == 1) {
			mAdapter.getList().clear();
		    }
		    mAdapter.getList().addAll(words);
		    mPageIndex++;
		} else {
		    showToast(mAdapter.getList().size() != 0 ? "暂无更多" : "暂无数据");
		}

		mListView.stopRefresh();
		mListView.stopLoadMore();

		isRefreshing = false;
	    }

	    @Override
	    public void onFailure(String errorMsg, int status) {
		// TODO Auto-generated method stub
		showToast(errorMsg);
		//		progressDialog.dismiss();
		isRefreshing = false;
	    }
	});
    }

    @Override
    public void onHiddenChanged(boolean hidden) {

	super.onHiddenChanged(hidden);
    }

    @Override
    public void onClick(View v) {
	// TODO Auto-generated method stub
	int index = 0;
	switch (v.getId()) {
	case R.id.tv_tab_1:
	    index = 0;
	    break;
	case R.id.tv_tab_2:
	    index = 1;
	    break;
	case R.id.tv_tab_3:
	    index = 2;
	    break;
	}
	mViewPager.setCurrentItem(index);
	mCurrentPageIndex = index;
    }

    /**
     * 可保存 fragment 的状态
     * @author sparklee
     *
     */
    public class RankingFragmentPagerAdapter extends FragmentPagerAdapter {
	List<Fragment> fragments;

	FragmentManager fragmentManager;

	public RankingFragmentPagerAdapter(FragmentManager fm) {
	    super(fm);
	    fragmentManager = fm;
	    // TODO Auto-generated constructor stub
	}

	public RankingFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
	    super(fm);
	    fragmentManager = fm;
	    this.fragments = fragments;
	}

	@Override
	public int getCount() {
	    // TODO Auto-generated method stub
	    return fragments.size();
	}

	@Override
	public int getItemPosition(Object object) {
	    // TODO Auto-generated method stub
	    return super.getItemPosition(object);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
	    // TODO Auto-generated method stub
	    Fragment fragment = fragments.get(position);
	    fragmentManager.beginTransaction().hide(fragment).commit();
	}

	@Override
	public Fragment getItem(int position) {
	    // TODO Auto-generated method stub 
	    // 
	    Fragment fragment = null;
	    fragment = fragments.get(position);
	    Bundle bundle = new Bundle();
	    bundle.putString("id", "" + position);
	    fragment.setArguments(bundle);
	    return fragment;

	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
	    // TODO Auto-generated method stub
	    Fragment fragment = (Fragment) super.instantiateItem(container, position);
	    fragmentManager.beginTransaction().show(fragment).commit();
	    return fragment;
	}
    }

}
