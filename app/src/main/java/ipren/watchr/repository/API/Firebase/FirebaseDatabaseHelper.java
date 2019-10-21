package ipren.watchr.repository.API.Firebase;

import android.net.Uri;
import android.util.Log;
import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ipren.watchr.dataHolders.CurrentUser;
import ipren.watchr.dataHolders.FireComment;
import ipren.watchr.dataHolders.FireRating;
import ipren.watchr.dataHolders.PublicProfile;
import ipren.watchr.dataHolders.User;

public class FirebaseDatabaseHelper {

    //These paths are placed on root.
    private final String RATING_PATH = "ratings";
    private final String COMMENT_PATH = "comments";
    private final String USER_PATH = "users";

    //This collection is located in root/user/movie_list
    private final String MOVIE_COLLECTION = "movie_lists";
    // movie_list/list_data/..list of movies
    private final String MOVIE_COLLECTION_DATA = "list_data";

    //static JS fields
    private final String USER_ID_FIELD = "user_id";
    private final String MOVIE_ID_FIELD = "movie_id";
    private final String COMMENT_TXT_FIELD = "text";


    FirebaseFirestore fireStore;

    private HashMap<String, MutableLiveData<FireComment[]>> commentsByMovie_id = new HashMap<>();
    private HashMap<String, MutableLiveData<FireComment[]>> commentsByUser_id = new HashMap<>();

