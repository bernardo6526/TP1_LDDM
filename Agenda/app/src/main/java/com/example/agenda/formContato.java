package com.example.agenda;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URLEncoder;

public class formContato extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button = findViewById(R.id.btnSalvar);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText txtNome = (EditText) findViewById(R.id.txtNome);
                EditText txtTelefone = (EditText) findViewById(R.id.txtTelefone);
                EditText txtEmail = (EditText) findViewById(R.id.txtEmail);
                EditText txtEndereco = (EditText) findViewById(R.id.txtEndereco);
                EditText txtWebsite = (EditText) findViewById(R.id.txtWebsite);

                String nome = txtNome.getText().toString();
                String telefone = txtTelefone.getText().toString();
                String email = txtEmail.getText().toString();
                String endereco = txtEndereco.getText().toString();
                String website = txtWebsite.getText().toString();
                Pessoa contato = new Pessoa(nome,telefone,email,endereco,website);


                whatsapp(nome,telefone);
                sendEmail(nome,email);
                insertContact(nome,telefone,email,endereco,website); // será executado primeiro

            }
        });
    }

    public void insertContact(String nome, String telefone, String email, String endereco, String website) {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        intent.putExtra(ContactsContract.Intents.Insert.NAME, nome);
        intent.putExtra(ContactsContract.Intents.Insert.PHONE, telefone);
        intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email);
        intent.putExtra(ContactsContract.Intents.Insert.POSTAL, endereco);
        intent.putExtra("finishActivityOnSaveCompleted", true);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 1);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                alerta("Contato salvo com sucesso!");
            }
        }
    }

    public void whatsapp(String nome, String telefone){
        PackageManager packageManager = this.getPackageManager();
        Intent enviarZap = new Intent(Intent.ACTION_VIEW);
        String phone = "5531" + telefone;

        try {
            String url = "https://api.whatsapp.com/send?phone=%22"+ phone +"&text=" + URLEncoder.encode("Cadastro realizado, " + nome, "UTF-8");
            enviarZap.setPackage("com.whatsapp");
            enviarZap.setData(Uri.parse(url));

            if (enviarZap.resolveActivity(packageManager) != null) {
                this.startActivity(enviarZap);
                alerta("Mensagem pronta para ser enviada!");
            } else {
                Toast.makeText(this.getApplicationContext(),"Mensagem nao enviada",Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void sendEmail(String nome,String email) {
            Log.i("Send email", "");

            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.setType("text/plain");


            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Cadastro");
            emailIntent.putExtra(Intent.EXTRA_TEXT   , "Cadastro realizado, "+nome);


            try {
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                finish();
                alerta("Email pronto para ser enviado!");
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(formContato.this,
                        "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    void alerta(String s){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(s);
        builder.create().show();
    }
}
