package ipren.watchr.repository.API.Firebase;

import android.net.Uri;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ipren.watchr.MockClasses.MockTaskResponse;
import ipren.watchr.dataHolders.Comment;
import ipren.watchr.dataHolders.PublicProfile;
import ipren.watchr.dataHolders.Rating;
import ipren.watchr.dataHolders.User;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Uri.class, FieldValue.class})
public class FireBaseDatabaseHelperTest {

    private final String RATING_PATH = "ratings";
    private final String COMMENT_PATH = "comments";
    private final String USER_PATH = "users";
    private String TEST_USERNAME = "testUsername";
    private String TEST_UID = "UID_Test";
    private String TEST_USER_UID = "USER_UID_TEST";
    private String TEST_MOVIE_ID = "MOVIE_ID_TEST";
    private String TEST_TEXT = "TEST_TEXT";
    private String TEST_VALID_URL = "avalidURI.com/pic";
    private int score = 0;

    private final String TEST_MOVIE_LIST = "TEST_MOVIE_LIST";

    private final String USER_USERNAME_ID_FIELD = "username";
    private final String USER_PROFILE_URI_ID_FIELD = "photoUri";
    private final String MOVIE_COLLECTION = "movie_lists";
    private final String MOVIE_ARRAY_ID_FIELD = "movies";
    private final String USER_ID_FIELD = "user_id";
    private final String MOVIE_ID_FIELD = "movie_id";
    private final String COMMENT_TXT_ID_FIELD = "text";
    private final String SCORE_ID_FIELD = "score";

    private final String DATE_CREATED_ID_FIELD = "date_created";


    private boolean methodExecuted = false;
    private boolean method2Executed = false;
    private boolean method3Executed = false;
    FireBaseDatabaseHelper firebaseDatabaseHelper;
    //   private SetOptions setOptionsReferenceTest = mock(SetOptions.class);
    private Uri TEST_URI1 = mock(Uri.class);
    private Uri TEST_URI2 = mock(Uri.class);
    boolean switchBool = false;

    private int counter = 0;
    private int counter2 = 0;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() {
        methodExecuted = false;
        method2Executed = false;
        method3Executed = false;
        TEST_USERNAME = "testUsername";
        TEST_UID = "UID_Test";
        TEST_USER_UID = "USER_UID_TEST";
        TEST_MOVIE_ID = "MOVIE_ID_TEST";
        TEST_TEXT = "TEST_TEXT";
        TEST_URI1 = mock(Uri.class);
        TEST_URI2 = mock(Uri.class);
        counter = 0;
        counter2 = 0;
        score = 0;
        switchBool = false;
    }

    @Test
    public void syncUserWithDatabase() {
        HashMap<String, Object> testMap = new HashMap<>();

        FirebaseFirestore firestore = mock(FirebaseFirestore.class);
        when(firestore.collection(anyString())).then(e -> {
            fail();
            return null;
        });
        firebaseDatabaseHelper = new FireBaseDatabaseHelper(firestore);
        firebaseDatabaseHelper.syncUserWithDatabase(null);

        DocumentReference documentReference = mock(DocumentReference.class);
        when(documentReference.set(any(Map.class), any(SetOptions.class))).then(e -> {
            Map<String, Object> arg1 = e.getArgument(0);
            Object arg2 = e.getArgument(1);
            if (arg1 != null && arg2.equals(SetOptions.merge())) {
                assertTrue(arg1.containsKey(USER_USERNAME_ID_FIELD));
                assertTrue(arg1.containsKey(USER_PROFILE_URI_ID_FIELD));
                assertEquals(TEST_USERNAME, arg1.get(USER_USERNAME_ID_FIELD));
                if (TEST_URI1 == null || TEST_URI1.toString().startsWith("android.resource://")) {
                    assertNull(arg1.get(USER_PROFILE_URI_ID_FIELD));
                } else {
                    assertTrue(TEST_URI1.toString().equals(arg1.get(USER_PROFILE_URI_ID_FIELD).toString()));
                }

                counter++;
                return null;
            } else {
                fail();
                return null;
            }
        });
        CollectionReference collection = mock(CollectionReference.class);
        mockGetDocument(TEST_UID, documentReference, collection);
        firestore = mock(FirebaseFirestore.class);
        mockGetCollectionByFireStore(USER_PATH, collection, firestore);

        firebaseDatabaseHelper = new FireBaseDatabaseHelper(firestore);
        when(TEST_URI1.toString()).thenReturn("android.resource://path/to/local/resource");
        firebaseDatabaseHelper.syncUserWithDatabase(new User(TEST_USERNAME, "not used", TEST_URI1, TEST_UID, false));
        TEST_URI1 = null;
        firebaseDatabaseHelper.syncUserWithDatabase(new User(TEST_USERNAME, "not used", TEST_URI1, TEST_UID, false));
        TEST_URI1 = mock(Uri.class);
        when(TEST_URI1.toString()).thenReturn("www.notalocalres/pic");
        firebaseDatabaseHelper.syncUserWithDatabase(new User(TEST_USERNAME, "not used", TEST_URI1, TEST_UID, false));
        assertEquals(3, counter);


    }

