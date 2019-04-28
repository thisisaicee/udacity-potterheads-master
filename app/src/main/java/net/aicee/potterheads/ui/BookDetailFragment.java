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

package net.aicee.potterheads.ui;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import net.aicee.potterheads.R;
import net.aicee.potterheads.adapters.BookBodyAdapter;
import net.aicee.potterheads.data.Article;
import net.aicee.potterheads.loaders.BookLoader;
import net.aicee.potterheads.utils.GlideApp;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.List;




public class BookDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Article> {
    public static String EXTRA_ARTICLE = "article";
    public static final String BUNDLE_RECYCLER_LAYOUT = "recycler_layout";

    View mParentLayout;

    @BindView(R.id.detail_image)
    ImageView imageView;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @Nullable
    @BindView(R.id.detail_collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.share_button)
    FloatingActionButton shareButton;
    @Nullable
    @BindView(R.id.article_header_group)
    ConstraintLayout articleHeader;
    @BindView(R.id.article_title_text)
    TextView titleTextView;
    @BindView(R.id.article_author_date_text)
    TextView authorDateTextView;

    private Article article;
    private Unbinder unbinder;
    private boolean isTablet;
    private boolean headerAnimating = false;
    private RecyclerView.LayoutManager recyclerviewLayoutManager;

    public static BookDetailFragment newInstance(int articleId) {
        Bundle args = new Bundle();
        args.putInt(EXTRA_ARTICLE, articleId);

        BookDetailFragment fragment = new BookDetailFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_article_detail, container, false);
        mParentLayout = getActivity().findViewById(android.R.id.content);

        // restore recycler view position
        if (savedInstanceState != null) {
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            recyclerviewLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
            recyclerviewLayoutManager.onRestoreInstanceState(savedRecyclerLayoutState);
            if (view.findViewById(R.id.detail_app_bar) != null)
                ((AppBarLayout) view.findViewById(R.id.detail_app_bar)).setExpanded(false);
        }

        if (getArguments() != null && getArguments().containsKey(EXTRA_ARTICLE)) {

            unbinder = ButterKnife.bind(this, view);

            isTablet = getResources().getBoolean(R.bool.isTablet);


            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setHasOptionsMenu(true);

            getActivity().getLoaderManager().initLoader(0, null, this);

        } else {
            Toast.makeText(getActivity().getApplicationContext(),R.string.no_active_internet,Toast.LENGTH_LONG).show();
            getActivity().finish();
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // save recycler view position
        if (recyclerviewLayoutManager != null)
            outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, recyclerviewLayoutManager.onSaveInstanceState());
    }

    private void setUpUi() {
        GlideApp
                .with(getActivity().getApplicationContext())
                .asBitmap()
                .load(article.getPhoto())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new BitmapImageViewTarget(imageView) {
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
                                    articleContainerColor = palette.getDarkMutedColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.colorPrimary));

                                int textViewColor;
                                if (palette.getLightMutedSwatch() != null)
                                    textViewColor = palette.getLightMutedSwatch().getRgb();
                                else
                                    textViewColor = palette.getLightVibrantColor(ContextCompat.getColor(getActivity().getApplicationContext(), android.R.color.white));

                                articleHeader.setBackgroundColor(articleContainerColor);
                                titleTextView.setTextColor(textViewColor);
                                authorDateTextView.setTextColor(textViewColor);
                            }
                        });
                    }
                });

        if (mCollapsingToolbarLayout != null)
            mCollapsingToolbarLayout.setTitle(article.getTitle());

        toolbar.setTitle(article.getTitle());


        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareArticle(article);
            }
        });
        slideUp(shareButton, 300);

        titleTextView.setText(article.getTitle());
        authorDateTextView.setText(getResources().getString(R.string.article_author_date, article.getFormattedDate(), article.getAuthor()));
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        List<String> article = this.article.getSplitBody();
        if (isTablet && articleHeader != null) {
            article.add(0, "<br/> <br/> ");
        }
        BookBodyAdapter adapter = new BookBodyAdapter(getActivity().getApplicationContext(), article);
        recyclerView.setAdapter(adapter);

        if (recyclerviewLayoutManager == null)
            recyclerviewLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());

        recyclerView.setLayoutManager(recyclerviewLayoutManager);
        recyclerView.setHasFixedSize(true);


        // If on tablet mode apply animations to show/hide the article header
        if (isTablet && articleHeader != null) {
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    if (dy > 20)
                        hideHeader();
                    else if (dy < -20)
                        showHeader();
                }
            });
        }

        slideUp(recyclerView, 500);

        if (isTablet)
            showHeader();
    }

    public void slideUp(View view, int timeMilli) {
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,
                0,
                view.getHeight(),
                0);
        animate.setDuration(timeMilli);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    public void hideHeader() {
        if (!headerAnimating && articleHeader.getVisibility() == View.VISIBLE) {
            articleHeader.animate()
                    .alpha(0f)
                    .translationY(-articleHeader.getHeight())
                    .withStartAction(new Runnable() {
                        @Override
                        public void run() {
                            headerAnimating = true;
                        }
                    })
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            articleHeader.setVisibility(View.GONE);
                            headerAnimating = false;
                        }
                    })
                    .start();
        }
    }

    public void showHeader() {
        if (!headerAnimating && articleHeader.getVisibility() == View.GONE) {
            articleHeader.animate()
                    .alpha(1f)
                    .translationY(0)
                    .withStartAction(new Runnable() {
                        @Override
                        public void run() {
                            articleHeader.setVisibility(View.VISIBLE);
                            headerAnimating = true;
                        }
                    })
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            headerAnimating = false;
                        }
                    })
                    .start();
        }
    }

    private void shareArticle(Article article) {
        Intent chooser = ShareCompat.IntentBuilder.from(getActivity())
                .setType("text/plain")
                .setText(String.format("%s - %s", article.getTitle(), article.getAuthor()))
                .getIntent();

        Intent intent = Intent.createChooser(chooser, getString(R.string.action_share));

        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (isTablet)
                getActivity().onBackPressed();
            else
                getActivity().supportFinishAfterTransition();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<Article> onCreateLoader(int id, @Nullable Bundle args) {
        return new BookLoader(getActivity().getApplicationContext(), getArguments().getInt(EXTRA_ARTICLE));
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Article> loader, Article data) {
        article = data;
        setUpUi();
        loader.reset();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Article> loader) {

    }
}