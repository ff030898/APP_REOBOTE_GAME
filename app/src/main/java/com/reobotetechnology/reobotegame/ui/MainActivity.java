package com.reobotetechnology.reobotegame.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.ui.ConfiguracoesActivity;
import com.reobotetechnology.reobotegame.ui.NotificacoesActivity;
import com.reobotetechnology.reobotegame.ui.PerfilActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    private long backPressedTime;
    private Toast backToast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_biblia, R.id.navigation_feed, R.id.navigation_ranking, R.id.navigation_mensagens)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


    }

    private void alert(String s) {
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }



    @Override
    public void onBackPressed() {

        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            backToast = Toast.makeText(getApplicationContext(), "Pressione novamente para sair", Toast.LENGTH_SHORT);
            backToast.show();
        }

        backPressedTime = System.currentTimeMillis();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);

        return true;
    }



    //ao clicar nos itens do menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_sair) {
            finish();
        }else if (item.getItemId() == R.id.menu_perfil){
            startActivity(new Intent(getApplicationContext(), PerfilActivity.class));
        }
        else if(item.getItemId() == R.id.menu_configuracoes){
            startActivity(new Intent(getApplicationContext(), ConfiguracoesActivity.class));
        }else if(item.getItemId() == R.id.menu_notificacoes){
            startActivity(new Intent(getApplicationContext(), NotificacoesActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

}
