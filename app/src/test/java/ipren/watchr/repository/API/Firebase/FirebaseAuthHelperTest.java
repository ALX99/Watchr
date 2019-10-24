package ipren.watchr.repository.API.Firebase;
import android.net.Uri;
import android.net.wifi.hotspot2.pps.Credential;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.*;
import ipren.watchr.MockClasses.MockTaskResponse;
import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(FirebaseStorage.class)
public class FirebaseAuthHelperTest {

    private boolean callbackExecuted = false;
    private boolean callback2Executed = false;
    private boolean callback3Executed = false;
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseAuthHelper firebaseAuthHelper;
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();




    @Before
    public void setUp() {
        callbackExecuted = false;
        callback2Executed = false;
        callback3Executed = false;
        mAuth = mock(FirebaseAuth.class);
        user = mock(FirebaseUser.class);
        firebaseAuthHelper = new FirebaseAuthHelper(mAuth);
    }

    @Test
    public void refreshUsr() {
        when(mAuth.getCurrentUser()).thenReturn(null);
        firebaseAuthHelper.getUser().observeForever(e -> {
            if (e == null)
                callbackExecuted = true;
            else
                callback2Executed = true;
        });
        firebaseAuthHelper.refreshUsr();
        when(mAuth.getCurrentUser()).thenReturn(user);
        when(user.reload()).thenReturn(new MockTaskResponse().setResult(true).setResult(user));
        firebaseAuthHelper.refreshUsr();
        assertTrue(callbackExecuted);
        assertTrue(callback2Executed);

    }

    @Test
    public void resendVerificationEmail() {
        when(mAuth.getCurrentUser()).thenReturn(null);
        firebaseAuthHelper.resendVerificationEmail(e -> {
            if (e == null)
                callbackExecuted = true;
        });
        when(mAuth.getCurrentUser()).thenReturn(user);
        when(user.isEmailVerified()).thenReturn(true);
        firebaseAuthHelper.resendVerificationEmail(e -> {
            if (e == null)
                callback2Executed = true;
        });
        when(user.isEmailVerified()).thenReturn(false);
        when(user.sendEmailVerification()).thenReturn(new MockTaskResponse().setSuccessful(true));
        firebaseAuthHelper.resendVerificationEmail(e -> {
            if (e.isSuccessful())
                callback3Executed = true;
        });
        assertTrue(callbackExecuted);
        assertTrue(callback2Executed);
        assertTrue(callback3Executed);
    }

    @Test
    public void getUser() {
        String testUID = "TESTUID";
        assertNull(firebaseAuthHelper.getUser().getValue());
        when(mAuth.getCurrentUser()).thenReturn(user);
        when(user.getUid()).thenReturn(testUID);
        when(user.reload()).thenReturn(new MockTaskResponse().setResult(true).setResult(user));
        firebaseAuthHelper.refreshUsr();
        assertEquals(testUID, firebaseAuthHelper.getUser().getValue().getUID());
        when(mAuth.getCurrentUser()).thenReturn(null);
        firebaseAuthHelper.refreshUsr();
        assertNull(firebaseAuthHelper.getUser().getValue());

    }

    @Test
    public void loginUser() {
        String email = "test@email.com";
        String password = "123456";
        when(mAuth.signInWithEmailAndPassword(anyString(), anyString())).then(e -> {
            if (e.getArgument(0).equals(email) && e.getArgument(1).equals(password)) {
                callbackExecuted = true;
                return new MockTaskResponse().setSuccessful(true);
            }
            fail();
            return null;

        });
        firebaseAuthHelper.loginUser(email, password, e -> {
            if (e.isSuccessful())
                callback2Executed = true;
            else
                fail();
        });
        assertTrue(callbackExecuted);
        assertTrue(callback2Executed);
    }

    @Test
    public void registerUser() {
        String email = "email@TEST.com";
        String password = "654321";
        doAnswer(e -> {
            callback3Executed = true;
            return null;
        }).when(user).sendEmailVerification(); //Checks if emailVerification has been sent

        AuthResult result = mock(AuthResult.class);
        when(result.getUser()).thenReturn(user);
        when(mAuth.createUserWithEmailAndPassword(anyString(), anyString())).then(e -> {
                if(e.getArgument(0).equals(email) && e.getArgument(1).equals(password)){
                    callbackExecuted = true;
                    return new MockTaskResponse().setSuccessful(true).setResult(result);
                }else{
                    fail();
                    return null;
                }
        });
        firebaseAuthHelper.registerUser(email, password, e -> {
            if(e.isSuccessful())
                callback2Executed = true;
            else
                fail();
        });
        firebaseAuthHelper.registerUser(email, password, null); //Checking if it can handle null callback
        assertTrue(callbackExecuted);
        assertTrue(callback2Executed);
        assertTrue(callback3Executed);
    }

