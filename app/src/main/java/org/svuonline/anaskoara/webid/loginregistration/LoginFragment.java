package org.svuonline.anaskoara.webid.loginregistration;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.svuonline.anaskoara.webid.loginregistration.models.MyApplication;
import org.svuonline.anaskoara.webid.loginregistration.server.ServerRequest;
import org.svuonline.anaskoara.webid.loginregistration.server.ServerResponse;
import org.svuonline.anaskoara.webid.loginregistration.models.User;
import org.svuonline.anaskoara.webid.loginregistration.server.RequestInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class LoginFragment extends Fragment implements View.OnClickListener{

    private AppCompatButton btn_login;
    private EditText et_login,et_password;
    private TextView tv_register;
    private ProgressBar progress;
    private SharedPreferences pref;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login,container,false);
        initViews(view);
        return view;
    }

    private void initViews(View view){

        pref = getActivity().getPreferences(0);

        btn_login = (AppCompatButton)view.findViewById(R.id.btn_login);
        tv_register = (TextView)view.findViewById(R.id.tv_register);
        et_login = (EditText)view.findViewById(R.id.et_login);
        et_password = (EditText)view.findViewById(R.id.et_password);

        progress = (ProgressBar)view.findViewById(R.id.progress);

        btn_login.setOnClickListener(this);
        tv_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.tv_register:
                goToRegister();
                break;

            case R.id.btn_login:
                String login = et_login.getText().toString();
                String password = et_password.getText().toString();

                if(!login.isEmpty() && !password.isEmpty()) {

                    progress.setVisibility(View.VISIBLE);

                    loginProcess(login,password);

                } else {

                    Snackbar.make(getView(), "Fields are empty !", Snackbar.LENGTH_LONG).show();
                }
                break;

        }
    }
    private void loginProcess(String login,String password){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                //.addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RequestInterface requestInterface = retrofit.create(RequestInterface.class);

        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.LOGIN_OPERATION);
        request.setUser(user);
        Call<ServerResponse> response = requestInterface.operation(request);

        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
               String  x=response.body().toString();
                ServerResponse resp = response.body();
                Snackbar.make(getView(), resp.getMessage(), Snackbar.LENGTH_LONG).show();

                if( resp.getResult().equals(Constants.SUCCESS)){
                    User user=resp.getUser();


                    MyApplication app = (MyApplication) getActivity().getApplicationContext();
                    app.setLoggedIn(true);
                    app.setUser(user);
                    goToProfile();

                }
                progress.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                progress.setVisibility(View.INVISIBLE);
                Log.d(Constants.TAG,"failed");
                Snackbar.make(getView(), t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();

            }
        });
    }

    private void goToRegister(){

        Fragment register = new RegisterFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame,register);
        ft.commit();
    }

    private void goToProfile(){
        MainActivity activity=(MainActivity)getActivity();

        //Fragment profile = new ProfileFragment();
        //FragmentTransaction ft = getFragmentManager().beginTransaction();
        ///ft.replace(R.id.fragment_frame,profile);
        //ft.commit();

        Intent detailIntent = new Intent(activity, ProductsActivity.class);
        //int productId=selectedProduct.getId();
        //detailIntent.putExtra("id",productId);
        startActivity(detailIntent);
        //activity.viewMenu(true);
    }
}
