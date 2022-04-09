/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage.jdbc.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
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
    public static boolean isEquals(Object obj1, Object obj2){
        if(obj1 == null){
            return obj2 == null;
        }
        if(obj1 instanceof List<?> lst1){
            if(!(obj2 instanceof List<?>)){
                return false;
            }
            var lst2 = (List<?>) obj2;
            if(lst1.size() != lst2.size()){
                return false;
            }
            for(int n =0; n < lst1.size(); n++){
                if(!Objects.equals(lst1.get(n), lst2.get(n))){
                    return false;
                }
            }
            return true;
        }
        return obj1.equals(obj2);
    }

    private JdbcUtils(){}

}