    @Test
    public void saveMovieToList() {
        FirebaseFirestore firestore = mock(FirebaseFirestore.class);
        CollectionReference userCollection = mock(CollectionReference.class);
        DocumentReference userIdDoc = mock(DocumentReference.class);
        CollectionReference userMovieLists = mock(CollectionReference.class);
        DocumentReference movieListDoc = mock(DocumentReference.class);

        mockGetCollectionByFireStore(USER_PATH, userCollection, firestore);
        mockGetDocument(TEST_USER_UID, userIdDoc, userCollection);
        mockGetCollectionByDocument(MOVIE_COLLECTION, userMovieLists, userIdDoc);
        mockGetDocument(TEST_MOVIE_LIST, movieListDoc, userMovieLists);
        PowerMockito.mockStatic(FieldValue.class);
        BDDMockito.given(FieldValue.arrayUnion(anyString())).willAnswer(e -> {
            if (e.getArgument(0).equals(TEST_MOVIE_ID)) {
                counter2++;
                return e.callRealMethod();
            } else
                fail();
            return null;
        });
        when(movieListDoc.update(anyString(), any(FieldValue.class))).then(e -> {
            String fieldName = e.getArgument(0);
            if (fieldName.equals(MOVIE_ARRAY_ID_FIELD)) {
                counter++;
                return new MockTaskResponse().setSuccessful(true);
            } else {
                fail();
            }
            return null;
        });

        FireBaseDatabaseHelper firebaseDatabaseHelper = new FireBaseDatabaseHelper(firestore);
        firebaseDatabaseHelper.saveMovieToList(TEST_MOVIE_LIST, TEST_MOVIE_ID, TEST_USER_UID, null);
        firebaseDatabaseHelper.saveMovieToList(TEST_MOVIE_LIST, TEST_MOVIE_ID, TEST_USER_UID, e -> {
            if (e.isSuccessful())
                methodExecuted = true;
        });

        assertEquals(2, counter);
        assertEquals(counter, counter2);
    }

    @Test
    public void deleteMovieFromList() {
        FirebaseFirestore firestore = mock(FirebaseFirestore.class);
        CollectionReference userCollection = mock(CollectionReference.class);
        DocumentReference userIdDoc = mock(DocumentReference.class);
        CollectionReference userMovieLists = mock(CollectionReference.class);
        DocumentReference movieListDoc = mock(DocumentReference.class);

        mockGetCollectionByFireStore(USER_PATH, userCollection, firestore);
        mockGetDocument(TEST_USER_UID, userIdDoc, userCollection);
        mockGetCollectionByDocument(MOVIE_COLLECTION, userMovieLists, userIdDoc);
        mockGetDocument(TEST_MOVIE_LIST, movieListDoc, userMovieLists);
        PowerMockito.mockStatic(FieldValue.class);
        BDDMockito.given(FieldValue.arrayRemove(anyString())).willAnswer(e -> {
            if (e.getArgument(0).equals(TEST_MOVIE_ID)) {
                counter2++;
                return e.callRealMethod();
            } else
                fail();
            return null;
        });
        when(movieListDoc.update(anyString(), any(FieldValue.class))).then(e -> {
            String fieldName = e.getArgument(0);
            if (fieldName.equals(MOVIE_ARRAY_ID_FIELD)) {
                counter++;
                return new MockTaskResponse().setSuccessful(true);
            } else {
                fail();
            }
            return null;
        });

        FireBaseDatabaseHelper firebaseDatabaseHelper = new FireBaseDatabaseHelper(firestore);
        firebaseDatabaseHelper.deleteMovieFromList(TEST_MOVIE_LIST, TEST_MOVIE_ID, TEST_USER_UID, null);
        firebaseDatabaseHelper.deleteMovieFromList(TEST_MOVIE_LIST, TEST_MOVIE_ID, TEST_USER_UID, e -> {
            if (e.isSuccessful())
                methodExecuted = true;
        });

        assertEquals(2, counter);
        assertEquals(counter, counter2);
    }


