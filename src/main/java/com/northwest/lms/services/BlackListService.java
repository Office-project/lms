package com.northwest.lms.services;


import com.northwest.lms.models.BlackListedToken;


public interface BlackListService {

    BlackListedToken blackListToken(String  token);

//    BlackListedToken getToken(String  token);

    boolean tokenExist(String token);
}
