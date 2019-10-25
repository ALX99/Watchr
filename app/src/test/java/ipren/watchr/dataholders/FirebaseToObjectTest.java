package ipren.watchr.dataholders;

import com.google.firebase.firestore.DocumentId;

import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

//This class checks Classes that have toObject called on them by firebase, making sure they
// have the correct fieldnames....
public class FirebaseToObjectTest {


    @Test
    public void commentObjectTest() {
        //Firebase signature
        String MOVIE_ID = "movie_id"; //String
        String USER_ID = "user_id"; // String
        String SCORE_ID = "score";   //int
        //UID not relevant check annotation    @DocumentId

        String[] stringFields = {MOVIE_ID, USER_ID}; //These fields are strings in the database
        String[] intFields = {SCORE_ID};              //This field is a number in the database
        String[] allFields = {MOVIE_ID, USER_ID, SCORE_ID}; //All fields used for other variables in the database

        areFieldsInClassTest(Rating.class, String.class, stringFields); //Check if the string fields are in the class
        areFieldsInClassTest(Rating.class, int.class, intFields);           //Check if the int field is in the class
        classHasUnusedAnnotatedFieldTest(Rating.class, DocumentId.class, allFields);  //Check if a field has the annotation provided, but at the same time is not a used field.
    }

    @Test
    public void ratingObjectTest() {
        //Firebase entry signature
        String MOVIE_ID = "movie_id"; // String
        String USER_ID = "user_id"; // String
        String TEXT_ID = "text"; // String
        String DATE_ID = "date_created"; // Date
        //UID not relevant check annotation    @DocumentId

        String[] stringFields = {MOVIE_ID, USER_ID, TEXT_ID,};  //These fields are strings in the database
        String[] dateFields = {DATE_ID};                          //This field is a Timestamp  in the database conversion to Date is possible
        String[] allFields = {MOVIE_ID, USER_ID, TEXT_ID, DATE_ID}; //All fields used for other variables in the database


        areFieldsInClassTest(Comment.class, String.class, stringFields); //Check if the string(s) fields are in the class
        areFieldsInClassTest(Comment.class, Date.class, dateFields);  //Check if the Date field(s) is in the class
        classHasUnusedAnnotatedFieldTest(Comment.class, DocumentId.class, allFields);  //Check if a field has the annotation provided, but at the same time is not a used field.

    }

    // This class checks if there is a field with the provided annotation, and checks if that field is not a used variable
    private void classHasUnusedAnnotatedFieldTest(Class testClass, Class annotation, String... usedFields) {
        for (Field field : testClass.getDeclaredFields()) {
            if (fieldHasAnnotation(field, DocumentId.class) && !Arrays.asList(usedFields).contains(field.getName())) {
                return;
            }
        }
        fail();
    }

    //Checks if the provided fields are in the provided class and that they have the correct type
    private void areFieldsInClassTest(Class testClass, Class fieldType, String... fieldNames) {

        for (String fieldName : fieldNames) {
            Field field = findField(testClass, fieldName);
            assertNotNull(field);
            assertTrue(Modifier.isPrivate(field.getModifiers()));
            assertEquals(field.getType(), fieldType);
        }

    }

    //Checks if a field has the provided annotation if so returns true
    private boolean fieldHasAnnotation(Field field, Class expAnnotation) {
        for (Annotation annotation : field.getAnnotations()) {
            if (annotation.annotationType().equals(expAnnotation))
                return true;
        }
        return false;
    }

    //Fins a field matching the provided name in a class, if it cant be found returns null
    private Field findField(Class testClass, String fieldName) {
        for (Field field : testClass.getDeclaredFields()) {
            if (field.getName().equals(fieldName))
                return field;
        }
        return null;

    }
}
