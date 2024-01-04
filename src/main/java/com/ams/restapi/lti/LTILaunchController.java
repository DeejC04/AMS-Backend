package com.ams.restapi.lti;

import edu.ksu.lti.launch.controller.LtiLaunchController;
import edu.ksu.lti.launch.controller.OauthController;
import edu.ksu.lti.launch.exception.NoLtiSessionException;
import edu.ksu.lti.launch.model.LtiLaunchData;
import edu.ksu.lti.launch.model.LtiSession;
import edu.ksu.lti.launch.security.CanvasInstanceChecker;
import edu.ksu.lti.launch.service.LtiSessionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@Scope("request")
public class LTILaunchController extends LtiLaunchController {
    private static final Logger LOG = LogManager.getLogger(LTILaunchController.class);

    @Autowired
    public LtiSessionService ltiSessionService;
    @Autowired
    private CanvasInstanceChecker instanceChecker;

    /**
     * We have our applications return the LTI configuration XML when you hit
     * the root of the context in a browser. It's an easy place to keep
     * the necessary XML and this method sets the host name/port to appropriate
     * values when running in dev/test by examining the incoming HTTP request.
     */
    @RequestMapping("/")
    public ModelAndView basePath(HttpServletRequest request) {
        LOG.info("Showing Activity Reporting configuration XML");
        String ltiLaunchUrl = OauthController.getApplicationBaseUrl(request, true) + "/launch";
        LOG.debug("LTI launch URL: " + ltiLaunchUrl);
        return new ModelAndView("ltiConfigure", "url", ltiLaunchUrl);
    }

    @RequestMapping("/index")
    public ModelAndView showButton() throws NoLtiSessionException {
        LtiSession ltiSession = ltiSessionService.getLtiSession();
        if (ltiSession.getEid() == null || ltiSession.getEid().isEmpty()) {
            throw new AccessDeniedException("You cannot access this content without a valid session");
        }
        return new ModelAndView("index", "username", ltiSession.getEid());
    }

    @Override
    @RequestMapping(value = { "/launch" }, method = { RequestMethod.POST })
    public String ltiLaunch(@ModelAttribute LtiLaunchData ltiData, HttpSession session) throws Exception {
        session.invalidate();
        LOG.debug("launch!");
        String canvasCourseId = ltiData.getCustom_canvas_course_id();
        String eID = ltiData.getCustom_canvas_user_login_id();
        LtiSession ltiSession = new LtiSession();
        ltiSession.setApplicationName(this.getApplicationName());
        ltiSession.setInitialViewPath(this.getInitialViewPath());
        ltiSession.setEid(eID);
        ltiSession.setCanvasCourseId(canvasCourseId);
        ltiSession.setCanvasDomain(ltiData.getCustom_canvas_api_domain());
        ltiSession.setLtiLaunchData(ltiData);
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpSession newSession = sra.getRequest().getSession();
        newSession.setAttribute(LtiSession.class.getName(), ltiSession);
        this.instanceChecker.assertValidInstance(ltiSession);
        Logger var10000 = LOG;
        String var10001 = this.getApplicationName();
        var10000.info("launching LTI integration '" + var10001 + "' from " + ltiSession.getCanvasDomain()
                + " for course: " + canvasCourseId + " as user " + eID);
        var10000.info("forwarding user to: " + this.getInitialViewPath());
        return "forward:" + this.getInitialViewPath();
    }

    /**
     * After authenticating the LTI launch request, the user is forwarded to
     * this path. It is the initial page your user will see in their browser.
     */
    @Override
    protected String getInitialViewPath() {
        return "/index";
    }

    @Override
    protected String getApplicationName() {
        return "Attendance Management System";
    }
}
