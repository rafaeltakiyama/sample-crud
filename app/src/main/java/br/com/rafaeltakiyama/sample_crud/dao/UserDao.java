package br.com.rafaeltakiyama.sample_crud.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import br.com.rafaeltakiyama.sample_crud.dao.database.EnumTbUser;
import br.com.rafaeltakiyama.sample_crud.model.User;

/**
 * Created by rafaelakiyama on 07/08/16.
 */

public class UserDao extends Dao<User>{

    public UserDao(Context context) {
        super(context);
    }

    @Override
    protected String nomeTabela() {
        return EnumTbUser.TB_USER.getName();
    }

    @Override
    protected String whereClause() {
        return EnumTbUser.ID.getName() + " = ? ";
    }

    @Override
    protected String[] whereValues(User element) {
        return new String[]{String.valueOf(element.getId())};
    }

    @Override
    protected User fromCursor(Cursor cursor) {

        User user = new User();

        int idxIdUser = cursor.getColumnIndex(EnumTbUser.ID.getName());
        user.setId(cursor.getInt(idxIdUser));

        int idxFirstName = cursor.getColumnIndex(EnumTbUser.FIRST_NAME.getName());
        user.setFirstName(cursor.getString(idxFirstName));

        int idxLastName = cursor.getColumnIndex(EnumTbUser.LAST_NAME.getName());
        user.setLastName(cursor.getString(idxLastName));

        return user;
    }

    @Override
    protected ContentValues fromElement(User element) {

        ContentValues values = new ContentValues();

        values.put(EnumTbUser.ID.getName(), element.getId());
        values.put(EnumTbUser.FIRST_NAME.getName(), element.getFirstName());
        values.put(EnumTbUser.LAST_NAME.getName(), element.getLastName());

        return values;
    }
}
