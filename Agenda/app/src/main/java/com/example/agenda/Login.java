package com.example.agenda;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static android.app.PendingIntent.getActivity;

public class Login extends AppCompatActivity {
    CallbackManager callbackManager;
    boolean islogged = false;
    ImageButton liLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        callbackManager = CallbackManager.Factory.create();

        //--------------------------------------FACEBOOK LOGIN --------------------------------------

        LoginButton loginButton = (LoginButton) findViewById(R.id.btnFacebook);
        loginButton.setReadPermissions("email");
        // If using in a fragment


        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {


                alerta("Você logou com sucesso!");
            }

            @Override
            public void onCancel() {
                alerta("Cancelando...");
            }

            @Override
            public void onError(FacebookException exception) {
                alerta(exception.toString());
                //GetKeyHash();
            }


        });

        //--------------------------------------FACEBOOK LOGIN --------------------------------------

        //--------------------------------------LINKEDIN LOGIN --------------------------------------
        final Activity thisActivity = this;
        liLoginButton = (ImageButton) findViewById(R.id.btnLinkedin);
        liLoginButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view){
                    if (!islogged) {
                        islogged = true;
                        alerta("Você logou com sucesso!");
                        changeBtnLinkedin(liLoginButton, islogged);
                        LISessionManager.getInstance(getApplicationContext()).init(thisActivity, buildScope(), new AuthListener() {
                            @Override
                            public void onAuthSuccess() {
                                //alerta("Você logou com sucesso!");
                            }

                            @Override
                            public void onAuthError(LIAuthError error) {
                                alerta(error.toString());
                            }
                        }, true);
                    } else{

                        LISessionManager.getInstance(getApplicationContext()).clearSession();
                        islogged = false;
                        changeBtnLinkedin(liLoginButton,islogged);
                        alerta("Desconectado!");
                    }

                }
        });


        //--------------------------------------LINKEDIN LOGIN --------------------------------------
    }

    void changeBtnLinkedin(ImageButton btn, Boolean status){
        if(status){
            btn.setImageResource(R.drawable.linkedinbtn2); // muda imagem pro logout
        }else{
            btn.setImageResource(R.drawable.linkedinbtn); // muda imagem pro login
        }
    }

    void alerta(String s){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(s);
        builder.create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void GetKeyHash()
    {

        PackageInfo info;
        try
        {
            info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures)
            {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String keyhash = new String(Base64.encode(md.digest(), 0));
                // String something = new String(Base64.encodeBytes(md.digest()));
                alerta(keyhash);
            }
        }
        catch (PackageManager.NameNotFoundException e1)
        {
            alerta(e1.toString());
        }
        catch (NoSuchAlgorithmException e)
        {
            alerta(e.toString());
        }
        catch (Exception e)
        {
            alerta(e.toString());
        }

    }

    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.W_SHARE);
    }

}
