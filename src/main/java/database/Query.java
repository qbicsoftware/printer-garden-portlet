package database;

import javafx.util.Pair;

import java.util.List;
import java.util.stream.Collectors;

public final class Query {

    public  Query(){

    }

    public static String selectFrom(List<String> entry, List<String> location){
        return "SELECT " + entry.stream()
                                .collect(Collectors.joining(", "))
                +" FROM "
                         + location.stream()
                                   .collect(Collectors.joining(", "));

    }

    public static String selectFromWhereAnd(List<String> entry, List<String> location, List<Pair<String, String>> condition){
        String query =  "SELECT " + entry.stream().collect(Collectors.joining(", "))
                + " FROM " + location.stream().collect(Collectors.joining(", "))
                + " WHERE ";
        String where = "";
        int condCounter = 0;
        for (Pair<String, String> c: condition) {
            where = where.concat(c.getKey().concat(" = '").concat(c.getValue()).concat("'"));
            if(condCounter < condition.size() - 1){
                where += " AND ";
            }
            condCounter += 1;

        }
        return query.concat(where);
    }

    public static String innerJoinOn(String table, String on, String condition){
        return "INNER JOIN " + table + " ON " + on + " = " + condition;
    }

    public static String deleteFromWhere(String tablename, String attribute, String value){
        return "DELETE FROM " + tablename + " WHERE " + attribute + "='" + value + "'";
    }

    public static String insertIntoSingleValues(String tablename, List<String> entry, List<String> values){
        return "INSERT INTO " + tablename + " (" + entry.stream().collect(Collectors.joining(", ")) + ") " +
                "VALUES ('" + values.stream().collect(Collectors.joining("', '")) +"')";
    }

    public static String insertIntoStatementValues(String tablename, List<String> entry, List<String> values){
        return "INSERT INTO " + tablename + " (" + entry.stream().collect(Collectors.joining(", ")) + ") " +
                "VALUES ((" + values.stream().collect(Collectors.joining("), (")) +"))";
    }

    public static String selectDistinctFrom(List<String> entry, List<String> location) {
        return "SELECT DISTINCT " + entry.stream()
                .collect(Collectors.joining(", "))
                + " FROM "
                + location.stream()
                .collect(Collectors.joining(", "));
    }
}
