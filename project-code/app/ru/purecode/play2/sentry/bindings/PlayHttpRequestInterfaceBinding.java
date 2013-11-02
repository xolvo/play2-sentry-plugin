package ru.purecode.play2.sentry.bindings;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Map;

import net.kencochrane.raven.marshaller.json.InterfaceBinding;

import org.apache.commons.lang3.StringUtils;

import play.mvc.Http;
import play.mvc.Http.Request;
import ru.purecode.play2.sentry.interfaces.PlayHttpRequestInterface;

import com.fasterxml.jackson.core.JsonGenerator;

public class PlayHttpRequestInterfaceBinding implements InterfaceBinding<PlayHttpRequestInterface> {
	private static final String URL = "url";
    private static final String METHOD = "method";
    private static final String DATA = "data";
    private static final String QUERY_STRING = "query_string";
    private static final String COOKIES = "cookies";
    private static final String HEADERS = "headers";
    private static final String ENVIRONMENT = "env";
    private static final String ENV_REMOTE_ADDR = "REMOTE_ADDR";
    private static final String ENV_SERVER_NAME = "SERVER_NAME";
    private static final String ENV_SERVER_PORT = "SERVER_PORT";
    private static final String ENV_LOCAL_ADDR = "LOCAL_ADDR";
    private static final String ENV_LOCAL_NAME = "LOCAL_NAME";
    private static final String ENV_SERVER_PROTOCOL = "SERVER_PROTOCOL";
    private static final String ENV_REQUEST_SECURE = "REQUEST_SECURE";
    private static final String ENV_REMOTE_USER = "REMOTE_USER";
    
    private String buildUrl(Request request) {
    	return String.format("http://%s", request.host()) + request.path();
    }
    
    private Integer extractPort(Request request) {
    	String host = request.host();
    	String[] hparts = host.split(":");
    	
    	Integer port;
    	
    	if(hparts.length == 2)
    		port = Integer.parseInt(hparts[1]);
    	else
    		port = 80;
    	
    	return port;
    }
    
    private String extractQueryString(Request request) {
    	int index = request.uri().indexOf("?");
    	if(index == -1)
    		return "";
    	
    	return request.uri().substring(index + 1);
    }
    
	@Override
	public void writeInterface(JsonGenerator generator, PlayHttpRequestInterface playHttpInterface) throws IOException {
		Request request = playHttpInterface.getRequest();
		
		generator.writeStartObject();
		generator.writeStringField(URL, buildUrl(request));
		generator.writeStringField(METHOD, request.method());
		
		generator.writeFieldName(DATA);
		writeData(generator, request.body().asFormUrlEncoded());
		
		generator.writeStringField(QUERY_STRING, extractQueryString(request));
		
		generator.writeFieldName(COOKIES);
		writeCookies(generator, request.headers().get(Http.HeaderNames.COOKIE));
		
		generator.writeFieldName(HEADERS);
		writeHeaders(generator, request.headers());
		
		generator.writeFieldName(ENVIRONMENT);
		writeEnvironment(generator, request);
		
		generator.writeEndObject();
	}

	private void writeEnvironment(JsonGenerator generator, Request request) throws IOException {
		InetAddress localhost = InetAddress.getLocalHost();
		
		generator.writeStartObject();
        generator.writeStringField(ENV_REMOTE_ADDR, request.remoteAddress());
        generator.writeStringField(ENV_SERVER_NAME, request.host());
        generator.writeNumberField(ENV_SERVER_PORT, extractPort(request));
        generator.writeStringField(ENV_LOCAL_ADDR, localhost.getHostAddress());
        generator.writeStringField(ENV_LOCAL_NAME, localhost.getHostName());
        generator.writeStringField(ENV_SERVER_PROTOCOL, request.version());
        generator.writeBooleanField(ENV_REQUEST_SECURE, request.username() != null);
        generator.writeStringField(ENV_REMOTE_USER, request.username());
        generator.writeEndObject();
	}

	private void writeHeaders(JsonGenerator generator, Map<String, String[]> headers) throws IOException {
		generator.writeStartObject();
        for(String header_key : headers.keySet()) {
        	if(header_key.equals(Http.HeaderNames.COOKIE))
        		continue;
        	
        	generator.writeStringField(header_key,
        		StringUtils.join(headers.get(header_key), ", "));
        }
        generator.writeEndObject();
	}

	private void writeCookies(JsonGenerator generator, String[] cookies) throws IOException {
		if(cookies == null) {
			generator.writeNull();
			return;
		}
		
    	String _cookies = cookies.length != 0 ? cookies[0] : null;
    	if(_cookies == null) {
    		generator.writeNull();
    		return;
    	}
    	
    	generator.writeStartObject();
    	
    	String[] cookies_parts = _cookies.split(";");
    	for(String cp : cookies_parts) {
    		String[] key_value = cp.trim().split("=");
    		
    		String k = null, v = null;
    		
    		if(key_value.length == 2) {
    			k = key_value[0];
    			v = key_value[1];
    		} else if(key_value.length == 1) {
    			k = key_value[0];
    		}
    		
    		if(k != null && v != null)
    			generator.writeStringField(k, v);
    	}
    	
    	generator.writeEndObject();
	}

	private void writeData(JsonGenerator generator, Map<String, String[]> parameterMap) throws IOException {
		if(parameterMap == null) {
            generator.writeNull();
            return;
        }

        generator.writeStartObject();
        for(Map.Entry<String, String[]> parameter : parameterMap.entrySet()) {
        	String key = parameter.getKey();
        	String[] value = parameter.getValue();
        	if(key != null && value != null)
        		generator.writeStringField(key, StringUtils.join(value, ", "));
        }
        generator.writeEndObject();
	}
}