    @Test
    public void addComment() {

        FirebaseFirestore firestore = mock(FirebaseFirestore.class);
        CollectionReference pathCollection = mock(CollectionReference.class);
        when(pathCollection.add(anyMap())).then(e -> {
            Map<String, Object> map = e.getArgument(0);
            assertEquals(TEST_USER_UID, map.get(USER_ID_FIELD));
            assertEquals(TEST_USER_UID, map.get(USER_ID_FIELD));
            assertEquals(TEST_USER_UID, map.get(USER_ID_FIELD));
            assertTrue(map.containsKey(DATE_CREATED_ID_FIELD));
            assertNotNull(map.containsKey(DATE_CREATED_ID_FIELD));
            counter++;
            return new MockTaskResponse().setSuccessful(true);
        });
        mockGetCollectionByFireStore(COMMENT_PATH, pathCollection, firestore);
        new FireBaseDatabaseHelper(firestore).addComment(TEST_TEXT, TEST_MOVIE_ID, TEST_USER_UID, null);
        new FireBaseDatabaseHelper(firestore).addComment(TEST_TEXT, TEST_MOVIE_ID, TEST_USER_UID, e -> {
            methodExecuted = true;
        });
        assertEquals(2, counter);
        assertTrue(methodExecuted);
    }

    @Test
    public void removeComment() {
        FirebaseFirestore firestore = mock(FirebaseFirestore.class);
        CollectionReference pathCollection = mock(CollectionReference.class);
        DocumentReference userCommentDOc = mock(DocumentReference.class);
        when(userCommentDOc.delete()).then(e -> {
            counter++;
            return new MockTaskResponse().setSuccessful(true);
        });
        mockGetCollectionByFireStore(COMMENT_PATH, pathCollection, firestore);
        mockGetDocument(TEST_UID, userCommentDOc, pathCollection);
        new FireBaseDatabaseHelper(firestore).removeComment(TEST_UID, null);
        new FireBaseDatabaseHelper(firestore).removeComment(TEST_UID, e -> {
            if (e.isSuccessful())
                methodExecuted = true;
        });
        assertEquals(2, counter);
        assertTrue(methodExecuted);
    }

    @Test
    public void removeRating() {
        FirebaseFirestore firestore = mock(FirebaseFirestore.class);
        CollectionReference pathCollection = mock(CollectionReference.class);
        DocumentReference userCommentDOc = mock(DocumentReference.class);
        mockGetCollectionByFireStore(RATING_PATH, pathCollection, firestore);
        mockGetDocument(TEST_UID, userCommentDOc, pathCollection);
        when(userCommentDOc.delete()).then(e -> {
            counter++;
            return new MockTaskResponse().setSuccessful(true);
        });

        new FireBaseDatabaseHelper(firestore).removeRating(TEST_UID, null);
        new FireBaseDatabaseHelper(firestore).removeRating(TEST_UID, e -> {
            if (e.isSuccessful())
                methodExecuted = true;
        });
        assertEquals(2, counter);
        assertTrue(methodExecuted);

    }

