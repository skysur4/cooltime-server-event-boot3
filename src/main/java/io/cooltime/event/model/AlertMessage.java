package io.cooltime.event.model;

import java.io.Serializable;

import io.cooltime.event.EventConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlertMessage implements Serializable {
	public AlertMessage(String message){
		this.message = message;
	}

	private static final long serialVersionUID = 3458694356756548852L;

	@Schema(title = "메시지 종류", requiredMode = RequiredMode.REQUIRED, allowableValues = {EventConstants.NOTIFY_INIT, EventConstants.NOTIFY_FINISH, EventConstants.NOTIFY_LOG, EventConstants.NOTIFY_INFO, EventConstants.NOTIFY_WARN, EventConstants.NOTIFY_ERROR})
	private String type = EventConstants.NOTIFY_LOG;

	@Schema(title = "알림/로그", requiredMode = RequiredMode.REQUIRED, example = "테스트 알림 또는 로그 메시지입니다")
	private String message;

	@Schema(title = "진행율", example = "12")
	private int progress = 0;

	@Schema(title = "진행버퍼", example = "50")
	private int buffer = 0;
}
