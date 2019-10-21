package ipren.watchr.repository.API.Firebase;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ipren.watchr.dataHolders.Comment;
import ipren.watchr.dataHolders.Rating;
import ipren.watchr.dataHolders.PublicProfile;
import ipren.watchr.dataHolders.User;

public class FirebaseDatabaseHelper {

    //These paths are placed on root.
    private final String RATING_PATH = "ratings";
    private final String COMMENT_PATH = "comments";
    private final String USER_PATH = "users";

    //This collection is located in root/user/movie_list
    private final String MOVIE_COLLECTION = "movie_lists";


    //static JS fields
    private final String USER_ID_FIELD = "user_id";
    private final String MOVIE_ID_FIELD = "movie_id";
    private final String COMMENT_TXT_ID_FIELD = "text";
    private final String SCORE_ID_FIELD = "score";
    private final String MOVIE_ARRAY_ID_FIELD = "movies";

    private final String USER_USERNAME_ID_FIELD = "username";
    private final String USER_PROFILE_URI_ID_FIELD = "photoUri";

    private final String ANDROID_RESOURCE_PREFIX = "android.resource://";

    FirebaseFirestore fireStore;

    private HashMap<String, MutableLiveData<Comment[]>> commentsByMovie_id = new HashMap<>();
    private HashMap<String, MutableLiveData<Comment[]>> commentsByUser_id = new HashMap<>();

    private HashMap<String, MutableLiveData<Rating[]>> ratingByMovie_id = new HashMap<>();
    private HashMap<String, MutableLiveData<Rating[]>> ratingByUser_id = new HashMap<>();

    private HashMap<String, MutableLiveData<PublicProfile>> publicProfiles = new HashMap<>();
    //user_id -> (movie_list , LiveData<movie_id[]>)
    private HashMap<String, HashMap<String, MutableLiveData<String[]>>> movieListByUser_id = new HashMap<>();


    FirebaseDatabaseHelper() {
        fireStore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        fireStore.setFirestoreSettings(settings);

    }


    void syncUserWithDatabase(User user) {
        if (user == null)
            return;
        Uri uri = user.getUserProfilePictureUri();
        Map<String, Object> userData = new HashMap<>();
        userData.put(USER_USERNAME_ID_FIELD, user.getUserName());
        userData.put(USER_PROFILE_URI_ID_FIELD, uri != null && !isUriLocal(user.getUserProfilePictureUri()) ? uri.toString() : null);
        fireStore.collection(USER_PATH).document(user.getUID()).set(userData, SetOptions.merge());
    }


    public void saveMovieToList(String list, String movie_id, String user_id, OnCompleteListener callback) {

        fireStore.collection(USER_PATH).document(user_id).collection(MOVIE_COLLECTION)
                .document(list).update(MOVIE_ARRAY_ID_FIELD, FieldValue.arrayUnion(movie_id))
                .addOnCompleteListener(e -> {
                    if (e.getException() != null && e.getException().getLocalizedMessage().startsWith("NOT_FOUND: No document to update: ")) {
                        HashMap<String, List<String>> entry = new HashMap<>();
                        List<String> data = new LinkedList<>();
                        data.add(movie_id);
                        entry.put(MOVIE_ARRAY_ID_FIELD, data);
                        Task task = fireStore.collection(USER_PATH)
                                .document(user_id)
                                .collection(MOVIE_COLLECTION)
                                .document(list)
                                .set(entry);
                        attachCallback(task, callback);
                    } else if (callback != null)
                        callback.onComplete(e);
                });
    }

    public void deleteMovieFromList(String list, String movie_id, String user_id, OnCompleteListener callback) {
        attachCallback(fireStore.collection(USER_PATH)
                .document(user_id).collection(MOVIE_COLLECTION).document(list)
                .update(MOVIE_ARRAY_ID_FIELD, FieldValue.arrayRemove(movie_id)), callback);

    }

    public LiveData<String[]> getMovieListByUserID(String list, String user_id) {
        if (!movieListByUser_id.containsKey(user_id) || !movieListByUser_id.get(user_id).containsKey(list)) {
            MutableLiveData<String[]> movieList = new MutableLiveData<>();
            if (!movieListByUser_id.containsKey(user_id))
                movieListByUser_id.put(user_id, new HashMap<>());
            movieListByUser_id.get(user_id).put(list, movieList);
            fireStore.collection(USER_PATH).document(user_id).collection(MOVIE_COLLECTION).document(list).addSnapshotListener((res, err) -> {
                        if (err != null)
                            return;
                        //if list does not exist result will be null;
                        try {
                            List<String> movies = (List<String>) res.get("movies");
                            movieList.postValue(movies.toArray(new String[movies.size()]));
                        } catch (Exception e) {
                            movieList.postValue(new String[0]);
                            Log.e("Firebase", "Could not parse array, res");
                        }

                    }
            );
        }
        return movieListByUser_id.get(user_id).get(list);
    }


    void addComment(String text, String movie_id, String user_id, OnCompleteListener callback) {
        Map<String, String> comment = new HashMap<>();
        comment.put(USER_ID_FIELD, user_id);
        comment.put(MOVIE_ID_FIELD, movie_id);
        comment.put(COMMENT_TXT_ID_FIELD, text);

        attachCallback(fireStore.collection(COMMENT_PATH).add(comment), callback);

    }

