package com.zipcodewilmington.jdbc.oop.model;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class StatementExecutor implements Closeable {
    private final List<ResultSetHandler> resultSetHandlers = new ArrayList<>();
    private final Connection connection;

    public StatementExecutor(Connection connection) {
        this.connection = connection;
    }

    /**
     * executes update statement on the respective connection object
     *
     * @param updateStatement string representative of a SQL update statement
     */
    public void executeUpdate(String updateStatement) {
        try {
            Statement statement = getConnection().createStatement();
            statement.executeUpdate(updateStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * executes query statement on the respective connection object
     *
     * @param queryStatement string representative of a SQL query statement
     */
    public ResultSetHandler executeQuery(String queryStatement) {
        ResultSetHandler resultSetHandler = this.query(queryStatement);
        resultSetHandlers.add(resultSetHandler);
        return resultSetHandler;
    }

    /**
     * executes query statement on the respective connection object
     *
     * @param queryStatement string representative of a SQL query statement
     * @return wrapper of ResultSet
     */
    private ResultSetHandler query(String queryStatement) {
        ResultSet resultSet = null;
        try {
            Statement statement = getConnection().createStatement();
            resultSet = statement.executeQuery(queryStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ResultSetHandler(resultSet);
    }


    public Connection getConnection() {
        return connection;
    }


    /**
     * closes connection object and all resultSetHandlers
     */
    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (ResultSetHandler resultSetHandler : resultSetHandlers) {
            resultSetHandler.close();
        }
    }

    @Override // Invoked upon garbage collection
    public void finalize() {
        close();
    }


}