    private HashMap<String, MutableLiveData<FireRating[]>> ratingByMovie_id = new HashMap<>();
    private HashMap<String, MutableLiveData<FireRating[]>> ratingByUser_id = new HashMap<>();

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
        userData.put("username", user.getUserName());
        userData.put("photoUri", uri != null && !isUriLocal(user.getUserProfilePictureUri()) ? uri.toString() : null);
        fireStore.collection("users").document(user.getUID()).set(userData, SetOptions.merge());
    }


    public void saveMovieToList(String list, String movie_id, String user_id, OnCompleteListener callback) {

        fireStore.collection(USER_PATH).document(user_id).collection(MOVIE_COLLECTION)
                .document(list).update("movies", FieldValue.arrayUnion(movie_id))
                .addOnCompleteListener(e -> {
                    if (e.getException() != null && e.getException().getLocalizedMessage().startsWith("NOT_FOUND: No document to update: ")) {
                        HashMap<String, List<String>> entry = new HashMap<>();
                        List<String> data = new LinkedList<>();
                        data.add(movie_id);
                        entry.put("movies", data);
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
        Task task = fireStore.collection(USER_PATH).document(user_id).collection(MOVIE_COLLECTION).document(list).update("movies", FieldValue.arrayRemove(movie_id));
        attachCallback(task, callback);

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
        comment.put(COMMENT_TXT_FIELD, text);

        Task task = fireStore.collection(COMMENT_PATH).add(comment);
        attachCallback(task, callback);

    }

    void removeComment(String comment_id, OnCompleteListener callback) {
        Task task = fireStore.collection(COMMENT_PATH).document(comment_id).delete();
        attachCallback(task, callback);

    }

    void addRating(int score, String movie_id, String user_id, OnCompleteListener callback) {
        Map<String, Object> rating = new HashMap<>();

        rating.put(USER_ID_FIELD, user_id);
        rating.put(MOVIE_ID_FIELD, movie_id);
        rating.put("score", new Integer(score));

        //This method updates existing documents if they exists or creates if they dont. Also removes duplicates if there are any
        fireStore.collection(RATING_PATH).whereEqualTo(MOVIE_ID_FIELD, movie_id).whereEqualTo(USER_ID_FIELD, user_id).get().addOnCompleteListener( e -> {
            if(e.isSuccessful()){
                if(e.getResult().isEmpty()){
                    attachCallback( fireStore.collection(RATING_PATH).add(rating), callback);
                } else {
                    boolean first = true;
                    for (DocumentSnapshot doc : e.getResult()){
                        if(first) {
                            attachCallback(doc.getReference().set(rating), callback);
                            first = false;
                        }else {
                            doc.getReference().delete();
                        }
                    }
                }

            }else if (callback != null)
                callback.onComplete(e);
        });


    //    Task task = fireStore.collection(RATING_PATH).add(rating);
      //  attachCallback(task, callback);
    }

    void removeRating(String rating_id, OnCompleteListener callback) {
        Task task = fireStore.collection(RATING_PATH).document(rating_id).delete();
        attachCallback(task, callback);
    }


    // that update rather than replace.

    LiveData<FireComment[]> getCommentByMovieID(String movie_id) {
        if (!commentsByMovie_id.containsKey(movie_id))
            commentsByMovie_id.put(movie_id, listenToResources(COMMENT_PATH, MOVIE_ID_FIELD, movie_id, FireComment.class));

        return commentsByMovie_id.get(movie_id);
    }

    LiveData<FireComment[]> getCommentsByUserID(String user_id) {
        if (!commentsByUser_id.containsKey(user_id))
            commentsByUser_id.put(user_id, listenToResources(COMMENT_PATH, USER_ID_FIELD, user_id, FireComment.class));

        return commentsByUser_id.get(user_id);
    }

    LiveData<FireRating[]> getRatingByUserID(String user_id) {

        if (!ratingByUser_id.containsKey(user_id))
            ratingByUser_id.put(user_id, listenToResources(RATING_PATH, USER_ID_FIELD, user_id, FireRating.class));

        return ratingByUser_id.get(user_id);
    }


    LiveData<FireRating[]> getRatingByMovieID(String movie_id) {
        if (!ratingByMovie_id.containsKey(movie_id))
            ratingByMovie_id.put(movie_id, listenToResources(RATING_PATH, MOVIE_ID_FIELD, movie_id, FireRating.class));

        return ratingByMovie_id.get(movie_id);
    }

    LiveData<PublicProfile> getPublicProfile(String user_id) {
        if (!publicProfiles.containsKey(user_id)) {
            MutableLiveData<PublicProfile> publicProfile = new MutableLiveData<>();
            fireStore.collection(USER_PATH).document(user_id).addSnapshotListener((results, error) -> {
                if (error != null)
                    return;
                Object object = results.get("photoUri");
                publicProfile.postValue(new PublicProfile(object != null ? Uri.parse((String) object) : null, results.get("username", String.class)));
            });
            publicProfiles.put(user_id, publicProfile);
        }
        return publicProfiles.get(user_id);
    }


    private <T> MutableLiveData<T[]> listenToResources(String path, String field, String id, Class<T> type) {

        MutableLiveData<T[]> dataList = new MutableLiveData<>(null);

        Query query = fireStore.collection(path).whereEqualTo(field, id);
        query.addSnapshotListener((value, exception) -> {
            if (exception != null) {
                Log.w("Firestore", "Listen failed", exception);
                return;
            }
            try {
                List<T> newSet = new ArrayList<>();
                for (QueryDocumentSnapshot doc : value) {
                    Log.e("Firebase", doc.toString());
                    newSet.add(doc.toObject(type));
                }

                dataList.postValue(newSet.toArray((T[]) Array.newInstance(type, newSet.size())));
            } catch (Exception e) {
                Log.e("Firebase", "Could not parse response into registered type");
            }

        });

        return dataList;
    }

    private void attachCallback(Task task, OnCompleteListener callback) {
        if (callback != null)
            task.addOnCompleteListener(callback);
    }

    private boolean isUriLocal(Uri uri) {
        if (uri == null)
            return false;
        if (uri.toString().startsWith("android.resource://"))
            return true;
        return false;

    }


    class UserLiveData extends MutableLiveData<CurrentUser> {

        ListenerRegistration movieCollectionListener;
        ListenerRegistration commentListener;
        ListenerRegistration movieRatingListener;
        HashMap<String, ListenerRegistration> movieListListener = new HashMap<>();

        HashMap<String, String[]> movieLists = new HashMap<>();
        FireComment[] fireComments = new FireComment[0];
        FireRating[] fireRatings = new FireRating[0];
        User user;

        @Override
        public void postValue(CurrentUser user) {
            this.user = user;
            unregisterAllListener();

            fireStore.collection(USER_PATH).document(user.getUID()).collection(MOVIE_COLLECTION).addSnapshotListener((result, error) -> {
                if (error != null)
                    return;
                movieLists.clear();

                for (DocumentSnapshot doc : result.getDocuments()) {

                    try {
                        List<String> movies = (List<String>) doc.get("movies");
                        movieLists.put(doc.getId(), movies.toArray(new String[movies.size()]));
                    } catch (Exception e) {
                        movieLists.put(doc.getId(), new String[0]);
                        Log.e("Firebase", "Could not parse array, res");
                    }
                }
                updateUser();

            });
            commentListener = fireStore.collection("comments").whereEqualTo(USER_ID_FIELD, user.getUID()).addSnapshotListener((res, error) -> {
                if (error != null)
                    return;

                List<FireComment> newSet = new ArrayList<>();
                assert res != null;
                for (QueryDocumentSnapshot doc : res) {
                    newSet.add(doc.toObject(FireComment.class));
                }
                FireComment[] comments = newSet.toArray(new FireComment[0]);
                fireComments = comments;
                updateUser();
            });

            movieRatingListener = fireStore.collection(RATING_PATH).whereEqualTo(USER_ID_FIELD, user.getUID()).addSnapshotListener((result, error) -> {
                if (error != null)
                    return;

                List<FireRating> newSet = new ArrayList<>();
                assert result != null;
                for (QueryDocumentSnapshot doc : result) {
                    newSet.add(doc.toObject(FireRating.class));
                }
                FireRating[] ratings = newSet.toArray(new FireRating[newSet.size()]);
                fireRatings = ratings;
                updateUser();
            });

        }

        private void updateUser() {
            super.postValue(new CurrentUser(user, fireRatings, fireComments, movieLists));
        }

        void unregisterAllListener() {
            if (movieCollectionListener != null)
                movieCollectionListener.remove();
            if (movieListListener != null) {
                for (String key : movieListListener.keySet())
                    movieListListener.remove(key);
                movieListListener.clear();
            }
            if (commentListener != null)
                commentListener.remove();

            if (movieRatingListener != null)
                movieRatingListener.remove();
        }


    }

}
