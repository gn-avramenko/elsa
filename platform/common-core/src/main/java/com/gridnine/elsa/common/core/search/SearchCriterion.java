/*****************************************************************
 * Gridnine http://www.gridnine.com
 * Project: Elsa
 *****************************************************************/

package com.gridnine.elsa.common.core.search;

import com.gridnine.elsa.common.core.model.common.BaseIdentity;
import com.gridnine.elsa.common.core.model.domain.EntityReference;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public abstract class SearchCriterion {

    public static<T extends FieldNameSupport & EqualitySupport> SimpleCriterion eq(T property, Object value){
        return new SimpleCriterion(property.name, SimpleCriterion.Operation.EQ, value);
    }

    public static<T extends FieldNameSupport & EqualitySupport> SimpleCriterion ne(T property, Object value){
        return new SimpleCriterion(property.name, SimpleCriterion.Operation.NE, value);
    }

    public static<T extends FieldNameSupport & StringOperationsSupport> SimpleCriterion like(T property, String value){
        return new SimpleCriterion(property.name, SimpleCriterion.Operation.LIKE, value);
    }

    public static<T extends FieldNameSupport & StringOperationsSupport> SimpleCriterion ilike(T property, String value){
        return new SimpleCriterion(property.name, SimpleCriterion.Operation.ILIKE, value);
    }

    public static<T extends FieldNameSupport & ComparisonSupport> SimpleCriterion gt(T property, Number value){
        return new SimpleCriterion(property.name, SimpleCriterion.Operation.GT, value);
    }

    public static<T extends FieldNameSupport & ComparisonSupport> SimpleCriterion gt(T property, LocalDate value){
        return new SimpleCriterion(property.name, SimpleCriterion.Operation.GT, value);
    }

    public static<T extends FieldNameSupport & ComparisonSupport> SimpleCriterion gt(T property, LocalDateTime value){
        return new SimpleCriterion(property.name, SimpleCriterion.Operation.GT, value);
    }

    public static<T extends FieldNameSupport & ComparisonSupport> SimpleCriterion ge(T property, Number value){
        return new SimpleCriterion(property.name, SimpleCriterion.Operation.GE, value);
    }

    public static<T extends FieldNameSupport & ComparisonSupport> SimpleCriterion ge(T property, LocalDate value){
        return new SimpleCriterion(property.name, SimpleCriterion.Operation.GE, value);
    }

    public static<T extends FieldNameSupport & ComparisonSupport> SimpleCriterion ge(T property, LocalDateTime value){
        return new SimpleCriterion(property.name, SimpleCriterion.Operation.GE, value);
    }

    public static<T extends FieldNameSupport & ComparisonSupport> SimpleCriterion le(T property, Number value){
        return new SimpleCriterion(property.name, SimpleCriterion.Operation.LE, value);
    }

    public static<T extends FieldNameSupport & ComparisonSupport> SimpleCriterion le(T property, LocalDate value){
        return new SimpleCriterion(property.name, SimpleCriterion.Operation.LE, value);
    }

    public static<T extends FieldNameSupport & ComparisonSupport> SimpleCriterion le(T property, LocalDateTime value){
        return new SimpleCriterion(property.name, SimpleCriterion.Operation.LE, value);
    }

    public static<T extends FieldNameSupport & ComparisonSupport> SimpleCriterion lt(T property, Number value){
        return new SimpleCriterion(property.name, SimpleCriterion.Operation.LT, value);
    }

    public static<T extends FieldNameSupport & ComparisonSupport> SimpleCriterion lt(T property, LocalDate value){
        return new SimpleCriterion(property.name, SimpleCriterion.Operation.LT, value);
    }

    public static<T extends FieldNameSupport & ComparisonSupport> SimpleCriterion lt(T property, LocalDateTime value){
        return new SimpleCriterion(property.name, SimpleCriterion.Operation.LT, value);
    }

    public static<T extends FieldNameSupport & CollectionSupport> SimpleCriterion contains(T property, String value){
        return new SimpleCriterion(property.name, SimpleCriterion.Operation.CONTAINS, value);
    }

    public static<T extends FieldNameSupport & CollectionSupport, E extends Enum<E>> SimpleCriterion contains(T property, E value){
        return new SimpleCriterion(property.name, SimpleCriterion.Operation.CONTAINS, value);
    }

    public static<T extends FieldNameSupport & CollectionSupport, E extends BaseIdentity> SimpleCriterion contains(T property, EntityReference<E> value){
        return new SimpleCriterion(property.name, SimpleCriterion.Operation.CONTAINS, value);
    }

    public static<T extends FieldNameSupport & CollectionSupport> CheckCriterion isEmpty(T property){
        return new CheckCriterion(property.name, CheckCriterion.Check.IS_EMPTY);
    }

    public static<T extends FieldNameSupport & CollectionSupport> CheckCriterion isNotEmpty(T property){
        return new CheckCriterion(property.name, CheckCriterion.Check.NOT_EMPTY);
    }

    public static<T extends FieldNameSupport> CheckCriterion isNull(T property){
        return new CheckCriterion(property.name, CheckCriterion.Check.IS_NULL);
    }

    public static<T extends FieldNameSupport> CheckCriterion isNotNull(T property){
        return new CheckCriterion(property.name, CheckCriterion.Check.IS_NOT_NULL);
    }

    public static<T extends FieldNameSupport & ComparisonSupport> BetweenCriterion between(T property, Number lo, Number hi){
        return new BetweenCriterion(property.name, lo, hi);
    }

    public static<T extends FieldNameSupport & ComparisonSupport> BetweenCriterion between(T property, LocalDate lo, LocalDate hi){
        return new BetweenCriterion(property.name, lo, hi);
    }

    public static<T extends FieldNameSupport & ComparisonSupport> BetweenCriterion between(T property, LocalDateTime lo, LocalDateTime hi){
        return new BetweenCriterion(property.name, lo, hi);
    }

    public static<T extends FieldNameSupport & ComparisonSupport> InCriterion<String> inStringCollection(T property, List<String> values) {
        return new InCriterion<>(property.name, values);
    }

    public static<T extends FieldNameSupport & ComparisonSupport, E extends Enum<E>> InCriterion<E> inEnumCollection(T property, List<E> values) {
        return new InCriterion<>(property.name, values);
    }

    public static<T extends FieldNameSupport & ComparisonSupport, E extends BaseIdentity> InCriterion<EntityReference<E>> inEntityCollection(T property, List<EntityReference<E>> values) {
        return new InCriterion<>(property.name, values);
    }

    public static<T extends FieldNameSupport & ComparisonSupport> InCriterion<String> inStringCollection(T property, String... values) {
        return new InCriterion<>(property.name, Arrays.asList(values));
    }

    @SafeVarargs
    public static<T extends FieldNameSupport & ComparisonSupport, E extends Enum<E>> InCriterion<E> inEnumCollection(T property, E... values) {
        return new InCriterion<>(property.name, Arrays.asList(values));
    }

    @SafeVarargs
    public static<T extends FieldNameSupport & ComparisonSupport, E extends BaseIdentity> InCriterion<EntityReference<E>> inEntityCollection(T property, EntityReference<E>... values) {
        return new InCriterion<>(property.name, Arrays.asList(values));
    }

    public static JunctionCriterion and(List<SearchCriterion> crits) {
        return new JunctionCriterion(false, crits);
    }

    public static JunctionCriterion and(SearchCriterion... crits) {
        return new JunctionCriterion(false, Arrays.asList(crits));
    }

    public static JunctionCriterion or(List<SearchCriterion> crits) {
        return new JunctionCriterion(true, crits);
    }

    public static JunctionCriterion or(SearchCriterion... crits) {
        return new JunctionCriterion(true, Arrays.asList(crits));
    }

    public static NotCriterion not(SearchCriterion crit) {
        return new NotCriterion(crit);
    }

}
