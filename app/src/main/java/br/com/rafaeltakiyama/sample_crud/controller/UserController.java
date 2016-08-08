package br.com.rafaeltakiyama.sample_crud.controller;

import android.content.Context;
import android.database.SQLException;

import java.util.*;

import br.com.rafaeltakiyama.sample_crud.dao.DaoFactory;
import br.com.rafaeltakiyama.sample_crud.dao.database.BancoHelper;
import br.com.rafaeltakiyama.sample_crud.model.User;

/**
 * Created by rafaelakiyama on 07/08/16.
 */

public class UserController implements IController<User> {

    @Override
    public User insert(User entidade, Context context) throws SQLException {

        long id;

        BancoHelper.instance().open(context);
        id = DaoFactory.get(User.class).insert(entidade);
        BancoHelper.instance().close();

        if (id > 0) {
            entidade.setId((int) id);
        } else {
            throw new SQLException("Could not insert the user.");
        }

        return entidade;
    }

    @Override
    public long update(User entidade, Context context) throws SQLException {

        long rowAffected = 0;

        BancoHelper.instance().open(context);
        rowAffected = DaoFactory.get(User.class).update(entidade);
        BancoHelper.instance().close();

        if (rowAffected <= 0) {
            throw new SQLException("Could not update the user.");
        }

        return rowAffected;
    }

    @Override
    public List<User> listAll(Context context) throws SQLException {

        List<User> list;

        BancoHelper.instance().open(context);
        list = DaoFactory.get(User.class).selectAll();
        BancoHelper.instance().close();

        if (list != null && !list.isEmpty()) {
            return list;
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public int delete(User entidade, Context context) throws SQLException {

        int rowDeleted = 0;

        BancoHelper.instance().open(context);
        rowDeleted = DaoFactory.get(User.class).delete(entidade);
        BancoHelper.instance().close();

        if (rowDeleted <= 0) {
            throw new SQLException("Could not delete the user.");
        }

        return rowDeleted;
    }

    @Override
    public int deleteAll(Context context) throws SQLException {

        int rowsDeleted = 0;

        BancoHelper.instance().open(context);
        rowsDeleted = DaoFactory.get(User.class).deleteAllLines();
        BancoHelper.instance().close();

        if (rowsDeleted <= 0) {
            throw new SQLException("Could not delete all users.");
        }

        return rowsDeleted;
    }

    @Override
    public int count(Context context) throws SQLException {

        int count;

        BancoHelper.instance().open(context);
        count = DaoFactory.get(User.class).count();
        BancoHelper.instance().close();

        return count;
    }
}
