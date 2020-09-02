package com.tanto;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class User {
	private String idUser;
	private String name;
	private String nickname;
	private String hobby;
	private Date oncreat = new Date();
	private Map<String, Object> address = new HashMap<String, Object>();
	
	public String getIdUser() {
		return idUser;
	}
	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getHobby() {
		return hobby;
	}
	public void setHobby(String hobby) {
		this.hobby = hobby;
	}
	public Date getOncreat() {
		return oncreat;
	}
	public void setOncreat(Date oncreat) {
		this.oncreat = oncreat;
	}
	public Map<String, Object> getAddress() {
		return address;
	}
	public void setAddress(Map<String, Object> address) {
		this.address = address;
	}	
}
