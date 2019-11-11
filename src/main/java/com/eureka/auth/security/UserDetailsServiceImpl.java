package com.eureka.auth.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.Timetable1.EditTimetable;
import com.example.Timetable1.repository.EditTimetableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service(value = "userDetailsService")   // It has to be annotated with @Service.
public class UserDetailsServiceImpl implements UserDetailsService  {

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		// hard coding the users. All passwords must be encoded.
//		final List<AppUser> users = Arrays.asList(
//			new AppUser(1, "omar", encoder.encode("12345"), "USER"),

//			new AppUser(2, "admin", encoder.encode("12345"), "ADMIN")
//		);

		final List<EditTimetable> users = getAllTeachers();

		for(EditTimetable user: users) {
			if(user.getUsername().equals(username)) {
				
				// Remember that Spring needs roles to be in this format: "ROLE_" + userRole (i.e. "ROLE_ADMIN")
				// So, we need to set it to that format, so we can verify and compare roles (i.e. hasRole("ADMIN")).
				List<GrantedAuthority> grantedAuthorities = AuthorityUtils
		                	.commaSeparatedStringToAuthorityList("ROLE_" + user.getRole());
				
				// The "User" class is provided by Spring and represents a model class for user to be returned by UserDetailsService
				// And used by auth manager to verify and check user authentication.
				return new User(user.getUsername(), user.getPassword(), grantedAuthorities);
			}
		}
		
		// If user not found. Throw this exception.
		throw new UsernameNotFoundException("Username: " + username + " not found");
	}

	public ArrayList<EditTimetable> getAllTeachers() {
		try {
			RestTemplate rest = new RestTemplate();

			return rest.exchange("http://localhost:8081/teachers",
					HttpMethod.GET, null, new ParameterizedTypeReference<ArrayList<EditTimetable>>() {
					}
					)
					.getBody();
		} catch (Exception e) {
			System.err.println("Exception in TacoCloudClient: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	// A (temporary) class represent the user saved in the database.
//	private static class AppUser {
//		private Integer id;
//	    private String username, password;
//	    private String role;
//
//		public AppUser(Integer id, String username, String password, String role) {
//	    	this.id = id;
//	    	this.username = username;
//	    	this.password = password;
//	    	this.role = role;
//	    }
//
//		public Integer getId() {
//			return id;
//		}
//
//		public void setId(Integer id) {
//			this.id = id;
//		}
//
//		public String getUsername() {
//			return username;
//		}
//
//		public void setUsername(String username) {
//			this.username = username;
//		}
//
//		public String getPassword() {
//			return password;
//		}
//
//		public void setPassword(String password) {
//			this.password = password;
//		}
//	    public String getRole() {
//			return role;
//		}
//
//		public void setRole(String role) {
//			this.role = role;
//		}
//	}
}