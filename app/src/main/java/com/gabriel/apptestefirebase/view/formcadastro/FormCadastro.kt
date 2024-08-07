package com.gabriel.apptestefirebase.view.formcadastro

import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.gabriel.apptestefirebase.R
import com.gabriel.apptestefirebase.databinding.ActivityFormCadastroBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class FormCadastro : AppCompatActivity() {

    private lateinit var binding: ActivityFormCadastroBinding
    private var auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormCadastroBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


//        capturando valores digitado
          binding.btCadastrar.setOnClickListener {view ->

              val email = binding.editEmail.text.toString()
              val senha = binding.editSenha.text.toString()

//              validações
              if ( email.isEmpty() || senha.isEmpty()){
                  val snackbar = Snackbar.make(view,"Preencha todos os campos!", Snackbar.LENGTH_SHORT)
                  snackbar.setBackgroundTint(Color.RED)
                  snackbar.show()
              }else{
                  // Resultado de cadastro
                    auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener {cadastro ->
                        if (cadastro.isSuccessful){
                            val snackbar = Snackbar.make(view,"Sucesso ao cadastra usuário!", Snackbar.LENGTH_SHORT)
                            snackbar.setBackgroundTint(Color.GREEN)
                            snackbar.show()
                            binding.editEmail.setText("")
                            binding.editSenha.setText("")
                        }
                        // Aqui vamos tratar as mensagens de erro
                    }.addOnFailureListener {exception ->

                        // mensagens de erros (excessoes)
                        val mensagemErro = when(exception){
                            is FirebaseAuthWeakPasswordException -> "Digite uma senha com no mínimo 6 caracteres!"
                            is FirebaseAuthInvalidCredentialsException -> "Digite um Email válido!"
                            is FirebaseAuthUserCollisionException -> "Esta conta já foi cadastrada!"
                            is FirebaseNetworkException -> "Sem conexão com a internet!"
                            else -> "Erro ao cadastrar usuário!"
                        }

                        val snackbar = Snackbar.make(view,mensagemErro, Snackbar.LENGTH_SHORT)
                        snackbar.setBackgroundTint(Color.RED)
                        snackbar.show()
                        binding.editEmail.setText("")
                        binding.editSenha.setText("")

                    }
              }

          }

    }
}