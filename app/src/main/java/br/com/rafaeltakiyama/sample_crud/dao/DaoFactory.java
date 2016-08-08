package br.com.rafaeltakiyama.sample_crud.dao;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import br.com.rafaeltakiyama.sample_crud.model.User;

public class DaoFactory {

    private static Map<Class<?>, Dao<?>> daos;

    public static void init(Context context) {
        daos = new HashMap<>();
        daos.put(User.class, new UserDao(context));
    }

    @SuppressWarnings("unchecked")
    public static <T> Dao<T> get(Class<T> type) {
        return (Dao<T>) daos.get(type);
    }
}
