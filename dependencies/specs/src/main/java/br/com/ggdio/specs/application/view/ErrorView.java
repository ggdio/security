package br.com.ggdio.specs.application.view;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.ggdio.specs.application.exception.ApplicationException;
import br.com.ggdio.specs.application.model.ErrorDetail;

/**
 * View for errors detailing.
 * 
 * @author Guilherme Dio
 * @version 1.0.0-RELEASE, 30 Jul 2018
 * @since 1.0.0-RELEASE
 */
@XmlRootElement(name="error")
@XmlAccessorType(XmlAccessType.FIELD)
public class ErrorView implements ItemView<Object> {

	@XmlElement(name="code")
	private String code;
	
	@XmlElement(name="status")
	private int status;
	
	@XmlElement(name="message")
	private String message;
	
	@XmlElement(name="method")
	private String method;
	
	@XmlElement(name="path")
	private String path;
	
	@XmlElementWrapper(name="stackTraces")
	@XmlElement(name="stackTrace")
	private String[] stackTrace;
	
	@XmlElement(name="timestamp")
	private long timestamp = System.currentTimeMillis();
	
	public ErrorView() {
		this.code = "";
		this.message = "";
		this.path = "";
		this.stackTrace = new String[0];
	}
	
	public ErrorView(String code, String message, StackTraceElement[] stackElements) {
		this.code = code;
		this.message = message;
		
		if(stackElements != null && stackElements.length > 0) {
			this.stackTrace = new String[stackElements.length];
			for (int i = 0; i < stackElements.length; i++) {
				stackTrace[i] = stackElements[i].toString();
			}
		} else {
			stackTrace = new String[0];
		}
	}
	
	public ErrorView(String code, String message, String[] stackTrace) {
		this.code = code;
		this.message = message;
		this.stackTrace = stackTrace;
	}
	
	public ErrorView(ApplicationException e) {
		this(e.getCode(), e.getMessage(), e.getStackTrace());
	}
	
	public String getCode() {
		return code;
	}
	
	public int getStatus() {
		return status;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getMethod() {
		return method;
	}
	
	public String getPath() {
		return path;
	}
	
	public String[] getStackTrace() {
		return stackTrace;
	}
	
	public long getTimestamp() {
		return timestamp;
	}
	
	public ErrorView code(String code) {
		this.code = code;
		return this;
	}
	
	public ErrorView status(int status) {
		this.status = status;
		return this;
	}
	
	public ErrorView message(String message) {
		this.message = message;
		return this;
	}
	
	public ErrorView method(String method) {
		this.method = method;
		return this;
	}
	
	public ErrorView path(String path) {
		this.path = path;
		return this;
	}
	
	public ErrorView stackTrace(String[] stackTrace) {
		this.stackTrace = stackTrace;
		return this;
	}
	
	public ErrorView timestamp(long timestamp) {
		this.timestamp = timestamp;
		return this;
	}
	
	public static ErrorView from(ErrorDetail error, Object...args) {
		return ErrorView.from(error, null, args);
	}
	
	public static ErrorView from(ErrorDetail error, StackTraceElement[] stack, Object...args) {
		if(args != null && args.length > 0) {
			try {
				return new ErrorView(error.getCode(), String.format(error.getMessage(), args), stack).status(error.getHTTPStatus());
				
			} catch(Exception e) { 
				//e.ignore();
				
			}
		}
		
		return new ErrorView(error.getCode(), error.getMessage(), stack).status(error.getHTTPStatus());
			
	}
	
}