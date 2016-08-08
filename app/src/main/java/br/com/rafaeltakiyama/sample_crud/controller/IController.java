package br.com.rafaeltakiyama.sample_crud.controller;

import android.content.Context;
import android.database.SQLException;
import java.util.*;

/**
 * Created by rafaelakiyama on 07/08/16.
 */

public interface IController<T> {

    T insert(T entidade, Context context) throws SQLException;

    long update (T entidade, Context context) throws SQLException;

    List<T> listAll(Context context) throws SQLException;

    int delete(T entidade, Context context) throws SQLException;

    int deleteAll(Context context) throws SQLException;

    int count(Context context) throws SQLException;
}
