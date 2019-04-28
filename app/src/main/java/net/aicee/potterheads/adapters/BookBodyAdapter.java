/*
  *  Copyright 2019 Ifeanyichukwu Onyeabor

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

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import net.aicee.potterheads.R;


public class BookBodyAdapter extends RecyclerView.Adapter<BookBodyAdapter.TextHolder> {
    private Context context;
    private List<String> list;

    public BookBodyAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public BookBodyAdapter.TextHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = R.layout.list_item_content_text;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layout, parent, false);

        return new TextHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookBodyAdapter.TextHolder holder, int position) {
        String paragraph = list.get(position);

        holder.bind(paragraph);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class TextHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_paragraph)
        TextView paragraph;

        TextHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(String paragraph) {
            //format paragraph
            this.paragraph.setText(Html.fromHtml(Pattern.compile("").matcher(paragraph).replaceAll("")));
            this.paragraph.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }
}
