/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage.repository.jdbc.model;

import com.gridnine.elsa.server.storage.transaction.TransactionManager;

import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JdbcUtils {

    private final static ThreadLocal<Connection> connections = new ThreadLocal<>();

    public static void setConnection(Connection connection){
        connections.set(connection);
    }

    public static String getTableName(String id){
        var idx = id.lastIndexOf(".");
        return idx == -1? id.toLowerCase(): id.substring(idx+1).toLowerCase();
    }

    public static String getVersionTableName(String id){
        var idx = id.lastIndexOf(".");
        return (idx == -1? id.toLowerCase(): id.substring(idx+1).toLowerCase())+"version";
    }

    public static String getCaptionTableName(String id){
        var idx = id.lastIndexOf(".");
        return (idx == -1? id.toLowerCase(): id.substring(idx+1).toLowerCase())+"caption";
    }

    public static boolean isNull(ResultSet rs, String propertyName) throws SQLException {
        return rs.getObject(propertyName) == null;
    }
    public static boolean isEquals(Object obj1, Object obj2) throws SQLException {
        if(obj1 == null){
            return obj2 == null;
        }
        if(obj1 instanceof Array){
            if(!(obj2 instanceof Array)){
                return false;
            }
            var arr1 = (Object[]) ((Array) obj1).getArray();
            var arr2 = (Object[]) ((Array) obj2).getArray();
            if(arr1.length != arr2.length){
                return false;
            }
            for(int n =0; n < arr1.length; n++){
                if(!Objects.equals(arr1[n], arr2[n])){
                    return false;
                }
            }
            return true;
        }
        return obj1.equals(obj2);
    }

    public static void update(String sql){
        TransactionManager.get().withTransaction((ctx) -> {
            try(var ps = connections.get().prepareStatement(sql)){
                ps.execute();
            }
        });
    }

    public static<T> T update(String sql, ResultSetMapper<T> rsm){
        return TransactionManager.get().withTransaction((ctx) -> {
            try(var ps = connections.get().prepareStatement(sql)){
                ResultSet rs = ps.executeQuery();
                if(!rs.next()){
                    return null;
                }
                return rsm.getValue(rs);
            }
        }, false);
    }

    public static boolean isNotEmptyResult(String sql){
        return TransactionManager.get().withTransaction((ctx) -> {
            try(var ps = connections.get().prepareStatement(sql)){
                var rs = ps.executeQuery();
                return rs.next();
            }
        }, true);
    }

    public static<T> T query(String sql, ResultSetMapper<T> rsm){
        return TransactionManager.get().withTransaction((ctx) -> {
            try(var ps = connections.get().prepareStatement(sql)){
                var rs = ps.executeQuery();
                if(!rs.next()){
                    return null;
                }
                return rsm.getValue(rs);
            }
        }, true);
    }

    public static<T> List<T> queryForList(String sql,  ResultSetMapper<T> rsm){
        return TransactionManager.get().withTransaction((ctx) -> {
            try(var ps = connections.get().prepareStatement(sql)){
                var rs = ps.executeQuery();
                List<T> result = new ArrayList<>();
                while(rs.next()){
                    result.add(rsm.getValue(rs));
                }
                return result;
            }
        }, true);
    }

    private JdbcUtils(){}

    public interface ResultSetMapper<T> {
        T getValue(ResultSet rs) throws Exception;
    }

}
