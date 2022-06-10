package com.msimplelogic.helper;



import java.util.ArrayList;
import com.msimplelogic.activities.R;
import com.msimplelogic.model.Theme;

/**
 * Created by Pankaj on 12-11-2017.
 */

public class ThemeUtil {
    public static final int THEME_BLUE = 0;
    public static final int THEME_DARK = 1;

    public static int getThemeId(int theme){
        int themeId=0;
        switch (theme){

            case THEME_BLUE  :
                themeId = R.style.AppTheme;
                break;
            case THEME_DARK  :
                //themeId = R.style.AppTheme_DARK;
                break;

            default:
                break;
        }
        return themeId;
    }

    public static ArrayList<Theme> getThemeList(){
        ArrayList<Theme> themeArrayList = new ArrayList<>();
        themeArrayList.add(new Theme(0,R.color.primaryColorBlue, R.color.primaryDarkColorBlue, R.color.secondaryColorBlue));
        themeArrayList.add(new Theme(1,R.color.black, R.color.back, R.color.primaryColorDarkTheme));
        return themeArrayList;
    }
}
