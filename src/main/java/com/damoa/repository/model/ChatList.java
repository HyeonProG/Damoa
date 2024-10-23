package com.damoa.repository.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatList {
    private int id;
    private int companyId;
    private int freelancerId;
    private Timestamp createdAt;
}
