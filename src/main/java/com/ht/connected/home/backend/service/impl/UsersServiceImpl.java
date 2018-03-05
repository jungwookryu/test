package com.ht.connected.home.backend.service.impl;

import com.ht.connected.home.backend.model.entity.Users;
import com.ht.connected.home.backend.repository.UsersRepository;
import com.ht.connected.home.backend.service.UsersService;
import com.ht.connected.home.backend.service.impl.base.CrudServiceImpl;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersServiceImpl extends CrudServiceImpl<Users, Integer> implements UsersService{

	private UsersRepository userRepository;

	@Autowired
	public UsersServiceImpl(@NotNull UsersRepository usersRepository) {
		super(usersRepository);
		this.userRepository = usersRepository;
	}
	
	public List<Users> getUser(String userId){
		return userRepository.findByUserId(userId);
	}
/*	
//	private Authentication authentication()
	  @Override
	  public AuthenticationToken createToken(HttpServletRequest request)
	  {
	    AuthenticationToken token = null;
	    String authorization = request.getHeader(HEADER_AUTHORIZATION);

	    if (!Strings.isNullOrEmpty(authorization))
	    {
	      String[] parts = authorization.split("\\s+");

	      if (parts.length > 0)
	      {
	        token = createToken(request, parts[0], parts[1]);

	        if (token == null)
	        {
	          logger.warn("could not create token from authentication header");
	        }
	      }
	      else
	      {
	        logger.warn("found malformed authentication header");
	      }
	    }

	    return token;
	  }
	}*/
}
