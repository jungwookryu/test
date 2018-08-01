package com.ht.connected.home.backend.user;

public interface UserService {
	User getUser(String userEmail);
	Boolean getExistUser(String userEmail);
	User modify(int no,User user);
	User register(User users);
	
}