    @Test
    public void signOut() {
        doAnswer(e -> {
            callbackExecuted = true;
            return null;
        }).when(mAuth).signOut();
        firebaseAuthHelper.signOut();
        assertTrue(callbackExecuted);

    }

    @Test
    public void resetPassword() {
        String emailTest = "email@TEST.com";
        when(mAuth.sendPasswordResetEmail(anyString())).then(e -> {
            if (e.getArgument(0).equals(emailTest)) {
                callbackExecuted = true;
                return new MockTaskResponse().setSuccessful(true);
            } else
                fail();
            return null;
        });
        firebaseAuthHelper.resetPassword(emailTest, e -> {
            if (e.isSuccessful())
                callback2Executed = true;
            else
                fail();
        });
        assertTrue(callbackExecuted);
        assertTrue(callback2Executed);

    }

    //TODO FINNISH THIS TEST
    @Test
    public void updateProfile() {
        String UID = "testUID1234";
        String FIRESTORE_PIC_FOLDER = "pics/";
        Uri providedUri = Uri.parse("www.testUri/pic");
        String providedUsername = "usernameTest";
        when(user.getUid()).thenReturn(UID);
        when(user.reload()).thenReturn(new MockTaskResponse().setSuccessful(true));
        when(user.updateProfile(any(UserProfileChangeRequest.class))).then(e -> {
            UserProfileChangeRequest change = e.getArgument(0);
            if(providedUri != null && !change.getPhotoUri().equals(providedUri)){
                fail();
                return null;
            }else if(providedUsername != null && !providedUsername.equals(change.getDisplayName())){
                fail();
                return null;
            }else
                return new MockTaskResponse().setSuccessful(true);
        });
        when(mAuth.getCurrentUser()).thenReturn(user);
        PowerMockito.mockStatic(FirebaseStorage.class);
        StorageReference storageReference = mock(StorageReference.class);
        when(storageReference.child(anyString())).then(e -> {
            if(e.getArgument(0).equals(FIRESTORE_PIC_FOLDER + UID)){
                StorageReference stor =  mock(StorageReference.class);
                when(stor.putFile(any(Uri.class))).then(e2 -> {
                    if(e2 == null)
                        fail();
                    else if(e.equals(providedUri))
                        return new MockTaskResponse().setSuccessful(true);
                    return null;
                });
                return stor;
            }else{
                fail();
                return null;
            }
        });
        FirebaseStorage fireStorage = mock(FirebaseStorage.class);
        when(fireStorage.getReference()).thenReturn(storageReference);
        BDDMockito.given(FirebaseStorage.getInstance()).willReturn(fireStorage);

        firebaseAuthHelper.updateProfile(providedUsername, providedUri, e -> {
            if(e.isSuccessful())
                callbackExecuted = true;
        });


    }

    @Test
    public void changePassword() {

        String oldPassword = "123456";
        String newPassword = "654321";

        AuthCredential cred = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);
        String s = cred.getClass().toString();


        when(user.reauthenticate(any(AuthCredential.class))).then(e -> {
            if(e.getArgument(0) instanceof EmailAuthCredential) {
                callbackExecuted = true;
                return new MockTaskResponse().setSuccessful(true);
            }else{
                fail();
                return null;
            }
        });
        when(user.updatePassword(anyString())).then(e -> {
            if(e.getArgument(0).equals(newPassword)){
                callback2Executed = true;
                return new MockTaskResponse().setSuccessful(true);
            }else{
                fail();
                return null;
            }
        });
        when(mAuth.getCurrentUser()).thenReturn(null);
        firebaseAuthHelper.changePassword(oldPassword, newPassword, e -> {
            if(e == null)
                callback2Executed = true;
        });
        firebaseAuthHelper.changePassword(oldPassword, newPassword, null);
        when(mAuth.getCurrentUser()).thenReturn(user);
        firebaseAuthHelper.changePassword(oldPassword, newPassword, e -> {
            if(e.isSuccessful())
                callback3Executed = true;
            else
                fail();
        });
        assertTrue(callbackExecuted);
        assertTrue(callback2Executed);
        assertTrue(callback3Executed);
        firebaseAuthHelper.changePassword(oldPassword, newPassword, null);

    }
}