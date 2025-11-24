package com.example.libraryManagement.in.Component;


import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class OnlineUserTracker {

    private final Set<Long> onlineUsers= ConcurrentHashMap.newKeySet();

    public void userConnected(Long userId)
    {
        onlineUsers.add(userId);
    }

    public void userDisconnected(Long userId)
    {
        onlineUsers.remove(userId);
    }

    public boolean isUserOnline(Long userId)
    {
        return onlineUsers.contains(userId);
    }

    public Set<Long> getOnlineUsers(){
        return onlineUsers;
    }
}
