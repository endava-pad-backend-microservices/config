package com.endava.config.entity;

import lombok.Builder;

@Builder
public class User {
    private int maxConnections;
    private int timeoutMs;
}
