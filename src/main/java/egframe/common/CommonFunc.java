package egframe.common;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.jdbc.core.CallableStatementCallback;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import egframe.iteach4u.entity.exm_mat_make_hd;
import egframe.iteach4u.entity.exm_mat_make_list;
import egframe.iteach4u.entity.exm_mat_unit_hd;
import egframe.iteach4u.entity.exm_mat_unit_row;
import egframe.iteach4u.entity.exm_mat_unit_view;

public class CommonFunc  {
	
	public CommonFunc() {
		
	}
	public String convertListToJson(exm_mat_make_hd viewdata2) {
	    ObjectMapper objectMapper = new ObjectMapper();
	    objectMapper.registerModule(new JavaTimeModule());
	    
	    try {
	    	System.out.println("Java 객체로 변환된 내용 확인: " + viewdata2.getClass().getName());
	        System.out.println("변환 전 객체 내용: ");
	        System.out.println(viewdata2);  // 객체 자체를 출력
	        String jsonString = objectMapper.writeValueAsString(viewdata2);
	        System.out.println("JSON 변환 결과 (중간 결과 확인): ");
	        System.out.println(jsonString);  // 변환된 JSON 결과
	        return jsonString;
	    } catch (JsonProcessingException e) {
	        e.printStackTrace();
	        return "Error processing JSON";  // 반환할 오류 메시지 혹은 null 처리
	    }
	}	
	public String convertListToJson(exm_mat_unit_view viewdata2) {
	    ObjectMapper objectMapper = new ObjectMapper();
	    objectMapper.registerModule(new JavaTimeModule());
	    
	    try {
	    	System.out.println("Java 객체로 변환된 내용 확인: " + viewdata2.getClass().getName());
	        System.out.println("변환 전 객체 내용: ");
	        System.out.println(viewdata2);  // 객체 자체를 출력
	        String jsonString = objectMapper.writeValueAsString(viewdata2);
	        System.out.println("JSON 변환 결과 (중간 결과 확인): ");
	        System.out.println(jsonString);  // 변환된 JSON 결과
	        return jsonString;
	    } catch (JsonProcessingException e) {
	        e.printStackTrace();
	        return "Error processing JSON";  // 반환할 오류 메시지 혹은 null 처리
	    }
	}	
	public String convertListToJson(exm_mat_make_list viewdata2) {
	    ObjectMapper objectMapper = new ObjectMapper();
	    objectMapper.registerModule(new JavaTimeModule());
	    
	    try {
	    	System.out.println("Java 객체로 변환된 내용 확인: " + viewdata2.getClass().getName());
	        System.out.println("변환 전 객체 내용: ");
	        System.out.println(viewdata2);  // 객체 자체를 출력
	        String jsonString = objectMapper.writeValueAsString(viewdata2);
	        System.out.println("JSON 변환 결과 (중간 결과 확인): ");
	        System.out.println(jsonString);  // 변환된 JSON 결과
	        return jsonString;
	    } catch (JsonProcessingException e) {
	        e.printStackTrace();
	        return "Error processing JSON";  // 반환할 오류 메시지 혹은 null 처리
	    }
	}	
	public String convertListToJson(exm_mat_unit_hd hddata) {
	    ObjectMapper objectMapper = new ObjectMapper();
	    objectMapper.registerModule(new JavaTimeModule());
	    
	    try {
	    	System.out.println("Java 객체로 변환된 내용 확인: " + hddata.getClass().getName());
	        System.out.println("변환 전 객체 내용: ");
	        System.out.println(hddata);  // 객체 자체를 출력
	        String jsonString = objectMapper.writeValueAsString(hddata);
	        System.out.println("JSON 변환 결과 (중간 결과 확인): ");
	        System.out.println(jsonString);  // 변환된 JSON 결과
	        return jsonString;
	    } catch (JsonProcessingException e) {
	        e.printStackTrace();
	        return "Error processing JSON";  // 반환할 오류 메시지 혹은 null 처리
	    }
	}	
	public String convertListToJson(exm_mat_unit_row rowdata) {
	    ObjectMapper objectMapper = new ObjectMapper();
	    objectMapper.registerModule(new JavaTimeModule());
	    
	    try {
	    	System.out.println("Java 객체로 변환된 내용 확인: " + rowdata.getClass().getName());
	        System.out.println("변환 전 객체 내용: ");
	        System.out.println(rowdata);  // 객체 자체를 출력
	        String jsonString = objectMapper.writeValueAsString(rowdata);
	        System.out.println("JSON 변환 결과 (중간 결과 확인): ");
	        System.out.println(jsonString);  // 변환된 JSON 결과
	        return jsonString;
	    } catch (JsonProcessingException e) {
	        e.printStackTrace();
	        return "Error processing JSON";  // 반환할 오류 메시지 혹은 null 처리
	    }
	}	
	public static List<Long> pickRandomElements(List<Long> list, Long count) {
        if (list == null || list.isEmpty() || count <= 0) {
            return Collections.emptyList();
        }
        
        List<Long> copy = new ArrayList<>(list);
        Collections.shuffle(copy);
        return copy.subList(0, Math.min(count.intValue(), copy.size()));
    }
	
}