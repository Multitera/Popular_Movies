package com.example.andy.popularmovies;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andy.popularmovies.model.ClipInfo;
import com.example.andy.popularmovies.model.MovieExtra;
import com.example.andy.popularmovies.model.Review;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 1/28/2016.
 */
public class MovieDetailsAdapter extends RecyclerView.Adapter<MovieDetailsAdapter.ViewHolder> {
    private AdapterInterface adapterInterface;
    private int lengthClipInfo;
    private List<MovieExtra> cards;

    private static final int CLIP = 0;
    private static final int REVIEW = 1;

    public interface AdapterInterface {
        public void ClipViewCardClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class ClipViewHolder extends ViewHolder implements View.OnClickListener {
        ImageView youTubeIcon;
        TextView videoName;

        public ClipViewHolder(View itemView) {
            super(itemView);
            this.youTubeIcon = (ImageView) itemView.findViewById(R.id.youtube_icon);
            this.videoName = (TextView) itemView.findViewById(R.id.video_name);
        }

        @Override
        public void onClick(View v) {
            adapterInterface.ClipViewCardClick(getLayoutPosition());
        }
    }

    public class ReviewViewHolder extends ViewHolder {
        TextView author;
        TextView content;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            this.author = (TextView) itemView.findViewById(R.id.author);
            this.content = (TextView) itemView.findViewById(R.id.content);
        }
    }

    public MovieDetailsAdapter(AdapterInterface adapterInterface) {
        this.adapterInterface = adapterInterface;
        cards = new ArrayList<MovieExtra>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view;
        if (viewType == CLIP) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_video, viewGroup, false);
            return new ClipViewHolder(view);
        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_review, viewGroup, false);
            return new ReviewViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position < lengthClipInfo) {
            ClipInfo info = (ClipInfo) cards.get(position);
            ClipViewHolder clipHolder = (ClipViewHolder) holder;
            clipHolder.videoName.setText(info.getName());
        } else {
            Review review = (Review) cards.get(position);
            ReviewViewHolder reviewHolder = (ReviewViewHolder) holder;
            reviewHolder.author.setText(review.getAuthor());
            reviewHolder.content.setText(review.getContent());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position < lengthClipInfo)
            return CLIP;
        else
            return REVIEW;
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public void addClipInfoList(List<ClipInfo> clipInfoList) {
        lengthClipInfo = clipInfoList.size();
        cards.addAll(clipInfoList);
    }

    public void addReviews(List<Review> reviews) {
        cards.addAll(reviews);
    }
}
