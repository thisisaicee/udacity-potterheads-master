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

package net.aicee.potterheads.holders;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import net.aicee.potterheads.R;
import net.aicee.potterheads.utils.ImageSizer;


public class BooksViewHolder extends RecyclerView.ViewHolder {
    public ImageSizer thumbnailView;
    public View view;
    public TextView titleView;
    public TextView authorView;
    public TextView dateView;
    public CardView itemCardContainer;

    public BooksViewHolder(View view) {
        super(view);
        this.view = view.findViewById(R.id.article);
        thumbnailView = view.findViewById(R.id.thumbnail);
        titleView = view.findViewById(R.id.article_title);
        authorView = view.findViewById(R.id.article_author);
        dateView = view.findViewById(R.id.article_date);
        itemCardContainer = view.findViewById(R.id.item_card);
    }

}
