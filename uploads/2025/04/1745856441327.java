package egframe.data.service;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.vaadin.flow.component.grid.editor.Editor;
import egframe.data.entitycontrol.sys_blank;
import egframe.data.entitycontrol.sys_column_dic;
import egframe.data.entitycontrol.sys_control_type;
import egframe.data.entitycontrol.sys_dw_column;
import egframe.data.entitycontrol.sys_module;
import egframe.data.entitycontrol.sys_program_create;
import egframe.data.entitycontrol.sys_program_entity;
import egframe.data.entitycontrol.sys_program_entity_column;
import egframe.data.entitycontrol.sys_program_entity_control;
import egframe.data.entitycontrol.sys_program_free_control;
import egframe.data.entitycontrol.sys_program_grid_control;
import egframe.data.entitycontrol.sys_program_search;
import egframe.data.entitycontrol.sys_program_search_control;
import egframe.data.entitycontrol.sys_program_tab;
import egframe.data.entitycontrol.sys_program_type;
import egframe.data.entitycontrol.sys_user;
import egframe.data.entitycontrol.sys_user_menu;
import egframe.data.entitycontrol.sys_control_place_type;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;

import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.checkbox.Checkbox;
//import com.ebsol.ebframe.data.entitycontrol.sys.sys_control_place_type;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.IntegerField;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
@JsonIgnoreProperties(ignoreUnknown = true)
@Service
public class gridDBO implements Serializable{ 
	public   JdbcTemplate con = new JdbcTemplate();
	public   DataSource ds = null;
	ResultSet rs = null;
	PreparedStatement preparedStatement= null;
	Connection connection = null;
	SysDBO sysdbo;
	@Autowired
  	public gridDBO() {
	}
  	public gridDBO(SysDBO sys) {
  		ds = sys.sys_ds;
  		con = sys.sys_con;
	}
	public void setDS(DataSource sys_ds) {
		ds = sys_ds;
	}
	public void setCon(JdbcTemplate sys_con) {
		con = sys_con;
	}
	public String convertListToJson(Object programList) {
	    ObjectMapper objectMapper = new ObjectMapper();
	    objectMapper.registerModule(new JavaTimeModule());
	    try {
	        return objectMapper.writeValueAsString(programList);
	    } catch (JsonProcessingException e) {
	        e.printStackTrace();
	        return null;
	    }
	}	
   public void addProcedure(String tablenm) {

        try  {
        	connection = con.getDataSource().getConnection();
            String tableName = tablenm;
            String additionalCondition = "tablenm = '"+tablenm+"'";
            String additionalCode = "SELECT "+tablenm+"_delete(json_array) INTO result_text;";
            deleteProcedure(connection, tablenm, tablenm+"_delete", "json_array");
            
            updateProcedure(connection, tablenm, tablenm+"_update", tablenm+"_insert", "json_array");

            System.out.println("Code updated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void deleteProcedure(Connection connection, String tableName, String functionName, String parameterName) throws Exception {
        String existingCode = fetchExistingCode(connection, "table_delete");

        String additionalCondition = "tablenm = '" + tableName + "'";
        String additionalCode = "SELECT " + functionName + "(" + parameterName + ") INTO result_text;";
        String updatedCode = addConditionAndCode(existingCode, additionalCondition, additionalCode);

        updateCodeInDatabase(connection, "table_delete", updatedCode);
    }
    
    private static void updateProcedure(Connection connection, String tableName, String updatefunctionName,String insertfunctionName, String parameterName) throws Exception {
        String existingCode = fetchExistingCode(connection, "table_update");

        String additionalCondition = "tablenm = '" + tableName + "'";
        String additionalCodeU = "SELECT " + updatefunctionName + "(" + parameterName + ") INTO result_text;";
        String additionalCodeI = "SELECT " + insertfunctionName + "(" + parameterName + ") INTO result_text;";
         String updatedCode = addConditionAndCode(existingCode, additionalCondition,tableName, additionalCodeU,additionalCodeI);

        updateCodeInDatabase(connection, "table_update", updatedCode);
        
        
    }

    private static String fetchExistingCode(Connection connection, String functionName) throws Exception {
        String sql = "SELECT prosrc FROM pg_proc WHERE proname = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, functionName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("prosrc");
                } else {
                    throw new RuntimeException("Stored procedure not found: " + functionName);
                }
            }
        }
    }
    private static String addConditionAndCode(String existingCode, String additionalCondition, String additionalCode) {
        if (!additionalCondition.isEmpty() && !existingCode.contains(additionalCondition)) {
            int index = existingCode.indexOf("BEGIN");
            if (index != -1) {
                StringBuilder updatedCode = new StringBuilder(existingCode);
                updatedCode.insert(index + 6, "\nIF " + additionalCondition + " THEN\n" + additionalCode + "\nEND IF;\n");
                return updatedCode.toString();
            } else {
                return existingCode + "\nIF " + additionalCondition + " THEN\n" + additionalCode + "\nEND IF;\n";
            }
        } else {
            return existingCode;
        }
    }
    private static String addConditionAndCode(String existingCode, String additionalCondition, String tablenm,String additionalCodeU, String additionalCodeI) {
        // If additionalCondition is not empty and does not exist, insert the new condition and code
        if (!additionalCondition.isEmpty() && !existingCode.contains(additionalCondition)) {
            // Find the position to insert the new condition and code
            int index = existingCode.indexOf("BEGIN");

            // If "BEGIN" is found, insert the new condition and code
            if (index != -1) {
                StringBuilder updatedCode = new StringBuilder(existingCode);
                int idx = existingCode.indexOf("IF modi = 'U' THEN");
                if (idx != -1) {
                	//updatedCode.insert(index + "IF modi = 'U'::varchar THEN".length(), "\n" + additionalCodeI + "\n");
                	updatedCode.insert(idx + "IF modi = 'U' THEN".length(), "\n" +
                            "    IF tablenm = '"+tablenm+"' THEN\n" +
                            "        " + additionalCodeU + "\n" +
                            "    END IF;\n");
                }

                int i = updatedCode.indexOf("IF modi = 'I' THEN");
                if (i != -1) {
                	updatedCode.insert(i + "IF modi = 'I' THEN".length(), "\n" +
                            "    IF tablenm = '"+tablenm+"' THEN\n" +
                            "        " + additionalCodeI + "\n" +
                            "    END IF;\n");
                }
                //updatedCode.append("END IF;\n");
                return updatedCode.toString();
            } else {
                // If "BEGIN" is not found, just append the new condition and code at the end
                return existingCode + "\nIF " + additionalCondition + " THEN\n" +
                       "    IF modi = 'U'::varchar THEN\n" +
                       "        " + additionalCodeU + "\n" +
                       "    END IF;\n" +
                       "    IF modi = 'I' THEN\n" +
                       "        " + additionalCodeI + "\n" +
                       "    END IF;\n" +
                       "END IF;\n";
            }
        } else {
            // If additionalCondition is empty or already exists, return the existing code as is
            return existingCode;
        }
    }

    
    private static void updateCodeInDatabase(Connection connection, String functionName, String updatedCode) throws Exception {
        String sql = "UPDATE pg_proc SET prosrc = ? WHERE proname = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, updatedCode);
            statement.setString(2, functionName);
            statement.executeUpdate();
        }
    }	

	public void insert_dynamic(String tablenm,List<Object> ins) throws SQLException {
		preparedStatement = null;
	    rs = null;
	    connection = con.getDataSource().getConnection(); ;
		try {
			for (Object row : ins) {
				String json = convertListToJson(row);
				try {
					ObjectMapper objectMapper = new ObjectMapper();
					JsonNode jsonNode = objectMapper.readTree(json);
					String modi = jsonNode.get("modify").asText();
		
					if (modi.equals("I")) {
					    String functionName = "insert_data_dynamic";
					    String tableName = tablenm;  // 대상 테이블명
					    //String jsonString = jsonNode.toString();  // JSON 객체를 문자열로 변환
		
					    // PostgreSQL에서 함수 호출 예시
					    String callFunctionQuery = "SELECT insert_data_dynamic(?::varchar, ?::json)";
					    preparedStatement = connection.prepareStatement(callFunctionQuery);
				        preparedStatement.setString(1, tablenm);
				        preparedStatement.setString(2, json);

					    rs = preparedStatement.executeQuery();
		
					    if (rs.next()) {
					        String result = rs.getString(1);
					        System.out.println("결과: " + result);
					    }
					}

				} catch (Exception e) {
					e.printStackTrace();
				}			        

			}
		} finally {
		    try {
		        if (rs != null) {
		            rs.close();
		        }
		        if (preparedStatement != null) {
		        	preparedStatement.close();
		        }
		        if (connection != null) {
		            connection.close();
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		}
	}
	public void update_dynamic(String tablenm,List<Object> del, List<Object> ins) throws SQLException {
		preparedStatement = null;
	    rs = null;
	    connection = con.getDataSource().getConnection(); ;
		try {
			for (Object row : ins) {
				String json = convertListToJson(row);
				try {
					ObjectMapper objectMapper = new ObjectMapper();
					JsonNode jsonNode = objectMapper.readTree(json);
					String modi = jsonNode.get("modify").asText();
		
					if (modi.equals("U")) {
					    String functionName = "insert_data_dynamic";
					    String tableName = tablenm;  // 대상 테이블명
					    //String jsonString = jsonNode.toString();  // JSON 객체를 문자열로 변환
		
					    // PostgreSQL에서 함수 호출 예시
					    String callFunctionQuery = "SELECT update_data_dynamic(?::varchar, ?::json)";
					    preparedStatement = connection.prepareStatement(callFunctionQuery);
				        preparedStatement.setString(1, tablenm);
				        preparedStatement.setString(2, json);

					    rs = preparedStatement.executeQuery();
		
					    if (rs.next()) {
					        String result = rs.getString(1);
					        System.out.println("결과: " + result);
					    }
					}

				} catch (Exception e) {
					e.printStackTrace();
				}			        

			}
		} finally {
		    try {
		        if (rs != null) {
		            rs.close();
		        }
		        if (preparedStatement != null) {
		        	preparedStatement.close();
		        }
		        if (connection != null) {
		            connection.close();
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		}
	}
    
	public void update(String tablenm,List<Object> org, List<Object> ups) throws SQLException {
		preparedStatement = null;
	    rs = null;
	    connection = con.getDataSource().getConnection(); ;
		if(org.size()!=ups.size()) {
			return ;
		}
		try {
			int cnt = org.size();
			for (int i=0 ; cnt > i ;i++) {
				Object row = ups.get(i);
				Object orgrow = org.get(i);
				String upsjson = convertListToJson(row);
				String orgjson = convertListToJson(orgrow);
				try {
					ObjectMapper objectMapper = new ObjectMapper();
					JsonNode jsonNode = objectMapper.readTree(upsjson);
					String modi = jsonNode.get("modify").asText();
		
					if (modi.equals("U")) {
					    String functionName = "insert_data_dynamic";
					    String tableName = tablenm;  // 대상 테이블명
					    //String jsonString = jsonNode.toString();  // JSON 객체를 문자열로 변환
		
					    // PostgreSQL에서 함수 호출 예시
					    String callFunctionQuery = "SELECT delete_data_dynamic(?::varchar, ?::json)";
					    preparedStatement = connection.prepareStatement(callFunctionQuery);
				        preparedStatement.setString(1, tablenm);
				        preparedStatement.setString(2, orgjson);

					    rs = preparedStatement.executeQuery();
		
					    if (rs.next()) {
					        String result = rs.getString(1);
					        System.out.println(" delete 결과: " + result);
					    }
					    callFunctionQuery = "SELECT insert_data_dynamic(?::varchar, ?::json)";
					    preparedStatement = connection.prepareStatement(callFunctionQuery);
				        preparedStatement.setString(1, tablenm);
				        preparedStatement.setString(2, upsjson);

					    rs = preparedStatement.executeQuery();
		
					    if (rs.next()) {
					        String result = rs.getString(1);
					        System.out.println("insert 결과: " + result);
					    }
					}else if(modi.equals("I")){
					    String functionName = "insert_data_dynamic";
					    String tableName = tablenm;  // 대상 테이블명
					    //String jsonString = jsonNode.toString();  // JSON 객체를 문자열로 변환
		
					    // PostgreSQL에서 함수 호출 예시
					    String callFunctionQuery = "SELECT insert_data_dynamic(?::varchar, ?::json)";
					    preparedStatement = connection.prepareStatement(callFunctionQuery);
				        preparedStatement.setString(1, tablenm);
				        preparedStatement.setString(2, upsjson);

					    rs = preparedStatement.executeQuery();
		
					    if (rs.next()) {
					        String result = rs.getString(1);
					        System.out.println(" delete 결과: " + result);
					    }
						
					}

				} catch (Exception e) {
					e.printStackTrace();
				}			        

			}
		} finally {
		    try {
		        if (rs != null) {
		            rs.close();
		        }
		        if (preparedStatement != null) {
		        	preparedStatement.close();
		        }
		        if (connection != null) {
		            connection.close();
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		}
	}
	public void delete_dynamic(String tablenm,List<Object> del, List<Object> ins) throws SQLException {
		preparedStatement = null;
	    rs = null;
	    connection = con.getDataSource().getConnection(); ;
		try {
			for (Object row : del) {
				String json = convertListToJson(row);
				try {
					ObjectMapper objectMapper = new ObjectMapper();
					JsonNode jsonNode = objectMapper.readTree(json);
					//String modi = jsonNode.get("modify").asText();
		
					    String functionName = "delete_data_dynamic";
					    String tableName = tablenm;  // 대상 테이블명
					    //String jsonString = jsonNode.toString();  // JSON 객체를 문자열로 변환
		
					    // PostgreSQL에서 함수 호출 예시
					    String callFunctionQuery = "SELECT delete_data_dynamic(?::varchar, ?::json)";
					    preparedStatement = connection.prepareStatement(callFunctionQuery);
				        preparedStatement.setString(1, tablenm);
				        preparedStatement.setString(2, json);

					    rs = preparedStatement.executeQuery();
		
					    if (rs.next()) {
					        String result = rs.getString(1);
					        System.out.println("결과: " + result);
					    }

				} catch (Exception e) {
					e.printStackTrace();
				}			        

			}
		} finally {
		    try {
		        if (rs != null) {
		            rs.close();
		        }
		        if (preparedStatement != null) {
		        	preparedStatement.close();
		        }
		        if (connection != null) {
		            connection.close();
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		}
	}
	public void delete_dynamic(String tablenm,List<Object> del) throws SQLException {
		preparedStatement = null;
	    rs = null;
	    connection = con.getDataSource().getConnection(); ;
		try {
			for (Object row : del) {
				String json = convertListToJson(row);
				try {
					ObjectMapper objectMapper = new ObjectMapper();
					JsonNode jsonNode = objectMapper.readTree(json);
					//String modi = jsonNode.get("modify").asText();
		
					    String functionName = "delete_data_dynamic";
					    String tableName = tablenm;  // 대상 테이블명
					    //String jsonString = jsonNode.toString();  // JSON 객체를 문자열로 변환
		
					    // PostgreSQL에서 함수 호출 예시
					    String callFunctionQuery = "SELECT delete_data_dynamic(?::varchar, ?::json)";
					    preparedStatement = connection.prepareStatement(callFunctionQuery);
				        preparedStatement.setString(1, tablenm);
				        preparedStatement.setString(2, json);

					    rs = preparedStatement.executeQuery();
		
					    if (rs.next()) {
					        String result = rs.getString(1);
					        System.out.println("결과: " + result);
					    }

				} catch (Exception e) {
					e.printStackTrace();
				}			        

			}
		} finally {
		    try {
		        if (rs != null) {
		            rs.close();
		        }
		        if (preparedStatement != null) {
		        	preparedStatement.close();
		        }
		        if (connection != null) {
		            connection.close();
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		}
	}
    
}