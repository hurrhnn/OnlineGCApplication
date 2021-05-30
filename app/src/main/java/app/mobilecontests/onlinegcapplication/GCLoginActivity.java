package app.mobilecontests.onlinegcapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.classroom.Classroom;
import com.google.api.services.classroom.ClassroomScopes;
import com.google.api.services.classroom.model.Course;
import com.google.api.services.classroom.model.CourseWork;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GCLoginActivity extends AppCompatActivity {

    SignInButton signIn;
    ImageButton backBtn;
    Button loginBtn;

    int RC_SIGN_IN = 0;
    GoogleSignInClient mGoogleSignInClient;
    ClassroomServiceHelper mClassroomServiceHelper;

    Map<Course, List<CourseWork>> classes = new HashMap<>();
    ArrayList<String> classNames = new ArrayList<>();
    ArrayList<String> classIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gclogin);

        signIn = findViewById(R.id.google_signin_btn);
        signIn.setOnClickListener(v -> {
            if (v.getId() == R.id.google_signin_btn) {
                signIn();
            }
        });
        loginBtn = findViewById(R.id.signin_btn);
        loginBtn.setOnClickListener(v -> {
            Intent intent = new Intent(GCLoginActivity.this, OCLoginActivity.class);
            startActivity(intent);
        });

        backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(GCLoginActivity.this, OCLoginActivity.class);
            startActivity(intent);
        });
    }

    private void signIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope(ClassroomScopes.CLASSROOM_COURSES_READONLY), new Scope(ClassroomScopes.CLASSROOM_COURSEWORK_ME_READONLY))
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        Intent intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            handleSignInResult(data);
        }
    }

    private void handleSignInResult(Intent data) {
        GoogleSignIn.getSignedInAccountFromIntent(data)
                .addOnSuccessListener(account -> {
                    List<String> classroomScopes = new ArrayList<>();
                    classroomScopes.add(ClassroomScopes.CLASSROOM_COURSES_READONLY);
                    classroomScopes.add(ClassroomScopes.CLASSROOM_COURSEWORK_ME_READONLY);
                    GoogleAccountCredential googleAccountCredential = GoogleAccountCredential.usingOAuth2(GCLoginActivity.this, classroomScopes);
                    googleAccountCredential.setSelectedAccount(account.getAccount());

                    Classroom classroom = new Classroom.Builder(
                            AndroidHttp.newCompatibleTransport(),
                            new GsonFactory(),
                            googleAccountCredential)
                            .setApplicationName("OnlineGCApplication")
                            .build();

                    mClassroomServiceHelper = new ClassroomServiceHelper(classroom);
                    // Signed in successfully, show authenticated UI.
                    course();

                }).addOnFailureListener(e -> {
        });
    }

    public void course() { //classroom 가져오기
        ProgressDialog progressDialog = new ProgressDialog(GCLoginActivity.this);
        progressDialog.setTitle("Getting classroom list");
        progressDialog.setMessage("Please wait..");
        progressDialog.show();

        mClassroomServiceHelper.listCourses().addOnSuccessListener(courses -> {
            for (Course course : courses) {
                String className = course.getName();
                String classId = course.getId();

                Task<List<CourseWork>> courseWorksLoader = mClassroomServiceHelper.listCourseWorks(course.getId()).addOnSuccessListener(courseWorks -> {
                    if (courseWorks == null) return;
                    classes.put(course, courseWorks);
                });

                while (true) {
                    if (courseWorksLoader.isComplete()) break;
                }
                classNames.add(className);
                classIds.add(classId);
            }
        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Check your Classroom", Toast.LENGTH_LONG).show();
        }).addOnCompleteListener(task -> {
            progressDialog.dismiss();
//            int i = 0;
//            while(true) {
//                if(classes.size() != 0)
//                    break;
//                System.out.println(classes.size());
//            }
//            System.out.println("aaaaaaaaaaaaaaaaaaaaaa" + classes.size());
//            for (Course course : classes.keySet()) {
//                System.out.println(++i + ". " + course.getName() + ", " + classes.get(course).size() + "개의 과제");
//            }
        });

//        Intent intent = new Intent(MainActivity.this, ResultActivity.class);
//        intent.putExtra("courses", classes.keySet().toArray());
//        intent.putExtra("courseWorks", classes.values().toArray());
//        startActivity(intent);
    }
}
