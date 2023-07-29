package com.ams.restapi;

import org.springframework.stereotype.Component;

import com.ams.restapi.attendance.AttendanceRepository;

import edu.ksu.canvas.CanvasApiFactory;
import edu.ksu.canvas.interfaces.CanvasReader;
import edu.ksu.canvas.oauth.NonRefreshableOauthToken;
import edu.ksu.canvas.oauth.OauthToken;
import io.github.cdimascio.dotenv.Dotenv;

@Component
public class CanvasAccess {

    private final CanvasApiFactory apiFactory;
    private final OauthToken oauthToken;

    public CanvasAccess() {
        Dotenv env = Dotenv.load();
        apiFactory = new CanvasApiFactory(env.get("CANVAS_URL"));
        oauthToken = new NonRefreshableOauthToken(env.get("APIKEY"));
        System.out.println("USING TOKEN: " + oauthToken.getAccessToken());
    }

    public <T extends CanvasReader> T getReader(Class<T> type) {
        return apiFactory.getReader(type, oauthToken);
    }
}
