package com.pwyql.pointmakemoney.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pwyql.pointmakemoney.R;
import com.pwyql.pointmakemoney.list.ViewHolder;
import com.pwyql.pointmakemoney.util.ActivityUtil;

public class WordsAdapter extends BaseAdapter {
    List<Word> words;
    Context context;

    public WordsAdapter(List<Word> words, Context context) {
	// TODO Auto-generated constructor stub
	this.words = words;
	this.context = context;
    }

    @Override
    public int getCount() {
	// TODO Auto-generated method stub
	return words.size();
    }

    @Override
    public Object getItem(int arg0) {
	// TODO Auto-generated method stub
	return words.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
	// TODO Auto-generated method stub
	return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
	// TODO Auto-generated method stub
	if (null == convertView) {
	    convertView = LayoutInflater.from(context).inflate(R.layout.item_word, null);
	}
	Word word = words.get(position);

	TextView tvName = ViewHolder.get(convertView, R.id.tv_name);
	TextView tvWord = ViewHolder.get(convertView, R.id.tv_word);
	TextView tvTime = ViewHolder.get(convertView, R.id.tv_word_time);

	tvName.setText(word.name + ": ");
	tvWord.setText(word.content);
	tvTime.setText(ActivityUtil.convertTimeToString(word.time * 1000L));
	return convertView;
    }

    public void setList(List<Word> words) {
	this.words = words;
	notifyDataSetChanged();
    }

    public List<Word> getList() {
	return this.words;
    }

    public static class Word {
	public String id;
	public String name, content;
	public int time;

	public Word(String id, String name, String content, int time) {
	    this.id = id;
	    this.name = name;
	    this.content = content;
	    this.time = time;
	}
    }

}
