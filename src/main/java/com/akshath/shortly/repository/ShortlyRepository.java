package com.akshath.shortly.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class ShortlyRepository {
    
    @Autowired
    JdbcTemplate jdbcTemplate;

    Logger log = Logger.getLogger(this.getClass().getCanonicalName());

    public long insert(String table, Map<String, Object> data) {
        String[] values = new String[data.size()];
        Integer index = 0;
        String[] columns = new String[data.size()];
        String[] querypart = new String[data.size()];

        for(Map.Entry<String, Object> dataentry: data.entrySet()) {
            String columnname = dataentry.getKey();
            values[index] = "?";
            columns[index] = columnname;
            querypart[index] = columnname+" = VALUES("+columnname+")";
            index++;
        }

        String query = "INSERT INTO "+ table +"("+String.join(",", columns)+") VALUES("+String.join(",", values)+") "
                    + "ON DUPLICATE KEY UPDATE "+String.join(",", querypart);

        log.info("Query execution ... STARTED");
        log.info("Query: "+ query);
        log.info("Params: "+ data.toString());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(new PreparedStatementCreator(){
        
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                Integer index = 1;
                for(Map.Entry<String, Object> dEntry: data.entrySet()) {
                    Object columnvalue = dEntry.getValue();

                    if(columnvalue == null) {
                        statement.setNull(index, Types.INTEGER);
                    } else {
                        statement.setObject(index, columnvalue);
                    }

                    index++;
                }

                return statement;
            }
        }, keyHolder);

        long insertId = 0;
        try {
            if(keyHolder.getKeyList().size() > 1) {
                insertId = Long.parseLong(keyHolder.getKeyList().get(0).get("GENERATED_KEY").toString());
            } else if(keyHolder.getKeyList().size() == 1) {
                insertId = keyHolder.getKey().longValue();
            }
        } catch(Exception e) {
            log.severe(e.toString());
            log.severe(Arrays.toString(e.getStackTrace()));
        }
        
        log.info("Query execution ... COMPLETED");

        return insertId;
    }

    public void update(String query) {
        log.info("Query execution ... STARTED");
        log.info("Query: "+ query);

        jdbcTemplate.update(query);

        log.info("Query execution ... COMPLETED");
    }

    public void update(String table, String tablekey, Long tablekeyval, Map<String, Object> data) {
        String query = "UPDATE "+ table +" SET ";

        String[] temp = new String[data.size()];
        Integer index = 0;

        for(Map.Entry<String, Object> dataentry: data.entrySet()) {
            String columnname = dataentry.getKey();
            temp[index] = columnname+"=?";
            index++;
        }

        query += String.join(", ", temp)+" ";
        query += "WHERE "+tablekey+"=?";

        log.info("Query execution ... STARTED");
        log.info("Query: "+ query);
        log.info("Params: "+ data.toString());

        jdbcTemplate.update(query, new PreparedStatementSetter() {
        
            @Override
            public void setValues(PreparedStatement ps) {
                try{
                    Integer index = 1;
                    for(Map.Entry<String, Object> dEntry: data.entrySet()) {
                        Object columnvalue = dEntry.getValue();

                        if(columnvalue == null) {
                            ps.setNull(index, Types.INTEGER);
                        } else {
                            ps.setObject(index, columnvalue);
                        }

                        index++;
                    }

                    ps.setObject(index, tablekeyval);
                } catch(Exception e) {
                    log.severe(e.toString());
                    log.severe(Arrays.toString(e.getStackTrace()));
                }
            }
        });

        log.info("Query execution ... COMPLETED");
    }

    public List<Map<String, Object>> select(String table, Map<String, Object> criteria) {
        String[] values = new String[criteria.size()];
        Integer index = 0;
        Object[] params = new Object[criteria.size()];

        for(Map.Entry<String, Object> dataentry: criteria.entrySet()) {
            String columnname = dataentry.getKey();
            values[index] = columnname+" = ? ";
            params[index] = dataentry.getValue();
            index++;
        }

        String query = "SELECT * FROM "+ table + " WHERE "+String.join(" AND ", values);

        log.info("Query execution ... STARTED");
        log.info("Query: "+ query);
        log.info("Params: "+ Arrays.toString(params));

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, params);
        log.info("Query execution ... COMPLETED");
        return rows;
    }
}
