/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.server.core.storage;

import com.gridnine.elsa.common.core.model.domain.BaseAsset;
import com.gridnine.elsa.common.core.model.domain.VersionInfo;
import com.gridnine.elsa.common.core.search.SearchQuery;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

public interface Database {
    <A extends BaseAsset> DatabaseAssetWrapper<A> loadAssetWrapper(Class<A> aClass, long id) throws Exception;

    <A extends BaseAsset> void saveAsset(DatabaseAssetWrapper<A> aDatabaseAssetWrapper, DatabaseAssetWrapper<A> oldAsset) throws Exception;

    <A extends BaseAsset> void saveAssetVersion(Class<A> aClass, long id, BlobWrapper data, VersionInfo vi) throws Exception;

    <A extends BaseAsset> List<A> searchAssets(Class<A> cls, SearchQuery updateQuery) throws Exception;

    List<VersionMetadata> getVersionsMetadata(Class<?> cls, long id) throws Exception;

    <A extends BaseAsset> void deleteAsset(Class<A> aClass, long id) throws Exception;

    VersionData loadVersion(Class<?> cls, long id, int number) throws Exception;

    void deleteVersion(Class<?> cls, long id, int number) throws Exception;

    <A extends BaseAsset> A loadAsset(Class<A> cls, long id) throws Exception;

    void updateCaptions(Class<?> aClass, long id, LinkedHashMap<Locale, String> captions, boolean insert) throws Exception;

    void updateCaptions(Class<?> aClass, long id, String name, boolean insert) throws Exception;

    void deleteCaptions(Class<?> aClass, long id) throws Exception;
}
