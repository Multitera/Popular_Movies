package com.example.andy.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andy.popularmovies.model.ClipInfo;
import com.example.andy.popularmovies.model.Movie;
import com.example.andy.popularmovies.model.MovieExtra;
import com.example.andy.popularmovies.model.Review;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 1/28/2016.
 */
public class MovieDetailsAdapter extends RecyclerView.Adapter<MovieDetailsAdapter.ViewHolder> {
    private AdapterInterface adapterInterface;
    private Context context;
    private int lengthClipInfo;
    private boolean isFavorite;
    private Movie movie;
    private List<MovieExtra> cards;

    private static final int DETAILS = 0;
    private static final int CLIP_HEADER = 1;
    private static final int CLIP = 2;
    private static final int REVIEW_HEADER = 3;
    private static final int REVIEW = 4;

    public interface AdapterInterface {
        public void ClipViewCardClick(int position);

        public void FavoriteButtonPressed(boolean isFavorite);

        public void StartTransition();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class DetailsViewHolder extends ViewHolder {
        TextView movieTitle;
        TextView rating;
        TextView dateReleased;
        TextView overview;
        ImageView poster;
        Button favorite;

        public DetailsViewHolder(View itemView) {
            super(itemView);
            movieTitle = (TextView) itemView.findViewById(R.id.movieTitleText);
            rating = (TextView) itemView.findViewById(R.id.rating);
            dateReleased = (TextView) itemView.findViewById(R.id.dateReleasedText);
            overview = (TextView) itemView.findViewById(R.id.overviewText);
            poster = (ImageView) itemView.findViewById(R.id.posterImage);
            poster.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    poster.getViewTreeObserver().removeOnPreDrawListener(this);
                    adapterInterface.StartTransition();
                    return true;
                }
            });
            favorite = (Button) itemView.findViewById(R.id.favoriteButton);
            favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapterInterface.FavoriteButtonPressed(isFavorite);
                }
            });
        }
    }

    public class HeaderViewHolder extends ViewHolder {
        TextView listHeader;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            this.listHeader = (TextView) itemView.findViewById(R.id.list_header);
        }
    }

    public class ClipViewHolder extends ViewHolder {
        ImageView youTubeIcon;
        TextView videoName;

        public ClipViewHolder(View itemView) {
            super(itemView);
            this.youTubeIcon = (ImageView) itemView.findViewById(R.id.youtube_icon);
            this.videoName = (TextView) itemView.findViewById(R.id.video_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapterInterface.ClipViewCardClick(getLayoutPosition() - CLIP);
                }
            });
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

    public MovieDetailsAdapter(Context context, AdapterInterface adapterInterface, Movie movie) {
        this.context = context;
        this.adapterInterface = adapterInterface;
        this.movie = movie;
        cards = new ArrayList<MovieExtra>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view;
        switch (viewType) {
            case DETAILS:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_movie_details, viewGroup, false);
                return new DetailsViewHolder(view);
            case CLIP_HEADER:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_header, viewGroup, false);
                return new HeaderViewHolder(view);
            case CLIP:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_video, viewGroup, false);
                return new ClipViewHolder(view);
            case REVIEW_HEADER:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_header, viewGroup, false);
                return new HeaderViewHolder(view);
            case REVIEW:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_review, viewGroup, false);
                return new ReviewViewHolder(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case DETAILS:
                DetailsViewHolder detailsViewHolder = (DetailsViewHolder) holder;
                detailsViewHolder.movieTitle.setText(movie.getTitle());
                detailsViewHolder.rating.setText(movie.getVote_average());
                detailsViewHolder.dateReleased.setText(movie.getRelease_date());
                detailsViewHolder.overview.setText(movie.getOverview());
                Picasso.with(context).load(context.getString(R.string.poster_url) + movie.getPoster_path()).into(detailsViewHolder.poster);
                if (isFavorite)
                    detailsViewHolder.favorite.setText(R.string.unfavorite_button);
                else
                    detailsViewHolder.favorite.setText(R.string.favorite_button);
                break;
            case CLIP_HEADER:
                HeaderViewHolder clipHeader = (HeaderViewHolder) holder;
                clipHeader.listHeader.setText(R.string.video_header);
                break;
            case CLIP:
                ClipInfo info = (ClipInfo) cards.get(position - CLIP);
                ClipViewHolder clipHolder = (ClipViewHolder) holder;
                clipHolder.videoName.setText(info.getName());
                break;
            case REVIEW_HEADER:
                HeaderViewHolder reviewHeader = (HeaderViewHolder) holder;
                reviewHeader.listHeader.setText(R.string.review_header);
                break;
            case REVIEW:
                Review review = (Review) cards.get(position - REVIEW_HEADER);
                ReviewViewHolder reviewHolder = (ReviewViewHolder) holder;
                reviewHolder.author.setText(review.getAuthor());
                reviewHolder.content.setText(review.getContent());
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == DETAILS)
            return DETAILS;
        else if (position == CLIP_HEADER)
            return CLIP_HEADER;
        else if (position < lengthClipInfo + CLIP)
            return CLIP;
        else if (position == lengthClipInfo + CLIP)
            return REVIEW_HEADER;
        else
            return REVIEW;

    }

    @Override
    public int getItemCount() {
        if (cards.isEmpty())
            return 1;
        else
            return cards.size() + 3;
    }

    public void addClipInfoList(List<ClipInfo> clipInfoList) {
        lengthClipInfo = clipInfoList.size();
        cards.addAll(clipInfoList);
    }

    public void addReviews(List<Review> reviews) {
        cards.addAll(reviews);
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }
}
