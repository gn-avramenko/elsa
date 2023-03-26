/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.storage.repository.jdbc;

import com.gridnine.elsa.common.l10n.SupportedLocalesProvider;
import com.gridnine.elsa.common.model.common.EnumMapper;
import com.gridnine.elsa.common.model.common.Xeption;
import com.gridnine.elsa.common.utils.LocaleUtils;
import com.gridnine.elsa.common.utils.TextUtils;
import com.gridnine.elsa.meta.domain.DomainMetaRegistry;
import com.gridnine.elsa.meta.serialization.SerializableMetaRegistry;
import com.gridnine.elsa.server.storage.repository.jdbc.model.JdbcUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class JdbcEnumMapperImpl implements EnumMapper {
    private final Map<String,Map<String, Integer>>  name2Id = new HashMap<>();
    private final Map<String, Map<Integer,String>> id2name = new HashMap<>();

    public JdbcEnumMapperImpl() {
        for(String enumId: DomainMetaRegistry.get().getEnumsIds()){
            var it = SerializableMetaRegistry.get().getEnums().get(enumId);
            var res = JdbcUtils.queryForList("select id, enumconstant, %s from %s".formatted(TextUtils.join(SupportedLocalesProvider.get().getSupportedLocales()
                    .stream().map(loc -> "%sname".formatted(loc.getLanguage())).toList(), ", "), JdbcUtils.getTableName(it.getId())), (rs) ->{
                var item = new EnumTableData(rs.getInt("id"), rs.getString("enumconstant"), new HashMap<>());
                for(Locale locale : SupportedLocalesProvider.get().getSupportedLocales()){
                    item.localizations.put(locale.getLanguage(), rs.getString("%sname".formatted(locale.getLanguage())));
                }
                return item;
            });
            var map = new HashMap<String, Integer>();
            for(var enumItem: it.getItems().values()){
                var row = res.stream().filter(it2 ->enumItem.getId().equals(it2.name)).findFirst().orElse(null);
                if(row == null){
                    Integer id = null;
                    boolean found = false;
                    while (id == null || found) {
                        id = JdbcUtils.updateAndReturnResult(JdbcDialect.get().getSequenceNextValueSql("intid"), rs -> rs.getInt(1));
                        var id2 = id;
                        found = res.stream().anyMatch(it2 -> id2 != null && id2.equals(it2.id));
                    }
                    JdbcUtils.update("insert into %s(id, enumconstant, %s) values (%s, '%s', %s)".formatted(
                            JdbcUtils.getTableName(it.getId()),
                            TextUtils.join(SupportedLocalesProvider.get().getSupportedLocales().stream().map(loc -> "%sName".formatted(loc.getLanguage())).toList(), ", "),
                            id,enumItem.getId(),
                            TextUtils.join(SupportedLocalesProvider.get().getSupportedLocales().stream()
                                    .map(loc -> "'%s'".formatted(LocaleUtils.getLocalizedName(enumItem.getDisplayNames(), loc, enumItem.getId()))).toList(), ", ")
                    ));
                    map.put(enumItem.getId(), id);
                } else {
                    var differs = SupportedLocalesProvider.get().getSupportedLocales().stream().anyMatch(loc ->!LocaleUtils.getLocalizedName(enumItem.getDisplayNames(), loc, enumItem.getId()).equals(row.localizations().get(loc.getLanguage())));
                    if(differs){
                        JdbcUtils.update("update %s set %s where enumconstant='%s'".formatted(
                                JdbcUtils.getTableName(it.getId()),
                                TextUtils.join(SupportedLocalesProvider.get().getSupportedLocales().stream()
                                        .map(loc -> "%sname='%s'".formatted(loc.getLanguage(), LocaleUtils.getLocalizedName(enumItem.getDisplayNames(), loc, enumItem.getId()))).toList(), ", "),
                                enumItem.getId()));
                    }
                    map.put(enumItem.getId(), (Integer) row.id);
                }

            }
            var nid = new LinkedHashMap<String, Integer>();
            name2Id.put(it.getId(), nid);
            var idn = new LinkedHashMap<Integer, String>();
            id2name.put(it.getId(), idn);
            map.forEach((name, id) ->{
                nid.put(name, id);
                idn.put(id, name);
            });
        }
    }

    @Override
    public int getId(Enum<?> value) {
        var className = value.getClass().getName();
        if(!name2Id.containsKey(className)){
            return value.ordinal();
        }
        var result = name2Id.get(className).get(value.name());
        if(result == null){
            throw Xeption.forDeveloper("unknown value %s of enum %s".formatted(value.name(), className));
        }
        return result;
    }

    @Override
    public String getName(int id, Class<Enum<?>> cls) {
        var className = cls.getName();
        if(!id2name.containsKey(className)){
            for(var item: cls.getEnumConstants()){
                if(item.ordinal() == id){
                    return item.name();
                }
            }
            throw Xeption.forDeveloper("unknown id %s of enum %s".formatted(id, className));
        }
        var result = id2name.get(className).get(id);
        if(result == null){
            throw Xeption.forDeveloper("unknown id %s of enum %s".formatted(id, className));
        }
        return result;
    }

    record EnumTableData(int id, String name, Map<String,String> localizations){}
}