    @Test
    public void getPublicProfile() {

        FirebaseFirestore firestore = mock(FirebaseFirestore.class);
        CollectionReference pathCollection = mock(CollectionReference.class);
        DocumentReference userDoc = mock(DocumentReference.class);
        mockGetCollectionByFireStore(USER_PATH, pathCollection, firestore);
        mockGetDocument(USER_ID_FIELD, userDoc, pathCollection);
        DocumentSnapshot userSnap = mock(DocumentSnapshot.class);
        PowerMockito.mockStatic(Uri.class);
        BDDMockito.given(Uri.parse(anyString())).willAnswer(e -> {
            if (e.getArgument(0) != null) {
                Uri uri = mock(Uri.class);
                when(uri.toString()).thenReturn(e.getArgument(0));
                return uri;
            } else
                return null;

        });

        when(userDoc.addSnapshotListener(any())).then(e -> {
            ((EventListener<DocumentSnapshot>) e.getArgument(0)).onEvent(userSnap, null);
            counter++;
            return null;
        });
        when(userSnap.get(anyString())).then(e -> {
            if (e.getArgument(0).equals(USER_PROFILE_URI_ID_FIELD)) {
                return TEST_VALID_URL;
            } else if (e.getArgument(0).equals(USER_USERNAME_ID_FIELD)) {
                return TEST_USERNAME;
            } else
                fail();

            return null;

        });
        when(userSnap.get(anyString(), any(Class.class))).then(e -> {
            if (e.getArgument(0).equals(USER_PROFILE_URI_ID_FIELD)) {
                return TEST_VALID_URL;
            } else if (e.getArgument(0).equals(USER_USERNAME_ID_FIELD)) {
                return TEST_USERNAME;
            } else
                fail();

            return null;
        });
        FireBaseDatabaseHelper databaseHelper = new FireBaseDatabaseHelper(firestore);
        LiveData<PublicProfile> live = databaseHelper.getPublicProfile(USER_ID_FIELD);
        assertEquals(TEST_VALID_URL, live.getValue().getProfilePhotoUri().toString());
        assertEquals(TEST_USERNAME, live.getValue().getUsername());
        assertEquals(live, databaseHelper.getPublicProfile(USER_ID_FIELD));
        assertEquals(1, counter);
        TEST_VALID_URL = "anothervalid.com/pic";
        TEST_USERNAME = "TESTNAME";
        databaseHelper = new FireBaseDatabaseHelper(firestore);
        live = databaseHelper.getPublicProfile(USER_ID_FIELD);
        assertEquals(TEST_VALID_URL, live.getValue().getProfilePhotoUri().toString());
        assertEquals(TEST_USERNAME, live.getValue().getUsername());
        assertEquals(live, databaseHelper.getPublicProfile(USER_ID_FIELD));
        assertEquals(2, counter);


    }

    @Test
    public void addRating() {
        FirebaseFirestore firestore = mock(FirebaseFirestore.class);
        Query queryMovieID = mock(Query.class);
        Query queryUserID = mock(Query.class);
        CollectionReference ratingCol = mock(CollectionReference.class);
        mockGetCollectionByFireStore(RATING_PATH, ratingCol, firestore);
        mockQuery(MOVIE_ID_FIELD, TEST_MOVIE_ID, ratingCol, queryMovieID);
        mockQuery(USER_ID_FIELD, TEST_USER_UID, queryMovieID, queryUserID);
        when(queryUserID.get()).then(e -> {
            counter++;
            QuerySnapshot snapshot = mock(QuerySnapshot.class);
            when(snapshot.isEmpty()).thenReturn(true);
            return new MockTaskResponse().setSuccessful(true).setResult(snapshot);

        });
        when(ratingCol.add(any())).then(e -> {
            counter2++;
            Map map = e.getArgument(0);
            assertEquals(TEST_USER_UID, map.get(USER_ID_FIELD));
            assertEquals(TEST_MOVIE_ID, map.get(MOVIE_ID_FIELD));
            assertEquals(2, map.get(SCORE_ID_FIELD));
            return new MockTaskResponse().setSuccessful(true);
        });

        new FireBaseDatabaseHelper(firestore).addRating(2, TEST_MOVIE_ID, TEST_USER_UID, null);
        new FireBaseDatabaseHelper(firestore).addRating(2, TEST_MOVIE_ID, TEST_USER_UID, e -> {
            if (e.isSuccessful())
                methodExecuted = true;
        });
        assertTrue(methodExecuted);
        assertEquals(2, counter);
        assertEquals(2, counter2);

    }


