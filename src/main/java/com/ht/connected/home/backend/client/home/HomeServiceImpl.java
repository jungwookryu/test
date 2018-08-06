package com.ht.connected.home.backend.client.home;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ht.connected.home.backend.client.home.sharehome.ShareHome;
import com.ht.connected.home.backend.client.home.sharehome.ShareHomeRepository;
import com.ht.connected.home.backend.client.user.User;
import com.ht.connected.home.backend.client.user.UserService;

@Service
public class HomeServiceImpl implements HomeService {

	private final static Logger logger = LoggerFactory.getLogger(HomeServiceImpl.class);

	@Autowired
	HomeRepository homeRepository;

	@Autowired
	UserService userService;

	@Autowired
	ShareHomeRepository shareHomeRepository;

	enum Share {
		share, shareRemove, masterModify
	}

	public static enum ShareRole {
		master, share, general
	}

	public enum Status {
		accept, request
	}

	@Override
	public Page getHomeList(Pageable pageable, String nickname) {
		Home home = new Home();
		home.setNickname(nickname);
		Page<Home> page = homeRepository.findAll(Example.of(home), pageable);
		return page;
	}

	@Override
	public List<Home> getHomeListByEmail(String authUserEmail) {

		return getHomeListByEmail(authUserEmail, "");
	}

	@Override
	public List<Home> getHomeListByEmail(String authUserEmail, String nickname) {

		if (Objects.isNull(nickname)) {
			nickname = "";
		}
		return getHomeListByEmail(authUserEmail, nickname, "");
	}

	@Override
	public List<Home> getHomeListByEmail(String authUserEmail, String nickname, String role) {

		User user = userService.getUser(authUserEmail);
		List<Home> homes = new ArrayList();
		List<ShareHome> shareHomes = shareHomeRepository.findByUserNoAndRoleContaining(user.getNo(), role);

		if (shareHomes.size() > 0) {
			List<Integer> nos = new ArrayList();

			shareHomes.forEach(ShareHome -> nos.add(ShareHome.getHomeNo()));
			homes = homeRepository.findByNoInAndNicknameContaining(nos, nickname);
			
			for (Home home : homes) {
				shareHomes.forEach(ShareHome -> {
					if(ShareHome.getHomeNo()==home.getNo() && ShareHome.getUserNo()==user.getNo()) {
						home.setRole(ShareHome.getRole());
					}
				});
			}
		}
		return homes;
	}

	@Override
	public Home createHome(Home requestHome) {

		Home saveHome = homeRepository.save(requestHome);
		return saveHome;
	}

	@Override
	public Home findOne(int no) {

		return homeRepository.findOne(no);

	}

	@Override
	public boolean shareHome(Home originHome, User user) {
		boolean bShare = false;
		ShareHome shareHome = new ShareHome(originHome.getNo(), user.getNo(), Share.share.name(),
				Status.request.name());
		shareHomeRepository.save(shareHome);
		bShare = true;
		return bShare;
	}

	@Override
	@Transactional
	public boolean masterModifyHome(Home originHome, User user) {

		boolean bShare = false;
		if (!Objects.isNull(user)) {
			// master를 share로 변경
			ShareHome originShareHome = shareHomeRepository.findByUserNoAndHomeNo(originHome.getOwnerUserNo(),
					originHome.getNo());
			if(Objects.isNull(originShareHome)) {
				originShareHome = new ShareHome(originHome.getNo(), originHome.getOwnerUserNo(), ShareRole.share.name(),Status.request.name());
				shareHomeRepository.save(originShareHome);
			}else {
				shareHomeRepository.setModifyStatusForNo(ShareRole.share.name(), originShareHome.getNo());
			}
			// share를 master로 변경
			ShareHome shareShareHome = shareHomeRepository.findByUserNoAndHomeNo(user.getNo(),originHome.getNo());
			if(Objects.isNull(shareShareHome)) {
				shareShareHome = new ShareHome(originHome.getNo(), user.getNo(), ShareRole.master.name(),Status.request.name());
				shareHomeRepository.save(originShareHome);
			}else {
				shareHomeRepository.setModifyStatusForNo(ShareRole.master.name(), shareShareHome.getNo());
			}
			
			// Home 정보의 마스터 이메일 변경
			originHome.setOwnerUserEmail(user.getUserEmail());
			originHome.setOwnerUserNo(user.getNo());
			originHome.setOwnerUserAor(user.getUserAor());
			homeRepository.save(originHome);
		}
		bShare = true;
		return bShare;

	}
	
	@Override
	public boolean shareRemoveHome(Home originHome, User user) {
		boolean bShare = false;
		shareHomeRepository.deleteByHomeNoAndUserNo(originHome.getNo(), user.getNo());
		bShare = true;
		return bShare;
	}
	@Override
	public Home getHomeByUserInfo(int userNo) {
		Home rtnHome = homeRepository.findByOwnerUserNo(userNo);
		if (Objects.isNull(rtnHome)) {
			return new Home();
		}
		return rtnHome;
	}

}
