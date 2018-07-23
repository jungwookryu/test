package com.ht.connected.home.backend.update.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.ht.connected.home.backend.controller.rest.CommonController;
import com.ht.connected.home.backend.update.model.entity.UPDeviceVersion;
import com.ht.connected.home.backend.update.model.entity.UPFileVersion;
import com.ht.connected.home.backend.update.service.UPService;

@Controller
public class UPController extends CommonController {

    private static final Log logging = LogFactory.getLog(UPController.class);

    @Autowired
    private UPService upService;
    
    
    @PostMapping("/device/update")
    public ResponseEntity<String> rest(@RequestBody UPDeviceVersion request) {
        request.setIotAccount(getAuthUserEmail());
        upService.updateOwnDevice(request);
        return new ResponseEntity<String>("", null, HttpStatus.OK);
    }
    

    /**
     * 로그인 페이지.
     *
     * @return
     */
    @GetMapping("/update/login")
    public ResponseEntity<String> index() {        
        return getHTMLResponseEntity("templates/admin/user/login.html");
    }

    /**
     * 로그인 요청 처리.
     *
     * @param body
     * @param session
     * @param httpServletResponse
     * @return
     * @throws Exception
     */
    @PostMapping("/update/login")
    public ResponseEntity<String> login(@RequestParam Map<String, String> body, HttpSession session,
            HttpServletResponse httpServletResponse) throws IOException {
        String redirectUrl = "/update/login";
        if (upService.login(body, session)) {
            redirectUrl = "/update/file/register";
        }
        return redirect(redirectUrl);
    }

    /**
     * 업데이트 페이지.
     *
     * @param session
     * @param model
     * @return
     */
    @GetMapping("/update/file/register")
    public ResponseEntity<String> form(HttpSession session) {
        ResponseEntity<String> response = redirect("/update/login");        
        if (upService.isLoggedIn(session)) {
            response = getHTMLResponseEntity("templates/admin/update/form.html");
        }
        return response;
    }

    /**
     * 업데이트 요청 처리.
     *
     * @param body
     * @return
     * @throws JSONException 
     */
    @PostMapping("/update/file/register")
    public ResponseEntity<String> updateVersion(@RequestBody UPFileVersion request) throws JSONException {
        upService.addVersion(request);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", true);
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }

    /**
     * 리다이랙트.
     *
     * @param redirectUri
     * @return
     */
    private ResponseEntity<String> redirect(String redirectUri) {
        URI uri = null;
        try {
            uri = new URI(redirectUri);
        } catch (URISyntaxException e) {
            logging.error(e.getMessage());
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uri);
        return new ResponseEntity<>(null, httpHeaders, HttpStatus.SEE_OTHER);
    }
    
    private ResponseEntity<String> getHTMLResponseEntity(String filePath) {
        String html = null;
        try {
            InputStream file = getClass().getClassLoader().getResourceAsStream(filePath);
            html = IOUtils.toString(file, "UTF-8");
        } catch (IOException e) {
            logging.error(String.format("Loading html file failed : %s", filePath));
        }
        return new ResponseEntity<String>(html, HttpStatus.OK);
    }

}
