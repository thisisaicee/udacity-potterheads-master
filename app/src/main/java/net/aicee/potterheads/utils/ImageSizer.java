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

package net.aicee.potterheads.utils;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

public class ImageSizer extends AppCompatImageView {
    private float aRatio = 1.5f;

    public ImageSizer(Context context) {
        super(context);
    }

    public ImageSizer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ImageSizer(Context context, AttributeSet attributeSet, int style) {
        super(context, attributeSet, style);
    }

    public void setAspectRatio(float aspectRatio) {
        aRatio = aspectRatio;
        requestLayout();
    }

    @Override
    protected void onMeasure(int width, int height) {
        super.onMeasure(width, height);
        int measuredWidth = getMeasuredWidth();
        setMeasuredDimension(measuredWidth, (int) (measuredWidth / aRatio));
    }
}
