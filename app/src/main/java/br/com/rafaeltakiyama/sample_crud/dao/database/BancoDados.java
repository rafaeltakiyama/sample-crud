package br.com.rafaeltakiyama.sample_crud.dao.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import br.com.rafaeltakiyama.sample_crud.R;

class BancoDados extends SQLiteOpenHelper { //nao � publica... so quem esta no mesmo pacote enxerga- (BancoHelper)

    //so vamos trabalhar com um Banco
    public static final String BANCO_NOME = "sample_crud_db";

    private Context context;


    public BancoDados(Context context, int version) {
        super(context, BANCO_NOME, null, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        InputStream is = context.getResources().openRawResource(R.raw.banco);//acessa os dados bits
        InputStreamReader isr = new InputStreamReader(is);//Leitor de stream - para n�o trabalhar com bit
        BufferedReader bf = new BufferedReader(isr);//otimiza a leitura - memoria - tenta impedir que estoure a memoria

        String line;

        try {
            StringBuilder command = new StringBuilder();

            while ((line = bf.readLine()) != null) {
                command.append(line);

                if (line.endsWith(";")) {
                    db.execSQL(command.toString());
                    command = new StringBuilder();
                }

            }
        } catch (Exception e) {
            Log.e("BancoDados", "Erro criado banco de dados", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        InputStream is = null;

        if (oldVersion == 1) {
            is = getClass().getResourceAsStream("banco_upgrade.sql");//acessa os dados bits

        }

        InputStreamReader isr = new InputStreamReader(is);//Leitor de stream - para n�o trabalhar com bit
        BufferedReader bf = new BufferedReader(isr);//otimiza a leitura - memoria - tenta impedir que estoure a memoria

        String line;

        try {
            StringBuilder command = new StringBuilder();

            while ((line = bf.readLine()) != null) {
                command.append(line);

                if (line.endsWith(";")) {
                    db.execSQL(command.toString());
                    command = new StringBuilder();
                }

            }
        } catch (Exception e) {
            Log.e("BancoDados", "Erro criado banco de dados", e);
        }
    }

}
