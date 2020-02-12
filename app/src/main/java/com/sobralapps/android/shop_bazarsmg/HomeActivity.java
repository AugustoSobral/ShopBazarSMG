package com.sobralapps.android.shop_bazarsmg;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import com.sobralapps.android.shop_bazarsmg.Data.Objects.AvisoPerguntaRespostaForFirebase;
import com.sobralapps.android.shop_bazarsmg.FragmentsNavMenu.AboutUsFragment;
import com.sobralapps.android.shop_bazarsmg.FragmentsNavMenu.AvisosFragment;
import com.sobralapps.android.shop_bazarsmg.FragmentsNavMenu.CategoriasFragment;
import com.sobralapps.android.shop_bazarsmg.FragmentsNavMenu.FavoritosFragment;
import com.sobralapps.android.shop_bazarsmg.FragmentsNavMenu.HomeFragment;
import com.sobralapps.android.shop_bazarsmg.FragmentsNavMenu.MyAccountFragment;
import com.sobralapps.android.shop_bazarsmg.FragmentsNavMenu.ProfileFragment;
import com.sobralapps.android.shop_bazarsmg.FragmentsNavMenu.PromocoesFragment;
import com.sobralapps.android.shop_bazarsmg.Sell.SellOptionsActivity;
import com.sobralapps.android.shop_bazarsmg.Start_Login_Register.LoginActivity;
import com.sobralapps.android.shop_bazarsmg.Start_Login_Register.LoginRegisterForAnonimousUser;
import com.sobralapps.android.shop_bazarsmg.Start_Login_Register.StartActivity;
import com.facebook.login.LoginManager;

