package com.kharazmiuniversity.khu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kharazmiuniversity.khu.data.KhuAPI;
import com.kharazmiuniversity.khu.data.LoginUserController;
import com.kharazmiuniversity.khu.models.ErrorResponse;
import com.kharazmiuniversity.khu.models.Token;
import com.kharazmiuniversity.khu.models.User;

public class LoginFragment extends Fragment
{

    private EditText username;
    private EditText password;
    private Button login;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews(view);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginUser();
            }
        });

    }

    private void loginUser()
    {
        LoginUserController loginUserController = new LoginUserController(loginUserCallback);
        User user = new User();

        user.setUsername(username.getText().toString());
        user.setPassword(password.getText().toString());

        loginUserController.start(user);
    }


    KhuAPI.LoginUserCallback loginUserCallback = new KhuAPI.LoginUserCallback() {
        @Override
        public void onResponse(boolean successful , ErrorResponse errorResponse, Token token )
        {
            if (successful)
            {
                Toast.makeText(getActivity(), token.getLoginMessage() ,Toast.LENGTH_SHORT).show();

                MyPreferenceManager.getInstance(getActivity()).putUsername(username.getText().toString());
                MyPreferenceManager.getInstance(getActivity()).putAccessToken(token.getJwt());
                MyPreferenceManager.getInstance(getActivity()).putProffessor(token.getProffessor());
                MyPreferenceManager.getInstance(getActivity()).putUser_name(token.getUser_name());

                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(

                        new Intent("login_finished")

                );


            }
            else {

                Toast.makeText(getActivity(),  errorResponse.getError() ,Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onFailure(String cause)
        {
            Toast.makeText(getActivity(),cause,Toast.LENGTH_SHORT).show();
        }
    };



   /* KhuAPI.LoginUserCallback loginUserCallback = new KhuAPI.LoginUserCallback() {
        @Override
        public void onResponse(boolean successful, String errorDescription, TokenResponse tokenResponse)
        {
            if (successful)
            {
                Toast.makeText(getActivity(),"login shod!!!!!!!" ,Toast.LENGTH_SHORT).show();


                MyPreferenceManager.getInstance(getActivity()).putUsername(username.getText().toString());
                MyPreferenceManager.getInstance(getActivity()).putAccessToken(tokenResponse.getAccessToken());

                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(

                        new Intent("login_finished")

                );

            }
        }

        @Override
        public void onFailure(String cause) {

        }
    };
*/

    private void findViews(View view)
    {
        username = (EditText) view.findViewById(R.id.username);
        password = (EditText) view.findViewById(R.id.password);
        login = ( Button) view.findViewById(R.id.login_botton);
    }

}
