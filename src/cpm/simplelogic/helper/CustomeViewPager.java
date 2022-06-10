package cpm.simplelogic.helper;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by jaspreetkaur on 11/02/18.
 */

public class CustomeViewPager extends ViewPager {

    public CustomeViewPager(Context context) {
        super(context);
    }

    public CustomeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void requestChildFocus(View child, View focused) {
        //Do nothing, disables automatic focus behaviour

    }

}