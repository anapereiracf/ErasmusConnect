package com.example.erasmushelp.data;

import android.Manifest;

public interface Consts {

    //--------------------DATABASE--------------------
    String DATABASE_PATH = "https://erasmushelp11-default-rtdb.europe-west1.firebasedatabase.app/";
    String PATH_USERS = "Users";
    String PATH_PLACES = "Places";

    //--------------------EXTERNAL RESOURCES--------------------
    String ESN_LINK = "https://www.esn.org";
    String FACEBOOK_GROUP = "fb://group/1595131684091282";

    //--------------------HOUSING--------------------
    String ERASMUS_PLAY = "https://erasmusplay.com/en/venezia.html";
    String ERASMUSU = "https://erasmusu.com/en/erasmus-venice/student-housing?";
    String RENTOLA = "https://rentola.com/for-rent?location=Venice&rent_per=month&order=rent_asc";
    String HOUSING_ANYWHERE = "https://housinganywhere.com/s/Venice--Italy/student-accommodation";
    String UNIPLACES = "https://www.uniplaces.com/accommodation/venice";
    String CF_HOUSING = "https://www.unive.it/pag/20098/";

    //--------------PERMISSIONS------------------------
    String MAN_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    String MAN_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
}

