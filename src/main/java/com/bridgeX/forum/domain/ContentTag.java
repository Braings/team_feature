package com.bridgeX.forum.domain;
import lombok.Getter;

@Getter
public enum ContentTag {
	INFORMATION("INFORMATION"), QUESTION("QUESTION"), TALK("TALK");

	ContentTag(String value) {
		this.value = value;
	}

	private String value;

}
