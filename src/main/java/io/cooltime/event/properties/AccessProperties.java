package io.cooltime.event.properties;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.nimbusds.jose.util.ArrayUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.security.access")
public class AccessProperties {
	private List<String> free = Lists.newArrayList();
	private List<String> users = Lists.newArrayList();
	private List<String> admins = Lists.newArrayList();

	public String[] getFreePaths(String... pattern) {
		return ArrayUtils.concat(free.toArray(new String[0]), pattern);
	}

	public String[] getUserPaths(String... pattern) {
		return ArrayUtils.concat(users.toArray(new String[0]), pattern);
	}

	public String[] getAdminPaths(String... pattern) {
		return ArrayUtils.concat(admins.toArray(new String[0]), pattern);
	}
}