package app.mobilecontests.onlinegcapplication;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import app.mobilecontests.onlinegcapplication.classroom.GCServiceHelper;
import app.mobilecontests.onlinegcapplication.sqlite.SQLiteHelper;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.classroom.Classroom;
import com.google.api.services.classroom.ClassroomScopes;
import com.google.api.services.classroom.model.Course;
import com.google.api.services.classroom.model.CourseWork;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    GCServiceHelper gcServiceHelper;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLiteHelper sqLiteHelper = new SQLiteHelper(this, "ogc.db", null, 1);
        database = sqLiteHelper.getWritableDatabase();
        sqLiteHelper.onCreate(database);

        findViewById(R.id.sign_in_button).setOnClickListener(v -> {
            if (v.getId() == R.id.sign_in_button) {
                signIn();
            }
        });

        findViewById(R.id.login).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, OCLoginActivity.class);
            startActivity(intent);
        });
    }

    public void signIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope(ClassroomScopes.CLASSROOM_COURSES_READONLY), new Scope(ClassroomScopes.CLASSROOM_COURSEWORK_ME_READONLY))
                .build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);


        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (!Boolean.parseBoolean(String.valueOf(requestCode))) {
            handleSignInResult(data);
        }
    }

    private void handleSignInResult(Intent data) {
        GoogleSignIn.getSignedInAccountFromIntent(data)
                .addOnSuccessListener(account -> {
                    List<String> classroomScopes = new ArrayList<>();
                    classroomScopes.add(ClassroomScopes.CLASSROOM_COURSES_READONLY);
                    classroomScopes.add(ClassroomScopes.CLASSROOM_COURSEWORK_ME_READONLY);
                    GoogleAccountCredential googleAccountCredential = GoogleAccountCredential.usingOAuth2(MainActivity.this, classroomScopes);
                    googleAccountCredential.setSelectedAccount(account.getAccount());

                    Classroom classroom = new Classroom.Builder(
                            AndroidHttp.newCompatibleTransport(),
                            new GsonFactory(),
                            googleAccountCredential)
                            .setApplicationName("OnlineGCApplication")
                            .build();

                    gcServiceHelper = new GCServiceHelper(classroom);
                    // Signed in successfully, show authenticated UI.
                    course();

                }).addOnFailureListener(e -> {
        });
    }

    public void course() { //classroom 가져오기
        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Getting classroom list");
        progressDialog.setMessage("Please wait..");
        progressDialog.show();

        gcServiceHelper.listCourses().addOnSuccessListener(courses -> {
            for (Course course : courses) {

                Task<List<CourseWork>> courseWorksLoader = gcServiceHelper.listCourseWorks(course.getId()).addOnSuccessListener(courseWorks -> {
                    if (courseWorks == null) return;

                    boolean isGCExist = false, isGCOverride = false;
                    byte[] existedCourseBlob = null, existedCourseWorksBlob = null;

                    Gson gson = new Gson();
                    Cursor cursor = database.query("google_classroom", new String[]{"course", "course_works"}, null, null, null, null, null);
                    while (cursor.moveToNext()) {
                        byte[] courseBlob = cursor.getBlob(0);

                        String courseJson = new String(courseBlob);
                        Course existedCourse = gson.fromJson(courseJson, new TypeToken<Course>() {
                        }.getType());

                        if (existedCourse.getName().equals(course.getName())) {
                            isGCExist = true;
                            if (!existedCourse.getUpdateTime().equals(course.getUpdateTime())) {
                                existedCourseBlob = courseBlob;
                                existedCourseWorksBlob = cursor.getBlob(1);
                                isGCOverride = true;
                            }
                            break;
                        }
                    }
                    cursor.close();

                    if (isGCExist) {
                        if (isGCOverride) {
                            database.delete("google_classroom", "course=? AND course_works=?", new String[]{Arrays.toString(existedCourseBlob), Arrays.toString(existedCourseWorksBlob)});

                            ContentValues contentValues = new ContentValues();
                            contentValues.put("course", gson.toJson(course).getBytes());
                            contentValues.put("course_works", gson.toJson(courseWorks).getBytes());

                            database.insert("google_classroom", null, contentValues);
                        }
                    } else {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("course", gson.toJson(course).getBytes());
                        contentValues.put("course_works", gson.toJson(courseWorks).getBytes());

                        database.insert("google_classroom", null, contentValues);
                    }
                });

                while (true) {
                    if (courseWorksLoader.isComplete()) break;
                }
            }
        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "An error occurred while loading the classroom.", Toast.LENGTH_LONG).show();
        }).addOnCompleteListener(task -> {
            progressDialog.dismiss();

            Intent intent = new Intent(MainActivity.this, ResultActivity.class);
            startActivity(intent);
        });
    }
}
