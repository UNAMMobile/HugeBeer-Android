package com.chelathon.unammobile.hugebeer;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;


public class PreLoginActivity extends AppCompatActivity {

    public static String BASE_URL="https://hugebeer.firebaseio.com";
    private Firebase mFirebaseRef;
    private CallbackManager callbackManager;
    private LoginButton loginButton;

    public static AccessToken accessToken;

    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_pre_login);
        getSupportActionBar().hide();
        context=this;
        //Firebase
        Firebase.setAndroidContext(context);
        mFirebaseRef = new Firebase(BASE_URL);
        //Facebook

        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");
        // Other app specific specialization

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                accessToken=loginResult.getAccessToken();
                onFacebookAccessTokenChange(accessToken);
                Toast.makeText(context,accessToken.getUserId(),Toast.LENGTH_LONG).show();
                // App code
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void onFacebookAccessTokenChange(AccessToken token) {
        if (token != null) {
            mFirebaseRef.authWithOAuthToken("facebook", token.getToken(), new Firebase.AuthResultHandler() {
                @Override
                public void onAuthenticated(AuthData authData) {
                    // The Facebook user is now authenticated with your Firebase app
                    Intent intent=new Intent(context,MainActivity.class);
                    startActivity(intent);
                }
                @Override
                public void onAuthenticationError(FirebaseError firebaseError) {
                    Toast.makeText(context,firebaseError.toString(),Toast.LENGTH_SHORT).show();
                    // there was an error
                }
            });
        } else {
            Toast.makeText(context,"Token null",Toast.LENGTH_SHORT).show();
            /* Logged out of Facebook so do a logout from the Firebase app */
            mFirebaseRef.unauth();
        }


    }


}
