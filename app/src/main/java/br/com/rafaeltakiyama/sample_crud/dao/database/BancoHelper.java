package br.com.rafaeltakiyama.sample_crud.dao.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

//responsabilidade de abrir e fechar o banco.. entre outras
public class BancoHelper {

    // n�o deixar multiplas instancias do banco aberta. enquanto uma n�o for
    // salva.. as outras n�o h� enxergam

    public static SQLiteDatabase db;

    public static final int BANCO_VERSION = 1;

    private static BancoHelper instance;

    //padr�o singleton
    public static BancoHelper instance() {
        if (instance == null) {
            instance = new BancoHelper();
        }
        return instance;
    }

    //padr�o singleton // nao tem construtor publico
    private BancoHelper() {
    }

    public SQLiteDatabase open(Context context) {
        try {
            if (db == null || !db.isOpen()) {
                BancoDados banco = new BancoDados(context, BANCO_VERSION);
                db = banco.getWritableDatabase();
                Log.d("BancoHelper", "Opening DB");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return db;
    }

    public void close() {
        if (db.isOpen()) {
            db.close();
            Log.d("BancoHelper", "Closing DB");
        }
    }
}
