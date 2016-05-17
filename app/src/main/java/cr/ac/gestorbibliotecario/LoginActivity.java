package cr.ac.gestorbibliotecario;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import cr.ac.util.JsonParser;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    private static final String TAG_SUCCESS = "success";
    private static String wsLogin = "http://smarttourcr.esy.es/WSBiblioteca/Login.php";
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private Button btnLogIn;
    private View mProgressView;
    private View mLoginFormView;
    private JsonParser jParserLogin = new JsonParser();
    private String TAG_NOMBRE = "nombre";
    private String TAG_APELLIDO = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        btnLogIn = (Button) findViewById(R.id.email_sign_in_button);
        mPasswordView = (EditText) findViewById(R.id.password);


        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Autenticacion autenticacion = new Autenticacion();
                autenticacion.execute("", "", "");
            }
        });
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    public class Autenticacion extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> parametros = new ArrayList<NameValuePair>();
            parametros.add(new BasicNameValuePair("nombre", mEmailView.getText().toString()));
            //mEmailView.getText().toString()
            parametros.add(new BasicNameValuePair("password", mPasswordView.getText().toString()));
            // mPasswordView.getText().toString()
            JSONObject json = jParserLogin.makeHttpRequest(wsLogin, "POST", parametros);
            Log.d("Create Response", json.toString());
            try {
                int success = json.getInt("success");

                if (success == 1) {
                    Intent i = new Intent(getApplicationContext(), BuscarLibros.class);
                    finish();
                    startActivity(i);
                } else {
                    //Toast.makeText(getApplicationContext(), "Datos erroneos", Toast.LENGTH_LONG).show();
                }
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }
    }

}

