package com.pwyql.pointmakemoney.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pwyql.pointmakemoney.R;
import com.pwyql.pointmakemoney.adapter.GoodsAdapter.Goods;
import com.pwyql.pointmakemoney.list.ViewHolder;
import com.pwyql.pointmakemoney.util.ImageLoaderOptions;

public class GoodsAdapter extends BaseAdapter {

    List<Goods> goodsList;
    Context context;
    OnGoodsOnClickListener onGoodsOnClickListener;

    public OnGoodsOnClickListener getOnGoodsOnClickListener() {
	return onGoodsOnClickListener;
    }

    public void setOnGoodsOnClickListener(OnGoodsOnClickListener onGoodsOnClickListener) {
	this.onGoodsOnClickListener = onGoodsOnClickListener;
    }

    public GoodsAdapter(Context context, List<Goods> goods) {
	this.goodsList = goods;
	this.context = context;
    }

    //    @Override
    //    public int getItemViewType(int position) {
    //	// TODO Auto-generated method stub
    //	if (0 == position) {
    //	    return 0;
    //	}
    //
    //	return 1;
    //    }

    //    
    //    @Override
    //    public int getViewTypeCount() {
    //	// TODO Auto-generated method stub
    //	return 2;
    //    }

    //    @Override
    public View getView(int arg0, View convertView, ViewGroup arg2) {
	// TODO Auto-generated method stub
	if (null == convertView) {
	    //	    if (getItemViewType(arg0) == 1) {
	    convertView = LayoutInflater.from(context).inflate(R.layout.item_goods, null);
	    //	    } else {
	    //		convertView = LayoutInflater.from(context).inflate(R.layout.layout_top_slide, null);
	    //		
	    //	    }
	}

	//	if (getItemViewType(arg0) == 1) {
	final Goods goods = goodsList.get(arg0);

	ImageView ivImg = ViewHolder.get(convertView, R.id.iv_img);
	TextView tvTitle = ViewHolder.get(convertView, R.id.tv_title);
	TextView tvPrice = ViewHolder.get(convertView, R.id.tv_price);
	com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(goods.imgUrl, ivImg, ImageLoaderOptions.normalOptions());
	tvTitle.setText(goods.title);
	tvPrice.setText(goods.price + "");

	convertView.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (onGoodsOnClickListener != null) {
		    onGoodsOnClickListener.onClick(goods);
		}
	    }
	});
	//	}

	return convertView;
    }

    public void addAll(List<Goods> goods) {
	goodsList.addAll(goods);
	notifyDataSetChanged();
    }

    public void clear() {
	goodsList.clear();
    }

    public List<Goods> getList() {
	return this.goodsList;
    }

    @Override
    public long getItemId(int arg0) {
	// TODO Auto-generated method stub
	return arg0;
    }

    @Override
    public Object getItem(int arg0) {
	// TODO Auto-generated method stub
	return this.goodsList.get(arg0);
    }

    @Override
    public int getCount() {
	// TODO Auto-generated method stub
	return goodsList.size();
    }

    public static class Goods implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3140033474966829282L;
	private String id;

	public String getId() {
	    return id;
	}

	public void setId(String id) {
	    this.id = id;
	}

	public String getTitle() {
	    return title;
	}

	public void setTitle(String title) {
	    this.title = title;
	}

	public String getImgUrl() {
	    return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
	    this.imgUrl = imgUrl;
	}

	public int getPrice() {
	    return price;
	}

	public void setPrice(int price) {
	    this.price = price;
	}

	public int getQuantity() {
	    return quantity;
	}

	public void setQuantity(int quantity) {
	    this.quantity = quantity;
	}

	private String title;
	private String imgUrl;
	private int price;
	private int quantity;

	public Goods(String id, String title, String imgUrl, int price, int quantity) {
	    this.id = id;
	    this.title = title;
	    this.imgUrl = imgUrl;
	    this.price = price;
	    this.quantity = quantity;
	}
    }

    public static interface OnGoodsOnClickListener {
	void onClick(Goods goods);
    }

}
