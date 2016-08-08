package br.com.rafaeltakiyama.sample_crud.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.rafaeltakiyama.sample_crud.dao.database.BancoHelper;


public abstract class Dao<T> {

    private Context context;

    protected abstract String nomeTabela();

    protected abstract String whereClause();

    protected abstract String[] whereValues(T element);

    protected abstract T fromCursor(Cursor cursor);

    protected abstract ContentValues fromElement(T element);

    private SQLiteDatabase db;

    public Boolean addListObj(List<T> element) {

        try {
            String sql = "select * from " + nomeTabela() + ";";
            List<T> listElementoLocal = selectAllImpl(sql, null);

            if (element.size() > 0)
                Log.i(element.get(0).getClass().getSimpleName(), "Inserindo...");

            db().beginTransaction();
            try {
                for (T fromWs : element) {
                    if (!(isRepeated(fromWs, listElementoLocal))) {
                        insert(fromWs);
                    }
                }
                db().setTransactionSuccessful();
            } finally {
                db().endTransaction();
            }
        } catch (Exception e) {
            Log.e("Dao", "Erro: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            Log.i(element.get(0).getClass().getSimpleName(), "Concluido.");
        }
        return true;
    }

    public Boolean isRepeated(T elementoWs, List<T> listElementoLocal) {

        Boolean aux = false;

        if (listElementoLocal != null) {
            for (T elementoLocal : listElementoLocal) {
                if (elementoLocal.equals(elementoWs)) {
                    aux = true;
                    return aux;
                }
            }
        } else {
            Log.i(elementoWs.getClass().getSimpleName(), "Nao ha dados no banco");
        }
        return aux;
    }

    protected SQLiteDatabase db() {
        return BancoHelper.instance().db;
    }

    public Dao(Context context) {
        this.context = context;
    }

    protected Context getContext() {
        return context;
    }

    public long insert(T element) {
        ContentValues values = fromElement(element);

        long id = 0;

        if (db().isOpen()) {

            id = db().insertWithOnConflict(nomeTabela(), null, values, SQLiteDatabase.CONFLICT_REPLACE);
        }

        return id;
    }

    public long update(T element) {
        ContentValues values = fromElement(element);

        int rowAffected = 0;

        if (db().isOpen()) {
            rowAffected = db().update(nomeTabela(), values, whereClause(), whereValues(element));
        }

        return rowAffected;
    }

    public int delete(T element) {

        int rowDeleted = 0;
        if (db().isOpen()) {
            rowDeleted = db().delete(nomeTabela(), whereClause(),
                    whereValues(element));
        }

        return rowDeleted;
    }

    public int deleteAllLines() {

        int rowsDeleted = 0;

        if (db().isOpen()) {
            rowsDeleted = db().delete(nomeTabela(), null, null);
        }

        return rowsDeleted;

    }

    public void dropTable() {

        if (db().isOpen()) {
            db().execSQL("DROP TABLE IF EXISTS " + nomeTabela());
        }
    }

    public T selectPorCodigo(long codigo) {

        String sql = "select * from " + nomeTabela() + " where "
                + whereClause();

        return selectImpl(null, sql, String.valueOf(codigo));

    }

    public T select(long codigo, T element) {

        String sql = "select * from " + nomeTabela() + " where "
                + whereClause();

        return selectImpl(element, sql, String.valueOf(codigo));
    }

    protected T selectImpl(T element, String sql, String... parametros) { // var
        // args
        // permite
        // passar
        // n
        // parametros
        Cursor cursor = null;
        try {
            cursor = db().rawQuery(sql, parametros);

            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                return fromCursor(cursor);
            }

            throw new RuntimeException("N�o entrou no select");

        } finally {

            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

    }

    public List<T> selectAll() {
       return selectAllImpl("select * from " + nomeTabela());
    }

    public List<T> selectAllImpl(String sql, String... parametros) {
        Cursor cursor = null;
        try {
            BancoHelper.instance();
            if (!BancoHelper.db.isOpen())
                BancoHelper.instance().open(context);
            cursor = BancoHelper.db.rawQuery(sql, parametros);

            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                List<T> result = new ArrayList<T>();
                do {
                    result.add(fromCursor(cursor));
                } while (cursor.moveToNext());

                return result;
            }

            return null;
            //throw new RuntimeException("N�o entrou no select");

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {

            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    public List<T> selectFiltered(HashMap<String, String> parametros) {

        Cursor cursor = null;
        String sql = "select * from " + nomeTabela() + " where ";
        String[] params = new String[parametros.size()];
        int i = 0;

        for (String chave : parametros.keySet()) {
            String valor = (String) parametros.get(chave);
            if (sql.endsWith("where "))
                sql += chave + " = ?";
            else
                sql += " and " + chave + " = ?";

            params[i++] = valor;
        }

        try {
            BancoHelper.instance();
            cursor = BancoHelper.db.rawQuery(sql, params);

            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                List<T> result = new ArrayList<T>();
                do {
                    result.add(fromCursor(cursor));
                } while (cursor.moveToNext());

                return result;
            }

            return null;
        } finally {

            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }


    public int count() {
        int count = 0;

        String sql = "SELECT COUNT (*) AS COUNT FROM " + nomeTabela();

        if (db().isOpen()) {
            count = (int) DatabaseUtils.longForQuery(db(), sql, null);
        }

        return count;
    }

}
