package ipren.watchr.MockClasses;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.concurrent.Executor;

//TODO remake this test
public class MockTaskResponse extends Task {
    boolean isComplete = false;
    boolean isSuccessful = false;
    boolean isCanceled = false;
    Object result = null;
    Exception exception = null;

    @NonNull
    @Override
    public Task addOnCompleteListener(@NonNull OnCompleteListener onCompleteListener) {
        onCompleteListener.onComplete(this);
        return this;
    }

    @Override
    public boolean isComplete() {
        return isComplete;
    }

    public MockTaskResponse setComplete(boolean complete) {
        this.isComplete = complete;
        return this;
    }

    @Override
    public boolean isSuccessful() {
        return isSuccessful;
    }

    public MockTaskResponse setSuccessful(boolean successful) {
        this.isSuccessful = successful;
        return this;

    }

    @Override
    public boolean isCanceled() {
        return isCanceled;
    }

    public MockTaskResponse setCanceled(boolean canceled) {
        this.isCanceled = canceled;
        return this;
    }

    @Nullable
    @Override
    public Object getResult() {
        return result;
    }

    public MockTaskResponse setResult(Object result) {
        this.result = result;
        return this;
    }

    @Nullable
    @Override
    public Exception getException() {
        return exception;
    }

    public MockTaskResponse setException(Exception exception) {
        this.exception = exception;
        return this;
    }

    @NonNull
    @Override
    public Task addOnSuccessListener(@NonNull OnSuccessListener onSuccessListener) {
        return null;
    }

    @NonNull
    @Override
    public Task addOnSuccessListener(@NonNull Executor executor, @NonNull OnSuccessListener onSuccessListener) {
        return null;
    }

    @NonNull
    @Override
    public Task addOnSuccessListener(@NonNull Activity activity, @NonNull OnSuccessListener onSuccessListener) {
        return null;
    }

    @NonNull
    @Override
    public Task addOnFailureListener(@NonNull OnFailureListener onFailureListener) {
        return null;
    }

    @NonNull
    @Override
    public Task addOnFailureListener(@NonNull Executor executor, @NonNull OnFailureListener onFailureListener) {
        return null;
    }

    @NonNull
    @Override
    public Task addOnFailureListener(@NonNull Activity activity, @NonNull OnFailureListener onFailureListener) {
        return null;
    }

    @Nullable
    @Override
    public Object getResult(@NonNull Class aClass) throws Throwable {
        return null;
    }
}
