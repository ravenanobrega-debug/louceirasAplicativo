package br.com.example.louceirasdotalhado;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;

public class TelaInicialActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1001;

    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton btnGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial); // seu layout com SignInButton

        btnGoogle = findViewById(R.id.btnGoogle);

        // Configura opções de login
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Cria cliente Google Sign-In
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Se já está logado, vai direto
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            Toast.makeText(this, "Bem-vindo de volta: " + account.getEmail(), Toast.LENGTH_SHORT).show();
            // Se já estiver logado, redireciona para a InicioActivity
            Intent intent = new Intent(TelaInicialActivity.this, InicioActivity.class);
            startActivity(intent);
            finish(); // Fecha a TelaInicialActivity para evitar que o usuário volte
        }

        // Clique no botão de login
        btnGoogle.setOnClickListener(v -> signIn());
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            if (task.isSuccessful()) {
                GoogleSignInAccount account = task.getResult();
                Toast.makeText(this, "Login bem-sucedido: " + account.getEmail(), Toast.LENGTH_SHORT).show();

                // Após o login ser bem-sucedido, redireciona para a InicioActivity
                Intent intent = new Intent(TelaInicialActivity.this, InicioActivity.class);
                startActivity(intent);
                finish(); // Fecha a TelaInicialActivity para evitar que o usuário volte
            } else {
                Toast.makeText(this, "Falha no login", Toast.LENGTH_SHORT).show();
                Log.e("LOGIN_GOOGLE", "Erro: ", task.getException());
            }
        }
    }
}
