package sqlartifact;

import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@CrossOrigin
@Controller
public class ApiController {
	@RequestMapping("/test")
	public String Abc() {
		return "success";
	}
	
	@RequestMapping(value="/create", 
			produces = MediaType.APPLICATION_JSON_VALUE, 
			method = RequestMethod.POST)
	@ResponseBody
	public String Create(@RequestParam("name") String name, 
			@RequestParam("subject1") String subject1, 
			@RequestParam("subject2") String subject2, 
			@RequestParam("subject3") String subject3 ) {
		DataSource ds;
		Connection con;
		JsonObjectBuilder res = Json.createObjectBuilder();
		try {
			Context ic = new InitialContext();
	         ds = (DataSource) ic.lookup("java:comp/env/jdbc/jit");
	         con = ds.getConnection();
	         PreparedStatement pstmt = null;
	         String query = null;
	         query = "INSERT INTO user_details(`name`, `subject1`, `subject2`,"
	         		+ " `subject3`) VALUES(?, ?, ?, ?)";
	         pstmt = con.prepareStatement(query);
	         pstmt.setString(1, name);
	         pstmt.setString(2, subject1);
	         pstmt.setString(3, subject2);
	         pstmt.setString(4, subject3);
	         int result = pstmt.executeUpdate();
	         System.out.println(result);
	         if(result>0) {
	        	 con.close();
	        	 res = Json.createObjectBuilder()
	        			 .add("status", true)
	        			 .add("message", "success");
	         }
	         else {
	        	 con.close();
	        	 res = Json.createObjectBuilder()
	        			 .add("status", false)
	        			 .add("message", "error");
	         }
	         
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
		JsonObject jsonObject = res.build();
		String jsonString;
			StringWriter writer = new StringWriter();
			Json.createWriter(writer).write(jsonObject);
			jsonString = writer.toString();
			return jsonString;
		
	}
	@RequestMapping(value="/get", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getAll() {
		DataSource ds;
		Connection con;
		JsonObjectBuilder res = Json.createObjectBuilder();
		ArrayList<String> id = new ArrayList<String>();
		ArrayList<String> name = new ArrayList<String>();
		ArrayList<String> subject1 = new ArrayList<String>();
		ArrayList<String> subject2 = new ArrayList<String>();
		ArrayList<String> subject3 = new ArrayList<String>();
		ArrayList<String> average = new ArrayList<String>();
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();
		try {
			Context ic = new InitialContext();
	         ds = (DataSource) ic.lookup("java:comp/env/jdbc/jit");
	         con = ds.getConnection();
	         PreparedStatement pstmt = null;
	         String query = null;
	         query = "SELECT `id`,`name`, `subject1`, `subject2`, `subject3`, "
	         		+ "(subject1+subject2+subject3)/3 as average FROM `user_details`";
	         pstmt = con.prepareStatement(query);
	         ResultSet result = pstmt.executeQuery(query);
	         System.out.println(result);
	         while(result.next()) {
	        	 id.add(result.getString("id"));
	        	 name.add(result.getString("name"));
	        	 subject1.add(result.getString("subject1"));
	        	 subject2.add(result.getString("subject2"));
	        	 subject3.add(result.getString("subject3"));
	        	 average.add(result.getString("average"));
	         }
	         con.close();
	         String nameJson = gson.toJson(name);
	         String idJson = gson.toJson(id);
	         String subject1Json = gson.toJson(subject1);
	         String subject2Json = gson.toJson(subject2);
	         String subject3Json = gson.toJson(subject3);
	         String averageJson = gson.toJson(average);
	         if(result != null)
	        	 res = Json.createObjectBuilder()
	        	 .add("status", true)
	        	 .add("message", "success")
	        	 .add("name", nameJson)
	        	 .add("id", idJson)
	        	 .add("subject1", subject1Json)
	        	 .add("subject2", subject2Json)
	        	 .add("subject3", subject3Json)
	        	 .add("average", averageJson);	        
	         
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
		JsonObject jsonObject = res.build();
		String jsonString;
			StringWriter writer = new StringWriter();
			Json.createWriter(writer).write(jsonObject);
			jsonString = writer.toString();
			return jsonString;
		
	}
	@RequestMapping(value="/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String delete(@PathVariable("id") String id) {
		
		DataSource ds;
		Connection con;
		JsonObjectBuilder res = Json.createObjectBuilder();
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();
		try {
			Context ic = new InitialContext();
	         ds = (DataSource) ic.lookup("java:comp/env/jdbc/jit");
	         con = ds.getConnection();
	         PreparedStatement pstmt = null;
	         String query = null;
	         query = "DELETE FROM `user_details` where `id`="+id;
	         pstmt = con.prepareStatement(query);

	         int result = pstmt.executeUpdate();
	         System.out.println(result);
	         if(result>0) {
	        	 con.close();
	        	 res = Json.createObjectBuilder()
	        			 .add("status", true)
	        			 .add("message", "success");
	         }
	         else {
	        	 con.close();
	        	 res = Json.createObjectBuilder()
	        			 .add("status", true)
	        			 .add("message", "success");
 
	         }
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
		JsonObject jsonObject = res.build();
		String jsonString;
			StringWriter writer = new StringWriter();
			Json.createWriter(writer).write(jsonObject);
			jsonString = writer.toString();
			return jsonString;
		
	}
	@RequestMapping(value="/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String getById(@PathVariable("id") String id) {
		DataSource ds;
		Connection con;
		JsonObjectBuilder res = Json.createObjectBuilder();
		try {
			Context ic = new InitialContext();
	         ds = (DataSource) ic.lookup("java:comp/env/jdbc/jit");
	         con = ds.getConnection();
	         PreparedStatement pstmt = null;
	         String query = null;
	         query = "SELECT * FROM `user_details` where `id`=?";
	         pstmt = con.prepareStatement(query);
	         pstmt.setInt(1,Integer.parseInt(id));
	         ResultSet result = pstmt.executeQuery();
	         System.out.println(result);
	         if(result.next()) {
	        	 res = Json.createObjectBuilder()
	        			 .add("status", true)
	        			 .add("message", "success")
	        	 		 .add("name", result.getString("name"))
	        	 		 .add("subject1", result.getString("subject1"))
	        	 		 .add("subject2", result.getString("subject2"))
	        	 		 .add("subject3", result.getString("subject3"));
	        	 con.close();
	         }
	         else {
	        	 con.close();
	        	 res = Json.createObjectBuilder()
	        			 .add("status", true)
	        			 .add("message", "no data found against corresponding ID");
 
	         }
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
		JsonObject jsonObject = res.build();
		String jsonString;
			StringWriter writer = new StringWriter();
			Json.createWriter(writer).write(jsonObject);
			jsonString = writer.toString();
			return jsonString;
		
	}
	@RequestMapping(value="/update/{id}", 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String update(@PathVariable("id") String id, 
			@RequestParam("name") String name, 
			@RequestParam("subject1") String subject1, 
			@RequestParam("subject2") String subject2, 
			@RequestParam("subject3") String subject3) {
		DataSource ds;
		Connection con;
		JsonObjectBuilder res = Json.createObjectBuilder();
		try {
			Context ic = new InitialContext();
	         ds = (DataSource) ic.lookup("java:comp/env/jdbc/jit");
	         con = ds.getConnection();
	         PreparedStatement pstmt = null;
	         String query = null;
	         query = "UPDATE `user_details` SET  `name`=?, "
	         		+ "`subject1`=?, `subject2`=?, `subject3`=?  where `id`=?";
	         pstmt = con.prepareStatement(query);
	         pstmt.setString(1, name);
	         pstmt.setString(2, subject1);
	         pstmt.setString(3, subject2);
	         pstmt.setString(4, subject3);
	         pstmt.setInt(5,Integer.parseInt(id));
	         int result = pstmt.executeUpdate();
	         System.out.println(result);
	         if(result>0) {
	        	 res = Json.createObjectBuilder()
	        			 .add("status", true)
	        			 .add("message", "success");
	        	 con.close();
	         }
	         else {
	        	 con.close();
	        	 res = Json.createObjectBuilder()
	        			 .add("status", false)
	        			 .add("message", "erro");
 
	         }
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
		JsonObject jsonObject = res.build();
		String jsonString;
			StringWriter writer = new StringWriter();
			Json.createWriter(writer).write(jsonObject);
			jsonString = writer.toString();
			return jsonString;
		
	}
	@RequestMapping(value="/operations", 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String operations( 
			@RequestParam("function") 	String function, 
			@RequestParam("subject") String subject) {
		DataSource ds;
		Connection con;
		JsonObjectBuilder res = Json.createObjectBuilder();
		try {
			Context ic = new InitialContext();
	         ds = (DataSource) ic.lookup("java:comp/env/jdbc/jit");
	         con = ds.getConnection();
	         PreparedStatement pstmt = null;
	         String query = null;
	         if(function.equals("avg"))
	         query = "SELECT AVG("+subject+") as average FROM `user_details`;";
	         else	
	        	 query = "SELECT "+function+"("+subject+") as subject FROM `user_details`";
	         pstmt = con.prepareStatement(query);
	         ResultSet result = pstmt.executeQuery();
	         System.out.println(result);
	         if(result.next()) {
	        	 if(function.equals("avg")) {
	        		 res = Json.createObjectBuilder()
	        				 .add("status", true)
	        				 .add("message", "success")
	        		 		 .add("value",result.getString("average"));
	        		 con.close();
	        		 
	        	 }
	        	 else {
	        		 
	        		 res = Json.createObjectBuilder()
	        				 .add("status", true)
	        				 .add("message", "success")
	        				 .add("value", result.getString("subject"));
	        		 con.close();
	        		 
	        	 }
	         }
	         else {
	        	 con.close();
	        	 res = Json.createObjectBuilder()
	        			 .add("status", false)
	        			 .add("message", "erro");
 
	         }
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
		JsonObject jsonObject = res.build();
		String jsonString;
			StringWriter writer = new StringWriter();
			Json.createWriter(writer).write(jsonObject);
			jsonString = writer.toString();
			return jsonString;
		
	}
	@RequestMapping(value="/five/{select}", 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String TopFive(@PathVariable("select") String select) {
		 System.out.println(select);
		DataSource ds;
		Connection con;
		JsonObjectBuilder res = Json.createObjectBuilder();
		try {
			Context ic = new InitialContext();
	         ds = (DataSource) ic.lookup("java:comp/env/jdbc/jit");
	         con = ds.getConnection();
	         PreparedStatement pstmt = null;
	         String query = null;
	         query = "SELECT * FROM `user_details` ORDER BY `subject1` "+select+", `subject2` "+select+", `subject3`"+select+" LIMIT 5;";
	         pstmt = con.prepareStatement(query);
	         ResultSet result = pstmt.executeQuery();
	         System.out.println(result);
	         ArrayList<String> name = new ArrayList<String>();
	 		ArrayList<String> subject1 = new ArrayList<String>();
	 		ArrayList<String> subject2 = new ArrayList<String>();
	 		ArrayList<String> subject3 = new ArrayList<String>();
	 		GsonBuilder gsonBuilder = new GsonBuilder();
	 		Gson gson = gsonBuilder.create();
	         while(result.next()) {
	        	 name.add(result.getString("name"));
	        	 subject1.add(result.getString("subject1"));
	        	 subject2.add(result.getString("subject2"));
	        	 subject3.add(result.getString("subject3"));
	         }
	         con.close();
	         String nameJson = gson.toJson(name);
	         String subject1Json = gson.toJson(subject1);
	         String subject2Json = gson.toJson(subject2);
	         String subject3Json = gson.toJson(subject3);
	         if(result != null)
	        	 res = Json.createObjectBuilder()
	        	 .add("status", true)
	        	 .add("message", "success")
	        	 .add("name", nameJson)
	        	 .add("subject1", subject1Json)
	        	 .add("subject2", subject2Json)
	        	 .add("subject3", subject3Json);
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
		JsonObject jsonObject = res.build();
		String jsonString;
			StringWriter writer = new StringWriter();
			Json.createWriter(writer).write(jsonObject);
			jsonString = writer.toString();
			return jsonString;
		
	}
	
	@RequestMapping(value="/filters", 
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String Filters( 
			@RequestParam("filter") 	String filter, 
			@RequestParam("subject") String subject,
			@RequestParam("marks") String marks) {
		DataSource ds;
		Connection con;
		JsonObjectBuilder res = Json.createObjectBuilder();
		try {
			Context ic = new InitialContext();
	         ds = (DataSource) ic.lookup("java:comp/env/jdbc/jit");
	         con = ds.getConnection();
	         PreparedStatement pstmt = null;
	         String query = null;
	         if(filter.equals("greater"))
	        	 query = "SELECT * FROM `user_details` WHERE "+subject+">"+marks+";";
	         else if(filter.equals("equals"))
	        	 query = "SELECT * FROM `user_details` WHERE "+subject+"="+marks+";";
	         else
	        	 query = "SELECT * FROM `user_details` WHERE "+subject+"<"+marks+";";
	        ArrayList<String> name = new ArrayList<String>();
		 		ArrayList<String> subject1 = new ArrayList<String>();
		 		ArrayList<String> subject2 = new ArrayList<String>();
		 		ArrayList<String> subject3 = new ArrayList<String>();
		 		GsonBuilder gsonBuilder = new GsonBuilder();
		 		Gson gson = gsonBuilder.create();
		 		 pstmt = con.prepareStatement(query);
		         ResultSet result = pstmt.executeQuery();
		         while(result.next()) {
		        	 name.add(result.getString("name"));
		        	 subject1.add(result.getString("subject1"));
		        	 subject2.add(result.getString("subject2"));
		        	 subject3.add(result.getString("subject3"));
		         }
		         con.close();
		         String nameJson = gson.toJson(name);
		         String subject1Json = gson.toJson(subject1);
		         String subject2Json = gson.toJson(subject2);
		         String subject3Json = gson.toJson(subject3);
		         if(result != null)
		        	 res = Json.createObjectBuilder()
		        	 .add("status", true)
		        	 .add("message", "success")
		        	 .add("name", nameJson)
		        	 .add("subject1", subject1Json)
		        	 .add("subject2", subject2Json)
		        	 .add("subject3", subject3Json);
				}catch(Exception e) {
					System.out.println(e.getMessage());
				}
			JsonObject jsonObject = res.build();
			String jsonString;
				StringWriter writer = new StringWriter();
				Json.createWriter(writer).write(jsonObject);
				jsonString = writer.toString();
				return jsonString;
		
	}
	
}
