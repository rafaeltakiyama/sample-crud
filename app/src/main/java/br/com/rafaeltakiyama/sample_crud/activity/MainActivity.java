package br.com.rafaeltakiyama.sample_crud.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.rafaeltakiyama.sample_crud.R;
import br.com.rafaeltakiyama.sample_crud.adapter.UserAdapter;
import br.com.rafaeltakiyama.sample_crud.controller.UserController;
import br.com.rafaeltakiyama.sample_crud.dao.DaoFactory;
import br.com.rafaeltakiyama.sample_crud.delegate.Delegate;
import br.com.rafaeltakiyama.sample_crud.model.User;

public class MainActivity extends AppCompatActivity implements Delegate {

    private EditText edtTxtFirstName;
    private EditText edtTxtLastName;
    private Button btnInsert;
    private Context context;
    private UserController controller = new UserController();
    private UserAdapter userAdapter;
    private List<User> users = new ArrayList<>();
    private User userToUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DaoFactory.init(context);

        initializeComponents();

        users = controller.listAll(context);

        addRecyclerView();
    }

    private void addRecyclerView() {
        RecyclerView recList = (RecyclerView) findViewById(R.id.recycler_view);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        userAdapter = new UserAdapter(users, this);
        recList.setAdapter(userAdapter);
    }

    private void initializeComponents() {
        context = this;
        edtTxtFirstName = (EditText) findViewById(R.id.edt_txt_first_name);
        edtTxtLastName = (EditText) findViewById(R.id.edt_txt_last_name);
        btnInsert = (Button) findViewById(R.id.btn_insert);
        btnInsert.setOnClickListener(onClickBtnInsertUpdate());
    }

    private View.OnClickListener onClickBtnInsertUpdate() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validadeEdtTxt()) {

                    User user = new User();
                    user.setFirstName(edtTxtFirstName.getText().toString());
                    user.setLastName(edtTxtLastName.getText().toString());

                    if (userToUpdate != null) {
                        user.setId(userToUpdate.getId());
                        long rowsAffected = controller.update(user, context);
                        if (rowsAffected > 0) {
                            Toast.makeText(context, getString(R.string.string_user_updated), Toast.LENGTH_SHORT).show();
                            users.clear();
                            users.addAll(controller.listAll(context));
                        }
                        btnInsert.setText(getString(R.string.string_insert));
                        userToUpdate = null;

                    } else {
                        User usuarioAdicionado = controller.insert(user, context);
                        Log.i("User Submitted", usuarioAdicionado.toString());
                        users.add(usuarioAdicionado);
                    }

                    userAdapter.notifyDataSetChanged();
                    cleanEditText();
                }
            }
        };
    }

    private void cleanEditText() {
        edtTxtFirstName.setText("");
        edtTxtLastName.setText("");
        edtTxtFirstName.requestFocus();
    }

    private boolean validadeEdtTxt() {

        boolean edtTxtFirstNameOk;
        boolean edtTxtLastNameOk;
        String error = getResources().getString(R.string.blank_field);


        if (edtTxtFirstName.getText().toString().trim().equalsIgnoreCase("")) {
            edtTxtFirstName.setError(error);
            edtTxtFirstNameOk = false;
        } else {
            edtTxtFirstNameOk = true;
        }

        if (edtTxtLastName.getText().toString().trim().equalsIgnoreCase("")) {
            edtTxtLastName.setError(error);
            edtTxtLastNameOk = false;
        } else {
            edtTxtLastNameOk = true;
        }

        return edtTxtFirstNameOk && edtTxtLastNameOk;
    }

    @Override
    public void onClickRecycleView(User user) {
        showDialogManageUser(user);
    }

    private void showDialogManageUser(final User user) {
        LayoutInflater li = LayoutInflater.from(context);
        View dialogView = li.inflate(R.layout.dialog_manage_user, null);
        final RadioGroup radioButton = (RadioGroup) dialogView.findViewById(R.id.radio_group_manage_user);

        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(R.string.string_manage_user);
        builder.setView(dialogView);
        builder.setPositiveButton(R.string.string_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                int selectedRadioBtn = radioButton.getCheckedRadioButtonId();

                switch (selectedRadioBtn) {
                    case R.id.radio_update:
                        updateUser(user);
                        break;

                    case R.id.radio_delete:
                        deleteUser(user);
                        break;
                }


            }
        });
        builder.setNegativeButton(R.string.string_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteUser(User user) {
        try {
            int rowAffected = controller.delete(user, context);
            if (rowAffected > 0) {
                Toast.makeText(context, getString(R.string.user_deleted), Toast.LENGTH_SHORT).show();
                users.remove(user);
                userAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateUser(User user) {
        userToUpdate = user;
        edtTxtFirstName.setText(user.getFirstName());
        edtTxtLastName.setText(user.getLastName());
        edtTxtFirstName.requestFocus();
        btnInsert.setText(getString(R.string.string_update));
    }
}
