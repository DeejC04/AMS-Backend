package com.ams.restapi.authentication;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ams.restapi.courseInfo.CourseInfo;
import com.ams.restapi.courseInfo.CourseInfoRepository;

import edu.ksu.canvas.CanvasApiFactory;
import edu.ksu.canvas.interfaces.EnrollmentReader;
import edu.ksu.canvas.model.Enrollment;
import edu.ksu.canvas.oauth.NonRefreshableOauthToken;
import edu.ksu.canvas.oauth.OauthToken;
import edu.ksu.canvas.requestOptions.GetEnrollmentOptions;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Component
public class CanvasSectionRefresher {
    private static final CanvasApiFactory API;
    private static final OauthToken TOKEN;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    CourseInfoRepository courseInfoRepository;

    @Autowired
    private EntityManager entityManager;
    
    @Autowired
    UserService userService;

    static{
        Dotenv env = Dotenv.load();
        API = new CanvasApiFactory(env.get("CANVAS_URL"));;
        TOKEN = new NonRefreshableOauthToken(env.get("API_KEY"));
    }

    @Scheduled(fixedRate = 360000)
    @Transactional
    public void updateUserSections() throws IOException {
        List<CourseInfo> sectionList = parseCSV("sections.csv");
        EnrollmentReader sectionReader = API.getReader(EnrollmentReader.class, TOKEN);
    
        for (CourseInfo section : sectionList) {
            Long courseId = section.getCourseId();
            List<Enrollment> sections = sectionReader.getSectionEnrollments(new GetEnrollmentOptions(String.valueOf(courseId)));
    
            for (Enrollment enrollment : sections) {
                String email = enrollment.getUser().getLoginId();
                String name = enrollment.getUser().getName(); 

                User user = userService.findOrCreateUser(email, name);
    
                Role.RoleType roleType = getRoleTypeFromCanvasEnrollment(enrollment.getType());
                Role role = roleRepository.findByRole(roleType)
                    .orElseThrow(() -> new RuntimeException("Role not found"));
                role = entityManager.merge(role);
                user.getRoles().add(role);
    
                userRepository.save(user);
            }
        }
    }
    
    private Role.RoleType getRoleTypeFromCanvasEnrollment(String canvasRole) {
        switch (canvasRole) {
            case "TeacherEnrollment":
                return Role.RoleType.INSTRUCTOR;
            case "TaEnrollment":
                return Role.RoleType.TA;
            default:
                return Role.RoleType.DEFAULT;
        }
    }

    public List<CourseInfo> parseCSV(String filePath) {
        List<CourseInfo> sections = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                CourseInfo section = new CourseInfo(Long.parseLong(values[0]), Long.parseLong(values[1]), "CSE110", "COOR170",List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY),
                        LocalTime.of(12, 15), LocalTime.of(13,  5));
                sections.add(section);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sections;
    }
}
