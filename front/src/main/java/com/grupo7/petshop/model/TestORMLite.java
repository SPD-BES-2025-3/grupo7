package com.grupo7.petshop.model;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.util.List;

public class TestORMLite {
    public static void main(String[] args) throws Exception {
        String databaseUrl = "jdbc:sqlite:petshop.db";
        ConnectionSource connectionSource = new JdbcConnectionSource(databaseUrl);

        // Cria a tabela se não existir
        TableUtils.createTableIfNotExists(connectionSource, Cliente.class);

        // Cria o DAO para Cliente
        Dao<Cliente, Integer> clienteDao = DaoManager.createDao(connectionSource, Cliente.class);

        // Cria e salva um cliente
        Cliente cliente = new Cliente("João", "joao@email.com", "99999-9999", "123.456.789-00", "Rua X", "Obs", true);
        clienteDao.create(cliente);

        // Busca e imprime todos os clientes
        List<Cliente> clientes = clienteDao.queryForAll();
        for (Cliente c : clientes) {
            System.out.println(c.getNome() + " - " + c.getEmail());
        }

        connectionSource.close();
    }
} 