    void removeComment(String comment_id, OnCompleteListener callback) {
        attachCallback(fireStore.collection(COMMENT_PATH).document(comment_id).delete(), callback);
    }

    void addRating(int score, String movie_id, String user_id, OnCompleteListener callback) {
        Map<String, Object> rating = new HashMap<>();

        rating.put(USER_ID_FIELD, user_id);
        rating.put(MOVIE_ID_FIELD, movie_id);
        rating.put(SCORE_ID_FIELD, new Integer(score));

        //This method updates existing documents if they exists or creates if they dont. Also removes duplicates if there are any
        fireStore.collection(RATING_PATH).whereEqualTo(MOVIE_ID_FIELD, movie_id).whereEqualTo(USER_ID_FIELD, user_id).get().addOnCompleteListener(e -> {
            if (e.isSuccessful()) {
                if (e.getResult().isEmpty()) {
                    attachCallback(fireStore.collection(RATING_PATH).add(rating), callback);
                } else {
                    boolean first = true;
                    for (DocumentSnapshot doc : e.getResult()) {
                        if (first) {
                            attachCallback(doc.getReference().set(rating), callback);
                            first = false;
                        } else {
                            doc.getReference().delete();
                        }
                    }
                }

            } else if (callback != null)
                callback.onComplete(e);
        });

    }

    void removeRating(String rating_id, OnCompleteListener callback) {
        attachCallback(fireStore.collection(RATING_PATH).document(rating_id).delete(), callback);
    }


    // that update rather than replace.

    LiveData<Comment[]> getCommentByMovieID(String movie_id) {
        if (!commentsByMovie_id.containsKey(movie_id))
            commentsByMovie_id.put(movie_id, listenToResources(COMMENT_PATH, MOVIE_ID_FIELD, movie_id, Comment.class));

        return commentsByMovie_id.get(movie_id);
    }

    LiveData<Comment[]> getCommentsByUserID(String user_id) {
        if (!commentsByUser_id.containsKey(user_id))
            commentsByUser_id.put(user_id, listenToResources(COMMENT_PATH, USER_ID_FIELD, user_id, Comment.class));

        return commentsByUser_id.get(user_id);
    }

    LiveData<Rating[]> getRatingByUserID(String user_id) {
        if (!ratingByUser_id.containsKey(user_id))
            ratingByUser_id.put(user_id, listenToResources(RATING_PATH, USER_ID_FIELD, user_id, Rating.class));

        return ratingByUser_id.get(user_id);
    }


    LiveData<Rating[]> getRatingByMovieID(String movie_id) {
        if (!ratingByMovie_id.containsKey(movie_id))
            ratingByMovie_id.put(movie_id, listenToResources(RATING_PATH, MOVIE_ID_FIELD, movie_id, Rating.class));

        return ratingByMovie_id.get(movie_id);
    }

    LiveData<PublicProfile> getPublicProfile(String user_id) {
        if (!publicProfiles.containsKey(user_id)) {
            MutableLiveData<PublicProfile> publicProfile = new MutableLiveData<>();
            fireStore.collection(USER_PATH).document(user_id).addSnapshotListener((results, error) -> {
                if (error != null)
                    return;
                Object object = results.get(USER_PROFILE_URI_ID_FIELD);
                publicProfile.postValue(new PublicProfile(object != null ? Uri.parse((String) object) : null, results.get(USER_USERNAME_ID_FIELD, String.class)));
            });
            publicProfiles.put(user_id, publicProfile);
        }
        return publicProfiles.get(user_id);
    }


    //Creates a listener for a specific resource and makes it update a liveData object with the result;
    //The function returns a livedata object wich autoupdates based on the parameters provided
    private <T> MutableLiveData<T[]> listenToResources(String path, String field, String id, Class<T> type) {

        MutableLiveData<T[]> dataList = new MutableLiveData<>();

        Query query = fireStore.collection(path).whereEqualTo(field, id);
        query.addSnapshotListener((value, exception) -> {
            if (exception != null) {
                Log.w("FireStore", "Listen failed", exception);
                return;
            }
            try {
                List<T> newSet = new ArrayList<>();
                for (QueryDocumentSnapshot doc : value) {
                    newSet.add(doc.toObject(type));
                }

                dataList.postValue(newSet.toArray((T[]) Array.newInstance(type, newSet.size())));
            } catch (Exception e) {
                dataList.postValue(((T[])Array.newInstance(type, 0)));
                Log.e("FireStore", "Could not parse response into registered type");
            }

        });

        return dataList;
    }

    //Attaches a callback to a task if its not null
    private void attachCallback(Task task, OnCompleteListener callback) {
        if (callback != null)
            task.addOnCompleteListener(callback);
    }

    //Checks if an URI is a resource on the device
    private boolean isUriLocal(Uri uri) {
        if (uri == null)
            return false;
        if (uri.toString().startsWith(ANDROID_RESOURCE_PREFIX))
            return true;
        return false;

    }
}
