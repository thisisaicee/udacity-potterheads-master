/*
    Copyright 2019 Ifeanyichukwu Onyeabor

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */


package net.aicee.potterheads.adapters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.List;

import net.aicee.potterheads.R;
import net.aicee.potterheads.data.Book;
import net.aicee.potterheads.holders.BooksViewHolder;
import net.aicee.potterheads.ui.BookDetailActivity;
import net.aicee.potterheads.ui.MainActivity;
import net.aicee.potterheads.utils.GlideApp;

public class BooksAdapter extends RecyclerView.Adapter<BooksViewHolder> {
    private static final String TAG = BooksAdapter.class.toString();

    private MainActivity listActivity;
    private List<Book> articleList;


    public BooksAdapter(MainActivity mainActivity, List<Book> articles) {
        this.listActivity = mainActivity;
        this.articleList = articles;
    }

    @Override
    public BooksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_article, parent, false);
        return new BooksViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final BooksViewHolder holder, final int position) {
        final Book article = articleList.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(listActivity, BookDetailActivity.class);

                intent.putExtra(MainActivity.INTENT_EXTRA_ARTICLE_ID, article.getId());

                if (!listActivity.getResources().getBoolean(R.bool.isTablet)) {
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(listActivity, holder.view, listActivity.getString(R.string.article_transition));
                    listActivity.startActivity(intent, options.toBundle());
                } else
                    listActivity.startActivity(intent);
            }
        });

        holder.titleView.setText(article.getTitle());
        holder.dateView.setText(article.getFormattedDate());
        holder.authorView.setText(article.getAuthor());

        GlideApp.with(listActivity.getApplicationContext())
                .asBitmap()
                .load(article.getThumb())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new BitmapImageViewTarget(holder.thumbnailView) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        super.onResourceReady(resource, transition);
                        Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(@NonNull Palette palette) {
                                int articleContainerColor;
                                if (palette.getDarkVibrantSwatch() != null)
                                    articleContainerColor = palette.getDarkVibrantSwatch().getRgb();
                                else
                                    articleContainerColor = palette.getDarkMutedColor(ContextCompat.getColor(listActivity.getApplicationContext(), R.color.colorPrimary));

                                int textViewColor;
                                if (palette.getLightMutedSwatch() != null)
                                    textViewColor = palette.getLightMutedSwatch().getRgb();
                                else
                                    textViewColor = palette.getLightVibrantColor(ContextCompat.getColor(listActivity.getApplicationContext(), android.R.color.white));

                                holder.view.setBackgroundColor(articleContainerColor);
                                holder.titleView.setTextColor(textViewColor);
                                holder.authorView.setTextColor(textViewColor);
                                holder.dateView.setTextColor(textViewColor);
                            }
                        });
                    }
                });

        holder.thumbnailView.setAspectRatio((float) article.getAspectRatio());
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

}