    @Test
    public void getMovieListByUserID() {
        String movieID1= "AVATAR";
        String movieID2 = "TERMINATOR";
        String movieID3 = "INTERSTELLAR";
        FirebaseFirestore firestore = mock(FirebaseFirestore.class);
        CollectionReference userPathCol = mock(CollectionReference.class);
        DocumentReference userIdDoc = mock(DocumentReference.class);
        CollectionReference movieCollection = mock(CollectionReference.class);
        DocumentReference movieListDoc = mock(DocumentReference.class);

        mockGetCollectionByFireStore(USER_PATH, userPathCol, firestore);
        mockGetDocument(TEST_USER_UID, userIdDoc, userPathCol);
        mockGetCollectionByDocument(MOVIE_COLLECTION,movieCollection, userIdDoc );
        mockGetDocument(TEST_MOVIE_LIST, movieListDoc,movieCollection);

        when(movieListDoc.addSnapshotListener(any())).then( e -> {
            DocumentSnapshot docSnap = mock(DocumentSnapshot.class);
            when(docSnap.get(anyString())).then(e2 -> {
                counter++;
                if(e2.getArgument(0).equals(MOVIE_ARRAY_ID_FIELD)){
                    List<String> returnValue = new LinkedList<>();
                    returnValue.add(movieID1); returnValue.add(movieID2);returnValue.add(movieID3);
                    return returnValue;
                } else {
                    fail();
                    return null;
                }
            });

            EventListener<DocumentSnapshot> doc = e.getArgument(0);
            doc.onEvent(docSnap, null);
            return null;
        });

        FireBaseDatabaseHelper fDatabaseHelper = new FireBaseDatabaseHelper(firestore);
       LiveData<String[]> liveData = fDatabaseHelper.getMovieListByUserID(TEST_MOVIE_LIST, TEST_USER_UID);
       assertNotNull(liveData.getValue());
       assertEquals( movieID1, liveData.getValue()[0]);
        assertEquals( movieID2, liveData.getValue()[1]);
        assertEquals( movieID3, liveData.getValue()[2]);
        assertEquals(1, counter);
        assertEquals(liveData, fDatabaseHelper.getMovieListByUserID(TEST_MOVIE_LIST, TEST_USER_UID));
        assertEquals(liveData, fDatabaseHelper.getMovieListByUserID(TEST_MOVIE_LIST, TEST_USER_UID));
        assertEquals(1, counter);


    }

    //LAst
    @Test
    public void getCommentByMovieID() {
        FirebaseFirestore firestore = mock(FirebaseFirestore.class);
        mockListenToRes(COMMENT_PATH, MOVIE_ID_FIELD, TEST_MOVIE_ID, firestore);
        FireBaseDatabaseHelper databaseHelper = new FireBaseDatabaseHelper(firestore);
        assertEquals(0,counter);
        LiveData<Comment[]> firstValue = databaseHelper.getCommentByMovieID(TEST_MOVIE_ID);

        assertEquals(firstValue.getValue().getClass(), Comment[].class);
        assertEquals(0, firstValue.getValue().length);
        assertEquals(1,counter);
        assertEquals(firstValue,  databaseHelper.getCommentByMovieID(TEST_MOVIE_ID));
        assertEquals(firstValue,  databaseHelper.getCommentByMovieID(TEST_MOVIE_ID));
        assertEquals(1,counter);



     }

    @Test
    public void getCommentsByUserID() {
        FirebaseFirestore firestore = mock(FirebaseFirestore.class);
        mockListenToRes(COMMENT_PATH, USER_ID_FIELD, TEST_USER_UID, firestore);
        FireBaseDatabaseHelper databaseHelper = new FireBaseDatabaseHelper(firestore);
        assertEquals(0,counter);
        LiveData<Comment[]> firstValue = databaseHelper.getCommentsByUserID(TEST_USER_UID);
        assertTrue(firstValue.getValue().getClass().equals(Comment[].class) );
        assertEquals(0, firstValue.getValue().length);
        assertEquals(1,counter);
        assertEquals(firstValue,  databaseHelper.getCommentsByUserID(TEST_USER_UID));
        assertEquals(firstValue,  databaseHelper.getCommentsByUserID(TEST_USER_UID));
        assertEquals(1,counter);

    }

