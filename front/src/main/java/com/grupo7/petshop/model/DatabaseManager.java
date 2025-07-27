package com.grupo7.petshop.model;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.table.TableUtils;

import java.io.IOException;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String DATABASE_URL = "jdbc:sqlite:petshop.db";
    private static ConnectionSource connectionSource;

    public static void init() throws SQLException {
        System.out.println("Iniciando conexão com o banco...");
        if (connectionSource == null) {
            connectionSource = new JdbcConnectionSource(DATABASE_URL);
            // Cria as tabelas se não existirem
            TableUtils.createTableIfNotExists(connectionSource, Cliente.class);
            TableUtils.createTableIfNotExists(connectionSource, Produto.class);
            TableUtils.createTableIfNotExists(connectionSource, Pet.class);
            TableUtils.createTableIfNotExists(connectionSource, Venda.class);
            TableUtils.createTableIfNotExists(connectionSource, Agendamento.class);
            System.out.println("Conexão criada e tabelas garantidas.");
        }
    }

    public static Dao<Cliente, Integer> getClienteDao() throws SQLException {
        init();
        return DaoManager.createDao(connectionSource, Cliente.class);
    }
    public static Dao<Produto, Integer> getProdutoDao() throws SQLException {
        init();
        return DaoManager.createDao(connectionSource, Produto.class);
    }
    public static Dao<Pet, Integer> getPetDao() throws SQLException {
        init();
        return DaoManager.createDao(connectionSource, Pet.class);
    }
    public static Dao<Venda, Integer> getVendaDao() throws SQLException {
        init();
        return DaoManager.createDao(connectionSource, Venda.class);
    }
    public static Dao<Agendamento, Integer> getAgendamentoDao() throws SQLException {
        init();
        return DaoManager.createDao(connectionSource, Agendamento.class);
    }

    public static void close() {
        if (connectionSource != null) {
            try {
                connectionSource.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
