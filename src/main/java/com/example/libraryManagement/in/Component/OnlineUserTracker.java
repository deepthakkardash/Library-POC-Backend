package com.example.libraryManagement.in.Component;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@Component
public class OnlineUserTracker {

    private final Set<String> onlineUsers= ConcurrentHashMap.newKeySet();

    public void userConnected(String userId)
    {
        onlineUsers.add(userId);
        log.info("Added");
    }

    public void userDisconnected(String userId)
    {
        onlineUsers.remove(userId);
    }

    public boolean isUserOnline(String userId)
    {
        return onlineUsers.contains(userId);
    }

    public Set<String> getOnlineUsers(){
        return onlineUsers;
    }
}
