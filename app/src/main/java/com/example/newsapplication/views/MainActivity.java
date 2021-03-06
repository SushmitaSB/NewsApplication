package com.example.newsapplication.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newsapplication.R;
import com.example.newsapplication.model.SharedPreferenceConfig;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.login_button)
    LoginButton loginButton;

    @BindView(R.id.userId)
    TextView textView;

    @BindView(R.id.btId)
    Button button;

    @BindView(R.id.loginText)
    TextView loginText;

    @BindView(R.id.google_login_button)
    SignInButton signInButton;

    @BindView(R.id.signoutId)
    Button signOutButton;

    private CallbackManager mCallbackManager;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private AccessTokenTracker accessTokenTracker;
    private static final String TAG = "Facebookauthentication";
    public static final String MY_PREFS_NAME = "loggedIn";

    private GoogleSignInClient googleSignInClient;
    private String TAG1 = "MainActivity";
    private int RC_SIGN_IN =  1;
    private FirebaseAuth mAuth;
    private SharedPreferenceConfig sharedPreferenceConfig;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitializedVariables();
        UpdateActivityUi();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, HomePage.class);
                    startActivity(intent);
            }
        });

        //Faceboook login
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG,"onSuccess" + loginResult);

                    handleFacebookToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG,"onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG,"onError");
            }
        });

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        user = firebaseAuth.getCurrentUser();
                        sharedPreferenceConfig.LoginStatus(true);
                        textView.setVisibility(View.VISIBLE);
                        button.setVisibility(View.VISIBLE);
                        textView.setText(user.getDisplayName());
                        signInButton.setVisibility(View.GONE);
                        loginText.setVisibility(View.GONE);
                    } else {
                        signInButton.setVisibility(View.VISIBLE);
                        button.setVisibility(View.GONE);
                        textView.setVisibility(View.GONE);
                        loginText.setVisibility(View.VISIBLE);
                    }

            }
        };

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                    if (currentAccessToken == null) {
                        firebaseAuth.signOut();
                    }
            }
        };


        //For google signIn
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    signIn();
                    signOutButton.setVisibility(View.VISIBLE);
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    googleSignInClient.signOut();
                    signOutButton.setVisibility(View.GONE);
                    loginButton.setVisibility(View.VISIBLE);
                    signInButton.setVisibility(View.VISIBLE);
                    loginText.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                    button.setVisibility(View.GONE);
                    sharedPreferenceConfig.LoginStatus(false);
                    sharedPreferenceConfig.SignInStatus(false);


            }
        });


    }

    private void UpdateActivityUi() {
        if(sharedPreferenceConfig.read_login_status()){
            button.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
            loginText.setVisibility(View.GONE);
        }else {
            button.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
            loginText.setVisibility(View.VISIBLE);
        }

        if (sharedPreferenceConfig.read_signin_status()){
            loginButton.setVisibility(View.GONE);
            signOutButton.setVisibility(View.VISIBLE);
            button.setVisibility(View.VISIBLE);
        }else {
            textView.setVisibility(View.GONE);
            loginButton.setVisibility(View.VISIBLE);
            signOutButton.setVisibility(View.GONE);
            button.setVisibility(View.GONE);
        }
    }

    private void InitializedVariables() {
        ButterKnife.bind(this);
        firebaseAuth = FirebaseAuth.getInstance();
        FacebookSdk.sdkInitialize(getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("email", "public_profile");
        sharedPreferenceConfig = new SharedPreferenceConfig(MainActivity.this);
        mAuth = FirebaseAuth.getInstance();
    }

    private void signIn(){
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    //for handling signin result
    private void handleSignInResult( Task<GoogleSignInAccount> task){
        try {
            GoogleSignInAccount acc = task.getResult(ApiException.class);
            Toast.makeText(this, "Signed in Sucessfully", Toast.LENGTH_SHORT).show();
            FireBaseGoogleAuth(acc);
        }catch (Exception ex){
            Toast.makeText(this, "Signed in Failed", Toast.LENGTH_SHORT).show();
            FireBaseGoogleAuth(null);
        }

    }

    //for handling google signin
    private void FireBaseGoogleAuth( GoogleSignInAccount acc){
        AuthCredential authCredential = GoogleAuthProvider.getCredential(acc.getIdToken(), null);
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Sucessfull", Toast.LENGTH_SHORT).show();
                    FirebaseUser mUser = mAuth.getCurrentUser();
                    GoogleSignInAccount ac = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                    Toast.makeText(MainActivity.this, ac.getDisplayName(), Toast.LENGTH_SHORT).show();
                    //update ui
                    sharedPreferenceConfig.LoginStatus(true);
                    sharedPreferenceConfig.SignInStatus(true);
                    loginButton.setVisibility(View.GONE);
                    loginText.setVisibility(View.GONE);
                    button.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                    textView.setText(mUser.getDisplayName());
                }else{
                    Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    loginButton.setVisibility(View.VISIBLE);
                    loginText.setVisibility(View.VISIBLE);
                    button.setVisibility(View.GONE);
                    textView.setVisibility(View.GONE);
                }
            }
        });
    }

//For handling facebook token
    private void handleFacebookToken(AccessToken accessToken){
        Log.d(TAG,"handleFacebookToken" + accessToken);

        AuthCredential authCredential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.d(TAG, "sign in with credential: sucessful");
                }else{
                    Log.d(TAG, "sign in with credential: failure" , task.getException());
                    signInButton.setVisibility(View.VISIBLE);
                    Toast.makeText(MainActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null){
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }
}
