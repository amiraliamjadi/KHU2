package com.kharazmiuniversity.khu;

import android.content.Context;
import android.content.SharedPreferences;

// using singleton pattern
public class MyPreferenceManager
{

    // yek nemune misazim ta az an baraye dastresi estefade konim
    // bayad static bashad chon yek method static be an mikhahad dastresi peyda konad
    private static MyPreferenceManager instance = null;


    // be yek file be ma dastresi mide va mitunim negash konim va bekhanim esh
    private SharedPreferences sharedPreferences = null;
    // baraye inke betunim toosh benevisim esh az editor estefade mikonim
    private SharedPreferences.Editor editor = null;

    // be har hal rahi baraye dastresi bayad bashad pas in method e public ra tarif mikonim
    // parameter e context migooyad ke alan tooye che activity E hastim
    public static MyPreferenceManager getInstance(Context context)
    {
        // age null bood meghdar avaliye midim bad return mikonim
        // age nabud ke hamun meghdar ra return mikonad
        if (instance == null)
        {
            // chon constrauctor private e dakhel e in class bedun e moshkel new kardim
            // va motmaen Em ke kharej e in class in amal soorat nemigirad
            // eyne bala context bara ertebat ba activity ast
            instance = new MyPreferenceManager(context);
        }

        return instance;
    }

    // chon mikhahim yek noskhe faghat bashad va kasi az birun be an dastresi nadarad
    // constructor ra private tarif mikonim
    // context bara moshakhas kardan inke dar kodam activity hastim ,ast
    private MyPreferenceManager(Context context)
    {
        // dar an activity marboote ke hastim esm e an maslan file chiye ke get esh konim dakhel
        // variable berizim yani parameter e aval naam e delbekhah e mas
        // parameter e dovom marboot be an ast ke che kasi be an dastresi dashte bashad
        // yani private e dakheli bashad ya az birun masalan google map ham be an dastresi dashte bashad
        // inja masalan halat e private ra entekhab kardim ke faghat khodemun be an dastresi darim
        sharedPreferences = context.getSharedPreferences("my_prefences",context.MODE_PRIVATE);

        // baraye farakhani method edit kardan
        editor = sharedPreferences.edit();
    }



    public String getUsername()
    {
        return sharedPreferences.getString("username", null);
    }

    public void putUsername(String username)
    {
        editor.putString("username", username);
        editor.apply();
    }



    public String getUser_name()
    {
        return sharedPreferences.getString("user_name", null);
    }

    public void putUser_name(String user_name)
    {
        editor.putString("user_name", user_name);
        editor.apply();
    }


    public String getAccessToken()
    {
        return sharedPreferences.getString("access_token", null);
    }

    public void putAccessToken(String accessToken)
    {
        editor.putString("access_token", accessToken);
        editor.apply();
    }

    public boolean getProffessor()
    {
        return sharedPreferences.getBoolean("proffessor",false);
    }

    public void putProffessor(boolean proffessor)
    {
        editor.putBoolean("proffessor", proffessor);
        editor.apply();
    }



    public void clearEverythings()
    {
        editor.clear();
        editor.apply();
    }



}
