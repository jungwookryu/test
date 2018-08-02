package com.ht.connected.home.backend.client.home;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ht.connected.home.backend.client.user.User;

public interface HomeService{

    Page getHomeList(Pageable pageable, String nickname);
    
    List<Home> getHomeListByEmail(String authUserEmail, String nickname);

    Home createHome(Home requestHome);

    Home findOne(int no);

    boolean shareHome(String mode, Home originHome, User user);
    
    
}
