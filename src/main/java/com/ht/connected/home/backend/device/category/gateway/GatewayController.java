package com.ht.connected.home.backend.device.category.gateway;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ht.connected.home.backend.client.home.Home;
import com.ht.connected.home.backend.client.home.HomeService;
import com.ht.connected.home.backend.client.user.User;
import com.ht.connected.home.backend.client.user.UserService;
import com.ht.connected.home.backend.common.AuditLogger;
import com.ht.connected.home.backend.controller.rest.CommonController;
import com.ht.connected.home.backend.device.category.zwave.ZWaveController;
import com.ht.connected.home.backend.userGateway.UserGatewayRepository;

/**
 * gateway(호스트)관련 요청 처리
 * @author 구정화
 */
@RestController
@RequestMapping("/gateway")
public class GatewayController extends CommonController {

    GatewayService gateWayService;
    @Autowired
    UserService userService;
    @Autowired
    UserGatewayRepository userGatewayRepository;
    @Autowired
    ZWaveController zwaveController;
    @Autowired
    HomeService homeService;
    @Autowired
    public GatewayController(GatewayService gateWayService) {
        this.gateWayService = gateWayService;
    }

    /**
     * 호스트 등록
     * @param registerHostRequestDto
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    @PostMapping
    public ResponseEntity registerGateway(@RequestBody HashMap<String, String> req)
            throws Exception {
    	String serial = req.get("serial");
    	AuditLogger.startLog(this.getClass(), "Checking a gateway is registered : " + serial);
        Gateway gateway = gateWayService.findBySerial(serial);
        if (isNull(gateway)) {
        	AuditLogger.endLog(this.getClass(), "Checking a gateway is registered : failed");
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
        	List lst = new ArrayList();
        	lst.add(gateway);
        	HashMap map = new HashMap<>();
        	map.put("list", lst);
        	AuditLogger.endLog(this.getClass(), "Checking a gateway is registered : succeed");
            return new ResponseEntity(map,HttpStatus.OK);
        }
    }

    /**
     * 로그인한 사용자가 등록한 모든 gateway를 조회한다.
     * @param req
     * @return
     * @throws Exception
     */

	@GetMapping
	public ResponseEntity<HashMap<String, Object>> getGatewayList(
			@RequestParam(value = "shomeNos", required = false) String shomeNos) throws Exception {
		
		List<Integer> iHomes = new ArrayList();
		if (!Objects.isNull(shomeNos)) {
			String[] sHomes = shomeNos.split(",");
			for (String sHome : sHomes) {
				iHomes.add(Integer.parseInt(sHome));
			}
		}else {
			List<Home> home = homeService.getHomeListByEmail(getAuthUserEmail());
			iHomes.add(home.get(0).getNo());
		}
		List lstGateways = gateWayService.getGatewayListByHome(iHomes);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("list", lstGateways);
		return new ResponseEntity(map, HttpStatus.OK);
	}
    
    @DeleteMapping(value = "/{no}")
    public ResponseEntity deleteGateway(@PathVariable("no") int no) throws Exception {
        gateWayService.delete(no);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @PutMapping(value = "/{no}")
    public ResponseEntity modifyGateway(@PathVariable("no") int no, @RequestBody Gateway gateway) {
        String userEmail = getAuthUserEmail();
        Gateway originGateway = gateWayService.findOne(no);
        if(null!=originGateway) {
            if(originGateway.getCreated_user_id().equals(userEmail)){
                Gateway rtnGateway = gateWayService.modifyGateway(originGateway, gateway);
                return new ResponseEntity<>(rtnGateway,HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }
    

    
}