import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    boolean doubleBackToExitPressedOnce = false;
    private Toast toastExit;

    private TextView text_view_name_header;
    private TextView text_view_email_header;
    private ImageView image_profile_header;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference users_tokens_ref = db.collection("users_tokens");
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private TextView avisosNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        if (mUser != null && !mUser.isAnonymous())
            updateUserToken();

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);

        text_view_name_header = headerView.findViewById(R.id.text_view_header_name);
        text_view_email_header = headerView.findViewById(R.id.text_view_header_email);
        image_profile_header = headerView.findViewById(R.id.profile_pic_nav_header);
        Button btn_login_header = headerView.findViewById(R.id.btn_login_header);
        LinearLayout linearLayout_header = headerView.findViewById(R.id.linear_layout_header);

        //Se o usuários está logado:
        if (mUser != null && !mUser.isAnonymous()) {
            text_view_name_header.setText(mUser.getDisplayName());
            if (mUser.getEmail() == null || mUser.getEmail().isEmpty()) {

                //Removendo o código do país para mostrar na tela.
                StringBuilder phoneNoCountryCode = new StringBuilder(mUser.getPhoneNumber());
                phoneNoCountryCode.deleteCharAt(0);
                phoneNoCountryCode.deleteCharAt(0);
                phoneNoCountryCode.deleteCharAt(0);
                text_view_email_header.setText(phoneNoCountryCode.toString());
            } else {
                text_view_email_header.setText(mUser.getEmail());
            }
            Picasso.get().load(mUser.getPhotoUrl()).placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                    .into(image_profile_header);
        }

        //Se o usuários não está logado:
        if (mUser == null || mUser.isAnonymous()) {
            linearLayout_header.setPadding(0, (int) getResources().getDimension(R.dimen.linear_layout_header_top_padding), 0, 0);
            text_view_name_header.setText("Bem vindo!");
            text_view_email_header.setText("Entre na sua conta para anunciar, ver favoritos, etc.");

            image_profile_header.setVisibility(View.GONE);
            btn_login_header.setVisibility(View.VISIBLE);
            navigationView.getMenu().findItem(R.id.nav_logout).setTitle("Entrar ou criar conta").setIcon(R.drawable.ic_add_account_grey_24dp);
        }

        //Quando a main activity for criada este código abrirá o fragment inicial, usa-se a verificação savedInstance para evitar criação de mais de um fragment em mudanças de config.
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, new HomeFragment()).commit();
            getSupportActionBar().setTitle("Livre Mercado Diamantina");
            navigationView.setCheckedItem(R.id.nav_home); //Muda o cursor no NavView para a opção inicial.
        }

        //Verificando se o aplicativo foi iniciado por uma notificação de aviso
        if(getIntent().getExtras()!=null){
            String notification_origem_code = getIntent().getExtras().getString("NOTIFICATION");
            if(notification_origem_code.equals("NOTIFICATION")) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, new AvisosFragment()).commit();
                getSupportActionBar().setTitle("Avisos");
                navigationView.setCheckedItem(R.id.nav_avisos);
            }
        }

        btn_login_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        image_profile_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, new ProfileFragment()).commit();
                drawer.closeDrawers();
                navigationView.setCheckedItem(R.id.nav_my_account);
            }
        });

        MenuItem menuItem = navigationView.getMenu().findItem(R.id.nav_avisos);
        avisosNumber = (TextView) menuItem.getActionView();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {

            case (R.id.nav_home):
                getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, new HomeFragment()).commit();
                getSupportActionBar().setTitle("Livre Mercado Diamantina");
                break;

            case (R.id.nav_fav):
                getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, new FavoritosFragment()).commit();
                getSupportActionBar().setTitle("Favoritos");
                break;

            case (R.id.nav_avisos):
                getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, new AvisosFragment()).commit();
                getSupportActionBar().setTitle("Avisos");
                break;

            case (R.id.nav_sell):
                if (mUser != null && !mUser.isAnonymous())
                    startActivity(new Intent(HomeActivity.this, SellOptionsActivity.class));
                if (mUser == null || mUser.isAnonymous())
                    startActivity(new Intent(HomeActivity.this, LoginRegisterForAnonimousUser.class));
                break;

            case (R.id.nav_my_account):
                getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, new MyAccountFragment()).commit();
                getSupportActionBar().setTitle("Minha conta");
                break;

            case (R.id.nav_categorias):
                getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, new CategoriasFragment()).commit();
                getSupportActionBar().setTitle("Categorias");
                break;

            case (R.id.nav_about_us):
                getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, new AboutUsFragment()).commit();
                getSupportActionBar().setTitle("Sobre");
                break;

            case (R.id.nav_promocoes):
                getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, new PromocoesFragment()).commit();
                getSupportActionBar().setTitle("Promoções");
                break;

            case (R.id.nav_logout):
                if (mUser != null && !mUser.isAnonymous()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Fazer Logout.").setMessage("Tem certeza que deseja sair da sua conta?")
                            .setNegativeButton("Cancelar", dialogClickListener)
                            .setPositiveButton("Sim", dialogClickListener).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Entrar - Registrar").setMessage("Ir para a tela de login -  registro?")
                            .setNegativeButton("Cancelar", dialogClickListener)
                            .setPositiveButton("Sim", dialogClickListener).show();
                }
                break;
        }

        drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Configura um ClickListener pra uma dialog box de confirmação para logout.
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked
                    LoginManager.getInstance().logOut(); //Logout no facebook
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(HomeActivity.this, StartActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };

    //Duplo click para fechar o app.
    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragments_container);

        if (getSupportFragmentManager().getBackStackEntryCount() == 0 || currentFragment instanceof HomeFragment || currentFragment instanceof CategoriasFragment || currentFragment instanceof FavoritosFragment || currentFragment instanceof MyAccountFragment) {
            drawer = findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
            //Duplo click em voltar para fechar o aplicativo, qnd o drawer não está a mostra, senão ele só fecha o drawer.
            else {
                if (doubleBackToExitPressedOnce) {
                    finishAffinity();
                    return;
                }
                this.doubleBackToExitPressedOnce = true;
                toastExit = Toast.makeText(this, "Pressione novamente para sair", Toast.LENGTH_LONG);
                toastExit.show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            }
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    public void updateUserToken(){

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                Map<String, Object> user = new HashMap<>();
                user.put("uid", mUser.getUid());
                user.put("token", instanceIdResult.getToken());
                users_tokens_ref.document(mUser.getUid()).set(user);
            }
        });
    }

    //Criando o número de notificações para o navigation menu
    @Override
    protected void onStart() {
        super.onStart();
        db.collection("users_avisos").document(mUser.getUid()).collection("avisos")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }
                        int avisosUnreadNumber = 0;
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            AvisoPerguntaRespostaForFirebase aviso = documentSnapshot.toObject(AvisoPerguntaRespostaForFirebase.class);
                            if(!aviso.isAvisoRead()){
                                avisosUnreadNumber++;
                            }
                        }
                        initializeCountDrawer(avisosUnreadNumber);
                    }
                });
    }

    private void initializeCountDrawer(int number){
        //Gravity property aligns the text
        avisosNumber.setGravity(Gravity.CENTER);
        avisosNumber.setTypeface(null, Typeface.BOLD);
        avisosNumber.setTextColor(getResources().getColor(R.color.colorAccent));
        avisosNumber.setBackground(getResources().getDrawable(R.drawable.bg_menu_drawer_text_count));
        avisosNumber.setTextColor(getResources().getColor(R.color.colorWhite));
        avisosNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
        FrameLayout.LayoutParams llp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        llp.setMargins(0, getResources().getDimensionPixelSize(R.dimen.size12dp), 0, getResources().getDimensionPixelSize(R.dimen.size12dp));
        llp.height = (getResources().getDimensionPixelSize(R.dimen.size20dp));
        llp.width = (getResources().getDimensionPixelSize(R.dimen.size20dp));
        avisosNumber.setLayoutParams(llp);


        TextView drawerCount = findViewById(R.id.tv_nav_drawer_count);

        if(number>0) {
            avisosNumber.setVisibility(View.VISIBLE);
            avisosNumber.setText("" + number);
            drawerCount.setVisibility(View.VISIBLE);
            drawerCount.setText(""+number);
        }
        if(number>99) {
            avisosNumber.setVisibility(View.VISIBLE);
            avisosNumber.setText("99+");
            drawerCount.setVisibility(View.VISIBLE);
            drawerCount.setText("00+");
        }
        if(number==0) {
            drawerCount.setVisibility(View.GONE);
            avisosNumber.setVisibility(View.GONE);
        }

    }

    //Fechar a toastExit mais rápido depois que o app fechar.
    @Override
    protected void onDestroy() {
        if (toastExit != null)
            toastExit.cancel();
        super.onDestroy();
    }
}
