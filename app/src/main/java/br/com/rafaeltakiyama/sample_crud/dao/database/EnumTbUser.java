package br.com.rafaeltakiyama.sample_crud.dao.database;

/**
 * Created by rafaelakiyama on 07/08/16.
 */

public enum EnumTbUser {

    TB_USER("tb_user"),
    ID("id"),
    FIRST_NAME("first_name"),
    LAST_NAME("last_name");

    private String name;

    EnumTbUser(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
