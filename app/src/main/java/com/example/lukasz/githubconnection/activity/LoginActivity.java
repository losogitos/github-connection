package com.example.lukasz.githubconnection.activity;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lukasz.githubconnection.R;
import com.example.lukasz.githubconnection.connection.Connection;
import com.example.lukasz.githubconnection.model.User;
import com.example.lukasz.githubconnection.utils.Session;

import okhttp3.Credentials;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getName();
    private TextView userName;
    private EditText password;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userName = (TextView) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.sign_in_button);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userNameText = userName.getText().toString();
                String passwordText = password.getText().toString();
                login(userNameText, passwordText);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Session.getInstance().logout();
    }

    private void login(final String userName, final String password) {

        Connection.gitHubService().login(Credentials.basic(userName, password)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<User>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted");
                Session.getInstance().setCredentials(userName, password);
                displayRepos(userName);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "Error: " + e);
                Snackbar.make(LoginActivity.this.userName, LoginActivity.this.getString(R.string.error_login_failed),
                        Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onNext(User user) {
                Log.d(TAG, "User: " + user);
            }
        });

    }

    private void displayRepos(String userName) {
        Intent intent = new Intent(LoginActivity.this, ReposActivity.class);
        intent.putExtra(ReposActivity.USER, userName);
        startActivity(intent);
    }
}
