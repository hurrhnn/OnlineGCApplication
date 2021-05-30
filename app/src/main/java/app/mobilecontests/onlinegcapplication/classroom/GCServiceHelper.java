package app.mobilecontests.onlinegcapplication.classroom;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.api.services.classroom.Classroom;
import com.google.api.services.classroom.model.Course;
import com.google.api.services.classroom.model.CourseWork;
import com.google.api.services.classroom.model.ListCourseWorkResponse;
import com.google.api.services.classroom.model.ListCoursesResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class GCServiceHelper {

    private final Executor executor = Executors.newFixedThreadPool(1024);
    private final Classroom classroomService;
    String pageToken = null;

    public GCServiceHelper(Classroom googleClassroomService) {
        classroomService = googleClassroomService;
    }

    public Task<List<Course>> listCourses() {
        return Tasks.call(executor, () -> {
            List<Course> courses = new ArrayList<>();
            do {
                ListCoursesResponse response = classroomService.courses().list()
                        .setPageSize(100)
                        .setPageToken(pageToken)
                        .execute();
                courses.addAll(response.getCourses());
                pageToken = response.getNextPageToken();
            } while (pageToken != null);
            return courses;
        });
    }

    public Task<List<CourseWork>> listCourseWorks(final String courseId) {
        return Tasks.call(executor, () -> {
            ListCourseWorkResponse response = classroomService.courses().
                    courseWork().list(courseId).execute();
            return response.getCourseWork();
        });
    }
}
