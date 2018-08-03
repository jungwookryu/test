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

import com.ht.connected.home.backend.client.home.sharehome.ShareHome;
import com.ht.connected.home.backend.client.home.sharehome.ShareHomeRepository;
import com.ht.connected.home.backend.client.user.User;
import com.ht.connected.home.backend.client.user.UserService;
import com.ht.connected.home.backend.common.Common;

@Service
public class HomeServiceImpl implements HomeService {

	Logger logger = LoggerFactory.getLogger(HomeServiceImpl.class);

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

		if (Common.empty(nickname)) {
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
	public boolean shareHome(String mode, Home originHome, User user) {
		boolean bShare = false;
		// 공유
		if (Share.share.name().equals(mode)) {
			ShareHome shareHome = new ShareHome(originHome.getNo(), user.getNo(), Share.share.name(),
					Status.request.name());
			shareHomeRepository.save(shareHome);
			bShare = true;
		}
		// 공유해제
		if (Share.shareRemove.name().equals(mode)) {
			shareHomeRepository.deleteByHomeNoAndUserNo(originHome.getNo(), user.getNo());
			bShare = true;
		}
		// 마스터 변경
		if (Share.masterModify.name().equals(mode)) {
			User originUser = userService.getUser(originHome.getOwnerUserEmail());
			if (!Objects.isNull(user)) {
				// Home 정보의 마스터 이메일 변경
				originHome.setOwnerUserEmail(user.getUserEmail());
				homeRepository.saveAndFlush(originHome);
				// master를 share로 변경
				ShareHome originShareHome = shareHomeRepository.findByUserNoAndHomeNo(originHome.getOwnerUserNo(),
						originHome.getNo());
				originShareHome.setRole(ShareRole.share.name());
				shareHomeRepository.save(originShareHome);
				// share를 master로 변경
				ShareHome shareUserGateway = shareHomeRepository.findByUserNoAndHomeNo(originHome.getNo(),
						user.getNo());
				shareUserGateway.setRole(ShareRole.master.name());
				shareHomeRepository.save(shareUserGateway);
			}
			bShare = true;
		}
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
