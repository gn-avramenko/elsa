/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage.database.jdbc.model;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class JdbcUtils {
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

    private JdbcUtils(){}

}
