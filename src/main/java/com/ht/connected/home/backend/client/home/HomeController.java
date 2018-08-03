package com.ht.connected.home.backend.client.home;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ht.connected.home.backend.client.home.HomeServiceImpl.Share;
import com.ht.connected.home.backend.client.home.HomeServiceImpl.ShareRole;
import com.ht.connected.home.backend.client.home.HomeServiceImpl.Status;
import com.ht.connected.home.backend.client.home.sharehome.ShareHome;
import com.ht.connected.home.backend.client.user.User;
import com.ht.connected.home.backend.client.user.UserService;
import com.ht.connected.home.backend.common.Common;
import com.ht.connected.home.backend.controller.rest.CommonController;

/**
 * Home(호스트)관련 요청 처리
 * @author ijlee
 */
@RestController
@RequestMapping("/home")
public class HomeController extends CommonController {

    HomeService homeService;
    UserService userService;

    @Autowired
    public HomeController(HomeService homeService,
            UserService userService) {
        this.homeService = homeService;
        this.userService = userService;
    }

    /**
     * 로그인한 사용자가 등록한 모든 Home를 조회한다.
     * @param req
     * @return
     * @throws Exception
     */
    @GetMapping
    public ResponseEntity<HashMap<String, Object>> getHomeList(@RequestParam(value = "nickname", required = false) String nickname)
            throws Exception {
        String authUserEmail = getAuthUserEmail();
        List<Home> homes = homeService.getHomeListByEmail(authUserEmail, nickname);
        Home rtnHome = new Home();
        rtnHome.setHomes(homes);
        return new ResponseEntity(rtnHome, HttpStatus.OK);
    }
    
    /**
     * 
     * @param map
     * @return
     */
    @PutMapping
    public ResponseEntity shareHome(@RequestBody HashMap map) {
        String userEmail = getAuthUserEmail();
        int no = (int) map.getOrDefault("home_no",-1);
        String share_user_email = (String) map.getOrDefault("share_user_email","");
        String mode = (String) map.getOrDefault("mode","");
        Home originHome = homeService.findOne(no);
        User user =userService.getUser(share_user_email);
        if(Common.empty(share_user_email) ||
                (-1 == no) || Objects.isNull(originHome) || 
                (!userEmail.equals(originHome.getOwnerUserEmail())) ||
            (Objects.isNull(user))) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        boolean rtnShareHome = false;
		// 공유
		if (Share.share.name().equals(mode)) {
			rtnShareHome = homeService.shareHome(originHome, user);
		}
		// 공유해제
		if (Share.shareRemove.name().equals(mode)) {
			rtnShareHome = homeService.shareRemoveHome(originHome, user);
		}
		// 마스터 변경
		if (Share.masterModify.name().equals(mode)) {
			rtnShareHome = homeService.masterModifyHome(originHome, user);
		}
        if(rtnShareHome) {
            return new ResponseEntity<>(rtnShareHome,HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

    }
    
    /**
     * 로그인한 사용자가 등록한 모든 Home를 조회한다.
     * @param req
     * @return
     * @throws Exception
     */
    @PostMapping
    public ResponseEntity<HashMap<String, Object>> registHome(@RequestBody Home requestHome) throws Exception {
        String authUserEmail = getAuthUserEmail();
        Home rtnHome = homeService.createHome(requestHome);
        return new ResponseEntity(rtnHome, HttpStatus.OK);
    }    
    
}
