package br.com.empessoa8.carcare.email;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import br.com.empessoa8.carcare.R;

public class Act_Email extends AppCompatActivity {

    private EditText edt_email, edt_subject, edt_message;
    private Button btn_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act__email);
        edt_email = (EditText) findViewById(R.id.edit_email);
        edt_subject = (EditText) findViewById(R.id.edit_assunto);
        edt_subject.requestFocus();

        edt_message = (EditText) findViewById(R.id.edit_message);
        btn_send = (Button) findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String to = edt_email.getText().toString();
                String subject = edt_subject.getText().toString();
                String message = edt_message.getText().toString();

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, message);

                intent.setType("message/rfc822");
                startActivity(Intent.createChooser(intent, "Select Email app"));
            }
        });
    }
}