    @Test
    public void getRatingByUserID() {
        FirebaseFirestore firestore = mock(FirebaseFirestore.class);
        mockListenToRes(RATING_PATH, USER_ID_FIELD, TEST_USER_UID, firestore);
        FireBaseDatabaseHelper databaseHelper = new FireBaseDatabaseHelper(firestore);
        assertEquals(0,counter);
        LiveData<Rating[]> firstValue = databaseHelper.getRatingByUserID(TEST_USER_UID);
        assertEquals(firstValue.getValue().getClass(), Rating[].class);
        assertEquals(0, firstValue.getValue().length);
        assertEquals(1,counter);
        assertEquals(firstValue,  databaseHelper.getRatingByUserID(TEST_USER_UID));
        assertEquals(firstValue,  databaseHelper.getRatingByUserID(TEST_USER_UID));
        assertEquals(1,counter);

    }

    @Test
    public void getRatingByMovieID() {
        FirebaseFirestore firestore = mock(FirebaseFirestore.class);
        mockListenToRes(RATING_PATH, MOVIE_ID_FIELD, TEST_MOVIE_ID, firestore);
        FireBaseDatabaseHelper databaseHelper = new FireBaseDatabaseHelper(firestore);
        assertEquals(0,counter);
        LiveData<Rating[]> firstValue = databaseHelper.getRatingByMovieID(TEST_MOVIE_ID);
        assertEquals(firstValue.getValue().getClass(), Rating[].class);
        assertEquals(0, firstValue.getValue().length);
        assertEquals(1,counter);
        assertEquals(firstValue,  databaseHelper.getRatingByMovieID(TEST_MOVIE_ID));
        assertEquals(firstValue,  databaseHelper.getRatingByMovieID(TEST_MOVIE_ID));
        assertEquals(1,counter);

    }



    private void mockListenToRes(String PATH, String FIELD, String ID, FirebaseFirestore firestore){

        CollectionReference pathCol = mock(CollectionReference.class);
        mockGetCollectionByFireStore(PATH, pathCol, firestore);
        when(pathCol.whereEqualTo(anyString(), anyString())).then( e -> {
            String field = e.getArgument(0);
            String value = e.getArgument(1);
            if(field.equals(FIELD) && value.equals(ID)){
                counter++;
                Query query = mock(Query.class);
                when(query.addSnapshotListener(any())).then(e2 -> {
                    EventListener<QuerySnapshot> callb = e2.getArgument(0);
                    callb.onEvent(null, null); // Testing fallback value and with other logic
                    return null;
                });
                return query;
            }else
                fail();

            return null;
        });


    }

    private void mockGetDocument(String ID, DocumentReference returnValue, CollectionReference colRef) {
        when(colRef.document(anyString())).then(e -> {
            if (!e.getArgument(0).equals(ID)) {
                fail();
            } else {
                return returnValue;
            }
            return null;
        });

    }

    private void mockGetCollectionByFireStore(String path, CollectionReference returnValue, FirebaseFirestore firestore) {
        when(firestore.collection(anyString())).then(e -> {
            if (e.getArgument(0).equals(path)) {
                return returnValue;
            } else {
                fail();
                return null;
            }
        });
    }

    private void mockGetCollectionByDocument(String path, CollectionReference returnValue, DocumentReference docRef) {
        when(docRef.collection(anyString())).then(e -> {
            if (e.getArgument(0).equals(path)) {
                return returnValue;
            } else {
                fail();
                return null;
            }
        });
    }

    private void mockQuery(String exp1, String exp2, CollectionReference col, Query returnValue) {
        when(col.whereEqualTo(anyString(), anyString())).then(e -> {
            if (e.getArgument(0).equals(exp1) && e.getArgument(1).equals(exp2)) {
                return returnValue;
            } else {
                fail();
                return null;
            }

        });

    }

    private void mockQuery(String exp1, String exp2, Query query, Query returnValue) {
        when(query.whereEqualTo(anyString(), anyString())).then(e -> {
            if (e.getArgument(0).equals(exp1) && e.getArgument(1).equals(exp2)) {
                return returnValue;
            } else {
                fail();
                return null;
            }

        });

    }
}