package ipren.watchr.repository.API;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ipren.watchr.dataHolders.FireComment;

public class FirebaseDatabaseHelper {

    FirebaseFirestore fireStore;

    HashMap<String, MutableLiveData<FireComment[]>> commentsByMovie_id = new HashMap();
    HashMap<String, MutableLiveData<FireComment[]>> commentsByUser_id = new HashMap();

    public FirebaseDatabaseHelper() {
        fireStore = FirebaseFirestore.getInstance();

    }

    public void addComment(String user_id, String movie_id, String text, OnCompleteListener callback) {
        Map<String, String> comment = new HashMap<>();

        comment.put("user_id", user_id);
        comment.put("movie_id", movie_id);
        comment.put("text", text);

        fireStore.collection("comments").add(comment).addOnCompleteListener(callback);

    }

    public void addRating(String user_id, String movie_id, int score, OnCompleteListener callback) {
        Map<String, String> comment = new HashMap<>();

        comment.put("user_id", user_id);
        comment.put("movie_id", movie_id);
        comment.put("score", "" + score);

        fireStore.collection("ratings").add(comment).addOnCompleteListener(callback);

    }

    //TODO These methods replace the previous dataSet on change, if possible replace with  functions
    // that update rather than replace.

    public LiveData<FireComment[]> getCommentByMovieID(String movie_id) {
        if (!commentsByMovie_id.containsKey(movie_id)) {

            MutableLiveData<FireComment[]> commentList = new MutableLiveData<>(null);
            commentsByMovie_id.put(movie_id, commentList);

            Query query = fireStore.collection("comments").whereEqualTo("movie_id", movie_id);
            query.addSnapshotListener((value, exception) -> {
                if (exception != null) {
                    Log.w("Firestore", "Listen failed", exception);
                    return;
                }

                List<FireComment> newSet = new ArrayList<>();
                for (QueryDocumentSnapshot doc : value) {
                    newSet.add(doc.toObject(FireComment.class));
                }
                commentList.postValue((FireComment[]) newSet.toArray());

            });
        }

        return commentsByMovie_id.get(movie_id);
    }

    public LiveData<FireComment[]> getCommentsByUserID(String user_id) {
        if (!commentsByUser_id.containsKey(user_id)) {

            MutableLiveData<FireComment[]> commentList = new MutableLiveData<>(null);
            commentsByUser_id.put(user_id, commentList);

            Query query = fireStore.collection("comments").whereEqualTo("user_id", user_id);
            query.addSnapshotListener((value, exception) -> {
                if (exception != null) {
                    Log.w("Firestore", "Listen failed", exception);
                    return;
                }

                List<FireComment> newSet = new ArrayList<>();
                for (QueryDocumentSnapshot doc : value) {
                    newSet.add(doc.toObject(FireComment.class));
                }

                commentList.postValue(newSet.toArray(new FireComment[newSet.size()]));

            });
        }
        return commentsByUser_id.get(user_id);
    }

    public

    void syncUserWithDatabase(FirebaseUser user) {
        if (user == null)
            return;
            Uri uri = user.getPhotoUrl();
          Map<String, Object> userData = new HashMap<>();
          userData.put("username", user.getDisplayName());
          userData.put("photoUri", uri !=null ? uri.toString() : null);
            fireStore.collection("users").document(user.getUid()).set(userData, SetOptions.merge());
    }

}
