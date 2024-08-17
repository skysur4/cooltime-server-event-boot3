package io.cooltime.event.model;

import java.util.List;

import org.springframework.util.ObjectUtils;

import com.google.common.collect.Lists;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventMessageRequest extends AlertMessage {

	private static final long serialVersionUID = 5130628833896516685L;

	@Schema(title = "보낸이", hidden = true)
	private String sender;

	@Schema(title = "받는이")
	private List<String> receivers = Lists.newArrayList();

	@Schema(hidden = true)
	public void addReceivers(String... receivers) {
		for(String receiver : receivers) {
			this.receivers.add(receiver);
		}
	}

	@Schema(title = "받는이", hidden = true)
	public List<String> getReceivers(){
		if(ObjectUtils.isEmpty(this.receivers)) {
			this.receivers.add(this.sender);
		}

		return this.receivers;
	}

	@Schema(hidden = true)
	public AlertMessage getAlertMessage(){
		AlertMessage alertMessage = new AlertMessage(this.getType(), this.getMessage(), this.getProgress(), this.getBuffer());

		return alertMessage;
	}
}
